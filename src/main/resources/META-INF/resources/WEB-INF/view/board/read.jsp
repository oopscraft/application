<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<c:set var="SKIN_DIR" value="/WEB-INF/theme/${__configuration.theme}/board/${board.skin}" scope="request"/>
<c:set var="SKIN_URI" value="${pageContext.request.contextPath}/resources/theme/${__configuration.theme}/board/${board.skin}" scope="request"/>
<script type="text/javascript">
var board = new duice.data.Map(${app:toJson(board)});
var article = new duice.data.Map();
var files = new duice.data.List();
var replies = new duice.data.List();
var reply = new duice.data.Map();

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
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleId}'
		,type: 'GET'
		,success: function(data, textStatus, jqXHR) {
			article.fromJson(data);
			files.fromJson(data.files);
  	 	}
	});	
}

/**
 * Modifies article
 */
function modifyArticle(){
	location.href = '${pageContext.request.contextPath}/board/${boardId}/write?articleId=${param.articleId}';
}

/**
 * Deletes article.
 */
function deleteArticle(){
	<spring:message code="application.text.article" var="item"/>
	new duice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleId}'
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
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleId}/replies'
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
function addChildReply(id) {
	cancelReply();
	reply.fromJson({});
	reply.set('upperId', id);
	$('#app-board-reply-container-child-'+id).append($('#app-board-reply-editor'));
}

/**
 * Modifies reply
 */
function modifyReply(id){
	cancelReply();
	replies.forEach(function(map){
		if(map.get('id') == id){
			reply.fromJson(map.toJson());
			return false;
		}
	});
	var modifyReplyContainer = $('#app-board-reply-container-modify-'+id);
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
		 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleId}/reply'
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
function deleteReply(id) {
	<spring:message code="application.text.article" var="item"/>
	new duice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		$.ajax({
			 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleId}/reply/' + id
			,type: 'DELETE'
			,success: function(data, textStatus, jqXHR) {
				getReplies();
		 	}
		});
	}).open();
}

/**
 * Download file
 */
function downloadFile(id){
    var link = document.createElement('a');
    link.href = '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleId}/file/' + id;
    link.click();
}
</script>
<jsp:include page="${SKIN_DIR}/read.jsp" flush="true"/>


