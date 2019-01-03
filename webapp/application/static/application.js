var __loader; 
$(document).ajaxStart(function(event) {
	__loader = new juice.ui.__().load(document.body);
});

// If not configure, "Provisional headers are shown" error occured in chrome.
$(document).ajaxSend(function(event, jqxhr, settings) {
	jqxhr.setRequestHeader('Cache-Control','no-cache, no-store, must-revalidate');
	jqxhr.setRequestHeader('Pragma','no-cache');
	jqxhr.setRequestHeader('Expires','0');
});

// checks stop event
$(document).ajaxStop(function(event) {
	if(__loader){
		__loader.release();
	}
});

// Checks error except cancelation(readyState = 0)
$(document).ajaxError(function(event, jqXHR, ajaxSettings,thrownError ){
	console.log(jqXHR);
	if(jqXHR.readyState > 0){
		console.log(event);
		console.log(jqXHR);
		console.log(ajaxSettings);
		console.log(thrownError);
		alert(jqXHR.responseText);
	}
});

/**
 * Parsed total count from Content-Range header
 * @Param {Object} jqXHR
 */
function __parseTotalCount(jqXHR){
	var contentRange = jqXHR.getResponseHeader("Content-Range");
	try {
		var totalCount = contentRange.split(' ')[1].split('/')[1];
		return parseInt(totalCount);
	}catch(e){
		return -1;
	}
}
