<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="/WEB-INF/tld/application.tld"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<script type="text/javascript">
var pageSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
pageSearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var pageSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var pages = new juice.data.List();
var page = new juice.data.Map();
page.setReadonly('id', true);
page.setEnable(false);
var isNewPage = false;
var accessAuthorities = new juice.data.List();

// types
var types = ${app:toJson(types)};

// accessPolicies
var policies = ${app:toJson(policies)};

/**
 * On document loaded
 */
$( document ).ready(function() {
	getPages();
	
	$('#accessPolicySelect').change(function(event){
		onPolicyChanged('access');
	});
});

/**
 * on policy changed.
 */
function onPolicyChanged(type) {
	var policy = page.get(type + 'Policy');
	var authoritiesTable = $('#' + type + 'AuthoritiesTable');
	if(policy == 'AUTHORIZED') {
		authoritiesTable.show();
	}else{
		authoritiesTable.hide();
	}
}

/**
 * Gets pages
 */
function getPages(page) {
	if(page){
		pageSearch.set('page',page);
	}
	$.ajax({
		 url: 'page/getPages'
		,type: 'GET'
		,data: pageSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			pages.fromJson(data);
			pageSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#pagesTable').hide().fadeIn();
			
			//  find current row index.			
			var index = pages.indexOf(function(row){
				return row.get('id') == window.page.get('id');
			});
			pages.setIndex(index);
   	 	}
	});	
}

/**
 * Gets page
 */
function getPage(id) {
	$.ajax({
		 url: 'page/getPage'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			page.setEnable(true);
			page.fromJson(data);
			accessAuthorities.fromJson(data.accessAuthorities);
			page.setReadonly('id', true);
			isNewPage = false;
			
			// animates page
			$('#pageTable').hide().fadeIn();
			
			// hide or show authorities table
			onPolicyChanged('access');
  	 	}
	});	
}

/**
 * Clears page
 */
function clearPage(){
	page.fromJson({});
	accessAuthorities.fromJson([]);
}

/**
 * Adds page
 */
function addPage() {
	
	// prepare
	clearPage();
	isNewPage = true;
	
	// add new data
	page.set('id', __generateRandomId());
	page.set('accessPolicy', 'ANONYMOUS');
	page.setEnable(true);
	page.setReadonly('id', false);
}

/**
 * Saves page.
 */
function savePage() {
	
	// checks duplicated id. 
	if(isNewPage == true){
		var id = page.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'page/getPage'
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
	
	// checks id
	if(__isEmpty(page.get('id'))){
		<spring:message code="application.text.id" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}

	// checks  name
	if(__isEmpty(page.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Saves page
	<spring:message code="application.text.message" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
		.afterConfirm(function() {
			var pageJson = page.toJson();
			pageJson.accessAuthorities = accessAuthorities.toJson();
			$.ajax({
				 url: 'page/savePage'
				,type: 'POST'
				,data: JSON.stringify(pageJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getPage(page.get('id'));
					getPages();
			 	}
			});	
		}).open();
}

/**
 * Deletes page
 */
function deletePage(){

	// Removes pages
	<spring:message code="application.text.page" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: 'page/removePage'
			,type: 'GET'
			,data: { id: page.get('id') }
			,success: function(data, textStatus, jqXHR) {
				page.fromJson({});
				page.setEnable(false);
				getPages();
	  	 	}
		});	
	}).open();
}

/**
 * Adds Access Authority
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
 * Preview page
 */
function openPage() {
	var pageUrl = '${pageContext.request.contextPath}/page/' + page.get('id');
	 window.open(pageUrl);
}

</script>
<style type="text/css">
</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_page.png"/>&nbsp;
	<spring:message code="application.text.page"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Pages													-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-juice="ComboBox" data-juice-bind="pageSearch.key" data-juice-options="pageSearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="pageSearch.value" style="width:100px;"/>
				<button onclick="javascript:getBoards();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addPage();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="pagesTable" data-juice="Grid" data-juice-bind="pages" data-juice-item="page">
			<colgroup>
				<col style="width:10%"/>
				<col style="width:30%"/>
				<col/>
				<col style="width:10%"/>
				<col style="width:3%"/>
			</colgroup>
			<thead>
				<tr>
					<th>
						<spring:message code="application.text.no"/>
					</th>
					<th>
						<spring:message code="application.text.page"/>
						<spring:message code="application.text.id"/>
					</th>
					<th>
						<spring:message code="application.text.page"/>
						<spring:message code="application.text.name"/>
					</th>
					<th>
						<spring:message code="application.text.page"/>
						<spring:message code="application.text.type"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.page.get('id')}}" onclick="javascript:getPage(this.dataset.id);">
					<td class="text-center">
						{{pageSearch.get('rows')*(pageSearch.get('page')-1)+$context.index+1}}
					</td>
					<td>
						<label data-juice="Label" data-juice-bind="page.id" class="id"></label>
					</td>
					<td>
						<label data-juice="Label" data-juice-bind="page.name"></label>
					</td>
					<td class="text-center">
						<label data-juice="Label" data-juice-bind="page.type"></label>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="pageSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getPages(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Page Details										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.page"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:openPage();">
					<i class="icon-link"></i>
					<spring:message code="application.text.page"/>
					<spring:message code="application.text.open"/>
				</button>
				<button onclick="javascript:savePage();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deletePage();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="pageTable" class="detail">
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
					<input class="id" data-juice="TextField" data-juice-bind="page.id" style="width:20rem;"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="page.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.type"/>
					</span>
				</th>
				<td>
					<select data-juice="ComboBox" data-juice-bind="page.type" data-juice-options="types" style="width:15rem;"></select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.value"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="page.value"/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.layout"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="page.layoutId"/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.access"/>
						<spring:message code="application.text.policy"/>
					</span>
				</th>
				<td>
					<select id="accessPolicySelect" data-juice="ComboBox" data-juice-bind="page.accessPolicy" data-juice-options="policies" style="width:15rem;"></select>
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
									<button class="small" data-index="{{$context.index}}" onclick="javascript:removeAccessAuthority(this.dataset.index);">
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
