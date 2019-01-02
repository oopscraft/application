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
		<meta name="viewport" content="width=1024, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval+10}">
		<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/assets/img/application.ico">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/assets/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/assets/lib/jquery.js"></script>
 		<script src="${pageContext.request.contextPath}/assets/lib/moment-with-locales.min.js"></script>
 		<script src="${pageContext.request.contextPath}/assets/lib/Chart.js/Chart.js"></script>
 		<link href="${pageContext.request.contextPath}/assets/icon/css/icon.css" rel="stylesheet">
 		
 		<!-- polyfill -->
		<script src="${pageContext.request.contextPath}/assets/lib/polyfill/dataset.js"></script>
		<script src="${pageContext.request.contextPath}/assets/lib/polyfill/classList.js"></script>

 		<!-- web font -->
 		<link href="${pageContext.request.contextPath}/assets/font/code.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/assets/font/font.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/assets/font/font-kr.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/assets/font/font-ja.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/assets/font/font-zh.css" rel="stylesheet" type="text/css" />
 		
 		<!-- Application -->
		<script src="${pageContext.request.contextPath}/assets/application.js"></script>
		<link href="${pageContext.request.contextPath}/assets/application.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<c:set var="skin" value="application"/>
		<!-- ====================================================== -->
		<!-- Header													-->
		<!-- ====================================================== -->
		<jsp:include page="/WEB-INF/skin/${skin}/header.jsp" flush="true"/>
		
		<!-- ====================================================== -->
		<!-- Main													-->
		<!-- ====================================================== -->
		<tiles:insertAttribute name="main"/>
		
		<!-- ====================================================== -->
		<!-- Footer													-->
		<!-- ====================================================== -->
		<jsp:include page="/WEB-INF/skin/${skin}/footer.jsp" flush="true"/>
	</body>
</html>
