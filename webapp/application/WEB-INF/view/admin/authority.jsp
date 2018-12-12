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
	,rows: 30
	,totalCount:-1
});
authoritySearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var authoritySearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var authorities = new juice.data.List();
var authority = new juice.data.Map();
authority.setReadonly('id', true);
authority.setEnable(false);

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
	authority.setEnable(true);
	$.ajax({
		 url: 'authority/getAuthority'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			authority.fromJson(data);
			$('#authorityTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds authority
 */
function addAuthority() {
	
	<spring:message code="application.text.id" var="item"/>
	new juice.ui.Prompt('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.beforeConfirm(function(event){
			var id = event.value;
	
			// checks duplicated id.
			var isDuplicated = false;
			$.ajax({
				 url: 'authority/getAuthority'
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
			authorities.clearIndex();
			authority.fromJson({});
			authority.set('id', id);
		})
		.open();
}

/**
 * Saves authority.
 */
function saveAuthority() {
	<spring:message code="application.text.authority" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.beforeConfirm(function(){
			if(authority.get('name') == null) {
				alert('fdsa');
				return false;
			}
		})
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
}

/**
 * Removes authority
 */
function removeAuthority() {

	// check embedded data
	if(authority.get('embeddedYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemoveEmbeddedItem"/>').open();
		return false;
	}
	
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
					getAuthorities();
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
				<select data-juice="ComboBox" data-juice-bind="authoritySearch.key" data-juice-options="authoritySearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="authoritySearch.value" style="width:100px;"/>
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
						<spring:message code="application.text.authority"/>
						<spring:message code="application.text.id"/>
					</th>
					<th>
						<spring:message code="application.text.authority"/>
						<spring:message code="application.text.name"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.authority.get('id')}}" onclick="javascript:getAuthority(this.dataset.id);">
					<td class="text-center">
						{{authoritySearch.get('rows')*(authoritySearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="text-left {{$context.authority.get('embeddedYn')=='Y'?'embedded':''}}">
						<label data-juice="Label" data-juice-bind="authority.id" class="id"></label>
					</td>
					<td class="text-left"><label data-juice="Label" data-juice-bind="authority.name"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="authoritySearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getAuthorities(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Authority Details										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-key"></i>
					<spring:message code="application.text.authority"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveAuthority();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:removeAuthority();">
					<i class="icon-trash"></i>
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
					<i class="icon-attention"></i>
					<spring:message code="application.text.id"/>
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
