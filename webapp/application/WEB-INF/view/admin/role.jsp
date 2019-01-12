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
/* role search condition */
var roleSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
/* defines roleSearch Map change event handler */
roleSearch.afterChange(function(event){
	/* reset value when key changed */
	if(event.name == 'key'){
		this.set('value','');
	}
});
var roleSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var roles = new juice.data.List();
var role = new juice.data.Map();
role.setReadonly('id', true);
role.setEnable(false);
var authorities = new juice.data.List();

/**
 * On document loaded
 */
$( document ).ready(function() {
	getRoles();
});

/**
 * Gets roles
 */
function getRoles(page) {
	if(page){
		roleSearch.set('page',page);
	}
	$.ajax({
		 url: 'role/getRoles'
		,type: 'GET'
		,data: roleSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			roles.fromJson(data);
			roleSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#rolesTable').hide().fadeIn();
			
			//  find current row index.			
			var index = roles.indexOf(function(row){
				return row.get('id') == role.get('id');
			});
			roles.setIndex(index);
   	 	}
	});	
}

/**
 * Gets role
 */
function getRole(id) {
	$.ajax({
		 url: 'role/getRole'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			role.setEnable(true);
			role.fromJson(data);
			authorities.fromJson(data.authorities);
			$('#roleTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds role
 */
function addRole() {

	<spring:message code="application.text.id" var="item"/>
	new juice.ui.Prompt('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.beforeConfirm(function(event){
			var id = event.value;
	
			// checks duplicated id.
			var isDuplicated = false;
			$.ajax({
				 url: 'role/getRole'
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
			roles.clearIndex();
			role.fromJson({});
			role.set('id', id);
			role.setEnable(true);
			authorities.fromJson([]);
		})
		.open();
}

/**
 * Adds Authority
 */
function addAuthority(){
	__authoritiesDialog
	.setDisable(function(row){
		var $row = row;
		var contains = authorities.contains(function(row){
			return row.get('id') == $row.get('id');
		})
		if(contains){
			return true;
		}
	}).afterConfirm(function(rows){
		authorities.addRows(rows);
	}).open();
}


/**
 * Removes authoritiy.
 */
function removeAuthority(index){
	authorities.removeRow(index);
}

/**
 * Saves Role
 */
function saveRole(){
	
	// Checks validation of authority
	if(juice.util.validator.isEmpty(role.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Saves role
	<spring:message code="application.text.role" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.afterConfirm(function() {
			var roleJson = role.toJson();
			roleJson.authorities = authorities.toJson();
			console.log(roleJson);
			$.ajax({
				 url: 'role/saveRole'
				,type: 'POST'
				,data: JSON.stringify(roleJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<spring:message code="application.text.role" var="item"/>
					var message = '<spring:message code="application.message.saveItem.complete" arguments="${item}"/>';
					new juice.ui.Alert(message)
						.afterConfirm(function(){
							getRole(role.get('id'));
							getRoles();
						}).open();
			 	}
			});	
		}).open();
}

/**
 * Removes role
 */
function removeRole() {
	
	// Checks system data
	if(role.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes role
	<spring:message code="application.text.role" var="item"/>
	var message = '<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'role/removeRole'
			,type: 'GET'
			,data: { id: role.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.role" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					role.fromJson({});
					role.setEnable(false);
					getRoles();
				}).open();
	  	 	}
		});	
	}).open();
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_role.png"/>&nbsp;
	<spring:message code="application.text.role"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Roles													-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<div class="title2">
					<i class="icon-search"></i>
				</div>
				<select data-juice="ComboBox" data-juice-bind="roleSearch.key" data-juice-options="roleSearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="roleSearch.value" style="width:100px;"/>
				<button onclick="javascript:getRoles();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addRole();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="rolesTable" data-juice="Grid" data-juice-bind="roles" data-juice-item="role">
			<colgroup>
				<col style="width:10%;"/>
				<col/>
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
				<tr data-id="{{$context.role.get('id')}}" onclick="javascript:getRole(this.dataset.id);">
					<td class="text-center">
						{{roleSearch.get('rows')*(roleSearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="{{$context.role.get('systemDataYn')=='Y'?'systemData':''}}">
						<label data-juice="Label" data-juice-bind="role.id" class="id"></label>
					</td>
					<td>
						<label data-juice="Label" data-juice-bind="role.name"></label>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="roleSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getRoles(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Role Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-card"></i>
					<spring:message code="application.text.role"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveRole();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:removeRole();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="roleTable" class="detail">
			<colgroup>
				<col style="width:30%;"/>
				<col style="width:70%;"/>
			</colgroup>
			<tr>
				<th>
					<i class="icon-attention"></i>
					<spring:message code="application.text.id"/>
				</th>
				<td>
					<input class="id" data-juice="TextField" data-juice-bind="role.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td><input data-juice="TextField" data-juice-bind="role.name"/></td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="role.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<i class="icon-key"></i>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.authorities"/>
				</th>
				<td>
					<table data-juice="Grid" data-juice-bind="authorities" data-juice-item="authority">
						<colgroup>
							<col style="width:40%;"/>
							<col style="width:50%;"/>
							<col style="width:10%;"/>
						</colgroup>
						<thead>
							<tr>
								<th>
									<spring:message code="application.text.id"/>
								</th>
								<th>
									<spring:message code="application.text.name"/>
								</th>
								<th>
									<button onclick="javascript:addAuthority();">
										<i class="icon-plus"></i>
									</button>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="{{$context.authority.get('id')}}">
								<td><label data-juice="Label" data-juice-bind="authority.id" class="id"></label></td>
								<td><label data-juice="Label" data-juice-bind="authority.name"></label></td>
								<td class="text-center">
									<button data-index="{{$context.index}}" onclick="javascript:removeAuthority(this.dataset.index);">
										<i class="icon-minus"></i>
									</button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
