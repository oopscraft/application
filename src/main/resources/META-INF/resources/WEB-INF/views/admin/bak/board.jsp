<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<script type="text/javascript">
var boardSearch = new duice.data.Map({
	 rows: 30
	,page: 1
	,searchType: null
	,searchValue: null
	,totalCount:-1
});
var boardSearchTypes = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'ID', text:'<spring:message code="application.text.id"/>' }
	,{ value:'NAME', text:'<spring:message code="application.text.name"/>' }
];
var boards = new duice.data.List();
var board = new duice.data.Map();
var accessAuthorities = new duice.data.List();
var readAuthorities = new duice.data.List();
var writeAuthorities = new duice.data.List();
var categories = new duice.data.List();
var isNew = false;

// skins
var skins = ${app:toJson(skins)};

//policies
var policies = ${app:toJson(policies)};

// rowsPerPage options
var rowsPerPage = ${app:toJson(rowsPerPage)};;

/**
 * clear edit
 */
function clearEdit() {
	board.fromJson({});
	accessAuthorities.fromJson([]);
	readAuthorities.fromJson([]);
	writeAuthorities.fromJson([]);
	categories.fromJson([]);
	onPolicyChanged('access');
	onPolicyChanged('read');
	onPolicyChanged('write');
	onCategoryUseYnChanged(board.get('categoryUseYn'));
}

/**
 * enable edit
 */
function enableEdit(editable) {
	if(editable == true){
		board.setEnable(true);
		board.setReadonly('id', false);
		$('#editDiv').find('button').attr('disabled',false);
	}else{
		board.setEnable(false);
		board.setReadonly('id', true);
		$('#editDiv').find('button').attr('disabled',true);
	}
}

/**
 * On document loaded
 */
