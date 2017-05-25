/*jshint esversion: 6 */

var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var async = require("async");
var moment = require('moment');
var ObjectID = require('mongodb').ObjectID;


module.exports.getPhoto = function(db, photoID, processResult) {
	console.log('photoID', photoID);
	db.get('photos').findOne({
		'_id': new ObjectID(photoID)
	}).then(function(result) {
		processResult(result);
	}, function(error) {
		throw error;
	});
};

module.exports.setPhoto = function(db, photo, processResult) {
	db.get('photos').insert({
		'photo': photo
	}).then(processResult, function(error) {
		throw error;
	});
};
