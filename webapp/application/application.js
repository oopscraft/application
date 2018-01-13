/**
 * Disabled Specific Event
 * @param e
 * @returns
 */
$(document).keypress(function(e) { 
	if (e.keyCode == 13){
		e.preventDefault();
	}
});

/**
 * Overriding Date format
 */
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";
 
    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;
     
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

/**
 * Overriding String prototype
 */
String.prototype.string = function(len){
	var s = '', i = 0; 
	while (i++ < len) { 
		s += this; 
	} 
	return s;
};
String.prototype.zf = function(len){
	return "0".string(len - this.length) + this;
};

/**
 * Overding Number prototype
 */
Number.prototype.zf = function(len){
	return this.toString().zf(len);
};
Number.prototype.format = function(){
	if(this==0) return 0;
	var reg = /(^[+-]?\d+)(\d{3})/;
	var n = (this + '');
	while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');
	return n;
};

/**
 * Return random integer
 */
function getRandom(min,max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
}

/**
 * Returns ramdom color
 */
function getRandomColor() {
	var letters = '0123456789ABCDEF';
	var color = '#';
	for (var i = 0; i < 6; i++) {
		color += letters[Math.floor(Math.random() * 16)];
	}
	return color;
}

/**
 * Fires alert message
 */
$.alert = function(message, callback) {
	alertify.alert(
		 '<img src="data:image/gif;base64,R0lGODlhFAAUANUrAEm245nW75zX8Fy+5hWh2/n9/gSb2Rei3IfP7crq9/f8/iCm3fv9/oTO7Amd2fj8/kGz4nbI6g+f2rzk9fX7/UO04hmj3AKa2Ob1+xuk3JHT7pLT7gab2Q2e2gOa2PD5/dXu+VK65VC55AWb2VO65ef1+0a14wGZ2PP6/ROh2wCZ2P///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS41LWMwMjEgNzkuMTU1NzcyLCAyMDE0LzAxLzEzLTE5OjQ0OjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InV1aWQ6NUQyMDg5MjQ5M0JGREIxMTkxNEE4NTkwRDMxNTA4QzgiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MDJENkQ3MTIzMDcyMTFFNEJBOEJDOEMzNzk1QjcxOTkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6REM1NjQ0QjAzMDcxMTFFNEJBOEJDOEMzNzk1QjcxOTkiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgSWxsdXN0cmF0b3IgQ0MgMjAxNCAoTWFjaW50b3NoKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ1dWlkOjFmNDA1MGUwLTY0MGEtNmI0Yy1hMTViLTJiMzc0ZmQyMDIyYSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDphOWY4MTVlMi03NDJhLTRmMzktOWUwYS00MzM1NGJmMTQ0ZWYiLz4gPGRjOnRpdGxlPiA8cmRmOkFsdD4gPHJkZjpsaSB4bWw6bGFuZz0ieC1kZWZhdWx0Ij5JbmZvcm1hdGlvbiBpY29uPC9yZGY6bGk+IDwvcmRmOkFsdD4gPC9kYzp0aXRsZT4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAArACwAAAAAFAAUAAAGi8CVUIgKAAgqFQEQQA2fQkEqSaWmBNBVYVDtUgeFJ9dLHgwFZJXB4MWipt5DCQO3FtMLBSXjNaZVFgdkR14XIhoRHmRIXh0VICshf2kJKyZpjF0SDA8OiwBkJCsTaUxkDSsbaU11VBAoHwgnXSlOaF4jHGRYQmOTSWZDW78qYFlSaVdZQ0WESkxOT0EAOw==" style="vertical-align:text-bottom;"/> Alert'
		,message
		,function() {
			if(callback != undefined){
				callback.call();
			}
		 }
 	);		 
}

/**
 * Fires confirm message
 */
