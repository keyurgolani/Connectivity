var winston = require('winston');

var logger = new winston.Logger({
	transports: [
		new winston.transports.File({
			level: 'silly',
			colorize: true,
			timestamp: true,
			filename: './logs/logging.log',
			maxsize: 100000,
			maxFiles: 1000,
			logstash: true,
			tailable: true,
			zippedArchive: false,
			json: true,
			stringify: false,
			prettyPrint: true,
			depth: 5,
			humanReadableUnhandledException: true,
			showLevel: true,
			stderrLevels: ['error', 'debug']
		}),
		new winston.transports.Console({
			level: 'debug',
			handleExceptions: true,
			json: true,
			timestamp: true,
			prettyPrint: true,
			colorize: true
		})
	],
	exitOnError: false
});

// Generic Logging - Specific to method and operation
module.exports.genericLog = function(genericLog, user_id) {
	var logString = "Generic: " + genericLog;
	if (user_id !== null && user_id !== undefined && user_id !== 0) {
		logString = logString + " - " + "UserID: " + user_id;
	}
	logger.log("info", logString);
};

// Database interaction logging.
module.exports.logQuery = function(sql) {
	logger.log("debug", "Query: " + sql);
};

// Method Entry Logging
module.exports.logMethodEntry = function(file, method) {
	logger.log("debug", "MethodEntered: " + " - " + "File: " + file + " - " + "Method: " + method);
};

module.exports.stream = {
	write: function(message, encoding) {
		logger.info(message);
	}
};
