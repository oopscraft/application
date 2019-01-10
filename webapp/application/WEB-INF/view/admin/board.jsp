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
var boardSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
boardSearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var boardSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var boards = new juice.data.List();
var board = new juice.data.Map();
board.setReadonly('id', true);
board.setEnable(false);
var accessAuthorities = new juice.data.List();
var readAuthorities = new juice.data.List();
var writeAuthorities = new juice.data.List();

//policies
var policies = new Array();
$.ajax({
	 url: 'board/getPolicies'
	,type: 'GET'
	,data: {}
	,success: function(data, textStatus, jqXHR) {
		data.forEach(function(item){
			policies.push({
				value: item,
				text: item
			});
		});
	}
});

// rowsPerPage options
var rowsPerPage = [
	{text:'10 Rows',value:'10'},
	{text:'20',value:'20'},
	{text:'30',value:'30'},
	{text:'40',value:'40'},
	{text:'50',value:'50'}
];

/**
 * On document loaded
 */
$( document ).ready(function() {
	getBoards();

	$('#accessPolicySelect').change(function(event){
		onPolicyChanged('access');
	});
	$('#readPolicySelect').change(function(event){
		onPolicyChanged('read');
	});
	$('#writePolicySelect').change(function(event){
		onPolicyChanged('write');
	});
});

/**
 * on policy changed.
 */
function onPolicyChanged(type) {
	var policy = board.get(type + 'Policy');
	var authoritiesTable = $('#' + type + 'AuthoritiesTable');
	if(policy == 'AUTHORIZED') {
		authoritiesTable.show();
	}else{
		authoritiesTable.hide();
	}
}

/**
 * Gets board list
 */
function getBoards(page) {
	if(page){
		boardSearch.set('page',page);
	}
	$.ajax({
		 url: 'board/getBoards'
		,type: 'GET'
		,data: boardSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			boards.fromJson(data);
			boardSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#boardsTable').hide().fadeIn();
			
			//  find current row index.			
			var index = boards.indexOf(function(row){
				return row.get('id') == board.get('id');
			});
			boards.setIndex(index);
   	 	}
	});	
}

/**
 * Gets board
 */
function getBoard(id) {
	$.ajax({
		 url: 'board/getBoard'
		,type: 'GET'
		,data: {id:id}
		,success: function(data, textStatus, jqXHR) {
			board.setEnable(true);
			board.fromJson(data);
			accessAuthorities.fromJson(data.accessAuthorities);
			readAuthorities.fromJson(data.readAuthorities);
			writeAuthorities.fromJson(data.writeAuthorities);
			$('#boardTable').hide().fadeIn();
			
			// hide or show authorities table.
			onPolicyChanged('access');
  	 	}
	});	
}

/**
 * Adds board
 */
