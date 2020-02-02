<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="app" uri="http://application.oopscraft.net"%>
<c:set var="SKIN_DIR" value="/WEB-INF/theme/${__configuration.theme}/board/${board.skin}" scope="request"/>
<c:set var="SKIN_URI" value="${pageContext.request.contextPath}/resources/theme/${__configuration.theme}/board/${board.skin}" scope="request"/>
<script type="text/javascript">
var board = new duice.data.Map(${app:toJson(board)});
var article = new duice.data.Map();
var files = new duice.data.List();

/**
 * On document loaded
 */
$( document ).ready(function() {
	<c:if test="${param.articleId != null}">
	getArticle();
	</c:if>
});

/**
 * windows unload
 */
$(window).on('beforeunload', function(){
    return "When you move the screen, the changed data will be initialized.";
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
 * Saves article
 */
function saveArticle() {
	<spring:message code="application.text.article" var="item"/>
	new duice.ui.Confirm('<spring:message code="application.message.saveItem.confirm" arguments="${item}"/>')
	.afterConfirm(function() {
		
		var articleData = article.toJson();
		articleData.files = files.toJson();
		
		$.ajax({
			 url: '${pageContext.request.contextPath}/api/board/${boardId}/article'
			,type: 'POST'
			,data: JSON.stringify(articleData)
			,contentType: "application/json"
			,success: function(data, textStatus, jqXHR) {
				$(window).off('beforeunload');
				history.back();
	 	 	}
		});
	}).open();
}

/**
 * Uploads file
 */
function uploadFile() {
	
	var file = document.createElement('input');
	file.setAttribute('type', 'file');
	file.addEventListener('change', function(e){
		var formData = new FormData();
		formData.append('file', this.files[0]);
		$.ajax({
			 url: '${pageContext.request.contextPath}/api/board/${boardId}/article/${param.articleId}/file'
			,type: 'POST'
			,data: formData
			,enctype: 'multipart/form-data'
			,cache: false
			,contentType: false
			,processData: false
			,xhr: function() {
				var xhr = new window.XMLHttpRequest();
				xhr.upload.addEventListener("progress", function(evt) {
					if (evt.lengthComputable) {
						var percentComplete = evt.loaded / evt.total;
						percentComplete = parseInt(percentComplete * 100);
						console.log(percentComplete);
					}
				}, false);
			    return xhr;
			}
			,success: function(data, textStatus, jqXHR) {
				files.addRow(new duice.data.Map(data));
	 	 	}
		});
	});
	file.click();
}

/**
 * Removes file
 */
function removeFile(index) {
	files.removeRow(index);
}
</script>
<jsp:include page="${SKIN_DIR}/write.jsp" flush="true"/>

