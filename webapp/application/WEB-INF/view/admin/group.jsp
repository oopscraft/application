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
var groups = new juice.data.Tree();
var group = new juice.data.Map();
group.setReadOnly('id',true);
var roles = new juice.data.List();
var authorities = new juice.data.List();

/**
 * On document loaded
 */
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
		,data: {}
		,success: function(data, textStatus, jqXHR) {
			console.log(data);
			groups.fromJson(data,'childGroups');

			// animates groups
			$('#groupsUl').hide().fadeIn();
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
			group.fromJson(data);
			roles.fromJson(data.roles);
			authorities.fromJson(data.authorities);
			group.setReadOnly('id',true);
			
			// breadcrumbs
			getBreadCrumbs(group.get('upperId'),function(breadCrumbs){
				var breadCrumbsNames = [];
				breadCrumbs.forEach(function(item){
					breadCrumbsNames.push(item.name);
				});
				group.set('breadCrumbs', breadCrumbsNames.join(' > '));
			});
			
			// animates group.
			$('#groupTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Clears group
 */
function clearGroup(){
	group.fromJson({});
	roles.fromJson([]);
	authorities.fromJson([]);
}

/**
 * Gets bread crumbs
 */
function getBreadCrumbs(id, callback) {
	$.ajax({
		 url: 'group/getBreadCrumbs'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			callback.call(this, data);
 	 	}
	});	
}

/**
 * Changes upperId
 */
function changeUpperId(){
	alert('fdasfdas');
}

/**
 * Adds Role
 */
function addRole(){
	__rolesDialog.open(function(selectedRoles){
		
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
 * Adds child group
 */
function addChildGroup(upperId) {
	
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
						<spring:message code="application.text.id" var="item"/>
						new juice.ui.Alert('<spring:message code="application.message.duplicatedItem" arguments="${item}"/>').open();
						isDuplicated = true;
					}
		 	 	}
			});
			if(isDuplicated == true){
				return false;
			}
		})
		.afterConfirm(function(event){
			var id = event.value;
	
			// add new data.
			clearGroup();
			group.set('id', id);
			group.set('upperId', upperId);
	
			// breadcrumbs
			getBreadCrumbs(upperId,function(breadCrumbs){
				var breadCrumbsNames = [];
				breadCrumbs.forEach(function(item){
					breadCrumbsNames.push(item.name);
				});
				group.set('breadCrumbs', breadCrumbsNames.join(' - '));
			});
		})
		.open();
}

/**
 * Saves group
 */
function saveGroup(){
	<spring:message code="application.text.group" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.beforeConfirm(function(){
			if(group.get('name') == null){
				alert('fdasfdsa');
				return false;
			}
		})
		.afterConfirm(function() {
			var groupJson = group.toJson();
			groupJson.roles = roles.toJson();
			groupJson.authorities = authorities.toJson();
			$.ajax({
				 url: 'group/saveGroup'
				,type: 'POST'
				,data: JSON.stringify(groupJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<spring:message code="application.text.group" var="item"/>
					var message = '<spring:message code="application.message.saveItem.complete" arguments="${item}"/>';
					new juice.ui.Alert(message)
					.afterConfirm(function(){
						getGroup(group.get('id'));
						getGroups();
					}).open();
			 	}
			});	
		})
		.open();
}

/**
 * Removes group
 */
function removeGroup() {
	<spring:message code="application.text.group" var="item"/>
	var message = '<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'group/removeGroup'
			,type: 'GET'
			,data: { id: group.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.group" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					clearGroup();
					getGroups();
				}).open();
	  	 	}
		});	
	}).open();
}


</script>
<style type="text/css">

</style>
<div class="title1">
	<i class="icon-folder"></i>
	<spring:message code="application.text.group"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Group Tree												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;border-bottom:solid 1px #ccc;">
			<div>
				<div class="title2">
					<i class="icon-tree"></i>
					<spring:message code="application.text.group"/>
					<spring:message code="application.text.list"/>
				</div>
			</div>
			<div style="width:20%;text-align:right;">
				<button onclick="javascript:addChildGroup(null);">
					<i class="icon-plus"></i>
				</button>
			</div>
		</div>
		<ul id="groupsUl" data-juice="TreeView" data-juice-bind="groups" data-juice-item="group" data-juice-editable="true">
			<li>
				<div style="display:flex;justify-content:space-between;border-bottom:dotted 1px #ccc;">
					<div data-id="{{$context.group.get('id')}}" onclick="javascript:getGroup(this.dataset.id);" style="width:80%;cursor:hand;cursor:pointer;">
						<i class="icon-file"></i>
						<label data-juice="Label" data-juice-bind="group.name"></label>
					</div>
					<div style="width:20%;min-width:100px;display:inline-block;text-align:right;">
						<button data-id="{{$context.group.get('id')}}" onclick="javascript:addChildGroup(this.dataset.id);">
							<i class="icon-plus"></i>
						</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Group Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-folder"></i>
					<spring:message code="application.text.group"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveGroup();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:removeGroup();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="groupTable" class="detail">
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
					<input class="id" data-juice="TextField" data-juice-bind="group.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="group.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.upper"/>
					<spring:message code="application.text.group"/>
				</th>
				<td>
					<label data-juice="Label" data-juice-bind="group.breadCrumbs"></label>
					<input type="hidden" data-juice="TextField" data-juice-bind="group.upperId"/>
					<button onclick="javascript:changeUpperId();">
						<i class="icon-tree"></i>
						<spring:message code="application.text.change"/>
					</button>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="group.description"></textarea>
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
