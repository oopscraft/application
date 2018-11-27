<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval+10}">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>

		<!-- font icon -->
 		<link href="${pageContext.request.contextPath}/lib/fontawesome/css/all.css" rel="stylesheet">
 		<script defer src="${pageContext.request.contextPath}/lib/fontawesome/js/all.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
        $(document).ajaxStart(function () {
			juice.progress.start();
        });

        $(document).ajaxStop(function () {
        	juice.progress.end();
        });
        
        $(document).ajaxError(function(event, jqXHR, ajaxSettings,thrownError ){
        	console.log(event);
        	console.log(jqXHR);
        	console.log(ajaxSettings);
        	console.log(thrownError);
        	alert(jqXHR.responseText);
        });
        
        /**
         * Prevents Enter keydown event.
         */ 
        document.addEventListener('keydown', function(event) {
            if (event.keyCode === 13) {
                event.preventDefault();
            }
        }, true);
        
        /**
         * Changes language
         */
        function __changeLanguage(lang) {
        	window.location = "?lang=" + lang;
        }
        
        /**
         * Parsed total count from Content-Range header
         */
        function __parseTotalCount(jqXHR){
        	var contentRange = jqXHR.getResponseHeader("Content-Range");
        	var totalCount = contentRange.split(' ')[1].split('/')[1];
        	try {
        		return parseInt(totalCount);
        	}catch(e){
        		return -1;
        	}
        }
        
        /**
         * Gets gropus and open dialog
         */
        var __groups = new juice.data.List();
        var __groupsDialog = $('#__groupsDialog');
        function __openGroupsDialog(callback) {
        	$.ajax({
	       		 url: '${pageContext.request.contextPath}/admin/getGroups'
	       		,type: 'GET'
	       		,data: {}
	       		,success: function(data, textStatus, jqXHR) {
	       			callback.call(callback, 'fdsa');
	          	 }
       		});	
        }
        
        /**
         * Gets roles and open dialog
         */
        var __roles = new juice.data.List();
        var __rolesDialog = $('#__rolesDialog');
        function __openRolesDialog(callback){
        	$.ajax({
	       		 url: '${pageContext.request.contextPath}/admin/getRoles'
	       		,type: 'GET'
	       		,data: {}
	       		,success: function(data, textStatus, jqXHR) {
	       			callback.call(callback, 'fdsa');
	          	 }
      		});	
        }
        
        /**
         * Gets authorities and open dialog
         */        
        var __authoritiesDialog = {
        	//dialog: new juice.ui.Dialog($('#__authoritiesDialog')[0]),
           	authoritySearch: new juice.data.Map({
	   			 key: null
	 			,value: null
	 			,page: 1
	 			,rows: 10
	 			,totalCount:-1
	 		}),
	 		authoritySearchKeys: [
				 { value:'', text:'- <spring:message code="text.all"/> -' }
				,{ value:'id', text:'<spring:message code="text.id"/>' }
				,{ value:'name', text:'<spring:message code="text.name"/>' }
	 		],
	 		authorities: new juice.data.List(),
	 		getAuthorities: function(page){
            	if(page){
            		this.authoritySearch.set('page',page);
            	}
            	$this = this;
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getAuthorities'
            		,type: 'GET'
            		,data: this.authoritySearch.toJson()
            		,success: function(data, textStatus, jqXHR) {
            			$this.authorities.fromJson(JSON.parse(data));
            			$this.authoritySearch.set('totalCount', __parseTotalCount(jqXHR));
            			$('#__authoritiesTable > tbody').hide().fadeIn();
               	 	}
            	});	
	 		},
			open: function(callback){
				this.callback = callback;
				this.getAuthorities(1);
				this.dialog = new juice.ui.Dialog($('#__authoritiesDialog')[0]);
				this.dialog.open();
			},
			selectAuthorities: function(){
	        	var selectedAuthorities = new juice.data.List();
	        	for(var i = 0, size = this.authorities.getRowCount(); i < size; i ++){
	        		var authority = this.authorities.getRow(i);
	        		if(authority.get('__selected') == true){
	        			selectedAuthorities.addRow(authority);
	        		}
	        	}				
				if(this.callback.call(this, selectedAuthorities) == false){
					return false;
				}else{
					this.dialog.close();					
				}
			}
        };

         
