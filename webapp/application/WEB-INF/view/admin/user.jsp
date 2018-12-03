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
var userSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 20
	,totalCount: -1
});
/* defines roleSearch Map change event handler */
userSearch.afterChange(function(event){
	/* reset value when key changed */
	if(event.name == 'key'){
		this.set('value','');
	}
});
var userSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
	,{ value:'email', text:'<spring:message code="application.text.email"/>' }
	,{ value:'phone', text:'<spring:message code="application.text.phone"/>' }
];
var users = new juice.data.List();
var user = new juice.data.Map();
user.setReadOnly('id', true);
var groups = new juice.data.List();
var roles = new juice.data.List();
var authorities = new juice.data.List();

/**
 * On document loaded
 */
$( document ).ready(function() {
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
			$('#usersTable > tbody').hide().fadeIn();
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
			user.fromJson(data);
			groups.fromJson(data.groups);
			roles.fromJson(data.roles);
			authorities.fromJson(data.authorities);
			$('#userTable').hide().fadeIn();
			
			// setting user photo
			$('#userPhotoImg').src = user.get('photo');
  	 	}
	});	
}

/**
 * Adds user.
 */
function addUser() {
	
	<spring:message code="application.text.id" var="item"/>
	new juice.ui.Prompt('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.beforeConfirm(function(event){
			var id = event.value;
	
			// checks duplicated id.
			var isDuplicated = false;
			$.ajax({
				 url: 'group/getGroup'
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
			users.clearIndex();
			user.fromJson({});
			user.set('id', id);
			user.setReadOnly('id',true);
			roles.fromJson([]);
			authorities.fromJson([]);
		})
		.open();
}

/**
 * Adds groups
 */
function addGroup() {
	__groupsDialog.open(function(selectedGroups){
		selectedGroups.forEach(function(group){
			groups.addRow(group);
		});
	});
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
	__rolesDialog.open(function (selectedRoles){
		
		// checks duplicated row
		var duplicated = false;
		for(var i = 0, size = selectedRoles.getRowCount(); i < size; i ++){
			var row = selectedRoles.getRow(i);
			duplicated = roles.containsRow(row, function(src,tar){
				if(src.get('id') == tar.get('id')){
					return true;
				}
			});
			if(duplicated == true){
				break;
			}
		}
		if(duplicated == true){
			<spring:message code="application.text.role" var="item"/>
			var message = '<spring:message code="application.message.duplicatedItem" arguments="${item}"/>';
			new juice.ui.Alert(message).open();
			return false;
		}

		// add selected rows.
		roles.addAll(selectedRoles);
	});
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
			<spring:message code="application.text.authority" var="item"/>
			var message = '<spring:message code="application.message.duplicatedItem" arguments="${item}"/>';
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
 * Saves user
 */
function saveUser() {
	<spring:message code="application.text.user" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.beforeConfirm(function(){
			if(user.get('name') == null){
				alert('fdsafdsa');
				return false;
			}
		})
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
					<spring:message code="application.text.user" var="item"/>
					var message = '<spring:message code="application.message.saveItem.complete" arguments="${item}"/>';
					new juice.ui.Alert(message)
						.afterConfirm(function(){
							getUser(user.get('id'));
							getUsers();
						}).open();
			 	}
			});	
		}).open();
}

/**
 * Removes User
 */
