<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<style type="text/css">
#loginContainerDiv {
	width: 100%;
	display: flex;
	height: 80vh;
	justify-content: center;
	align-items: center;
}
#loginDiv {
	width: 300px;
	margin-bottom: 20vh;
}
#loginDiv > div {
	margin:3px;
}
#idInput {
	line-height: 2.5;
	border-bottom: solid 1px #ccc;
	background-color: #fff;
}
#passwordInput {
	line-height: 2.5;
	border-bottom: solid 1px #ccc;
	background-color: #fff;
}
#loginButton {
	line-height: 2.5;
	width: 100%;
	font-weight: bold;
	position:relative;
	border: solid 1px #ccc !important;
	border-radius: 3px;
	background-color: #fff;
	padding:0 1rem;
	cursor:pointer;
	transition:200ms ease all;
	outline:none;
}
#loginButton:hover{
	outline: none;
	background-color: #eee;
	border: solid 1px gray;
}
#messageDiv {
	text-align: center;
	color: red;
}
#loginMenu {
    display: flex;
    justify-content: space-around;
}
#socialDiv {
    display: flex;
    justify-content: space-around;
    padding: 10px 0px;
}
</style>
<div id="loginContainerDiv">
	<div id="loginDiv">
		<div style="text-align:center; font-size:1.2em; font-weight:bold;">
			<spring:message code="application.label.login"/>
		</div>
		<div>
			<input id="idInput" data-duice="TextField" data-duice-bind="user.id" placeholder="<spring:message code="application.text.id"/>"/>
		</div>
		<div>
			<input id="passwordInput" type="password" data-duice="TextField" data-duice-bind="user.password" placeholder="<spring:message code="application.text.password"/>"/>
		</div>
		<div>
			<button id="loginButton" onclick="javascript:doLogin();" style="border:#fff;">
				<spring:message code="application.label.login"/>
			</button>
		</div>
		<div>
			<input type="checkbox" data-jucie="CheckBox" data-duice-bind="user._spring_security_remember_me"/> Remember em.
		</div>
		<div id="messageDiv">
		</div>
		<div id="loginMenu">
			<span>
				<spring:message code="application.label.findId"/>
			</span>
			|
			<span>
				<spring:message code="application.label.findPassword"/>
			</span>
			| 
			<span>
				<spring:message code="application.label.join"/>
			</span>
		</div>
		<hr style="margin:2em 0em; border:0px; border-top:solid 1px #eee;"/>
		<div style="text-align:center; font-size:1.2em; font-weight:bold;">
			<spring:message code="application.label.social"/>
			<spring:message code="application.label.login"/>
		</div>
		<div id="socialDiv">
			<img src="${pageContext.request.contextPath}/static/img/icon_ci_google.png"/>
			<img src="${pageContext.request.contextPath}/static/img/icon_ci_facebook.png"/>
			<img src="${pageContext.request.contextPath}/static/img/icon_ci_github.png"/>
			<img src="${pageContext.request.contextPath}/static/img/icon_ci_kakao.png"/>
			<img src="${pageContext.request.contextPath}/static/img/icon_ci_naver.png"/>
		</div>
	</div>
</div>