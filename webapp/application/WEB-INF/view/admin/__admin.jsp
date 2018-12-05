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
		<meta name="viewport" content="width=1024, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval+10}">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>
 		<link href="${pageContext.request.contextPath}/lib/icon/css/icon.css" rel="stylesheet">
 		<script src="${pageContext.request.contextPath}/lib/moment-with-locales.min.js"></script>
 		<script src="${pageContext.request.contextPath}/lib/Chart.js/Chart.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
		var __progress = new juice.ui.Progress();
        $(document).ajaxStart(function () {
        	__progress.start();
        });

        $(document).ajaxStop(function () {
        	__progress.end();
        });
        
        $(document).ajaxError(function(event, jqXHR, ajaxSettings,thrownError ){
        	console.log(event);
        	console.log(jqXHR);
        	console.log(ajaxSettings);
        	console.log(thrownError);
        	alert(jqXHR.responseText);
        });
        
        /* slide mobile navigation */
        function __viewResponsiveNav() {
        	var mainNav = $('main > nav');
        	mainNav.slideToggle(200);
        }
        
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
         * webSocketClient
         */
        var __wsUrl = (window.location.protocol == 'https' ? 'wss' : 'ws') + '://'
        			+ window.location.hostname 
        			+ (window.location.port ? ':'+window.location.port: '') + '/'
        			+ '${pageContext.request.contextPath}'
        			+ 'admin/admin.ws';
        var __webSocketClient = new juice.util.WebSocketClient(__wsUrl);
        __webSocketClient.onMessage(function(event){
        	//console.log(event.data);
        });
        __webSocketClient.open();
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
			height: 50px;
			background-color: #f7f7f7;
			border: solid 1px #ccc;
		}
		body > header nav.topNav {
			display: inline-block;
		}
		body > header nav.responsiveNav {
			display: none;
		}
		body > main {
			display: flex;
			align-items: center;
			justify-content: space-between;
			min-height: 768px;
			border: solid 0px #efefef;
		}
		body > main > nav {
			display: block;
			align-self: stretch;
			width: 200px;
			padding: 0px;
			border: solid 1px #ccc;
   			background-color: #fff;
		}
		body > main > nav > ul {
		    list-style: none;
		}
		body > main > nav > ul li {
			font-weight: normal;
			background-color: white;
			padding: 5px;
			border-top: solid 1px white;
			border-bottom: solid 1px #ccc;
		}
		body > main > nav > ul li:hover {
			font-weight: bold;
			background-color: #f7f7f7;
			cursor: hand;
			cursor: pointer;
		}
		body > main > nav > ul li > a {
			display: block;
			text-decoration: none !important;
		}
		body > main > section {
			align-self: stretch;
			width: 100%;
			border: solid 1px #efefef;
			padding: 10px;
		}
		body > footer {
			display: flex;
			align-items: center;
			justify-content: space-between;
			border: solid 1px #efefef;
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
		i {
			font-size: inherit;
		}
		table.detail {
			width: 100%;
			border-collapse: collapse;
			border: solid 1px #cccccc;
		}
		table.detail > tbody > tr > th {
			border: solid 1px #efefef;
			background-color: #fafafa;
			padding-left: 1.0rem;
			padding-right: 1.0rem;
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
			background-color: #f7f7f7;
			border-radius: 2px;
			font-weight: bold;
			cursor: hand;
			cursor: pointer;
		}
		a {
			text-decoration: none;
			cursor: hand;
			cursor: pointer;
		}
		a:hover {
			text-decoration: underline;
		}
		.container {
			display: flex;
			justify-content: space-between;
		}
		.division {
			border: dotted 1px #ccc;
			border-radius: 3px;
			margin: 5px;
			padding: 1rem;
			min-height:400px;
		}
		.id {
			font-weight: bold;
		}
		.must:before {
			content:"*";
			color: red;
		}
		.link {
			cursor: hand;
			cursor: pointer;
			text-decoration: underline;
		}
		.text-left {
			text-align: left !important;
			padding-left: 1rem !important;
		}
		.text-center {
			text-align: center !important;
		}
		.text-right {
			text-align: right !important;
			padding-right: 1rem;
		}
		</style>
	</head>
	<body>
		<!-- ====================================================== -->
		<!-- Header													-->
		<!-- ====================================================== -->
		<header>
			<span>
				<a href="monitor">
					<img src="${pageContext.request.contextPath}/img/application.png"/>
				</a>
			</span>
			<nav class="topNav" style="padding-right:10px;">
				<span>
					<i class="icon-globe"></i>
					<spring:message code="application.label.language"/>
					<select onchange="javascript:__changeLanguage(this.value)">
						<option value="en" ${pageContext.response.locale == 'en'?'selected':''}>English</option>
						<option value="ko" ${pageContext.response.locale == 'ko'?'selected':''}>한국어</option>
					</select>
				</span>
				&nbsp;
				<span>
					<i class="icon-logout"></i>
					<a href="${pageContext.request.contextPath}/admin/logout">
						<spring:message code="application.label.logout"/>
					</a>
				</span>
			</nav>
			<nav class="responsiveNav">
				<i class="icon-list" style="font-size:16px; padding-right:10px;" onclick="javascript:__viewResponsiveNav();"></i>
			</nav>
		</header>
		<main>
			<!-- ====================================================== -->
			<!-- Navigation												-->
			<!-- ====================================================== -->
			<nav>
				<ul>
					<li>
						<a href="monitor">
							<i class="icon-monitor"></i>
							<spring:message code="application.label.monitor"/>
						</a>
					</li>
					<li>
						<a href="user">
							<i class="icon-user"></i>
							<spring:message code="application.label.user"/>
						</a>
					</li>
					<li>
						<a href="group">
							<i class="icon-folder"></i>
							<spring:message code="application.label.group"/>
						</a>
					</li>
					<li>
						<a href="role">
							<i class="icon-card"></i>
							<spring:message code="application.label.role"/>
						</a>
					</li>
					<li>
						<a href="authority">
							<i class="icon-key"></i>
							<spring:message code="application.label.authority"/>
						</a>
					</li>
					<li>
						<a href="#menu">
							<i class="icon-list"></i>
							<spring:message code="application.label.menu"/>
						</a>
					</li>
					<li>
						<a href="#code">
							<i class="icon-code"></i>
							<spring:message code="application.label.code"/>
						</a>
					</li>
					<li>
						<a href="#message">
							<i class="icon-message"></i>
							<spring:message code="application.label.message"/>
						</a>
					</li>
					<li>
						<a href="#template">
							<i class="icon-template"></i>
							<spring:message code="application.label.template"/>
						</a>
					</li>
					<li>
						<a href="#content">
							<i class="icon-content"></i>
							<spring:message code="application.label.content"/>
						</a>
					</li>
					<li>
						<a href="#board">
							<i class="icon-bbs"></i>
							<spring:message code="application.label.board"/>
						</a>
					</li>
					<li>
						<a href="#notice">
							<i class="icon-notice"></i>
							<spring:message code="application.label.notice"/>
						</a>
					</li>
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
		<!-- Groups Dialog											-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        /**
         * Gets roles and open dialog
         */
        var __groupsDialog = {
	 		groups: new juice.data.Tree(),
	 		handler: {},
	 		disable: function(handler){
	 			this.handler.disable = handler;
	 			return this;
	 		},
	 		// open dialog
			open: function(callback){
				this.callback = callback;
				this.dialog = new juice.ui.Dialog($('#__groupsDialog')[0]);
				this.dialog.setTitle('<spring:message code="application.text.group"/> <spring:message code="application.text.list"/>');
				this.getGroups(); 
				this.dialog.open();
			},
			// gets roles
	 		getGroups: function(){
            	var $this = this;
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getGroups'
            		,type: 'GET'
            		,data: {}
            		,success: function(data, textStatus, jqXHR) {
            			$this.groups.fromJson(data,'childGroups');
            			$('#__groupsUl').hide().fadeIn();
            			
            			// disable
            			if($this.handler.disable){
            				var indexes = $this.groups.findIndexes($this.handler.disable);
            				indexes.forEach(function(index){
            					var node = $this.groups.getNode(index);
            					node.set('__disabled', true);
            				});
            			}
               	 	}
            	});	
            	console.log('@@@@@@@@@@@@@@@@@@' + $this.groups);
	 		},
	 		// selects rows by index
			select: function(index){
				index = JSON.parse(index);
				var node = this.groups.getNode(index);
				if(node.get('__selected') == true){
					node.set('__selected', false);
				}else{
					node.set('__selected', true);
				}
			},
			// confirm
			confirm: function(){
				var indexes = this.groups.findIndexes(function(node){
					if(node.get('__selected') == true){
						return true;
					}
				});
				var selectedGroups = []; 
				$this = this;
				indexes.forEach(function(index){
					var node = $this.groups.getNode(index);
					selectedGroups.push(node);
				});
				this.callback.call(this,selectedGroups);
				this.dialog.close();
			}
        };
        </script>
        <style type="text/css">
		#__groupsDialog {
			width: 500px;
			max-height: 100vh;
			padding: 1rem;
		}
		#__groupsDialog li > div {
			border-bottom:dotted 1px #ccc;
			line-height: 2rem;
		}
        </style>
		<dialog>
			<div id="__groupsDialog">
				<div>
					<ul id="__groupsUl" data-juice="TreeView" data-juice-bind="__groupsDialog.groups" data-juice-item="group">
						<li>
							<div data-index="{{$context.index}}" onclick="javascript:__groupsDialog.select(this.dataset.index);" style="width:100%;cursor:hand;cursor:pointer;">
								<input data-juice="CheckBox" data-juice-bind="group.__selected" readonly/>
								<label data-juice="Label" data-juice-bind="group.name"></label>
								{{$context.index}}
							</div>
						</li>
					</ul>
				</div>
				<br/>
				<div style="text-align:right;">
					<button onclick="javascript:__groupsDialog.confirm();">
						<i class="icon-check"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>

		<!-- ====================================================== -->
		<!-- Roles Dialog											-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        /**
         * Gets roles and open dialog
         */        
        var __rolesDialog = {
           	search: new juice.data.Map(),
	 		searchKeys: [
				 { value:'', text:'- <spring:message code="application.text.all"/> -' }
				,{ value:'id', text:'<spring:message code="application.text.id"/>' }
				,{ value:'name', text:'<spring:message code="application.text.name"/>' }
	 		],
	 		option: new juice.data.Map(),
	 		roles: new juice.data.List(),
	 		// open dialog
			open: function(callback){
				this.callback = callback;
	 			this.search.fromJson({
		   			 key: null
		 			,value: null
		 			,page: 1
		 			,rows: 10
		 			,totalCount:-1
	 			});
				this.dialog = new juice.ui.Dialog($('#__rolesDialog')[0]);
				this.dialog.setTitle('<spring:message code="application.text.role"/> <spring:message code="application.text.list"/>');
				this.getRoles(1); 
				this.dialog.open();
			},
			// gets roles
	 		getRoles: function(page){
            	if(page){
            		this.search.set('page',page);
            	}
            	$this = this;
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getRoles'
            		,type: 'GET'
            		,data: this.search.toJson()
            		,success: function(data, textStatus, jqXHR) {
            			$this.roles.fromJson(data);
            			$this.search.set('totalCount', __parseTotalCount(jqXHR));
            			$('#__rolesTable > tbody').hide().fadeIn();
               	 	}
            	});	
	 		},
	 		// selects rows by index
			select: function(index){
				if(this.roles.getRow(index).get('__selected') == true){
					this.roles.getRow(index).set('__selected', false);
				}else{
					this.roles.getRow(index).set('__selected', true);
				}
			},
			// selects all rows
			selectAll: function(){
				this.option.set('selectAll', this.option.get('selectAll') == true ? false : true);
				for(var i = 0, size = this.roles.getRowCount(); i < size; i ++){
					this.roles.getRow(i).set('__selected', this.option.get('selectAll'));
				}
			},
			/* confirm selected rows */
			confirm: function(){
	        	var selectedRoles = new juice.data.List();
	        	for(var i = 0, size = this.roles.getRowCount(); i < size; i ++){
	        		var role = this.roles.getRow(i);
	        		if(role.get('__selected') == true){
	        			selectedRoles.addRow(role);
	        		}
	        	}				
				if(this.callback.call(this, selectedRoles) == false){
					return false;
				}else{
					this.dialog.close();					
				}
			}
        };
        </script>
        <style type="text/css">
		#__rolesDialog {
			width: 500px;
		}
        </style>
		<dialog>
			<div id="__rolesDialog">
				<div style="display:flex; justify-content: space-between;">
					<div style="flex:auto;">
						<div class="title2">
							<i class="icon-search"></i>
						</div>
						<select data-juice="ComboBox" data-juice-bind="__rolesDialog.search.key" data-juice-options="__rolesDialog.searchKeys" style="width:100px;"></select>
						<input data-juice="TextField" data-juice-bind="__rolesDialog.search.value" style="width:100px;"/>
					</div>
					<div>
						<button onclick="javascript:__rolesDialog.getRoles();">
							<i class="icon-search"></i>
							<spring:message code="application.text.search"/>
						</button>
					</div>
				</div>
				<table id="__rolesTable" data-juice="Grid" data-juice-bind="__rolesDialog.roles" data-juice-item="role">
					<thead>
						<tr>
							<th>
								<input data-juice="CheckBox" data-juice-bind="__rolesDialog.option.selectAll" onclick="javascript:__rolesDialog.selectAll();"/>
							</th>
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
						<tr data-index="{{$context.index}}" onclick="javascript:__rolesDialog.select(this.dataset.index);">
							<td><input data-juice="CheckBox" data-juice-bind="role.__selected"/></td>
							<td>{{$context.index+1}}</td>
							<td><label data-juice="Label" data-juice-bind="role.id" class="id"></label></td>
							<td><label data-juice="Label" data-juice-bind="role.name"></label></td>
						</tr>
					</tbody>
				</table>
				<div>
					<ul data-juice="Pagination" data-juice-bind="__rolesDialog.search" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
						<li data-page="{{$context.page}}" onclick="javascript:__rolesDialog.getRoles(this.dataset.page);">{{$context.page}}</li>
					</ul>
				</div>
				<div style="text-align:right;">
					<button onclick="javascript:__rolesDialog.confirm();">
						<i class="icon-check"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>		

		<!-- ====================================================== -->
		<!-- Authorities Dialog										-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        /**
         * Gets authorities and open dialog
         */        
        var __authoritiesDialog = {
           	search: new juice.data.Map(),
	 		searchKeys: [
				 { value:'', text:'- <spring:message code="application.text.all"/> -' }
				,{ value:'id', text:'<spring:message code="application.text.id"/>' }
				,{ value:'name', text:'<spring:message code="application.text.name"/>' }
	 		],
	 		option: new juice.data.Map(),
	 		authorities: new juice.data.List(),
	 		/* open dialog */
			open: function(callback){
				this.callback = callback;
	 			this.search.fromJson({
		   			 key: null
		 			,value: null
		 			,page: 1
		 			,rows: 10
		 			,totalCount:-1
	 			});
		 		this.authorities.fromJson([]);
				this.dialog = new juice.ui.Dialog($('#__authoritiesDialog')[0]);
				this.dialog.setTitle('<spring:message code="application.text.authority"/> <spring:message code="application.text.list"/>');
				this.getAuthorities(1); 
				this.dialog.open();
			},
			/* gets authorities */
	 		getAuthorities: function(page){
            	if(page){
            		this.search.set('page',page);
            	}
            	$this = this;
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getAuthorities'
            		,type: 'GET'
            		,data: this.search.toJson()
            		,success: function(data, textStatus, jqXHR) {
            			$this.authorities.fromJson(data);
            			$this.search.set('totalCount', __parseTotalCount(jqXHR));
            			$('#__authoritiesTable > tbody').hide().fadeIn();
               	 	}
            	});	
	 		},
	 		/* selects rows by index */
			select: function(index){
				if(this.authorities.getRow(index).get('__selected') == true){
					this.authorities.getRow(index).set('__selected', false);
				}else{
					this.authorities.getRow(index).set('__selected', true);
				}
			},
			/* selects all rows */
			selectAll: function(){
				this.option.set('selectAll', this.option.get('selectAll') == true ? false : true);
				for(var i = 0, size = this.authorities.getRowCount(); i < size; i ++){
					this.authorities.getRow(i).set('__selected', this.option.get('selectAll'));
				}
			},
			/* confirm selected rows */
			confirm: function(){
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
        </script>
        <style type="text/css">
		#__authoritiesDialog {
			width: 500px;
		}
        </style>
		<dialog>
			<div id="__authoritiesDialog">
				<div style="display:flex; justify-content: space-between;">
					<div style="flex:auto;">
						<div class="title2">
							<i class="icon-search"></i>
						</div>
						<select data-juice="ComboBox" data-juice-bind="__authoritiesDialog.search.key" data-juice-options="__authoritiesDialog.searchKeys" style="width:100px;"></select>
						<input data-juice="TextField" data-juice-bind="__authoritiesDialog.search.value" style="width:100px;"/>
					</div>
					<div>
						<button onclick="javascript:__authoritiesDialog.getAuthorities();">
							<i class="icon-search"></i>
							<spring:message code="application.text.search"/>
						</button>
					</div>
				</div>
				<table id="__authoritiesTable" data-juice="Grid" data-juice-bind="__authoritiesDialog.authorities" data-juice-item="authority">
					<thead>
						<tr>
							<th>
								<input data-juice="CheckBox" data-juice-bind="__authoritiesDialog.option.selectAll" onclick="javascript:__authoritiesDialog.selectAll();"/>
							</th>
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
						<tr data-index="{{$context.index}}" onclick="javascript:__authoritiesDialog.select(this.dataset.index);">
							<td><input data-juice="CheckBox" data-juice-bind="authority.__selected"/></td>
							<td>{{$context.index+1}}</td>
							<td><label data-juice="Label" data-juice-bind="authority.id" class="id"></label></td>
							<td><label data-juice="Label" data-juice-bind="authority.name"></label></td>
						</tr>
					</tbody>
				</table>
				<div>
					<ul data-juice="Pagination" data-juice-bind="__authoritiesDialog.search" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
						<li data-page="{{$context.page}}" onclick="javascript:__authoritiesDialog.getAuthorities(this.dataset.page);">{{$context.page}}</li>
					</ul>
				</div>
				<div style="text-align:right;">
					<button onclick="javascript:__authoritiesDialog.confirm();">
						<i class="icon-check"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>		
		
	</body>
</html>
