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
		$("#propertiesTbody").append('<tr>' +
				'<td>'+ data.name +'</td>' +
				'<td><input id="' + data.name + '_update" class="_property_update" type="checkbox" checked/></td>' +
				'<td><input id="' + data.name + '_remove" class="_property_remove" type="checkbox" name="_property_remove"/></td>' +
				'<td><input id="' + data.name + '_index" class="_property_index" type="checkbox" name="_' + data.name + '_name_index" checked/></td>' +
				'<td><select id="' + data.name + '_type" class="_property_type" name="_' + data.name + '_name_type"></select></td>'+
				'<td><input id="' + data.name + '_value" class="_property_value" type="text" name="_' + data.name + '_name_value" value="" /></td>' +
            '</tr>');

		if (data.name == "allowed_methods" && json.kind == "OAuthDAO") {
			$('#' + data.name + '_type').append('<option value="java.util.List" selected="selected">list</option>');
		} else if (data.name == "not_allowed_methods" && json.kind == "OAuthDAO") {
          	$('#' + data.name + '_type').append('<option value="com.google.appengine.api.datastore.Text" selected="selected">text</option>');
        } else if (data.name == "date" && json.kind == "OAuthDAO") {
			$('#' + data.name + '_type').append('<option value="java.lang.Long" selected="selected">long</option>');
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

function onUpdateAllPropertiesChanged() {
	var checked = $("#updateAllProperties").is(":checked");
	$("._property_update").attr("checked", checked).trigger("change");
}

function onRemoveAllPropertiesChanged() {
	var checked = $("#removeAllProperties").is(":checked");
	$("._property_remove").attr("checked", checked).trigger("change");
}

function onIndexAllPropertiesChanged() {
	var checked = $("#indexAllProperties").is(":checked");
	$("._property_index").attr("checked", checked).trigger("change");
}

function onAddPropertyClicked() {
	var name = $("#propertyName").val();
	
	$("#propertiesTbody").append('<tr>' +
			'<td><input type="hidden" name="_new_properties" value="' + name + '"/>' + name + '</td>' +
			'<td><input id="' + name + '_update" class="_property_update" type="checkbox" checked/></td>' +
			'<td><input id="' + name + '_remove" class="_property_remove" type="checkbox" name="_property_remove"/></td>' +
			'<td><input id="' + name + '_index" class="_property_index" type="checkbox" name="_' + name + '_name_index" checked/></td>' +
			'<td><select id="' + name + '_type" class="_property_type" name="_' + name + '_name_type"></select></td>'+
			'<td><input id="' + name + '_value" class="_property_value" type="text" name="_' + name + '_name_value"/></td>' +
        '</tr>');
	
	$(typelist).each(function(i, type){
		if (type.value == "string") {
			$('#'+ name +'_type').append('<option value="' + type.name + '" selected="selected">' + type.value + '</option>');
		} else {
			$('#'+ name +'_type').append('<option value="' + type.name + '">' + type.value + '</option>');
		}
	});
}

function onPropertyRemoveChanged() {
	var element = $(this);
	var checked = element.is(":checked");
	var property = element.val();

	$("#" + property).prop("disabled", checked);
	$("#" + property + "_update").prop("disabled", checked);
	$("#" + property + "_type").prop("disabled", checked);
	$("#" + property + "_index").prop("disabled", checked);
	
	updatePropertyValueDisabled(property);

	if(property == "not_allowed_methods"){
		$("#not_allowed_methods").attr("disabled", "false");
	}
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
	$("#updateAllProperties").change(onUpdateAllPropertiesChanged);
	$("#removeAllProperties").change(onRemoveAllPropertiesChanged);
	$("#indexAllProperties").change(onIndexAllPropertiesChanged);
	$("#addProperty").click(onAddPropertyClicked);

	$(document).on("change", "._property_remove", onPropertyRemoveChanged);
	$(document).on("change", "._property_type", onPropertyTypeChanged);

	$("._property_remove").each(onPropertyRemoveChanged);
	$("._property_type").each(onPropertyTypeChanged);
});

function checkRequiredFields() {
	$('#form').submit(function (e) {

		var show_alert = false;
		var text = "";
		
		if ($('#key').val().trim().length <= 0) {
			  text += "ID / Key can not be empty\n";
			  show_alert = true;
		}
		
		if (show_alert) {
			alert(text);
			return false
		}
	});
}