function removeUser(){
	<spring:message code="application.text.user" var="item"/>
	var message = '<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'user/removeUser'
			,type: 'GET'
			,data: { id: user.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.user" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					getUsers();
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
	<i class="icon-user"></i>
	<spring:message code="application.text.user"/>
	<spring:message code="application.text.management"/>
</div>
<hr/>
<div class="container">
	<div class="left">
		<!-- ====================================================== -->
		<!-- User List												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<span class="title2">
					<i class="icon-search"></i>
				</span>
				<select data-juice="ComboBox" data-juice-bind="userSearch.key" data-juice-options="userSearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="userSearch.value" style="width:100px;"/>
			</div>
			<div>
				<button onclick="javascript:getUsers();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
		</div>
		<table id="usersTable" data-juice="Grid" data-juice-bind="users" data-juice-item="user">
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
						<spring:message code="application.text.email"/>
					</th>
					<th>
						<spring:message code="application.text.phone"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}" onclick="javascript:getUser(this.dataset.id);">
					<td>{{$context.index+1}}</td>
					<td><label data-juice="Label" data-juice-bind="user.id" class="id"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.email"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.phone"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="userSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getUsers(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<hr/>
	<div class="right">
		<!-- ====================================================== -->
		<!-- User Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-user"></i>
					<spring:message code="application.text.user"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addUser();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
				<button onclick="javascript:saveUser();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:removeUser();">
					<i class="icon-trash"></i>
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
					<i class="icon-attention"></i>
					<spring:message code="application.text.id"/>
				</th>
				<td>
					<input class="id" data-juice="TextField" data-juice-bind="user.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.password"/>
					</span>
				</th>
				<td>
					<input class="id" data-juice="TextField" data-juice-bind="user.password" style="width:30%;"/>
					<input class="id" data-juice="TextField" data-juice-bind="user.passwordConfirm" style="width:30%;"/>
					<button>
						<i class="icon-key"></i>
						<spring:message code="application.text.change"/>
					</button>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.email"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="user.email"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.phone"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="user.phone"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="user.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.nickname"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="user.nickname"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.photo"/>
				</th>
				<td>
					<img id="userPhotoImg" src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjxzdmcgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyAwIDAgMTI4IDEyOCIgaWQ9ItCh0LvQvtC5XzEiIHZlcnNpb249IjEuMSIgdmlld0JveD0iMCAwIDEyOCAxMjgiIHhtbDpzcGFjZT0icHJlc2VydmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPjxnPjxnPjxwYXRoIGQ9Ik02My45MTExMzI4LDcyLjAzNTY0NDVjLTEwLjI2NTYyNSwwLTE4LjYxNzE4NzUtOC4zNTIwNTA4LTE4LjYxNzE4NzUtMTguNjE3Njc1OCAgICBTNTMuNjQ1NTA3OCwzNC44MDAyOTMsNjMuOTExMTMyOCwzNC44MDAyOTNzMTguNjE4MTY0MSw4LjM1MjA1MDgsMTguNjE4MTY0MSwxOC42MTc2NzU4ICAgIFM3NC4xNzY3NTc4LDcyLjAzNTY0NDUsNjMuOTExMTMyOCw3Mi4wMzU2NDQ1eiBNNjMuOTExMTMyOCwzOC44MDAyOTNjLTguMDU5NTcwMywwLTE0LjYxNzE4NzUsNi41NTc2MTcyLTE0LjYxNzE4NzUsMTQuNjE3Njc1OCAgICBzNi41NTc2MTcyLDE0LjYxNzY3NTgsMTQuNjE3MTg3NSwxNC42MTc2NzU4YzguMDYwNTQ2OSwwLDE0LjYxODE2NDEtNi41NTc2MTcyLDE0LjYxODE2NDEtMTQuNjE3Njc1OCAgICBTNzEuOTcxNjc5NywzOC44MDAyOTMsNjMuOTExMTMyOCwzOC44MDAyOTN6IiBmaWxsPSIjMzM5OUNDIi8+PC9nPjxnPjxwYXRoIGQ9Ik0zMi43MDAxOTUzLDk0LjE4NTU0NjljLTEuMTA0NDkyMiwwLTItMC44OTU1MDc4LTItMiAgICBjMC0xMy4xOTkyMTg4LDE0LjgyNzE0ODQtMjMuOTM3OTg4MywzMy4wNTE3NTc4LTIzLjkzNzk4ODNjOC40MTAxNTYyLDAsMTYuNDE2MDE1NiwyLjI3MjQ2MDksMjIuNTQxOTkyMiw2LjM5ODkyNTggICAgYzAuOTE2MDE1NiwwLjYxNzE4NzUsMS4xNTgyMDMxLDEuODU5ODYzMywwLjU0MTk5MjIsMi43NzU4Nzg5Yy0wLjYxNzE4NzUsMC45MTYwMTU2LTEuODU4Mzk4NCwxLjE2MDE1NjItMi43NzYzNjcyLDAuNTQxNTAzOSAgICBDNzguNTg2OTE0MSw3NC4yNzc4MzIsNzEuMzc1LDcyLjI0NzU1ODYsNjMuNzUxOTUzMSw3Mi4yNDc1NTg2Yy0xNi4wMTk1MzEyLDAtMjkuMDUxNzU3OCw4Ljk0NDMzNTktMjkuMDUxNzU3OCwxOS45Mzc5ODgzICAgIEMzNC43MDAxOTUzLDkzLjI5MDAzOTEsMzMuODA0Njg3NSw5NC4xODU1NDY5LDMyLjcwMDE5NTMsOTQuMTg1NTQ2OXoiIGZpbGw9IiMzMzk5Q0MiLz48L2c+PGc+PHBhdGggZD0iTTg5LjEyOTg4MjgsODEuMzg5NjQ4NGMtMC41MjA1MDc4LDAtMS4wNDAwMzkxLTAuMjE5NzI2Ni0xLjQxMDE1NjItMC41ODk4NDM4ICAgIHMtMC41ODk4NDM4LTAuODkwMTM2Ny0wLjU4OTg0MzgtMS40MTAxNTYyYzAtMC41Mjk3ODUyLDAuMjE5NzI2Ni0xLjA0MDAzOTEsMC41ODk4NDM4LTEuNDE5OTIxOSAgICBjMC43MzA0Njg4LTAuNzM5NzQ2MSwyLjA4OTg0MzgtMC43Mzk3NDYxLDIuODMwMDc4MSwwYzAuMzcwMTE3MiwwLjM3OTg4MjgsMC41ODAwNzgxLDAuODkwMTM2NywwLjU4MDA3ODEsMS40MTk5MjE5ICAgIGMwLDAuNTIwMDE5NS0wLjIwOTk2MDksMS4wNDAwMzkxLTAuNTgwMDc4MSwxLjQxMDE1NjJDOTAuMTY5OTIxOSw4MS4xNjk5MjE5LDg5LjY2MDE1NjIsODEuMzg5NjQ4NCw4OS4xMjk4ODI4LDgxLjM4OTY0ODR6IiBmaWxsPSIjMzM5OUNDIi8+PC9nPjxnPjxwYXRoIGQ9Ik05MS44Mzk4NDM4LDg1Yy0wLjUzMDI3MzQsMC0xLjA0MDAzOTEtMC4yMjAyMTQ4LTEuNDEwMTU2Mi0wLjU5MDMzMiAgICBDOTAuMDQ5ODA0Nyw4NC4wNDAwMzkxLDg5LjgzOTg0MzgsODMuNTIwMDE5NSw4OS44Mzk4NDM4LDgzYzAtMC41MzAyNzM0LDAuMjA5OTYwOS0xLjA0MDAzOTEsMC41ODk4NDM4LTEuNDIwNDEwMiAgICBjMC43NDAyMzQ0LTAuNzM5NzQ2MSwyLjA4MDA3ODEtMC43Mzk3NDYxLDIuODIwMzEyNSwwQzkzLjYyOTg4MjgsODEuOTU5OTYwOSw5My44Mzk4NDM4LDgyLjQ2OTcyNjYsOTMuODM5ODQzOCw4MyAgICBjMCwwLjUyMDAxOTUtMC4yMDk5NjA5LDEuMDQwMDM5MS0wLjU4OTg0MzgsMS40MDk2NjhDOTIuODc5ODgyOCw4NC43Nzk3ODUyLDkyLjM1OTM3NSw4NSw5MS44Mzk4NDM4LDg1eiIgZmlsbD0iIzMzOTlDQyIvPjwvZz48Zz48cGF0aCBkPSJNOTQuNTQ5ODA0Nyw5NC4wMjAwMTk1Yy0wLjUzMDI3MzQsMC0xLjA0OTgwNDctMC4yMDk5NjA5LTEuNDE5OTIxOS0wLjU5MDMzMiAgICBjLTAuMzcwMTE3Mi0wLjM2OTYyODktMC41ODAwNzgxLTAuODg5NjQ4NC0wLjU4MDA3ODEtMS40MDk2NjhjMC0wLjUzMDI3MzQsMC4yMDk5NjA5LTEuMDQwMDM5MSwwLjU4MDA3ODEtMS40MjA0MTAyICAgIGMwLjc1LTAuNzM5NzQ2MSwyLjA4MDA3ODEtMC43Mzk3NDYxLDIuODMwMDc4MSwwYzAuMzcwMTE3MiwwLjM4MDM3MTEsMC41ODk4NDM4LDAuODkwMTM2NywwLjU4OTg0MzgsMS40MjA0MTAyICAgIGMwLDAuNTIwMDE5NS0wLjIxOTcyNjYsMS4wNDAwMzkxLTAuNTg5ODQzOCwxLjQwOTY2OEM5NS41ODk4NDM4LDkzLjgxMDA1ODYsOTUuMDY5MzM1OSw5NC4wMjAwMTk1LDk0LjU0OTgwNDcsOTQuMDIwMDE5NXoiIGZpbGw9IiMzMzk5Q0MiLz48L2c+PGc+PHBhdGggZD0iTTkzLjYzOTY0ODQsODkuNTA5NzY1NmMtMC41MTk1MzEyLDAtMS4wNDAwMzkxLTAuMjE5NzI2Ni0xLjQxMDE1NjItMC41ODk4NDM4ICAgIHMtMC41ODk4NDM4LTAuODkwMTM2Ny0wLjU4OTg0MzgtMS40MTAxNTYyYzAtMC41Mjk3ODUyLDAuMjE5NzI2Ni0xLjA0OTgwNDcsMC41ODk4NDM4LTEuNDE5OTIxOSAgICBjMC43NS0wLjc0MDIzNDQsMi4wODAwNzgxLTAuNzQwMjM0NCwyLjgzMDA3ODEsMGMwLjM3MDExNzIsMC4zNzk4ODI4LDAuNTgwMDc4MSwwLjg5MDEzNjcsMC41ODAwNzgxLDEuNDE5OTIxOSAgICBjMCwwLjUyMDAxOTUtMC4yMDk5NjA5LDEuMDQwMDM5MS0wLjU4MDA3ODEsMS40MTAxNTYyQzk0LjY3OTY4NzUsODkuMjkwMDM5MSw5NC4xNjk5MjE5LDg5LjUwOTc2NTYsOTMuNjM5NjQ4NCw4OS41MDk3NjU2eiIgZmlsbD0iIzMzOTlDQyIvPjwvZz48L2c+PC9zdmc+" style="height:5rem;"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.profile"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="user.message"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<i class="icon-folder"></i>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.groups"/>
				</th>
				<td>
					<table data-juice="Grid" data-juice-bind="groups" data-juice-item="group">
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
									<button onclick="javascript:addGroup();">
										<i class="icon-plus"></i>
									</button>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="{{$context.group.get('id')}}">
								<td><label data-juice="Label" data-juice-bind="group.id" class="id"></label></td>
								<td><label data-juice="Label" data-juice-bind="group.name"></label></td>
								<td>
									<button data-index="{{$context.index}}" onclick="javascript:removeGroup(this.dataset.index);">
										<i class="icon-minus"></i>
									</button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>
					<i class="icon-card"></i>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.roles"/>
				</th>
				<td>
					<table data-juice="Grid" data-juice-bind="roles" data-juice-item="role">
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
									<button onclick="javascript:addRole();">
										<i class="icon-plus"></i>
									</button>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="{{$context.role.get('id')}}">
								<td><label data-juice="Label" data-juice-bind="role.id" class="id"></label></td>
								<td><label data-juice="Label" data-juice-bind="role.name"></label></td>
								<td>
									<button data-index="{{$context.index}}" onclick="javascript:removeRole(this.dataset.index);">
										<i class="icon-minus"></i>
									</button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>
					<i class="icon-key"></i>
					<spring:message code="application.text.own"/>
					<spring:message code="application.text.authorities"/>
				</th>
				<td colspan="3">
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
								<td>
									<label data-juice="Label" data-juice-bind="authority.id" class="id"></label>
								</td>
								<td>
									<label data-juice="Label" data-juice-bind="authority.name"></label>
								</td>
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
