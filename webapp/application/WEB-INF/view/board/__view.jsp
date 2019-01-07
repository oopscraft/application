<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="/WEB-INF/tld/application.tld"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<link href="${pageContext.request.contextPath}/resource/board/skin/${board.skinId}/style.css" rel="stylesheet" type="text/css" />
<!-- global -->
<script type="text/javascript">
var board = new juice.data.Map(${app:toJson(board)});
var article = new juice.data.Map();
var replies = new juice.data.List();
var reply = new juice.data.Map();

/**
 * On document loaded
 */
$( document ).ready(function() {
	getArticle();
	getReplies();
});

/**
 * Gets article
 */
function getArticle() {
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleNo}'
		,type: 'GET'
		,success: function(data, textStatus, jqXHR) {
			article.fromJson(data);
  	 	}
	});	
}

/**
 * Modifies article
 */
function modifyArticle(){
	location.href = '${pageContext.request.contextPath}/board/${boardId}/write?articleNo=${param.articleNo}';
}

/**
 * Deletes article.
 */
function deleteArticle(){
	<spring:message code="application.text.article" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleNo}'
			,type: 'DELETE'
			,success: function(data, textStatus, jqXHR) {
				history.back();
	 	 	}
		});	
	}).open();
}

/**
 * Gets replies.
 */
function getReplies() {
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleNo}/replies'
		,type: 'GET'
		,success: function(data, textStatus, jqXHR) {
			// defines indent depth
			data.forEach(function(item){
				if(item.level) {
					item.indent = item.level.length;
				}else{
					item.indent = 0;
				}
				console.log(item.indent);
			});
			replies.fromJson(data);
			$('#app-board-reply-count').text(replies.getRowCount());
 	 	}
	});	
}

/**
 * Adds Reply
 */
function addReply(){
	reply.fromJson({});
	$('#app-board-reply-container').append($('#app-board-reply-editor'));
}

/**
 * Adds child reply
 */
function addChildReply(no) {
	cancelReply();
	reply.fromJson({});
	reply.set('upperNo', no);
	$('#app-board-reply-container-child-'+no).append($('#app-board-reply-editor'));
}

/**
 * Modifies reply
 */
function modifyReply(no){
	cancelReply();
	replies.forEach(function(map){
		if(map.get('no') == no){
			reply.fromJson(map.toJson());
			return false;
		}
	});
	var modifyReplyContainer = $('#app-board-reply-container-modify-'+no);
	modifyReplyContainer.children().hide();
	modifyReplyContainer.append($('#app-board-reply-editor'));
}

/**
 * Cancels reply 
 */
function cancelReply(){
	reply.fromJson({});
	var replyEditor = $('#app-board-reply-editor');
	replyEditor.parent().children().show();
	$('#app-board-reply-container').append(replyEditor);
}

/**
 * Saves reply
 */
function saveReply() {
	$.ajax({
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleNo}/reply'
		,type: 'POST'
		,data: JSON.stringify(reply.toJson())
		,contentType: "application/json"
		,success: function(data, textStatus, jqXHR) {
			cancelReply();
			getReplies();
	 	}
	});
}

/**
 * Deletes reply
 */
function deleteReply(no) {
	<spring:message code="application.text.article" var="item"/>
	new juice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleNo}/reply/' + no
			,type: 'DELETE'
			,success: function(data, textStatus, jqXHR) {
				getReplies();
		 	}
		});
	}).open();
}
</script>
<style type="text/css">
</style>
<jsp:include page="/WEB-INF/view/board/skin/${board.skinId}/view.jsp" flush="true"/>


