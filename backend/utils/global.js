module.exports.init = function() {
	// Initializing global validators
	email_validator = new RegExp("^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,24})$");

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
				} else {
					return JSON.stringify(obj) !== '{}';
				}
			}
		}
		return false;
	};

};