/*         
		var __authoritySearch = new juice.data.Map({
			 key: null
			,value: null
			,page: 1
			,rows: 20
			,totalCount:-1
		});
		var __authoritySearchKeys = [
			 { value:'', text:'- <spring:message code="text.all"/> -' }
			,{ value:'id', text:'<spring:message code="text.id"/>' }
			,{ value:'name', text:'<spring:message code="text.name"/>' }
		];
        var __authorities = new juice.data.List();
        function __openDialogAuthorities(callback) {
        	new juice.ui.Dialog($('#__authoritiesDialog')[0]).open();
        	__getAuthorities(1);
        	this.callback = callback;
        }
        function __getAuthorities(page) {
        	if(page){
        		__authoritySearch.set('page',page);
        	}
        	$.ajax({
        		 url: '${pageContext.request.contextPath}/admin/getAuthorities'
        		,type: 'GET'
        		,data: __authoritySearch.toJson()
        		,success: function(data, textStatus, jqXHR) {
        			__authorities.fromJson(JSON.parse(data));
        			__authoritySearch.set('totalCount', __parseTotalCount(jqXHR));
        			$('#__authoritiesTable > tbody').hide().fadeIn();
           	 	}
        	});	
        }
        function __selectAuthorities() {
        	var selectedAuthorities = new juice.data.List();
        	for(var i = 0, size = __authorities.getRowCount(); i < size; i ++){
        		var authority = __authorities.getRow(i);
        		if(authority.get('__selected') == true){
        			selectedAuthorities.addRow(authority);
        		}
        	}
        	console.log(__openDialogAuthorities);
        	console.log(__openDialogAuthorities.callback);
        	__openDialogAuthorities.callback.call(this,selectedAuthorities);
        }
        
*/
		</script>
		<style type="text/css">
		* {
			margin: 0px;
			padding: 0px;
			margin:0px;
			padding:0px;
			letter-spacing: -1px;
			font-family: Verdana,Dotum,sans-serif;
			font-size:11px;
			color: #555;
			line-height: 20px;
		}
		body {
		}
		body > header {
			display: flex;
			align-items: center;
			justify-content: space-between;
			border: solid 1px;
		}
		body > main {
			display: flex;
			align-items: center;
			justify-content: space-between;
			min-height: 768px;
			border: solid 1px;
		}
		body > main > nav {
			align-self: stretch;
			width: 200px;
			border: solid 1px;
			font-weight: bold;
		}
		body > main > section {
			align-self: stretch;
			width: 100%;
			border: solid 1px;
			padding: 10px;
		}
		body > footer {
			display: flex;
			align-items: center;
			justify-content: space-between;
			border: solid 1px;
		}
		@media (max-width: 1024px) {
			body > main {
				flex-direction: column;
			}
			body > main > nav {
				width: 100vw;
				border: solid 1px;
			}
		}
		.title1 {
            font-size: 18px;
            font-weight: bold;
            width: 100%;
		}
		.title2 {
			display: inline-block;
			font-size: 14px;
			font-weight: bold;
		}
		table.detail {
			width: 100%;
			border-collapse: collapse;
			border: solid 1px #cccccc;
		}
		table.detail > tbody > tr > th {
			border: solid 1px #efefef;
			background-color: #fafafa;
			padding: 1px;
			text-align: center;
		}
		table.detail > tbody > tr > td {
			border: solid 1px #efefef;
			padding: 1px;
		}
		table.detail > tbody > tr > td > table {
			border: solid 0px;
		}
		button {
			margin: 1px;
			padding-left: 5px;
			padding-right: 5px;
			border: solid 1px #aaaaaa;
			background-color: #efefef;
			border-radius: 2px;
			font-weight: bold;
			cursor: hand;
			cursor: pointer;
		}
		hr {
			margin: 2px;
			border-top: solid 1px #ccc;
			border-left: solid 1px #ccc;
			border-bottom: solid 1px #efefef;
			border-right: solid 1px #efefef;
		}
		a {
			text-decoration: none;
			cursor: hand;
			cursor: pointer;
		}
		a:hover {
			text-decoration: underline;
		}
		.id {
			font-weight: bold;
		}
		</style>
	</head>
	<body>
		<!-- ====================================================== -->
		<!-- Header													-->
		<!-- ====================================================== -->
		<header>
			<div>
				<a href="dash">
					<img src="${pageContext.request.contextPath}/img/application.png"/>
				</a>
			</div>
			<div style="padding-right:10px;">
				<span>
					<i class="fas fa-globe"></i>
					<select onchange="javascript:__changeLanguage(this.value)">
						<option value="en" ${pageContext.response.locale == 'en'?'selected':''}>English</option>
						<option value="ko" ${pageContext.response.locale == 'ko'?'selected':''}>한국어</option>
					</select>
				</span>
				&nbsp;
				<span>
					<i class="fas fa-power-off"></i>
					<a href="${pageContext.request.contextPath}/admin/logout">
						Logout
					</a>
				</span>
			</div>
		</header>
		<main>
			<!-- ====================================================== -->
			<!-- Navigation												-->
			<!-- ====================================================== -->
			<nav>
				NAV
				<ul>
					<li><a href="user"><spring:message code="label.user"/></a></li>
					<li><a href="group"><spring:message code="label.group"/></a></li>
					<li><a href="role"><spring:message code="label.role"/></a></li>
					<li><a href="authority"><spring:message code="label.authority"/></a></li>
					<li><a href="menu"><spring:message code="label.menu"/></a></li>
					<li><a href="code"><spring:message code="label.code"/></a></li>
					<li><a href="message"><spring:message code="label.message"/></a></li>
					<li><a href="template"><spring:message code="label.template"/></a></li>
					<li><a href="board"><spring:message code="label.board"/></a></li>
					<li><a href="content"><spring:message code="label.content"/></a></li>
				</ul>
			</nav>
			<!-- ====================================================== -->
			<!-- Section												-->
			<!-- ====================================================== -->
			<section>
				<tiles:insertAttribute name="main"/>
			</section>
		</main>
		<!-- ====================================================== -->
		<!-- Footer													-->
		<!-- ====================================================== -->
		<footer>
			FOOTER
		</footer>

		<!-- ====================================================== -->
		<!-- Users Dialog											-->
		<!-- ====================================================== -->
		<dialog>
			<div id="__usersDialog">
			__usersDialog
			</div>
		</dialog>

		<!-- ====================================================== -->
		<!-- Groups Dialog											-->
		<!-- ====================================================== -->
		<dialog>
			<div id="__groupsDialog">
			__groupsDialog 
			</div>
		</dialog>		

		<!-- ====================================================== -->
		<!-- Roles Dialog											-->
		<!-- ====================================================== -->
		<dialog>
			<div id="__rolesDialog">
			__rolesDialog 
			</div>
		</dialog>		

		<!-- ====================================================== -->
		<!-- Authorities Dialog										-->
		<!-- ====================================================== -->
		<dialog>
			<div id="__authoritiesDialog" style="width:800px;">
				<div style="display:flex; justify-content: space-between;">
					<div style="flex:auto;">
						<div class="title2">
							<i class="fas fa-search"></i>
						</div>
						<select data-juice="ComboBox" data-juice-bind="__authoritiesDialog.authoritySearch.key" data-juice-options="__authoritiesDialog.authoritySearchKeys" style="width:100px;"></select>
						<input data-juice="TextField" data-juice-bind="__authoritiesDialog.authoritySearch.value" style="width:100px;"/>
					</div>
					<div>
						<button onclick="javascript:__authoritiesDialog.getAuthorities();">
							<i class="fas fa-search"></i>
							<spring:message code="text.search"/>
						</button>
					</div>
				</div>
				<table id="__authoritiesTable" data-juice="Grid" data-juice-bind="__authoritiesDialog.authorities" data-juice-item="authority">
					<thead>
						<tr>
							<th>c</th>
							<th><spring:message code="text.no"/></th>
							<th><spring:message code="text.id"/></th>
							<th><spring:message code="text.name"/></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input data-juice="CheckBox" data-juice-bind="authority.__selected"/></td>
							<td>{{$context.index+1}}</td>
							<td><label data-juice="Label" data-juice-bind="authority.id" class="id"></label></td>
							<td><label data-juice="Label" data-juice-bind="authority.name"></label></td>
						</tr>
					</tbody>
				</table>
				<div>
					<ul data-juice="Pagination" data-juice-bind="__authoritiesDialog.authoritySearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
						<li data-page="{{$context.page}}" onclick="javascript:__authoritiesDialog.getAuthorities(this.dataset.page);">{{$context.page}}</li>
					</ul>
				</div>
				<button onclick="javascript:__authoritiesDialog.selectAuthorities();">
					선택
				</button>
			</div>
		</dialog>		
		
	</body>
</html>
