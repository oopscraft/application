<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>
		<script type="text/javascript">
		var userMap = new juice.data.Map();
		function doLogin() {
			var id = userMap.get('id');
			$.ajax({
				 url: '/admin/login/processing'
				,type: 'POST'
				,data: userMap.toJson()
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
		</style>
	</head>
	<body>
		<input type="text" data-juice="TextField" data-juice-bind="userMap.id"/>
		<input type="text" data-juice="TextField" data-juice-bind="userMap.password"/>
		<button onclick="javascript:doLogin();">Login</button>
		<div id="messageDiv"></div>
	</body>
</html>
