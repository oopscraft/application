<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="app" uri="/WEB-INF/tld/application.tld"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<link href="${pageContext.request.contextPath}/resource/board/skin/${board.skinId}/style.css" rel="stylesheet" type="text/css" />
<!-- global -->
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
	 page: 1
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
	getArticles();
});

/**
 * Gets message list
 */
function getArticles(page) {
	if(page){
		articleSearch.set('page',page);
	}
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/articles'
		,type: 'GET'
		,data: articleSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			articles.fromJson(data);
			articleSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#articlesTable').hide().fadeIn();
   	 	}
	});	
}

/**
 * Gets article
 */
function getArticle(articleNo) {
	location.href = '${pageContext.request.contextPath}/board/${boardId}/view?articleNo=' + articleNo;
}

/**
 * Writes article
 */
function writeArticle() {
	location.href = '${pageContext.request.contextPath}/board/${boardId}/write';
}
</script>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/view/board/skin/${board.skinId}/list.jsp" flush="true"/>
