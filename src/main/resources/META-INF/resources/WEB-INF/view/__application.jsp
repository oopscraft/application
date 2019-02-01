<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<c:set var="THEME_URI" value="${pageContext.request.contextPath}/resources/theme/${__configuration.theme}" scope="request"/>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=1024, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval+10}">
		<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/static/img/application.ico">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/lib/juice/juice.css">
		<script src="${pageContext.request.contextPath}/static/lib/juice/juice.js"></script>
		<script src="${pageContext.request.contextPath}/static/lib/jquery.js"></script>
 		<script src="${pageContext.request.contextPath}/static/lib/moment-with-locales.min.js"></script>
 		<script src="${pageContext.request.contextPath}/static/lib/Chart.js/Chart.js"></script>
 		<link href="${pageContext.request.contextPath}/static/icon/css/icon.css" rel="stylesheet">
 		
 		<!-- polyfill -->
		<script src="${pageContext.request.contextPath}/static/lib/polyfill/dataset.js"></script>
		<script src="${pageContext.request.contextPath}/static/lib/polyfill/classList.js"></script>

 		<!-- web font -->
 		<link href="${pageContext.request.contextPath}/static/font/code.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-kr.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-ja.css" rel="stylesheet" type="text/css" />
 		<link href="${pageContext.request.contextPath}/static/font/font-zh.css" rel="stylesheet" type="text/css" />
 		
		<!-- global -->
		<script type="text/javascript">
		$( document ).ready(function() {
			__getMenus();
			__getLanguages();
		});
		
		var __loader; 
        $(document).ajaxStart(function(event) {
       		__loader = new juice.ui.__().load(document.body);
        });

        // If not configure, "Provisional headers are shown" error occured in chrome.
        $(document).ajaxSend(function(event, jqxhr, settings) {
        	jqxhr.setRequestHeader('Cache-Control','no-cache, no-store, must-revalidate');
        	jqxhr.setRequestHeader('Pragma','no-cache');
        	jqxhr.setRequestHeader('Expires','0');
        });

		// checks stop event
        $(document).ajaxStop(function(event) {
        	if(__loader){
       			__loader.release();
        	}
        });
        
		// Checks error except cancelation(readyState = 0)
        $(document).ajaxError(function(event, jqXHR, ajaxSettings,thrownError ){
        	console.log(jqXHR);
        	if(jqXHR.readyState > 0){
	        	console.log(event);
	        	console.log(jqXHR);
	        	console.log(ajaxSettings);
	        	console.log(thrownError);
	        	alert(jqXHR.responseText);
        	}
        });

		/**
		 * login user information
		 */
		var __user = new juice.data.Map(${app:toJson(__user)});
		__user.set('language', '${pageContext.response.locale}');
		__user.afterChange(function(event){
			if(event.name == 'language'){
				window.location = '?language=' + event.value;
			}
		});	
		
		/**
		 * Gets languages
		 */
		var __languages = new Array();
		function __getLanguages() {
			$.ajax({
				 url: '${pageContext.request.contextPath}/api/locale/languages'
				,type: 'GET'
				,data: {}
				,success: function(data, textStatus, jqXHR) {
					console.log(data);
					data.forEach(function(item){
						__languages.push({
							value: item.language,
							text: item.displayName
						});
					});
					__user.notifyObservers();
				}
			});
		}
		
		/**
		 * Gets menus
		 */
		var __menus = new juice.data.Tree();
		function __getMenus() {
			$.ajax({
				 url: '${pageContext.request.contextPath}/api/menus'
				,type: 'GET'
				,data: {}
				,success: function(data, textStatus, jqXHR) {
					__menus.fromJson(data,'childMenus');
		   	 	}
			});	
		}
		
        /**
         * Parsed total count from Content-Range header
         * @Param {Object} jqXHR
         */
        function __parseTotalCount(jqXHR){
        	var contentRange = jqXHR.getResponseHeader("Content-Range");
        	try {
            	var totalCount = contentRange.split(' ')[1].split('/')[1];
        		return parseInt(totalCount);
        	}catch(e){
        		return -1;
        	}
        }
         
        /**
         * Validates ID value
         * @Param {String} id value
         */
        var __validator = {
        	// Checks ID	 
         	checkId: function(id) {
         		// Checks empty
         		if(juice.util.validator.isEmpty(id)){
             		<spring:message code="application.text.id" var="item"/>
       				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
				// Validates generic
				if(juice.util.validator.isGenericId(id) == false){
					throw '<spring:message code="application.message.invalidIdFormat"/>';
				}
				// length
				if(juice.util.validator.isLengthBetween(id,4,32) == false){
					<spring:message code="application.text.id" var="item"/>
					throw '<spring:message code="application.message.itemMustLengthBetween" arguments="${item},4,32"/>';
				}
         	},
         	checkPassword: function(password, passwordConfirm){
         		if(juice.util.validator.isEmpty(password)){
             		<spring:message code="application.text.password" var="item"/>
       				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
        		if(password != passwordConfirm){
        			<spring:message code="application.text.password" var="item"/>
        			throw '<spring:message code="application.message.itemNotMatch" arguments="${item}"/>';
        		}
        		if(juice.util.validator.isGenericPassword(password) == false){
        			throw '<spring:message code="application.message.invalidPassowrdFormat"/>';
        		}
         	},
         	// Checks name
         	checkName: function(name){
         		// Checks empty
         		if(juice.util.validator.isEmpty(name)){
             		<spring:message code="application.text.name" var="item"/>
       				throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
         		// check length
         		if(juice.util.validator.isLengthBetween(name,1,256) == false){
					<spring:message code="application.text.name" var="item"/>
					throw '<spring:message code="application.message.itemMustLengthBetween" arguments="${item},4,32"/>';
         		}
         	},
         	// Checks email address
         	checkEmailAddress: function(value){
         		if(juice.util.validator.isEmailAddress(value) == false){
         			throw '<spring:message code="application.message.invalidEmailAddressFormat"/>';
         		}
         	},
         	// Checks locale 
         	checkLocale: function(value){
         		if(juice.util.validator.isEmpty(value)){
					<spring:message code="application.text.locale" var="item"/>
					throw '<spring:message code="application.message.enterItem" arguments="${item}"/>';
         		}
         	},
         	// Checks phone number
         	checkPhoneNumber: function(value){
         		if(juice.util.validator.isPhoneNumber(value) == false){
         			throw '<spring:message code="application.message.invalidPhoneNumberFormat"/>';
         		}
         	}
        }
		</script>
	</head>
	<body>
		<!-- ====================================================== -->
		<!-- Header													-->
		<!-- ====================================================== -->
		<jsp:include page="/WEB-INF/theme/${__configuration.theme}/header.jsp" flush="false"/>
		
		<!-- ====================================================== -->
		<!-- Main													-->
		<!-- ====================================================== -->
		<tiles:insertAttribute name="main"/>
		
		<!-- ====================================================== -->
		<!-- Footer													-->
		<!-- ====================================================== -->
		<jsp:include page="/WEB-INF/theme/${__configuration.theme}/footer.jsp" flush="false"/>
	</body>
</html>
