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

/**
 * Adds Role
 */
function addRole(){
	__rolesDialog.open(function (selectedRoles){
		
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
			<c:set var="item"><spring:message code="text.role"/></c:set>
			var message = '<spring:message code="message.duplicatedItem" arguments="${item}"/>';
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
			<c:set var="item"><spring:message code="text.authority"/></c:set>
			var message = '<spring:message code="message.duplicatedItem" arguments="${item}"/>';
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


</script>
<style type="text/css">


</style>
<div class="title1">
	<i class="icon-folder"></i>
	<spring:message code="text.group"/>
	<spring:message code="text.management"/>
</div>
<hr/>
<div class="container">
	<div class="left">
		<!-- ====================================================== -->
		<!-- Group Tree												-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;border-bottom:solid 1px #efefef;">
			<div>
				<div class="title2">
					<i class="icon-tree"></i>
					<spring:message code="text.group"/>
					<spring:message code="text.list"/>
				</div>
			</div>
			<div style="width:20%;text-align:right;">
				<button>
					<i class="icon-plus"></i>
				</button>
			</div>
		</div>
		<ul data-juice="TreeView" data-juice-bind="groups" data-juice-item="group">
			<li>
				<div style="display:flex;justify-content:space-between;border-bottom:dotted 1px #ccc;" onmouseover="this.style.backgroundColor='#f7f7f7'" onmouseout="this.style.backgroundColor='#fff'">
					<div data-id="{{$context.group.get('id')}}" onclick="javascript:getGroup(this.dataset.id);" style="width:80%;cursor:hand;cursor:pointer;">
						<i class="icon-file"></i>
						<span class="id">
						[
						<label data-juice="Label" data-juice-bind="group.id"></label>
						]
						</span>
						<label data-juice="Label" data-juice-bind="group.name"></label>
					</div>
					<div style="width:20%;text-align:right;">
						<button>
							<i class="icon-up"></i>
						</button>
						<button>
							<i class="icon-down"></i>
						</button>
						<button>
							<i class="icon-plus"></i>
						</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<hr/>
	<div class="right">
		<!-- ====================================================== -->
		<!-- Group Details											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-folder"></i>
					<spring:message code="text.group"/>
					<spring:message code="text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:addRole();">
					<i class="icon-plus"></i>
					<spring:message code="text.new"/>
				</button>
				<button onclick="javascript:saveRole();">
					<i class="icon-disk"></i>
					<spring:message code="text.save"/>
				</button>
				<button onclick="javascript:removeRole();">
					<i class="icon-trash"></i>
					<spring:message code="text.remove"/>
				</button>
			</div>
		</div>
		<table class="detail">
			<colgroup>
				<col style="width:30%;"/>
				<col style="width:70%;"/>
			</colgroup>
			<tr>
				<th>
					<i class="icon-attention"></i>
					<spring:message code="text.id"/>
				</th>
				<td>
					<input class="id" data-juice="TextField" data-juice-bind="group.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="group.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="text.upper"/>
					<spring:message code="text.group"/>
				</th>
				<td>
					<label data-juice="Label" data-juice-bind="group.id"></label>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="group.description"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<i class="icon-card"></i>
					<spring:message code="text.own"/>
					<spring:message code="text.roles"/>
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
									<spring:message code="text.id"/>
								</th>
								<th>
									<spring:message code="text.name"/>
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
								<td><label data-juice="Label" data-juice-bind="role.id" class="id"></label></td>
								<td><label data-juice="Label" data-juice-bind="role.name"></label></td>
								<td>
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
					<spring:message code="text.own"/>
					<spring:message code="text.authorities"/>
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
									<spring:message code="text.id"/>
								</th>
								<th>
									<spring:message code="text.name"/>
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
								<td><label data-juice="Label" data-juice-bind="authority.id" class="id"></label></td>
								<td><label data-juice="Label" data-juice-bind="authority.name"></label></td>
								<td>
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
