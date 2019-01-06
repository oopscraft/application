<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style type="text/css">
</style>
<div class="app-board">
	<div style="display:flex; justify-content:space-between;">
		<div class="app-board-name">
			<c:out value="${board.name}"/>
		</div>
	</div>
	<div style="border:solid 1px #ccc; border-radius:1px; padding:1rem;">
		<div style="display:flex; justify-content:space-between; padding-bottom: 0.2rem;">
			<div style="width:70%; font-size:1.3rem; font-weight:bold;">
				<input data-juice="TextField" data-juice-bind="article.title" style="font-size:inherit; font-weight:inherit; border:none; border-bottom:dotted 1px #ccc;"/>
			</div>
			<div style="width:30%; text-align:right;">
				<button class="app-board-button" onclick="javascript:saveArticle();">
					<i class="icon-disk"></i>
					<spring:message code="application.text.save"/>
				</button>
				<button class="app-board-button" onclick="javascript:history.back();">
					<i class="icon-cancel"></i>
					<spring:message code="application.text.cancel"/>
				</button>
			</div>
		</div>
		<div style="height:70vh;">
			<div data-juice="HtmlEditor" data-juice-bind="article.contents"></div>
		</div>
	</div>
</div>

