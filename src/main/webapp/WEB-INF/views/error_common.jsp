<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page session="true" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
  						 pageEncoding="UTF-8"%>

<%@include file="include/header.jsp" %>

	<!-- Content Wrapper. Contains page content -->
	<div id="container">
		<!-- location_area -->
		<div class="location_area customer">
			<div class="box_inner">
				<h2 class="tit_page">에러페이지</h2>
				<p class="location">에러명 <span class="path">/</span> ${exception.getMessage() }</p>
			</div>
		</div>	
		<!-- //location_area -->

		<!-- bodytext_area -->
		<div class="bodytext_area box_inner">
		<p class="subttl">에러 상세내역</p>
			<ul class="app_list">
			<c:forEach items="${exception.getStackTrace()}" var="stack">
				<li class="clear">${stack.toString() }</li>
			</c:forEach>
			</ul>
		</div>
		<!-- //bodytext_area -->

	</div>
	<!-- //container -->

    
<%@include file="include/footer.jsp" %>