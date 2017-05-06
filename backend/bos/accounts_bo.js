var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");


module.exports.register = function(email, password, firstname, lastname, screenname, res) {
	var salt = bcrypt.genSaltSync(10);

	dao.insertData("account_details", {
		"email": email,
		"secret": bcrypt.hashSync(password, salt),
		"salt": salt
	}, function(account_result) {
		if (account_result.affectedRows === 1) {
			dao.insertData("profile_details", {
				'account': account_result.insertId,
				'f_name': firstname,
				'l_name': lastname,
				'screen_name': screenname,
				'timestamp': getTimestamp()
			}, function(profile_result) {
				if (profile_result.affectedRows === 1) {
					res.send({
						'status_code': 200,
						'message': 'User ' + firstname + ' created successfully !'
					});
				} else {
					res.send({
						'status_code': 400,
						'message': 'Bad Request'
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

module.exports.checkEmailAvailability = function(email, res) {
	dao.fetchData('COUNT(email) as count', 'account_details', {
		'email': email
	}, function(result) {
		if (result[0].count === 0) {
			res.send({
				"available": true
			});
		} else {
			res.send({
				"available": false
			});
		}
	});
};

module.exports.handleForgotRequest = function(email, res) {
	email_validator = new RegExp("^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,24})$");
	if (email.match(email_validator) !== null) {
		dao.fetchData("count(user_id) as matches", "user_account", {
			"email": email
		}, function(rows) {
			if (Number(rows[0].matches) > 0) {
				// TODO: Send an email to user -- Not going to implement
			} else {
				error_messages.push("Email ID not found in our records.");
				status_code = 400;
			}
		});
	} else {
		error_messages.push("Not valid Email ID");
		status_code = 400;
	}
};
