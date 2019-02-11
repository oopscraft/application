<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<link href="${SKIN_URI}/style.css" rel="stylesheet" type="text/css" />
<div class="app-board">
	<div style="display:flex; justify-content:space-between;">
		<div class="app-board-name">
			<img data-duice="Image" data-duice-bind="board.icon" data-duice-width="24" data-duice-height="24" src="${SKIN_URI}/img/icon_board.png" style="vertical-align:middle; width:1.25em; height:1.25em;"/>&nbsp;
			<c:out value="${board.name}"/>
		</div>
	</div>
	<div style="border:solid 1px #ccc; border-radius:1px; padding:1rem;">
		<div style="display:flex; justify-content:space-between; padding-bottom: 0.2rem;">
			<div style="width:70%; font-size:1.3rem; font-weight:bold;">
				<input data-duice="TextField" data-duice-bind="article.title" style="font-size:inherit; font-weight:inherit; border:none; border-bottom:dotted 1px #ccc;"/>
			</div>
			<div style="width:30%; text-align:right;">
				<button class="app-board-button" onclick="javascript:saveArticle();">
					<img class="icon" src="${SKIN_URI}/img/icon_save.png"/>
					<spring:message code="application.text.save"/>
				</button>
				<button class="app-board-button" onclick="javascript:history.back();">
					<img class="icon" src="${SKIN_URI}/img/icon_cancel.png"/>
					<spring:message code="application.text.cancel"/>
				</button>
			</div>
		</div>
		<div style="height:50rem;">
			<div data-duice="HtmlEditor" data-duice-bind="article.contents"></div>
		</div>
		<div style="text-align:right; padding:1rem;">
			<div>
				<ul data-duice="ListView" data-duice-bind="files" data-duice-item="file">
					<li>
						<i class="icon-attach"></i>
						<Label data-duice="Text" data-duice-bind="file.name" style="font-weight:bold; border-bottom:dotted 1px #ccc;"></Label>
						(<Label data-duice="Text" data-duice-bind="file.size" data-duice-format="number:0,0"></Label>)
						bytes
						<button class="app-board-button" data-index="[[$context.index]]" onclick="javascript:removeFile(this.dataset.index);">
							<i class="icon-cancel"></i>
						</button>
					</li>
				</ul>
			</div>
			<div>
				<button class="app-board-button" onclick="javascript:uploadFile();">
					<img class="icon" src="${SKIN_URI}/img/icon_file.png"/>
					<spring:message code="application.text.file"/>
					<spring:message code="application.text.attach"/>
				</button>
			</div>
		</div>
	</div>
</div>

