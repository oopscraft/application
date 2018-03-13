<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- global -->
<script type="text/javascript">

/* Data Structure */
var threadInfoList = new juice.data.List();

/* Chart Elements */
var osInfoChart = null;
var memInfoChart = null;
var classInfoChart = null;
var diskInfoChart = null;

/**
 * Document on load
 */
$( document ).ready(function() {
	// CPU Usage Chart
	osInfoChart = new Chart(
		 document.getElementById("osInfoChart").getContext("2d")
		 ,{
	          type: 'line'
	         ,data: {
	        	  labels: new Array(10)
	             ,datasets: [{
	                 label: "System Load Average"
	 		   		,fillColor: "rgba(252,147,65,0.5)"
	 				,strokeColor: "rgba(255,255,255,1)"
	 				,borderWidth:0.1
	 				,borderColor:"steelBlue"
	 				,lineTension:0.2
	 				,radius: 0
	                ,data: new Array(10)
	             }]
	         }
	         ,options: {
	             responsive: false,
	             animation: {
	            	 duration: 0
	             },
	             scales: {
                    xAxes: [{
                        display: true,
                        scaleLabel: {
                            display: false
                        }
                    }],
	                 yAxes: [{
	                     display: true,
	                     ticks: {
	                          max: 100
	                         ,min: 0
	                         ,stepSize: 20
	                   		 ,callback: function(value, index, values) {
	                    		 return value + ' %';
	                    	  }
	                     }
	                 }]
	             }
	         }
	     }
	);
	
	// Memory Usage Chart
	memInfoChart = new Chart(
		 document.getElementById("memInfoChart").getContext("2d")
		 ,{
	          type: 'line'
	         ,data: {
	        	  labels: new Array(10)
	             ,datasets: [{
	                 label: "Heap"
	 		   		,fillColor: "rgba(252,147,65,0.5)"
	 				,strokeColor: "rgba(255,255,255,1)"
	 				,borderWidth:0.1
	 				,borderColor:"steelBlue"
	 				,lineTension:0.2
	 				,radius: 0
	                ,data: new Array(10)
	             }
	             ,{
	                 label: "None Heap"
	 		   		,fillColor: "rgba(252,147,65,0.5)"
	 				,strokeColor: "rgba(255,255,255,1)"
	 				,borderWidth:0.1
	 				,borderColor:"steelBlue"
	 				,lineTension:0.2
	 				,radius: 0
	                ,data: new Array(10)
	             }]
	         }
	         ,options: {
	             responsive: false,
	             animation: {
	            	 duration: 0
	             },
	             scales: {
                    xAxes: [{
                        display: true,
                        scaleLabel: {
                            display: false
                        }
                    }],
	                 yAxes: [{
	                     display: true,
	                     ticks: {
	                          max: 1000
	                         ,min: 0
	                         ,stepSize: 200
	                   		 ,callback: function(value, index, values) {
	                    		 return value + ' MB';
	                    	  }
	                     }
	                 }]
	             }
	         }
	     }
	);
	
	// Disk Info Chart
	diskInfoChart = new Chart(
		 document.getElementById("diskInfoChart").getContext("2d")
		 ,{
	          type: 'pie'
	         ,data: {
	             datasets: [{
	                 label: "Heap Memory"
	 		   		,backgroundColor: ['#eeeeee', '#cccccc']
	                ,data: [100,0]
	             }]
	         }
	         ,options: {
	             responsive: false
	         }
	     }
	);
	
	// Class Info Chart
	classInfoChart = new Chart(
		 document.getElementById("classInfoChart").getContext("2d")
		 ,{
	          type: 'pie'
	         ,data: {
	             datasets: [{
	                 label: "Heap Memory"
	 		   		,backgroundColor: ['#eeeeee', '#cccccc']
	                ,data: [100,0]
	             }]
	         }
	         ,options: {
	             responsive: false
	         }
	     }
	);
});

