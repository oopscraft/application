<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style type="text/css">
* {
	margin: 0px;
	padding: 0px;
	font-size: inherit;
	font-family: inherit;
	line-height: inherit;
}
a, a:hover, a:active {
	color: inherit;
	text-decoration: none; 
	outline: none
}
img {
	border: none;
	vertical-align: middle;
}
th {
	font-weight:normal;
}
html {
	font-size: 0.75rem;
	line-height: 2;
	font-family: font, font-kr, font-ja, font-zh, sans-serif;
	color: #555;
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
.topNav {
	display: inline-block;
}
body > main {
	display: flex;
	align-items: center;
	justify-content: space-between;
	min-height: 90vh;
	border: none;
}
body > main > section {
    align-self: stretch;
    width: 100%;
    border: none;
    padding: 10px;
}
.leftNav {
	display: block;
	align-self: stretch;
    margin: 0.1rem;
    padding: 1rem;
	width: 15%;
	min-width: 150px;
	border: none;
    border-right: solid 1px #eee;
}
.leftNav .menuItem {
	padding: 0.25rem 0rem;
	border-bottom:dotted 1px #ccc;
	cursor:hand;
	cursor:pointer;
}
.leftNav .menuItem:hover {
	background-color: #eee;
	transition: all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms;
}
body > main > section {
	align-self: stretch;
	width: 100%;
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
			<select data-juice="ComboBox" data-juice-bind="__user.language" data-juice-options="__languages" style="width:10rem;"></select>
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
	<!-- ====================================================== -->
	<!-- Navigation												-->
	<!-- ====================================================== -->
	<nav class="leftNav">
		<div style="border-bottom:dotted 1px #ccc;">
			<i class="icon-menu"></i>
		</div>
		<ul data-juice="TreeView" data-juice-bind="__menus" data-juice-item="menu">
			<li>
				<a href="{{$context.menu.get('link')}}" class="menuItem" style="display:block;">
					<img class="icon" data-juice="Thumbnail" data-juice-bind="menu.icon" data-juice-width="24" data-juice-height="24" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAACKklEQVR42rWU709SYRTH/Sd70XqfW9JqZo10NTOSUMDN2Rq9qSxC+ol6hxCByS+Byw38RWiI2nrTCrhALRHz2z0nuK68CLV5tu+e58W5n+d7nnOe29V1UmEYug77oweYdr3As6cOVc6pJ4rsjb0dA1cvoyPgPdsddBKOxw9x5vSp9tDxMTPKcqkt0OOeRdDnxvme7uOhE+NWVKuVtsAppWyK6pfP0PWcxfEOy3Jb4JhlBLdu3oDt7gQG9H3QnetGLrcBTWClUsa/Rji0gL5LF44CrRaTChSEaQQCPtZrrxvC7CvMuJ7D9dIJp2OSNXnfxrnfv1UxNHhNGyh30JS/Y2c7j36tUSJg8w5rtV0cHPxUtb9fR72+h9ruDxa5kkuFBnAL/XoNoHnEqJYcCs5DkkRFCYiJGOKxCCsceovQgh9+3xzcgotzP33c0XZoum1QgQRaz2aQyaxibXUZS2mJlXonIplYREQBz/u9hyXrWwDlUrEBFHkUCErA5aUU0qkkAxPxKOKL4T+A+iu9R4HDhkH1pVCZTXcrK2l2JyXjLDERZYdUNgO3NlsDi8XfFx2NBBlEarqTxBiL3BEw8MbDudv5XGtgofCVk7wegaEk+jjCzQhwmeTMOzfDM9l0qDnYNJxNYEYplU7Ob35g5TayrPX3a6ysch0ppXEUtNcE0tj8T1TkonaXey/qYDWb+NFbRo0sGnZezYfrqGkYdLjJaAC9f1rpB3Fif/5fRO6q8pQoJI0AAAAASUVORK5CYII=" style="vertical-align:middle;"/>
					<label data-juice="Label" data-juice-bind="menu.name"></label>
				</a>
			</li>
		</ul>
	</nav>
	<!-- ====================================================== -->
	<!-- Section												-->
	<!-- ====================================================== -->
	<section>