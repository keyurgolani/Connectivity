/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");


module.exports.fetchProfile = function(params, res) {
	var queryParams = {};
	if (exists(params.profile_id)) {
		queryParams.profile_id = params.profile_id;
	}
	if (exists(params.account)) {
		queryParams.account = params.account;
	}
	dao.fetchData('*', 'profile_details', queryParams, function(profile_result) {
		if (profile_result.length > 0) {
			res.send({
				"status_code": 200,
				"message": profile_result
			});
		} else {
			res.send({
				"status_code": 500,
				"message": "Internal Error"
			});
		}
	})
}


module.exports.updateProfile = function(params, res) {
	var updateParams = {};
	if (exists(params.f_name)) {
		updateParams.f_name = params.f_name;
	}
	if (exists(params.l_name)) {
		updateParams.l_name = params.l_name;
	}
	if (exists(params.profile_pic)) {
		updateParams.profile_pic = params.profile_pic;
	}
	if (exists(params.location)) {
		updateParams.location = params.location;
	}
	if (exists(params.profession)) {
		updateParams.profession = params.profession;
	}
	if (exists(params.about_me)) {
		updateParams.about_me = params.about_me;
	}
	if (exists(params.screen_name)) {
		updateParams.screen_name = params.screen_name;
	}
	updateParams.timestamp = getTimestamp();
	var queryParams = {};
	if (exists(params.profile_id)) {
		queryParams.profile_id = params.profile_id;
	}
	if (exists(params.account)) {
		queryParams.account = params.account;
	}
	dao.updateData('profile_details', updateParams, queryParams, function(profile_result) {
		if (profile_result.affectedRows === 1) {
			res.send({
				"status_code": 200,
				"message": "Update Success"
			});
		} else {
			res.send({
				"status_code": 500,
				"message": "Internal Error"
			});
		}
	})
};
