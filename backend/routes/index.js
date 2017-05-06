/*jshint esversion: 6 */

var express = require('express');
var router = express.Router();
var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");

// var common_bo = require('../bos/common_bo');
// var homepage_bo = require('../bos/homepage_bo');
var accounts_bo = require('../bos/accounts_bo');
// var sell_bo = require('../bos/sell_bo');
// var cart_bo = require('../bos/cart_bo');
// var profile_bo = require('../bos/profile_bo');
// var item_bo = require('../bos/item_bo');

/* GET home page. */
router.get('/', function(req, res, next) {
	res.send({});
});

router.post('/register', function(req, res, next) {
	let password_validator = RegExp(/^[a-z0-9_-]{6,18}$/);
	if (exists(req.body.password) && req.body.password.match(password_validator) !== null) {
		accounts_bo.register(req.body.email, req.body.password, req.body.fname, req.body.lname, req.body.screenname, res);
	} else {
		res.send({
			'status_code': 400,
			'message': 'Bad Password'
		});
	}
});

module.exports = router;
