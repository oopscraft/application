<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<script type="text/javascript">
/**
 * user data
 */
var user = new duice.data.Map({
	id: null,
	password: null,
	passwordConfirm: null,
	email: null,
	name: null,
	nickname: null
});

/**
 * Do Join
 * @function
 */
function doJoin() {

	// checks id
	if(duice.util.StringUtils.isEmpty(user.get('id')) == true){
		new duice.ui.Alert('empty id').open();
		return false;
	}
	if(duice.util.StringUtils.isGenericId(user.get('id')) == false){
		new duice.ui.Alert('invalid id').open();
		return false;
	}
	
	// check password
	if(duice.util.StringUtils.isEmpty(user.get('password')) == true){
		new duice.ui.Alert('empty password').open();
		return false;
	}
	if(user.get('password') !== user.get('passwordConfirm')){
		new duice.ui.Alert('password and password confirm is not corrent').open();
		return false;
	}
	if(duice.util.StringUtils.isGenericPassword(user.get('password')) == false){
		new duice.ui.Alert('invalid password').open();
		return false;
	}
	
	
	// calls service
	alert(JSON.stringify(user.toJson()));
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/user'
		,type: 'POST'
		,data: JSON.stringify(user.toJson())
		,contentType: "application/json"
		,success: function(data, textStatus, jqXHR) {
			$(window).off('beforeunload');
			history.back();
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

<jsp:include page="/WEB-INF/theme/${__configuration.theme}/user/join.jsp" flush="true"/>
