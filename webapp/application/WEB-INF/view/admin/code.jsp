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
var codeSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
codeSearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var codeSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var codes = new juice.data.List();
var code = new juice.data.Map();
var items = new juice.data.List();
var isNew = false;

/**
 * clear edit
 */
function clearEdit(){
	code.fromJson({});
	items.fromJson([]);
}

/**
 * enable edit
 */
function enableEdit(enable){
	if(enable == true){
		code.setEnable(true);
		code.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		code.setEnable(false);
		code.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit();
	getCodes();
});

/**
 * Gets code list
 */
function getCodes(page) {
	if(page){
		codeSearch.set('page',page);
	}
	$.ajax({
		 url: 'code/getCodes'
		,type: 'GET'
		,data: codeSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			codes.fromJson(data);
			codeSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#codesTable').hide().fadeIn();
			
			//  find current row index.			
			var index = codes.indexOf(function(row){
				return row.get('id') == code.get('id');
			});
			codes.setIndex(index);
   	 	}
	});	
}

/**
 * Gets code
 */
function getCode(id) {
	$.ajax({
		 url: 'code/getCode'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			clearEdit();
			code.fromJson(data);
			items.fromJson(data.items);
			isNew = false;
			enableEdit(true);
			code.setReadonly('id', true);
			$('#codeTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds code
 */
function addCode() {
	clearEdit();
	enableEdit(true);
	codes.clearIndex();
	isNew = true;
}

/**
 * Saves code.
 */
function saveCode() {
	
	// checks id
	if(__isEmpty(code.get('id'))){
		<spring:message code="application.text.id" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}

	// checks  name
	if(__isEmpty(code.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = code.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'code/getCode'
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
	
	// Saves code
	<spring:message code="application.text.code" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.afterConfirm(function() {
			var codeJson = code.toJson();
			codeJson.items = items.toJson();
			$.ajax({
				 url: 'code/saveCode'
				,type: 'POST'
				,data: JSON.stringify(codeJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getCode(code.get('id'));
					getCodes();
			 	}
			});	
		}).open();
}

/**
 * Removes code
 */
function deleteCode() {

	// Checks system data
	if(code.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes code
	<spring:message code="application.text.code" var="item"/>
	var message = '<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'code/deleteCode'
			,type: 'GET'
			,data: { id: code.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getCodes();
	  	 	}
		});	
	}).open();
}

/**
 * Adds item
 */
function addItem() {
	var item = new juice.data.Map({
		codeId: code.get('id'),
		id: null,
		name: null
	});
	items.addRow(item);
}

/**
 * Moves item up.
 */
function moveItemUp(index){
	var from = parseInt(index);
	var to = from - 1;
	items.moveRow(from, to);
}

/**
 * Moves item down.
 */
function moveItemDown(index) {
	var from = parseInt(index);
	var to = from + 1;
	items.moveRow(from, to);
}

/**
 * Removes item
 */
function removeItem(index){
	items.removeRow(index);
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_code.png"/>&nbsp;
	<spring:message code="application.text.code"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Codes											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-juice="ComboBox" data-juice-bind="codeSearch.key" data-juice-options="codeSearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="codeSearch.value" style="width:100px;"/>
				<button onclick="javascript:getCodes();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addCode();">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="codesTable" data-juice="Grid" data-juice-bind="codes" data-juice-item="code">
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
						<spring:message code="application.text.id"/>
					</th>
					<th>
						<spring:message code="application.text.name"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.code.get('id')}}" onclick="javascript:getCode(this.dataset.id);">
					<td class="text-center">
						{{codeSearch.get('rows')*(codeSearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="{{$context.code.get('systemDataYn')=='Y'?'systemData':''}}">
						<label data-juice="Label" data-juice-bind="code.id" class="id"></label>
					</td>
					<td><label data-juice="Label" data-juice-bind="code.name"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="codeSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getCodes(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<!-- ====================================================== -->
	<!-- Code Details										-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.code"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveCode();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteCode();">
					<i class="icon-delete"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="codeTable" class="detail">
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
					
					<input class="id" data-juice="TextField" data-juice-bind="code.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="code.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="code.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<i class="icon-list"></i>
					<spring:message code="application.text.items"/>
				</th>
				<td>
					<table data-juice="Grid" data-juice-bind="items" data-juice-item="item">
						<colgroup>
							<col style="width:40%;"/>
							<col/>
							<col style="width:5%;"/>
						</colgroup>
						<thead>
							<tr>
								<th>
									<spring:message code="application.text.id"/>
								</th>
								<th>
									<spring:message code="application.text.name"/>
								</th>
								<th style="text-align:right;">
									<button class="small" onclick="javascript:addItem();">
										<i class="icon-add"></i>
									</button>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="{{$context.item.get('id')}}">
								<td>
									<input data-juice="TextField" data-juice-bind="item.id"/>
								</td>
								<td>
									<input data-juice="TextField" data-juice-bind="item.name"/>
								</td>
								<td class="text-center">
									<div style="display:flex;justify-content:center;">
										<button class="small" data-index="{{$context.index}}" onclick="javascript:moveItemUp(this.dataset.index);">
											<i class="icon-up"></i>
										</button>
										<button class="small" data-index="{{$context.index}}" onclick="javascript:moveItemDown(this.dataset.index);">
											<i class="icon-down"></i>
										</button>
										<button class="small" data-index="{{$context.index}}" onclick="javascript:removeItem(this.dataset.index);">
											<i class="icon-remove"></i>
										</button>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