function addBoard() {
	
	<spring:message code="application.text.id" var="item"/>
	new juice.ui.Prompt('<spring:message code="application.message.enterItem" arguments="${item}"/>')
		.beforeConfirm(function(event){
			var id = event.value;
	
			// checks duplicated id.
			var isDuplicated = false;
			$.ajax({
				 url: 'board/getBoard'
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
			boards.clearIndex();
			board.fromJson({});
			board.set('id', id);
			board.setEnable(true);
		})
		.open();
}

/**
 * Saves board.
 */
function saveBoard() {
	
	// Checks validation of board
	if(juice.util.validator.isEmpty(board.get('id'))){
		<spring:message code="application.text.id" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	if(juice.util.validator.isEmpty(board.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new juice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// Saves board
	<spring:message code="application.text.message" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
		.afterConfirm(function() {
			var boardJson = board.toJson();
			boardJson.accessAuthorities = accessAuthorities.toJson();
			boardJson.readAuthorities = readAuthorities.toJson();
			boardJson.writeAuthorities = writeAuthorities.toJson();
			$.ajax({
				 url: 'board/saveBoard'
				,type: 'POST'
				,data: JSON.stringify(boardJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<spring:message code="application.text.message" var="item"/>
					new juice.ui.Alert('<spring:message code="application.message.saveItem.complete" arguments="${item}"/>')
						.afterConfirm(function(){
							getBoard(board.get('id'));
							getBoards();
						}).open();
			 	}
			});	
		}).open();
}

/**
 * Deletes board
 */
function deleteBoard(){

	// Removes board
	<spring:message code="application.text.message" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.removeItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: 'board/removeMessage'
			,type: 'GET'
			,data: { id: board.get('id') }
			,success: function(data, textStatus, jqXHR) {
				<spring:message code="application.text.message" var="item"/>
				var message = '<spring:message code="application.message.removeItem.complete" arguments="${item}"/>';
				new juice.ui.Alert(message)
				.afterConfirm(function(){
					board.fromJson({});
					board.setEnable(false);
					getBoards();
				}).open();
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
 * Adds Read Authority
 */
function addReadAuthority(){
	__authoritiesDialog
	.setDisable(function(row){
		var $row = row;
		var contains = readAuthorities.contains(function(row){
			return row.get('id') == $row.get('id');
		})
		if(contains){
			return true;
		}
	}).afterConfirm(function(rows){
		readAuthorities.addRows(rows);
	}).open();
}

/**
 * Removes read authoritiy.
 */
function removeReadAuthority(index){
	readAuthorities.removeRow(index);
}

/**
 * Adds write Authority
 */
function addWriteAuthority(){
	__authoritiesDialog
	.setDisable(function(row){
		var $row = row;
		var contains = writeAuthorities.contains(function(row){
			return row.get('id') == $row.get('id');
		})
		if(contains){
			return true;
		}
	}).afterConfirm(function(rows){
		writeAuthorities.addRows(rows);
	}).open();
}

/**
 * Removes write authoritiy.
 */
function removeWriteAuthority(index){
	writeAuthorities.removeRow(index);
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<img src="${pageContext.request.contextPath}/static/img/icon_board.png"/>&nbsp;
	<spring:message code="application.text.board"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Boards											-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<div class="title2">
					<i class="icon-search"></i>
				</div>
				<select data-juice="ComboBox" data-juice-bind="boardSearch.key" data-juice-options="boardSearchKeys" style="width:100px;"></select>
				<input data-juice="TextField" data-juice-bind="boardSearch.value" style="width:100px;"/>
				<button onclick="javascript:getBoards();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addMessage();">
					<i class="icon-plus"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="boardsTable" data-juice="Grid" data-juice-bind="boards" data-juice-item="board">
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
						<spring:message code="application.text.message"/>
						<spring:message code="application.text.id"/>
					</th>
					<th>
						<spring:message code="application.text.message"/>
						<spring:message code="application.text.name"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="{{$context.board.get('id')}}" onclick="javascript:getBoard(this.dataset.id);">
					<td class="text-center">
						{{boardSearch.get('rows')*(boardSearch.get('page')-1)+$context.index+1}}
					</td>
					<td class="{{$context.board.get('systemDataYn')=='Y'?'systemData':''}}">
						<label data-juice="Label" data-juice-bind="board.id" class="id"></label>
					</td>
					<td><label data-juice="Label" data-juice-bind="board.name"></label></td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-juice="Pagination" data-juice-bind="boardSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getBoards(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
	</div>
	<div class="division" style="width:50%;">
		<!-- ====================================================== -->
		<!-- Board Details										-->
		<!-- ====================================================== -->
		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<i class="icon-key"></i>
					<spring:message code="application.text.board"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:saveBoard();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteBoard();">
					<i class="icon-trash"></i>
					<spring:message code="application.text.remove"/>
				</button>
			</div>
		</div>
		<table id="boardTable" class="detail">
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
					
					<input class="id" data-juice="TextField" data-juice-bind="board.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="board.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.layout"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="board.layoutId"/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.skin"/>
					</span>
				</th>
				<td>
					<input data-juice="TextField" data-juice-bind="board.skinId"/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<i class="icon-key"></i>
						<spring:message code="application.text.access"/>
						<spring:message code="application.text.policy"/>
					</span>
				</th>
				<td>
					<select id="accessPolicySelect" data-juice="ComboBox" data-juice-bind="board.accessPolicy" data-juice-options="policies" style="width:15rem;"></select>
					<table id="accessAuthoritiesTable" data-juice="Grid" data-juice-bind="accessAuthorities" data-juice-item="authority">
						<colgroup>
							<col style="width:40%;"/>
							<col style="width:50%;"/>
							<col style="width:10%;"/>
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
			<tr>
				<th>
					<i class="icon-key"></i>
					<spring:message code="application.text.read"/>
					<spring:message code="application.text.policy"/>
				</th>
				<td>
					<select id="readPolicySelect" data-juice="ComboBox" data-juice-bind="board.readPolicy" data-juice-options="policies" style="width:15rem;"></select>
					<table id="readAuthoritiesTable" data-juice="Grid" data-juice-bind="readAuthorities" data-juice-item="authority">
						<colgroup>
							<col style="width:40%;"/>
							<col style="width:50%;"/>
							<col style="width:10%;"/>
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
									<button class="small" onclick="javascript:addReadAuthority();">
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
									<button class="small" data-index="{{$context.index}}" onclick="javascript:removeReadAuthority(this.dataset.index);">
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
					<spring:message code="application.text.write"/>
					<spring:message code="application.text.policy"/>
				</th>
				<td>
					<select id="writePolicySelect" data-juice="ComboBox" data-juice-bind="board.writePolicy" data-juice-options="policies" style="width:15rem;"></select>
					<table id="writeAuthoritiesTable" data-juice="Grid" data-juice-bind="writeAuthorities" data-juice-item="authority">
						<colgroup>
							<col style="width:40%;"/>
							<col style="width:50%;"/>
							<col style="width:10%;"/>
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
									<button class="small" onclick="javascript:addWriteAuthority();">
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
									<button class="small" data-index="{{$context.index}}" onclick="javascript:removeWriteAuthority(this.dataset.index);">
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
					<spring:message code="application.text.rowsPerPage"/>
				</th>
				<td>
					<select data-juice="ComboBox" data-juice-bind="board.rowsPerPage" data-juice-options="rowsPerPage" style="width:10rem;"></select>
				</td>
			</tr>
			<tr>
				<th>
					카테고리 사용
				</th>
				<td>
					<input type="radio" data-juice="Radio" data-juice-bind="board.categoryUseYn" value="N"/>
					미사용
					<input type="radio" data-juice="Radio" data-juice-bind="board.categoryUseYn" value="Y"/>
					사용
					<select data-juice="ComboBox" data-juice-bind="board.rowsPerPage" data-juice-options="rowsPerPage" style="width:10rem;"></select>
				</td>
			</tr>
		</table>
	</div>
</div>
