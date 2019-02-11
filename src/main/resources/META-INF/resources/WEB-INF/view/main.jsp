<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<script type="text/javascript">
/**
 * On document loaded
 */
$( document ).ready(function() {
	getLatestArticles();
	getBestArticles();
});

// latest articles
var latestArticles = new duice.data.List([]);
var latestArticleSearch = new duice.data.Map({
	 page: 1
	,rows: 30
	,totalCount:-1
});

/**
 * gets latest articles
 */
function getLatestArticles(page) {
	if(page){
		latestArticleSearch.set('page', page);
	}
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/articles/latest'
		,type: 'GET'
		,data: latestArticleSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			latestArticles.fromJson(data);
			latestArticleSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#latestArticles').hide().fadeIn();
   	 	}
	});	
}

// best articles
var bestArticles = new duice.data.List([]);
var bestArticleSearch = new duice.data.Map({
	 page: 1
	,rows: 30
	,totalCount:-1
});

/**
 * gets best articles
 */
function getBestArticles(page) {
	if(page){
		bestArticleSearch.set('page',page);
	}
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/articles/best'
		,type: 'GET'
		,data: bestArticleSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			bestArticles.fromJson(data);
			bestArticleSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#bestArticles').hide().fadeIn();
   	 	}
	});	
}



</script>
<jsp:include page="/WEB-INF/theme/${__configuration.theme}/main.jsp" flush="true"/>
