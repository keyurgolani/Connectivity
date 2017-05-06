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

	isFunc = function(obj) {
		return typeof obj === 'function';
	};

	exists = function(obj) {
		if (typeof obj !== 'undefined') {
			if (obj !== null && obj !== undefined) {
				if (typeof obj === 'string') {
					return obj !== '';
				} else if (typeof obj === 'number') {
					return obj !== 0;
				} else {
					return JSON.stringify(obj) !== '{}';
				}
			}
		}
		return false;
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
