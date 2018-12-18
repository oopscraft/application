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
var aclSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
aclSearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var aclSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var acls = new juice.data.List();
var acl = new juice.data.Map();
acl.setReadonly('id', true);
acl.setEnable(false);

/**
 * On document loaded
 */
$( document ).ready(function() {
	getAcls();
});

/**
 * Gets ACL list
 */
function getAcls(page) {
	if(page){
		aclSearch.set('page',page);
	}
	$.ajax({
		 url: 'security/getAcls'
		,type: 'GET'
		,data: aclSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			acls.fromJson(data);
			aclSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#aclsTable').hide().fadeIn();
			
			//  find current row index.			
			var index = acls.indexOf(function(row){
				return row.get('id') == acl.get('id');
			});
			acls.setIndex(index);
   	 	}
	});	
}

/**
 * Gets ACL info
 */
function getAcl(uri, method) {
	$.ajax({
		 url: 'authority/getAcl'
		,type: 'GET'
		,data: { uri:uri, method:method }
		,success: function(data, textStatus, jqXHR) {
			acl.setEnable(true);
			acl.fromJson(data);
			$('#aclTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds authority
 */
function addAcl() {
	alert('addAcl');	
}

/**
 * Saves authority.
 */
function saveAcl() {
/*
	// Checks validation of authority
	if(juice.util.validator.isEmpty(authority.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
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
					<spring:message code="application.text.authority" var="item"/>
					var message = '<spring:message code="application.message.saveItem.complete" arguments="${item}"/>';
					new juice.ui.Alert(message)
						.afterConfirm(function(){
							getAuthority(authority.get('id'));
							getAuthorities();
						}).open();
			 	}
			});	
		}).open();
*/
}

/**
 * Removes authority
 */
function removeAcl() {
/*
	// Checks system data
	if(authority.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes authority
	<spring:message code="application.text.authority" var="item"/>
	var message = '<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'authority/removeAuthority'
			,type: 'GET'
			,data: { id: authority.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.authority" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					authority.fromJson({});
					authority.setEnable(false);
					getAuthorities();
				}).open();
	  	 	}
		});	
	}).open();
*/
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<i class="icon-key"></i>
	<spring:message code="application.text.authority"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Authorities											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<div class="title2">
					<i class="icon-search"></i>
				</div>
				<select data-juice="ComboBox" data-juice-bind="aclSearch.key" data-juice-options="aclSearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="aclSearch.value" style="width:100px;"/>
				<button onclick="javascript:getAuthorities();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addAuthority();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="aclsTable" data-juice="Grid" data-juice-bind="acls" data-juice-item="acl">
			<colgroup>
				<col style="width:10%"/>
				<col style="width:40%"/>
				<col style="width:10%"/>
				<col style="width:10%"/>
			</colgroup>
			<thead>
				<tr>
					<th>
						<spring:message code="application.text.no"/>
					</th>
					<th>
						<spring:message code="application.text.uri"/>
					</th>
					<th>
						<spring:message code="application.text.method"/>
					</th>
					<th>
						<spring:message code="application.text.accessPolicy"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-uri="{{$context.acl.get('uri')}}" data-method="{{$context.acl.get('method')}}" onclick="javascript:getAcl(this.dataset.uri, this.dataset.method);">
					<td class="text-center {{$context.acl.get('systemDataYn')=='Y'?'systemData':''}}">
						{{aclSearch.get('rows')*(aclSearch.get('page')-1)+$context.index+1}}
					</td>
					<td>
						<label data-juice="Label" data-juice-bind="acl.uri" class="id code"></label>
					</td>
					<td>
						<label data-juice="Label" data-juice-bind="acl.method" class="id code"></label>
					</td>
					<td>
						<label data-juice="Label" data-juice-bind="acl.accessPolicy"></label>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="aclSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getAcls(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- ACL Details										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-key"></i>
					<spring:message code="application.text.acl"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveAcl();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:removeAcl();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="aclTable" class="detail">
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
					<input class="id" data-juice="TextField" data-juice-bind="acl.uri"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="acl.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="acl.description"></textarea>
				</td>
			</tr>
		</table>
	</div>
</div>
