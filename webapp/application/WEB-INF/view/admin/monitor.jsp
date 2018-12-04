<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>

<script type="text/javascript">
var systemLoadAverageChart;
var memoryUsageChart;
var classCountChart;
var osInfo = new juice.data.Map();
var heapMemoryUsage = new juice.data.Map();
var nonHeapMemoryUsage = new juice.data.Map();
var classInfo = new juice.data.Map();
var threadInfos = new juice.data.List();

// Adds webSocket handler
__webSocketClient.addMessageHandler(function(event){
	var message = JSON.parse(event.data).message;
	//console.log(message);
	updateSystemLoadAverageChart(message);
	updateMemoryUsageChart(message);
	updateClassCountChart(message);

	var lastMonitorInfo = message[message.length-1];
	osInfo.fromJson(lastMonitorInfo.osInfo);
	heapMemoryUsage.fromJson(lastMonitorInfo.memInfo.heapMemoryUsage);
	nonHeapMemoryUsage.fromJson(lastMonitorInfo.memInfo.nonHeapMemoryUsage);
	classInfo.fromJson(lastMonitorInfo.classInfo);
	threadInfos.fromJson(lastMonitorInfo.threadInfos);
});

/**
 * On document loaded
 */
$( document ).ready(function() {
	drawSystemLoadAverageChart();
	drawMemoryUsageChart();
	drawClassCountChart();
	
	__webSocketClient.send(JSON.stringify({id:'monitorInfo'}));
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
					pointStyle: 'crossRot',
				}]
			},
		    options: {
				elements: {
					line: {
						tension: 0
					}
				},
			    animation: {
			        duration: 0
			    },
				scales: {
	                yAxes: [{
						display: true,
						ticks: {
							min: 0,
							max: 2,
							stepSize: 0.5
						}
					}],
				},
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
					pointStyle: 'crossRot',
				},{
					label: 'Heap Usage',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'steelblue',
					pointStyle: 'crossRot',
				},{
					label: 'None Heap Usage',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: '#333',
					pointStyle: 'crossRot',
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

/**
 * Draws classCountChart
 */
function drawClassCountChart(){
	classCountChart = new Chart(
		document.getElementById('classCountChart').getContext('2d'),{
		    type: 'line',
		    data:  {
				labels: [],
				datasets: [{
					label: 'Loaded Class',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'red',
					pointStyle: 'crossRot',
				},{
					label: 'Unload Class',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'gray',
					pointStyle: 'crossRot',
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
 * Updates classCountChart
 */
function updateClassCountChart(message){
	classCountChart.data.labels = [];
	classCountChart.data.datasets[0].data = [];
	message.forEach(function(monitorInfo){
		classCountChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
		classCountChart.data.datasets[0].data.push(monitorInfo.classInfo.loadedClassCount);
		classCountChart.data.datasets[1].data.push(monitorInfo.classInfo.unloadedClassCount);
	});
	classCountChart.update();
}

</script>
<style type="text/css">
.container {
	display: flex;
	justify-content: space-between;
}
</style>
<div class="title1">
	<i class="icon-monitor"></i>
	<spring:message code="application.text.monitor"/>
</div>
<div class="container">
	<div class="division" style="width:33%;">
		<div class="title2" style="width:100%;border-bottom:dotted 1px #ccc;">
			<i class="icon-file"></i>
			CPU Usage
		</div>
		<canvas id="systemLoadAverageChart"></canvas>
		<table class="detail">
			<colgroup>
				<col style="width:40%;">
				<col style="width:60%;">
			</colgroup>
			<tr>
				<th>name</th>
				<td><label data-juice="Label" data-juice-bind="osInfo.name"></label></td>
			</tr>
			<tr>
				<th>version</th>
				<td><label data-juice="Label" data-juice-bind="osInfo.version"></label></td>
			</tr>
			<tr>
				<th>arch</th>
				<td><label data-juice="Label" data-juice-bind="osInfo.arch"></label></td>
			</tr>
			<tr>
				<th>availableProcessors</th>
				<td><label data-juice="Label" data-juice-bind="osInfo.availableProcessors"></label></td>
			</tr>
			<tr>
				<th>systemLoadAverage</th>
				<td><label data-juice="Label" data-juice-bind="osInfo.systemLoadAverage"></label></td>
			</tr>
		</table>
	</div>
	<div class="division" style="width:33%;">
		<div class="title2" style="width:100%;border-bottom:dotted 1px #ccc;">
			<i class="icon-file"></i>
			Memory Usage
		</div>
		<canvas id="memoryUsageChart"></canvas>
		<table class="detail">
			<colgroup>
				<col style="width:20%;">
				<col style="width:30%;">
				<col style="width:20%;">
				<col style="width:30%;">
			</colgroup>
			<tr>
				<th>-</th>
				<th>heapMemoryUsage</th>
				<th>nonHeapMemoryUsage</th>
			</tr>
			<tr>
				<th>init</th>
				<td><label data-juice="Label" data-juice-bind="heapMemoryUsage.init"></label></td>
				<td><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.init"></label></td>
			</tr>
			<tr>
				<th>used</th>
				<td><label data-juice="Label" data-juice-bind="heapMemoryUsage.used"></label></td>
				<td><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.used"></label></td>
			</tr>
			<tr>
				<th>committed</th>
				<td><label data-juice="Label" data-juice-bind="heapMemoryUsage.committed"></label></td>
				<td><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.committed"></label></td>
			</tr>
			<tr>
				<th>max</th>
				<td><label data-juice="Label" data-juice-bind="heapMemoryUsage.max"></label></td>
				<td><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.max"></label></td>
			</tr>
		</table>
	</div>
	<div class="division" style="width:33%;">
		<div class="title2" style="width:100%;border-bottom:dotted 1px #ccc;">
			<i class="icon-file"></i>
			Class Count
		</div>
		<canvas id="classCountChart"></canvas>
		<table class="detail">
			<colgroup>
				<col style="width:40%;">
				<col style="width:60%;">
			</colgroup>
			<tr>
				<th>totalLoadedClassCount</th>
				<td><label data-juice="Label" data-juice-bind="classInfo.totalLoadedClassCount"></label></td>
			</tr>
			<tr>
				<th>loadedClassCount</th>
				<td><label data-juice="Label" data-juice-bind="classInfo.loadedClassCount"></label></td>
			</tr>
			<tr>
				<th>unloadedClassCount</th>
				<td><label data-juice="Label" data-juice-bind="classInfo.unloadedClassCount"></label></td>
			</tr>
		</table>
	</div>
</div>
<div class="container">
	<div class="division" style="width:100%;">
		<div class="title2" style="width:100%;">
			<i class="icon-file"></i>
			Thread List
		</div>
		<table id="threadInfosTable" data-juice="Grid" data-juice-bind="threadInfos" data-juice-item="threadInfo">
			<thead>
				<tr>
					<th>threadId</th>
					<th>threadName</th>
					<th>threadState</th>
					<th>waitedCount</th>
					<th>waitedTime</th>
					<th>blockCount</th>
					<th>blockTime</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><label data-juice="Label" data-juice-bind="threadInfo.threadId"></label></td>
					<td><label data-juice="Label" data-juice-bind="threadInfo.threadName"></label></td>
					<td><label data-juice="Label" data-juice-bind="threadInfo.threadState"></label></td>
					<td><label data-juice="Label" data-juice-bind="threadInfo.waitedCount"></label></td>
					<td><label data-juice="Label" data-juice-bind="threadInfo.waitedTime"></label></td>
					<td><label data-juice="Label" data-juice-bind="threadInfo.blockCount"></label></td>
					<td><label data-juice="Label" data-juice-bind="threadInfo.blockTime"></label></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>