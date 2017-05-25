/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");
var moment = require('moment');
var ObjectID = require('mongodb').ObjectID;
var notification_bo = require('./notification_bo');

module.exports.getMessages = function(db, profile_id, res) {
	async.parallel([
		function(callback) {
			dao.executeQuery('SELECT * FROM message_details, profile_details WHERE `profile` = ? and message_details.to = profile_details.profile_id', [profile_id], function(from_results) {
				callback(null, from_results);
			});
		},
		function(callback) {
			dao.executeQuery('SELECT * FROM message_details, profile_details WHERE `to` = ? and message_details.profile = profile_details.profile_id', [profile_id], function(to_results) {
				callback(null, to_results);
			});
		}
	], function(error, message_results) {
		if (error) {
			res.send({
				'status_code': 500,
				'message': 'Internal Error'
			})
		} else {
			var photo_ids = [];
			for (var i = 0; i < message_results[0].length; i++) {
				message_results[0][i].timestamp = moment(message_results[0][i].timestamp, "YYYY-MM-DDTHH:mm:ss.SSSZ").fromNow();
				if (message_results[0][i].profile_pic != 0 && photo_ids.indexOf(message_results[0][i].profile_pic) === -1) {
					photo_ids.push(new ObjectID(message_results[0][i].profile_pic))
				}
			}
			for (var j = 0; j < message_results[1].length; j++) {
				message_results[1][j].timestamp = moment(message_results[1][j].timestamp, "YYYY-MM-DDTHH:mm:ss.SSSZ").fromNow();
				if (message_results[1][j].profile_pic != 0 && photo_ids.indexOf(message_results[1][j].profile_pic) === -1) {
					photo_ids.push(new ObjectID(message_results[1][j].profile_pic))
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
				for (var k = 0; k < message_results[0].length; k++) {
					if (exists(photo_results[message_results[0][k].profile_pic])) {
						message_results[0][k].profile_pic = photo_results[message_results[0][k].profile_pic];
					}
				}
				for (var k = 0; k < message_results[1].length; k++) {
					if (exists(photo_results[message_results[1][k].profile_pic])) {
						message_results[1][k].profile_pic = photo_results[message_results[1][k].profile_pic];
					}
				}
				res.send({
					'status_code': 200,
					'message': {
						'from_messages': message_results[0],
						'to_messages': message_results[1]
					}
				})
			}, function(error) {
				throw error;
			})
		}
	})
};

module.exports.postMessage = function(profile, account, to, subject, message, res) {
	var queryParams = {};
	queryParams.screen_name = to;
	dao.fetchData('profile_id', 'profile_details', queryParams, function(profile_result) {
		if (profile_result.length > 0) {
			var toID = profile_result[0].profile_id;
			dao.insertData('message_details', {
				'profile': profile,
				'to': toID,
				'subject': subject,
				'message': message,
				'timestamp': getTimestamp()
			}, function(insert_result) {
				if (insert_result.affectedRows === 1) {
					dao.executeQuery('select email, screen_name from account_details, profile_details where account = user_id and screen_name = ?', [to], function(email_result) {
						async.parallel([
							function(callback) {
								sendEmail(email_result[0].email, "ConnActivity: New Message From " + email_result[0].screen_name, "Subject: " + subject + "</br>" + "Content: " + message, function(result) {
									callback(resilt);
								})
							},
							function(callback) {
								dao.fetchData("screen_name", "profile_details", {
									"profile_id": toID
								}, function(profile_result) {
									notification_bo.notify(toID, profile_result[0].screen_name + "New message.\nClick here to check it out!",
										profile);
								})
							}
						], function(error, results) {
							// Do Nothing
						})

					})
					res.send({
						'status_code': 200,
						'message': 'Message Posted'
					})
				} else {
					res.send({
						'status_code': 400,
						'message': 'Internal Error'
					})
				}
			})
		} else {
			res.send({
				"status_code": 500,
				"message": "Internal Error"
			});
		}
	})
};
