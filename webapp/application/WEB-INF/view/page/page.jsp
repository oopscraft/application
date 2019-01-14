<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:choose>
	<c:when test="${page.type == 'JSP'}">
		<jsp:include page="${page.value}" flush="true" />
	</c:when>
	<c:when test="${page.type == 'URL'}">
		<iframe style="width:100%; height:100%;border:none;" src="${page.value}"></iframe>
	</c:when>
</c:choose>

