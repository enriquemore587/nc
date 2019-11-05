$.ajax({
	  url: document.location.origin + "/fga/admin",
	  data: "service=system",
	  type: "post",
	  success: function(json) {
		  onSuccess(json);
	  },
	  error: function(xhr, ajaxOptions, thrownError) {
		  onError(xhr, ajaxOptions, thrownError);
	  }
});

$(function () {
    $('#sysProp').addTableFilter();
});

function onSuccess(json_obj) {
	var output="<tbody>";

	for (var i in json_obj) {
        output += "<tr><td>" + i + "</td><td>" + json_obj[i] + "<br/></td></tr>";
    }

	output += "</tbody>";

    $('#sysProp').append(output);
}

function onError(xhr, ajaxOptions, thrownError) {

}