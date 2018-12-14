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
	,rows: 30
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
user.setReadonly('id', true);
user.setEnable(false);
var groups = new juice.data.List();
var roles = new juice.data.List();
var authorities = new juice.data.List();

// code
var locales = new Array();
$.ajax({
	 url: 'user/getLocales'
	,type: 'GET'
	,data: {}
	,success: function(data, textStatus, jqXHR) {
		data.forEach(function(item){
			console.log(item);
			locales.push({
				value: item.locale,
				text: item.displayName
			});
		});
	}
});

var userStatuses = new Array();
$.ajax({
	 url: 'user/getUserStatuses'
	,type: 'GET'
	,data: {}
	,success: function(data, textStatus, jqXHR) {
		data.forEach(function(item){
			console.log(item);
			userStatuses.push({
				value: item.code,
				text: item.name
			});
		});
	}
});

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
	user.setEnable(true);
	
	$.ajax({
		 url: 'user/getUser'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			user.fromJson(data);
			groups.fromJson(data.groups);
			roles.fromJson(data.roles);
			authorities.fromJson(data.authorities);
			$('#passwordTr').hide();
			$('#userTable').hide().fadeIn();
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
			
			// Validates id value
			try {
				__validator.checkId(id);
			}catch(e){
				new juice.ui.Alert(e).open();
				return false;
			}
	
			// checks duplicated id.
			var isDuplicated = false;
			$.ajax({
				 url: 'user/getUser'
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
			user.set('__new', true);
			user.setEnable(true);
			user.set('id', id);
			user.set('statusCode', userStatuses[0].value);
			user.setReadonly('id',true);
			groups.fromJson([]);
			roles.fromJson([]);
			authorities.fromJson([]);
			$('#passwordTr').show();
		})
		.open();
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
	
	// Checks id
	try {
		__validator.checkId(user.get('id'));
	}catch(e){
		new juice.ui.Alert(e)
		.afterConfirm(function(){
			$('input[data-juice-bind="user.id"]').select();
		})
		.open();
		return false;
	}
	
	// Checks password
	if(user.get('__new') == true){
		console.log(user.get('__new'));
		try {
			__validator.checkPassword(user.get('password'), user.get('passwordConfirm'));
		}catch(e){
			new juice.ui.Alert(e)
			.afterConfirm(function(){
				$('input[data-juice-bind="user.password"]').select();
			})
			.open();
			return false;
		}
	}
	
	// Checks email
	try {
		__validator.checkEmailAddress(user.get('email'));
	}catch(e){
		new juice.ui.Alert(e)
		.afterConfirm(function(){
			$('input[data-juice-bind="user.email"]').select();
		})
		.open();
		return false;
	}
	
	// Checks locale
	try {
		__validator.checkLocale(user.get('locale'));
	}catch(e){
		new juice.ui.Alert(e)
		.afterConfirm(function(){
			$('select[data-juice-bind="user.locale"]').select();
		})
		.open();
		return false;
	}
	
	// Check phone number
	try {
		__validator.checkPhoneNumber(user.get('phone'));
	}catch(e){
		new juice.ui.Alert(e)
		.afterConfirm(function(){
			$('input[data-juice-bind="user.phone"]').select();
		})
		.open();
		return false;
	}

	// Checks name
	try {
		__validator.checkName(user.get('name'));
	}catch(e){
		new juice.ui.Alert(e)
		.afterConfirm(function(){
			$('input[data-juice-bind="user.name"]').select();
		})
		.open();
		return false;
	}
	
	// Checks statusCode
	if(juice.util.validator.isEmpty(user.get('StatusCode'))){
		<spring:message code="application.text.status" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.afterConfirm(function(){
			$('select[data-juice-bind="user.statusCode"]').select();
		})
		.open();
		return false;
	}
	
	// Saves user info
	<spring:message code="application.text.user" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
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
	
	// check embedded data
	if(user.get('embeddedYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.embeddedData"/>').open();
		return false;
	}
	
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
</style>
<div class="title1">
	<i class="icon-user"></i>
	<spring:message code="application.text.user"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container">
	<div class="division" style="width:50%;">
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
				<button onclick="javascript:getUsers();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addUser();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="usersTable" data-juice="Grid" data-juice-bind="users" data-juice-item="user">
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
						<spring:message code="application.text.email"/>
					</th>
					<th>
						<spring:message code="application.text.phone"/>
					</th>
					<th>
						<spring:message code="application.text.status"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.user.get('id')}}" onclick="javascript:getUser(this.dataset.id);">
					<td class="text-center">
						{{userSearch.get('rows')*(userSearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="{{$context.user.get('embeddedYn')=='Y'?'embedded':''}}">
						<label data-juice="Label" data-juice-bind="user.id" class="id"></label>
					</td>
					<td><label data-juice="Label" data-juice-bind="user.name"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.email"></label></td>
					<td><label data-juice="Label" data-juice-bind="user.phone"></label></td>
					<td class="text-center">
						<label data-juice="Label" data-juice-bind="user.statusName"></label>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="userSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getUsers(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
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
				<button>
					<i class="icon-key"></i>
					<spring:message code="application.text.password"/>
					<spring:message code="application.text.change"/>
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
			<tr id="passwordTr">
				<th>
					<span class="must">
						<spring:message code="application.text.password"/>
					</span>
				</th>
				<td>
					<input type="password" class="id" data-juice="TextField" data-juice-bind="user.password" style="width:15rem;"/>
					<input type="password" class="id" data-juice="TextField" data-juice-bind="user.passwordConfirm" style="width:15rem;"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.email"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="user.email" placeholder="_____@__________"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.locale"/>
					</span>
				</th>
				<td>
					<select data-juice="ComboBox" data-juice-bind="user.locale" data-juice-options="locales" style="width:15rem;"></select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.phone"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="user.phone" placeholder="___-____-____"/>
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
						<spring:message code="application.text.status"/>
					</span>
				</th>
				<td>
					<select data-juice="ComboBox" data-juice-bind="user.statusCode" data-juice-options="userStatuses" style="width:15rem;"></select>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.nickname"/>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="user.nickname"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.avatar"/>
				</th>
				<td>
					<img data-juice="Thumbnail" data-juice-bind="user.avatar" src="data:image/gif;base64,R0lGODlhZABkANUAAMPM1OHm6drg5NDX3f7+/ubq7fDy9Pr7/MXO1dzh5vHz9eTo7MPN1Pb3+Ont79HY3sfQ1+Xp7fT299vh5eHl6eXp7O7x8/X3+NTb4M/X3fb4+dje4+Dl6MHK0tbc4e3w8sjR187V3Pj5+vP199fd4v39/snR2N3i5uPn6/X2+Pv7/Pz8/cXO1tPa4O/x9O7w8/z8/Nnf4/r7+7/J0f///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAABkAGQAAAb/wJlwSCwaj8ikcslsOp/QqHRKrVqv2Kx2y+16v+CweEwum8/otPoMGEwilsuBQDhcLJHJALA2AzAVEjSDhIWFEhUYfH1fEAkGhpGSgwYJEIxbAAIKk52SCgKLmFUDDp6nkg4Do1QCDaiwhg0CrFAMAQSxuoQEAQy1TAgLu8SECwjASQzDxc0Lv8lGAc3UNAHRRQK51cUEtNgzA6/czQ2r0QCm5NQOorUC69zftRCc8dQKl7UJ99wJtQAg9aNmwF0fDAO5YWBVIWG1CqMACHLYTILBNAMoVjvXZ4JGahMwRfjYLAImCySLWcB0ISWxC5gOuNx1ANO2mbAI2MQZSycj/5k8UdVk1DLoKZiMUBr1tJLRyKWdTDLyCHVSSEYZq0riuEaiVkMWRzX8SgjiKIRkBy2MKPBrwX1p/9GzVzVfMnha5wFUt7QdNnFLzYGboS2ot8FCpvG8hnjGMpzPGgsR5vKY5CG3bibsBe3yEFcOZ3k+UmqgqtFINNGtBuoiaiKO2hKrpO81EhYtAriAUQyGiwAtWNgugsBDAQ33NBTwgOx1iAAjNI4IEMLzgwJASR4o8ABxhgIrgq4okCGaCQoiqoqgYKIWCdlVDZDABAJFibSDSqAAsebBB/yGfNAdGhukAGAkKWxwxgkqHCiJCieQ0QEHDnrCQQdhdEBBhadQgL/hFxRyeAoHX5wgIiwRcrFBgyeeooKCWjxgYIuopDDgFSD8RyMsH/B3BQo76oLCFSTcFyQsJcxHhQnwHemJAe1NsaGTulAwRQbpURmLCOVFUYCWuxQQxQPhgRnLCjc28aWZuojpRAjZsSlUdU0oJmcsjC2BQHR3xjJCc0p40OcuHjCx5qCwuJkEC8ghCosGwiXRgqO6tKCEnZSekucRL2QKywtJICCDp6jIAGgRGZAKS5dGxKAqKjEggemrkmwaBAA7" style="width:100px; height:100px; margin:1px;"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.signature"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="user.signature" style="height:100px;"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.joinDate"/>
					</span>
				</th>
				<td>
					<label data-juice="Label" data-juice-bind="user.joinDate" data-juice-format="date:yyyy-MM-dd hh:mm:ss"></label>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.closeDate"/>
					</span>
				</th>
				<td>
					<label data-juice="Label" data-juice-bind="user.closeDate"></label>
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
								<td>
									<label data-juice="Label" data-juice-bind="group.id" class="id"></label>
								</td>
								<td>
									<label data-juice="Label" data-juice-bind="group.name"></label>
								</td>
								<td class="text-center">
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
								<td>
									<label data-juice="Label" data-juice-bind="role.id" class="id"></label>
								</td>
								<td>
									<label data-juice="Label" data-juice-bind="role.name"></label>
								</td>
								<td class="text-center">
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
