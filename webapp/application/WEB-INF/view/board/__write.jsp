<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!-- global -->
<script type="text/javascript">
var article = new juice.data.Map();

/**
 * On document loaded
 */
$( document ).ready(function() {
	<c:if test="${param.articleNo != null}">
	getArticle();
	</c:if>
});

/**
 * Gets article
 */
function getArticle() {
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleNo}'
		,type: 'GET'
		,success: function(data, textStatus, jqXHR) {
			article.fromJson(data);
  	 	}
	});	
}

/**
 * Saves article
 */
function saveArticle() {
	<spring:message code="application.text.article" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
		.afterConfirm(function() {
			$.ajax({
				 url: '${pageContext.request.contextPath}/api/board/${boardId}/article'
				,type: 'POST'
				,data: JSON.stringify(article.toJson())
				,contentType: "application/json"
				,success: function(data, textStatus, jqXHR) {
					<spring:message code="application.text.article" var="item"/>
					new juice.ui.Alert('<spring:message code="application.message.saveItem.complete" arguments="${item}"/>')
						.afterConfirm(function(){
							history.back();
						}).open();
		 	 	}
			});
		}).open();
}
</script>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/view/board/write.jsp" flush="true"/>
