<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<script type="text/javascript">
var userSearch = new duice.data.Map({
	 rows: 30
	,page: 1
	,searchType: null
	,searchValue: null
	,totalCount:-1
});
var userSearchTypes = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'ID', text:'<spring:message code="application.text.id"/>' }
	,{ value:'NAME', text:'<spring:message code="application.text.name"/>' }
	,{ value:'EMAIL', text:'<spring:message code="application.text.email"/>' }
	,{ value:'PHONE', text:'<spring:message code="application.text.phone"/>' }
];
var users = new duice.data.List();
var user = new duice.data.Map();
var groups = new duice.data.List();
var roles = new duice.data.List();
var authorities = new duice.data.List();
var isNew = false;

// locales
var locales = ${app:toJson(locales)};

// statuses
var statuses = ${app:toJson(statuses)}

/**
 * clear edit
 */
function clearEdit(){
	user.fromJson({});
	groups.fromJson([]);
	roles.fromJson([]);
	authorities.fromJson([]);
}

/**
 * enable edit
 */
function enableEdit(enable){
	if(enable == true){
		user.setEnable(true);
		user.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		user.setEnable(false);
		user.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
	getUsers();
});

/**
 * Gets user list
 */
function getUsers(page) {
	if(page){
		userSearch.set('page',page);
	}
	$.ajax({
		 url: 'user/getUsers'
		,type: 'GET'
		,data: userSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			users.fromJson(data);
			userSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#usersTable').hide().fadeIn();
			
			//  find current row index.			
			var index = users.indexOf(function(row){
				return row.get('id') == user.get('id');
			});
			users.setIndex(index);
   	 	}
	});	
}

/**
 * Gets user
 */
