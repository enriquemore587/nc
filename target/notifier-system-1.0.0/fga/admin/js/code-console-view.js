function calculateSize(s) {
	var length = s.length;
	var rows = 0;
	var cols = 0;
	var colsMax = 0;

	for (var i = 0; i < length; i++) {
		var c = s[i];
		if (c == "\n") {
			rows++;
			colsMax = Math.max(cols, colsMax);
			cols = 0;
		} else {
			cols++;
		}
	}

	return {
		cols: colsMax,
		rows: rows
	}
}

function resizeTextArea(cols, rows) {
	var $element = $(this);
	var content = calculateSize($element.val());
	$element.attr("cols", Math.max(120, content.cols + 5)).attr("rows", Math.max(5, content.rows + 1));
}

$(function() {
	$("#code").each(resizeTextArea);
	$("#code").keyup(function() {
		$("#code").each(resizeTextArea);
	});
});
