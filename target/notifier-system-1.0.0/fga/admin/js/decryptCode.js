$(document).ready(function(){
	$("#decrypt_button").click(function() {
		$.ajax({
			  url: document.location.origin + "/fga/admin",
			  data: "service=adminControl&op=decrypt&code=" + encodeURIComponent($("#param_code").val()),
			  type: "post",
			  success: function(json) {
				  onSuccess(json);
			  },
			  error: function(xhr, ajaxOptions, thrownError) {
				  onError(xhr, ajaxOptions, thrownError);
			  }
		});
		
		function onSuccess(json_obj) {
			
			var message = json_obj.message;
			if (message == undefined) {
				message = "";
			}
			
			$('#message').val(decodeURIComponent(message));
		}

		function onError(xhr, ajaxOptions, thrownError) {
			
		}
	});
});