function getUser(id) {
	$.ajax({
		 url: 'user/getUser'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			clearEdit();
			user.fromJson(data);
			groups.fromJson(data.groups);
			roles.fromJson(data.roles);
			authorities.fromJson(data.authorities);
			isNew = false;
			enableEdit(true);
			user.setReadonly('id',true);
			$('#passwordTr').hide();
			$('#userTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds user.
 */
function addUser() {
	clearEdit();
	user.fromJson({});
	user.set('status', statuses[0].value);
	users.clearIndex();
	enableEdit(true);
	isNew = true;
	$('#passwordTr').show();
}

/**
 * Adds groups
 */
function addGroup() {
	__groupsDialog
	.setUnique(false)
	.setDisable(function(node){
		var contains = groups.contains(function(row){
			return row.get('id') == node.get('id');
		});
		if(contains){
			return true;
		}
	}).afterConfirm(function(nodes){
		groups.addRows(nodes);
	}).open();

}

/**
 * Removes group
 */
function removeGroup(index) {
	groups.removeRow(index);
}

/**
 * Adds Role
 */
function addRole(){
	__rolesDialog
	.setDisable(function(row){
		var $row = row;
		var contains = roles.contains(function(row){
			return row.get('id') == $row.get('id');
		})
		if(contains){
			return true;
		}
	}).afterConfirm(function(rows){
		roles.addRows(rows);
	}).open();
}

/**
 * Removes role.
 */
function removeRole(index){
	roles.removeRow(index);
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
 * Saves user
 */
function saveUser() {
	
	// Check id
	try {
 		// Checks empty
 		if(duice.util.StringUtils.isEmpty(user.get('id'))){
     		<spring:message code="application.text.id" var="item"/>
			throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
 		}
		// Validates generic
		if(duice.util.StringUtils.isGenericId(user.get('id')) == false){
			throw '<spring:message code="application.message.invalidIdFormat"/>';
		}
		// length
		if(duice.util.StringUtils.isLengthBetween(user.get('id'),4,32) == false){
			<spring:message code="application.text.id" var="item"/>
			throw '<spring:message code="application.message.itemMustLengthBetween" arguments="${item},4,32"/>';
		}
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = user.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'user/getUser'
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
	
	// Checks password
	if(isNew == true){
		try {
			__validator.checkPassword(user.get('password'), user.get('passwordConfirm'));
		}catch(e){
			new duice.ui.Alert(e)
			.afterConfirm(function(){
				$('input[data-duice-bind="user.password"]').select();
			})
			.open();
			return false;
		}
	}
	
	// check name
	try {
 		// Checks empty
 		if(duice.util.StringUtils.isEmpty(user.get('name'))){
     		<spring:message code="application.text.name" var="item"/>
				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
 		}
 		// check length
 		if(duice.util.StringUtils.isLengthBetween(user.get('name'),1,256) == false){
			<spring:message code="application.text.name" var="item"/>
			throw '<spring:message code="application.message.itemMustLengthBetween" arguments="${item},4,32"/>';
 		}
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// Checks nickname
	try {
 		// Checks empty
 		if(duice.util.StringUtils.isEmpty(user.get('nickname'))){
     		<spring:message code="application.text.name" var="item"/>
				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
 		}
 		// check length
 		if(duice.util.StringUtils.isLengthBetween(user.get('nickname'),1,256) == false){
			<spring:message code="application.text.name" var="item"/>
			throw '<spring:message code="application.message.itemMustLengthBetween" arguments="${item},4,32"/>';
 		}
	}catch(e){
		new duice.ui.Alert(e)
		.afterConfirm(function(){
			$('input[data-duice-bind="user.nickname"]').select();
		})
		.open();
		return false;
	}
	
	// Checks email
	try {
		if(duice.util.StringUtils.isEmailAddress(user.get('email')) == false){
 			throw '<spring:message code="application.message.invalidEmailAddressFormat"/>';
 		}
	}catch(e){
		new duice.ui.Alert(e)
		.afterConfirm(function(){
			$('input[data-duice-bind="user.email"]').select();
		})
		.open();
		return false;
	}

	// Checks statusCode
	if(duice.util.StringUtils.isEmpty(user.get('status'))){
		<spring:message code="application.text.status" var="item"/>
		new duice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.afterConfirm(function(){
			$('select[data-duice-bind="user.status"]').select();
		})
		.open();
		return false;
	}
	
	// Saves user info
	<spring:message code="application.text.user" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
		.afterConfirm(function() {
			var userJson = user.toJson();
			userJson.groups = groups.toJson();
			userJson.roles = roles.toJson();
			userJson.authorities = authorities.toJson();
			$.ajax({
				 url: 'user/saveUser'
				,type: 'POST'
				,data: JSON.stringify(userJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getUser(user.get('id'));
					getUsers();
			 	}
			});	
		}).open();
}

/**
 * Removes User
 */
function deleteUser(){
	
	// check system data
	if(user.get('systemDataYn') == 'Y'){
		new duice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	<spring:message code="application.text.user" var="item"/>
	var message = '<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'user/deleteUser'
			,type: 'GET'
			,data: { id: user.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getUsers();
	  	 	}
		});	
	}).open();
}

</script>
<style type="text/css">
</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_user.png"/>&nbsp;
	<spring:message code="application.text.user"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container">
	<!-- ====================================================== -->
	<!-- User List												-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-duice="ComboBox" data-duice-bind="userSearch.searchType" data-duice-options="userSearchTypes" style="width:100px;"></select>
				<input data-duice="TextField" data-duice-bind="userSearch.searchValue" style="width:100px;"/>
				<button onclick="javascript:getUsers();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addUser();">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="usersTable" data-duice="Grid" data-duice-bind="users" data-duice-item="user">
			<colgroup>
				<col style="width:10%;"/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col style="width:100px;"/>
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
					<th>
						<spring:message code="application.text.nickname"/>
					</th>
					<th>
						<spring:message code="application.text.email"/>
					</th>
					<th>
						<spring:message code="application.text.status"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="[[$context.user.get('id')]]" onclick="javascript:getUser(this.dataset.id);">
					<td class="text-center">
						[[userSearch.get('rows')*(userSearch.get('page')-1)+$context.index+1]]
					</td>
					<td class="[[$context.user.get('systemDataYn')=='Y'?'systemData':'']]">
						<span data-duice="Text" data-duice-bind="user.id" class="id"></span>
					</td>
					<td><span data-duice="Text" data-duice-bind="user.name"></span></td>
					<td><span data-duice="Text" data-duice-bind="user.nickname"></span></td>
					<td><span data-duice="Text" data-duice-bind="user.email"></span></td>
					<td class="text-center">
						<span data-duice="Text" data-duice-bind="user.status"></span>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-duice="Pagination" data-duice-bind="userSearch" data-duice-rows="rows" data-duice-page="page" data-duice-total-count="totalCount" data-duice-page-size="5">
				<li data-page="[[$context.page]]" onclick="javascript:getUsers(this.dataset.page);">[[$context.page]]</li>
			</ul>
		</div>
	</div>
	<!-- ====================================================== -->
	<!-- User Details											-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-user"></i>
					<spring:message code="application.text.user"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button>
					<i class="icon-change"></i>
					<spring:message code="application.text.password"/>
					<spring:message code="application.text.change"/>
				</button>
				<button onclick="javascript:saveUser();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteUser();">
					<i class="icon-delete"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="userTable" class="detail">
			<colgroup>
				<col style="width:30;">
				<col style="width:70%;">
			</colgroup>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.id"/>
					</span>
				</th>
				<td>
					<input class="id" data-duice="TextField" data-duice-bind="user.id"/>
				</td>
			</tr>
			<tr id="passwordTr">
				<th>
					<span class="must">
						<spring:message code="application.text.password"/>
					</span>
				</th>
				<td>
					<input type="password" class="id" data-duice="TextField" data-duice-bind="user.password" style="width:15rem;"/>
					<input type="password" class="id" data-duice="TextField" data-duice-bind="user.passwordConfirm" style="width:15rem;"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="user.name"/>
				</td>
			</tr>
			<tr>
				<th class="must">
					<spring:message code="application.text.nickname"/>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="user.nickname"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.email"/>
					</span>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="user.email" placeholder="_____@__________"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.status"/>
					</span>
				</th>
				<td>
					<select data-duice="ComboBox" data-duice-bind="user.status" data-duice-options="statuses" style="width:15rem;"></select>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.locale"/>
					</span>
				</th>
				<td>
					<select data-duice="ComboBox" 
						data-duice-bind="user.locale" 
						data-duice-options="locales" 
						data-duice-option-value="locale"
						data-duice-option-text="displayName"
						style="width:15rem;">
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.phone"/>
					</span>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="user.phone" placeholder="___-____-____"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.avatar"/>
				</th>
				<td>
					<img data-duice="Image" data-duice-bind="user.avatar" data-duice-width="64" data-duice-height="64" src="${pageContext.request.contextPath}/static/img/icon_avatar.png" style="width:64px; height:64px;"/>
					<img data-duice="Image" data-duice-bind="user.avatar" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_avatar.png" style="width:48px; height:48px;"/>
					<img data-duice="Image" data-duice-bind="user.avatar" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_avatar.png" style="width:32px; height:32px;"/>
					<img data-duice="Image" data-duice-bind="user.avatar" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_avatar.png" style="width:24px; height:24px;"/>
					<img data-duice="Image" data-duice-bind="user.avatar" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_avatar.png" style="width:16px; height:16px;"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.signature"/>
				</th>
				<td>
					<textarea data-duice="TextArea" data-duice-bind="user.signature" style="height:100px;"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.joinDate"/>
					</span>
				</th>
				<td>
					<span data-duice="Text" data-duice-bind="user.joinDate" data-duice-format="date:yyyy-MM-dd hh:mm:ss"></span>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.closeDate"/>
					</span>
				</th>
				<td>
					<span data-duice="Text" data-duice-bind="user.closeDate"></span>
				</td>
			</tr>
			<tr>
				<th>
					<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_group.png"/>
					<br/>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.groups"/>
				</th>
				<td>
					<table data-duice="Grid" data-duice-bind="groups" data-duice-item="group">
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
									<button class="small" onclick="javascript:addGroup();">
										<i class="icon-add"></i>
									</button>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="[[$context.group.get('id')]]">
								<td>
									<span data-duice="Text" data-duice-bind="group.id" class="id"></span>
								</td>
								<td>
									<span data-duice="Text" data-duice-bind="group.name"></span>
								</td>
								<td class="text-center">
									<button class="small" data-index="[[$context.index]]" onclick="javascript:removeGroup(this.dataset.index);">
										<i class="icon-remove"></i>
									</button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>
					<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_role.png"/>
					<br/>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.roles"/>
				</th>
				<td>
					<table data-duice="Grid" data-duice-bind="roles" data-duice-item="role">
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
									<button class="small" onclick="javascript:addRole();">
										<i class="icon-add"></i>
									</button>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="[[$context.role.get('id')]]">
								<td>
									<span data-duice="Text" data-duice-bind="role.id" class="id"></span>
								</td>
								<td>
									<span data-duice="Text" data-duice-bind="role.name"></span>
								</td>
								<td class="text-center">
									<button class="small" data-index="[[$context.index]]" onclick="javascript:removeRole(this.dataset.index);">
										<i class="icon-remove"></i>
									</button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>
					<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_authority.png"/>
					<br/>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.authorities"/>
				</th>
				<td colspan="3">
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
								<td>
									<span data-duice="Text" data-duice-bind="authority.id" class="id"></span>
								</td>
								<td>
									<span data-duice="Text" data-duice-bind="authority.name"></span>
								</td>
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
