/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");

module.exports.getMessages = function(profile_id, res) {
	async.parallel([
		function(callback) {
			dao.fetchData('*', 'message_details', {
				'profile': profile_id
			}, function(from_results) {
				callback(null, from_results);
			});
		},
		function(callback) {
			dao.fetchData('*', 'message_details', {
				'to': profile_id
			}, function(from_results) {
				callback(null, from_results);
			});
		}
	], function(error, results) {
		if (error) {
			res.send({
				'status_code': 500,
				'message': 'Internal Error'
			})
		} else {
			res.send({
				'status_code': 200,
				'message': {
					'from_messages': results[0],
					'to_messages': results[1]
				}
			})
		}
	})
};

module.exports.postMessage = function(profile, account, to, subject, message, res) {
	dao.insertData('message_details', {
		'profile': profile,
		'to': to,
		'subject': subject,
		'message': message,
		'timestamp': getTimestamp()
	}, function(insert_result) {
		if (insert_result.affectedRows === 1) {
			dao.executeQuery('select email, screen_name from account_details, profile_details where account = user_id and profile_id = ?', [to], function(email_result) {
				sendEmail(email_result[0].email, "ConnActivity: New Message From " + email_result[0].screen_name,
					"Subject: " + subject + "</br>" +
					"Content: " + message,
					function(result) {
						res.send({
							'status_code': 200,
							'message': 'Message Posted'
						})
					})
			})
		} else {
			res.send({
				'status_code': 400,
				'message': 'Internal Error'
			})
		}
	})
};
