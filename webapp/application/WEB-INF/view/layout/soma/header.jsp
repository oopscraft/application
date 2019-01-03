<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<style type="text/css">
* {
	margin:0px;
	padding:0px;
	font-family: font, font-kr, font-ja, font-zh, sans-serif;
	font-size:11px;
	color: #555;
	line-height: 20px;
}
html {
	overflow: -moz-scrollbars-vertical; 
	overflow-y: scroll;
}
body {
	min-width: 1280px;
}
body > header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	height: 50px;
	background-color: #eee;
	border: none;
	border-bottom: solid 1px #ddd;
	padding: 0rem 0.5rem;
}
body > header nav.topNav {
	display: inline-block;
}
body > main {
	min-height: 90vh;
	border: none;
}
</style>
<header>
	<span>
		<a href="monitor">
			<img src="${pageContext.request.contextPath}/static/img/application.png"/>
		</a>
	</span>
	<nav class="topNav" style="padding-right:10px;">
		<span>
			<i class="icon-globe"></i>
			<spring:message code="application.label.language"/>
			<!-- 
			<select data-juice="ComboBox" data-juice-bind="__user.language" data-juice-options="__languages" style="width:10rem;"></select>
			-->
		</span>
		&nbsp;
		<span>
			<i class="icon-logout"></i>
			<a href="${pageContext.request.contextPath}/admin/logout">
				<spring:message code="application.label.logout"/>
			</a>
		</span>
	</nav>
</header>
<main>