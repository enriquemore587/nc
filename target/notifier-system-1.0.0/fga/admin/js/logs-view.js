$(function() {
	$("#dest").change(onDestChanged);
	$("#field").change(onFieldChanged);
	$("#fields").keyup(onFieldsKeyUp);
	$("#outputFormat").change(onOutputFormatChanged);
	$("#time").change(onTimeChanged);
	onDestChanged();
	onOutputFormatChanged();
	onTimeChanged();
});

function onDestChanged() {
	var value = $("#dest").val();
	if (value == "inline") {
		$("#filenameBlock").hide();
		$("#filename").attr("disabled", true);
	} else {
		$("#filenameBlock").show();
		$("#filename").attr("disabled", false);
	}
}

function onFieldChanged() {
	var values = $("#field").val();
	if (values) {
		$("#fields").val(values.join(","));
	}
}

function onFieldsKeyUp() {
	var values = $("#fields").val().split(",");
	$("#field").val(values);
}

function onOutputFormatChanged() {
	var value = $("#outputFormat").val();
	if (value == "delimited") {
		$("#fieldSeparatorBlock").show();
		$("#fieldSeparator").attr("disabled", false);
	} else {
		$("#fieldSeparatorBlock").hide();
		$("#fieldSeparator").attr("disabled", true);
	}
}

function onTimeChanged() {
	var value = $("#time").val();
	if (value == "time") {
		$("#timeBlock").show();
		$("#startTimeUsec").attr("disabled", false);
		$("#endTimeUsec").attr("disabled", false);
		$("#markBlock").hide();
		$("#mark").attr("disabled", true);
	} else if (value == "mark") {
		$("#markBlock").show();
		$("#mark").attr("disabled", false);
		$("#timeBlock").hide();
		$("#startTimeUsec").attr("disabled", true);
		$("#endTimeUsec").attr("disabled", true);
	}
}
