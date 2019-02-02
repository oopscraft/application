<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<script type="text/javascript">
var menus = new juice.data.Tree();
menus.setEnable(false);
var menu = new juice.data.Map();
var displayAuthorities = new juice.data.List();
var accessAuthorities = new juice.data.List();
var isNew = false;

//types
var types = ${app:toJson(types)};

// policies
var policies = ${app:toJson(policies)};

/**
 * clear edit
 */
function clearEdit(){
	menu.fromJson({});
	displayAuthorities.fromJson([]);
	accessAuthorities.fromJson([]);
	onPolicyChanged('display');
}

/**
 * enable edit
 */
function enableEdit(enable){
	if(enable == true){
		menu.setEnable(true);
		menu.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		menu.setEnable(false);
		menu.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
	getMenus();
	
	$('#displayPolicySelect').change(function(event){
		onPolicyChanged('display');
	});
	
	$('#accessPolicySelect').change(function(event){
		onPolicyChanged('access');
	});
});

/**
 * on policy changed.
 */
function onPolicyChanged(type) {
	var policy = menu.get(type + 'Policy');
	var authoritiesTable = $('#' + type + 'AuthoritiesTable');
	if(policy == 'AUTHORIZED') {
		authoritiesTable.show();
	}else{
		authoritiesTable.hide();
	}
}

/**
 * Gets menus
 */
function getMenus() {
	$.ajax({
		 url: 'menu/getMenus'
		,type: 'GET'
		,data: {}
		,success: function(data, textStatus, jqXHR) {
			menus.fromJson(data,'childMenus');
			$('#menusUl').hide().fadeIn();
			
			// find current node index
			var index = menus.indexOf(function(node){
				return node.get('id') == menu.get('id');
			});
			menus.setIndex(index);
   	 	}
	});	
}

/**
 * Gets menu
 */
function getMenu(id) {
	$.ajax({
		 url: 'menu/getMenu'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			clearEdit();
			menu.fromJson(data);
			displayAuthorities.fromJson(data.displayAuthorities);
			accessAuthorities.fromJson(data.accessAuthorities);
			isNew = false;
			enableEdit(true);
			menu.setReadonly('id',true);
			
			// breadcrumbs
			getBreadCrumbs(menu.get('upperId'),function(breadCrumbs){
				var breadCrumbsNames = [];
				breadCrumbs.forEach(function(item){
					breadCrumbsNames.push(item.name);
				});
				menu.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
			});

			// show menuTable
			$('#menuTable').hide().fadeIn();
			
			// hide or show authorities table.
			onPolicyChanged('display');
			onPolicyChanged('access');
  	 	}
	});	
}

/**
 * Gets bread crumbs
 */