var wsProtocol = null;
if(window.location.protocol === 'https:'){
	wsProtocol = 'wss:';
}else{
	wsProtocol = 'ws:';
}
var webSocketClient = new juice.util.WebSocketClient(wsProtocol + '//' + location.host + '/application/console/webSocket');
/**
 * Creates web socket client
 */
 webSocketClient.onMessage(function(e){
	var dataJson = JSON.parse(e.data);
	console.log(dataJson);
	
	if(dataJson.id == 'jmxInfo'){
		var result = dataJson.result;
		
//		printOsInfo(result.osInfo);
//		printMemInfo(result.memInfo);
//		printDiskInfo(result.diskInfo);
//		printClassInfo(result.classInfo);
		threadInfoList.fromJson(result.threadInfoList);
	}
});
webSocketClient.open();

/**
 * printOsInfo
 */
function printOsInfo(osInfo){
	
	// updates chart
	osInfoChart.data.datasets[0].data.shift();
	osInfoChart.data.datasets[0].data.push(osInfo.systemLoadAverage * 100);
	osInfoChart.update();
	
	// updates table
	var osInfoTable = new JsonTable(osInfo);
	var osInfoContainer = $('#osInfoContainer');
	osInfoContainer.empty();
	osInfoTable.draw(osInfoContainer,'table table-bordered');
}

/**
 * printMemInfo
 */
function printMemInfo(memInfo) {
	
	// updates chart
	memInfo.heapMemoryUsage.init = (memInfo.heapMemoryUsage.init/1024/1024).toFixed(2);
	memInfo.heapMemoryUsage.used = (memInfo.heapMemoryUsage.used/1024/1024).toFixed(2);
	memInfo.heapMemoryUsage.committed = (memInfo.heapMemoryUsage.committed/1024/1024).toFixed(2);
	memInfo.heapMemoryUsage.max = (memInfo.heapMemoryUsage.max/1024/1024).toFixed(2);
	memInfo.nonHeapMemoryUsage.init = (memInfo.nonHeapMemoryUsage.init/1024/1024).toFixed(2);
	memInfo.nonHeapMemoryUsage.used = (memInfo.nonHeapMemoryUsage.used/1024/1024).toFixed(2);
	memInfo.nonHeapMemoryUsage.committed = (memInfo.nonHeapMemoryUsage.committed/1024/1024).toFixed(2);
	memInfo.nonHeapMemoryUsage.max = (memInfo.nonHeapMemoryUsage.max/1024/1024).toFixed(2);
	memInfoChart.data.datasets[0].data.shift();
	memInfoChart.data.datasets[0].data.push(memInfo.heapMemoryUsage.used);
	memInfoChart.data.datasets[1].data.shift();
	memInfoChart.data.datasets[1].data.push(memInfo.nonHeapMemoryUsage.used);
	memInfoChart.update();
	
	// update table
	memInfo.heapMemoryUsage.init = new Number(memInfo.heapMemoryUsage.init).format() + ' MB';
	memInfo.heapMemoryUsage.used = new Number(memInfo.heapMemoryUsage.used).format() + ' MB';
	memInfo.heapMemoryUsage.committed = new Number(memInfo.heapMemoryUsage.committed).format() + ' MB';
	memInfo.heapMemoryUsage.max = new Number(memInfo.heapMemoryUsage.max).format() + ' MB';
	memInfo.nonHeapMemoryUsage.init = new Number(memInfo.nonHeapMemoryUsage.init).format() + ' MB';
	memInfo.nonHeapMemoryUsage.used = new Number(memInfo.nonHeapMemoryUsage.used).format() + ' MB';
	memInfo.nonHeapMemoryUsage.committed = new Number(memInfo.nonHeapMemoryUsage.committed).format() + ' MB';
	memInfo.nonHeapMemoryUsage.max = new Number(memInfo.nonHeapMemoryUsage.max).format() + ' MB';
	var memInfoTable = new JsonTable(memInfo);
	var memInfoContainer = $('#memInfoContainer');
	memInfoContainer.empty();
	memInfoTable.draw(memInfoContainer,'table table-bordered');

}

/**
 * printDiskInfo
 */
