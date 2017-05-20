/*jshint esversion: 6 */

var express = require('express');
var router = express.Router();
var dao = require('../utils/dao');
var bcrypt = require("bcrypt");
var logger = require("../utils/logger");
var properties = require('properties-reader')('properties.properties');
var ObjectID = require('mongodb').ObjectID


var accounts_bo = require('../bos/accounts_bo');
var profile_bo = require('../bos/profile_bo');
var message_bo = require('../bos/message_bo');
var timeline_bo = require('../bos/timeline_bo');
var notification_bo = require('../bos/notification_bo');

// Dummy Homepage GET route
router.get('/', function(req, res, next) {
	res.send({});
});

// Version Checking
router.post('/version', function(req, res, next) {
	res.send({
		'version': properties.get('version.oldest_compatible')
	});
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

router.post('/verifyAccount', function(req, res, next) {
	if (exists(req.body.code) &&
		exists(req.body.email) &&
		req.body.email.match(email_validator) !== null) {
		accounts_bo.verifyAccount(req.body.email, req.body.code, res);
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


// Account Related Routes
router.post('/updateProfile', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				if ((exists(req.body.profile_id) && isNum(req.body.profile_id)) ||
					(exists(req.body.account_id) && isNum(req.body.account_id))) {
					profile_bo.updateProfile({
						'profile_id': req.body.profile_id,
						'account': req.body.account_id,
						'f_name': req.body.f_name,
						'l_name': req.body.l_name,
						'profile_pic': req.body.profile_pic,
						'location': req.body.location,
						'profession': req.body.profession,
						'about_me': req.body.about_me,
						'screen_name': req.body.screen_name,
						'uniqueID': req.body.unique_id
					}, res);
				} else {
					res.send({
						'status_code': 400,
						'message': 'Bad Request'
					});
				}
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				})
			}
		})
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		})
	}
});

router.post('/fetchProfile', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				if ((exists(req.body.profile_id) && isNum(req.body.profile_id)) ||
					(exists(req.body.account_id) && isNum(req.body.account_id))) {
					profile_bo.fetchProfile({
						'profile_id': req.body.profile_id,
						'account': req.body.account_id
					}, res);
				} else {
					profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
						profile_bo.fetchProfile({
							'profile_id': profile_id
						}, res);
					});
				}
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				})
			}
		})
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		})
	}
});

// Message Related Routes
router.post('/messages', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
					message_bo.getMessages(profile_id, res);
				})
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				})
			}
		})
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		})
	}
});

router.post('/sendMessage', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
					if (exists(req.body.to)) {
						message_bo.postMessage(profile_id, user_id, req.body.to, req.body.subject, req.body.message, res);
					} else {
						res.send({
							'status_code': 400,
							'message': 'Bad Request'
						})
					}
				})
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				})
			}
		})
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		})
	}
});

// Timeline Related Routes
router.post('/timeline', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
					if (exists(req.body.profile)) {
						// Will check if the timeline requested is user's own timeline inside the function
						// Will return results accordingly.
						timeline_bo.fetchFriendTimeline(profile_id, req.body.profile, res)
					} else {
						timeline_bo.fetchOwnTimeline(profile_id, res)
					}
				})
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				})
			}
		})
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		})
	}
});

router.post('/post', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
					timeline_bo.addPost(profile_id, req.body.post, exists(req.body.photo) ? req.body.photo : 0, res);
				});
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				});
			}
		});
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		});
	}
});

router.post('/savePhoto', function(req, res, next) {
	console.log('req.body.unique_id', req.body.unique_id);
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				console.log('CONDITION PASSED');
				req.db.get('photos')
					.insert({
						'photo': req.body.photo
					})
					.then(function(photo_result) {

						console.log(photo_result._id);
						res.send({
							'status_code': 200,
							'message': photo_result._id
						});
					}, function(error) {
						console.log(error);
						throw error;
					});
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				});
			}
		});
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		});
	}
});

router.post('/getPhoto', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				console.log(req.body.photo_id);
				req.db.get('photos').find({
						'_id': new ObjectID(req.body.photo_id)
					})
					.then(function(photo_result) {
						res.send({
							'status_code': 200,
							'message': photo_result[0].photo
						});
					}, function(error) {
						throw error;
					});
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				});
			}
		});
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		});
	}
});

// Follow profile
router.post('/followProfile', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				if (exists(req.body.following)) {
					//TODO: Create Method - Check code
					profile_bo.isPublicProfile(req.body.following, function(isPublic) {
						if (isPublic) {
							profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
								profile_bo.followProfile(profile_id, req.body.following, res);
							});
						} else {
							res.send({
								'status_code': 403,
								'message': 'Forbidden'
							});
						}
					})
				} else {
					res.send({
						'status_code': 400,
						'message': 'Invalid Request'
					});
				}
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				});
			}
		});
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		});
	}
});


// Add Friend
router.post('/addFriend', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				if (!exists(req.body.friend) && !exists(req.body.friend_email)) {
					res.send({
						'status_code': 400,
						'message': 'Invalid Request'
					});
				} else {
					if (exists(req.body.friend_email)) {
						// TODO: Create Method- check code
						profile_bo.getIDFromEmail(req.body.friend_email, function(friend_user_id, friend_profile_id) {
							if (friend_profile_id !== 0 && friend_user_id !== 0) {
								profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
									// TODO: Create Method - check code
									profile_bo.addFriend(profile_id, friend_profile_id, res);
								});
							} else {
								// Perform Sending of Email With Proper format and data.
								// sendEmail()
							}
						})
					} else {
						profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
							profile_bo.addFriend(profile_id, req.body.friend, res);
						});
					}
				}
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				});
			}
		});
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		});
	}
});

router.post('/getNotifications', function(req, res, next) {
	if (exists(req.body.unique_id)) {
		accounts_bo.isUniqueIDValid(req.body.unique_id, function(isValid) {
			if (isValid) {
				profile_bo.getIDFromUniqueID(req.body.unique_id, function(user_id, profile_id) {
					notification_bo.fetchNotifications(profile_id, res);
				});
			} else {
				res.send({
					'status_code': 403,
					'message': 'Forbidden'
				});
			}
		});
	} else {
		res.send({
			'status_code': 403,
			'message': 'Forbidden'
		});
	}
});

module.exports = router;
