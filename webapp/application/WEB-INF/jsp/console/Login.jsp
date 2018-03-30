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
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>

		<!-- library -->
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>
		<script src="${pageContext.request.contextPath}/lib/popper.js"></script>
		
		<!-- bootstrap -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/bootstrap/css/bootstrap.min.css">
		<script src="${pageContext.request.contextPath}/lib/bootstrap/js/bootstrap.min.js"></script>
		
		<!-- fontawesome -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/fontawesome/css/fa-svg-with-js.css">
		<script src="${pageContext.request.contextPath}/lib/fontawesome/js/fontawesome-all.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
		/**
		 * Checking keypress
		 */
		$(document).ready(function(){
			checkCaptchaRequired();
			$('#admin').focus();
		});
		$(document).keypress(function(e) { 
			if (e.keyCode == 13){
				doLogin();
			}
		});
		
		/**
		 * checkCaptchaRequired
		 */
		function checkCaptchaRequired() {
			$.ajax({
				 type:'GET'
				,url:'${pageContext.request.contextPath}/console/login/isCaptchaRequired'
				,success: function(captchaRequired) {
					if(captchaRequired == true){
						$('#captchaDiv').show();
					}else{
						$('#captchaDiv').hide();
					}
		    	 }
				,error: function(response) {
					$('#messageDiv').text(response.responseText);
				}
			});
		}

		/**
		 * reloadCaptcha
		 */
		function reloadCaptcha() {
			$.ajax({
				 type:'GET'
				,url:'${pageContext.request.contextPath}/console/login/prepareCaptcha'
				,success: function(response) {
					$('#captchaImage').attr('src','${pageContext.request.contextPath}/console/login/getCaptchaImage');
					$('#captchaAudio').attr('src','${pageContext.request.contextPath}/console/login/getCaptchaAudio');
		    	 }
				,error: function(response) {
					$('#messageDiv').text(response.responseText);
				}
			});
		}
		 
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
			<div class="container" style="max-width:24rem;">
				<div class="row">
					<div class="col">
						<img src="/application/img/logo_application.png"/>
					</div>
				</div>
				<div class="row">
					<div class="col input-group">
						<div class="input-group-addon">
							<i class="fas fa-user"></i>
						</div>
						<input type="text" class="form-control" id="admin" placeholder="Administrator">
					</div>
				</div>
				<div class="row">
					<div class="col input-group">
						<div class="input-group-addon">
							<i class="fas fa-key" aria-hidden="true">
						</i></div>
						<input type="password" class="form-control" id="password" placeholder="Password">
					</div>
				</div>
				<div id="captchaDiv" class="row" style="display:none;">
					<div class="col">
						<span style="font-weight:bold;">
							<i class="fas fa-lock" aria-hidden="true" style="color:orangered;"></i>
							Security Check
						</span>
						<br/>
						If you are not robot, Please enter the text below.
						<br/>
						<img id="captchaImage" src="${pageContext.request.contextPath}/console/login/getCaptchaImage"/>
						&nbsp;
						<audio id="captchaAudio" controls="true" src="${pageContext.request.contextPath}/console/login/getCaptchaAudio" type="audio/wav"></audio>
						<br/>
						<button type="button" class="btn btn-info" onclick="javascript:reloadCaptcha();">
							<i class="fas fa-sync-alt"></i>
						</button>
						<input type="text" class="form-control" id="captcha" placeholder="Anwser is...">
					</div>
				</div>
				<div class="row">
					<div class="col">
						<button type="button" class="btn btn-primary" style="width:100%;" onclick="javascript:doLogin();">
							Login
						</button>
					</div>
				</div>
				<div class="row">
					<div class="col text-center">
						<div id="messageDiv"></button>
					</div>
				</div>
			</div>
		</main>
		<!-- end of body -->
	</body>
</html>
