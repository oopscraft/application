<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<table id="articleTable" data-juice="Grid" data-juice-bind="articles" data-juice-item="article">
	<colgroup>
		<col style="width:10%"/>
		<col/>
		<col style="width:10%"/>
	</colgroup>
	<thead>
		<tr>
			<th>
				<spring:message code="application.text.no"/>
			</th>
			<th>
				<spring:message code="application.text.title"/>
			</th>
			<th>
				<spring:message code="application.text.writer"/>
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
<div style="display: flex; justify-content: space-between;">
	<div>
	</div>
	<div>
		<ul data-juice="Pagination" data-juice-bind="articleSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
			<li data-page="{{$context.page}}" onclick="javascript:getMessages(this.dataset.page);">{{$context.page}}</li>
		</ul>
	</div>
	<div>
		<button onclick="javascript:postArticle();">등록</button>
	</div>
</div>