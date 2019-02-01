<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<c:set var="SKIN_DIR" value="/WEB-INF/theme/${__configuration.theme}/board/${board.skin}" scope="request"/>
<c:set var="SKIN_URI" value="${pageContext.request.contextPath}/resources/theme/${__configuration.theme}/board/${board.skin}" scope="request"/>
<script type="text/javascript">
var board = new juice.data.Map(${app:toJson(board)});
var categories = new juice.data.List(${app:toJson(board.categories)});
var categoryOptions = [{ value:'', text:'- <spring:message code="application.text.all"/> -' }];
categories.forEach(function(item){
	categoryOptions.push({
		value: item.get('id'),
		text: item.get('name')
	});
});
var articleSearch = new juice.data.Map({
	 rows:${board.rowsPerPage}
	,page: 1
	,categoryId: null
	,searchType: null
	,searchValue: null
	,totalCount:-1
});
articleSearch.afterChange(function(event){
	if(event.name == 'searchType'){
		this.set('searchValue','');
	}
	if(event.name == 'categoryId'){
		getArticles();	
	}
});
var articleSearchTypes = [
	 { value:'', text:'- <spring:message code="application.text.all"/> -' }
	,{ value:'TITLE', text:'<spring:message code="application.text.title"/>' }
	,{ value:'TITLE_CONTENTS', text:'<spring:message code="application.text.title"/> + <spring:message code="application.text.contents"/>' }
	,{ value:'USER', text:'<spring:message code="application.text.writer"/>' }
];
var articles = new juice.data.List();

/**
 * On document loaded
 */
$( document ).ready(function() {
	getArticles(window.location.hash.replace('#',''));
});

/**
 * Gets message list
 */
function getArticles(page) {
	if(page){
		articleSearch.set('page',page);
		window.location.hash = '#' + page;
	}
	
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/articles'
		,type: 'GET'
		,data: articleSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			articleSearch.set('totalCount', __parseTotalCount(jqXHR));
			articles.fromJson(data);
			$('#articlesTable').hide().fadeIn();
   	 	}
	});	
}

/**
 * Gets article
 */
function readArticle(articleId) {
	location.href = '${pageContext.request.contextPath}/board/${boardId}/read?articleId=' + articleId;
}

/**
 * Writes article
 */
function writeArticle() {
	location.href = '${pageContext.request.contextPath}/board/${boardId}/write';
}
</script>
<jsp:include page="${SKIN_DIR}/list.jsp" flush="true"/>
