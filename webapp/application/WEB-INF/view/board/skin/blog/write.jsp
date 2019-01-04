<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<div>
	<div>
		<input data-juice="TextField" data-juice-bind="article.title"/>
	</div>
	<div>
		<div data-juice="HtmlEditor" data-juice-bind="article.contents"></div>
	</div>
	<div>
		<textarea data-juice="TextArea" data-juice-bind="article.contents"></textarea>
	</div>
	<div style="display: flex; justify-content: end;">
		<div>
			<button id="writeButton" onclick="javascript:postArticle();">
				<i class="icon-edit"></i>
				<spring:message code="application.text.write"/>
			</button>
		</div>
	</div>
</div>
