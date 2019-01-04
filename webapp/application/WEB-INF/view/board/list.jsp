<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style type="text/css">
table#articlesTable thead tr {
	border-top: solid 1px #ccc;
	border-bottom: solid 1px #ccc;
}
table#articlesTable tbody tr {
	border-bottom: dotted 1px #ccc;
}
table#articlesTable thead tr th,
table#articlesTable tbody tr td {
	border: none;
}
table#articlesTable tbody tr td:nth-child(1) {
	text-align: center;
}
table#articlesTable tbody tr td:nth-child(2) {
}
table#articlesTable tbody tr td:nth-child(3) {
	text-align: center;
}
table#articlesTable tbody tr td:nth-child(4) {
	text-align: center;
}
table#articlesTable tbody tr td:nth-child(5) {
	text-align: center;
}
table#articlesTable tbody tr td:nth-child(6) {
	text-align: center;
}
button#writeButton {
	margin: 0.2rem 0rem;
	border: solid 1px #aaa;
	border-radius: 1px;
	background-color: #eee;
	padding:0 1rem;
	cursor:pointer;
	transition:200ms ease all;
	outline:none;
	cursor: pointer;
	cursor: hand;
	font-weight: bold;
}
button#writeButton:hover {
	border: solid 1px steelblue;
	box-shadow: 0px 0px 1px 1px #ccc;
}
</style>
<div style="padding:1rem;">
	<table id="articlesTable" data-juice="Grid" data-juice-bind="articles" data-juice-item="article">
		<colgroup>
			<col style="width:10rem"/>
			<col/>
			<col style="width:15rem"/>
			<col style="width:10rem"/>
			<col style="width:5rem"/>
			<col style="width:15rem"/>
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
				<th>
					<spring:message code="application.text.vote"/>
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
					<span style="display:{{$context.article.get('replyCount') > 0 ? 'inline-block' : 'none'}}">
					(<label data-juice="Label" data-juice-bind="article.replyCount"></label>)
					</span>
				</td>
				<td>
					<label data-juice="Label" data-juice-bind="article.registDate" data-juice-format="date:yyyy-MM-dd hh:mm:ss"></label>
				</td>
				<td>
					<img data-juice="Thumbnail" data-juice-bind="article.userAvatar" src="data:image/gif;base64,R0lGODlhZABkANUAAMPM1OHm6drg5NDX3f7+/ubq7fDy9Pr7/MXO1dzh5vHz9eTo7MPN1Pb3+Ont79HY3sfQ1+Xp7fT299vh5eHl6eXp7O7x8/X3+NTb4M/X3fb4+dje4+Dl6MHK0tbc4e3w8sjR187V3Pj5+vP199fd4v39/snR2N3i5uPn6/X2+Pv7/Pz8/cXO1tPa4O/x9O7w8/z8/Nnf4/r7+7/J0f///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAABkAGQAAAb/wJlwSCwaj8ikcslsOp/QqHRKrVqv2Kx2y+16v+CweEwum8/otPoMGEwilsuBQDhcLJHJALA2AzAVEjSDhIWFEhUYfH1fEAkGhpGSgwYJEIxbAAIKk52SCgKLmFUDDp6nkg4Do1QCDaiwhg0CrFAMAQSxuoQEAQy1TAgLu8SECwjASQzDxc0Lv8lGAc3UNAHRRQK51cUEtNgzA6/czQ2r0QCm5NQOorUC69zftRCc8dQKl7UJ99wJtQAg9aNmwF0fDAO5YWBVIWG1CqMACHLYTILBNAMoVjvXZ4JGahMwRfjYLAImCySLWcB0ISWxC5gOuNx1ANO2mbAI2MQZSycj/5k8UdVk1DLoKZiMUBr1tJLRyKWdTDLyCHVSSEYZq0riuEaiVkMWRzX8SgjiKIRkBy2MKPBrwX1p/9GzVzVfMnha5wFUt7QdNnFLzYGboS2ot8FCpvG8hnjGMpzPGgsR5vKY5CG3bibsBe3yEFcOZ3k+UmqgqtFINNGtBuoiaiKO2hKrpO81EhYtAriAUQyGiwAtWNgugsBDAQ33NBTwgOx1iAAjNI4IEMLzgwJASR4o8ABxhgIrgq4okCGaCQoiqoqgYKIWCdlVDZDABAJFibSDSqAAsebBB/yGfNAdGhukAGAkKWxwxgkqHCiJCieQ0QEHDnrCQQdhdEBBhadQgL/hFxRyeAoHX5wgIiwRcrFBgyeeooKCWjxgYIuopDDgFSD8RyMsH/B3BQo76oLCFSTcFyQsJcxHhQnwHemJAe1NsaGTulAwRQbpURmLCOVFUYCWuxQQxQPhgRnLCjc28aWZuojpRAjZsSlUdU0oJmcsjC2BQHR3xjJCc0p40OcuHjCx5qCwuJkEC8ghCosGwiXRgqO6tKCEnZSekucRL2QKywtJICCDp6jIAGgRGZAKS5dGxKAqKjEggemrkmwaBAA7" style="width:1rem; height:1rem; vertical-align:middle;"/>
					<label data-juice="Label" data-juice-bind="article.userNickname"></label>
				</td>
				<td>
					<label data-juice="Label" data-juice-bind="article.readCount" data-juice-format="number:0,0"></label>
				</td>
				<td>
					<i class="icon-thumbs-up"></i>
					<label data-juice="Label" data-juice-bind="article.votePositiveCount" data-juice-format="number:0,0"></label>
					<i class="icon-thumbs-down"></i>
					<label data-juice="Label" data-juice-bind="article.voteNegativeCount" data-juice-format="number:0,0"></label>
				</td>
			</tr>
		</tbody>
	</table>
	<div style="display: flex; justify-content: space-between;">
		<div>
		</div>
		<div>
			<ul data-juice="Pagination" data-juice-bind="articleSearch" data-juice-rows="rows" data-juice-page="page" data-juice-total-count="totalCount" data-juice-page-size="5">
				<li data-page="{{$context.page}}" onclick="javascript:getArticles(this.dataset.page);">{{$context.page}}</li>
			</ul>
		</div>
		<div>
			<button id="writeButton" onclick="javascript:writeArticle();">
				<i class="icon-edit"></i>
				<spring:message code="application.text.write"/>
			</button>
		</div>
	</div>
</div>