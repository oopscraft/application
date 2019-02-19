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
var osInfo = new duice.data.Map();
var heapMemoryUsage = new duice.data.Map();
var nonHeapMemoryUsage = new duice.data.Map();
var classInfo = new duice.data.Map();
var threadInfos = new duice.data.List();

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
			printTop(lastMonitorInfo.top);
			
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
			printTop(monitorInfo.top);

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
 * Prints TOP contents.
 */
function printTop(top) {
	$('#topDiv').html(top);
}

/**
 * Draws systemLoadAverageChart
 */
function drawSystemLoadAverageChart(monitorInfos){
	systemLoadAverageChart = new Chart(
		document.getElementById('systemLoadAverageChart').getContext('2d'),{
		    type: 'line',
		    data:  {
				spans: [],
				datasets: [{
					span: 'System Load Average',
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
	
	systemLoadAverageChart.data.spans = [];
	systemLoadAverageChart.data.datasets[0].data = [];
	monitorInfos.forEach(function(monitorInfo){
		systemLoadAverageChart.data.spans.push(moment(monitorInfo.date).format('mm:ss'));
		systemLoadAverageChart.data.datasets[0].data.push(monitorInfo.osInfo.systemLoadAverage);
	});
	systemLoadAverageChart.update();
}

/**
 * Updates systemLoadAverageChart
 */
function updateSystemLoadAverageChart(monitorInfo){
	systemLoadAverageChart.data.spans.shift();
	systemLoadAverageChart.data.datasets[0].data.shift();
	systemLoadAverageChart.data.spans.push(moment(monitorInfo.date).format('mm:ss'));
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
				spans: [],
				datasets: [{
					span: 'Heap Size',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'red',
					pointStyle: 'crossRot',
				},{
					span: 'Heap Usage',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'steelblue',
					pointStyle: 'crossRot',
				},{
					span: 'None Heap Usage',
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
	memoryUsageChart.data.spans = [];
	memoryUsageChart.data.datasets[0].data = [];
	monitorInfos.forEach(function(monitorInfo){
		memoryUsageChart.data.spans.push(moment(monitorInfo.date).format('mm:ss'));
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
	memoryUsageChart.data.spans.shift();
	memoryUsageChart.data.datasets[0].data.shift();
	memoryUsageChart.data.datasets[1].data.shift();
	memoryUsageChart.data.datasets[2].data.shift();
	memoryUsageChart.data.spans.push(moment(monitorInfo.date).format('mm:ss'));
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
				spans: [],
				datasets: [{
					span: 'Loaded Class',
					data: [],
					fill: false,
					borderWidth: 1,
					borderColor: 'red',
					pointStyle: 'crossRot',
				},{
					span: 'Unload Class',
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
	classCountChart.data.spans = [];
	classCountChart.data.datasets[0].data = [];
	classCountChart.data.datasets[1].data = [];
	monitorInfos.forEach(function(monitorInfo){
		classCountChart.data.spans.push(moment(monitorInfo.date).format('mm:ss'));
		classCountChart.data.datasets[0].data.push(monitorInfo.classInfo.loadedClassCount);
		classCountChart.data.datasets[1].data.push(monitorInfo.classInfo.unloadedClassCount);
	});
	classCountChart.update();
}

/**
 * Updates classCountChart
 */
function updateClassCountChart(monitorInfo){
	classCountChart.data.spans.shift();
	classCountChart.data.datasets[0].data.shift();
	classCountChart.data.datasets[1].data.shift();
	classCountChart.data.spans.push(moment(monitorInfo.date).format('mm:ss'));
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
	width: 100%;
	height: 20em;
	font-size:1em;
    padding: 0rem 1rem;
    overflow: scroll;
	background-color: black;
	color: white;
	font-family: Consolas, Courier New;
	white-space: pre-line;
}
</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_monitor.png"/>&nbsp;
	<spring:message code="application.text.monitor"/>
</div>
<div class="container">
	<div id="threadInfosDiv" class="division" style="width:100%;">
		<div class="title2" style="width:100%;border-bottom:dotted 1px #ccc;">
			<i class="icon-file"></i>
			Table of Processes
		</div>
		<br/>
		<div id="topDiv">
		</div>
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
				<td><span data-duice="Text" data-duice-bind="osInfo.name"></span></td>
			</tr>
			<tr>
				<th>version</th>
				<td><span data-duice="Text" data-duice-bind="osInfo.version"></span></td>
			</tr>
			<tr>
				<th>arch</th>
				<td><span data-duice="Text" data-duice-bind="osInfo.arch"></span></td>
			</tr>
			<tr>
				<th>availableProcessors</th>
				<td><span data-duice="Text" data-duice-bind="osInfo.availableProcessors"></span></td>
			</tr>
			<tr>
				<th>systemLoadAverage</th>
				<td><span data-duice="Text" data-duice-bind="osInfo.systemLoadAverage"></span></td>
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
				<td class="text-right"><span data-duice="Text" data-duice-bind="heapMemoryUsage.init" data-duice-format="number:0"></span>bytes</td>
				<td class="text-right"><span data-duice="Text" data-duice-bind="nonHeapMemoryUsage.init" data-duice-format="number:0"></span>bytes</td>
			</tr>
			<tr>
				<th>used</th>
				<td class="text-right"><span data-duice="Text" data-duice-bind="heapMemoryUsage.used" data-duice-format="number:0"></span>bytes</td>
				<td class="text-right"><span data-duice="Text" data-duice-bind="nonHeapMemoryUsage.used" data-duice-format="number:0"></span>bytes</td>
			</tr>
			<tr>
				<th>committed</th>
				<td class="text-right"><span data-duice="Text" data-duice-bind="heapMemoryUsage.committed" data-duice-format="number:0"></span>bytes</td>
				<td class="text-right"><span data-duice="Text" data-duice-bind="nonHeapMemoryUsage.committed" data-duice-format="number:0"></span>bytes</td>
			</tr>
			<tr>
				<th>max</th>
				<td class="text-right"><span data-duice="Text" data-duice-bind="heapMemoryUsage.max" data-duice-format="number:0"></span>bytes</td>
				<td class="text-right"><span data-duice="Text" data-duice-bind="nonHeapMemoryUsage.max" data-duice-format="number:0"></span>bytes</td>
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
				<td class="text-right"><span data-duice="Text" data-duice-bind="classInfo.totalLoadedClassCount" data-duice-format="number:0"></span></td>
			</tr>
			<tr>
				<th>loadedClassCount</th>
				<td class="text-right"><span data-duice="Text" data-duice-bind="classInfo.loadedClassCount" data-duice-format="number:0"></span></td>
			</tr>
			<tr>
				<th>unloadedClassCount</th>
				<td class="text-right"><span data-duice="Text" data-duice-bind="classInfo.unloadedClassCount" data-duice-format="number:0"></span></td>
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
		<table id="threadInfosTable" data-duice="Grid" data-duice-bind="threadInfos" data-duice-item="threadInfo">
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
					<td class="text-center"><span data-duice="Text" data-duice-bind="threadInfo.threadId"></span></td>
					<td><span data-duice="Text" data-duice-bind="threadInfo.threadName"></span></td>
					<td><span data-duice="Text" data-duice-bind="threadInfo.threadState"></span></td>
					<td class="text-right"><span data-duice="Text" data-duice-bind="threadInfo.waitedCount" data-duice-format="number:0"></span></td>
					<td class="text-right"><span data-duice="Text" data-duice-bind="threadInfo.waitedTime" data-duice-format="number:0"></span>m</td>
					<td class="text-right"><span data-duice="Text" data-duice-bind="threadInfo.blockCount" data-duice-format="number:0"></span></td>
					<td class="text-right"><span data-duice="Text" data-duice-bind="threadInfo.blockTime" data-duice-format="number:0"></span>m</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>