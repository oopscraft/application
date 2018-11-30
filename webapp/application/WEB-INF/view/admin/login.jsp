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
		<link href="${pageContext.request.contextPath}/lib/icon/css/icon.css" rel="stylesheet">
		<script type="text/javascript">
		var user = new juice.data.Map();
		function doLogin() {
			var id = user.get('id');
			$.ajax({
				 url: '/admin/login/processing'
				,type: 'POST'
				,data: user.toJson()
				,success: function(data, textStatus, jqXHR) {
					location.href='${pageContext.request.contextPath}/admin';
		    	 }
			 	,error: function(jqXHR, textStatus, errorThrown) {
					$('#messageDiv').text(jqXHR.responseText);
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
			letter-spacing: -1px;
			font-family: Verdana,Dotum,sans-serif;
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
				<spring:message code="text.id"/>
				<input data-juice="TextField" data-juice-bind="user.id"/>
			</div>
			<div>
				<i class="icon-key"></i>
				<spring:message code="text.password"/>
				<input type="password" data-juice="TextField" data-juice-bind="user.password"/>
			</div>
			<div>
				<i class="icon-globe"></i>
				<spring:message code="label.language"/>
				<select onchange="javascript:window.location = '?lang=' + this.value;">
					<option value="en" ${pageContext.response.locale == 'en'?'selected':''}>English</option>
					<option value="ko" ${pageContext.response.locale == 'ko'?'selected':''}>한국어</option>
				</select>
			</div>
			<div>
				<button id="loginButton" onclick="javascript:doLogin();">
					<i class="icon-login"></i>
					Login
				</button>
			</div>
			<div id="messageDiv">
			</div>
		</div>
	</body>
</html>
