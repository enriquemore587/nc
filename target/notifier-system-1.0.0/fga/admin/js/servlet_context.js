$.ajax({
	  url: document.location.origin + "/fga/admin",
	  data: "service=context",
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
				"<tr><th>Display Name</th><td>" + json_obj.name + "</td></tr>" +
				"<tr><th>Document Root</th><td>" + json_obj.realPath + "</td></tr>" +
				"<tr><th>Server Info</th><td>" + json_obj.serverInfo + "</td></tr>" +
				"<tr><th>Servlet Version</th><td>" + json_obj.majorVersion + "." + json_obj.minorVersion + "</td></tr>" +
				"</tbody>";

	$('#servletDetail').append(details);

	var init_parameters ="<tbody>";

	for (var i in json_obj.initParameters) {

		init_parameters += "<tr><td>" + i + "</td><td>" + json_obj.initParameters[i] + "</td></tr>";
    }

	init_parameters += "</tbody>";

	$('#initParameters').append(init_parameters);

	var attributes ="<tbody>";

	json_obj.attributes = sortByKey(json_obj.attributes, 'name');

	for (var i in json_obj.attributes) {

		attributes += "<tr><td><span style='font-weight:bold'> Name:</span>" + json_obj.attributes[i].name +"<br/><br/><span style='font-weight:bold'> Type:</span>" + json_obj.attributes[i].type+"<br/><br/><span style='font-weight:bold'> Value:</span>"+json_obj.attributes[i].value+"</td></tr>";
    }

	attributes += "</tbody>";

	$('#listAttributes').append(attributes);
}

function onError(xhr, ajaxOptions, thrownError) {

}

function sortByKey(array, key) {
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    });
}