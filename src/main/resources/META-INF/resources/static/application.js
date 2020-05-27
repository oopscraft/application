/**
 * JQUERY AJAX setting
 */
var _progress = null;
$(document).ajaxStart(function(event) {
	console.debug('$(document).ajaxStart',event);
	_progress = new duice.Progress(document.body);
	_progress.start();
});
// If not configure, "Provisional headers are shown" error found in CHROME.
$(document).ajaxSend(function(event, jqXHR, settings) {
	console.debug('$(document).ajaxSend',event,jqXHR,settings);
	var csrfToken = duice.getCookie('X-Csrf-Token');
	jqXHR.setRequestHeader("X-Csrf-Token",csrfToken);
	jqXHR.setRequestHeader('Cache-Control','no-cache, no-store, must-revalidate');
	jqXHR.setRequestHeader('Pragma','no-cache');
	jqXHR.setRequestHeader('Expires','0');
});
// Checks error except cancellation(readyState = 0)
$(document).ajaxError(function(event, jqXHR, settings, thrownError){
	console.debug('$(document).ajaxError', jqXHR);
	if(settings.suppressErrors){
		return;
	}
	if(jqXHR.readyState > 0){
		console.error(event, jqXHR, settings, thrownError);
		try {
			var response = JSON.parse(jqXHR.responseText);
			duice.alert('<span style="font-weight:bold; color:red;">' + response.message + '</span>');
		}catch(e){
			duice.alert('<span style="font-weight:bold; color:red;">' + response.responseText + '</span>');
		}
	}
});
// Checks
$(document).ajaxSuccess(function(event, jqXHR, settings){
	console.debug('$(document).ajaxSuccess',event,jqXHR,settings);
});
// Checks
$(document).ajaxComplete(function(event, jqXHR, settings){
	console.debug('$(document).ajaxComplete',event,jqXHR,settings);
});
// checks stop event
$(document).ajaxStop(function(event) {
	console.debug('$(document).ajaxStop',event);
	if(_progress){
		_progress.stop();
	}
});

/**
 * Parsed total count from Content-Range header
 * @Param {Object} jqXHR
 */
const _parseTotalCount = function(jqXHR){
	var totalCount = -1;
	var contentRange = jqXHR.getResponseHeader("Content-Range");
	try {
    	var totalCount = contentRange.split(' ')[1].split('/')[1];
		totalCount = parseInt(totalCount);
		if(isNaN(totalCount)){
			return -1;
		}
		return totalCount;
	}catch(e){
		console.error(e);
		return -1;
	}
}

/**
 * Opens link
 */
const _openLink = function(linkUrl, linkTarget){
	if(linkTarget === '_blank'){
		window.open(linkUrl,'_blank');
	}else{
		window.location.href = linkUrl;
	}
}

/**
 * Changes language
 */ 
const _changeLanguage = function(language){
	duice.setCookie('X-Locale', language);
	window.location = '?_lang=' + language;
}


