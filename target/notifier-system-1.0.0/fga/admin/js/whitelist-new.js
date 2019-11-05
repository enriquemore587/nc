$(document).ready(function() {
	var kindSelected = $.post('kind');
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=datastore&op=new&kind=" + kindSelected,
		  type: "post",
		  success: function(json) {
			  onSuccessListDataEdit(json);
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
		  	alert("ERROR");
		  }
	});
});

(function($) {
	    $.post = function(key)   {
	        key = key.replace(/[\[]/, '\\[');
	        key = key.replace(/[\]]/, '\\]');
	        var pattern = "[\\?&]" + key + "=([^&#]*)";
	        var regex = new RegExp(pattern);
	        var url = unescape(window.location.href);
	        var results = regex.exec(url);
	        if (results === null) {
	            return null;
	        } else {
	            return results[1];
	        }
	    }
	})(jQuery);

var typelist;

function onSuccessListDataEdit(json) {
	typelist = json.typesList;
	
	$("#entityName").append(json.kind);
	$("#kind").val(json.kind);
	$("#properties").val(JSON.stringify(json.properties));
	
	$(json.properties).each(function(i, data) {
		$("#propertiesTbody").append('<tr>');
		$("#propertiesTbody").append('<td><b>'+ data.name +'</b></td>');
		$("#propertiesTbody").append('<td><select id="' + data.name + '_type" class="_property_type" name="_' + data.name + '_name_type"></select></td>');
		$("#propertiesTbody").append('<td><input id="' + data.name + '_value" class="_property_value" type="text" name="_' + data.name + '_name_value" value="" /></td>');
		$("#propertiesTbody").append('</tr>');

		if (data.name == "allowed_methods" && json.kind == "OAuthDAO") {
			$('#' + data.name + '_type').append('<option value="java.util.List" selected="selected">list</option>');
			$('#' + data.name + '_value').val('GET,POST,PUT,DELETE');
		} else if (data.name == "not_allowed_methods" && json.kind == "OAuthDAO") {
			$('#' + data.name + '_type').append('<option value="com.google.appengine.api.datastore.Text" selected="selected">text</option>');
		} else if (data.name == "date" && json.kind == "OAuthDAO") {
			$('#' + data.name + '_type').append('<option value="java.lang.Long" selected="selected">long</option>');
			$('#' + data.name + '_value').val($.now());
		} else if ((data.name == "consumer_key" || data.name == "description") && json.kind == "OAuthDAO") {
			$('#' + data.name + '_type').append('<option value="java.lang.String" selected="selected">string</option>');
		} else {
			$(json.typesList).each(function(i, type) {
				if (type.value == "string") {
					$('#' + data.name + '_type').append('<option value="' + type.name + '" selected="selected">' + type.value + '</option>');
				} else {
					$('#' + data.name + '_type').append('<option value="' + type.name + '">' + type.value + '</option>');
				}
			});
		}
	});
}

function ckeckRequiredFields() {
	$('#form').submit(function (e) {
		
		var show_alert = false;
		var text = "";
		
		if ($('#key').val().trim().length <= 0) {
			  text += "App ID can not be empty\n";
			  show_alert = true;
		}
		
		if ($('#consumer_key_value').val().trim().length <= 0) {
			  text += "Consumer Key can not be empty\n";
			  show_alert = true;
		}
		
		if (show_alert) {
			alert(text);
			return false
		}
	});
}

function onPropertyTypeChanged() {
	var element = $(this);
	var property = element.data("property");
	updatePropertyValueDisabled(property);
}

function updatePropertyValueDisabled(property) {
	var removed = $("#" + property + "_remove").is(":checked");
	var type = $("#" + property + "_type").val();

	$("#" + property).prop("disabled", removed || (type == ""));
}

$(function() {

	$(document).on("change", "._property_type", onPropertyTypeChanged);

	$("._property_type").each(onPropertyTypeChanged);
});