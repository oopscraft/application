<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/static/img/application.ico">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/static/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/static/lib/jquery.js"></script>
		<link href="${pageContext.request.contextPath}/static/icon/css/icon.css" rel="stylesheet">
		
 		<!-- polyfill -->	
		<script src="https://polyfill.io/v3/polyfill.min.js"></script>
		
 		<!-- web font -->
 		<link href="${pageContext.request.contextPath}/static/font/font.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-kr.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-ja.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-zh.css" rel="stylesheet" type="text/css" />
 		
		<script type="text/javascript">
		var user = new juice.data.Map({
			id: null,
			pasword: null,
			language: '${pageContext.response.locale}'
		});
		user.afterChange(function(event){
			if(event.name == 'language'){
				window.location = '?language=' + event.value;
			}
		});
		
		// languages
		var languages = new Array();
		$( document ).ready(function() {
			$.ajax({
				 url: '${pageContext.request.contextPath}/api/locale/languages'
				,type: 'GET'
				,data: {}
				,success: function(data, textStatus, jqXHR) {
					data.forEach(function(item){
						languages.push(item);
					});
					user.notifyObservers();
				}
			});
		});
		
		/**
		 * On document loaded
		 */
		$( document ).ready(function() {
			$('#idInput').val('').focus();
			
			// enter key login
			$(document).on('keyup', function(e) {
				if(e.which == 13) {
					console.log('13');
					doLogin();
				}
			});
			
		});

		/**
		 * Login 
		 */
		function doLogin() {

			if(juice.util.StringUtils.isEmpty(user.get('id'))){
				<spring:message code="application.text.id" var="item"/>
				var message = '<spring:message code="application.message.enterItem" arguments="${item}"/>';
				printMessage(message);
				$('#idInput').focus();
				return false;
			}
			
			if(juice.util.StringUtils.isEmpty(user.get('password'))){
				<spring:message code="application.text.password" var="item"/>
				var message = '<spring:message code="application.message.enterItem" arguments="${item}"/>';
				printMessage(message);
				$('#passwordInput').focus();
				return false;
			}
			
			$.ajax({
				 url: '/admin/login/processing'
				,type: 'POST'
				,data: user.toJson()
				,success: function(data, textStatus, jqXHR) {
					location.href='${pageContext.request.contextPath}/admin';
		    	 }
			 	,error: function(jqXHR, textStatus, errorThrown) {
			 		var message = jqXHR.responseText;
			 		printMessage(message);
				 }
			});
		}
		
		/**
		 * Prints message
		 */
		function printMessage(message) {
			var messageDiv = $('#messageDiv');
			messageDiv.hide().html(message).fadeIn();
		}
		</script>
		<style type="text/css">
		* {
			margin: 0px;
			padding: 0px;
			font-size: inherit;
			font-family: inherit;
			line-height: inherit;
		}
		html {
			font-size: 12px;
			line-height: 2.5;
			font-family: font, font-kr, font-ja, font-zh, sans-serif;
			color: #555;
		}
		body {
			min-width: 1280px;
			display: flex;
			height: 100vh;
			justify-content: center;
			align-items: center;
			background-color: #eee;
		}
		#loginDiv {
			width: 300px;
			margin-bottom: 20vh;
		}
		#loginDiv > div {
			margin:2px;
		}
		input {
			border-bottom: solid 1px #ccc;
			background-color: #fff;
		}
		button{
			position:relative;
			border: solid 1px #ccc !important;
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
		#loginButton {
			width: 100%;
			font-weight: bold;
		}
		#messageDiv {
			text-align: center;
			font-weight: bold;
			color: steelblue;
		}
		</style>
	</head>
	<body>
		<div id="loginDiv">
			<div>
				<img src="${pageContext.request.contextPath}/static/img/application.png"/>
			</div>
			<div>
				<input id="idInput" data-juice="TextField" data-juice-bind="user.id" placeholder="<spring:message code="application.text.id"/>"/>
			</div>
			<div>
				<input id="passwordInput" type="password" data-juice="TextField" data-juice-bind="user.password" placeholder="<spring:message code="application.text.password"/>"/>
			</div>
			<div>
				<select data-juice="ComboBox" data-juice-bind="user.language" data-juice-options="languages" data-juice-option-value="language" data-juice-option-text="displayName" style="background-color:white;"></select>
			</div>
			<div>
				<button id="loginButton" onclick="javascript:doLogin();" style="border:#fff;">
					<spring:message code="application.label.login"/>
				</button>
			</div>
			<div id="messageDiv">
			</div>
		</div>
	</body>
</html>
