<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="/WEB-INF/tld/application.tld"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<script type="text/javascript">
var menus = new juice.data.Tree();
var menu = new juice.data.Map();
menu.setReadonly('id',true);
menu.setEnable(false);
var isNewMenu = false;
var displayAuthorities = new juice.data.List();

// displayPolicies
var displayPolicies = ${app:toJson(displayPolicies)};

/**
 * On document loaded
 */
$( document ).ready(function() {
	getMenus();
	
	$('#displayPolicySelect').change(function(event){
		onPolicyChanged('display');
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
			console.log(data);
			menus.fromJson(data,'childMenus');

			// animates menus
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
			menu.setEnable(true);
			menu.fromJson(data);
			displayAuthorities.fromJson(data.displayAuthorities);
			menu.setReadonly('id',true);
			isNewMenu = false;
			
			// breadcrumbs
			getBreadCrumbs(menu.get('upperId'),function(breadCrumbs){
				var breadCrumbsNames = [];
				breadCrumbs.forEach(function(item){
					breadCrumbsNames.push(item.name);
				});
				menu.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
			});
			
			// animates menu.
			$('#menuTable').hide().fadeIn();
			
			// hide or show authorities table.
			onPolicyChanged('display');
  	 	}
	});	
}

/**
 * Clears menu
 */
