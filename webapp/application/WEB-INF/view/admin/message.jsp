<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!-- global -->
<script type="text/javascript">
var messageSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
messageSearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var messageSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var messages = new juice.data.List();
var message = new juice.data.Map();
message.setReadonly('id', true);
message.setEnable(false);

/**
 * On document loaded
 */
$( document ).ready(function() {
	getMessages();
});

/**
 * Gets message list
 */
function getMessages(page) {
	if(page){
		messageSearch.set('page',page);
	}
	$.ajax({
		 url: 'message/getMessages'
		,type: 'GET'
		,data: messageSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			messages.fromJson(data);
			messageSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#messagesTable').hide().fadeIn();
			
			//  find current row index.			
			var index = messages.indexOf(function(row){
				return row.get('id') == message.get('id');
			});
			messages.setIndex(index);
   	 	}
	});	
}

/**
 * Gets message
 */
function getMessage(id) {
	$.ajax({
		 url: 'message/getMessage'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			message.setEnable(true);
			message.fromJson(data);
			$('#messageTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds message
 */
function addMessage() {
	
	<spring:message code="application.text.id" var="item"/>
	new juice.ui.Prompt('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.beforeConfirm(function(event){
			var id = event.value;
	
			// checks duplicated id.
			var isDuplicated = false;
			$.ajax({
				 url: 'message/getMessage'
				,type: 'GET'
				,data: {id: id}
				,async: false
				,success: function(data, textStatus, jqXHR) {
					if(data != null && data.id == id){
						if(data != null && data.id == event.value){
							isDuplicated = true;
						}
					}
		 	 	}
			});
			if(isDuplicated == true){
				<spring:message code="application.text.id" var="item"/>
				new juice.ui.Alert('<spring:message code="application.message.duplicatedItem" arguments="${item}"/>').open();
				return false;
			}
		})
		.afterConfirm(function(event){
			var id = event.value;
			messages.clearIndex();
			message.fromJson({});
			message.set('id', id);
			message.setEnable(true);
		})
		.open();
}

/**
 * Saves message.
 */
function saveMessage() {
	
	// Checks validation of message
	if(juice.util.validator.isEmpty(message.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	if(juice.util.validator.isEmpty(message.get('value'))){
		<spring:message code="application.text.value" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Saves message
	<spring:message code="application.text.message" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
		.afterConfirm(function() {
			var messageJson = message.toJson();
			$.ajax({
				 url: 'message/saveMessage'
				,type: 'POST'
				,data: JSON.stringify(messageJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<spring:message code="application.text.message" var="item"/>
					new juice.ui.Alert('<spring:message code="application.message.saveItem.complete" arguments="${item}"/>')
						.afterConfirm(function(){
							getMessage(message.get('id'));
							getMessages();
						}).open();
			 	}
			});	
		}).open();
}

/**
 * Removes message
 */
function removeMessage() {

	// Checks system data
	if(message.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes message
	<spring:message code="application.text.message" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: 'message/removeMessage'
			,type: 'GET'
			,data: { id: message.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.message" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					message.fromJson({});
					message.setEnable(false);
					getMessages();
				}).open();
	  	 	}
		});	
	}).open();
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<i class="icon-key"></i>
	<spring:message code="application.text.message"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Messages											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<div class="title2">
					<i class="icon-search"></i>
				</div>
				<select data-juice="ComboBox" data-juice-bind="messageSearch.key" data-juice-options="messageSearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="messageSearch.value" style="width:100px;"/>
				<button onclick="javascript:getMessages();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addMessage();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="messagesTable" data-juice="Grid" data-juice-bind="messages" data-juice-item="message">
			<colgroup>
				<col style="width:10%"/>
				<col style="width:30%"/>
				<col style="width:60%"/>
			</colgroup>
			<thead>
				<tr>
					<th>
						<spring:message code="application.text.no"/>
					</th>
					<th>
						<spring:message code="application.text.message"/>
						<spring:message code="application.text.id"/>
					</th>
					<th>
						<spring:message code="application.text.message"/>
						<spring:message code="application.text.name"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.message.get('id')}}" onclick="javascript:getMessage(this.dataset.id);">
					<td class="text-center">
						{{messageSearch.get('rows')*(messageSearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="{{$context.message.get('systemDataYn')=='Y'?'systemData':''}}">
						<label data-juice="Label" data-juice-bind="message.id" class="id"></label>
					</td>
					<td><label data-juice="Label" data-juice-bind="message.name"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="messageSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getMessages(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Message Details										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-key"></i>
					<spring:message code="application.text.message"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveMessage();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:removeMessage();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="messageTable" class="detail">
			<colgroup>
				<col style="width:30%;">
				<col style="width:70%;">
			</colgroup>
			<tr>
				<th>
					<i class="icon-attention"></i>
					<spring:message code="application.text.id"/>
				</th>
				<td>
					
					<input class="id" data-juice="TextField" data-juice-bind="message.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="message.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.value"/>
					</span>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="message.value"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="message.description"></textarea>
				</td>
			</tr>
		</table>
	</div>
</div>
