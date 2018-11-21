<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>
		<script type="text/javascript">
		function doSignIn() {
			var signInForm = $('#signInForm');
			var signInFormData = new FormData(signInForm);
			$.ajax({
				 url: '/admin/sign/in'
				,type: 'POST'
				,processData: false
				,contentType: false
				,data: signInFormData
				,success: function(response) {
					location.href='${pageContext.request.contextPath}/admin/dash';
		    	 }
				,error: function(response) {
					$('#messageDiv').text(response.responseText);
				}
			});
		}
		</script>
		<style type="text/css">
		</style>
	</head>
	<body>
			<form id="signInForm" action="/admin/sign/in" method="POST">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<input type="text" name="id"/>
				<input type="password" name="password"/>
				<input type="submit" value="submit"/>
			</form>
			<button onclick="javascript:doSignIn();">Sign In</button>
			<div id="messageDiv">
				${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
			</div>
	</body>
</html>
