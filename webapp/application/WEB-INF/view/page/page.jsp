<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
console.log('${page.value}');
</script>
<!--  
<iframe src="${page.value}">


<iframe style="width:100%; height:100%;" src="https://docs.google.com/gview?url=https://github.com/oopscraft/application/blob/master/doc/Application%20Architecture%20Definition.docx?raw=true&embedded=true">

</iframe>
-->

<c:out value="${page.value}"/>

<%@ include file="request.getParameter('page').getValue()" %>