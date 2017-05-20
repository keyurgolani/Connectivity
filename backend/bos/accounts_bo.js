/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");


module.exports.register = function(email, password, fullname, screenname, res) {
	var salt = bcrypt.genSaltSync(10);
	var uniqueID = getRandom(25);
	var verificationCode = getRandom(4, 3);

	dao.insertData("account_details", {
		"email": email,
		"secret": bcrypt.hashSync(password, salt),
		"salt": salt,
		"verification_code": verificationCode,
		"unique_id": uniqueID
	}, function(account_result) {
		if (account_result.affectedRows === 1) {
			async.parallel([
				function(callback) {
					sendEmail(email, "ConnActivity - Account Verification", verificationCode, function(email_result) {
						callback(null, email_result);
					});
				},
				function(callback) {
					dao.insertData("profile_details", {
						'account': account_result.insertId,
						'fullname': fullname,
						'screen_name': screenname,
						'timestamp': getTimestamp()
					}, function(profile_result) {
						if (profile_result.affectedRows === 1) {
							dao.insertData('preference_details', {
								'profile': profile_result.insertId,
								'notification': 1,
								'notification_method': 1,
								'public': 1,
								'timestamp': getTimestamp()
							}, function(preferences_result) {
								callback(null, true);
							})
						} else {
							callback(true, null)
						}
					});
				}
			], function(error, results) {
				if (error) {
					res.send({
						'status_code': 400,
						'message': 'Bad Request'
					});
				} else {
					res.send({
						'status_code': 200,
						'message': 'User ' + fullname + ' created successfully !'
					});
				}
			});
		} else {
			res.send({
				'status_code': 400,
				'message': 'Bad Request'
			});
		}
	});
};

module.exports.isUniqueIDValid = function(uniqueID, processResult) {
	dao.fetchData('count(user_id) as count', 'account_details', {
		'unique_id': uniqueID
	}, function(combination_result) {
		if (combination_result[0].count === 1) {
			processResult(true);
		} else {
			processResult(false);
		}
	});
};

module.exports.getUserIDFromUniqueID = function(uniqueID, processResult) {
	dao.fetchData('user_id', 'account_details', {
		'unique_id': uniqueID
	}, function(user_results) {
		processResult(user_results[0].user_id);
	});
};

module.exports.isUniqueIDAccount = function(uniqueID, user_id, processResult) {
	dao.executeQuery('SELECT count(user_id) as count from account_details where unique_id = ? and user_id = ?', [uniqueID, user_id], function(combination_result) {
		if (combination_result[0].count === 1) {
			processResult(true);
		} else {
			processResult(false);
		}
	})
};

module.exports.signin = function(email, password, req, res) {
	var uniqueID = getRandom(25);
	dao.fetchData('user_id, secret, salt', 'account_details', {
		'email': email
	}, function(credentials_details) {
		if (bcrypt.hashSync(password, credentials_details[0].salt) === credentials_details[0].secret) {
			dao.updateData('account_details', {
				'unique_id': uniqueID
			}, {
				'user_id': credentials_details[0].user_id
			}, function(unique_id_result) {
				dao.fetchData('verification_code', 'account_details', {
					'user_id': credentials_details[0].user_id
				}, function(verification_result) {
					if (exists(verification_result[0].verification_code)) {
						res.send({
							'status_code': 301,
							'message': 'Verify'
						});
					} else {
						res.send({
							'status_code': 200,
							'message': {
								'user_id': credentials_details[0].user_id,
								'unique_id': uniqueID
							}
						});
					}
				})
			})
		} else {
			res.send({
				'status_code': 401,
				'message': 'Invalid Credentials'
			});
		}
	});
};

module.exports.verifyAccount = function(email, code, res) {
	dao.fetchData('verification_code, unique_id', 'account_details', {
		'email': email
	}, function(verification_result) {
		if (verification_result[0].verification_code == code) {
			dao.updateData('account_details', {
				'verification_code': null
			}, {
				'email': email
			}, function(reset_verification_result) {
				if (reset_verification_result.affectedRows === 1) {
					res.send({
						'status_code': 200,
						'message': {
							'unique_id': verification_result[0].unique_id
						}
					});
				} else {
					res.send({
						'status_code': 500,
						'message': 'Internal Error'
					});
				}
			})
		} else {
			res.send({
				'status_code': 401,
				'message': 'Invalid Code'
			});
		}
	});
};

module.exports.checkEmailAvailability = function(email, res) {
	dao.fetchData('COUNT(email) as count', 'account_details', {
		'email': email
	}, function(result) {
		if (result[0].count === 0) {
			res.send({
				"status_code": 200,
				"message": "Available"
			});
		} else {
			res.send({
				"status_code": 404,
				"message": "Unavailable"
			});
		}
	});
};

module.exports.handleForgotRequest = function(email, res) {
	if (email.match(email_validator) !== null) {
		dao.fetchData("count(user_id) as matches", "account_details", {
			"email": email
		}, function(rows) {
			if (Number(rows[0].matches) > 0) {
				// TODO: Generate a reset code, store it to the DB and send that in email.
				let resetCode = getRandom(4, 3);
				async.parallel([
					function(callback) {
						sendEmail(email, 'ConnActivity - Password Reset', resetCode, function(result) {
							// TODO: Log the results properly to logger rather than to console directly
							callback(null, result);
						})
					},
					function(callback) {
						dao.updateData('account_details', {
							'verification_code': resetCode
						}, {
							'email': email
						}, function(result) {
							callback(null, result);
						})
					}
				], function(error, results) {
					res.send({
						'status_code': 200,
						'message': 'Reset Link Sent'
					});
				})
			} else {
				res.send({
					'status_code': 404,
					'message': 'Email not found'
				});
			}
		});
	} else {
		res.send({
			'status_code': 400,
			'message': 'Bad Email ID'
		});
	}
};
