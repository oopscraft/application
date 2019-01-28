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
var authoritySearch = new juice.data.Map({
	 rows: 30
	,page: 1
	,searchType: null
	,searchValue: null
	,totalCount:-1
});
var authoritySearchTypes = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'ID', text:'<spring:message code="application.text.id"/>' }
	,{ value:'NAME', text:'<spring:message code="application.text.name"/>' }
];
var authorities = new juice.data.List();
var authority = new juice.data.Map();
var isNew = false;

/**
 * clear edit
 */
function clearEdit() {
	authority.fromJson({});
}

/**
 * enable edit
 */
function enableEdit(editable) {
	if(editable == true){
		authority.setEnable(true);
		authority.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		authority.setEnable(false);
		authority.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
	getAuthorities();
});

/**
 * Gets authority list
 */
function getAuthorities(page) {
	if(page){
		authoritySearch.set('page',page);
	}
	$.ajax({
		 url: 'authority/getAuthorities'
		,type: 'GET'
		,data: authoritySearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			authorities.fromJson(data);
			authoritySearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#authoritiesTable').hide().fadeIn();
			
			//  find current row index.			
			var index = authorities.indexOf(function(row){
				return row.get('id') == authority.get('id');
			});
			authorities.setIndex(index);
   	 	}
	});	
}

/**
 * Gets authority
 */
function getAuthority(id) {
	$.ajax({
		 url: 'authority/getAuthority'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			clearEdit();
			authority.fromJson(data);
			isNew = false;
			enableEdit(true);
			authority.setReadonly('id',true);
			$('#authorityTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds authority
 */
function addAuthority() {
	clearEdit();
	authorities.clearIndex();
	enableEdit(true);
	isNew = true;
}

/**
 * Saves authority.
 */
function saveAuthority() {
	
	// Check id
	try {
		__validator.checkId(authority.get('id'));
	}catch(e){
		new juice.ui.Alert(e).open();
		return false;
	}
	
	// check name
	try {
		__validator.checkName(authority.get('name'));
	}catch(e){
		new juice.ui.Alert(e).open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = authority.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'authority/getAuthority'
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
	
	// Saves authority
	<spring:message code="application.text.authority" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.afterConfirm(function() {
			var authorityJson = authority.toJson();
			$.ajax({
				 url: 'authority/saveAuthority'
				,type: 'POST'
				,data: JSON.stringify(authorityJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getAuthority(authority.get('id'));
					getAuthorities();
			 	}
			});	
		}).open();
}

/**
 * Deletes authority
 */
function deleteAuthority() {

	// Checks system data
	if(authority.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes authority
	<spring:message code="application.text.authority" var="item"/>
	var message = '<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'authority/deleteAuthority'
			,type: 'GET'
			,data: { id: authority.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getAuthorities();
	  	 	}
		});	
	}).open();
}

</script>
<style type="text/css">
</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_authority.png"/>&nbsp;
	<spring:message code="application.text.authority"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<!-- ====================================================== -->
	<!-- Authorities											-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-juice="ComboBox" data-juice-bind="authoritySearch.searchType" data-juice-options="authoritySearchTypes" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="authoritySearch.searchValue" style="width:100px;"/>
				<button onclick="javascript:getAuthorities();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addAuthority();">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="authoritiesTable" data-juice="Grid" data-juice-bind="authorities" data-juice-item="authority">
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
				<tr data-id="{{$context.authority.get('id')}}" onclick="javascript:getAuthority(this.dataset.id);">
					<td class="text-center">
						{{authoritySearch.get('rows')*(authoritySearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="{{$context.authority.get('systemDataYn')=='Y'?'systemData':''}}">
						<label data-juice="Label" data-juice-bind="authority.id" class="id"></label>
					</td>
					<td><label data-juice="Label" data-juice-bind="authority.name"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="authoritySearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getAuthorities(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<!-- ====================================================== -->
	<!-- Authority Details										-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.authority"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveAuthority();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteAuthority();">
					<i class="icon-delete"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="authorityTable" class="detail">
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
					<input class="id" data-juice="TextField" data-juice-bind="authority.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="authority.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="authority.description"></textarea>
				</td>
			</tr>
		</table>
	</div>
</div>
