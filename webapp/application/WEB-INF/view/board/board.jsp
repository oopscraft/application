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
		 url: '${pageContext.request.contextPath}/api/board/${id}/articles'
		,type: 'GET'
		,data: articleSearch.toJson()
		,success: function(data, textStatus, jqXHR) {
			articles.fromJson(data);
			articleSearch.set('totalCount', __parseTotalCount(jqXHR));
			$('#articleTable').hide().fadeIn();
   	 	}
	});	
}
</script>
<style type="text/css">
</style>

<table id="articleTable" data-juice="Grid" data-juice-bind="articles" data-juice-item="article">
	<colgroup>
		<col style="width:10%"/>
		<col/>
		<col style="width:10%"/>
	</colgroup>
	<thead>
		<tr>
			<th>
				fdsa
			</th>
			<th>
				fdsa
			</th>
			<th>
				22
			</th>
		</tr>
	</thead>
	<tbody>
		<tr data-no="{{$context.article.get('no')}}" onclick="javascript:getArticle(this.dataset.no);">
			<td class="text-center">
				<label data-juice="Label" data-juice-bind="article.no"></label>
			</td>
			<td>
				<label data-juice="Label" data-juice-bind="article.title"></label>
			</td>
			<td>
			</td>
		</tr>
	</tbody>
</table>
<div>
	<ul data-juice="Pagination" data-juice-bind="articleSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
		<li data-page="{{$context.page}}" onclick="javascript:getMessages(this.dataset.page);">{{$context.page}}</li>
	</ul>
</div>