$.confirm = function(message, callback) {
	alertify.confirm(
		 '<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAMAAAC6V+0/AAAAOVBMVEUAn+P///8vsegSpuWg2/Vxyu93zPDn9vxmxe5Zwe31+/5Vv+yZ2fREueru+f0irOfd8vvM7Pm75fhicjjEAAAAh0lEQVQYlW2R2RaDIAxEJ4ssFq31/z9W0ATbyjxwwoUJSQA1Rc0swlnjuUVblGFiNThP+NI0n/CHVdqg4k9KiJZvCUEsb4RdfNFnXd92FfkKylZ5uOIMc6d0Q4Z4ftnI7HLDQsmP0Zuh0tvyh4CweJTRaw/ursiLB+3ujuM2xwMZj2445Od3HFAuAq5yercSAAAAAElFTkSuQmCC" style="vertical-align:text-bottom;"/> Confirm'
		 ,message
		 ,function() {
			if(callback != undefined) {
				callback.call();
			}
		 }
		 ,function() {}
	);
}



/**
 * WebSocketClient Prototype (Auto reconnectable)
 * var url = 'wss://127.0.0.1:9090/webSocket.ws';
 * var webSocketClient = new WebSocketClient();
 * webSocketClient.open(url);
 * webSocketClient.onmessage = function(e){
 * 	console.log(e);
 * }
 */
var WebSocketClient = function(){}
WebSocketClient.prototype.open = function(url){
	this.url = url;
	this.instance = new WebSocket(this.url);
	var __this__ = this;
	this.instance.onopen = function(e){
		__this__.onopen(e);
	}
	this.instance.onmessage = function(e){
		__this__.onmessage(e);
	}
	this.instance.onclose = function(e){
		console.log(e);
		__this__.onclose(e);
		// reconnect
		__this__.open(__this__.url);
	}
	this.instance.onerror = function(e){
		__this__.onerror(e);
	}
}
WebSocketClient.prototype.send = function(message){
	try{
		this.instance.send(message);
	}catch (e){
		console.log(e);
	}
}
WebSocketClient.prototype.onopen = function(e){	console.log("WebSocketClient: open",arguments);	}
WebSocketClient.prototype.onmessage = function(e){	console.log("WebSocketClient: message",arguments);	}
WebSocketClient.prototype.onerror = function(e){	console.log("WebSocketClient: error",arguments);	}
WebSocketClient.prototype.onclose = function(e){	console.log("WebSocketClient: closed",arguments);	}


/**
 * Creates and return inner table element.
 * @param jsonData
 * @returns
 */
var JsonTable = function(jsonData){
	this.jsonData = jsonData;
}
JsonTable.prototype.createTable = function(data, className) {
	var table = $('<table class="' + className + '"></table>');

	if(data instanceof Array) {
		var tableThead = $('<thead></thead>');
		var tableTheadTr = $('<tr></tr>');
		tableThead.append(tableTheadTr);
		var tableTbody = $('<tbody></tbody>');
		for(var idx = 0; idx < data.length; idx ++ ) {
			var record = data[idx];
			var tableTbodyTr = $('<tr></tr>');
			if(idx == 0) {
				for(var name in record){
					tableTheadTr.append($('<th>' + name + '</th>'));
				}
			}
			var tableTbodyTr = $('<tr></tr>');
			for(var name in record){
				var value = record[name];
				if(value instanceof Array || value instanceof Object) {
					tableTbodyTr.append($('<td></td>').append(this.createTable(value)));
				}else{
					tableTbodyTr.append($('<td>' + value + '</td>'));
				}
			}
			tableTbody.append(tableTbodyTr);
		}
		table.append(tableThead);
		table.append(tableTbody);
		return table;
	}
	else if(data instanceof Object) {
		var tableThead = $('<thead></thead>');
		var tableTbody = $('<tbody></tbody>');
		for(var name in data){
			var tableTbodyTr = $('<tr></tr>');
			var value = data[name];
			tableTbodyTr.append($('<td>' + name + '</td>'));
			tableTbodyTr.append($('<td></td>').append(this.createTable(value)));
			tableTbody.append(tableTbodyTr);
		}
		table.append(tableThead);
		table.append(tableTbody);
		return table;
	}
	else {
		return data;
	}
}
JsonTable.prototype.draw = function(container, className) {
	var table = this.createTable(this.jsonData, className);
	container.append(table);
}



