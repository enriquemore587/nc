$(document).ready(function(){
	
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=task&op=comboList",
		  type: "post",
		  success: function(json) {
			  onSuccessComboList(json);
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
			  onErrorComboList(xhr, ajaxOptions, thrownError);
		  }
	});
	
	function onSuccessComboList(json) {
		$('#queue').append(json.taskList);
	}

	function onErrorComboList(xhr, ajaxOptions, thrownError) {
		
	}
	
	$("#enqueue_task").click(function() {
		$.ajax({
			  url: document.location.origin + "/fga/admin",
			  data: "service=task&op=enqueueTask&url=" + encodeURIComponent($("input[name='url']").val()) + "&RLimit=" + $("input[name='RLimit']").val() + "&ALimit=" + $("input[name='ALimit']").val() + "&queueName=" + $('#queue_name').val(),
			  type: "post",
			  success: function(json) {
				  onSuccessEnqueueTask(json);
			  },
			  error: function(xhr, ajaxOptions, thrownError) {
				  onErrorEnqueueTask(xhr, ajaxOptions, thrownError);
			  }
		});
		
		function onSuccessEnqueueTask(json) {
			var message = json.message;
			if (message == undefined) {
				message = "";
			}
			
			$('#task_result').append(message);
		}

		function onErrorEnqueueTask(xhr, ajaxOptions, thrownError) {
			
		}
	});
});

function showForm(queue_name, selIndex) {
	
	$('#task_detail').css("visibility","visible");
	$('#task_detail').css("display","block");
	
	if (selIndex != 0) {
		$('#queue_name').val(queue_name);
	}
	
	$.ajax({
		  url: document.location.origin + "/fga/admin",
		  data: "service=task&op=paramsForTask&queueName=" + queue_name,
		  type: "post",
		  success: function(json) {
			  onSuccess(json);
		  },
		  error: function(xhr, ajaxOptions, thrownError) {
			  onError(xhr, ajaxOptions, thrownError);
		  }
	});
	
	function onSuccess(json) {
		$('#table_task_detail').empty();
		$('#task_result').empty();
		$('#table_task_detail').append(json.paramList);
	}

	function onError(xhr, ajaxOptions, thrownError) {
		
	}
}







