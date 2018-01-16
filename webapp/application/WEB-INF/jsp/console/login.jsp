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
		<script src="/application/lib/jquery.js"></script>
		<script src="/application/lib/popper.js"></script>
		
		<!-- bootstrap -->
		<link rel="stylesheet" href="/application/lib/bootstrap/css/bootstrap.min.css">
		<script src="/application/lib/bootstrap/js/bootstrap.min.js"></script>
		
		<!-- font-awesome -->
		<link rel="stylesheet" href="/application/lib/font-awesome/css/font-awesome.css">
		
		<!--  core -->
		<link rel="stylesheet" href="/application/application.css">
		<script src="/application/application.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
		/**
		 * Checking keypress
		 */
		$(document).ready(function(){
			$('#admin').focus();
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
			var formData = new FormData(loginForm);
			formData.append('admin', adminInput.val());
			formData.append('password', passwordInput.val());
			$.ajax({
				 url: ''
				,processData: false
				,contentType: false
				,data: formData
				,type: 'POST'
				,success: function(response) {
					location.href='${pageContext.request.contextPath}' + '/console';
		    	 }
				,error: function(response) {
					$('#message').text(response.responseText);
				}
			});
			
		}
		</script>
		<style type="text/css">
		.vertical-center {
			min-height: 80%;  /* Fallback for browsers do NOT support vh unit */
			min-height: 80vh; /* These two lines are counted as one :-)       */
			display: flex;
			align-items: center;
		}
		input#admin {
			height: 34px !important;
		}
		input#password {
			height: 34px !important;
		}
		div#message {
			height: 34px;
			line-height: 34px;
			text-align: center;
			font-weight: bold;
			color: orangered;
		}
		
		
#loginDiv {
	display: table;
	width: 100%;
	padding: 30px;
	border: groove 2px #efefef;
	border-radius: 2px;
	background: -webkit-linear-gradient(#efefef, white);
	background: -o-linear-gradient(#efefef, white);
	background: -moz-linear-gradient(#efefef, white);
	background: linear-gradient(#efefef, white);
}
#loginForm {
   display: table-cell;
   text-align: center;
   vertical-align: middle;
}
		</style>
	</head>
	<body>
		<!-- start of body -->
		<main>
			<div class="container-fluid">
				<div class="row">
					<div class="col"></div>
  					<div class="col vertical-center">
						<!-- Starts Login Panel -->
						<div id="loginDiv">
                        <form id="loginForm" class="form-horizontal" role="form" style="width:300px;">
                            <div class="input-group text-center">
								<img src="/application/img/logo_application.png"/>
							</div>
							<div class="input-group mb-2 mb-sm-0" style="margin:1px;">
								<div class="input-group-addon"><i class="fa fa-user" aria-hidden="true"></i></div>
								<input type="text" class="form-control" id="admin" placeholder="Administrator">
							</div>
							<div class="input-group mb-2 mb-sm-0" style="margin:1px;">
								<div class="input-group-addon"><i class="fa fa-key" aria-hidden="true"></i></div>
								<input type="password" class="form-control" id="password" placeholder="Password">
							</div>
							<div class="input-group mb-2 mb-sm-0" style="margin:1px;">
								<button type="button" class="btn btn-primary btn-lg" style="width:100%;" onclick="javascript:doLogin();">
									Login
								</button>
							</div>
                           	<div id="message"> </div>
						</form> 
						</div>
						<!-- Ends Login Panel -->
					</div>
					<div class="col"></div>
				</div>

			</div>
		</main>
		<!-- end of body -->
	</body>
</html>
