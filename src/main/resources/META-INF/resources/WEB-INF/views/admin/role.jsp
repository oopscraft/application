<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
/* role search condition */
var roleSearch = new duice.data.Map({
	 rows: 30
	,page: 1
	,searchType: null
	,searchValue: null
	,totalCount:-1
});
var roleSearchTypes = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'ID', text:'<spring:message code="application.text.id"/>' }
	,{ value:'NAME', text:'<spring:message code="application.text.name"/>' }
];
var roles = new duice.data.List();
var role = new duice.data.Map();
var authorities = new duice.data.List();
var isNew = false;

/**
 * clear edit
 */
function clearEdit(){
	role.fromJson({});
	authorities.fromJson([]);
}

/**
 * enable edit
 */
function enableEdit(enable){
	if(enable == true){
		role.setEnable(true);
		role.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		role.setEnable(false);
		role.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
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
			clearEdit();
			role.fromJson(data);
			authorities.fromJson(data.authorities);
			isNew = false;
			enableEdit(true);
			role.setReadonly('id', true);
			$('#roleTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds role
 */
function addRole() {
	clearEdit();
	roles.clearIndex();
	enableEdit(true);
	isNew = true;
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
	
	// Check id
	try {
		__validator.checkId(role.get('id'));
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// check name
	try {
		__validator.checkName(role.get('name'));
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = role.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'role/getRole'
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
			new duice.ui.Alert('<spring:message code="application.message.duplicatedItem" arguments="${item}"/>').open();
			return false;
		}
	}
	
	// Saves role
	<spring:message code="application.text.role" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
		.afterConfirm(function() {
			var roleJson = role.toJson();
			roleJson.authorities = authorities.toJson();
			$.ajax({
				 url: 'role/saveRole'
				,type: 'POST'
				,data: JSON.stringify(roleJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getRole(role.get('id'));
					getRoles();
			 	}
			});	
		}).open();
}

/**
 * Deletes role
 */
function deleteRole() {
	
	// Checks system data
	if(role.get('systemDataYn') == 'Y'){
		new duice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes role
	<spring:message code="application.text.role" var="item"/>
	var message = '<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'role/deleteRole'
			,type: 'GET'
			,data: { id: role.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getRoles();
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
	<!-- ====================================================== -->
	<!-- Roles													-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-duice="ComboBox" data-duice-bind="roleSearch.searchType" data-duice-options="roleSearchTypes" style="width:100px;"></select>
				<input data-duice="TextField" data-duice-bind="roleSearch.searchValue" style="width:100px;"/>
				<button onclick="javascript:getRoles();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addRole();">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="rolesTable" data-duice="Grid" data-duice-bind="roles" data-duice-item="role">
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
				<tr data-id="[[$context.role.get('id')]]" onclick="javascript:getRole(this.dataset.id);">
					<td class="text-center">
						[[roleSearch.get('rows')*(roleSearch.get('page')-1)+$context.index+1]]
					</td>
					<td class="[[$context.role.get('systemDataYn')=='Y'?'systemData':'']]">
						<span data-duice="Text" data-duice-bind="role.id" class="id"></span>
					</td>
					<td>
						<span data-duice="Text" data-duice-bind="role.name"></span>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-duice="Pagination" data-duice-bind="roleSearch" data-duice-rows="rows" data-duice-page="page" data-duice-total-count="totalCount" data-duice-page-size="5">
				<li data-page="[[$context.page]]" onclick="javascript:getRoles(this.dataset.page);">[[$context.page]]</li>
			</ul>
		</div>
	</div>
	<!-- ====================================================== -->
	<!-- Role Details											-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.role"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveRole();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteRole();">
					<i class="icon-delete"></i>
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
					<span class="must">
						<spring:message code="application.text.id"/>
					</span>
				</th>
				<td>
					<input class="id" data-duice="TextField" data-duice-bind="role.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td><input data-duice="TextField" data-duice-bind="role.name"/></td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-duice="TextArea" data-duice-bind="role.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_authority.png"/>
					<br/>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.authorities"/>
				</th>
				<td>
					<table data-duice="Grid" data-duice-bind="authorities" data-duice-item="authority">
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
								<th>
									<button class="small" onclick="javascript:addAuthority();">
										<i class="icon-add"></i>
									</button>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="[[$context.authority.get('id')]]">
								<td><span data-duice="Text" data-duice-bind="authority.id" class="id"></span></td>
								<td><span data-duice="Text" data-duice-bind="authority.name"></span></td>
								<td class="text-center">
									<button class="small" data-index="[[$context.index]]" onclick="javascript:removeAuthority(this.dataset.index);">
										<i class="icon-remove"></i>
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
