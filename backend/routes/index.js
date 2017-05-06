/*jshint esversion: 6 */

var express = require('express');
var router = express.Router();
var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");


var accounts_bo = require('../bos/accounts_bo');

// Dummy Homepage GET route
router.get('/', function(req, res, next) {
	res.send({});
});


// Account Related Routes
router.post('/register', function(req, res, next) {
	if (exists(req.body.password) &&
		exists(req.body.email) &&
		req.body.password.match(password_validator) !== null &&
		req.body.email.match(email_validator) !== null) {
		accounts_bo.register(req.body.email, req.body.password, req.body.fname, req.body.lname, req.body.screenname, res);
	} else {
		res.send({
			'status_code': 400,
			'message': 'Bad Details'
		});
	}
});

router.post('/signin', function(req, res, next) {
	if (exists(req.body.password) &&
		exists(req.body.email) &&
		req.body.password.match(password_validator) !== null &&
		req.body.email.match(email_validator) !== null) {
		accounts_bo.signin(req.body.email, req.body.password, req, res);
	} else {
		res.send({
			'status_code': 400,
			'message': 'Bad Credentials'
		});
	}

});

router.post('/emailAvailable', function(req, res, next) {
	accounts_bo.checkEmailAvailability(req.body.email, res);
});

router.post('/forgot', function(req, res, next) {
	if (exists(req.body.email) &&
		req.body.email.match(email_validator) !== null) {
		accounts_bo.handleForgotRequest(req.body.email, res);
	} else {
		res.send({
			'status_code': 400,
			'message': 'Bad Email'
		});
	}
});

module.exports = router;
