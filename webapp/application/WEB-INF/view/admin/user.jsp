<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!-- global -->
<script type="text/javascript">
var userFindMap = new juice.data.Map();
var userList = new juice.data.List();
var userMap = new juice.data.Map();
var userGroupList = new juice.data.List();
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
		,success: function(data, textStatus, jqXHR) {
			userList.fromJson(JSON.parse(data));
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
			userMap.fromJson(JSON.parse(data));
  	 	}
	});	
}

/**
 * Gets user group list
 */
function getUserGroups(id){
	$.ajax({
		 url: 'user/getUserGroups'
		,type: 'GET'
		,success: function(data, textStatus, jqXHR) {
			userGroupList.fromJson(JSON.parse(data));
  	 	}
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
<span class="title1">
	<i class="material-icons">face</i>
	User Management
</span>
<div class="container">
	<div class="left">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<span class="title2">
					<i class="material-icons">account_box</i>
					Find User
				</span>
				<select data-juice="ComboBox" data-juice-bind="userFindMap.key" data-juice-options="[]" style="width:100px;"></select>
			</div>
			<div>
				<button onclick="javascript:getUsers();">Find</button>
				<button onclick="javascript:getUsers();">&lt;</button>
				<button onclick="javascript:getUsers();">&gt;</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="userList" data-juice-item="user">
			<thead>
				<tr>
					<th>No</th>
					<th>ID</th>
					<th>Name</th>
					<th>Email</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}" onclick="javascript:getUser(this.dataset.id);">
					<td>{{$context.index+1}}</td>
					<td><label data-juice="Label" data-juice-bind="user.id"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.email"></label></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="right">
		<!-- ====================================================== -->
		<!-- User Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<span class="title2">
					<i class="material-icons">person</i>
					User Detail
				</span>
			</div>
			<div>
				<button onclick="javascript:getUserList();">Save</button>
				<button onclick="javascript:getUserList();">Remove</button>
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
				<td><label data-juice="Label" data-juice-bind="userMap.id"></label></td>
				<th>Password</th>
				<td><button>Change</button></td>
			</tr>
			<tr>
				<th>Name</th>
				<td><input data-juice="TextField" data-juice-bind="userMap.name"/></td>
				<th>Nickname</th>
				<td><input data-juice="TextField" data-juice-bind="userMap.nickname"/></td>
			</tr>
			<tr>
				<th>Email</th>
				<td><input data-juice="TextField" data-juice-bind="userMap.email"/></td>
				<th>Phone</th>
				<td><input data-juice="TextField" data-juice-bind="userMap.phone"/></td>
			</tr>
			<tr>
				<th>fdsa</th>
				<td>fdsa</td>
				<th>fda</th>
				<td>fdsa</td>
			</tr>
		</table>
		<!-- ====================================================== -->
		<!-- Related Group											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<span class="title2">
					<i class="material-icons">folder_shared</i>
					Related Group
				</span>				
			</div>
			<div>
				<button>Add</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="userList" data-juice-item="user">
			<thead>
				<tr>
					<th>Group ID</th>
					<th>Group Name</th>
					<th>-</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}" onclick="javascript:getUser(this.dataset.id);">
					<td><label data-juice="Label" data-juice-bind="user.id"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td><button>Remove</button></td>
				</tr>
			</tbody>
		</table>
		<!-- ====================================================== -->
		<!-- Related Role											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<span class="title2">
					<i class="material-icons">folder_shared</i>
					Related Role
				</span>
			</div>
			<div>
				<button>Add</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="userList" data-juice-item="user">
			<thead>
				<tr>
					<th>Role ID</th>
					<th>Role Name</th>
					<th>-</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}" onclick="javascript:getUser(this.dataset.id);">
					<td><label data-juice="Label" data-juice-bind="user.id"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td><button>Remove</button></td>
				</tr>
			</tbody>
		</table>
		<!-- ====================================================== -->
		<!-- Related Authority										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<span class="title2">
					<i class="material-icons">folder_shared</i>
					Related Authority
				</span>
			</div>
			<div>
				<button>Add</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="userList" data-juice-item="user">
			<thead>
				<tr>
					<th>Role ID</th>
					<th>Role Name</th>
					<th>-</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}" onclick="javascript:getUser(this.dataset.id);">
					<td><label data-juice="Label" data-juice-bind="user.id"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td><button>Remove</button></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
