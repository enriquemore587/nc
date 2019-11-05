$.ajax({
	  url: document.location.origin + "/fga/admin",
	  data: "service=logout",
	  type: "post",
	  success: function(json) {
		  onSuccess(json);
	  },
	  error: function(xhr, ajaxOptions, thrownError) {
		  onError(xhr, ajaxOptions, thrownError);
	  }
});

function onSuccess(json) {
	location.href="fga/admin/index.html";
}

function onError(xhr, ajaxOptions, thrownError) {
	
}