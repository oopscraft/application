<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- global -->
<script type="text/javascript">

// define data structure
var searchMap = new juice.data.Map();
searchMap.fromJson({key: null, value:null, rows:30, page:1});
searchKeyOptions = [
	 {value:null,text:'-'}
	,{value:'id',text:'ID'}
	,{value:'email',text:'Email'}
	,{value:'mobile',text:'Mobile'}
	,{value:'name',text:'Nasme'}
	,{value:'nickname',text:'Nickname'}
];
var userList = new juice.data.List();
var userMap = new juice.data.Map();
var userGroupList = new juice.data.List();

// code
var userStatusCode = [{value:'A',text:'Active'},{value:'C',text:'Cancel'}];
var useYn = [{value:'Y',text:'Yes'},{value:'N',text:'No'}];

// on document loaded.
$(document).ready(function() {
	getUserList(1);
});

/**
 * Getting user list
 */
function getUserList(page){
	page && searchMap.set('page', page);
	$.ajax({
		 type:'GET'
		,url:'${pageContext.request.contextPath}' + '/console/user/getUserList'
		,data: searchMap.toJson()
		,dataType:'json'
		,encode: true
		,success:function(response) {
			console.log(response);
			userList.fromJson(response);
		 }
		,error: function(response) {
			console.log(response);
			juice.ui.alert(response)
			.open();
			return false;
		 }
	});	
}

/**
 * Getting user detail
 */
function getUserDetail(id) {
	$('#userId').attr('readonly', true);
	
	// getting user information
	$.ajax({
		 type:'GET'
		,url:'${pageContext.request.contextPath}' + '/console/user/getUser'
		,data: {'id':id}
		,dataType:'json'
		,encode: true
		,success:function(response) {
			console.log(response);
			userMap.fromJson(response);
		 }
		,error: function(response) {
			console.log(response);
			juice.ui.alert(response).open();
			return false;
		 }
	});
	// getting user group list.
	$.ajax({
		 type:'GET'
		,url:'${pageContext.request.contextPath}' + '/console/user/getUserGroupList'
		,data: {'id':id}
		,dataType:'json'
		,encode: true
		,success:function(response) {
			console.log(response);
			userGroupList.fromJson(response);
		 }
		,error: function(response) {
			console.log(response);
			juice.ui.alert(response).open();
			return false;
		 }
	});	
}

/**
 * Save User
 */
function saveUser() {
	juice.confirm('Do you want to save User Information?')
	.onConfirm(function() {
		$.ajax({
			 type:'POST'
			,url:'${pageContext.request.contextPath}' + '/console/user/saveUser'
			,dataType:'json'
			,contentType: 'application/json'
			,data: JSON.stringify(userMap.toJson())
	        ,processData: false
			,success:function(response) {
				console.log(response);
				getUserList();
				getUserDetail(userMap.get('id'));
			 }
			,error: function(response) {
				console.log(response);
				juice.ui.alert(response).open();
				return false;
			 }
		});
	})
	.open();
}

/**
 * prepareNewUser
 */
function prepareNewUser() {
	userMap.fromJson({});
	var userIdObj = $('#userId');
	userIdObj.attr('readonly', false);
	userIdObj.focus();
}

/**
 * removeUser
 */
function removeUser() {
	juice.confirm('Do you want to remove User?')
	.onConfirm(function() {
		$.ajax({
			 type:'POST'
			,url:'${pageContext.request.contextPath}' + '/console/user/removeUser'
			,data: {'id':userMap.get('id')}
			,dataType:'json'
			,encode: true
			,success:function(response) {
				console.log(response);
				getUserList();
				userMap.fromJson({});
				userGroupList.fromJson([]);
			 }
			,error: function(response) {
				console.log(response);
				juice.ui.alert(response).open();
				return false;
			 }
		});
	})
	.open();
}
</script>
<style type="text/css">
.user {
	width: 100%;
	border-collapse: collapse;
	border: solid 1px #efefef;
}
.user th {
	border: solid 1px #efefef;
	background-color: #fafafa;
	padding-left: 1rem;
}
.user td {
	border: solid 1px #efefef;
}
</style>
<h1>
	User
	<small>Management of User</small>
