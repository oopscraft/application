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
		<script src="/appboot/lib/jquery.js"></script>
		<script src="/appboot/lib/popper.js"></script>
		
		<!-- bootstrap -->
		<link rel="stylesheet" href="/appboot/lib/bootstrap/css/bootstrap.min.css">
		<script src="/appboot/lib/bootstrap/js/bootstrap.min.js"></script>
		
		<!-- font-awesome -->
		<link rel="stylesheet" href="/appboot/lib/font-awesome/css/font-awesome.css">
		
		<!--  core -->
		<link rel="stylesheet" href="/appboot/appboot.css">
		<script src="/appboot/appboot.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
		</script>
		<style type="text/css">
		.vertical-center {
			min-height: 80%;  /* Fallback for browsers do NOT support vh unit */
			min-height: 80vh; /* These two lines are counted as one :-)       */
			display: flex;
			align-items: center;
		}
		input#username {
			height: 34px !important;
		}
		input#password {
			height: 34px !important;
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
                        <form id="loginform" class="form-horizontal" role="form" style="width:300px;">
                            <div class="input-group text-center">
								<img src="/appboot/img/logo_core.png"/>
							</div>
							<div class="input-group mb-2 mb-sm-0" style="margin:1px;">
								<div class="input-group-addon"><i class="material-icons">person</i></div>
								<input type="text" class="form-control" id="username" placeholder="Username">
							</div>
							<div class="input-group mb-2 mb-sm-0" style="margin:1px;">
								<div class="input-group-addon"><i class="material-icons">lock</i></div>
								<input type="password" class="form-control" id="password" placeholder="Password">
							</div>
							<div class="input-group mb-2 mb-sm-0" style="margin:1px;">
								<button type="button" class="btn btn-primary btn-lg" style="width:100%;">Login</button>
							</div>
                           	<div id="message"></div>
						</form>    
						<!-- Ends Login Panel -->
					</div>
					<div class="col"></div>
				</div>

			</div>
		</main>
		<!-- end of body -->
	</body>
</html>
