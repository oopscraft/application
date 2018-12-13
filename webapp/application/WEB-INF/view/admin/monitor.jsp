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

/**
 * On document loaded
 */
$( document ).ready(function() {
	getMonitorInfos();
});

/**
 * Gets monitorInfos
 */
function getMonitorInfos() {
	$.ajax({
		 url: 'monitor/getMonitorInfos'
		,type: 'GET'
		,data: {}
		,success: function(data, textStatus, jqXHR) {
			console.log(data);
			var monitorInfos = data;
			var lastMonitorInfo = monitorInfos[monitorInfos.length-1];
			
			// updates top message.
			$('#topDiv').html(lastMonitorInfo.top);
			
			// draws chart
			drawSystemLoadAverageChart(monitorInfos);
			drawMemoryUsageChart(monitorInfos);
			drawClassCountChart(monitorInfos);
			
			// updates data
			osInfo.fromJson(lastMonitorInfo.osInfo);
			heapMemoryUsage.fromJson(lastMonitorInfo.memInfo.heapMemoryUsage);
			nonHeapMemoryUsage.fromJson(lastMonitorInfo.memInfo.nonHeapMemoryUsage);
			classInfo.fromJson(lastMonitorInfo.classInfo);
			threadInfos.fromJson(lastMonitorInfo.threadInfos);
			
			// starts websocket receive
			startWebSocketReceive();
  	 	}
	});	
}

/**
 * Starts webSocket receive
 */
function startWebSocketReceive() {
	__webSocketClient.addMessageHandler(function(event){
		var data = JSON.parse(event.data);
		var id = data.id;
		var message = data.message;

		if(id == 'monitorInfo'){
			var monitorInfo = message;
			
			// updates top message
			$('#topDiv').html(monitorInfo.top);

			// updates chart
			updateSystemLoadAverageChart(monitorInfo);
			updateMemoryUsageChart(monitorInfo);
			updateClassCountChart(monitorInfo);

			// updates data
			osInfo.fromJson(monitorInfo.osInfo);
			heapMemoryUsage.fromJson(monitorInfo.memInfo.heapMemoryUsage);
			nonHeapMemoryUsage.fromJson(monitorInfo.memInfo.nonHeapMemoryUsage);
			classInfo.fromJson(monitorInfo.classInfo);
			threadInfos.fromJson(monitorInfo.threadInfos);
		}
	});
}

/**
 * Draws systemLoadAverageChart
 */
function drawSystemLoadAverageChart(monitorInfos){
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
			    }
		    }
		}
	);
	
	systemLoadAverageChart.data.labels = [];
	systemLoadAverageChart.data.datasets[0].data = [];
	monitorInfos.forEach(function(monitorInfo){
		systemLoadAverageChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
		systemLoadAverageChart.data.datasets[0].data.push(monitorInfo.osInfo.systemLoadAverage);
	});
	systemLoadAverageChart.update();
}

/**
 * Updates systemLoadAverageChart
 */
function updateSystemLoadAverageChart(monitorInfo){
	systemLoadAverageChart.data.labels.shift();
	systemLoadAverageChart.data.datasets[0].data.shift();
	systemLoadAverageChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
	systemLoadAverageChart.data.datasets[0].data.push(monitorInfo.osInfo.systemLoadAverage);
	systemLoadAverageChart.update();
}

/**
 * Draws memoryUsageChart
 */
function drawMemoryUsageChart(monitorInfos){
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
				},
			    animation: {
			        duration: 0
			    },
		    }
		}
	);
	
	// updates chart data
	memoryUsageChart.data.labels = [];
	memoryUsageChart.data.datasets[0].data = [];
	monitorInfos.forEach(function(monitorInfo){
		memoryUsageChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
		memoryUsageChart.data.datasets[0].data.push(monitorInfo.memInfo.heapMemoryUsage.max/1024);
		memoryUsageChart.data.datasets[1].data.push(monitorInfo.memInfo.heapMemoryUsage.used/1024);
		memoryUsageChart.data.datasets[2].data.push(monitorInfo.memInfo.nonHeapMemoryUsage.used/1024);
	});
	memoryUsageChart.update();
}

/**
 * Updates systemLoadAverageChart
 */
function updateMemoryUsageChart(monitorInfo){
	memoryUsageChart.data.labels.shift();
	memoryUsageChart.data.datasets[0].data.shift();
	memoryUsageChart.data.datasets[1].data.shift();
	memoryUsageChart.data.datasets[2].data.shift();
	memoryUsageChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
	memoryUsageChart.data.datasets[0].data.push(monitorInfo.memInfo.heapMemoryUsage.max/1024);
	memoryUsageChart.data.datasets[1].data.push(monitorInfo.memInfo.heapMemoryUsage.used/1024);
	memoryUsageChart.data.datasets[2].data.push(monitorInfo.memInfo.nonHeapMemoryUsage.used/1024);
	memoryUsageChart.update();
}