</h1>
<div class="container-fluid">
	<div class="row">
		<div class="col">
			<div style="display:flex; justify-content:space-between;;">
				<span>
					<i class="fas fa-search"></i>
					&nbsp;
					<select data-juice="ComboBox" data-juice-bind="searchMap.key" data-juice-options="searchKeyOptions">
					</select>
					<input data-juice="TextField" data-juice-bind="searchMap.value" style="width:100px;"/>
					<button onclick="javascript: getUserList(1);">Search</button>
					<button onclick="javascript:getUserList(searchMap.get('page')-1);"><</button>
					<input data-juice="TextField" data-juice-bind="searchMap.page" style="width:30px;text-align:center;" readonly/>
					<button onclick="javascript:getUserList(searchMap.get('page')+1);">></button>
				</span>
				<span>
					<button onclick="javascript:prepareNewUser();">New User</button>
				</span>
			</div>
			<div>
				<table data-juice="Grid" data-juice-bind="userList" data-juice-item="user">
					<thead>
						<tr>
							<th>No</th>
							<th>ID</th>
							<th>Name</th>
							<th>Email</th>
							<th>Mobile</th>
							<th>Status</th>
						</tr>
					</thead>
					<tbody>
						<tr onclick="javascript: getUserDetail('{{$context.user.get('id')}}');">
							<td align="center">
								{{($context.index+1) + searchMap.get('rows')*(searchMap.get('page')-1)}}
							</td>
							<td>
								<label data-juice="Label" data-juice-bind="user.id"/>
							</td>
							<td>
								<label data-juice="Label" data-juice-bind="user.name"/>
							</td>
							<td>
								<label data-juice="Label" data-juice-bind="user.email"/>
							</td>
							<td>
								<label data-juice="Label" data-juice-bind="user.mobile"/>
							</td>
							<td>
								<select data-juice="ComboBox" data-juice-bind="user.statusCode" data-juice-options="userStatusCode" disabled/></select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="col">
			<div style="display:flex; justify-content:space-between;;">
				<span>
					<h2>
						<i class="fas fa-user"></i>
						User Detail
					</h2>
				</span>
				<span>
					<button onclick="javascript:saveUser();">Save</button>
					<button onclick="javascript:removeUser();">Remove</button>
				</span>
			</div>
			<div>
				<table class="user">
					<tr>
				    	<th>
				    		ID
				    	</th>
				    	<td>
				    		<img src="https://avatars3.githubusercontent.com/u/24501078?s=80&v=4" style="height:2rem; height:2rem;"/>
				    		<input id="userId" data-juice="TextField" data-juice-bind="userMap.id" readonly></label>
				    	</td>
				    	<th>
				    		Password
				    	</th>
				    	<td>
				    		<button onclick="javascript:alert('');">Change Password</button>
				    	</td>
					</tr>
					<tr>
				    	<th>
				    		Email
				    	</th>
				    	<td>
				    		<input data-juice="TextField" data-juice-bind="userMap.email"/>
				    	</td>
				    	<th>
				    		Mobile
				    	</th>
				    	<td>
				    		<input data-juice="TextField" data-juice-bind="userMap.mobile"/>
				    	</td>
					</tr>
					<tr>
				    	<th>
				    		Name
				    	</th>
				    	<td>
				    		<input data-juice="TextField" data-juice-bind="userMap.name"/>
				    	</td>
				    	<th>
				    		Nickname
				    	</th>
				    	<td>
				    		<input data-juice="TextField" data-juice-bind="userMap.nickname"/>
				    	</td>
					</tr>

					<tr>
				    	<th>
				    		Status
				    	</th>
				    	<td>
				    		<select data-juice="ComboBox" data-juice-bind="userMap.statusCode" data-juice-options="userStatusCode"/></select>
				    	</td>
				    	<th>
				    		Join Date
				    	</th>
				    	<td>
				    		<label data-juice="Label" data-juice-bind="userMap.joinDate"/>
				    	</td>
					</tr>
					<tr style="height:2rem;">
				    	<th>
				    		Message
				    	</th>
				    	<td colspan="3">
				    		<textarea data-juice="TextArea" data-juice-bind="userMap.message"></textarea>
				    	</td>
					</tr>
					<tr>
						<th>Description</th>
						<td colspan="3">
							<div data-juice="HtmlEditor" data-juice-bind="userMap.description" style="height:200px;"></div>
						</td>
					</tr>
				</table>
			</div>
			<hr/>
			<div style="display:flex; justify-content:space-between;;">
				<span>
					<h2>
						<i class="fas fa-folder" aria-hidden="true"></i>
						User Group
					</h2>
				</span>
				<span>
					<button onclick="javascript:addUserGroupDialog();">Add</button>
				</span>
			</div>
			<div>
				<table data-juice="Grid" data-juice-bind="userGroupList" data-juice-item="group">
					<thead>
						<tr>
							<th>No</th>
							<th>Group ID</th>
							<th>Group Name</th>
							<th>-</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td align="center">
								{{$context.index+1}}
							</td>
							<td>
								<label data-juice="Label" data-juice-bind="group.id"/>
							</td>
							<td>
								<label data-juice="Label" data-juice-bind="group.name"/>
							</td>
							<td>
								<button>Delete</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>


<!-------------------------------------------------------------------->
<!-- addUserGroupDialog											-->
<!-------------------------------------------------------------------->
<script type="text/javascript">
var groupList = new juice.data.List();
/**
 * openUserGroupDialog
 */
function addUserGroupDialog() {
	// retrieves group list
	$.ajax({
		 type:'GET'
		,url:'${pageContext.request.contextPath}' + '/console/user/getGroupList'
		,data: null
		,dataType:'json'
		,encode: true
		,success:function(response) {
			console.log(response);
			groupList.fromJson(response);
		 }
		,error: function(response) {
			console.log(response);
			juice.alert(response)
				.open();
			return false;
		 }
	});	
	
	// open dialog
	var userDetailDialog = juice.dialog($('#addUserGroupDialog')[0])
		.setTitle('Add User Group')
		.open();
}

</script>
<dialog style="display:none;">
	<script type="text/javascript">
	var groupSearchMap = new juice.data.Map();
	</script>
	<div id="addUserGroupDialog" style="width:800px; height:400px;">
		<div>
			<table data-juice="Grid" data-juice-bind="groupList" data-juice-item="group">
				<thead>
					<tr>
						<th>c</th>
						<th>Group ID</th>
						<th>Group Name</th>
						<th>-</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td align="center">
							<input type="checkbox"/>
						</td>
						<td>
							<label data-juice="Label" data-juice-bind="group.id"/>
						</td>
						<td>
							<label data-juice="Label" data-juice-bind="group.name"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</dialog>

