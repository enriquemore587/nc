$(document).ready(function(){
	$("#loaderList").show();
	$("div.box-content").hide();
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=datastore&op=query",
		  type: "post",
		  success: function(json) {
			  onSuccessDatastoreList(json);
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
              alert("Error executing query!");
		  }
	});

	function onSuccessDatastoreList(json) {

		var output="";
	
		if (json.namespaces != undefined) {
			output +=   "<tr>" +
					        "<th class=\"sorting\"><label for=\"namespace\">Namespace</label></th>" +
					        "<td class=\"center\"><select id=\"namespace\" name=\"namespace\">";
	
			$(json.namespaces).each(function(i,namespace){
				output += "<option value=\"" + checkUndefined(namespace.name) + "\">" + checkUndefined(namespace.name) + "</option>";
		    });
	
			output += "</select>" + "</td>" +
			            "</tr>";
		}
		
		if (json.kinds != undefined) {
			output +=	"<tr>" +
					        "<th class='sorting'><label for=\"kind\">Kind</label></th>" +
					        "<td class='center'>" +
						        "<select id=\"kind\">";
	
			$(json.kinds).each(function(i,kind){
				output += "<option value=\"" + checkUndefined(kind.name) + "\">" + checkUndefined(kind.name) + "</option>";
			});
	
			output +=           "</select>" +
					        "</td>" +
				        "</tr>";
		}

		if (json.pageSizes != undefined) {
            output +=	"<tr>" +
                        "<th class=\"sorting\"><label for=\"pageSize\">Page size</label></th>" +
                        "<td class=\"center\"><select id=\"pageSize\" name=\"pageSize\">";

            $(json.pageSizes).each(function(i,pageSize){
                output += "<option value=\"" + checkUndefined(pageSize.name) + "\">" + checkUndefined(pageSize.value) + "</option>";
            });

            output +=   "</select></td>" +
                        "</tr>";
		}
		
		output += "<tr>" +
				    "<th class=\"sorting\"><label for=\"offset\">Offset</label></th>" +
				    "<td class=\"center\"><input id=\"offset\" name=\"offset\" value=\"0\" size=\"20\"/></td>" +
			      "</tr>" +
			      "<tr>" +
				    "<th class=\"sorting\"><label for=\"count\">Count</label></th>" +
				    "<td class=\"center\"><input id=\"count\" type=\"checkbox\" name=\"count\"/></td>" +
			      "</tr>" +
			      "<tr>" +
			        "<th class=\"sorting\"><label for=\"filter\">Filter</label></th>" +
			        "<td class=\"center\">" +
				        "<select id=\"property_name\"></select>  " +  
				        "<select id=\"filter_operator\">" +
					        "<option value=\"EQUAL\">EQUAL</option>" +
					        "<option value=\"GREATER_THAN\">GREATER_THAN</option>" +
					        "<option value=\"GREATER_THAN_OR_EQUAL\">GREATER_THAN_OR_EQUAL</option>" +
					        "<option value=\"LESS_THAN\">LESS_THAN</option>" +
					        "<option value=\"LESS_THAN_OR_EQUAL\">LESS_THAN_OR_EQUAL</option>" +
					        "<option value=\"NOT_EQUAL\">NOT_EQUAL</option>" +
				        "</select> "+ 
				        "<br/><input id=\"filter\" value=\"\" size=\"30\"/>" +
				    "</td>" +
			      "</tr>";

		$('#datastore_search').append(output);
		$("#loaderList").hide();
		$("div.box-content").show();
		$("#kind").change(onKindChange);
	}

	$("#execute_query").click(function() {
		var kindSelected = $('#kind').find(":selected").val();
		var countSelected = $('#count').prop('checked');
		var pageSize = $('#pageSize').find(":selected").val();
		var offset = $('#offset').val();
		var propertyName = $('#property_name').find(":selected").val();
		var filterOperator = $('#filter_operator').find(":selected").val();
		var propertyValue = $('#filter').val();

		$.ajax({
			  url: document.location.origin + "/fga/admin",
			  data: "service=datastore" +
			        "&op=query" +
			        "&kind=" + kindSelected +
			  		"&count=" + countSelected +
			  		"&pageSize=" + pageSize +
			  		"&offset=" + offset +
			  		"&propertyName=" + checkUndefined(propertyName) +
			  		"&propertyValue=" + checkUndefined(propertyValue) +
			  		"&filterOperator=" + checkUndefined(filterOperator),
			  type: "post",
			  success: function(json) {
				  onSuccessListData(json);
			  },
			  error: function(xhr, ajaxOptions, thrownError) {
                  alert("Error executing query");
			  }
		});
	});

    $('#first_page_top, #first_page_bottom').click(function(e) {
        e.preventDefault();

        var kind = $('#kind').find(":selected").val();
        var offset = parseInt($('#offset').val());
        var pageSize = parseInt($('#pageSize').find(":selected").val());
        if (offset != 0) {
            getData(kind, 10, 0);
            return false;
        }
    });

    $('#previous_page_top, #previous_page_bottom').click(function(e) {
        e.preventDefault();

        var kind = $('#kind').find(":selected").val();
        var offset = parseInt($('#offset').val());
        var pageSize = parseInt($('#pageSize').find(":selected").val());
        if ((offset - pageSize) >= 0) {
            getData(kind, pageSize, offset - pageSize);
            return false
        }
    });

    $('#next_page_top, #next_page_bottom').click(function(e) {
        e.preventDefault();

        var kind = $('#kind').find(":selected").val();
        var offset = parseInt($('#offset').val());
        var pageSize = parseInt($('#pageSize').find(":selected").val());
        var entityCount = parseInt($('#entityCount').val());

        if ((offset + pageSize) < entityCount) {
            getData(kind, pageSize, pageSize + offset);
            return false
        }
    });
});

