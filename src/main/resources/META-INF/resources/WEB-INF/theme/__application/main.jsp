<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<style type="text/css">
.container {
	display: flex;
	justify-content: space-between;
}
.division {
	border: none;
	margin: 5px;
	padding: 1rem;
}
</style>
<div class="container">
	<div class="division" style="width:100%; height:300px;">

	</div>
</div>
<div class="container">
	<div class="division" style="width:50%;">
		<div class="title2" style="width:100%; border-bottom:dotted 1px #ccc;">
			<img class="icon" src="${THEME_URI}/img/icon_latest.png"/>
			<span style="font-weight:bold;">
				최근 게시글
			</span>
		</div>
		<ul id="latestArticles" data-juice="ListView" data-juice-bind="latestArticles" data-juice-item="article">
			<li>
				<div>
					<label data-juice="Label" data-juice-bind="article.title"></label>
				</div>
			</li>
		</ul>
	</div>
	<div class="division" style="width:50%;">
		<div class="title2" style="width:100%; border-bottom:dotted 1px #ccc;">
			<img class="icon" src="${THEME_URI}/img/icon_best.png"/>
			<span style="font-weight:bold;">
				베스트 게시글
			</span>
		</div>
		<ul id="besttArticles" data-juice="ListView" data-juice-bind="bestArticles" data-juice-item="article">
			<li>
				<div>
					<label data-juice="Label" data-juice-bind="article.title"></label>
				</div>
			</li>
		</ul>
	</div>
</div>


