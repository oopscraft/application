<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<script type="text/javascript">
var user = new duice.data.Map({
	id: null,
	password: null,
	'_spring_security_remember_me': false
});

/**
 * On document loaded
 */
$( document ).ready(function() {
	$('#idInput').val('').focus();
	
	// enter key login
	$(document).on('keyup', function(e) {
		if(e.which == 13) {
			console.log('13');
			doLogin();
		}
	});
});

/**
 * Login 
 */
function doLogin() {

	if(duice.util.StringUtils.isEmpty(user.get('id'))){
		<spring:message code="application.text.id" var="item"/>
		var message = '<spring:message code="application.message.enterItem" arguments="${item}"/>';
		printMessage(message);
		$('#idInput').focus();
		return false;
	}
	
	if(duice.util.StringUtils.isEmpty(user.get('password'))){
		<spring:message code="application.text.password" var="item"/>
		var message = '<spring:message code="application.message.enterItem" arguments="${item}"/>';
		printMessage(message);
		$('#passwordInput').focus();
		return false;
	}
	
	$.ajax({
		 url: '/user/login/processing'
		,type: 'POST'
		,data: user.toJson()
		,success: function(data, textStatus, jqXHR) {
			location.href='${pageContext.request.contextPath}/';
    	 }
	 	,error: function(jqXHR, textStatus, errorThrown) {
	 		var message = jqXHR.responseText;
	 		printMessage(message);
		 }
	});
}

/**
 * Prints message
 */
function printMessage(message) {
	var messageDiv = $('#messageDiv');
	messageDiv.hide().html(message).fadeIn();
}
</script>

<jsp:include page="/WEB-INF/theme/${__configuration.theme}/user/login.jsp" flush="true"/>
