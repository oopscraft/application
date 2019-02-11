<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href="${SKIN_URI}/style.css" rel="stylesheet" type="text/css" />
<div class="app-board">
	<div style="display:flex; justify-content:space-between; align-items: baseline;"">
		<div class="app-board-name">
			<img data-duice="Image" data-duice-bind="board.icon" data-duice-readonly="true" src="${SKIN_URI}/img/icon_board.png" style="vertical-align:middle; width:1.25em; height:1.25em;"/>&nbsp;
			<c:out value="${board.name}"/>
		</div>
		<c:if test="${board.categoryUseYn == 'Y'}">
			<div style="text-align:right;">
				<img class="icon" src="${SKIN_URI}/img/icon_category.png"/>&nbsp;
				<spring:message code="application.text.category"/>&nbsp;
				<select data-duice="ComboBox" data-duice-bind="articleSearch.categoryId" data-duice-options="categoryOptions" style="width:15em;"></select>
			</div>
		</c:if>
	</div>
	<div>
		<table id="articlesTable" data-duice="Grid" data-duice-bind="articles" data-duice-item="article">
			<colgroup>
				<col style="width:10rem;"/>
				<col/>
				<col style="width:15rem;"/>
				<col style="width:15rem;"/>
				<col style="width:10rem;"/>
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
						<spring:message code="application.text.date"/>
					</th>
					<th>
						<spring:message code="application.text.writer"/>
					</th>
					<th>
						<spring:message code="application.text.read"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr data-id="[[$context.article.get('id')]]" onclick="javascript:readArticle(this.dataset.id);">
					<td>
						[[articleSearch.get('totalCount') - (articleSearch.get('rows')*(articleSearch.get('page')-1)) - $context.index]]
						<span data-duice="Text" data-duice-bind="article.no"></span>
					</td>
					<td style="text-align:left;">
						<span data-duice="Text" data-duice-bind="article.title"></span>
						<c:if test="${board.replyUseYn == 'Y'}">
							<span style="display:[[$context.article.get('replyCount') > 0 ? 'inline-block' : 'none']]">
								(<span data-duice="Text" data-duice-bind="article.replyCount"></span>)
							</span>
						</c:if>
						<c:if test="${board.fileUseYn == 'Y'}">
							<span style="display:[[$context.article.get('fileCount') > 0 ? 'inline-block' : 'none']]">
								<img class="icon" src="${SKIN_URI}/img/icon_file.png"/>
							</span>
						</c:if>
						<c:if test="${board.categoryUseYn == 'Y'}">
							<span data-duice="Text" data-duice-bind="article.categoryName" style="color:#aaa; font-style:italic;"></span>
						</c:if>
					</td>
					<td>
						<span data-duice="Text" data-duice-bind="article.registDate" data-duice-format="date:yyyy-MM-dd hh:mm:ss"></span>
					</td>
					<td>
						<img data-duice="Image" data-duice-bind="article.userAvatar" src="data:image/gif;base64,R0lGODlhZABkANUAAMPM1OHm6drg5NDX3f7+/ubq7fDy9Pr7/MXO1dzh5vHz9eTo7MPN1Pb3+Ont79HY3sfQ1+Xp7fT299vh5eHl6eXp7O7x8/X3+NTb4M/X3fb4+dje4+Dl6MHK0tbc4e3w8sjR187V3Pj5+vP199fd4v39/snR2N3i5uPn6/X2+Pv7/Pz8/cXO1tPa4O/x9O7w8/z8/Nnf4/r7+7/J0f///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAABkAGQAAAb/wJlwSCwaj8ikcslsOp/QqHRKrVqv2Kx2y+16v+CweEwum8/otPoMGEwilsuBQDhcLJHJALA2AzAVEjSDhIWFEhUYfH1fEAkGhpGSgwYJEIxbAAIKk52SCgKLmFUDDp6nkg4Do1QCDaiwhg0CrFAMAQSxuoQEAQy1TAgLu8SECwjASQzDxc0Lv8lGAc3UNAHRRQK51cUEtNgzA6/czQ2r0QCm5NQOorUC69zftRCc8dQKl7UJ99wJtQAg9aNmwF0fDAO5YWBVIWG1CqMACHLYTILBNAMoVjvXZ4JGahMwRfjYLAImCySLWcB0ISWxC5gOuNx1ANO2mbAI2MQZSycj/5k8UdVk1DLoKZiMUBr1tJLRyKWdTDLyCHVSSEYZq0riuEaiVkMWRzX8SgjiKIRkBy2MKPBrwX1p/9GzVzVfMnha5wFUt7QdNnFLzYGboS2ot8FCpvG8hnjGMpzPGgsR5vKY5CG3bibsBe3yEFcOZ3k+UmqgqtFINNGtBuoiaiKO2hKrpO81EhYtAriAUQyGiwAtWNgugsBDAQ33NBTwgOx1iAAjNI4IEMLzgwJASR4o8ABxhgIrgq4okCGaCQoiqoqgYKIWCdlVDZDABAJFibSDSqAAsebBB/yGfNAdGhukAGAkKWxwxgkqHCiJCieQ0QEHDnrCQQdhdEBBhadQgL/hFxRyeAoHX5wgIiwRcrFBgyeeooKCWjxgYIuopDDgFSD8RyMsH/B3BQo76oLCFSTcFyQsJcxHhQnwHemJAe1NsaGTulAwRQbpURmLCOVFUYCWuxQQxQPhgRnLCjc28aWZuojpRAjZsSlUdU0oJmcsjC2BQHR3xjJCc0p40OcuHjCx5qCwuJkEC8ghCosGwiXRgqO6tKCEnZSekucRL2QKywtJICCDp6jIAGgRGZAKS5dGxKAqKjEggemrkmwaBAA7" style="width:1rem; height:1rem; vertical-align:middle;"/>
						<span data-duice="Text" data-duice-bind="article.userNickname"></span>
					</td>
					<td>
						<span data-duice="Text" data-duice-bind="article.readCount" data-duice-format="number:0"></span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div style="display:flex; justify-content:space-between;">
		<div style="width:33%;">
			<select data-duice="ComboBox" data-duice-bind="articleSearch.searchType" data-duice-options="articleSearchTypes" style="width:10rem;"></select>
			<input data-duice="TextField" data-duice-bind="articleSearch.searchValue" style="width:15rem;"/>
			<button class="app-board-button" onclick="javascript:getArticles();">
				<img class="icon" src="${SKIN_URI}/img/icon_search.png"/>
				<spring:message code="application.text.search"/>
			</button>
		</div>
		<div style="width:33%;">
			<ul data-duice="Pagination" data-duice-bind="articleSearch" data-duice-rows="rows" data-duice-page="page" data-duice-total-count="totalCount" data-duice-page-size="5">
				<li data-page="[[$context.page]]" onclick="javascript:getArticles(this.dataset.page);">[[$context.page]]</li>
			</ul>
		</div>
		<div style="width:33%; text-align:right;">
			<button class="app-board-button" onclick="javascript:writeArticle();">
				<img class="icon" src="${SKIN_URI}/img/icon_write.png"/>
				<spring:message code="application.text.article"/>
				<spring:message code="application.text.write"/>
			</button>
		</div>
	</div>
</div>