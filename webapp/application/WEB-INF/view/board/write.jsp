<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style type="text/css">
#articleDiv {
	margin: 1rem;
	padding: 1rem;
	border: solid 1px #ccc;
	border-radius: 1px;
}
#articleDiv #titleDiv {
	padding: 0.2rem;
}
#articleDiv #contentsDiv {
	padding: 0.2rem;
	min-height: 70vh;
}
button#saveButton {
	margin: 0.2rem 0rem;
	border: solid 1px #aaa;
	border-radius: 1px;
	background-color: #eee;
	padding:0 1rem;
	cursor:pointer;
	transition:200ms ease all;
	outline:none;
	cursor: pointer;
	cursor: hand;
	font-weight: bold;
}
button#saveButton:hover {
	border: solid 1px steelblue;
	box-shadow: 0px 0px 1px 1px #ccc;
}
</style>
<div id="articleDiv">
	<div style="display: flex; justify-content:flex-end;">
		<div>
			<button id="saveButton" onclick="javascript:saveArticle();">
				<i class="icon-disk"></i>
				<spring:message code="application.text.save"/>
			</button>
		</div>
	</div>
	<div id="titleDiv">
		<input data-juice="TextField" data-juice-bind="article.title" style="font-weight:bold;"/>
	</div>
	<div id="contentsDiv" style="height:50vh;">
		<div data-juice="HtmlEditor" data-juice-bind="article.contents"></div>
	</div>
</div>

