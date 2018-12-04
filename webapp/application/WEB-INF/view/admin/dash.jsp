<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>

<script type="text/javascript">

// Adds webSocket handler
__webSocketClient.addMessageHandler(function(event){
	var message = JSON.parse(event.data).message;
	console.log(message);
	updateSystemLoadAverageChart(message);
	updateMemoryUsageChart(message);
});

var systemLoadAverageChart;
var memoryUsageChart;


/**
 * On document loaded
 */
$( document ).ready(function() {
	
	drawSystemLoadAverageChart();
	drawMemoryUsageChart();
});

/**
 * Draws systemLoadAverageChart
 */
function drawSystemLoadAverageChart(){
	systemLoadAverageChart = new Chart(
		document.getElementById('systemLoadAverageChart').getContext('2d'),{
		    type: 'line',
		    data:  {
				labels: [],
				datasets: [{
					label: 'System Load Average',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'steelblue',
					pointStyle: 'cross',
				}]
			},
		    options: {
				elements: {
					line: {
						tension: 0
					}
				}
		    }
		}
	);
}

/**
 * Updates systemLoadAverageChart
 */
function updateSystemLoadAverageChart(message){
	systemLoadAverageChart.data.labels = [];
	systemLoadAverageChart.data.datasets[0].data = [];
	message.forEach(function(monitorInfo){
		systemLoadAverageChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
		systemLoadAverageChart.data.datasets[0].data.push(monitorInfo.osInfo.systemLoadAverage);
	});
	systemLoadAverageChart.update();
}

/**
 * Draws memoryUsageChart
 */
function drawMemoryUsageChart(){
	memoryUsageChart = new Chart(
		document.getElementById('memoryUsageChart').getContext('2d'),{
		    type: 'line',
		    data:  {
				labels: [],
				datasets: [{
					label: 'Heap Size',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'red',
					pointStyle: 'cross',
				},{
					label: 'Heap Usage',
					data: [],
					fill: true,
					borderWidth: 1,
					borderColor: 'steelblue',
					pointStyle: 'cross',
				},{
					label: 'None Heap Usage',
					data: [],
					fill: true,
					borderWidth: 1,
					borderColor: '#333',
					pointStyle: 'cross',
				}]
			},
		    options: {
				elements: {
					line: {
						tension: 0
					}
				}
		    }
		}
	);
}

/**
 * Updates systemLoadAverageChart
 */
function updateMemoryUsageChart(message){
	memoryUsageChart.data.labels = [];
	memoryUsageChart.data.datasets[0].data = [];
	message.forEach(function(monitorInfo){
		memoryUsageChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
		memoryUsageChart.data.datasets[0].data.push(monitorInfo.memInfo.heapMemoryUsage.max/1024);
		memoryUsageChart.data.datasets[1].data.push(monitorInfo.memInfo.heapMemoryUsage.used/1024);
		memoryUsageChart.data.datasets[2].data.push(monitorInfo.memInfo.nonHeapMemoryUsage.used/1024);
	});
	memoryUsageChart.update();
}















function createMemoryUsageChart(){
	var data = [20, 10];
	var ctx = document.getElementById('memoryUsageChart').getContext('2d');
	var myLineChart = new Chart(ctx, {
	    type: 'line',
	    data: data,
	    options: {
	        elements: {
	            line: {
	                tension: 0, // disables bezier curves
	            }
	        }
	    }
	});
}

function createClassCountChart(){
	var data = [20, 10];
	var ctx = document.getElementById('classCountChart').getContext('2d');
	var myLineChart = new Chart(ctx, {
	    type: 'line',
	    data: data,
	    options: {
	        elements: {
	            line: {
	                tension: 0, // disables bezier curves
	            }
	        }
	    }
	});
}
    
</script>
<style type="text/css">
.container {
	display: flex;
	justify-content: space-between;
}
</style>
<div class="container">
	<div style="width:33%;">
		<div class="title2" style="width:100%;border-bottom:dotted 1px #ccc;">
			<i class="icon-user"></i>
			CPU Usage
		</div>
		<canvas id="systemLoadAverageChart"></canvas>
	</div>
	
	<div style="width:33%;">
		<div class="title2" style="width:100%;border-bottom:dotted 1px #ccc;">
			<i class="icon-user"></i>
			CPU Usage
		</div>
		<canvas id="memoryUsageChart"></canvas>
	</div>
	
	<div style="width:33%;">
		<canvas id="classCountChart"></canvas>
	</div>
</div>