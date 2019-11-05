$.ajax({
	  url: document.location.origin + "/fga/admin",
	  data: "service=session",
	  type: "post",
	  success: function(json) {
		  onSuccess(json);
	  },
	  error: function(xhr, ajaxOptions, thrownError) {
		  onError(xhr, ajaxOptions, thrownError);
	  }
});

function onSuccess(json_obj) {

	var id = json_obj.id;
	if (id == undefined)
		id = "";

	var creationTime = json_obj.creationTime;
	if (creationTime == undefined)
		creationTime = "";

	var lastAccesedTime = json_obj.lastAccesedTime;
	if (lastAccesedTime == undefined)
		lastAccesedTime = "";

	var maxInactiveInterval = json_obj.maxInactiveInterval;
	if (maxInactiveInterval == undefined)
		maxInactiveInterval = "";

	var details = "<tbody>" +
					"<tr><th>Session ID</th><td>" + id + "</td></tr>" +
					"<tr><th>Creation Time</th><td>" + creationTime + "</td></tr>" +
					"<tr><th>Last Accessed Time</th><td>" + lastAccesedTime + "</td></tr>" +
					"<tr><th>Max Inactive Interval (s)</th><td>" + maxInactiveInterval + "</td></tr>" +
					"</tbody>";

	$('#sessionDetail').append(details);

	var attributes ="<tbody>";

	json_obj.attributes = sortByKey(json_obj.attributes, 'name');

	for (var i in json_obj.attributes) {

		attributes += "<tr><td>" + json_obj.attributes[i].name + "</td><td>" + json_obj.attributes[i].type + "</td><td>" + json_obj.attributes[i].value + "</td></tr>";
    }

	attributes += "</tbody>";

	$('#sessionAttr').append(attributes);
}

function onError(xhr, ajaxOptions, thrownError) {

}

function sortByKey(array, key) {
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    });
}
