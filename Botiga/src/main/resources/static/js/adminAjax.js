jQuery(document).ready(function() {

	$(document).ready(function() {
		$("#file").on("change", uploadFile);
	});
	
	$(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader('X-CSRF-TOKEN', token);
    });
	
	function uploadFile() {
		$.ajax({
			url : "/uploadcsv",
			type : "POST",
			data : new FormData($("#csv_form")[0]),
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			cache : false
		});
	}
});