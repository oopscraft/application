<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"  isErrorPage="true" %>
<%@page isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
//response.setStatus(HttpServletResponse.SC_OK);
%>
<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript">
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
			background-color: #fff;
		}
		#messageDiv {
			width: 30vw;
			margin-bottom: 20vh;
			text-align: center;

		}
		.statusCode {
			height: 3rem;
			font-size: 3rem;
			font-family: Arial Black;
			font-weight: bold;
		}
		.message {
			font-size: 1rem;
			font-family: Courier New, Consolas;
			font-weight: bold;	
		}
		</style>
	</head>
	<body>
		<div id="messageDiv">
			<div class="statusCode">
				<c:out value="${requestScope['javax.servlet.error.status_code']}"/>
			</div>
 			<div class="message">
 				<c:out value="${requestScope['javax.servlet.error.message']}"/>
 			</div>
		</div>
	</body>
</html>
