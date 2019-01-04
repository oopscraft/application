<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style type="text/css">
#articleDiv {
	margin: 1rem;
	padding: 1rem;
	border: solid 1px #ccc;
	border-radius: 1px;
}
#articleDiv #titleDiv {
	border-bottom: dotted 1px #ccc;
	display: flex;
	justify-content: space-between;
}
#articleDiv #detailDiv {
	display: flex;
	justify-content: space-between;
	padding-left:0.5rem;
}
#articleDiv #contentsDiv {
	min-height: 70vh;
}
button#modifyButton {
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
button#modifyButton:hover {
	border: solid 1px steelblue;
	box-shadow: 0px 0px 1px 1px #ccc;
}
button#deleteButton {
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
button#deleteButton:hover {
	border: solid 1px steelblue;
	box-shadow: 0px 0px 1px 1px #ccc;
}
</style>
<div id="articleDiv">
	<div id="titleDiv">
		<div>
			<label data-juice="Label" data-juice-bind="article.title" style="font-size:1.3rem; font-weight:bold;"></label>
		</div>
		<div>
			<button id="modifyButton" onclick="javascript:modifyArticle();">
				<i class="icon-disk"></i>
				<spring:message code="application.text.modify"/>
			</button>
			<button id="deleteButton" onclick="javascript:deleteArticle();">
				<i class="icon-trash"></i>
				<spring:message code="application.text.delete"/>
			</button>
		</div>
	</div>
	<div id="detailDiv">
		<div>
			<img data-juice="Thumbnail" data-juice-bind="article.userAvatar" src="data:image/gif;base64,R0lGODlhZABkANUAAMPM1OHm6drg5NDX3f7+/ubq7fDy9Pr7/MXO1dzh5vHz9eTo7MPN1Pb3+Ont79HY3sfQ1+Xp7fT299vh5eHl6eXp7O7x8/X3+NTb4M/X3fb4+dje4+Dl6MHK0tbc4e3w8sjR187V3Pj5+vP199fd4v39/snR2N3i5uPn6/X2+Pv7/Pz8/cXO1tPa4O/x9O7w8/z8/Nnf4/r7+7/J0f///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAABkAGQAAAb/wJlwSCwaj8ikcslsOp/QqHRKrVqv2Kx2y+16v+CweEwum8/otPoMGEwilsuBQDhcLJHJALA2AzAVEjSDhIWFEhUYfH1fEAkGhpGSgwYJEIxbAAIKk52SCgKLmFUDDp6nkg4Do1QCDaiwhg0CrFAMAQSxuoQEAQy1TAgLu8SECwjASQzDxc0Lv8lGAc3UNAHRRQK51cUEtNgzA6/czQ2r0QCm5NQOorUC69zftRCc8dQKl7UJ99wJtQAg9aNmwF0fDAO5YWBVIWG1CqMACHLYTILBNAMoVjvXZ4JGahMwRfjYLAImCySLWcB0ISWxC5gOuNx1ANO2mbAI2MQZSycj/5k8UdVk1DLoKZiMUBr1tJLRyKWdTDLyCHVSSEYZq0riuEaiVkMWRzX8SgjiKIRkBy2MKPBrwX1p/9GzVzVfMnha5wFUt7QdNnFLzYGboS2ot8FCpvG8hnjGMpzPGgsR5vKY5CG3bibsBe3yEFcOZ3k+UmqgqtFINNGtBuoiaiKO2hKrpO81EhYtAriAUQyGiwAtWNgugsBDAQ33NBTwgOx1iAAjNI4IEMLzgwJASR4o8ABxhgIrgq4okCGaCQoiqoqgYKIWCdlVDZDABAJFibSDSqAAsebBB/yGfNAdGhukAGAkKWxwxgkqHCiJCieQ0QEHDnrCQQdhdEBBhadQgL/hFxRyeAoHX5wgIiwRcrFBgyeeooKCWjxgYIuopDDgFSD8RyMsH/B3BQo76oLCFSTcFyQsJcxHhQnwHemJAe1NsaGTulAwRQbpURmLCOVFUYCWuxQQxQPhgRnLCjc28aWZuojpRAjZsSlUdU0oJmcsjC2BQHR3xjJCc0p40OcuHjCx5qCwuJkEC8ghCosGwiXRgqO6tKCEnZSekucRL2QKywtJICCDp6jIAGgRGZAKS5dGxKAqKjEggemrkmwaBAA7" style="width:1rem; height:1rem; vertical-align:middle;"/>
			<label data-juice="Label" data-juice-bind="article.userNickname"></label>
			(<label data-juice="Label" data-juice-bind="article.userId"></label>)
		</div>
		<div style="vertical-align:middle;">
			<label data-juice="Label" data-juice-bind="article.registDate" data-juice-format="date:yyyy-MM-dd hh:mm:ss"></label>
		</div>
	</div>
	<div id="contentsDiv">
		<pre data-juice="Contents" data-juice-bind="article.contents"></pre>
	</div>
</div>
