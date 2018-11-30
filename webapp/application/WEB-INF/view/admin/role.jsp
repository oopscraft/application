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
	,rows: 20
	,totalCount:-1
});
/* defines roleSearch Map change event handler */
roleSearch.onChange(function(event){
	/* reset value when key changed */
	if(event.name == 'key'){
		this.set('value','');
	}
});
var roleSearchKeys = [
	 { value:'', text:'- <spring:message code="text.all"/> -' }
	,{ value:'id', text:'<spring:message code="text.id"/>' }
	,{ value:'name', text:'<spring:message code="text.name"/>' }
];
var roles = new juice.data.List();
var role = new juice.data.Map();
role.onChange(function(event){
	// Checks ID is unique.
	if(event.name == 'id') {
		var valid = true;
		$.ajax({
			 url: 'role/getRole'
			,type: 'GET'
			,data: {id:event.value}
			,async: false
			,success: function(data, textStatus, jqXHR) {
				var roleJson = JSON.parse(data);
				if(roleJson != null && roleJson.id == event.value){
					new juice.ui.Alert('<spring:message code="message.duplicatedId"/>').open();
					valid = false;
				}
	 	 	}
		});
		return valid;
	}
});
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
			roles.fromJson(JSON.parse(data));
			roleSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#rolesTable > tbody').hide().fadeIn();
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
			var roleJson = JSON.parse(data);
			role.fromJson(roleJson);
			role.setReadOnly('id',true);
			authorities.fromJson(roleJson.authorities);
			$('#roleTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds role
 */
function addRole() {
	roles.clearIndex();
	role.fromJson({});
	role.setReadOnly('id',false);
	authorities.fromJson([]);
}

/**
 * Adds Authority
 */
function addAuthority(){
	__authoritiesDialog.open(function (selectedAuthorities){
		
		// checks duplicated row
		var duplicated = false;
		for(var i = 0, size = selectedAuthorities.getRowCount(); i < size; i ++){
			var row = selectedAuthorities.getRow(i);
			duplicated = authorities.containsRow(row, function(src,tar){
				if(src.get('id') == tar.get('id')){
					return true;
				}
			});
			if(duplicated == true){
				break;
			}
		}
		if(duplicated == true){
			<c:set var="item"><spring:message code="text.authority"/></c:set>
			var message = '<spring:message code="message.duplicatedItem" arguments="${item}"/>';
			new juice.ui.Alert(message).open();
			return false;
		}

		// add selected rows.
		authorities.addAll(selectedAuthorities);
	});
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
	<c:set var="item"><spring:message code="text.role"/></c:set>
	var message = '<spring:message code="message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.onConfirm(function() {
			var roleJson = role.toJson();
			roleJson.authorities = authorities.toJson();
			console.log(roleJson);
			$.ajax({
				 url: 'role/saveRole'
				,type: 'POST'
				,data: JSON.stringify(roleJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<c:set var="item"><spring:message code="text.role"/></c:set>
					var message = '<spring:message code="message.saveItem.complete" arguments="${item}"/>';
					new juice.ui.Alert(message)
						.onConfirm(function(){
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
	<c:set var="item"><spring:message code="text.role"/></c:set>
	var message = '<spring:message code="message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.onConfirm(function() {
		$.ajax({
			 url: 'role/removeRole'
			,type: 'GET'
			,data: { id: role.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<c:set var="item"><spring:message code="text.role"/></c:set>
				var message = '<spring:message code="message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.onConfirm(function(){
					getRoles();
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
	<i class="icon-card"></i>
	<spring:message code="text.role"/>
	<spring:message code="text.management"/>
</div>
<hr/>
<div class="container">
	<div class="left">
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
			</div>
			<div>
				<button onclick="javascript:getRoles();">
					<i class="icon-search"></i>
					<spring:message code="text.search"/>
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
						<spring:message code="text.no"/>
					</th>
					<th>
						<spring:message code="text.id"/>
					</th>
					<th>
						<spring:message code="text.name"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.role.get('id')}}" onclick="javascript:getRole(this.dataset.id);">
					<td>{{$context.index+1}}</td>
					<td><label data-juice="Label" data-juice-bind="role.id" class="id"></label></td>
					<td><label data-juice="Label" data-juice-bind="role.name"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="roleSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getRoles(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<hr/>
	<div class="right">
		<!-- ====================================================== -->
		<!-- Role Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-card"></i>
					<spring:message code="text.role"/>
					<spring:message code="text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addRole();">
					<i class="icon-plus"></i>
					<spring:message code="text.new"/>
				</button>
				<button onclick="javascript:saveRole();">
					<i class="icon-disk"></i>
					<spring:message code="text.save"/>
				</button>
				<button onclick="javascript:removeRole();">
					<i class="icon-trash"></i>
					<spring:message code="text.remove"/>
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
					<spring:message code="text.id"/>
				</th>
				<td>
					<input class="id" data-juice="TextField" data-juice-bind="role.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="text.name"/>
					</span>
				</th>
				<td><input data-juice="TextField" data-juice-bind="role.name"/></td>
			</tr>
			<tr>
				<th>
					<spring:message code="text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="role.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<i class="icon-key"></i>
					<spring:message code="text.own"/>
					<spring:message code="text.authorities"/>
				</th>
				<td>
					<table data-juice="Grid" data-juice-bind="authorities" data-juice-item="authority">
						<colgroup>
							<col/>
							<col/>
							<col style="width:10%;"/>
						</colgroup>
						<thead>
							<tr>
								<th>
									<spring:message code="text.id"/>
								</th>
								<th>
									<spring:message code="text.name"/>
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
								<td>
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
