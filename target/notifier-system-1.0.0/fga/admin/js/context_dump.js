$.ajax({
	  url: document.location.origin + "/fga/admin",
	  data: "service=request",
	  type: "post",
	  success: function(json) {
		  onSuccess(json);
	  },
	  error: function(xhr, ajaxOptions, thrownError) {
		  onError(xhr, ajaxOptions, thrownError);
	  }
});

function onSuccess(json_obj) {

	var details = "<tbody>" +
				"<tr><th>Query String</th><td>" + json_obj.queryString + "</td></tr>" +
				"<tr><th>Method</th><td>" + json_obj.method + "</td></tr>" +
				"<tr><th>Remote User</th><td>" + json_obj.remoteUser + "</td></tr>" +
				"<tr><th>Remote Address</th><td>" + json_obj.remoteAddr + "</td></tr>" +
				"</tbody>";

	$('#cdRequest').append(details);

	var headers ="<tbody>";

	for (var i in json_obj.headers) {

		headers += "<tr><th>" + i + "</th><td>" + json_obj.headers[i] + "</td></tr>";
    }

	headers += "</tbody>";

	$('#cdRequestHeaders').append(headers);

	var attributes ="<tbody>";

	json_obj.attributes = sortByKey(json_obj.attributes, 'name');

	for (var i in json_obj.attributes) {

		attributes += "<tr><td>" + json_obj.attributes[i].name + "</td><td>" + json_obj.attributes[i].type + "</td><td>" + json_obj.attributes[i].value + "</td></tr>";
    }

	attributes += "</tbody>";

	$('#cdRequestAttr').append(attributes);

	var parameters ="<tbody>";

	json_obj.parameters = sortByKey(json_obj.parameters, 'name');

	for (var i in json_obj.parameters) {

		parameters += "<tr><td>" + json_obj.parameters[i].name + "</td><td>" + json_obj.parameters[i].type + "</td><td>" + json_obj.parameters[i].value + "</td></tr>";
    }

	parameters += "</tbody>";

	$('#cdRequestParam').append(parameters);
}

function onError(xhr, ajaxOptions, thrownError) {

}

function sortByKey(array, key) {
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    });
}