function getBreadCrumbs(id, callback) {
	$.ajax({
		 url: 'menu/getBreadCrumbs'
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
	__menusDialog
		.setDisable(function(node){
			if(node.get('id') == menu.get('id')|| node.get('id') == menu.get('upperId')){
				return true;
			}
		}).afterConfirm(function(node){
			menu.set('upperId', node.get('id'));
			
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
			menu.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
		}).open();
}

/**
 * removes upperId
 */
function removeUpperId(){
	menu.set('upperId',null);
	menu.set('breadCrumbs', '');
}

/**
 * Adds display authority
 */
function addDisplayAuthority(){
	__authoritiesDialog
	.setDisable(function(row){
		var $row = row;
		var contains = displayAuthorities.contains(function(row){
			return row.get('id') == $row.get('id');
		})
		if(contains){
			return true;
		}
	}).afterConfirm(function(rows){
		displayAuthorities.addRows(rows);
	}).open();
}

/**
 * Removes display authoritiy.
 */
function removeDisplayAuthority(index){
	displayAuthorities.removeRow(index);
}

/**
 * Adds access authority
 */
function addAccessAuthority(){
	__authoritiesDialog
	.setDisable(function(row){
		var $row = row;
		var contains = accessAuthorities.contains(function(row){
			return row.get('id') == $row.get('id');
		})
		if(contains){
			return true;
		}
	}).afterConfirm(function(rows){
		accessAuthorities.addRows(rows);
	}).open();
}

/**
 * Removes access authoritiy.
 */
function removeAccessAuthority(index){
	accessAuthorities.removeRow(index);
}


/**
 * Adds child menu
 */
function addMenu(upperId) {
	clearEdit();	
	menu.set('id', __generateRandomId());
	menu.set('upperId', upperId);
	menu.set('type', 'NONE');
	menu.set('accessPolicy', 'ANONYMOUS');
	menu.set('displayPolicy','ANONYMOUS');


	// breadcrumbs
	getBreadCrumbs(upperId,function(breadCrumbs){
		var breadCrumbsNames = [];
		breadCrumbs.forEach(function(item){
			breadCrumbsNames.push(item.name);
		});
		menu.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
	});
	
	menus.clearIndex();
	enableEdit(true);
	isNew = true;
}

/**
 * Saves menu
 */
function saveMenu(){
	
	// Checks validation 
	if(__isEmpty(menu.get('id'))){
		<spring:message code="application.text.id" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Checks validation of authority
	if(__isEmpty(menu.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = menu.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'menu/getMenu'
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
			new juice.ui.Alert('<spring:message code="application.message.duplicatedItem" arguments="${item}"/>').open();
			return false;
		}
	}
	
	// Saves menu
	<spring:message code="application.text.menu" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.afterConfirm(function() {
			var menuJson = menu.toJson();
			menuJson.displayAuthorities = displayAuthorities.toJson();
			$.ajax({
				 url: 'menu/saveMenu'
				,type: 'POST'
				,data: JSON.stringify(menuJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getMenu(menu.get('id'));
					getMenus();
			 	}
			});	
		})
		.open();
}

/**
 * Removes menu
 */
function deleteMenu() {
	
	// Checks embedded data
	if(menu.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Checks child node exists.
	var index = menus.indexOf(function(node){
		return node.get('id') == menu.get('id');
	});
	if(menus.getNode(index).getChildNodes().length > 0){
		<spring:message code="application.text.menu" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.hasChildItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Removes menu
	<spring:message code="application.text.menu" var="item"/>
	var message = '<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'menu/deleteMenu'
			,type: 'GET'
			,data: { id: menu.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getMenus();
	  	 	}
		});	
	}).open();
}

/**
 * Opens menu
 */
function openMenu() {
	var pageUrl = '${pageContext.request.contextPath}/menu/' + menu.get('id');
	 window.open(pageUrl);
}
</script>
<style type="text/css">
div.menuItem {
	display:flex;
	justify-content:space-between;
	padding: 0.25rem 0rem;
	border-bottom:dotted 1px #ccc;
	cursor:hand;
	cursor:pointer;
}
div.menuItem:hover {
	background-color: #f7f7f7;
	transition: all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms;
}
</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_menu.png"/>&nbsp;
	<spring:message code="application.text.menu"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<!-- ====================================================== -->
	<!-- Menu Tree												-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;border-bottom:solid 1px #eee;">
			<div>
				<div class="title2">
					<spring:message code="application.text.menu"/>
					<spring:message code="application.text.list"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addMenu(null);">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<ul id="menusUl" data-juice="TreeView" data-juice-bind="menus" data-juice-item="menu">
			<li data-id="{{$context.menu.get('id')}}" onclick="javascript:getMenu(this.dataset.id);">
				<div class="menuItem" >
					<div>
						<img class="icon" data-juice="Image" data-juice-bind="menu.icon" data-juice-width="24" data-juice-height="24" src="${pageContext.request.contextPath}/static/img/icon_empty.png"/>
						<span class="{{$context.menu.get('systemDataYn')=='Y'?'systemData':''}}">
							<label data-juice="Label" data-juice-bind="menu.name"></label>
						</span>
					</div>
					<div style="display:inline-block;text-align:right;">
						<button class="small" data-id="{{$context.menu.get('id')}}" onclick="javascript:addMenu(this.dataset.id); event.stopPropagation();">
							<i class="icon-add"></i>
						</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<!-- ====================================================== -->
	<!-- Menu Details											-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.menu"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:openMenu();">
					<i class="icon-open"></i>
					<spring:message code="application.text.menu"/>
					<spring:message code="application.text.open"/>
				</button>
				<button onclick="javascript:saveMenu();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteMenu();">
					<i class="icon-delete"></i>
					<spring:message code="application.text.delete"/>
				</button>
			</div>
		</div>
		<table id="menuTable" class="detail">
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.id"/>
					</span>
				</th>
				<td>
					<input class="id" data-juice="TextField" data-juice-bind="menu.id" style="width:20em;"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="menu.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.upper"/>
					<spring:message code="application.text.menu"/>
				</th>
				<td>
					<div style="display:flex; justify-content:space-between;">
						<label data-juice="Label" data-juice-bind="menu.breadCrumbs"></label>
						<input type="hidden" data-juice="TextField" data-juice-bind="menu.upperId"/>
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
					<spring:message code="application.text.icon"/>
				</th>
				<td>
					<img data-juice="Image" data-juice-bind="menu.icon" data-juice-width="32" data-juice-height="32" src="${pageContext.request.contextPath}/static/img/icon_empty.png"/>
					<img data-juice="Image" data-juice-bind="menu.icon" data-juice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_empty.png" style="width:24px; height:24px;"/>
					<img data-juice="Image" data-juice-bind="menu.icon" data-juice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_empty.png" style="width:16px; height:16px;"/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.type"/>
					</span>
				</th>
				<td>
					<select data-juice="ComboBox" data-juice-bind="menu.type" data-juice-options="types" style="width:15rem;"></select>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.link"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="menu.value"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="menu.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.displaySeq"/>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="menu.displaySeq" style="width:5rem; text-align:right;"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.display"/>
						<spring:message code="application.text.policy"/>
					</span>
				</th>
				<td>
					<select id="displayPolicySelect" data-juice="ComboBox" data-juice-bind="menu.displayPolicy" data-juice-options="policies" style="width:15rem;"></select>
					<table id="displayAuthoritiesTable" data-juice="Grid" data-juice-bind="displayAuthorities" data-juice-item="authority">
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
									<button class="small" onclick="javascript:addDisplayAuthority();">
										<i class="icon-add"></i>
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
									<button class="small" data-index="{{$context.index}}" onclick="javascript:removeDisplayAuthority(this.dataset.index);">
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
					<span class="must">
						<spring:message code="application.text.access"/>
						<spring:message code="application.text.policy"/>
					</span>
				</th>
				<td>
					<select id="accessPolicySelect" data-juice="ComboBox" data-juice-bind="menu.accessPolicy" data-juice-options="policies" style="width:15rem;"></select>
					<table id="accessAuthoritiesTable" data-juice="Grid" data-juice-bind="accessAuthorities" data-juice-item="authority">
						<colgroup>
							<col style="width:40%;"/>
							<col/>
							<col style="width:5%;"/>
						</colgroup>
						<thead>
							<tr>
								<th>
									<spring:message code="application.text.authority"/>
									<spring:message code="application.text.id"/>
								</th>
								<th>
									<spring:message code="application.text.authority"/>
									<spring:message code="application.text.name"/>
								</th>
								<th>
									<button class="small" onclick="javascript:addAccessAuthority();">
										<i class="icon-add"></i>
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
									<button class="small" data-index="{{$context.index}}" onclick="javascript:removeAccessAuthority(this.dataset.index);">
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
