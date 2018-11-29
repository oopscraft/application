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
	 key: null
	,value: null
	,page: 1
	,rows: 20
	,totalCount:-1
});
authoritySearch.onChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var authoritySearchKeys = [
	 { value:'', text:'- <spring:message code="text.all"/> -' }
	,{ value:'id', text:'<spring:message code="text.id"/>' }
	,{ value:'name', text:'<spring:message code="text.name"/>' }
];
var authorities = new juice.data.List();
var authority = new juice.data.Map();
authority.onChange(function(event){
	// Checks ID is unique.
	if(event.name == 'id') {
		var valid = true;
		$.ajax({
			 url: 'authority/getAuthority'
			,type: 'GET'
			,data: {id:event.value}
			,async: false
			,success: function(data, textStatus, jqXHR) {
				var authorityJson = JSON.parse(data);
				if(authorityJson != null && authorityJson.id == event.value){
					new juice.ui.Alert('<spring:message code="message.duplicatedId"/>').open();
					valid = false;
				}
	 	 	}
		});
		return valid;
	}
});

/**
 * On document loaded
 */
$( document ).ready(function() {
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
			authorities.fromJson(JSON.parse(data));
			authoritySearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#authoritiesTable > tbody').hide().fadeIn();
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
			var authorityJson = JSON.parse(data);
			authority.fromJson(authorityJson);
			authority.setReadOnly('id',true);
			$('#authorityTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds authority
 */
function addAuthority() {
	authorities.clearIndex();
	authority.fromJson({});
	authority.setReadOnly('id',false);
}

/**
 * Saves authority.
 */
function saveAuthority() {
	<c:set var="item"><spring:message code="text.authority"/></c:set>
	var message = '<spring:message code="message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.onConfirm(function() {
			var authorityJson = authority.toJson();
			$.ajax({
				 url: 'authority/saveAuthority'
				,type: 'POST'
				,data: JSON.stringify(authorityJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<c:set var="item"><spring:message code="text.authority"/></c:set>
					var message = '<spring:message code="message.saveItem.complete" arguments="${item}"/>';
					new juice.ui.Alert(message)
						.onConfirm(function(){
							getAuthority(authority.get('id'));
							getAuthorities();
						}).open();
			 	}
			});	
		}).open();
}

/**
 * Removes authority
 */
function removeAuthority() {
	<c:set var="item"><spring:message code="text.authority"/></c:set>
	var message = '<spring:message code="message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.onConfirm(function() {
		$.ajax({
			 url: 'authority/removeAuthority'
			,type: 'GET'
			,data: { id: authority.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<c:set var="item"><spring:message code="text.authority"/></c:set>
				var message = '<spring:message code="message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.onConfirm(function(){
					getAuthorities();
				}).open();
	  	 	}
		});	
	}).open();
}

</script>
<style type="text/css">
.container {
	display: flex;
	justify-content: space-between;
}
.left {
	width: 50%;
}
.right {
	width: 50%;
}
</style>
<div class="title1">
	<i class="icon-key"></i>
	<spring:message code="text.authority"/>
	<spring:message code="text.management"/>
</div>
<hr/>
<div class="container">
	<div class="left">
		<!-- ====================================================== -->
		<!-- Authorities											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<div class="title2">
					<i class="icon-search"></i>
				</div>
				<select data-juice="ComboBox" data-juice-bind="authoritySearch.key" data-juice-options="authoritySearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="authoritySearch.value" style="width:100px;"/>
			</div>
			<div>
				<button onclick="javascript:getAuthorities();">
					<i class="icon-search"></i>
					<spring:message code="text.search"/>
				</button>
			</div>
		</div>
		<table id="authoritiesTable" data-juice="Grid" data-juice-bind="authorities" data-juice-item="authority">
			<thead>
				<tr>
					<th>
						<spring:message code="text.no"/>
					</th>
					<th>
						<spring:message code="text.authority"/>
						<spring:message code="text.id"/>
					</th>
					<th>
						<spring:message code="text.authority"/>
						<spring:message code="text.name"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.authority.get('id')}}" onclick="javascript:getAuthority(this.dataset.id);">
					<td>{{$context.index+1}}</td>
					<td><label data-juice="Label" data-juice-bind="authority.id" class="id"></label></td>
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
	<hr/>
	<div class="right">
		<!-- ====================================================== -->
		<!-- Authority Details										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-key"></i>
					<spring:message code="text.authority"/>
					<spring:message code="text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addAuthority();">
					<i class="icon-plus"></i>
					<spring:message code="text.new"/>
				</button>
				<button onclick="javascript:saveAuthority();">
					<i class="icon-disk"></i>
					<spring:message code="text.save"/>
				</button>
				<button onclick="javascript:removeAuthority();">
					<i class="icon-trash"></i>
					<spring:message code="text.remove"/>
				</button>
			</div>
		</div>
		<table id="authorityTable" class="detail">
			<colgroup>
				<col style="width:20%;">
				<col style="width:30%;">
				<col style="width:20%;">
				<col style="width:30%;">
			</colgroup>
			<tr>
				<th>
					<i class="icon-attention"></i>
					<spring:message code="text.authority"/>
					<spring:message code="text.id"/>
				</th>
				<td>
					
					<input class="id" data-juice="TextField" data-juice-bind="authority.id"/>
				</td>
				<th>
					*
					<spring:message code="text.authority"/>
					<spring:message code="text.name"/>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="authority.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="text.authority"/>
					<spring:message code="text.description"/>
				</th>
				<td colspan="3">
					<textarea data-juice="TextArea" data-juice-bind="authority.description"></textarea>
				</td>
			</tr>
		</table>
	</div>
</div>