/**
 * Draws classCountChart
 */
function drawClassCountChart(monitorInfos){
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
				},
			    animation: {
			        duration: 0
			    },
		    }
		}
	);
	
	// updates chart data
	classCountChart.data.labels = [];
	classCountChart.data.datasets[0].data = [];
	classCountChart.data.datasets[1].data = [];
	monitorInfos.forEach(function(monitorInfo){
		classCountChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
		classCountChart.data.datasets[0].data.push(monitorInfo.classInfo.loadedClassCount);
		classCountChart.data.datasets[1].data.push(monitorInfo.classInfo.unloadedClassCount);
	});
	classCountChart.update();
}

/**
 * Updates classCountChart
 */
function updateClassCountChart(monitorInfo){
	classCountChart.data.labels.shift();
	classCountChart.data.datasets[0].data.shift();
	classCountChart.data.datasets[1].data.shift();
	classCountChart.data.labels.push(moment(monitorInfo.date).format('mm:ss'));
	classCountChart.data.datasets[0].data.push(monitorInfo.classInfo.loadedClassCount);
	classCountChart.data.datasets[1].data.push(monitorInfo.classInfo.unloadedClassCount);
	classCountChart.update();
}

</script>
<style type="text/css">
.container {
	display: flex;
	justify-content: space-between;
}
#topDiv {
	height: 25rem;
	padding: 0rem 1rem;
	overflow: auto;
	color: white;
	background-color: black;
	font-family: Courier New, Consolas;
	line-height: 1rem;
}
</style>
<div class="title1">
	<i class="icon-monitor"></i>
	<spring:message code="application.text.monitor"/>
</div>
<div class="container">
	<div id="threadInfosDiv" class="division" style="width:100%;">
		<div class="title2" style="width:100%;border-bottom:dotted 1px #ccc;">
			<i class="icon-file"></i>
			Table of Processes
		</div>
		<pre id="topDiv">
		</pre>
	</div>
</div>
<div class="container">
	<div id="systemLoadAverageDiv" class="division" style="width:33%;">
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
	<div id="memoryUsageDiv" class="division" style="width:33%;">
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
				<td class="text-right"><label data-juice="Label" data-juice-bind="heapMemoryUsage.init" data-juice-format="number:0,0"></label>bytes</td>
				<td class="text-right"><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.init" data-juice-format="number:0,0"></label>bytes</td>
			</tr>
			<tr>
				<th>used</th>
				<td class="text-right"><label data-juice="Label" data-juice-bind="heapMemoryUsage.used" data-juice-format="number:0,0"></label>bytes</td>
				<td class="text-right"><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.used" data-juice-format="number:0,0"></label>bytes</td>
			</tr>
			<tr>
				<th>committed</th>
				<td class="text-right"><label data-juice="Label" data-juice-bind="heapMemoryUsage.committed" data-juice-format="number:0,0"></label>bytes</td>
				<td class="text-right"><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.committed" data-juice-format="number:0,0"></label>bytes</td>
			</tr>
			<tr>
				<th>max</th>
				<td class="text-right"><label data-juice="Label" data-juice-bind="heapMemoryUsage.max" data-juice-format="number:0,0"></label>bytes</td>
				<td class="text-right"><label data-juice="Label" data-juice-bind="nonHeapMemoryUsage.max" data-juice-format="number:0,0"></label>bytes</td>
			</tr>
		</table>
	</div>
	<div id="classCountDiv" class="division" style="width:33%;">
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
				<td class="text-right"><label data-juice="Label" data-juice-bind="classInfo.totalLoadedClassCount" data-juice-format="number:0,0"></label></td>
			</tr>
			<tr>
				<th>loadedClassCount</th>
				<td class="text-right"><label data-juice="Label" data-juice-bind="classInfo.loadedClassCount" data-juice-format="number:0,0"></label></td>
			</tr>
			<tr>
				<th>unloadedClassCount</th>
				<td class="text-right"><label data-juice="Label" data-juice-bind="classInfo.unloadedClassCount" data-juice-format="number:0,0"></label></td>
			</tr>
		</table>
	</div>
</div>
<div class="container">
	<div id="threadInfosDiv" class="division" style="width:100%;">
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
					<td class="text-right"><label data-juice="Label" data-juice-bind="threadInfo.waitedCount" data-juice-format="number:0,0"></label></td>
					<td class="text-right"><label data-juice="Label" data-juice-bind="threadInfo.waitedTime" data-juice-format="number:0,0"></label>m</td>
					<td class="text-right"><label data-juice="Label" data-juice-bind="threadInfo.blockCount" data-juice-format="number:0,0"></label></td>
					<td class="text-right"><label data-juice="Label" data-juice-bind="threadInfo.blockTime" data-juice-format="number:0,0"></label>m</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>