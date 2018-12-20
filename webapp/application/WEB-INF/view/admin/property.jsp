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
var propertySearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
propertySearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var propertySearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var properties = new juice.data.List();
var property = new juice.data.Map();
property.setReadonly('id', true);
property.setEnable(false);

/**
 * On document loaded
 */
$( document ).ready(function() {
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
			property.setEnable(true);
			property.fromJson(data);
			$('#propertyTable').hide().fadeIn();
  	 	}
	});	
}

/**
 * Adds property
 */
function addProperty() {
	
	<spring:message code="application.text.id" var="item"/>
	new juice.ui.Prompt('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.beforeConfirm(function(event){
			var id = event.value;
	
			// checks duplicated id.
			var isDuplicated = false;
			$.ajax({
				 url: 'property/getProperty'
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
			properties.clearIndex();
			property.fromJson({});
			property.set('id', id);
			property.setEnable(true);
		})
		.open();
}

/**
 * Saves property.
 */
function saveProperty() {
	
	// Checks validation of property
	if(juice.util.validator.isEmpty(property.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	if(juice.util.validator.isEmpty(property.get('value'))){
		<spring:message code="application.text.value" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Saves property
	<spring:message code="application.text.property" var="item"/>
	var message = '<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
		.afterConfirm(function() {
			var propertyJson = property.toJson();
			$.ajax({
				 url: 'property/saveProperty'
				,type: 'POST'
				,data: JSON.stringify(propertyJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<spring:message code="application.text.property" var="item"/>
					var message = '<spring:message code="application.message.saveItem.complete" arguments="${item}"/>';
					new juice.ui.Alert(message)
						.afterConfirm(function(){
							getProperty(property.get('id'));
							getProperties();
						}).open();
			 	}
			});	
		}).open();
}

/**
 * Removes property
 */
function removeProperty() {

	// Checks system data
	if(property.get('systemDataYn') == 'Y'){
		new juice.ui.Alert('<spring:message code="application.message.notAllowRemove.systemData"/>').open();
		return false;
	}
	
	// Removes property
	<spring:message code="application.text.property" var="item"/>
	var message = '<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>';
	new juice.ui.Confirm(message)
	.afterConfirm(function() {
		$.ajax({
			 url: 'property/removeProperty'
			,type: 'GET'
			,data: { id: property.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.property" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					property.fromJson({});
					property.setEnable(false);
					getProperties();
				}).open();
	  	 	}
		});	
	}).open();
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<i class="icon-key"></i>
	<spring:message code="application.text.property"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Properties											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<div class="title2">
					<i class="icon-search"></i>
				</div>
				<select data-juice="ComboBox" data-juice-bind="propertySearch.key" data-juice-options="propertySearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="propertySearch.value" style="width:100px;"/>
				<button onclick="javascript:getProperties();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addProperty();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="propertiesTable" data-juice="Grid" data-juice-bind="properties" data-juice-item="property">
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
						<spring:message code="application.text.property"/>
						<spring:message code="application.text.id"/>
					</th>
					<th>
						<spring:message code="application.text.property"/>
						<spring:message code="application.text.name"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.property.get('id')}}" onclick="javascript:getProperty(this.dataset.id);">
					<td class="text-center">
						{{propertySearch.get('rows')*(propertySearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="{{$context.property.get('systemDataYn')=='Y'?'systemData':''}}">
						<label data-juice="Label" data-juice-bind="property.id" class="id"></label>
					</td>
					<td><label data-juice="Label" data-juice-bind="property.name"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="propertySearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getProperties(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Property Details										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-key"></i>
					<spring:message code="application.text.property"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveProperty();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:removeProperty();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
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
					<i class="icon-attention"></i>
					<spring:message code="application.text.id"/>
				</th>
				<td>
					
					<input class="id" data-juice="TextField" data-juice-bind="property.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="property.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.value"/>
					</span>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="property.value"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.description"/>
				</th>
				<td>
					<textarea data-juice="TextArea" data-juice-bind="property.description"></textarea>
				</td>
			</tr>
		</table>
	</div>
</div>
