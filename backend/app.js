/*jshint esversion: 6 */

var express = require('express');
var path = require('path');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var index = require('./routes/index');
var users = require('./routes/users');

var app = express();

var dao = require('./utils/dao');

var mongo = require('mongodb');
//Reference for monk usage and documentation: https://automattic.github.io/monk/
var monk = require('monk');
var properties = require('properties-reader')('properties.properties');
// MongoDB Config
var db = monk(properties.get('paths.mongoDBHosting')); // TODO: Fetch Properties like URL from Properties File on load!

db.options = {
	safe: true,
	castIds: false
};

require('./utils/global').init();

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	extended: false
}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


app.use(function(req, res, next) {
	req.db = db;
	next();
});



app.use('/', index);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
	var err = new Error('Not Found');
	err.status = 404;
	next(err);
});

// error handler
app.use(function(err, req, res, next) {
	// set locals, only providing error in development
	res.locals.message = err.message;
	res.locals.error = req.app.get('env') === 'development' ? err : {};

	// render the error page
	res.status(err.status || 500);
	res.send({
		'status_code': err.status || 500,
		'message': 'Not Found'
	});
});

module.exports = app;
