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
var propertySearch = new duice.data.Map({
	 rows: 30
	,page: 1
	,searchType: null
	,searchValue: null
	,totalCount:-1
});
var propertySearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'ID', text:'<spring:message code="application.text.id"/>' }
	,{ value:'NAME', text:'<spring:message code="application.text.name"/>' }
];
var properties = new duice.data.List();
var property = new duice.data.Map();

/**
 * clear edit
 */
function clearEdit() {
	property.fromJson({});
}

/**
 * enable edit
 */
function enableEdit(editable) {
	if(editable == true){
		property.setEnable(true);
		property.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		property.setEnable(false);
		property.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
	getProperties();
});

/**
 * Gets property list
 */
function getProperties(page) {
	if(page){
		propertySearch.set('page',page);
	}
	$.ajax({
		 url: 'property/getProperties'
		,type: 'GET'
		,data: propertySearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			properties.fromJson(data);
			propertySearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#propertiesTable').hide().fadeIn();
			
			//  find current row index.			
			var index = properties.indexOf(function(row){
				return row.get('id') == property.get('id');
			});
			properties.setIndex(index);
   	 	}
	});	
}

/**
 * Gets property
 */
function getProperty(id) {
	$.ajax({
		 url: 'property/getProperty'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			clearEdit();
			property.fromJson(data);
			isNew = false;
			enableEdit(true);
			property.setReadonly('id',true);
			$('#propertyTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds property
 */
function addProperty() {
	clearEdit();
	properties.clearIndex();
	enableEdit(true);
	isNew = true;
}

/**
 * Saves property.
 */
function saveProperty() {
	
	// Check id
	try {
		__validator.checkId(property.get('id'));
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// check name
	try {
		__validator.checkName(property.get('name'));
	}catch(e){
		new duice.ui.Alert(e).open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = property.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'property/getProperty'
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
	
	// Saves property
	<spring:message code="application.text.property" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
		.afterConfirm(function() {
			var propertyJson = property.toJson();
			$.ajax({
				 url: 'property/saveProperty'
				,type: 'POST'
				,data: JSON.stringify(propertyJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getProperty(property.get('id'));
					getProperties();
			 	}
			});	
		}).open();
}

/**
 * Removes property
 */
function deleteProperty() {

	// Checks system data
	if(property.get('systemDataYn') == 'Y'){
		new duice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes property
	<spring:message code="application.text.property" var="item"/>
	var message = '<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>';
	new duice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'property/deleteProperty'
			,type: 'GET'
			,data: { id: property.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getProperties();
	  	 	}
		});	
	}).open();
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_property.png"/>&nbsp;
	<spring:message code="application.text.property"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<!-- ====================================================== -->
	<!-- Properties												-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-duice="ComboBox" data-duice-bind="propertySearch.searchType" data-duice-options="propertySearchKeys" style="width:100px;"></select>
				<input data-duice="TextField" data-duice-bind="propertySearch.searchValue" style="width:100px;"/>
				<button onclick="javascript:getProperties();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addProperty();">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="propertiesTable" data-duice="Grid" data-duice-bind="properties" data-duice-item="property">
			<colgroup>
				<col style="width:10%"/>
				<col style="width:30%"/>
				<col style="width:60%"/>
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
				</tr>
			</thead>
			<tbody>
				<tr data-id="[[$context.property.get('id')]]" onclick="javascript:getProperty(this.dataset.id);">
					<td class="text-center">
						[[propertySearch.get('rows')*(propertySearch.get('page')-1)+$context.index+1]]
					</td>
					<td class="[[$context.property.get('systemDataYn')=='Y'?'systemData':'']]">
						<span data-duice="Text" data-duice-bind="property.id" class="id"></span>
					</td>
					<td><span data-duice="Text" data-duice-bind="property.name"></span></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-duice="Pagination" data-duice-bind="propertySearch" data-duice-rows="rows" data-duice-page="page" data-duice-total-count="totalCount" data-duice-page-size="5">
				<li data-page="[[$context.page]]" onclick="javascript:getProperties(this.dataset.page);">[[$context.page]]</li>
			</ul>
		</div>
	</div>
	<!-- ====================================================== -->
	<!-- Property Details										-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.property"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveProperty();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteProperty();">
					<i class="icon-delete"></i>
					<spring:message code="application.text.delete"/>
				</button>
			</div>
		</div>
		<table id="propertyTable" class="detail">
			<colgroup>
				<col style="width:30%;">
				<col style="width:70%;">
			</colgroup>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.id"/>
					</span>
				</th>
				<td>
					<input class="id" data-duice="TextField" data-duice-bind="property.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="property.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.value"/>
				</th>
				<td>
					<textarea data-duice="TextArea" data-duice-bind="property.value"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-duice="TextArea" data-duice-bind="property.description"></textarea>
				</td>
			</tr>
		</table>
	</div>
</div>