function printDiskInfo(diskInfo){
	
	// updates chart
	diskInfo.totalSpace = (diskInfo.totalSpace/1024/1024).toFixed(2);
	diskInfo.freeSpace = (diskInfo.freeSpace/1024/1024).toFixed(2);
	diskInfo.usableSpace = (diskInfo.usableSpace/1024/1024).toFixed(2);
	diskInfoChart.data.datasets[0].data[0] = diskInfo.freeSpace;
	diskInfoChart.data.datasets[0].data[1] = diskInfo.totalSpace - diskInfo.freeSpace;
	diskInfoChart.update();
	
	// updates table
	diskInfo.totalSpace = new Number(diskInfo.totalSpace).format() + ' MB';
	diskInfo.freeSpace = new Number(diskInfo.freeSpace).format() + ' MB';
	diskInfo.usableSpace = new Number(diskInfo.usableSpace).format() + ' MB';
	var diskInfoTable = new JsonTable(diskInfo);
	var diskInfoContainer = $('#diskInfoContainer');
	diskInfoContainer.empty();
	diskInfoTable.draw(diskInfoContainer,'table table-bordered');	
}

/**
 * printClassInfo
 */
function printClassInfo(classInfo){
	
	// updates chart
	classInfoChart.data.datasets[0].data[0] = classInfo.unloadedClassCount;
	classInfoChart.data.datasets[0].data[1] = classInfo.loadedClassCount;
	classInfoChart.update();
	
	// updates table
	classInfo.totalLoadedClassCount = new Number(classInfo.totalLoadedClassCount).format();
	classInfo.loadedClassCount = new Number(classInfo.loadedClassCount).format();
	classInfo.unloadedClassCount = new Number(classInfo.unloadedClassCount).format();
	var classInfoTable = new JsonTable(classInfo);
	var classInfoContainer = $('#classInfoContainer');
	classInfoContainer.empty();
	classInfoTable.draw(classInfoContainer,'table table-bordered');	
}

/**
 * printThreadInfoList
 */
function printThreadInfoList(threadInfoList){
	
	threadInfoList.fromJson(threadInfoList);
	
	var threadInfoListTable = new JsonTable(threadInfoList);
	var threadInfoListContainer = $('#threadInfoListContainer');
	
	
	
	threadInfoListContainer.empty();
	threadInfoListTable.draw(threadInfoListContainer,'table table-bordered');	
}

</script>
<style type="text/css">
</style>
<h1>
	Appliation Monitor
	<small>Application Monitor with JMX</small>
</h1>
<div class="container-fluid">
	<div class="row">
		<div class="col">
			<h2>
				<i class="fa fa-microchip" aria-hidden="true"></i>
				OS Information
			</h2>
			<canvas id="osInfoChart" width="300" height="150"></canvas>
			<div id="osInfoContainer"></div>
		</div>
		<div class="col">
			<h2>
				<i class="fa fa-battery-half" aria-hidden="true"></i>
				Memory Information
			</h2>
			<canvas id="memInfoChart" width="300" height="150"></canvas>
			<div id="memInfoContainer"></div>
		</div>
		<div class="col">
			<h2>
				<i class="fas fa-hdd"></i>
				Disk Information
			</h2>
			<canvas id="diskInfoChart" width="300" height="150"></canvas>
			<div id="diskInfoContainer"></div>
		</div>
		<div class="col">
			<h2>
				<i class="fa fa-clone" aria-hidden="true"></i>
				Classes Information
			</h2>
			<canvas id="classInfoChart" width="300" height="150"></canvas>
			<div id="classInfoContainer"></div>
		</div>
	</div>
	<div class="row">
		<div class="col">
			<h2>
				<i class="fa fa-th-list" aria-hidden="true"></i>
				Thread Information
			</h2>
			<div id="threadInfoListContainer">
			<table data-juice="Grid" data-juice-bind="threadInfoList" data-juice-item="threadInfo">
				<thead>
					<tr>
						<th>Thread ID</th>
						<th>Thread Name</th>
						<th>Thread State</th>
						<th>Wait Count</th>
						<th>Wait Time</th>
						<th>Block Count</th>
						<th>Block Time</th>
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
	</div>
</div>


