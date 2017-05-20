/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");
var moment = require('moment');
var ObjectID = require('mongodb').ObjectID;


module.exports.getPhoto = function(db, photoID, processResult) {
	db.get('photos').find({
		'_id': new ObjectID(photoID)
	}).then(processResult, function(error) {
		throw error;
	});
};
