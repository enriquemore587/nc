$.ajax({
	  url: document.location.origin + "/fga/admin",
	  data: "service=cookie",
	  type: "post",
	  success: function(json) {
		  onSuccess(json);
	  },
	  error: function(xhr, ajaxOptions, thrownError) {
		  onError(xhr, ajaxOptions, thrownError);
	  }
});

function onSuccess(json_obj) {
	var output="<tbody>";

	for (var i in json_obj) {
		var name = json_obj[i].name;
		if (name == undefined)
			name = "";

		var domain = json_obj[i].domain;
		if (domain == undefined)
			domain = "";

		var path = json_obj[i].path;
		if (path == undefined)
			path = "";

		var version = json_obj[i].version;
		if (version == undefined)
			version = "";

		var secure = json_obj[i].secure;
		if (secure == undefined)
			secure = "";

		var comment = json_obj[i].comment;
		if (comment == undefined)
			comment = "";

		var maxAge = json_obj[i].maxAge;
		if (maxAge == undefined)
			maxAge = "";

		var value = json_obj[i].value;
		if (value == undefined)
			value = "";

        output += "<tr><td>" + name + "</td>" +
        		"<td>" + domain + "</td>" +
				"<td>" + path + "</td>" +
				"<td>" + version + "</td>" +
				"<td>" + secure + "</td>" +
				"<td>" + comment + "</td>" +
				"<td>" + maxAge + "</td>" +
				"<td>" + value + "</td></tr>";
    }

	output += "</tbody>";

    $('#cookieTable').append(output);
}

function onError(xhr, ajaxOptions, thrownError) {

}