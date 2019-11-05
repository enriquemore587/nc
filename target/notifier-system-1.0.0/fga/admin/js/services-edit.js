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

    // Call to servet to get WS information
	$.ajax({
          url: document.location.origin + "/fga/admin/annotation-scanner",
          data: "",
          type: "post",
          success: function(json) {
              drawTable(json, $("#not_allowed_services_value").val());
          },
          error: function(xhr, ajaxOptions, thrownError) {
            alert("ERROR getting WS from application");
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

	if (property == "not_allowed_services") {
		$("#not_allowed_services").attr("disabled", "false");
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

var auxJsonMethods = {};
var auxArrayMethods = {};

function drawTable ( jsonMethods, jsonNotAllowedMethods ) {
	var tableSequence = "";

	if (jsonNotAllowedMethods != undefined &&
	        jsonNotAllowedMethods != null &&
	        jsonNotAllowedMethods.length > 0) {
	    auxJsonMethods = matchJSON (jsonMethods, JSON.parse(jsonNotAllowedMethods));
	} else {
	    auxJsonMethods = jsonMethods;
	}

	$("#not_allowed_services_value").attr("readonly", "readonly");

	$(auxJsonMethods).each (function ( i, data ) {
		auxArrayMethods[data["className"]] = {};
		auxArrayMethods[data["className"]]["path"] = data["path"];
		auxArrayMethods[data["className"]]["regexp"] = data["regexp"];
		auxArrayMethods[data["className"]]["className"] = data["className"];

		tableSequence = tableSequence + "<table id='" + data["className"] + "' class='table table-striped table-bordered bootstrap-datatable datatable dataTable'>"
									+ "										<tr>"
									+ "											<th><label><b>Path</b></label></th>"
									+ "											<td colspan='4'>" + data["path"] + "</td>"
				    			    + "                                     </tr>"
				    			    + "										<tr>"
									+ "											<th><label><b>Class Name</b></label></th>"
									+ "											<td colspan='4'>" + data["className"] + "</td>"
				    			    + "                                     </tr>";

		if ( data ["get"] != undefined && data [ "get" ].length > 0 ) {
			auxArrayMethods[data["className"]]["get"] = {};
			auxArrayMethods[data["className"]]["get"]["notAllowedMethods"] = 0;
			tableSequence = tableSequence + drawMethodsTable ( "get", data [ "get" ], auxArrayMethods[data["className"]]["get"], data["className"] );
		}
		if ( data ["post"] != undefined && data [ "post" ].length > 0) {
			auxArrayMethods[data["className"]]["post"] = {};
			auxArrayMethods[data["className"]]["post"]["notAllowedMethods"] = 0;
			tableSequence = tableSequence + drawMethodsTable ( "post", data [ "post" ], auxArrayMethods[data["className"]]["post"], data["className"] );
		}
		if ( data ["put"] != undefined && data [ "put" ].length > 0) {
			auxArrayMethods[data["className"]]["put"] = {};
			auxArrayMethods[data["className"]]["put"]["notAllowedMethods"] = 0;
			tableSequence = tableSequence + drawMethodsTable ( "put", data [ "put" ], auxArrayMethods[data["className"]]["put"], data["className"] );
		}
		if ( data ["delete"] != undefined && data [ "delete" ].length > 0) {
			auxArrayMethods[data["className"]]["delete"] = {};
			auxArrayMethods[data["className"]]["delete"]["notAllowedMethods"] = 0;
			tableSequence = tableSequence + drawMethodsTable ( "delete", data [ "delete" ], auxArrayMethods[data["className"]]["delete"], data["className"] );
		}
		tableSequence = tableSequence + "</table>";
	});

	$("#_table_notAllowedServices").append(tableSequence);
	$("#_table_notAllowedServices input[type=checkbox]").click(function() {
		var auxBranch = this.id.split("_");
		var auxNotAllowedMethods = auxArrayMethods[auxBranch[0]][auxBranch[1]]["notAllowedMethods"];
		if (this.checked) {
			auxArrayMethods[auxBranch[0]][auxBranch[1]][auxBranch[2]]["allowed"] = true;
			auxArrayMethods[auxBranch[0]][auxBranch[1]]["notAllowedMethods"] = auxNotAllowedMethods * 1 - 1;
		} else {
			auxArrayMethods[auxBranch[0]][auxBranch[1]][auxBranch[2]]["allowed"] = false;
			auxArrayMethods[auxBranch[0]][auxBranch[1]]["notAllowedMethods"] = auxNotAllowedMethods * 1 + 1;
		}

		var auxValue = JSON.stringify(generateNotAlowedMethodsJSON(auxJsonMethods, auxArrayMethods));
		if (auxValue != "[]") {
			$("#not_allowed_services_type").val("com\.google\.appengine\.api\.datastore\.Text");
			$("#not_allowed_services_value").val(auxValue);
			$("#not_allowed_services_value").prop("disabled", false);
		} else {
			$("#not_allowed_services_value").val("");
			$("#not_allowed_services_type").val(null);
			$("#not_allowed_services_value").prop("disabled", false);
		}
	});
}

function matchJSON (json, exceptions) {
	var matchJson = json;

	$(matchJson).each ( function ( i, data ){
		var _className = data["className"];
		$(exceptions).each(function (j, d) {
			if ( d [ "className" ] == _className ) {
				matchByType (data [ "get" ], d [ "get" ]);
				matchByType (data [ "post" ], d [ "post" ]);
				matchByType (data [ "put" ], d [ "put" ]);
				matchByType (data [ "delete" ], d [ "delete" ]);
			}
		});
	});

	return matchJson;
}

function matchByType(json, exceptions) {
	if (json != undefined && json.length > 0 && exceptions != undefined && exceptions.length > 0) {
        $(json).each( function (i, data) {
            $(exceptions).each(function (j, d) {
                if (d ["path"] != undefined) {
                    if (data["path"] == d ["path"] && data["name"] == d["name"]) {
                        data["allowed"] = d ["allowed"];
                    }
                } else {
                    if (data["name"] == d["name"]) {
                        data["allowed"] = d["allowed"];
                    }
                }
            });
        });
    }
}

function drawMethodsTable ( method_name, jsonMethods, arrayMethods, fatherName ) {
	var tableSequence = "";
	tableSequence = tableSequence	+ " <tr>"
									+ "		<th rowspan='" + (jsonMethods.length + 1) + "'>" + method_name.toUpperCase() + " methods</th>"
									+ "		<th>path</th>"
				    			    + "     <th>name</th>"
									+ " 	<th>allowed</th>"
									+ "	</tr>";

		$(jsonMethods).each(function (i, data) {
			arrayMethods[data["name"]] = {};
			arrayMethods[data["name"]]["path"] = data["path"];
			arrayMethods[data["name"]]["name"] = data["name"];
			arrayMethods[data["name"]]["allowed"] = data["allowed"];
			tableSequence = tableSequence + "<tr>"
										  +     "<td>";
			if (data["path"]) {
			    tableSequence = tableSequence + data["path"];
			}

			tableSequence = tableSequence +     "</td>"
										  +     "<td>";

			if (data["name"]) {
			    tableSequence = tableSequence + data["name"];
			}

			tableSequence = tableSequence +     "</td>"
				    			  		  +     "<td style='text-align: center;'>"
				    			  		  +         "<input id='" + fatherName + "_" + method_name + "_" + data["name"] + "' type='checkbox'";
			if (data["allowed"] && data["allowed"] == true) {
			    tableSequence = tableSequence + " checked='selected' ";
			} else {
			    arrayMethods["notAllowedMethods"] = arrayMethods["notAllowedMethods"] * 1 + 1;
			}
			tableSequence = tableSequence +         "/>"
			                              +     "</td>";
		});

	return tableSequence;
}

function generateNotAlowedMethodsJSON( jsonMethods, arrayInfoMethods ){
	var auxNotAllowedMethodsJSON = [];

	$(jsonMethods).each (function (i, data) {
		var auxNode = {};
		var isValidNode = false;

		auxNode["path"] = arrayInfoMethods[data["className"]]["path"];
		auxNode["regexp"] = arrayInfoMethods[data["className"]]["regexp"];
		auxNode["className"] = arrayInfoMethods[data["className"]]["className"];

		if (arrayInfoMethods[data["className"]]["get"] && arrayInfoMethods[data["className"]]["get"]["notAllowedMethods"] > 0) {
			isValidNode = true;

			auxNode["get"] = [];
			$( data["get"] ).each (function (j, d){
				if ( !(arrayInfoMethods[data["className"]]["get"][d["name"]]["allowed"]) ){
					var auxGetMethods = {};

					if ( d["path"] ) auxGetMethods["path"] = d["path"];
					if ( d["regexp"] ) auxGetMethods["regexp"] = d["regexp"];
					if ( d["name"] ) auxGetMethods["name"] = d["name"];
					auxGetMethods["allowed"] = false;

					auxNode["get"].push(auxGetMethods);
				}
			});
		}

		if (arrayInfoMethods[data["className"]]["post"] && arrayInfoMethods[data["className"]]["post"]["notAllowedMethods"] > 0) {
			isValidNode = true;

			auxNode["post"] = [];
			$( data["post"] ).each (function (j, d){
				if ( !(arrayInfoMethods[data["className"]]["post"][d["name"]]["allowed"]) ){
					var auxGetMethods = {};

					if ( d["path"] ) auxGetMethods["path"] = d["path"];
					if ( d["regexp"] ) auxGetMethods["regexp"] = d["regexp"];
					if ( d["name"] ) auxGetMethods["name"] = d["name"];
					auxGetMethods["allowed"] = false;

					auxNode["post"].push(auxGetMethods);
				}
			});
		}

		if (arrayInfoMethods[data["className"]]["put"] && arrayInfoMethods[data["className"]]["put"]["notAllowedMethods"] > 0) {
			isValidNode = true;

			auxNode["put"] = [];
			$( data["put"] ).each (function (j, d){
				if ( !(arrayInfoMethods[data["className"]]["put"][d["name"]]["allowed"]) ){
					var auxGetMethods = {};

					if ( d["path"] ) auxGetMethods["path"] = d["path"];
					if ( d["regexp"] ) auxGetMethods["regexp"] = d["regexp"];
					if ( d["name"] ) auxGetMethods["name"] = d["name"];
					auxGetMethods["allowed"] = false;

					auxNode["put"].push(auxGetMethods);
				}
			});
		}

		if (arrayInfoMethods[data["className"]]["delete"] && arrayInfoMethods[data["className"]]["delete"]["notAllowedMethods"] > 0) {
			isValidNode = true;

			auxNode["delete"] = [];
			$( data["delete"] ).each (function (j, d){
				if ( !(arrayInfoMethods[data["className"]]["delete"][d["name"]]["allowed"]) ){
					var auxGetMethods = {};

					if ( d["path"] ) auxGetMethods["path"] = d["path"];
					if ( d["regexp"] ) auxGetMethods["regexp"] = d["regexp"];
					if ( d["name"] ) auxGetMethods["name"] = d["name"];
					auxGetMethods["allowed"] = false;

					auxNode["delete"].push(auxGetMethods);
				}
			});
		}

		if ( isValidNode ) auxNotAllowedMethodsJSON.push(auxNode);
	});

	return auxNotAllowedMethodsJSON;
}