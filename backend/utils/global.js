/*jshint esversion: 6 */

const nodemailer = require('nodemailer');

module.exports.init = function() {
	// Initializing global validators
	email_validator = new RegExp("^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,24})$");
	password_validator = RegExp(/^[a-z0-9_-]{6,18}$/);

	// Initializing global functions
	getTimestamp = function() {
		return require('fecha').format(Date.now(), 'YYYY-MM-DD HH:mm:ss');
	};

	exists = function(obj) {
		if (typeof obj !== 'undefined') {
			if (obj !== null && obj !== undefined) {
				if (typeof obj === 'string') {
					return obj !== '';
				} else if (typeof obj === 'number') {
					return obj !== 0;
				} else if (typeof obj === 'object') {
					return JSON.stringify(obj) !== '{}';
				} else {
					return true;
				}
			}
		}
		return false;
	};

	isFunc = function(obj) {
		return exists(obj) && typeof obj === 'function';
	};

	isNum = function(obj) {
		return exists(obj) && typeof obj === 'number';
	}

	isStr = function(obj) {
		return exists(obj) && typeof obj === 'string';
	}

	isObj = function(obj) {
		return exists(obj) && typeof obj === 'object';
	}

	getRandom = function(length, type) {
		var generatedString = "";
		var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		if (isNum(type) && type === 1) {
			possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		} else if (isNum(type) && type === 2) {
			possible = "abcdefghijklmnopqrstuvwxyz";
		} else if (isNum(type) && type === 3) {
			possible = '0123456789'
		}
		for (var i = 0; i < length; i++) {
			generatedString += possible.charAt(Math.floor(Math.random() * possible.length));
		}
		return generatedString;
	};

	sendEmail = function(recepiant, subject, content, htmlContent, processResult) {
		let transporter = nodemailer.createTransport({
			service: 'gmail',
			auth: {
				user: 'cloud9.hps@gmail.com',
				pass: 'cloud123#'
			}
		});

		if (isFunc(htmlContent)) {
			processResult = htmlContent;
		}

		if (!exists(htmlContent) || isFunc(htmlContent)) {
			// TODO: Add more sophisticated template
			htmlContent = "<p>" + content + "</p>";
		}

		let mailOptions = {
			from: '"ConnActivity - No Reply" <no-reply@connactivity.com>',
			to: recepiant, // list of receivers
			subject: subject,
			text: content,
			html: htmlContent
		};

		// send mail with defined transport object
		transporter.sendMail(mailOptions, (error, result) => {
			if (error) {
				throw error;
			}
			processResult(result);
		});
	};

};
