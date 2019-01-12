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
group.setReadonly('id',true);
group.setEnable(false);
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
function clearUpperId(){
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
			group.setEnable(true);
	
			// breadcrumbs
			getBreadCrumbs(upperId,function(breadCrumbs){
				var breadCrumbsNames = [];
				breadCrumbs.forEach(function(item){
					breadCrumbsNames.push(item.name);
				});
				group.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
			});
		})
		.open();
}

/**
 * Saves group
 */
function saveGroup(){
	
	// Checks validation of authority
	if(juice.util.validator.isEmpty(group.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Saves group
	<spring:message code="application.text.group" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
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
	
	// Checks embedded data
	if(group.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Checks child node exists.
	var index = groups.indexOf(function(node){
		return node.get('id') == group.get('id');
	});
	console.log(groups.getNode(index).getChildNodes());
	if(groups.getNode(index).getChildNodes().length > 0){
		<spring:message code="application.text.group" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.hasChildItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Removes group
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
					group.setEnable(false);
					getGroups();
				}).open();
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
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Group Tree												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;border-bottom:solid 1px #eee;">
			<div>
				<div class="title2">
					<i class="icon-tree"></i>
					<spring:message code="application.text.group"/>
					<spring:message code="application.text.list"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addGroup(null);">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<ul id="groupsUl" data-juice="TreeView" data-juice-bind="groups" data-juice-item="group">
			<li>
				<div class="groupItem" data-id="{{$context.group.get('id')}}" onclick="javascript:getGroup(this.dataset.id);">
					<div>
						<i class="icon-file-o"></i>
						<span class="{{$context.group.get('systemDataYn')=='Y'?'systemData':''}}">
							<label data-juice="Label" data-juice-bind="group.name"></label>
						</span>
					</div>
					<div style="display:inline-block;text-align:right;">
						<button class="small" data-id="{{$context.group.get('id')}}" onclick="javascript:addGroup(this.dataset.id);">
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
					<div style="display:flex; justify-content:space-between;">
						<label data-juice="Label" data-juice-bind="group.breadCrumbs"></label>
						<input type="hidden" data-juice="TextField" data-juice-bind="group.upperId"/>
						<div>
							<button class="small" onclick="javascript:changeUpperId();">
								<i class="icon-change"></i>
								<spring:message code="application.text.change"/>
							</button>
							<button class="small" onclick="javascript:clearUpperId();">
								<i class="icon-cancel"></i>
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
					<textarea data-juice="TextArea" data-juice-bind="group.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.displaySeq"/>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="group.displaySeq" style="width:5rem; text-align:right;"/>
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
									<button class="small" onclick="javascript:addRole();">
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
									<button class="small" data-index="{{$context.index}}" onclick="javascript:removeRole(this.dataset.index);">
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
									<button class="small" onclick="javascript:addAuthority();">
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
									<button class="small" data-index="{{$context.index}}" onclick="javascript:removeAuthority(this.dataset.index);">
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
