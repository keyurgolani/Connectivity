/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");
var moment = require('moment');

module.exports.fetchNotifications = function(profile, res) {
	dao.fetchData('*', 'notification_details', {
		'profile': profile
	}, function(notification_results) {
		res.send({
			'status_code': 200,
			'message': notification_results
		})
	})
};

module.exports.notify = function(profile, text, friend) {
	dao.insertData('notification_details', {
		'profile': profile,
		'friend': friend,
		'text': text
	})
};
