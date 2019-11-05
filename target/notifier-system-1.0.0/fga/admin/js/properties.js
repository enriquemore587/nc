$(document).ready(function(){

	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=adminControl&op=getProperties",
		  type: "post",
		  success: function(json) {
			  onSuccessProperties(json);
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
			  onErrorProperties(xhr, ajaxOptions, thrownError);
		  }
	});

	function onSuccessProperties(json_obj) {

		var editableProperties="<tbody>";
		var j = 0;

		for (var i in json_obj.editableProperties) {
			editableProperties += "<tr><td>" + i + "</td><td><input type=\"text\" id=\"data_" + j + "\" value=\"" + json_obj.editableProperties[i] + "\"/></td><td>" +
					'<button style="margin-right: 10px;" class="btn btn-info" type="button" name="update" onclick="doUpdate(\'' + i + '\', $(\'#data_' + j + '\').val())">' +
                                    '<i class="icon-refresh icon-white"></i> Update' +
                    '</button>'+
                    '<button class="btn btn-danger" type="button" name="delete" onclick="doDelete(\'' + i + '\')">' +
                                    '<i class="icon-trash icon-white"></i> Delete' +
                    '</button>';
	        		/*"<input type=\"button\" name=\"update\" value=\"Update\" onclick=\"doUpdate('" + i + "',$('#data_" + j + "').val())\"/>" +
					"<input type=\"button\" name=\"delete\" value=\"Delete\" onclick=\"doDelete('" + i + "')\"/></td></tr>";*/
			j++;
	    }

		editableProperties += "</tbody>";

		$('#editable_properties').append(editableProperties);

		var allProperties="<tbody>";

		for (var i in json_obj.allProperties) {
			allProperties += "<tr><td>" + i + "</td><td>" + json_obj.allProperties[i] + "</td></tr>";
	    }

		allProperties += "</tbody>";

		$('#all_properties').append(allProperties);
	}

	function onErrorProperties(xhr, ajaxOptions, thrownError) {

	}

	$("#create_property").click(function() {
		$.ajax({
			  url: document.location.origin + "/fga/admin",
			  data: "service=adminControl&op=adminProperties&action=save&key=" + $("#property_key").val() + "&data=" + $("#property_data").val(),
			  type: "post",
			  success: function(json) {
				  onSuccessCreateProperty(json);
			  },
			  error: function(xhr, ajaxOptions, thrownError) {
				  onErrorCreateProperty(xhr, ajaxOptions, thrownError);
			  }
		});

		function onSuccessCreateProperty(json) {
			location.reload();
		}

		function onErrorCreateProperty(xhr, ajaxOptions, thrownError) {

		}
	});
});

function doUpdate(key, data) {
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=adminControl&op=adminProperties&action=save&key=" + key + "&data=" + data,
		  type: "post",
		  success: function(json) {
			  location.reload();
		  },
		  error: function(xhr, ajaxOptions, thrownError) {

		  }
	});
}

function doDelete(key) {
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=adminControl&op=adminProperties&action=delete&key=" + key,
		  type: "post",
		  success: function(json) {
			  location.reload();
		  },
		  error: function(xhr, ajaxOptions, thrownError) {

		  }
	});
}