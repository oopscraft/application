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
		<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval+10}">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/lib/jquery.js"></script>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		
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
			margin: 0px;
			padding: 0px;
			font-family: Tahoma, Dotum;
			font-size: 11px;
		}
		body {
			display: grid;
			grid-template-columns: 200px auto;
			grid-template-rows: 50px auto 50px;
			grid-template-areas: "header header" "nav section" "footer footer";
		}
		body > header {
			grid-area: header;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border: solid 1px;
		}
		body > nav {
			grid-area: nav;
			border: solid 1px;
		}
		body > section {
			grid-area: section;
			border: solid 1px;
			padding: 10px;
		}
		body > footer {
			grid-area: footer;
			border: solid 1px;
		}
		span.title1 {
			font-size: 16px;
			font-weight: bold;
		}
		span.title1 > i.material-icons {
			font-size: 24px;
			vertical-align: text-bottom;
		}
		span.title2 {
			font-size: 12px;
			font-weight: bold;
		}
		span.title2 > i.material-icons {
			font-size: 24px;
			vertical-align: text-bottom;
		}
		table.detail {
			width: 100%;
			border-collapse: collapse;
			border: solid 1px #cccccc;
		}
		table.detail > tbody > tr > th {
			border: solid 1px #efefef;
			background-color: #fafafa;
			padding: 0.4rem 0.2rem;
			text-align: center;
		}
		table.detail > tbody > tr > td {
			border: solid 1px #efefef;
			padding: 0.4rem 0.2rem;
		}
		</style>
	</head>
	<body>
		<header>
			<div>
				<a href="dash">
					<img src="${pageContext.request.contextPath}/img/application.png"/>
				</a>
			</div>
			<div style="padding-right:10px;">
				<a href="${pageContext.request.contextPath}/admin/logout">
					<i class="material-icons">logout</i>
				</a>
			</div>
		</header>
		<nav>
			NAV
			<ul>
				<li><a href="user">User Management</a></li>
				<li><a href="group">Group Management</a></li>
				<li><a href="authority">Authority Management</a></li>
				<li><a href="menu">메뉴 관리</a></li>
				<li><a href="code">코드 관리</a></li>
				<li><a href="message">메시지 관리</a></li>
				<li><a href="board">게시판 관리</a></li>
				<li><a href="content">컨텐츠 관리</a></li>
			</ul>
		</nav>
		<section>
			<tiles:insertAttribute name="main"/>
		</section>
		<footer>
			FOOTER
		</footer>
	</body>
</html>
