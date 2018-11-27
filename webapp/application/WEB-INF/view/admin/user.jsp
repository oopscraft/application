<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!-- global -->
<script type="text/javascript">
var userSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 10
	,totalCount:100
});
var userSearchOptions = [
	 { text:'전체', value:null }
	,{ text:'ID', value:'ID' }
	,{ text:'Name', value:'NAME' }
];
var users = new juice.data.List();
var user = new juice.data.Map();
var groups = new juice.data.List();
var roles= new juice.data.List();
var authorities = new juice.data.List();

$( document ).ready(function() {
	getUsers();
});

/**
 * Gets user list
 */
function getUsers() {
	$.ajax({
		 url: 'user/getUsers'
		,type: 'GET'
		,data: userSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			users.fromJson(JSON.parse(data));
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
			var userJson = JSON.parse(data);
			user.fromJson(userJson);
			groups.fromJson(userJson.groups);
			roles.fromJson(userJson.roles);
			authorities.fromJson(userJson.authorities);
			
			// setting user photo
			$('#userPhotoImg').src = user.get('photo');
  	 	}
	});	
}

/**
 * Saves user
 */
function saveUser() {
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
			alert("Save completed.");
			getUser(user.get('id'));
 	 	}
	});	
}

/**
 * Adds groups
 */
function addGroup() {
	__openGroupsDialog(function(selectedgroups){
		alert(selectedgroups);
	});
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
	<i class="fas fa-user-alt"></i>
	User Management
</div>
<div class="container">
	<div class="left">
		<!-- ====================================================== -->
		<!-- User List												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<span class="title2">
					<i class="fas fa-search"></i>
				</span>
				<select data-juice="ComboBox" data-juice-bind="userSearch.key" data-juice-options="userSearchOptions" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="userSearch.value" style="width:100px;"/>
			</div>
			<div>
				<button onclick="javascript:getUsers();">
					<i class="fas fa-search"></i>
					Find
				</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="users" data-juice-item="user">
			<thead>
				<tr>
					<th>No</th>
					<th>ID</th>
					<th>Name</th>
					<th>Nickname</th>
					<th>Email</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}" onclick="javascript:getUser(this.dataset.id);">
					<td>{{$context.index+1}}</td>
					<td><label data-juice="Label" data-juice-bind="user.id" class="id"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.nickname"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.email"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="userSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getUsers(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="right">
		<!-- ====================================================== -->
		<!-- User Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="fas fa-user-circle"></i>
					User Detail
				</div>
			</div>
			<div>
				<button onclick="javascript:saveUser();">
					<i class="fas fa-plus"></i>
					New
				</button>
				<button onclick="javascript:saveUser();">
					<i class="fas fa-save"></i>
					Save
				</button>
				<button onclick="javascript:removeUser();">
					<i class="far fa-trash-alt"></i>
					Remove
				</button>
			</div>
		</div>
		<table class="detail">
			<colgroup>
				<col style="width:20%;">
				<col style="width:30%;">
				<col style="width:20%;">
				<col style="width:30%;">
			</colgroup>
			<tr>
				<th>ID</th>
				<td><input class="id" data-juice="TextField" data-juice-bind="user.id" disabled/></td>
				<th>Password</th>
				<td><button><i class="fas fa-key"></i> Change</button></td>
			</tr>
			<tr>
				<th>
					Photo
				</th>
				<td>
					<img id="userPhotoImg" src="" style="height:100%;"/>
				</td>
				<th>
					Message
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="user.message"></textarea>
				</td>
			</tr>
			<tr>
				<th>Name</th>
				<td><input data-juice="TextField" data-juice-bind="user.name"/></td>
				<th>Nickname</th>
				<td><input data-juice="TextField" data-juice-bind="user.nickname"/></td>
			</tr>
			<tr>
				<th>Email</th>
				<td><input data-juice="TextField" data-juice-bind="user.email"/></td>
				<th>Phone</th>
				<td><input data-juice="TextField" data-juice-bind="user.phone"/></td>
			</tr>
			<tr>
				<th>Groups</th>
				<td colspan="3">
					<table data-juice="Grid" data-juice-bind="groups" data-juice-item="group">
						<thead>
							<tr>
								<th>ID</th>
								<th>Name</th>
								<th>-</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="{{$context.group.get('id')}}">
								<td>
									<a href=""><label data-juice="Label" data-juice-bind="group.id" class="id"></label></a>
								</td>
								<td><label data-juice="Label" data-juice-bind="group.name"></label></td>
								<td>
									<button><i class="fas fa-minus"></i></button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>Roles</th>
				<td colspan="3">
					<table data-juice="Grid" data-juice-bind="roles" data-juice-item="role">
						<thead>
							<tr>
								<th>ID</th>
								<th>Name</th>
								<th>-</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="{{$context.role.get('id')}}">
								<td><label data-juice="Label" data-juice-bind="role.id"></label></td>
								<td><label data-juice="Label" data-juice-bind="role.name"></label></td>
								<td>
									<button><i class="fas fa-minus"></i></button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>Authorities</th>
				<td colspan="3">
					<table data-juice="Grid" data-juice-bind="authorities" data-juice-item="authority">
						<thead>
							<tr>
								<th>ID</th>
								<th>Name</th>
								<th>-</th>
							</tr>
						</thead>
						<tbody>
							<tr data-id="{{$context.authority.get('id')}}">
								<td><label data-juice="Label" data-juice-bind="authority.id"></label></td>
								<td><label data-juice="Label" data-juice-bind="authority.name"></label></td>
								<td>
									<button><i class="fas fa-minus"></i></button>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