$( document ).ready(function() {
	clearEdit();
	enableEdit(false);
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
	$('input[name="categoryUseYn"]').each(function(){
		$(this).click(function(event){
			onCategoryUseYnChanged($(this).val());
		});
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
 * on cateogry use or not changed
 */
function onCategoryUseYnChanged(categoryUseYn){
	if(categoryUseYn == 'Y'){
		$('#categoriesTable').show();
	}else{
		$('#categoriesTable').hide();
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
			clearEdit();
			board.fromJson(data);
			accessAuthorities.fromJson(data.accessAuthorities);
			readAuthorities.fromJson(data.readAuthorities);
			writeAuthorities.fromJson(data.writeAuthorities);
			categories.fromJson(data.categories);
			isNew = false;
			enableEdit(true);
			board.setReadonly('id',true);
			$('#boardTable').hide().fadeIn();
			
			// hide or show authorities table.
			onPolicyChanged('access');
			onPolicyChanged('read');
			onPolicyChanged('write');
			
			// hide or show categories table
			onCategoryUseYnChanged(board.get('categoryUseYn'));
  	 	}
	});	
}

/**
 * Adds board
 */
function addBoard() {
	clearEdit();
	board.set('id', __generateRandomId());
	board.set('skin', skins[0]);
	board.set('accessPolicy', 'ANONYMOUS');
	board.set('readPolicy', 'ANONYMOUS');
	board.set('writePolicy', 'ANONYMOUS');
	board.set('rowsPerPage', 20);
	board.set('categoryUseYn', 'N');
	board.set('replyUseYn', 'N');
	board.set('fileUseYn', 'N');
	boards.clearIndex();
	enableEdit(true);
	isNew = true;
}

/**
 * Saves board.
 */
function saveBoard() {
	
	// Checks validation of board
	if(__isEmpty(board.get('id'))){
		<spring:message code="application.text.id" var="item"/>
		new duice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	if(__isEmpty(board.get('name'))){
		<spring:message code="application.text.name" var="item"/>
		new duice.ui.Alert('<spring:message code="application.message.enterItem" arguments="${item}"/>').open();
		return false;
	}
	
	// checks duplicated id. 
	if(isNew == true){
		var id = board.get('id');
		var isDuplicated = false;
		$.ajax({
			 url: 'board/getBoard'
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
	
	// Saves board
	<spring:message code="application.text.message" var="item"/>
	new duice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
		.afterConfirm(function() {
			var boardJson = board.toJson();
			boardJson.accessAuthorities = accessAuthorities.toJson();
			boardJson.readAuthorities = readAuthorities.toJson();
			boardJson.writeAuthorities = writeAuthorities.toJson();
			boardJson.categories = categories.toJson();
			$.ajax({
				 url: 'board/saveBoard'
				,type: 'POST'
				,data: JSON.stringify(boardJson)
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					getBoard(board.get('id'));
					getBoards();
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
	new duice.ui.Confirm('<spring:message code="application.message.deleteItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: 'board/deleteBoard'
			,type: 'GET'
			,data: { id: board.get('id') }
			,success: function(data, textStatus, jqXHR) {
				clearEdit();
				enableEdit(false);
				getBoards();
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

/**
 * Adds category.
 */
function addCategory(){
	categories.addRow(new duice.data.Map({
		boardId: board.get('id'),
		id: null,
		name: null
	}));
	makeCategoryDisplaySeq();
}

/**
 * Removes category.
 */
function removeCategory(index){
	categories.removeRow(index);
	makeCategoryDisplaySeq();
}

/**
 * moveUpCategory 
 */
function moveUpCategory(index){
	categories.moveRow(index, Math.max(index-1,0));
}

/**
 * moveDownCategory
 */
function moveDownCategory(index){
	categories.moveRow(index, Math.min(index+1,categories.getRowCount()));
}

/**
 * makeCategoryDisplaySeq
 */
function makeCategoryDisplaySeq() {
	var displaySeq = 1;
	categories.forEach(function(item){
		item.set('displaySeq', displaySeq ++);
	});
}

/**
 * Preview board
 */
function openBoard() {
	var pageUrl = '${pageContext.request.contextPath}/board/' + board.get('id');
	 window.open(pageUrl);
}

</script>
<style type="text/css">

</style>
<div class="title1">
	<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_board.png"/>&nbsp;
	<spring:message code="application.text.board"/>
	<spring:message code="application.text.management"/>
</div>
<div class="container" style="min-height:70vh;">
	<!-- ====================================================== -->
	<!-- Boards													-->
	<!-- ====================================================== -->
	<div class="division" style="width:50%;">
		<div style="display:flex; justify-content: space-between;">
			<div style="flex:auto;">
				<select data-duice="ComboBox" data-duice-bind="boardSearch.searchType" data-duice-options="boardSearchTypes" style="width:100px;"></select>
				<input data-duice="TextField" data-duice-bind="boardSearch.searchValue" style="width:100px;"/>
				<button onclick="javascript:getBoards();">
					<i class="icon-search"></i>
					<spring:message code="application.text.search"/>
				</button>
			</div>
			<div>
				<button onclick="javascript:addBoard();">
					<i class="icon-new"></i>
					<spring:message code="application.text.new"/>
				</button>
			</div>
		</div>
		<table id="boardsTable" data-duice="Grid" data-duice-bind="boards" data-duice-item="board">
			<colgroup>
				<col style="width:10%"/>
				<col style="width:30%"/>
				<col/>
				<col style="width:3%"/>
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
				<tr data-id="[[$context.board.get('id')]]" onclick="javascript:getBoard(this.dataset.id);">
					<td class="text-center">
						[[boardSearch.get('rows')*(boardSearch.get('page')-1)+$context.index+1]]
					</td>
					<td class="[[$context.board.get('systemDataYn')=='Y'?'systemData':'']]">
						<span data-duice="Text" data-duice-bind="board.id" class="id"></span>
					</td>
					<td>
						<span data-duice="Text" data-duice-bind="board.name"></span>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<ul data-duice="Pagination" data-duice-bind="boardSearch" data-duice-rows="rows" data-duice-page="page" data-duice-total-count="totalCount" data-duice-page-size="5">
				<li data-page="[[$context.page]]" onclick="javascript:getBoards(this.dataset.page);">[[$context.page]]</li>
			</ul>
		</div>
	</div>
	<!-- ====================================================== -->
	<!-- Board Details										-->
	<!-- ====================================================== -->
	<div id="editDiv" class="division" style="width:50%;">

		<div style="display:flex; justify-content: space-between;">
			<div>
				<div class="title2">
					<spring:message code="application.text.board"/>
					<spring:message code="application.text.details"/>
				</div>
			</div>
			<div>
				<button onclick="javascript:openBoard();">
					<i class="icon-open"></i>
					<spring:message code="application.text.board"/>
					<spring:message code="application.text.open"/>
				</button>
				<button onclick="javascript:saveBoard();">
					<i class="icon-save"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button onclick="javascript:deleteBoard();">
					<i class="icon-delete"></i>
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
					<span class="must">
						<spring:message code="application.text.id"/>
					</span>
				</th>
				<td>
					<input class="id" data-duice="TextField" data-duice-bind="board.id"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="must">
						<spring:message code="application.text.name"/>
					</span>
				</th>
				<td>
					<input data-duice="TextField" data-duice-bind="board.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.icon"/>
					</span>
				</th>
				<td>
					<img data-duice="Image" data-duice-bind="board.icon" data-duice-width="32" data-duice-height="32" src="${pageContext.request.contextPath}/static/img/icon_empty.png"/>
					<img data-duice="Image" data-duice-bind="board.icon" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_empty.png" style="width:24px; height:24px;" />
					<img data-duice="Image" data-duice-bind="board.icon" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_empty.png" style="width:16px; height:16px;" />
				</td>
			</tr>
			<tr>
				<th>
					<span>
						<spring:message code="application.text.skin"/>
					</span>
				</th>
				<td>
					<select data-duice="ComboBox" data-duice-bind="board.skin" data-duice-options="skins" style="width:15rem;"></select>
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
					<select id="accessPolicySelect" data-duice="ComboBox" data-duice-bind="board.accessPolicy" data-duice-options="policies" style="width:15rem;"></select>
					<table id="accessAuthoritiesTable" data-duice="Grid" data-duice-bind="accessAuthorities" data-duice-item="authority">
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
							<tr data-id="[[$context.authority.get('id')]]">
								<td>
									<span data-duice="Text" data-duice-bind="authority.id" class="id"></span>
								</td>
								<td>
									<span data-duice="Text" data-duice-bind="authority.name"></span>
								</td>
								<td class="text-center">
									<button class="small" data-index="[[$context.index]]" onclick="javascript:removeAccessAuthority(this.dataset.index);">
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
					<spring:message code="application.text.read"/>
					<spring:message code="application.text.policy"/>
				</th>
				<td>
					<select id="readPolicySelect" data-duice="ComboBox" data-duice-bind="board.readPolicy" data-duice-options="policies" style="width:15rem;"></select>
					<table id="readAuthoritiesTable" data-duice="Grid" data-duice-bind="readAuthorities" data-duice-item="authority">
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
									<button class="small" onclick="javascript:addReadAuthority();">
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
									<button class="small" data-index="[[$context.index]]" onclick="javascript:removeReadAuthority(this.dataset.index);">
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
					<spring:message code="application.text.write"/>
					<spring:message code="application.text.policy"/>
				</th>
				<td>
					<select id="writePolicySelect" data-duice="ComboBox" data-duice-bind="board.writePolicy" data-duice-options="policies" style="width:15rem;"></select>
					<table id="writeAuthoritiesTable" data-duice="Grid" data-duice-bind="writeAuthorities" data-duice-item="authority">
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
									<button class="small" onclick="javascript:addWriteAuthority();">
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
									<button class="small" data-index="[[$context.index]]" onclick="javascript:removeWriteAuthority(this.dataset.index);">
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
					<spring:message code="application.text.rowsPerPage"/>
				</th>
				<td>
					<select data-duice="ComboBox" data-duice-bind="board.rowsPerPage" data-duice-options="rowsPerPage" style="width:10rem;"></select>
					
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.category"/>
					<spring:message code="application.text.use"/>
				</th>
				<td>
					<div style="padding-left:0.5rem;">
						<input name="categoryUseYn" type="radio" data-duice="Radio" data-duice-bind="board.categoryUseYn" value="Y"/>
						<spring:message code="application.text.use.yes"/>
						<input name="categoryUseYn" type="radio" data-duice="Radio" data-duice-bind="board.categoryUseYn" value="N"/>
						<spring:message code="application.text.use.no"/>
					</div>
					<table id="categoriesTable" data-duice="Grid" data-duice-bind="categories" data-duice-item="category">
						<colgroup>
							<col style="width:10%;"/>
							<col style="width:30%;"/>
							<col/>
							<col style="width:5%;"/>
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
								<th>
									<div style="display:flex;justify-content:center;">
										<button class="small" onclick="javascript:addCategory();">
											<i class="icon-add"></i>
										</button>
									</div>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<span data-duice="Text" data-duice-bind="category.displaySeq"></span>
								</td>
								<td class="text-center">
									<input data-duice="TextField" data-duice-bind="category.id" class="id"/>
								</td>
								<td>
									<input data-duice="TextField" data-duice-bind="category.name" class="id"/>
								</td>
								<td class="text-center">
									<div style="display:flex;justify-content:center;">
										<button class="small" data-index="[[$context.index]]" onclick="javascript:moveUpCategory(this.dataset.index);">
											<i class="icon-up"></i>
										</button>
										<button class="small" data-index="[[$context.index]]" onclick="javascript:moveDownCategory(this.dataset.index);">
											<i class="icon-down"></i>
										</button>
										<button class="small" data-index="[[$context.index]]" onclick="javascript:removeCategory(this.dataset.index);">
											<i class="icon-remove"></i>
										</button>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.reply"/>
					<spring:message code="application.text.use"/>
				</th>
				<td>
					<div style="padding-left:0.5rem;">
						<input type="radio" data-duice="Radio" data-duice-bind="board.replyUseYn" value="Y"/>
						<spring:message code="application.text.use.yes"/>
						<input type="radio" data-duice="Radio" data-duice-bind="board.replyUseYn" value="N"/>
						<spring:message code="application.text.use.no"/>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="application.text.file"/>
					<spring:message code="application.text.attach"/>
					<spring:message code="application.text.use"/>
				</th>
				<td>
					<div style="padding-left:0.5rem;">
						<input type="radio" data-duice="Radio" data-duice-bind="board.fileUseYn" value="Y"/>
						<spring:message code="application.text.use.yes"/>
						<input type="radio" data-duice="Radio" data-duice-bind="board.fileUseYn" value="N"/>
						<spring:message code="application.text.use.no"/>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