function getData(kind, pageSize, offset) {
    $.ajax({
          url: document.location.origin + "/fga/admin",
          data: "service=datastore&op=query&kind=" + kind + "&count=false&pageSize=" + pageSize + "&offset=" + offset,
          type: "post",
          success: function(json) {
              onSuccessListData(json);
          },
          error: function(xhr, ajaxOptions, thrownError) {
              alert("Error getting data");
          }
    });
}

function onSuccessListData(json) {
//	$('#result_data').empty();
    $('#result_data').css("visibility","visible");

    // Set the number of entities
    var entityCountToShow = json.entityCount;
    if (typeof entityCountToShow === 'undefined') {
        entityCountToShow = 0;
    }
    $('#total_entities').empty();
    $('#total_entities').append("Total entities: " + entityCountToShow);
    $('#entityCount').val(json.entityCount);

    // Show or not entities count
    if (json.count == true) {
        $('#total_entities').css("display","block");
    } else {
        $('#total_entities').css("display","none");
    }

    // Set offset
    $('#offset').empty();
    $('#offset').val(json.offset);

    // Set page size
    $('#pageSize').val(json.pageSize);


    $('#offset_top').empty();
    $('#offset_bottom').empty();
    if (json.offset != 'undefined') {
        $('#offset_top').append(json.offset);
        $('#offset_bottom').append(json.offset);
    } else {
        $('#offset_top').append(0);
        $('#offset_bottom').append(0);
    }

    $('#result_data_content').empty();

    var searchFilter = "";
    $('#property_name').empty();

    if (json.properties != 'undefined') {
        var resultDataHeaders = "<thead>" +
            "<tr>" +
                "<td class=\"center\"><input id=\"selectAllEntities\" type=\"checkbox\"/></td>" +
                "<td class=\"center\"><b>ID / Key</b></td>";

        $(json.properties).each(function(i,propertie){
            resultDataHeaders += "<td><b>" + checkUndefined(propertie.name) + "</b></td>";
            searchFilter += "<option value=\"" + propertie.name + "\">" +propertie.name + "</option>";

        });

        resultDataHeaders += "</tr>" +
            "</thead>";

        $('#result_data_content').append(resultDataHeaders);
        $('#property_name').append(searchFilter);
    }

    if (json.entities != 'undefined') {
        var resultDataContent = "";
        var cssClass = "odd";
        $(json.entities).each(function(i,entity) {
            var kind = checkUndefined(json.kind);
            var value = checkUndefined(entity.key);
            var id = checkUndefined(entity.id);

            var url = "/fga/admin/datastore-edit.html?kind=" + kind + "&id=" + id;

            resultDataContent += "<tr class=\""+cssClass+"\">" +
                    "<td class=\"center\">" +
                        "<input class=\"_entity_selector\" type=\"checkbox\" name=\"id\" value=\"" + value + "\">" +
                    "</td>";
            resultDataContent += "<td><a href=\"" + url + "\">" + value + "</a></td>";
            if (entity.dataList !== 'undefined') {

                $(entity.dataList).each(function(i,data) {
                    resultDataContent += "<td>" + data.value + "</td>";
                });
                resultDataContent += "</tr>";

                if (cssClass == "odd") {
                    cssClass="even";
                } else {
                    cssClass="odd";
                }
            }
        });
        $('#result_data_content').append(resultDataContent);

    }
    $("#selectAllEntities").change( onSelectAllEntitiesChange );
}

function checkUndefined(data) {
	if(typeof data === 'undefined') {
		return "";
	}
	return data;
}

function onKindChange() {
	var kindSelected = $('#kind').find(":selected").val();

	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=datastore&op=get_properties&kind=" + kindSelected,
		  type: "post",
		  success: function(json) {
			  var searchFilter = "";

			  if (json != 'undefined') {
				  for (var k in json) {
					  searchFilter += "<option value=\"" + checkUndefined(json[k].name) + "\">" +
					  checkUndefined(json[k].name) + "</option>";
				  }
			  }

			  $('#property_name').empty();
			  $('#property_name').append(searchFilter);

		  },
		  error: function(xhr, ajaxOptions, thrownError) {
              alert("Error getting properties data!");
		  }
	});
}

function onSelectAllEntitiesChange() {
	var checked = $("#selectAllEntities").is(":checked");
	$("input._entity_selector").attr("checked", checked);
}


function deleteEntities() {
	var send = false;
	var r = confirm('Are you sure you want to delete the selected records?');
	if (!r) {
		return false;
	}

	var kindSelected = $('#kind').find(":selected").val();
	var data = "service=datastore&op=delete&kind=" + kindSelected;
    var entities = $("input._entity_selector");
	$(entities).each(function(i,check) {
		if (check.checked) {
			data += "&id=" + check.value;
			send = true;
		}
	});
	
	if (!send) {
		return false;
	}
	
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: data,
		  type: "post",
		  success: function(json) {
			  // Reload page
			  location.reload();
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
              alert("Error deleting data");
		  }
	});
}

function newEntity() {
	window.location = "/fga/admin/datastore-new.html?kind=" + $('#kind option:selected').val();
}