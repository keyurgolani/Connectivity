/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");


module.exports.register = function(email, password, firstname, lastname, screenname, res) {
	var salt = bcrypt.genSaltSync(10);
	var verificationCode = getRandom(4, 3);

	dao.insertData("account_details", {
		"email": email,
		"secret": bcrypt.hashSync(password, salt),
		"salt": salt,
		"verification_code": verificationCode
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
						'f_name': firstname,
						'l_name': lastname,
						'screen_name': screenname,
						'timestamp': getTimestamp()
					}, function(profile_result) {
						if (profile_result.affectedRows === 1) {
							callback(null, true);
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
						'message': 'User ' + firstname + ' created successfully !'
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

module.exports.signin = function(email, password, req, res) {
	dao.fetchData('user_id, secret, salt', 'account_details', {
		'email': email
	}, function(credentials_details) {
		if (bcrypt.hashSync(password, credentials_details[0].salt) === credentials_details[0].secret) {
			res.send({
				'status_code': 200,
				'message': {
					'user_id': credentials_details[0].user_id
				}
			});
		} else {
			res.send({
				'status_code': 401,
				'message': 'Invalid Credentials'
			});
		}
	});
};

module.exports.verifyAccount = function(email, code, res) {
	dao.fetchData('verification_code', 'account_details', {
		'email': email
	}, function(verification_result) {
		if (verification_result[0].verification_code == code) {
			dao.updateData('account_details', {
				'verification_code': ''
			}, {
				'email': email
			}, function(reset_verification_result) {
				if (reset_verification_result.affectedRows === 1) {
					res.send({
						'status_code': 200,
						'message': 'Account Verified'
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
				let resetCode = '103EX120A4FB91'
				sendEmail(email, 'ConnActivity - Password Reset', resetCode, function(result) {
					// TODO: Log the results properly to logger rather than to console directly
					console.log(result);
					res.send({
						'status_code': 200,
						'message': 'Reset Link Sent'
					})
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
