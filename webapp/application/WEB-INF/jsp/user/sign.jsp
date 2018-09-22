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
		
		<!-- juice -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/juice/juice.css">
		<script src="${pageContext.request.contextPath}/assets/juice/juice.js"></script>

		<!-- library -->
		<script src="${pageContext.request.contextPath}/assets/jquery.js"></script>
		<script src="${pageContext.request.contextPath}/assets/popper.js"></script>
		
		<!-- bootstrap -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap/css/bootstrap.min.css">
		<script src="${pageContext.request.contextPath}/assets/bootstrap/js/bootstrap.min.js"></script>
		
		<!-- fontawesome -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontawesome/css/fa-svg-with-js.css">
		<script src="${pageContext.request.contextPath}/assets/fontawesome/js/fontawesome-all.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
		/**
		 * Checking keypress
		 */
		$(document).ready(function(){
			$('#username').focus();
		});
		$(document).keypress(function(e) { 
			if (e.keyCode == 13){
				doLogin();
			}
		});
		 
		/**
		 * Request login
		 */
		function doLogin() {
			var loginForm = $('#loginForm');
			var adminInput = $('#admin');
			var passwordInput = $('#password');
			var captchaInput = $('#captcha');
			var formData = new FormData(loginForm);
			formData.append('admin', adminInput.val());
			formData.append('password', passwordInput.val());
			formData.append('captcha', captchaInput.val());
			$.ajax({
				 url: ''
				,processData: false
				,contentType: false
				,data: formData
				,type: 'POST'
				,success: function(response) {
					location.href='${pageContext.request.contextPath}/console';
		    	 }
				,error: function(response) {
					$('#messageDiv').text(response.responseText);
				}
			});
			checkCaptchaRequired();
		}
		</script>
		<style type="text/css">
		main {
			height:100vh; 
			display:flex; 
			justify-content:center; 
			align-items:center;
		}
		#messageDiv {
			color: red;
		}
		</style>
	</head>
	<body>
		<!-- start of body -->
		<main>
			<form action="/user/sign" method="POST">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<input type="text" name="id"/>
				<input type="password" name="password"/>
				<input type="submit" values="Sign In"/>
			</form>
			<div>
				${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
			</div>
		</main>
		<!-- end of body -->
	</body>
</html>
