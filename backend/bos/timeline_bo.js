/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");


module.exports.fetchOwnTimeline = function(profile, res) {
	dao.executeQuery('select * from post_details, profile_details where profile = profile_id and profile in (select friend from connection_details where profile = ? and pending = 0) or profile in (select profile from connection_details where friend = ? and pending = 0) or profile in (select following from follow_details, preference_details where follow_details.profile = ? and follow_details.following = preference_details.profile and preference_details.public = 1) or profile = ? ORDER BY post_details.timestamp desc;', [profile, profile, profile, profile], function(timeline_result) {
		res.send({
			'status_code': 200,
			'message': timeline_result
		})
	});
};

module.exports.fetchFriendTimeline = function(profile, friend, res) {
	dao.executeQuery('select count(connection_id) as count from connection_details where (profile = ? and friend = ? and pending = 0) or (profile = ? and friend = ? and pending = 0)', [profile, friend, friend, profile], function(connection_result) {
		if (connection_result[0].count > 0) {
			dao.executeQuery('select * from post_details, profile_details where profile_id = profile and profile = ?', [friend], function(post_result) {
				res.send({
					'status_code': 200,
					'message': post_result
				})
			})
		} else {
			dao.executeQuery('select count(follow_id) as count from follow_details, preference_details where follow_details.following = preference_details.profile  and public = 1 and follow_details.profile = 23 and following = 21', [profile, friend], function(follow_result) {
				if (follow_result[0].count > 0) {
					dao.executeQuery('select * from post_details, profile_details where profile_id = profile and profile = ?', [friend], function(post_result) {
						res.send({
							'status_code': 200,
							'message': post_result
						})
					})
				}
			})
		}
	})
};

module.exports.addPost = function(profile, post, photo, res) {
	dao.insertData('post_details', {
		'profile': profile,
		'post': post,
		'photo': photo,
		'timestamp': getTimestamp()
	}, function(post_result) {
		res.send({
			'status_code': 200,
			'message': 'Post Added Successfully'
		})
	});
};
