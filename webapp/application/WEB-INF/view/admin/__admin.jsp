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
		<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/img/application.ico">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>
 		<link href="${pageContext.request.contextPath}/lib/icon/css/icon.css" rel="stylesheet">
 		<script src="${pageContext.request.contextPath}/lib/moment-with-locales.min.js"></script>
 		<script src="${pageContext.request.contextPath}/lib/Chart.js/Chart.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
		var __loader; 
        $(document).ajaxStart(function () {
       		__loader = new juice.ui.__().load(document.body);
        });

        $(document).ajaxStop(function () {
        	if(__loader){
       			__loader.release();
        	}
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
         * @Param {String} language code
         */
        function __changeLanguage(lang) {
        	window.location = "?lang=" + lang;
        }
        
        /**
         * Parsed total count from Content-Range header
         * @Param {Object} jqXHR
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
         * Creates WebSocketClient instance
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
						<option value="ja" ${pageContext.response.locale == 'ja'?'selected':''}>日本</option>
						<option value="zh" ${pageContext.response.locale == 'zh'?'selected':''}>中国</option>
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
	 		items: new juice.data.Tree(),
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
				this.dialog = new juice.ui.Dialog($('#__groupsDialog')[0]);
				this.dialog.setTitle('<i class="icon-folder"> </i><spring:message code="application.text.group"/> <spring:message code="application.text.list"/>');
				this.dialog.open();
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
            				$this.items.startTransaction();
            				$this.items.forEach(function(node){
            					if($this.handler.disable.call($this,node)){
            						node.set('__selected', true);
            						node.setEnable(false);
            					}
            				});
            				$this.items.endTransaction();
            			}
            			$('#__groupsUl').hide().fadeIn();
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
	 				var nodes = new juice.data.List();
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
					<ul id="__groupsUl" data-juice="TreeView" data-juice-bind="__groupsDialog.items" data-juice-item="item">
						<li>
							<div data-id="{{$context.item.get('id')}}" data-enable="{{$context.item.enable}}" onclick="javascript:this.dataset.enable == 'false' || __groupsDialog.select(this.dataset.id);" style="width:100%;cursor:hand;cursor:pointer;">
								<input data-juice="CheckBox" data-juice-bind="item.__selected"/>
								<label data-juice="Label" data-juice-bind="item.name"></label>
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
		<!-- Roles Dialog										-->
		<!-- ====================================================== -->
		<script type="text/javascript">
        /**
         * Gets roles and open dialog
         */        
        var __rolesDialog = {
           	searchCondition: new juice.data.Map(),
           	searchConditionKeys: [
				 { value:'', text:'- <spring:message code="application.text.all"/> -' }
				,{ value:'id', text:'<spring:message code="application.text.id"/>' }
				,{ value:'name', text:'<spring:message code="application.text.name"/>' }
	 		],
	 		searchItems: new juice.data.List(),
	 		selectItems: new juice.data.List(),
	 		handler: {},
	 		option: new juice.data.Map(),
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
		   			 key: null
		 			,value: null
		 			,page: 1
		 			,rows: 10
		 			,totalCount:-1
	 			});
		 		this.searchItems.fromJson([]);
		 		this.selectItems.fromJson([]);
				this.dialog = new juice.ui.Dialog($('#__rolesDialog')[0]);
				this.dialog.setTitle('<i class="icon-card"> </i><spring:message code="application.text.role"/> <spring:message code="application.text.list"/>');
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
            					if($this.handler.disable.call($this,row) == false){
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
						<div class="title2">
							<i class="icon-search"></i>
						</div>
						<select data-juice="ComboBox" data-juice-bind="__rolesDialog.searchCondition.key" data-juice-options="__rolesDialog.searchConditionKeys" style="width:100px;"></select>
						<input data-juice="TextField" data-juice-bind="__rolesDialog.searchCondition.value" style="width:100px;"/>
					</div>
					<div>
						<button onclick="javascript:__rolesDialog.search();">
							<i class="icon-search"></i>
							<spring:message code="application.text.search"/>
						</button>
					</div>
				</div>
				<table id="__rolesTable" data-juice="Grid" data-juice-bind="__rolesDialog.searchItems" data-juice-item="item">
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
						<tr data-id="{{$context.item.get('id')}}" data-enable="{{$context.item.enable}}" onclick="javascript:this.dataset.enable == 'false' ||  __rolesDialog.select(this.dataset.id);">
							<td class="text-center"><input data-juice="CheckBox" data-juice-bind="item.__selected"/></td>
							<td class="text-center">
								{{__rolesDialog.searchCondition.get('rows')*(__rolesDialog.searchCondition.get('page')-1)+$context.index+1}}
							</td>
							<td><label data-juice="Label" data-juice-bind="item.id" class="id"></label></td>
							<td><label data-juice="Label" data-juice-bind="item.name"></label></td>
						</tr>
					</tbody>
				</table>
				<div>
					<ul data-juice="Pagination" data-juice-bind="__rolesDialog.searchCondition" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
						<li data-page="{{$context.page}}" onclick="javascript:__rolesDialog.doSearch(this.dataset.page);">{{$context.page}}</li>
					</ul>
				</div>
				<br/>
				<table data-juice="Grid" data-juice-bind="__rolesDialog.selectItems" data-juice-item="item">
					<thead>
						<tr>
							<th><spring:message code="application.text.id"/></th>
							<th><spring:message code="application.text.name"/></th>
							<th>-</th>
						</tr>
					</thead>
					<tbody>
						<tr data-index="{{$context.index}}" data-enable="{{$context.item.enable}}">
							<td><label data-juice="Label" data-juice-bind="item.id" class="id"></label></td>
							<td><label data-juice="Label" data-juice-bind="item.name"></label></td>
							<td class="text-center">
								<button data-id="{{$context.item.get('id')}}" onclick="javascript:__rolesDialog.unselect(this.dataset.id);">
									<i class="icon-minus"></i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				<br/>
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
	 		searchList: new juice.data.List(),
	 		selectList: new juice.data.List(),
	 		option: new juice.data.Map(),
	 		setFilter: function(filter){
	 			this.filter = filter;
	 			return this;
	 		},
			open: function(callback){
				var $this = this;
				this.callback = callback;
	 			this.search.fromJson({
		   			 key: null
		 			,value: null
		 			,page: 1
		 			,rows: 10
		 			,totalCount:-1
	 			});
		 		this.searchList.fromJson([]);
		 		this.selectList.fromJson([]);
				this.dialog = new juice.ui.Dialog($('#__authoritiesDialog')[0]);
				this.dialog.setTitle('<i class="icon-key"> </i><spring:message code="application.text.authority"/> <spring:message code="application.text.list"/>');
				this.doSearch(1); 
				this.dialog.afterClose(function() {
					$this.close();
				}).open();
			},
			close: function() {
				this.filter = null;
				this.dialog.close();
			},
			/* gets authorities */
	 		doSearch: function(page){
	 			var $this = this;
	 			if(page){
            		this.search.set('page',page);
            	}
            	$.ajax({
            		 url: '${pageContext.request.contextPath}/admin/getAuthorities'
            		,type: 'GET'
            		,data: this.search.toJson()
            		,success: function(data, textStatus, jqXHR) {
        				$this.searchList.fromJson([]);
            			data.forEach(function(item){
            				var row = new juice.data.Map(item);
            				
            				// checks filter
            				if($this.filter){
            					if($this.filter.call($this,row) == false){
            						row.set('__selected', true);
            						row.setEnable(false);
            					}
            				}
            				// exites in selected row
            				if($this.isAlreadySelected(row.get('id'))){
            					row.set('__selected',true);
            				}
            				
            				// adds row into list.
        					$this.searchList.addRow(row);
            			});
            			$this.search.set('totalCount', __parseTotalCount(jqXHR));
            			$('#__authoritiesTable > tbody').hide().fadeIn();
               	 	}
            	});	
	 		},
	 		isAlreadySelected: function(id){
	 			var indexOf = this.selectList.indexOf(function(row){
	 				return row.get('id') == id;
	 			});
	 			return indexOf > -1;
	 		},
	 		/* selects rows by index */
			select: function(id){
				var index = this.searchList.indexOf(function(row){
					return row.get('id') == id;
				});
				if(index > -1){
					var row = this.searchList.getRow(index);
					row.set('__selected', true);
				}
				this.selectList.addRow(row);
			},
			unselect: function(id){
				var index = this.searchList.indexOf(function(row){
					return row.get('id') == id;
				});
				if(index > -1){
					this.searchList.getRow(index).set('__selected', false);
				}
				
				// removes from selectList
				var index = this.selectList.indexOf(function(row){
					return row.get('id') == id;
				});
				if(index > -1){
					this.selectList.removeRow(index);
				}
			},
			toggleSelect: function(id){
				if(this.isAlreadySelected(id) == true){
					this.unselect(id);
				}else{
					this.select(id);
				}
			},
			/* confirm selected rows */
			doConfirm: function(){		
				if(this.callback.call(this, this.selectList) == false){
					return false;
				}else{
					this.close();			
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
						<button onclick="javascript:__authoritiesDialog.doSearch();">
							<i class="icon-search"></i>
							<spring:message code="application.text.search"/>
						</button>
					</div>
				</div>
				<table id="__authoritiesTable" data-juice="Grid" data-juice-bind="__authoritiesDialog.searchList" data-juice-item="item">
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
						<tr data-id="{{$context.item.get('id')}}" data-enable="{{$context.item.enable}}" onclick="javascript:this.dataset.enable == 'false' ||  __authoritiesDialog.toggleSelect(this.dataset.id);">
							<td class="text-center">
								<input data-juice="CheckBox" data-juice-bind="item.__selected"/>
							</td>
							<td class="text-center">
								{{__authoritiesDialog.search.get('rows')*(__authoritiesDialog.search.get('page')-1)+$context.index+1}}
							</td>
							<td><label data-juice="Label" data-juice-bind="item.id" class="id"></label></td>
							<td><label data-juice="Label" data-juice-bind="item.name"></label></td>
						</tr>
					</tbody>
				</table>
				<div>
					<ul data-juice="Pagination" data-juice-bind="__authoritiesDialog.search" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
						<li data-page="{{$context.page}}" onclick="javascript:__authoritiesDialog.doSearch(this.dataset.page);">{{$context.page}}</li>
					</ul>
				</div>
				<br/>
				<table data-juice="Grid" data-juice-bind="__authoritiesDialog.selectList" data-juice-item="item">
					<thead>
						<tr>
							<th><spring:message code="application.text.id"/></th>
							<th><spring:message code="application.text.name"/></th>
							<th>-</th>
						</tr>
					</thead>
					<tbody>
						<tr data-index="{{$context.index}}" data-enable="{{$context.item.enable}}">
							<td><label data-juice="Label" data-juice-bind="item.id" class="id"></label></td>
							<td><label data-juice="Label" data-juice-bind="item.name"></label></td>
							<td class="text-center">
								<button data-id="{{$context.item.get('id')}}" onclick="javascript:__authoritiesDialog.unselect(this.dataset.id);">
									<i class="icon-minus"></i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<div style="text-align:right;">
					<button onclick="javascript:__authoritiesDialog.doConfirm();">
						<i class="icon-check"></i>
						<spring:message code="application.text.confirm"/>
					</button>
				</div>
			</div>
		</dialog>		
		
	</body>
</html>
