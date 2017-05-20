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
};

module.exports.isUniqueIDProfile = function(uniqueID, profile_id, processResult) {
	dao.executeQuery('SELECT count(user_id) as count from account_details, profile_details where account = user_id and unique_id = ? and profile_id = ?', [uniqueID, profile_id], function(combination_result) {
		if (combination_result[0].count === 1) {
			processResult(true);
		} else {
			processResult(false);
		}
	})
};

module.exports.getIDFromUniqueID = function(uniqueID, processResult) {
	dao.executeQuery('select profile_id, user_id from profile_details, account_details where account = user_id and unique_id = ?', [uniqueID], function(profile_results) {
		processResult(profile_results[0].user_id, profile_results[0].profile_id);
	});
};

module.exports.updateProfile = function(params, res) {
	isUniqueIDProfile(params.uniqueID, params.profile_id, function(isValid) {
		if (isValid) {
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
		} else {
			res.send({
				'status_code': 403,
				'message': 'Forbidden'
			})
		}
	})
};

// Follow profile
module.exports.followProfile = function(profile, following, res) {
	dao.insertData('follow_details', {
		'profile': profile,
		'following': following
	}, function(follow_result) {
		if (follow_result.affectedRows === 1) {
			res.send({
				"status_code": 200,
				"message": "Following"
			});
		} else {
			res.send({
				"status_code": 400,
				"message": "Internal Error"
			});
		}
	})
};


// Is profile public
module.exports.isPublicProfile = function(following, processResult) {
	dao.fetchData('public', 'preference_details', {
		'profile': following
	}, function(preference_result) {
		processResult(preference_result[0].public === 1)
	})
};

// Get id
module.exports.getIDFromEmail = function(friend_email, processResult) {
	dao.executeQuery('select profile_id, user_id from profile_details, account_details where account = user_id and email = ?', [friend_email], function(email_results) {
		processResult(email_results[0].user_id, email_results[0].profile_id);
	});
};

// Add friend
module.exports.addFriend = function(profile, friend, res) {
	dao.insertData('connection_details', {
		'profile': profile,
		'friend': friend,
		'pending': 1
	}, function(addfriend_result) {
		if (addfriend_result.affectedRows === 1) {
			res.send({
				"status_code": 200,
				"message": "Friend request sent."
			});
		} else {
			res.send({
				"status_code": 400,
				"message": "Internal Error"
			});
		}
	})
};



//Update Settings
module.exports.updateSettings = function(params, res) {
	updateParams = {}
	if (exists(params.is_public)) {
		updateParams.public = params.is_public;
	}
	if (exists(params.do_notify)) {
		updateParams.notification = params.do_notify;
	}
	queryParams = {}
	if (exists(params.profile_id)) {
		queryParams.profile = params.profile_id
	}

	dao.updateData('preference_details', updateParams, queryParams, function(settings_result) {
		if (settings_result.affectedRows === 1) {
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
	});
};

//Get profile which would be notified
module.exports.fetchReceivers = function(profile_id, processResult) {
	dao.executeQuery('select friend as receiver, screen_name as name from connection_details, profile_details where profile = profile_id and profile = ? and pending = 0 union select profile as receiver, screen_name as name from connection_details, profile_details where friend = profile_id and friend = ? and pending = 0 union select profile as receiver, screen_name as name from follow_details, profile_details where following = profile_id and following = ?', [profile_id, profile_id, profile_id], function(receiver_results) {
		processResult(receiver_results);
	});
};
