<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>

<script type="text/javascript">
google.charts.load('current', {'packages':['corechart']});


/**
 * On document loaded
 */
$( document ).ready(function() {
	//__webSocketClient.send({id:'monitorInfo'});

	__webSocketClient.addMessageHandler(function(event){
		var message = JSON.parse(event.data).message;
		drawSystemLoadAverageChart(message);
	});
});

/**
 * drawSystemLoadAverageChart
 */
function drawSystemLoadAverageChart(message) {
	
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Sate');
	data.addColumn('number', 'System Load Average');
	
	message.forEach(function(monitorInfo){
		data.addRow([
			moment(new Date(monitorInfo.date)).format("mm:ss"),
			monitorInfo.osInfo.systemLoadAverage
		]);
	});
	
	var options = {
		title: 'System Load Average',
		curveType: 'function',
		legend: { position: 'bottom' },
		animation:{
			duration: 1000,
			easing: 'out',
			//startup: true //This is the new option
		}
	};

	var chart = new google.visualization.LineChart(document.getElementById('systemLoadAverageChart'));
	chart.draw(data, options);
}
    
    
</script>


<div id="systemLoadAverageChart" style="width:50%;"></div>
