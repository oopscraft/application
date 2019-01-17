<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
var messageSearch = new juice.data.Map({
	 type: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
messageSearch.afterChange(function(event){
	if(event.name == 'type'){
		this.set('value','');
	}
});
var messageSearchTypes = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var messages = new juice.data.List();
var message = new juice.data.Map();
var isNew = false;

/**
 * clear edit
 */
function clearEdit() {
	message.fromJson({});
}

/**
 * enable edit
 */
function enableEdit(editable) {
	if(editable == true){
		message.setEnable(true);
		message.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		message.setEnable(false);
		message.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}


/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
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
			clearEdit();
			message.fromJson(data);
			isNew = false;
			enableEdit(true);
			message.setReadonly('id',true);
			$('#messageTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds message
 */
function addMessage() {
	clearEdit();
	messages.clearIndex();
	enableEdit(true);
	isNew = true;
}

/**
 * Saves message.
 */
function saveMessage() {
	
	// Check id
	try {
		__validator.checkId(message.get('id'));
	}catch(e){
		new juice.ui.Alert(e).open();
		return false;
	}
	
	// check name
	try {
		__validator.checkName(message.get('name'));
	}catch(e){
		new juice.ui.Alert(e).open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = message.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'message/getMessage'
			,type: 'GET'
			,data: {id: id}
			,async: false
			,success: function(data, textStatus, jqXHR) {
				if(data != null && data.id == id){
					isDuplicated = true;
				}
	 	 	}
		});
		if(isDuplicated == true){
			<spring:message code="application.text.id" var="item"/>
			new juice.ui.Alert('<spring:message code="application.message.duplicatedItem" arguments="${item}"/>').open();
			return false;
		}
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
					getMessage(message.get('id'));
					getMessages();
			 	}
			});	
		}).open();
}

/**
 * Removes message
 */
function deleteMessage() {

	// Checks system data
	if(message.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes message
	<spring:message code="application.text.message" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>')
		.afterConfirm(function() {
			$.ajax({
				 url: 'message/deleteMessage'
				,type: 'GET'
				,data: { id: message.get('id') }
				,success: function(data, textStatus, jqXHR) {
					clearEdit();
					enableEdit(false);
					getMessages();
		  	 	}
			});	
		}).open();
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_message.png"/>&nbsp;
	<spring:message code="application.text.message"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<!-- ====================================================== -->
	<!-- Messages												-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-juice="ComboBox" data-juice-bind="messageSearch.type" data-juice-options="messageSearchTypes" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="messageSearch.value" style="width:100px;"/>
				<button onclick="javascript:getMessages();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addMessage();">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="messagesTable" data-juice="Grid" data-juice-bind="messages" data-juice-item="message">
			<colgroup>
				<col style="width:10%"/>
				<col style="width:30%"/>
				<col/>
			</colgroup>
			<thead>
				<tr>
					<th>
						<spring:message code="application.text.no"/>
					</th>
					<th>
						<spring:message code="application.text.id"/>
					</th>
					<th>
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
	<!-- ====================================================== -->
	<!-- Message Details										-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.message"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveMessage();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteMessage();">
					<i class="icon-delete"></i>
					<spring:message code="application.text.delete"/>
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
					<span class="must">
						<spring:message code="application.text.id"/>
					</span>
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
					<spring:message code="application.text.value"/>
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
