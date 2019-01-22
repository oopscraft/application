<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href="${SKIN_URI}/style.css" rel="stylesheet" type="text/css" />
<div class="app-board">
	<div style="display:flex; justify-content:space-between;">
		<div class="app-board-name">
			<img data-juice="Image" data-juice-bind="board.icon" data-juice-width="24" data-juice-height="24" src="${SKIN_URI}/img/icon_board.png" style="vertical-align:middle; width:1.25em; height:1.25em;"/>&nbsp;
			<c:out value="${board.name}"/>
		</div>
	</div>
	<div style="border:solid 1px #ccc; border-radius:1px; padding:1rem;">
		<div style="display:flex; justify-content:space-between; border-bottom:dotted 1px #ccc; padding-bottom: 0.2rem;">
			<div style="width:70%; font-size:1.25rem; font-weight:bold;">
				<label data-juice="Label" data-juice-bind="article.title" style="font-size:inherit;"></label>
			</div>
			<div style="width:30%; text-align:right;">
				<button class="app-board-button" onclick="javascript:modifyArticle();">
					<img class="icon" src="${SKIN_URI}/img/icon_edit.png"/>
					<spring:message code="application.text.modify"/>
				</button>
				<button class="app-board-button" onclick="javascript:deleteArticle();">
					<img class="icon" src="${SKIN_URI}/img/icon_delete.png"/>
					<spring:message code="application.text.delete"/>
				</button>
			</div>
		</div>
		<div style="display:flex; justify-content:space-between;">
			<div>
				<img data-juice="Image" data-juice-bind="article.userAvatar" src="data:image/gif;base64,R0lGODlhZABkANUAAMPM1OHm6drg5NDX3f7+/ubq7fDy9Pr7/MXO1dzh5vHz9eTo7MPN1Pb3+Ont79HY3sfQ1+Xp7fT299vh5eHl6eXp7O7x8/X3+NTb4M/X3fb4+dje4+Dl6MHK0tbc4e3w8sjR187V3Pj5+vP199fd4v39/snR2N3i5uPn6/X2+Pv7/Pz8/cXO1tPa4O/x9O7w8/z8/Nnf4/r7+7/J0f///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAABkAGQAAAb/wJlwSCwaj8ikcslsOp/QqHRKrVqv2Kx2y+16v+CweEwum8/otPoMGEwilsuBQDhcLJHJALA2AzAVEjSDhIWFEhUYfH1fEAkGhpGSgwYJEIxbAAIKk52SCgKLmFUDDp6nkg4Do1QCDaiwhg0CrFAMAQSxuoQEAQy1TAgLu8SECwjASQzDxc0Lv8lGAc3UNAHRRQK51cUEtNgzA6/czQ2r0QCm5NQOorUC69zftRCc8dQKl7UJ99wJtQAg9aNmwF0fDAO5YWBVIWG1CqMACHLYTILBNAMoVjvXZ4JGahMwRfjYLAImCySLWcB0ISWxC5gOuNx1ANO2mbAI2MQZSycj/5k8UdVk1DLoKZiMUBr1tJLRyKWdTDLyCHVSSEYZq0riuEaiVkMWRzX8SgjiKIRkBy2MKPBrwX1p/9GzVzVfMnha5wFUt7QdNnFLzYGboS2ot8FCpvG8hnjGMpzPGgsR5vKY5CG3bibsBe3yEFcOZ3k+UmqgqtFINNGtBuoiaiKO2hKrpO81EhYtAriAUQyGiwAtWNgugsBDAQ33NBTwgOx1iAAjNI4IEMLzgwJASR4o8ABxhgIrgq4okCGaCQoiqoqgYKIWCdlVDZDABAJFibSDSqAAsebBB/yGfNAdGhukAGAkKWxwxgkqHCiJCieQ0QEHDnrCQQdhdEBBhadQgL/hFxRyeAoHX5wgIiwRcrFBgyeeooKCWjxgYIuopDDgFSD8RyMsH/B3BQo76oLCFSTcFyQsJcxHhQnwHemJAe1NsaGTulAwRQbpURmLCOVFUYCWuxQQxQPhgRnLCjc28aWZuojpRAjZsSlUdU0oJmcsjC2BQHR3xjJCc0p40OcuHjCx5qCwuJkEC8ghCosGwiXRgqO6tKCEnZSekucRL2QKywtJICCDp6jIAGgRGZAKS5dGxKAqKjEggemrkmwaBAA7" style="width:1rem; height:1rem; vertical-align:middle;"/>
				<label data-juice="Label" data-juice-bind="article.userNickname"></label>
				(<label data-juice="Label" data-juice-bind="article.userId"></label>)
			</div>
			<div style="vertical-align:middle;">
				<label data-juice="Label" data-juice-bind="article.registDate" data-juice-format="date:yyyy-MM-dd hh:mm:ss"></label>
			</div>
		</div>
		<div style="min-height:50rem;">
			<pre data-juice="Text" data-juice-bind="article.contents"></pre>
		</div>
		<div style="padding:1rem;">
			<div style="text-align:right;">
				<ul data-juice="ListView" data-juice-bind="files" data-juice-item="file">
					<li>
						<img class="icon" src="${SKIN_URI}/img/icon_file.png"/>
						<Label data-juice="Label" data-juice-bind="file.name" style="font-weight:bold; border-bottom:dotted 1px #ccc;"></Label>
						(<Label data-juice="Label" data-juice-bind="file.size" data-juice-format="number:0,0"></Label>)
						bytes
						<button class="app-board-button" data-id="{{$context.file.get('id')}}" onclick="javascript:downloadFile(this.dataset.id)">
							<img class="icon" src="${SKIN_URI}/img/icon_download.png"/>
							<spring:message code="application.text.download"/>
						</button>
					</li>
				</ul>
			</div>
		</div>
		<div style="margin:0rem 1rem; border-bottom:dotted 1px #ccc; font-weight:bold;">
			<img class="icon" src="${SKIN_URI}/img/icon_reply.png" style="width:2em; height:2em;"/>&nbsp;
			<spring:message code="application.text.reply"/>:
			<span id="app-board-reply-count"></span>
		</div>
		<div>
			<ul data-juice="ListView" data-juice-bind="replies" data-juice-item="reply" style="padding:0.5rem;">
				<li style="margin:0.5rem; padding-left:{{$context.reply.get('indent')*2}}rem;">
					<div style="border-bottom: dotted 1px #ccc;">
						<div style="display:flex; justify-content:space-between;">
							<div>
								<i class="icon-level-down"></i>
								id:<label data-juice="Label" data-juice-bind="reply.id"></label>
								|
								upperNo:<label data-juice="Label" data-juice-bind="reply.upperNo"></label>
								|
								sequence:<label data-juice="Label" data-juice-bind="reply.sequence"></label>
								|
								level:<label data-juice="Label" data-juice-bind="reply.level"></label>
								<button data-id="{{$context.reply.get('id')}}" onclick="javascript:addChildReply(this.dataset.id);" class="app-board-button">
									<img class="icon" src="${SKIN_URI}/img/icon_reply.png"/>
									<spring:message code="application.text.reply"/>
								</button>
							</div>
							<div>
								<button data-id="{{$context.reply.get('id')}}" onclick="javascript:modifyReply(this.dataset.id);" class="app-board-button">
									<img class="icon" src="${SKIN_URI}/img/icon_edit.png"/>
									<spring:message code="application.text.modify"/>
								</button>
								<button data-id="{{$context.reply.get('id')}}" onclick="javascript:deleteReply(this.dataset.id);" class="app-board-button">
									<img class="icon" src="${SKIN_URI}/img/icon_delete.png"/>
									<spring:message code="application.text.delete"/>
								</button>
							</div>
						</div>
						<div id="app-board-reply-container-modify-{{$context.reply.get('id')}}">
							<pre data-juice="Text" data-juice-bind="reply.contents"></pre>
						</div>
						<div id="app-board-reply-container-child-{{$context.reply.get('id')}}">
						</div>
					</div>
				</li>
			</ul>
		</div>
		<div id="app-board-reply-container" style="padding:1rem;">
			<div id="app-board-reply-editor">
				<div>
					<img class="icon" src="${SKIN_URI}/img/icon_reply.png"/>
					<spring:message code="application.text.reply"/>
				</div>
				<sec:authorize access="!isAuthenticated()">
					<div>
						<input type="text"/>
					</div>
				</sec:authorize>
				<div>
					<textarea data-juice="TextArea" data-juice-bind="reply.contents" style="height:20rem;"></textarea>
				</div>
				<div style="text-align:right;">
					<button class="app-board-button" onclick="javascript:saveReply();">
						<img class="icon" src="${SKIN_URI}/img/icon_save.png"/>
						<spring:message code="application.text.save"/>
					</button>
					<button id="app-board-cancel-reply-button" class="app-board-button" onclick="javascript:cancelReply();">
						<img class="icon" src="${SKIN_URI}/img/icon_cancel.png"/>
						<spring:message code="application.text.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</div>
</div>