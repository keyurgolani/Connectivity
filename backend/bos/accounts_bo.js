var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");


module.exports.register = function(email, password, firstname, lastname, screenname, res) {
	var status_code = 200;
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
	dao.executeQuery("SELECT user_id, secret, salt FROM account_details WHERE email = ?", [email], function(credentials_details) {
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
