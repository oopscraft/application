<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!-- global -->
<script type="text/javascript">
var groupSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 10
	,totalCount:-1
});
var groupSearchOptions = [
	 { text:'ID', value:'ID' }
	,{ text:'Name', value:'NAME' }
];
var groups = new juice.data.Tree();
var group = new juice.data.Map();
var users = new juice.data.List();
var roles= new juice.data.List();
var authorities = new juice.data.List();

$( document ).ready(function() {
	getGroups();
});

/**
 * Gets groups
 */
function getGroups() {
	$.ajax({
		 url: 'group/getGroups'
		,type: 'GET'
		,data: groupSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			groups.fromJson(JSON.parse(data),'childGroups');
   	 	}
	});	
}

/**
 * Gets group
 */
function getGroup(id) {
	$.ajax({
		 url: 'group/getGroup'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			var groupJson = JSON.parse(data);
			group.fromJson(groupJson);
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
<div class="title1">
	<i class="fas fa-user-alt"></i>
	Group Management
</div>
<div class="container">
	<div class="left">
		<!-- ====================================================== -->
		<!-- Group List												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<div class="title2">
					<i class="fas fa-search"></i>
				</div>
				<select data-juice="ComboBox" data-juice-bind="groupSearch.key" data-juice-options="groupSearchOptions" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="groupSearch.value" style="width:100px;"/>
			</div>
			<div>
				<button onclick="javascript:getUsers();">
					<i class="fas fa-search"></i>
					Find
				</button>
			</div>
		</div>
		<ul data-juice="TreeView" data-juice-bind="groups" data-juice-item="group">
			<li>
				<span data-id="{{$context.group.get('id')}}" onclick="javascript:getGroup(this.dataset.id);">
					<label data-juice="Label" data-juice-bind="group.name"></label>
				</span>
				+
				up
				down
			</li>
		</ul>
		<div>
			<ul data-juice="Pagination" data-juice-bind="groupSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getGroups(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="right">
		<!-- ====================================================== -->
		<!-- Group Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="fas fa-user-circle"></i>
					User Detail
				</div>
			</div>
			<div>
				<button onclick="javascript:saveGroup();">
					<i class="fas fa-plus"></i>
					New
				</button>
				<button onclick="javascript:saveGroup();">
					<i class="fas fa-save"></i>
					Save
				</button>
				<button onclick="javascript:removeGroup();">
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
				<td><input class="id" data-juice="TextField" data-juice-bind="group.id" disabled/></td>
				<th>Name</th>
				<td><input data-juice="TextField" data-juice-bind="group.name"/></td>
			</tr>
			<tr>
				<th>Description</th>
				<td colspan="3">
					<textarea data-juice="TextArea" data-juice-bind="group.description"></textarea>
				</td>
			</tr>
		</table>
		<!-- ====================================================== -->
		<!-- Group Roles												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="fas fa-lock"></i>
					Related Roles
				</div>
			</div>
			<div>
				<button><i class="fas fa-plus"></i>Add</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="roles" data-juice-item="role">
			<thead>
				<tr>
					<th>Role ID</th>
					<th>Role Name</th>
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
		<!-- ====================================================== -->
		<!-- Related Authorities									-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="fas fa-barcode"></i>
					Related Authorities
				</div>
			</div>
			<div>
				<button><i class="fas fa-plus"></i>Add</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="authorities" data-juice-item="authority">
			<thead>
				<tr>
					<th>Role ID</th>
					<th>Role Name</th>
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
		<!-- ====================================================== -->
		<!-- Group Users											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="fas fa-user-friends"></i>
					Related Groups
				</div>				
			</div>
			<div>
				<button onclick="javascript:addGroup();"><i class="fas fa-plus"></i> Add</button>
			</div>
		</div>
		<table data-juice="Grid" data-juice-bind="users" data-juice-item="user">
			<thead>
				<tr>
					<th>Group ID</th>
					<th>Group Name</th>
					<th>-</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}">
					<td>
						<a href=""><label data-juice="Label" data-juice-bind="user.id" class="id"></label></a>
					</td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td>
						<button><i class="fas fa-minus"></i></button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
