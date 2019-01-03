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
var articleSearch = new juice.data.Map({
	 key: null
	,value: null
	,page: 1
	,rows: 30
	,totalCount:-1
});
articleSearch.afterChange(function(event){
	if(event.name == 'key'){
		this.set('value','');
	}
});
var articleSearchKeys = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'id', text:'<spring:message code="application.text.id"/>' }
	,{ value:'name', text:'<spring:message code="application.text.name"/>' }
];
var articles = new juice.data.List();
var article = new juice.data.Map();

/**
 * On document loaded
 */
$( document ).ready(function() {
	getArticle();
});

/**
 * Gets article
 */
function getArticle() {
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleNo}'
		,type: 'GET'
		,data: articleSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			article.fromJson(data);
  	 	}
	});	
}
</script>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/view/board/view.jsp" flush="true"/>
