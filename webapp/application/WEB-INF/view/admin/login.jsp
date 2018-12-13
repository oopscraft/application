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
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>
		<link href="${pageContext.request.contextPath}/icon/css/icon.css" rel="stylesheet">
		
 		<!-- web font -->
 		<link href="${pageContext.request.contextPath}/font/font.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/font/font-kr.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/font/font-ja.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/font/font-zh.css" rel="stylesheet" type="text/css" />
 		
		<script type="text/javascript">
		var user = new juice.data.Map({
			id: null,
			pasword: null
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

			if(juice.util.validator.isEmpty(user.get('id'))){
				$('#idInput').focus();
				alert('아이디입력해라.');
				return false;
			}
			
			if(juice.util.validator.isEmpty(user.get('password'))){
				$('#passwordInput').focus();
				alert('패스워드 입력해라');
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
			 		var messageDiv = $('#messageDiv');
			 		messageDiv.hide().html('<i class="icon-alert"></i>' + jqXHR.responseText).fadeIn();
				 }
			});
		}
		</script>
		<style type="text/css">
		* {
			margin: 0px;
			padding: 0px;
			margin:0px;
			padding:0px;
			font-family: font, font-kr, font-ja, font-zh, sans-serif;
			font-size:11px;
			color: #555;
			line-height: 20px;
		}
		body {
			display: flex;
			height: 100vh;
			justify-content: center;
			align-items: center;
			background-color: #f7f7f7;
		}
		#loginDiv {
			width: 300px;
			background-color: white;
			border: solid 1px #777;
			border-radius: 2px;
			padding: 20px;
			margin-bottom: 20vh;
		}
		#languageSelect {
			margin: 0px;
			padding-left: 0.5rem;
			padding-right: 0.5rem;
			color: #495057;
			min-width: 100px;
			-webkit-appearance: none;
			border-radius: 2px;
			border: 1px solid #cccccc;
			background-clip: padding-box;
			transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out;
			overflow: hidden;
		}
		button{
			position:relative;
			border: solid 1px #ccc;
			border-radius: 1px;
			background-color: #fafafa;
			padding:0 1rem;
			cursor:pointer;
			transition:200ms ease all;
			outline:none;
			cursor: pointer;
			cursor: hand;
		}
		button:hover{
			border: solid 1px steelblue;
			box-shadow: 0px 0px 1px 1px #ccc;
		}
		#loginButton {
			width: 100%;
			font-weight: bold;
			margin-top: 1.0rem;
		}
		#messageDiv {
			text-align: center;
			padding: 10px;
			font-weight: bold;
			color: steelblue;
		}
		</style>
	</head>
	<body>
		<div id="loginDiv">
			<div>
				<img src="${pageContext.request.contextPath}/img/application.png"/>
			</div>
			<div>
				<i class="icon-user"></i>
				<span style="font-weight:bold;"><spring:message code="application.text.id"/></span>
				<input id="idInput" data-juice="TextField" data-juice-bind="user.id"/>
			</div>
			<div>
				<i class="icon-key"></i>
				<span style="font-weight:bold;"><spring:message code="application.text.password"/></span>
				<input id="passwordInput" type="password" data-juice="TextField" data-juice-bind="user.password"/>
			</div>
			<div style="line-height:3rem;">
				<i class="icon-globe"></i>
				<span style="font-weight:bold;">
					<spring:message code="application.label.language"/>
				</span>
				<select id="languageSelect" onchange="javascript:window.location = '?lang=' + this.value;">
						<option value="en" ${pageContext.response.locale == 'en'?'selected':''}>English</option>
						<option value="ko" ${pageContext.response.locale == 'ko'?'selected':''}>한국어</option>
						<option value="ja" ${pageContext.response.locale == 'ja'?'selected':''}>日本</option>
						<option value="zh" ${pageContext.response.locale == 'zh'?'selected':''}>中国</option>
				</select>
			</div>
			<div>
				<button id="loginButton" onclick="javascript:doLogin();">
					<i class="icon-login"></i>
					<spring:message code="application.label.login"/>
				</button>
			</div>
			<div id="messageDiv">
			</div>
		</div>
	</body>
</html>
