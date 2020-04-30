<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- ...169, 625p. -->
<%@ page session="true" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
  						 pageEncoding="UTF-8"%>

<%@include file="../include/header.jsp" %>

	<!-- Content Wrapper. Contains page content -->
	<div id="container">
	
		<!-- location_area -->
		<div class="location_area customer">
			<div class="box_inner">
				<h2 class="tit_page">스프링 <span class="in">in</span> 자바</h2>
				<p class="location">고객센터 <span class="path">/</span> 공지사항</p>
				<ul class="page_menu clear">
					<li><a href="#" class="on">공지사항SUCCESS PAGE</a></li>
				</ul>
			</div>
		</div>	
		<!-- //location_area -->

		<!-- bodytext_area -->
		<div class="bodytext_area box_inner">
			<ul class="bbsview_list">
				<li class="bbs_title">${msg}!!!</li>
			</ul>
			<p class="btn_line">
				<button type="button" class="btn_baseColor btn-primary">목록</button>
			</p>
		</div>
		<!-- //bodytext_area -->

	</div>
	<!-- //container -->
<script>				
	$(document).ready(function(){
			
		$(".btn-primary").on("click", function(){
			self.location = "/board/listAll";
		});
		
	});
</script>
    
<%@include file="../include/footer.jsp" %>