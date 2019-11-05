$.ajax({
	  url: document.location.origin + "/fga/admin",
	  data: "service=user",
	  type: "post",
	  success: function(json) {
		  onSuccess(json);
	  },
	  error: function(xhr, ajaxOptions, thrownError) {
		  onError(xhr, ajaxOptions, thrownError);
	  }
});

function onSuccess(json_obj) {

	var remoteUser = json_obj.remoteUser;
	if (remoteUser == undefined) {
		remoteUser = "";
	}

	var userLoggedIn = json_obj.userLoggedIn;
	if (userLoggedIn == undefined) {
		userLoggedIn = "";
	}

	var userId = json_obj.userData.userId;
	if (userId == undefined) {
		userId = "";
	}

	var email = json_obj.userData.email;
	if (email == undefined) {
		email = "";
	}

	var nickName = json_obj.nickName;
	if (nickName == undefined) {
		nickName = "";
	}

	var authDomain = json_obj.userData.authDomain;
	if (authDomain == undefined) {
		authDomain = "";
	}

	var federatedIdentity = json_obj.userData.federatedIdentity;
	if (federatedIdentity == undefined) {
		federatedIdentity = "";
	}

	var isUserAdmin = json_obj.isUserAdmin;
	if (isUserAdmin == undefined) {
		isUserAdmin = "";
	}

	var details = "<tbody>" +
				"<tr><th>Remote User</th><td>" + remoteUser + "</td></tr>" +
				"<tr><th>Logged In</th><td>" + userLoggedIn + "</td></tr>" +
				"<tr><th>User ID</th><td>" + userId + "</td></tr>" +
				"<tr><th>Email</th><td>" + email + "</td></tr>" +
				"<tr><th>Nickname</th><td>" + nickName + "</td></tr>" +
				"<tr><th>Auth Domain</th><td>" + authDomain + "</td></tr>" +
				"<tr><th>Federated Identity</th><td>" + federatedIdentity + "</td></tr>" +
				"<tr><th>Administrator</th><td>" + isUserAdmin + "</td></tr>" +
				"</tbody>";

	$('#detailUser').append(details);
}

function onError(xhr, ajaxOptions, thrownError) {

}