function clearMenu(){
	menu.fromJson({});
	displayAuthorities.fromJson([]);
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
			if(node.get('id') == menu.get('id')
			|| node.get('id') == menu.get('upperId')
			){
				return true;
			}
		}).afterConfirm(function(node){
			console.log(node.get('name'));
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
 * Clears upperId
 */
function clearUpperId(){
	menu.set('upperId',null);
	menu.set('breadCrumbs', '');
}

/**
 * Adds Authority
 */
function addAuthority(){
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
 * Removes authoritiy.
 */
function removeAuthority(index){
	displayAuthorities.removeRow(index);
}

/**
 * Adds child menu
 */
function addMenu(upperId) {
	
	clearMenu();
	isNewMenu = true;
	
	// add new data.
	menu.set('id', __generateRandomId());
	menu.set('upperId', upperId);
	menu.set('displayPolicy','ANONYMOUS');
	menu.setEnable(true);
	menu.setReadonly('id',false);

	// breadcrumbs
	getBreadCrumbs(upperId,function(breadCrumbs){
		var breadCrumbsNames = [];
		breadCrumbs.forEach(function(item){
			breadCrumbsNames.push(item.name);
		});
		menu.set('breadCrumbs', breadCrumbsNames.join('<i class="icon-right"></i>'));
	});
}

/**
 * Saves menu
 */
function saveMenu(){
	
	// checks duplicated id. 
	if(isNewMenu == true){
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
	
	// Checks validation of authority
	if(__isEmpty(menu.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
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
					getMenu(data.id);
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
	console.log(menus.getNode(index).getChildNodes());
	if(menus.getNode(index).getChildNodes().length > 0){
		<spring:message code="application.text.menu" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.hasChildItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Removes menu
	<spring:message code="application.text.menu" var="item"/>
	var message = '<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'menu/deleteMenu'
			,type: 'GET'
			,data: { id: menu.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.menu" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					clearMenu();
					menu.setEnable(false);
					getMenus();
				}).open();
	  	 	}
		});	
	}).open();
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
	background-color: #eee;
	transition: all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms;
}
</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_menu.png"/>&nbsp;
	<spring:message code="application.text.menu"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Menu Tree												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;border-bottom:solid 1px #eee;">
			<div>
				<div class="title2">
					<i class="icon-tree"></i>
					<spring:message code="application.text.menu"/>
					<spring:message code="application.text.list"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addMenu(null);">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<ul id="menusUl" data-juice="TreeView" data-juice-bind="menus" data-juice-item="menu">
			<li data-id="{{$context.menu.get('id')}}" onclick="javascript:getMenu(this.dataset.id);">
				<div class="menuItem" >
					<div>
						<img class="icon" data-juice="Thumbnail" data-juice-bind="menu.icon" data-juice-width="24" data-juice-height="24" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAACKklEQVR42rWU709SYRTH/Sd70XqfW9JqZo10NTOSUMDN2Rq9qSxC+ol6hxCByS+Byw38RWiI2nrTCrhALRHz2z0nuK68CLV5tu+e58W5n+d7nnOe29V1UmEYug77oweYdr3As6cOVc6pJ4rsjb0dA1cvoyPgPdsddBKOxw9x5vSp9tDxMTPKcqkt0OOeRdDnxvme7uOhE+NWVKuVtsAppWyK6pfP0PWcxfEOy3Jb4JhlBLdu3oDt7gQG9H3QnetGLrcBTWClUsa/Rji0gL5LF44CrRaTChSEaQQCPtZrrxvC7CvMuJ7D9dIJp2OSNXnfxrnfv1UxNHhNGyh30JS/Y2c7j36tUSJg8w5rtV0cHPxUtb9fR72+h9ruDxa5kkuFBnAL/XoNoHnEqJYcCs5DkkRFCYiJGOKxCCsceovQgh9+3xzcgotzP33c0XZoum1QgQRaz2aQyaxibXUZS2mJlXonIplYREQBz/u9hyXrWwDlUrEBFHkUCErA5aUU0qkkAxPxKOKL4T+A+iu9R4HDhkH1pVCZTXcrK2l2JyXjLDERZYdUNgO3NlsDi8XfFx2NBBlEarqTxBiL3BEw8MbDudv5XGtgofCVk7wegaEk+jjCzQhwmeTMOzfDM9l0qDnYNJxNYEYplU7Ob35g5TayrPX3a6ysch0ppXEUtNcE0tj8T1TkonaXey/qYDWb+NFbRo0sGnZezYfrqGkYdLjJaAC9f1rpB3Fif/5fRO6q8pQoJI0AAAAASUVORK5CYII=" style="vertical-align:middle;"/>
						<span class="{{$context.menu.get('systemDataYn')=='Y'?'systemData':''}}">
							<label data-juice="Label" data-juice-bind="menu.name"></label>
						</span>
					</div>
					<div style="display:inline-block;text-align:right;">
						<button class="small" data-id="{{$context.menu.get('id')}}" onclick="javascript:addMenu(this.dataset.id); event.stopPropagation();">
							<i class="icon-plus"></i>
						</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Menu Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-folder"></i>
					<spring:message code="application.text.menu"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveMenu();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteMenu();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
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
							<button class="small" onclick="javascript:clearUpperId();">
								<i class="icon-cancel"></i>
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
					<img data-juice="Thumbnail" data-juice-bind="menu.icon" data-juice-width="32" data-juice-height="32" data-juice-editable="true" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAACKklEQVR42rWU709SYRTH/Sd70XqfW9JqZo10NTOSUMDN2Rq9qSxC+ol6hxCByS+Byw38RWiI2nrTCrhALRHz2z0nuK68CLV5tu+e58W5n+d7nnOe29V1UmEYug77oweYdr3As6cOVc6pJ4rsjb0dA1cvoyPgPdsddBKOxw9x5vSp9tDxMTPKcqkt0OOeRdDnxvme7uOhE+NWVKuVtsAppWyK6pfP0PWcxfEOy3Jb4JhlBLdu3oDt7gQG9H3QnetGLrcBTWClUsa/Rji0gL5LF44CrRaTChSEaQQCPtZrrxvC7CvMuJ7D9dIJp2OSNXnfxrnfv1UxNHhNGyh30JS/Y2c7j36tUSJg8w5rtV0cHPxUtb9fR72+h9ruDxa5kkuFBnAL/XoNoHnEqJYcCs5DkkRFCYiJGOKxCCsceovQgh9+3xzcgotzP33c0XZoum1QgQRaz2aQyaxibXUZS2mJlXonIplYREQBz/u9hyXrWwDlUrEBFHkUCErA5aUU0qkkAxPxKOKL4T+A+iu9R4HDhkH1pVCZTXcrK2l2JyXjLDERZYdUNgO3NlsDi8XfFx2NBBlEarqTxBiL3BEw8MbDudv5XGtgofCVk7wegaEk+jjCzQhwmeTMOzfDM9l0qDnYNJxNYEYplU7Ob35g5TayrPX3a6ysch0ppXEUtNcE0tj8T1TkonaXey/qYDWb+NFbRo0sGnZezYfrqGkYdLjJaAC9f1rpB3Fif/5fRO6q8pQoJI0AAAAASUVORK5CYII="/>
					<img data-juice="Thumbnail" data-juice-bind="menu.icon" data-juice-width="24" data-juice-height="24" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAACKklEQVR42rWU709SYRTH/Sd70XqfW9JqZo10NTOSUMDN2Rq9qSxC+ol6hxCByS+Byw38RWiI2nrTCrhALRHz2z0nuK68CLV5tu+e58W5n+d7nnOe29V1UmEYug77oweYdr3As6cOVc6pJ4rsjb0dA1cvoyPgPdsddBKOxw9x5vSp9tDxMTPKcqkt0OOeRdDnxvme7uOhE+NWVKuVtsAppWyK6pfP0PWcxfEOy3Jb4JhlBLdu3oDt7gQG9H3QnetGLrcBTWClUsa/Rji0gL5LF44CrRaTChSEaQQCPtZrrxvC7CvMuJ7D9dIJp2OSNXnfxrnfv1UxNHhNGyh30JS/Y2c7j36tUSJg8w5rtV0cHPxUtb9fR72+h9ruDxa5kkuFBnAL/XoNoHnEqJYcCs5DkkRFCYiJGOKxCCsceovQgh9+3xzcgotzP33c0XZoum1QgQRaz2aQyaxibXUZS2mJlXonIplYREQBz/u9hyXrWwDlUrEBFHkUCErA5aUU0qkkAxPxKOKL4T+A+iu9R4HDhkH1pVCZTXcrK2l2JyXjLDERZYdUNgO3NlsDi8XfFx2NBBlEarqTxBiL3BEw8MbDudv5XGtgofCVk7wegaEk+jjCzQhwmeTMOzfDM9l0qDnYNJxNYEYplU7Ob35g5TayrPX3a6ysch0ppXEUtNcE0tj8T1TkonaXey/qYDWb+NFbRo0sGnZezYfrqGkYdLjJaAC9f1rpB3Fif/5fRO6q8pQoJI0AAAAASUVORK5CYII="/>
					<img data-juice="Thumbnail" data-juice-bind="menu.icon" data-juice-width="16" data-juice-height="16" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAACKklEQVR42rWU709SYRTH/Sd70XqfW9JqZo10NTOSUMDN2Rq9qSxC+ol6hxCByS+Byw38RWiI2nrTCrhALRHz2z0nuK68CLV5tu+e58W5n+d7nnOe29V1UmEYug77oweYdr3As6cOVc6pJ4rsjb0dA1cvoyPgPdsddBKOxw9x5vSp9tDxMTPKcqkt0OOeRdDnxvme7uOhE+NWVKuVtsAppWyK6pfP0PWcxfEOy3Jb4JhlBLdu3oDt7gQG9H3QnetGLrcBTWClUsa/Rji0gL5LF44CrRaTChSEaQQCPtZrrxvC7CvMuJ7D9dIJp2OSNXnfxrnfv1UxNHhNGyh30JS/Y2c7j36tUSJg8w5rtV0cHPxUtb9fR72+h9ruDxa5kkuFBnAL/XoNoHnEqJYcCs5DkkRFCYiJGOKxCCsceovQgh9+3xzcgotzP33c0XZoum1QgQRaz2aQyaxibXUZS2mJlXonIplYREQBz/u9hyXrWwDlUrEBFHkUCErA5aUU0qkkAxPxKOKL4T+A+iu9R4HDhkH1pVCZTXcrK2l2JyXjLDERZYdUNgO3NlsDi8XfFx2NBBlEarqTxBiL3BEw8MbDudv5XGtgofCVk7wegaEk+jjCzQhwmeTMOzfDM9l0qDnYNJxNYEYplU7Ob35g5TayrPX3a6ysch0ppXEUtNcE0tj8T1TkonaXey/qYDWb+NFbRo0sGnZezYfrqGkYdLjJaAC9f1rpB3Fif/5fRO6q8pQoJI0AAAAASUVORK5CYII="/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.link"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="menu.link"/>
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
					<select id="displayPolicySelect" data-juice="ComboBox" data-juice-bind="menu.displayPolicy" data-juice-options="displayPolicies" style="width:15rem;"></select>
					<table id="displayAuthoritiesTable" data-juice="Grid" data-juice-bind="displayAuthorities" data-juice-item="authority">
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
