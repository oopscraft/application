<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<div id="loginDiv">
	<div>
		<img src="${pageContext.request.contextPath}/static/img/application.png"/>
	</div>
	<div>
		<input id="idInput" data-juice="TextField" data-juice-bind="user.id" placeholder="<spring:message code="application.text.id"/>"/>
	</div>
	<div>
		<input id="passwordInput" type="password" data-juice="TextField" data-juice-bind="user.password" placeholder="<spring:message code="application.text.password"/>"/>
	</div>
	<div>
		<button id="loginButton" onclick="javascript:doLogin();" style="border:#fff;">
			<spring:message code="application.label.login"/>
		</button>
	</div>
	<div id="messageDiv">
	</div>
</div>