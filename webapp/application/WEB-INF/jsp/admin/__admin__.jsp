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
		<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval+10}">

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
	
		<!-- chartjs -->
		<script src="${pageContext.request.contextPath}/lib/Chart.js/Chart.js"></script>
		
		<!-- global -->
		<script type="text/javascript">
        $(document).ajaxStart(function () {
			juice.progress.start();
        });

        $(document).ajaxStop(function () {
        	juice.progress.end();
        });
		</script>
		<style type="text/css">
		* {
			font-family: sans-serif, Dotum;
			font-size: 0.9375rem;
		}
		header {
			border-bottom: groove 1px #efefef;
		}
		h1 {
			font-size: 1.5rem;
			padding-top: 1rem;
			border-bottom: groove 2px #efefef;
		}
		h2 {
			font-size: 1.25rem;
			padding: 0.1rem;
		}
		h3 {
			font-size: 1rem;
		}
		</style>
	</head>
	<body>

		<!-- start of header -->
		<header>
		
			<!--  start of navigation -->
			<nav class="navbar navbar-expand-lg navbar-light bg-light">
				<a class="navbar-brand" href="${pageContext.request.contextPath}/console">
					<img src="${pageContext.request.contextPath}/img/logo_application.png"/>
			      	&nbsp; &nbsp;
				</a>
				<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<div class="collapse navbar-collapse" id="navbarSupportedContent">
					<ul class="navbar-nav mr-auto">
						<li class="nav-item">	
							<a class="nav-link" href="${pageContext.request.contextPath}/console/monitor">
								<i class="fas fa-desktop" aria-hidden="true"></i>
								Monitor
							</a>
						</li>
						<li class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-user" aria-hidden="true"></i>
								User
							</a>
							<div class="dropdown-menu">
								<a class="dropdown-item" href="${pageContext.request.contextPath}/console/user">
									<i class="fas fa-user" aria-hidden="true"></i>
									User
								</a>
								<a class="dropdown-item" href="${pageContext.request.contextPath}/console/group">
									<i class="fas fa-folder" aria-hidden="true"></i>
									Group
								</a>
								<div class="dropdown-divider"></div>
								<a class="dropdown-item" href="#">
									Role
								</a>
								<a class="dropdown-item" href="#">
									Privilege
								</a>
							</div>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${pageContext.request.contextPath}/console/menu">
								<i class="fas fa-bars" aria-hidden="true"></i>
								Menu
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${pageContext.request.contextPath}/console/code">
								<i class="fas fa-code" aria-hidden="true"></i>
								Code
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${pageContext.request.contextPath}/console/message">
								<i class="fas fa-comment" aria-hidden="true"></i>
								Message
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${pageContext.request.contextPath}/console/board">
								<i class="fas fa-sticky-note-o" aria-hidden="true"></i>
								Board
							</a>
						</li>
					</ul>
					<ul class="nav navbar-nav navbar-right">
	      				<li class="nav-item">
	      					<a class="nav-link" href="${pageContext.request.contextPath}/console/logout">
								<i class="fas fa-power-off" aria-hidden="true"></i>
	      						Logout 
	      					</a>
	      				</li>
					</ul>
				</div>
			</nav>
			<!--  ends of navigation -->
			
		</header>
		<!-- end and header -->

		<!-- start of body -->
		<main>
			<div class="container-fluid">
				<tiles:insertAttribute name="main"/>
			</div>
		</main>
		<!-- end of body -->
		
		<!-- start of footer -->
		<footer>
			<p></p>
		</footer>
		<!-- end of footer -->		
	</body>
</html>
