/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");
var moment = require('moment');
var ObjectID = require('mongodb').ObjectID;
var profile_bo = require('./profile_bo');
var notification_bo = require('./notification_bo');


module.exports.fetchOwnTimeline = function(db, profile, res) {
  dao.executeQuery('select distinct * from post_details, profile_details where profile = profile_id and (profile in (select friend from connection_details where profile = ? and pending = 0) or profile in (select profile from connection_details where friend = ? and pending = 0) or profile in (select following from follow_details, preference_details where follow_details.profile = ? and follow_details.following = preference_details.profile and preference_details.public = 1) or profile = ?) ORDER BY post_details.timestamp desc;', [profile, profile, profile, profile], function(timeline_result) {
    var photo_ids = []
    for (var i = 0; i < timeline_result.length; i++) {
      timeline_result[i].timestamp = moment(timeline_result[i].timestamp, "YYYY-MM-DDTHH:mm:ss.SSSZ").fromNow();
      if (timeline_result[i].profile_pic != 0 && photo_ids.indexOf(timeline_result[i].profile_pic) === -1) {
        photo_ids.push(new ObjectID(timeline_result[i].profile_pic))
      }
      if (timeline_result[i].photo != 0 && photo_ids.indexOf(timeline_result[i].photo) === -1) {
        photo_ids.push(new ObjectID(timeline_result[i].photo))
      }
    }
    db.get("photos").find({
      '_id': {
        $in: photo_ids
      }
    }).then(function(results) {
      photo_results = {};
      for (var j = 0; j < photo_ids.length; j++) {
        if (exists(results[j])) {
          photo_results[photo_ids[j]] = results[j].photo;
        }
      }
      for (var k = 0; k < timeline_result.length; k++) {
        if (exists(photo_results[timeline_result[k].profile_pic])) {
          timeline_result[k].profile_pic = photo_results[timeline_result[k].profile_pic];
        }
        if (exists(photo_results[timeline_result[k].photo])) {
          timeline_result[k].photo = photo_results[timeline_result[k].photo];
        }
      }
      res.send({
        'status_code': 200,
        'message': timeline_result
      })
    }, function(error) {
      throw error;
    })
  });
};

module.exports.fetchFriendTimeline = function(db, profile, friend, res) {
    if (profile === friend) {
      dao.executeQuery('select * from post_details, profile_details where profile = profile_id and profile = ? ORDER BY post_details.timestamp desc;', [profile], function(timeline_result) {

            var photo_ids = []
            for (var i = 0; i < timeline_result.length; i++) {
              timeline_result[i].timestamp = moment(timeline_result[i].timestamp, "YYYY-MM-DDTHH:mm:ss.SSSZ").fromNow();
              if (timeline_result[i].profile_pic != 0 && photo_ids.indexOf(timeline_result[i].profile_pic) === -1) {
                photo_ids.push(new ObjectID(timeline_result[i].profile_pic))
              }
              if (timeline_result[i].photo != 0 && photo_ids.indexOf(timeline_result[i].photo) === -1) {
                photo_ids.push(new ObjectID(timeline_result[i].photo))
              }

            }
            db.get("photos").find({
              '_id': {
                $in: photo_ids
              }
            }).then(function(results) {
                photo_results = {};
                for (var j = 0; j < photo_ids.length; j++) {
                  if (exists(results[j])) {
                    photo_results[photo_ids[j]] = results[j].photo;
                  }
                }
                for (var k = 0; k < timeline_result.length; k++) {
                  if (exists(photo_results[timeline_result[k].profile_pic])) {
                    timeline_result[k].profile_pic = photo_results[timeline_result[k].profile_pic];
                  }
                  if (exists(photo_results[timeline_result[k].photo])) {
                    timeline_result[k].photo = photo_results[timeline_result[k].photo];
                  }
                }

                res.send({
                  'status_code': 200,
                  'message': timeline_result
                })
              })
						});
					}
							else {
                dao.executeQuery('select count(connection_id) as count from connection_details where (profile = ? and friend = ? and pending = 0) or (profile = ? and friend = ? and pending = 0)', [profile, friend, friend, profile], function(connection_result) {
                  if (connection_result[0].count > 0) {
                    dao.executeQuery('select * from post_details, profile_details where profile_id = profile and profile = ?', [friend], function(post_result) {
                      for (var i = 0; i < post_result.length; i++) {
                        post_result[i].timestamp = moment(post_result[i].timestamp, "YYYY-MM-DDTHH:mm:ss.SSSZ").fromNow();
                      }
                      res.send({
                        'status_code': 200,
                        'message': post_result
                      })
                    })
                  } else {
                    dao.executeQuery('select count(follow_id) as count from follow_details, preference_details where follow_details.following = preference_details.profile  and public = 1 and follow_details.profile = ? and following = ?', [profile, friend], function(follow_result) {
                      if (follow_result[0].count > 0) {
                        dao.executeQuery('select * from post_details, profile_details where profile_id = profile and profile = ?', [friend], function(post_result) {
                          for (var i = 0; i < follow_result.length; i++) {
                            follow_result[i].timestamp = moment(follow_result[i].timestamp, "YYYY-MM-DDTHH:mm:ss.SSSZ").fromNow();







                          }
                          res.send({
                            'status_code': 200,
                            'message': follow_result
                          })
                        })
                      } else {
                        res.send({
                          'status_code': 403,
                          'message': 'Not Visible'
                        })
                      }
                    })
                  }
                })
              }
            };

            module.exports.addPost = function(profile, post, photo, res) {
              dao.insertData('post_details', {
                'profile': profile,
                'post': post,
                'photo': photo,
                'timestamp': getTimestamp()
              }, function(post_result) {
                profile_bo.fetchReceivers(profile, function(receiver_results) {
                  var notifyFunction = function(i) {
                    return function(callback) {
                      notification_bo.notify(receiver_results[i].profile,
                        receiver_results[i].name + "Added a new Post.\nClick here to check it out!",
                        profile)
                    }
                  }
                  var notifyFunctions = []
                  for (var i = 0; i < receiver_results.length; i++) {
                    notifyFunctions.push(notifyFunction(i))
                  }
                  async.parallel(notifyFunctions, function(error, results) {
                    // Do Nothing!
                  })
                });
                res.send({
                  'status_code': 200,
                  'message': 'Post Added Successfully'
                })
              });
            };
