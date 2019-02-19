<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval+10}">
		<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/static/img/application.ico">
		
		<!-- duice -->
		<link rel="stylesheet" href="//duice.oopscraft.net/src/duice.css">
		<script src="//duice.oopscraft.net/src/duice.js"></script>
		 
		<script src="${pageContext.request.contextPath}/static/lib/jquery.js"></script>
 		<script src="${pageContext.request.contextPath}/static/lib/moment-with-locales.min.js"></script>
 		<script src="${pageContext.request.contextPath}/static/lib/Chart.js/Chart.js"></script>
 		<link href="${pageContext.request.contextPath}/static/icon/css/icon.css" rel="stylesheet">
 		
 		<!-- polyfill -->
		<script src="${pageContext.request.contextPath}/static/lib/polyfill/dataset.js"></script>
		<script src="${pageContext.request.contextPath}/static/lib/polyfill/classList.js"></script>
		
 		<!-- web font -->
 		<link href="${pageContext.request.contextPath}/static/font/code.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-kr.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-ja.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-zh.css" rel="stylesheet" type="text/css" />

		<!-- global -->
		<script type="text/javascript">
		var __loader; 
        $(document).ajaxStart(function(event) {
       		__loader = new duice.ui.__().load(document.body);
        });

        // If not configure, "Provisional headers are shown" error occured in chrome.
        $(document).ajaxSend(function(event, jqxhr, settings) {
        	jqxhr.setRequestHeader('Cache-Control','no-cache, no-store, must-revalidate');
        	jqxhr.setRequestHeader('Pragma','no-cache');
        	jqxhr.setRequestHeader('Expires','0');
        });

		// checks stop event
        $(document).ajaxStop(function(event) {
        	if(__loader){
       			__loader.release();
        	}
        });
        
		// Checks error except cancelation(readyState = 0)
        $(document).ajaxError(function(event, jqXHR, ajaxSettings,thrownError ){
        	console.log(jqXHR);
        	if(jqXHR.readyState > 0){
	        	console.log(event);
	        	console.log(jqXHR);
	        	console.log(ajaxSettings);
	        	console.log(thrownError);
	        	alert(jqXHR.responseText);
        	}
        });
		
		var __menus = new duice.data.Tree([
			{
				name:'<spring:message code="application.label.monitor"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_monitor.png',
				value:'monitor' 
			},
			{
				name:'<spring:message code="application.label.user"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_user.png',
				value:'user' 
			},
			{
				name:'<spring:message code="application.label.group"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_group.png',
				value:'group'
				 
			},
			{
				name:'<spring:message code="application.label.role"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_role.png',
				value:'role' 
			},
			{
				name:'<spring:message code="application.label.authority"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_authority.png',
				value:'authority' 
			},
			{
				name:'<spring:message code="application.label.property"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_property.png',
				value:'property'
			},
			{
				name:'<spring:message code="application.label.message"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_message.png',
				value:'message'
			},
			{
				name:'<spring:message code="application.label.code"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_code.png',
				value:'code' 
			},
			{
				name:'<spring:message code="application.label.menu"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_menu.png',
				value:'menu'
			},
			{
				name:'<spring:message code="application.label.board"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_board.png',
				value:'board'
			},
			{
				name:'<spring:message code="application.label.api"/>', 
				icon:'${pageContext.request.contextPath}/static/img/icon_api.png',
				value:'javascript:window.open(\'/swagger-ui.html\');'
			}
		], 'childMenus');
		__menus.setEnable(false);
		
		// defines menus index
		__menus.setIndex(
			__menus.indexOf(function(node){
				return window.location.href.includes(node.get('value'));
			})
		);
		
		/**
		 * login user information
		 */
		var __user = new duice.data.Map(${app:toJson(__user)});
		__user.set('language', '${pageContext.response.locale}');
		__user.afterChange(function(event){
			if(event.name == 'language'){
				window.location = '?language=' + event.value;
			}
		});	
		
		/**
		 * Gets languages
		 */
		var __languages = new Array();
		$( document ).ready(function() {
			$.ajax({
				 url: '${pageContext.request.contextPath}/api/locale/languages'
				,type: 'GET'
				,data: {}
				,success: function(data, textStatus, jqXHR) {
					console.log(data);
					data.forEach(function(item){
						__languages.push(item);
					});
					__user.notifyObservers();
				}
			});
		});
		
		/**
		 * Generates random ID
		 */ 
		function __generateRandomId() {
			return duice.util.RandomUtils.generateUUID().replace(/-/g,'').toUpperCase();	
		}
        
        /**
         * Parsed total count from Content-Range header
         * @Param {Object} jqXHR
         */
        function __parseTotalCount(jqXHR){
        	var contentRange = jqXHR.getResponseHeader("Content-Range");
        	try {
            	var totalCount = contentRange.split(' ')[1].split('/')[1];
        		return parseInt(totalCount);
        	}catch(e){
        		return -1;
        	}
        }
         
        /**
         * Checks value is empty
         */
		function __isEmpty(value){
			return duice.util.StringUtils.isEmpty(value);
		}
         
        /**
         * Validates ID value
         * @Param {String} id value
         */
        var __validator = {
        	// Checks ID	 
         	checkId: function(id) {
         		// Checks empty
         		if(duice.util.StringUtils.isEmpty(id)){
             		<spring:message code="application.text.id" var="item"/>
       				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
				// Validates generic
				if(duice.util.StringUtils.isGenericId(id) == false){
					throw '<spring:message code="application.message.invalidIdFormat"/>';
				}
				// length
				if(duice.util.StringUtils.isLengthBetween(id,4,32) == false){
					<spring:message code="application.text.id" var="item"/>
					throw '<spring:message code="application.message.itemMustLengthBetween" arguments="${item},4,32"/>';
				}
         	},
         	checkPassword: function(password, passwordConfirm){
         		if(duice.util.StringUtils.isEmpty(password)){
             		<spring:message code="application.text.password" var="item"/>
       				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
        		if(password != passwordConfirm){
        			<spring:message code="application.text.password" var="item"/>
        			throw '<spring:message code="application.message.itemNotMatch" arguments="${item}"/>';
        		}
        		if(duice.util.StringUtils.isGenericPassword(password) == false){
        			throw '<spring:message code="application.message.invalidPassowrdFormat"/>';
        		}
         	},
         	// Checks name
         	checkName: function(name){
         		// Checks empty
         		if(duice.util.StringUtils.isEmpty(name)){
             		<spring:message code="application.text.name" var="item"/>
       				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
         		// check length
         		if(duice.util.StringUtils.isLengthBetween(name,1,256) == false){
					<spring:message code="application.text.name" var="item"/>
					throw '<spring:message code="application.message.itemMustLengthBetween" arguments="${item},4,32"/>';
         		}
         	},
         	// Checks email address
         	checkEmailAddress: function(value){
         		if(duice.util.StringUtils.isEmailAddress(value) == false){
         			throw '<spring:message code="application.message.invalidEmailAddressFormat"/>';
         		}
         	},
         	// Checks locale 
         	checkLocale: function(value){
         		if(duice.util.StringUtils.isEmpty(value)){
					<spring:message code="application.text.locale" var="item"/>
					throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
         	},
         	// Checks phone number
         	checkPhoneNumber: function(value){
         		if(duice.util.StringUtils.isPhoneNumber(value) == false){
         			throw '<spring:message code="application.message.invalidPhoneNumberFormat"/>';
         		}
         	}
        }

        /**
         * Creates WebSocketClient instance
         */
        var __wsUrl = (window.location.protocol == 'https' ? 'wss' : 'ws') + '://'
        			+ window.location.hostname 
        			+ (window.location.port ? ':'+window.location.port: '') + '/'
        			+ '${pageContext.request.contextPath}'
        			+ 'admin/admin.ws';
        var __webSocketClient = new duice.util.WebSocketClient(__wsUrl);
        __webSocketClient.onMessage(function(event){
        	//console.log(event.data);
        });
        __webSocketClient.open();
		</script>
		<style type="text/css">
		* {
			margin: 0px;
			padding: 0px;
			font-size: inherit;
			font-family: inherit;
			line-height: inherit;
		}
		a, a:hover, a:active {
			color: inherit;
			text-decoration: none; 
			outline: none
		}
		img {
			border: none;
			vertical-align: middle;
		}
		img.icon {
			height: 1.5em;
			width: 1.5em;
		}
		th {
			font-weight:normal;
		}
		html {
			font-size: 12px;
			line-height: 2;
			font-family: font, font-kr, font-ja, font-zh, sans-serif;
			color: #555;
		}
		body {
			min-width: 1366px;
		}
		body > header {
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 70px;
			background-color: #eee;
			border: none;
			border-bottom: groove 2px #ccc;
			padding: 0rem 0.5rem;
		}
		body > header nav.topNav {
			display: inline-block;
		}
		body > main {
			display: flex;
			align-items: center;
			justify-content: space-between;
			min-height: calc(100vh - 70px - 50px);
			border: none;
		}
		.leftNav {
			display: block;
			align-self: stretch;
		    margin: 0.1rem;
		    padding: 1rem;
		    padding-left: 0.1rem;
			width: 15%;
			min-width: 150px;
			border: none;
		    border-right: dotted 1px #ccc;
		}
		.leftNav .menuItem {
			padding: 0.25rem 0rem;
			border-bottom:dotted 1px #ccc;
			cursor:hand;
			cursor:pointer;
		}
		.leftNav .menuItem:hover {
			background-color: #eee;
			transition: all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms;
		}
		body > main > section {
			align-self: stretch;
			width: 100%;
			border: none;
			padding: 10px;
		}
		body > footer {
			height: 50px;
			display: flex;
			align-items: center;
			justify-content: center;
			border: none;
			border-top: dotted 1px #ccc;
			padding: 0rem 0.5rem;
		}
		dialog {
			display: none;
		}
		.title1 {
		    padding: 0rem 0.5rem;
            font-size: 1.2rem;
            font-weight: bold;
            width: 100%;
		}
		.title2 {
			display: inline-block;
			font-size: 1.1rem;
			font-weight: bold;
		}
		i {
			font-size: inherit;
		}
		table.detail {
			width: 100%;
			border-collapse: collapse;
			border: solid 1px #ddd;
		}
		table.detail > tbody > tr > th {
			border: solid 1px #ddd;
			background-color: #eee;
			padding-left: 1.0rem;
			padding-right: 1.0rem;
		}
		table.detail > tbody > tr > td {
			border: solid 1px #ddd;
			padding: 1px;
		}
		table.detail > tbody > tr > td > table {
			border: solid 0px;
		}
		button{
			position:relative;
			border: solid 1px #ccc;
			border-radius: 3px;
			background-color: #fff;
			padding:0 1rem;
			cursor:pointer;
			transition:200ms ease all;
			outline:none;
		}
		button:hover{
			outline: none;
			background-color: #eee;
			border: solid 1px gray;
		}
		button.small {
			background-color: #fff;
			padding: 0rem 0.4rem;
		}
		button.small:hover {
			background-color: #eee;
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
		}
		.systemData:after {
			content:"*";
			font-weight: bold;
			color: red;
		}
		.id {
			color: black;
		}
		.must:before {
			content:"*";
			font-weight: bold;
			color: red;
		}
		.link {
			cursor: hand;
			cursor: pointer;
			text-decoration: underline;
		}
		.code {
			font-family: code, consolas;
		}
		.text-left {
			text-align: left !important;
		}
		.text-center {
			text-align: center !important;
		}
		.text-right {
			text-align: right !important;
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
					<img src="${pageContext.request.contextPath}/static/img/application.png"/>
				</a>
			</span>
			<nav class="topNav" style="padding-right:10px;">
				<sec:authorize access="isAuthenticated()">
					<span>
						<img data-duice="Image" data-duice-bind="__user.avatar" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_avatar.png" style="width:32px; height:32px; border-radius:50%;"/>
						<span data-duice="Text" data-duice-bind="__user.nickname"></span>
					</span>
					&nbsp;&nbsp;
					<span>
						<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_logout.png"/>
						<a href="${pageContext.request.contextPath}/admin/logout">
							<spring:message code="application.label.logout"/>
						</a>
					</span>
				</sec:authorize>
				&nbsp;&nbsp;|&nbsp;&nbsp;
				<span>
					<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_language.png"/>
					<spring:message code="application.label.language"/>
					<select data-duice="ComboBox" data-duice-bind="__user.language" data-duice-options="__languages" data-duice-option-value="language" data-duice-option-text="displayName" style="width:10rem;"></select>
				</span>
			</nav>
		</header>
		<main>
			<!-- ====================================================== -->
			<!-- Navigation												-->
			<!-- ====================================================== -->
			<nav class="leftNav">
				<ul data-duice="TreeView" data-duice-bind="__menus" data-duice-item="menu">
					<li>
						<a data-index="[[$context.index]]" href="[[$context.menu.get('value')]]" class="menuItem" style="display:block;">
							<img class="icon" data-duice="Image" data-duice-bind="menu.icon" data-duice-width="24" data-duice-height="24" src="" alt="" style="vertical-align:middle;"/>
							<span data-duice="Text" data-duice-bind="menu.name"></span>
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
			<img src="${pageContext.request.contextPath}/static/img/copyright.png"/>
		</footer>

		<!-- ====================================================== -->
		<!-- Groups Dialog											-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        /**
         * Gets roles and open dialog
         */
        var __groupsDialog = {
	 		items: new duice.data.Tree(),
	 		unique: false,
	 		handler: {},
	 		setUnique: function(unique){
	 			this.unique = unique;
	 			return this;
	 		},
	 		setDisable: function(handler){
	 			this.handler.disable = handler;
	 			return this;
	 		},
	 		afterConfirm: function(handler){
	 			this.handler.afterConfirm = handler;
	 			return this;
	 		},
			open: function(callback){
				var $this = this;
		 		this.items.fromJson([],'childGroups');
				var title = '<spring:message code="application.text.group"/> <spring:message code="application.text.list"/>';
		 		this.dialog = new duice.ui.Dialog(title, $('#__groupsDialog')[0]);
				this.search();
			},
	 		search: function(){
	 			var $this = this;
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getGroups'
            		,type: 'GET'
            		,data: {}
            		,success: function(data, textStatus, jqXHR) {
            			$this.items.fromJson(data,'childGroups');

            			// Disabled node
            			if($this.handler.disable){
            				$this.items.forEach(function(node){
            					if($this.handler.disable.call($this,node)){
            						node.set('__selected', true);
            						node.setEnable(false);
            					}
            				});
            			}
            			$('#__groupsUl').hide().fadeIn();
            			$this.dialog.open();
               	 	}
            	});	
	 		},
	 		select: function(id){
	 			if(this.unique == true){
					this.items.forEach(function(node){
						if(node.enable == true){
							if(node.get('id') == id){
								node.set('__selected', true);
							}else{
								node.set('__selected', false);
							}
						}
					});
	 			}else{
	 				this.items.forEach(function(node){
						if(node.enable == true){
							if(node.get('id') == id){
								if(node.get('__selected')){
									node.set('__selected', false);
								}else{
									node.set('__selected', true);
								}
							}
						}
	 				});
	 			}
	 		},
	 		confirm: function(){
	 			if(this.unique == true){
	 				var node = this.items.findNode(function(node){
	 					if(node.enable == true && node.get('__selected') == true){
	 						return true;
	 					}
	 				});
		 			this.handler.afterConfirm.call(this, node);
	 			}else{
	 				var nodes = new duice.data.List();
	 				this.items.findNodes(function(node){
	 					if(node.enable == true && node.get('__selected') == true){
	 						return true;
	 					}
	 				}).forEach(function(item){
	 					nodes.addRow(item);
	 				});
	 				this.handler.afterConfirm.call(this, nodes);
	 			}
	 			// close
	 			this.close();
	 		},
	 		close: function() {
	 			this.unique = false;
	 			this.handler = {};
	 			this.dialog.close();
	 		}
        };
        </script>
        <style type="text/css">
		#__groupsDialog {
			width: 500px;
			max-height: 100vh;
		}
		#__groupsDialog li > div {
			border-bottom:dotted 1px #ccc;
			line-height: 2rem;
		}
        </style>
		<dialog>
			<div id="__groupsDialog">
				<div>
					<ul id="__groupsUl" data-duice="TreeView" data-duice-bind="__groupsDialog.items" data-duice-item="item">
						<li>
							<div data-id="[[$context.item.get('id')]]" data-enable="[[$context.item.enable]]" onclick="javascript:this.dataset.enable == 'false' || __groupsDialog.select(this.dataset.id);" style="width:100%;cursor:hand;cursor:pointer;">
								<input data-duice="CheckBox" data-duice-bind="item.__selected"/>
								<span data-duice="Text" data-duice-bind="item.name"></span>
							</div>
						</li>
					</ul>
				</div>
				<br/>
				<div style="text-align:right;">
					<button onclick="javascript:__groupsDialog.confirm();">
						<i class="icon-confirm"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>

		<!-- ====================================================== -->
		<!-- Roles Dialog										-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        /**
         * Gets roles and open dialog
         */        
        var __rolesDialog = {
           	searchCondition: new duice.data.Map(),
           	searchConditionKeys: [
				 { value:'', text:'- <spring:message code="application.text.all"/> -' }
				,{ value:'ID', text:'<spring:message code="application.text.id"/>' }
				,{ value:'NAME', text:'<spring:message code="application.text.name"/>' }
	 		],
	 		searchItems: new duice.data.List(),
	 		selectItems: new duice.data.List(),
	 		handler: {},
	 		option: new duice.data.Map(),
	 		setDisable: function(handler){
	 			this.handler.disable = handler;
	 			return this;
	 		},
	 		afterConfirm: function(handler){
	 			this.handler.afterConfirm = handler;
	 			return this;
	 		},
			open: function(){
				var $this = this;
	 			this.searchCondition.fromJson({
		 			 rows: 10
			 		,page: 1
		 			,searchType: null
		 			,searchValue: null
		 			,totalCount:-1
	 			});
		 		this.searchItems.fromJson([]);
		 		this.selectItems.fromJson([]);
				var title = '<spring:message code="application.text.role"/> <spring:message code="application.text.list"/>';
		 		this.dialog = new duice.ui.Dialog(title, $('#__rolesDialog')[0]);
				this.dialog.open();
				this.search(1);
			},
	 		search: function(page){
	 			var $this = this;
	 			if(page){
            		this.searchCondition.set('page',page);
            	}
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getRoles'
            		,type: 'GET'
            		,data: this.searchCondition.toJson()
            		,success: function(data, textStatus, jqXHR) {
        				$this.searchItems.fromJson(data);
        				
            			// Disabled node
            			if($this.handler.disable){
            				console.log($this.searchItems);
            				$this.searchItems.forEach(function(row){
            					if($this.handler.disable.call($this,row)){
            						row.set('__selected', true);
            						row.setEnable(false);
            					}
            				});
            			}
            			
            			// check selected
            			$this.searchItems.forEach(function(row){
            				var $row = row;
            				var alreadySelected = $this.selectItems.contains(function(row){
            					return row.get('id') == $row.get('id'); 
            				});
            				if(alreadySelected){
            					row.set('__selected',true);
            				}
            			});
            			
            			$this.searchCondition.set('totalCount', __parseTotalCount(jqXHR));
            			$('#__rolesTable > tbody').hide().fadeIn();
               	 	}
            	});	
	 		},
			select: function(id){
				var $this = this;
 				this.searchItems.forEach(function(row){
 					if(row.enable == false) {
 						return;
 					}
					if(row.get('id') == id){
						if(row.get('__selected')){
							row.set('__selected', false);
							var index = $this.selectItems.indexOf(function(row){
								return row.get('id') == id;
							});
							$this.selectItems.removeRow(index);
						}else{
							row.set('__selected', true);
							$this.selectItems.addRow(row);
						}
					}
 				});
			},
   			unselect: function(id) {
   				var index = this.selectItems.indexOf(function(row){
   					return (row.get('id') == id);
   				});
   				this.selectItems.removeRow(index);
   				this.searchItems.forEach(function(row){
   					if(row.get('id') == id){
   						row.set('__selected', false);
   					}
   				});
   			},
			confirm: function() {
				if(this.handler.afterConfirm){
					this.handler.afterConfirm.call(this, this.selectItems);
				}
	 			// close
	 			this.close();
			},
	 		close: function() {
	 			this.handler = {};
	 			this.dialog.close();
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
						<select data-duice="ComboBox" data-duice-bind="__rolesDialog.searchCondition.searchType" data-duice-options="__rolesDialog.searchConditionKeys" style="width:100px;"></select>
						<input data-duice="TextField" data-duice-bind="__rolesDialog.searchCondition.searchValue" style="width:100px;"/>
					</div>
					<div>
						<button onclick="javascript:__rolesDialog.search();">
							<i class="icon-search"></i>
							<spring:message code="application.text.search"/>
						</button>
					</div>
				</div>
				<table id="__rolesTable" data-duice="Grid" data-duice-bind="__rolesDialog.searchItems" data-duice-item="item">
					<colgroup>
						<col style="width:5%;"/>
						<col style="width:15%;"/>
						<col/>
						<col/>
					</colgroup>
					<thead>
						<tr>
							<th>
								-
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
						<tr data-id="[[$context.item.get('id')]]" data-enable="[[$context.item.enable]]" onclick="javascript:this.dataset.enable == 'false' ||  __rolesDialog.select(this.dataset.id);">
							<td class="text-center"><input data-duice="CheckBox" data-duice-bind="item.__selected"/></td>
							<td class="text-center">
								[[__rolesDialog.searchCondition.get('rows')*(__rolesDialog.searchCondition.get('page')-1)+$context.index+1]]
							</td>
							<td><span data-duice="Text" data-duice-bind="item.id" class="id"></span></td>
							<td><span data-duice="Text" data-duice-bind="item.name"></span></td>
						</tr>
					</tbody>
				</table>
				<div>
					<ul data-duice="Pagination" data-duice-bind="__rolesDialog.searchCondition" data-duice-rows="rows" data-duice-page="page" data-duice-total-count="totalCount" data-duice-page-size="5">
						<li data-page="[[$context.page]]" onclick="javascript:__rolesDialog.doSearch(this.dataset.page);">[[$context.page]]</li>
					</ul>
				</div>
				<br/>
				<table data-duice="Grid" data-duice-bind="__rolesDialog.selectItems" data-duice-item="item">
					<colgroup>
						<col/>
						<col/>
						<col style="width:5%;"/>
					</colgroup>
					<thead>
						<tr>
							<th><spring:message code="application.text.id"/></th>
							<th><spring:message code="application.text.name"/></th>
							<th>-</th>
						</tr>
					</thead>
					<tbody>
						<tr data-index="[[$context.index]]" data-enable="[[$context.item.enable]]">
							<td><span data-duice="Text" data-duice-bind="item.id" class="id"></span></td>
							<td><span data-duice="Text" data-duice-bind="item.name"></span></td>
							<td class="text-center">
								<button class="small" data-id="[[$context.item.get('id')]]" onclick="javascript:__rolesDialog.unselect(this.dataset.id);">
									<i class="icon-remove"></i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<div style="text-align:right;">
					<button onclick="javascript:__rolesDialog.confirm();">
						<i class="icon-confirm"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>	

		<!-- ====================================================== -->
		<!-- Authorities Dialog										-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        var __authoritiesDialog = {
			searchCondition: new duice.data.Map(),
			searchConditionKeys: [
   				 { value:'', text:'- <spring:message code="application.text.all"/> -' }
   				,{ value:'ID', text:'<spring:message code="application.text.id"/>' }
   				,{ value:'NAME', text:'<spring:message code="application.text.name"/>' }
   	 		],
   	 		searchItems: new duice.data.List(),
   	 		selectItems: new duice.data.List(),
   	 		handler: {},
   	 		option: new duice.data.Map(),
   	 		setDisable: function(handler){
   	 			this.handler.disable = handler;
   	 			return this;
   	 		},
   	 		afterConfirm: function(handler){
   	 			this.handler.afterConfirm = handler;
   	 			return this;
   	 		},
   			open: function(){
   				var $this = this;
   	 			this.searchCondition.fromJson({
   		 			 page: 1
   		 			,rows: 10
   		 			,searchType: null
   		 			,searchValue: null
   		 			,totalCount:-1
   	 			});
   		 		this.searchItems.fromJson([]);
   		 		this.selectItems.fromJson([]);
   		 		this.dialog = new duice.ui.Dialog($('#__authoritiesDialog')[0]);
   		 		this.dialog.setTitle('<spring:message code="application.text.authority"/> <spring:message code="application.text.list"/>');
   				this.dialog.open();
   				this.search(1);
   			},
   	 		search: function(page){
   	 			var $this = this;
   	 			if(page){
               		this.searchCondition.set('page',page);
               	}
               	$.ajax({
               		 url: '${pageContext.request.contextPath}/admin/getAuthorities'
               		,type: 'GET'
               		,data: this.searchCondition.toJson()
               		,success: function(data, textStatus, jqXHR) {
           				$this.searchItems.fromJson(data);
           				
               			// Disabled node
               			if($this.handler.disable){
               				console.log($this.searchItems);
               				$this.searchItems.forEach(function(row){
               					if($this.handler.disable.call($this,row)){
               						row.set('__selected', true);
               						row.setEnable(false);
               					}
               				});
               			}
               			
               			// check selected
               			$this.searchItems.forEach(function(row){
               				var $row = row;
               				var alreadySelected = $this.selectItems.contains(function(row){
               					return row.get('id') == $row.get('id'); 
               				});
               				if(alreadySelected){
               					row.set('__selected',true);
               				}
               			});
               			
               			$this.searchCondition.set('totalCount', __parseTotalCount(jqXHR));
               			$('#__rolesTable > tbody').hide().fadeIn();
                  	 }
               	});	
   	 		},
   			select: function(id){
   				var $this = this;
    				this.searchItems.forEach(function(row){
    					if(row.enable == false) {
    						return;
    					}
   					if(row.get('id') == id){
   						if(row.get('__selected')){
   							row.set('__selected', false);
   							var index = $this.selectItems.indexOf(function(row){
   								return row.get('id') == id;
   							});
   							$this.selectItems.removeRow(index);
   						}else{
   							row.set('__selected', true);
   							$this.selectItems.addRow(row);
   						}
   					}
    				});
   			},
   			unselect: function(id) {
   				var index = this.selectItems.indexOf(function(row){
   					return (row.get('id') == id);
   				});
   				this.selectItems.removeRow(index);
   				this.searchItems.forEach(function(row){
   					if(row.get('id') == id){
   						row.set('__selected', false);
   					}
   				});
   			},
   			confirm: function() {
   				if(this.handler.afterConfirm){
   					this.handler.afterConfirm.call(this, this.selectItems);
   				}
   	 			// close
   	 			this.close();
   			},
   	 		close: function() {
   	 			this.handler = {};
   	 			this.dialog.close();
   	 		}
		}
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
						<select data-duice="ComboBox" data-duice-bind="__authoritiesDialog.searchCondition.searchType" data-duice-options="__authoritiesDialog.searchConditionKeys" style="width:100px;"></select>
						<input data-duice="TextField" data-duice-bind="__authoritiesDialog.searchCondition.searchValue" style="width:100px;"/>
					</div>
					<div>
						<button onclick="javascript:__authoritiesDialog.search();">
							<i class="icon-search"></i>
							<spring:message code="application.text.search"/>
						</button>
					</div>
				</div>
				<table id="__authoritiesTable" data-duice="Grid" data-duice-bind="__authoritiesDialog.searchItems" data-duice-item="item">
					<colgroup>
						<col style="width:5%;"/>
						<col style="width:15%;"/>
						<col/>
						<col/>
					</colgroup>
					<thead>
						<tr>
							<th>
								-
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
						<tr data-id="[[$context.item.get('id')]]" data-enable="[[$context.item.enable]]" onclick="javascript:this.dataset.enable == 'false' ||  __authoritiesDialog.select(this.dataset.id);">
							<td class="text-center">
								<input data-duice="CheckBox" data-duice-bind="item.__selected"/>
							</td>
							<td class="text-center">
								[[__authoritiesDialog.searchCondition.get('rows')*(__authoritiesDialog.searchCondition.get('page')-1)+$context.index+1]]
							</td>
							<td><span data-duice="Text" data-duice-bind="item.id" class="id"></span></td>
							<td><span data-duice="Text" data-duice-bind="item.name"></span></td>
						</tr>
					</tbody>
				</table>
				<div>
					<ul data-duice="Pagination" data-duice-bind="__authoritiesDialog.searchCondition" data-duice-rows="rows" data-duice-page="page" data-duice-total-count="totalCount" data-duice-page-size="5">
						<li data-page="[[$context.page]]" onclick="javascript:__authoritiesDialog.search(this.dataset.page);">[[$context.page]]</li>
					</ul>
				</div>
				<br/>
				<table data-duice="Grid" data-duice-bind="__authoritiesDialog.selectItems" data-duice-item="item">
					<colgroup>
						<col/>
						<col/>
						<col style="width:5%;"/>
					</colgroup>
					<thead>
						<tr>
							<th><spring:message code="application.text.id"/></th>
							<th><spring:message code="application.text.name"/></th>
							<th>-</th>
						</tr>
					</thead>
					<tbody>
						<tr data-index="[[$context.index]]" data-enable="[[$context.item.enable]]">
							<td><span data-duice="Text" data-duice-bind="item.id" class="id"></span></td>
							<td><span data-duice="Text" data-duice-bind="item.name"></span></td>
							<td class="text-center">
								<button class="small" data-id="[[$context.item.get('id')]]" onclick="javascript:__authoritiesDialog.unselect(this.dataset.id);">
									<i class="icon-remove"></i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<div style="text-align:right;">
					<button onclick="javascript:__authoritiesDialog.confirm();">
						<i class="icon-confirm"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>		
		
		<!-- ====================================================== -->
		<!-- Menus Dialog											-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        /**
         * Gets menus dialog
         */
        var __menusDialog = {
	 		items: new duice.data.Tree(),
	 		handler: {},
	 		setDisable: function(handler){
	 			this.handler.disable = handler;
	 			return this;
	 		},
	 		afterConfirm: function(handler){
	 			this.handler.afterConfirm = handler;
	 			return this;
	 		},
			open: function(callback){
				var $this = this;
		 		this.items.fromJson([],'childGroups');
   				var title = '<img class="icon" src="${pageContext.request.contextPath}/static/img/icon_menu.png"/>&nbsp;&nbsp;<spring:message code="application.text.menu"/> <spring:message code="application.text.list"/>';
		 		this.dialog = new duice.ui.Dialog(title, $('#__menusDialog')[0]);
				this.search();
			},
	 		search: function(){
	 			var $this = this;
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getMenus'
            		,type: 'GET'
            		,data: {}
            		,success: function(data, textStatus, jqXHR) {
            			$this.items.fromJson(data,'childMenus');

            			// Disabled node
            			if($this.handler.disable){
            				$this.items.forEach(function(node){
            					if($this.handler.disable.call($this,node)){
            						node.set('__selected', true);
            						node.setEnable(false);
            					}
            				});
            			}
            			$('#__menusUl').hide().fadeIn();
            			$this.dialog.open();
               	 	}
            	});	
	 		},
	 		select: function(id){
				this.items.forEach(function(node){
					if(node.enable == true){
						if(node.get('id') == id){
							node.set('__selected', true);
						}else{
							node.set('__selected', false);
						}
					}
				});
	 		},
	 		confirm: function(){
 				var node = this.items.findNode(function(node){
 					if(node.enable == true && node.get('__selected') == true){
 						return true;
 					}
 				});
	 			this.handler.afterConfirm.call(this, node);

		 		// close
	 			this.close();
	 		},
	 		close: function() {
	 			this.handler = {};
	 			this.dialog.close();
	 		}
        };
        </script>
        <style type="text/css">
		#__menusDialog {
			width: 500px;
			max-height: 100vh;
		}
		#__menusDialog li > div {
			border-bottom:dotted 1px #ccc;
			padding: 0.25rem 0rem;
			line-height: 2rem;
		}
        </style>
		<dialog>
			<div id="__menusDialog">
				<div>
					<ul id="__menusUl" data-duice="TreeView" data-duice-bind="__menusDialog.items" data-duice-item="item">
						<li>
							<div data-id="[[$context.item.get('id')]]" data-enable="[[$context.item.enable]]" onclick="javascript:this.dataset.enable == 'false' || __menusDialog.select(this.dataset.id);" style="width:100%;cursor:hand;cursor:pointer;">
								<input data-duice="CheckBox" data-duice-bind="item.__selected"/>
								<img class="icon" data-duice="Image" data-duice-bind="item.icon" data-duice-width="24" data-duice-height="24" data-duice-readonly="true" src="${pageContext.request.contextPath}/static/img/icon_empty.png"/>
								<span data-duice="Text" data-duice-bind="item.name"></span>
							</div>
						</li>
					</ul>
				</div>
				<br/>
				<div style="text-align:right;">
					<button onclick="javascript:__menusDialog.confirm();">
						<i class="icon-confirm"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>
		
	</body>
</html>
