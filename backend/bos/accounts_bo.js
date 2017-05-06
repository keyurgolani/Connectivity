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
