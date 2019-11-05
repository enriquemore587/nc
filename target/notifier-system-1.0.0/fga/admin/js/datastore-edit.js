$(document).ready(function() {
	var kindSelected = getParameterByName('kind');
	var keySelected = getParameterByName('id');
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=datastore&op=edit&kind=" + kindSelected +
		  		"&id=" + keySelected,
		  type: "post",
		  success: function(json) {
			  onSuccessListDataEdit(json);
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
		  	alert("ERROR");
		  }
	});
});

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var typelist;

var dataTypesTextArea = ["blob",
            "shortblob",
            "text"];

function isInArray(dataType) {
    return dataTypesTextArea.indexOf(dataType.toLowerCase()) > -1;
}

function onSuccessListDataEdit(json) {
	typelist = json.typesList;
	var isChecked = "checked";
	var num_entities = json.entities.length;
	
	if (num_entities > 0) {
		if (json.entities.length < 2) {
			$("#keyValue").val(json.entities[0].key);
			$("#id").val(json.entities[0].id);
		} else {
			$("#keyValue").hide();
			$("#keyContainer").append(json.entities.length + " entities selected");
		}
	}
	
	$("#entityName").append(json.kind);
	$("#kind").val(json.kind);
	$(json.entities[0].dataList).each(function(i, data) {
		
		if (data.indexed == false) {
			isChecked = "";
		}
		
		$("#propertiesTbody").append('<tr>');
		$("#propertiesTbody").append('<td>'+ data.name +'</td>');
		$("#propertiesTbody").append('<td><input id="' + data.name + '_update" class="_property_update" type="checkbox" checked/></td>');
		$("#propertiesTbody").append('<td><input id="' + data.name + '_remove" class="_property_remove" type="checkbox" name="_property_remove"/></td>');
		$("#propertiesTbody").append('<td><input id="' + data.name + '_index" class="_property_index" type="checkbox" name="_' + data.name + '_name_index" ' + isChecked + '/></td>');
		$("#propertiesTbody").append('<td><select id="' + data.name + '_type" class="_property_type" name="_' + data.name + '_name_type"></select></td>');
		
		if (isInArray(data.type)) {
			$("#propertiesTbody").append('<td><textarea id="' + data.name + '_value" class="_property_value" name="_' + data.name + '_name_value" cols="70" rows="5">' + data.value + '</textarea></td>');
		} else {
			$("#propertiesTbody").append('<td><input id="' + data.name + '_value" class="_property_value" type="text" name="_' + data.name + '_name_value" value="' + data.value + '" /></td>');
		}
		
		$("#propertiesTbody").append( '</tr>');

        var selectData = "";
		$(json.typesList).each(function(i, type) {
			if (type.value == data.type.toLowerCase()) {
				selectData += '<option value="' + type.name + '" selected="selected">' + type.value + '</option>';
			} else {
				selectData += '<option value="' + type.name + '">' + type.value + '</option>';
			}
		});
		$('#' + data.name + '_type').append(selectData);
		selectData = "";
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
	
	$(typelist).each(function(i, type) {
		if (type.value == "string") {
			$('#' + name + '_type').append('<option value="' + type.name + '" selected="selected">' + type.value + '</option>');
		} else {
			$('#' + name + '_type').append('<option value="' + type.name + '">' + type.value + '</option>');
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