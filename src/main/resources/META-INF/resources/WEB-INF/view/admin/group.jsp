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
var groups = new duice.data.Tree();
var group = new duice.data.Map();
var roles = new duice.data.List();
var authorities = new duice.data.List();
var isNew = false;

/**
 * clear edit
 */
function clearEdit(){
	group.fromJson({});
	roles.fromJson([]);
	authorities.fromJson([]);
}

/**
 * enable edit
 */
function enableEdit(enable){
	if(enable == true){
		group.setEnable(true);
		group.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		group.setEnable(false);
		group.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
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
			
			// find current node index
			var index = groups.indexOf(function(node){
				return node.get('id') == group.get('id');
			});
			groups.setIndex(index);
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
			group.setEnable(true);
			group.fromJson(data);
			roles.fromJson(data.roles);
			authorities.fromJson(data.authorities);
			isNew = false;
			enableEdit(true);
			group.setReadonly('id',true);
			
			// breadcrumbs
			getBreadCrumbs(group.get('upperId'),function(breadCrumbs){
				var breadCrumbsNames = [];
				breadCrumbs.forEach(function(item){
					breadCrumbsNames.push(item.name);
				});
				group.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
			});
			
			// animates group.
			$('#groupTable').hide().fadeIn();
  	 	}
	});	
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
	__groupsDialog
		.setUnique(true)
		.setDisable(function(node){
			if(node.get('id') == group.get('id')
			|| node.get('id') == group.get('upperId')
			){
				return true;
			}
		}).afterConfirm(function(node){
			console.log(node.get('name'));
			group.set('upperId', node.get('id'));
			
			// find breadCrumbs
			var breadCrumbsNames = new Array();
			var upperNode = node;
			while(upperNode){
				if(upperNode.get('name')){
					breadCrumbsNames.push(upperNode.get('name'));	
				}
				upperNode = upperNode.getParentNode();
			}
			breadCrumbsNames.reverse();
			console.log(breadCrumbsNames);
			group.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
		}).open();
}

/**
 * Clears upperId
 */
function removeUpperId(){
	group.set('upperId',null);
	group.set('breadCrumbs', '');
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
 * Adds child group
 */
function addGroup(upperId) {
	clearEdit();
	group.set('id', '');
	group.set('upperId', upperId);
	console.log(group.data);

	// breadcrumbs
	getBreadCrumbs(upperId,function(breadCrumbs){
		var breadCrumbsNames = [];
		breadCrumbs.forEach(function(item){
			breadCrumbsNames.push(item.name);
		});
		group.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
	});
	
	groups.clearIndex();
	enableEdit(true);
	isNew = true;
	console.log(group.data);
}

/**
 * Saves group
 */
function saveGroup(){
	
	// Check id
	try {
		__validator.checkId(group.get('id'));
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// check name
	try {
		__validator.checkName(group.get('name'));
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = group.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'group/getGroup'
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
	
	// Saves group
	<spring:message code="application.text.group" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
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
					getGroup(group.get('id'));
					getGroups();
			 	}
			});	
		})
		.open();
}

/**
 * Deletes group
 */
function deleteGroup() {
	
	// Checks embedded data
	if(group.get('systemDataYn') == 'Y'){
		new duice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Checks child node exists.
	var index = groups.indexOf(function(node){
		return node.get('id') == group.get('id');
	});
	if(groups.getNode(index).getChildNodes().length > 0){
		<spring:message code="application.text.group" var="item"/>
		new duice.ui.Alert('<spring:message code="application.message.notAllowRemove.hasChildItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Removes group
	<spring:message code="application.text.group" var="item"/>
	var message = '<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'group/deleteGroup'
			,type: 'GET'
			,data: { id: group.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getGroups();
	  	 	}
		});	
	}).open();
}
</script>
<style type="text/css">
div.groupItem {
	display:flex;
	justify-content:space-between;
	border-bottom:dotted 1px #ccc;
	cursor:hand;
	cursor:pointer;
}
div.groupItem:hover {
	background-color: #eee;
}
</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_group.png"/>&nbsp;
	<spring:message code="application.text.group"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<!-- ====================================================== -->
	<!-- Group Tree												-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;border-bottom:solid 1px #eee;">
			<div>
				<div class="title2">
					<spring:message code="application.text.group"/>
					<spring:message code="application.text.list"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addGroup(null);">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<ul id="groupsUl" data-duice="TreeView" data-duice-bind="groups" data-duice-item="group">
			<li>
				<div class="groupItem" data-id="[[$context.group.get('id')]]" onclick="javascript:getGroup(this.dataset.id);">
					<div>
						<span class="[[$context.group.get('systemDataYn')=='Y'?'systemData':'']]">
							<span data-duice="Text" data-duice-bind="group.name"></span>
						</span>
					</div>
					<div style="display:inline-block;text-align:right;">
						<button class="small" data-id="[[$context.group.get('id')]]" onclick="javascript:addGroup(this.dataset.id); event.stopPropagation();">
							<i class="icon-add"></i>
						</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<!-- ====================================================== -->
	<!-- Group Details											-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.group"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveGroup();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteGroup();">
					<i class="icon-delete"></i>
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
					<span class="must">
						<spring:message code="application.text.id"/>
					</span>
				</th>
				<td>
					<input class="id" data-duice="TextField" data-duice-bind="group.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="group.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.upper"/>
					<spring:message code="application.text.group"/>
				</th>
				<td>
					<div style="display:flex; justify-content:space-between;">
						<span data-duice="Text" data-duice-bind="group.breadCrumbs"></span>
						<input type="hidden" data-duice="TextField" data-duice-bind="group.upperId"/>
						<div>
							<button class="small" onclick="javascript:changeUpperId();">
								<i class="icon-change"></i>
								<spring:message code="application.text.change"/>
							</button>
							<button class="small" onclick="javascript:removeUpperId();">
								<i class="icon-remove"></i>
							</button>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-duice="TextArea" data-duice-bind="group.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.displaySeq"/>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="group.displaySeq" style="width:5rem; text-align:right;"/>
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
