var kindSelected = "ServicesSecurity";

$(document).ready(function() {
	$("#loaderList").show();

	var countSelected = "true";
	var pageSize = "10";
	var offset = "0";

	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=datastore" +
		        "&op=query" +
		        "&kind=" + kindSelected +
		  		"&count=" + countSelected +
		  		"&pageSize=" + pageSize +
		  		"&offset=" + offset,
		  type: "post",
		  success: function(json) {
			  onSuccessListData(json);
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
			  alert("Error executing query!");
		  }
	});

	$('#first_page_top, #first_page_bottom').click(function(e) {
        e.preventDefault();

        var offset = parseInt($('#offset').val());
        if (offset != 0) {
            getData(10, 0);
            return false;
        }
    });

    $('#previous_page_top, #previous_page_bottom').click(function(e) {
        e.preventDefault();

        var offset = parseInt($('#offset').val());
        var pageSize = parseInt($('#pageSize').val());
        if ((offset - pageSize) >= 0) {
            getData(pageSize, offset - pageSize);
            return false
        }
    });

    $('#next_page_top, #next_page_bottom').click(function(e) {
        e.preventDefault();

        var offset = parseInt($('#offset').val());
        var pageSize = parseInt($('#pageSize').val());
        var entityCount = parseInt($('#entityCount').val());
        if ((offset + pageSize) < entityCount) {
            getData(pageSize, pageSize + offset);
            return false
        }
    });
});

function getData(pageSize, offset) {
    $.ajax({
          url: document.location.origin + "/fga/admin",
          data: "service=datastore" +
                "&op=query" +
                "&kind=" + kindSelected +
                "&count=true" +
                "&pageSize=" + pageSize +
                "&offset=" + offset,
          type: "post",
          success: function(json) {
              onSuccessListData(json);
          },
          error: function(xhr, ajaxOptions, thrownError) {
              alert("Error getting data!");
          }
    });
}

function onSuccessListData(json) {
//	$('#result_data').empty();
    $('#result_data').css("visibility","visible");
    $('#entities').css("display","block");
    $('#not_entities').css("display","none");

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

    if (json.entities.length > 0) {
        if (json.properties != 'undefined') {
            var resultDataHeaders = "<thead>" +
                "<tr>" +
                    "<td class=\"center\"><input id=\"selectAllEntities\" type=\"checkbox\"/></td>" +
                    "<td class=\"center\"><b>Client ID</b></td>";

            $(json.properties).each(function(i,property){
                resultDataHeaders += "<td><b>" + checkUndefined(property.name) + "</b></td>";
                searchFilter += "<option value=\"" + property.name + "\">" +property.name + "</option>";

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

                var url = "/fga/admin/services-edit.html?kind=" + kind + "&id=" + id;

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
    } else {
        $('#entities').css("display","none");
        $('#not_entities').css("display","block");
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
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=datastore" +
		        "&op=get_properties" +
		        "&kind=" + kindSelected,
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
	var entities = $("input._entity_selector");
	var send = false;
	var r = confirm('Are you sure you want to delete the selected records?');
	if (!r) {
		return false;
	}

	var data = "service=datastore" +
	           "&op=delete" +
	           "&kind=" + kindSelected;
		
	$(entities).each(function(i,check) {
		if (check.checked){
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
	window.location = "/fga/admin/datastore-new.html?kind=" + kindSelected;
}