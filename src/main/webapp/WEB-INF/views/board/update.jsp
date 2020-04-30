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
					<li><a href="#" class="on">공지사항</a></li>
					<li><a href="#">문의하기</a></li>
				</ul>
			</div>
		</div>	
		<!-- //location_area -->

		<!-- bodytext_area -->
		<div class="bodytext_area box_inner">
			<!-- appForm -->
			<form class="appForm" method="post">
			<input type='hidden' name='bno' value="${boardVO.bno}" readonly="readonly">
				<fieldset>
					<legend>입력 양식</legend>
					<p class="info_pilsoo pilsoo_item">필수입력</p>
					<ul class="app_list">
						<li class="clear">
							<label for="title" class="tit_lbl pilsoo_item">제목</label>
							<div class="app_content"><input value="${boardVO.title}" type="text" class="w100p" id="title" name="title" placeholder="제목을 입력해주세요"/></div>
						</li>
						<li class="clear">
							<label for="content" class="tit_lbl">내용</label>
							<div class="app_content"><textarea id="content" name="content" class="w100p" placeholder="내용을 남겨주세요.">${boardVO.content}</textarea></div>
						</li>
						<li class="clear">
							<label for="writer" class="tit_lbl pilsoo_item">작성자명</label>
							<div class="app_content"><input value="${boardVO.writer}" readonly="readonly" type="text" class="w100p" id="writer" name="writer" placeholder="이름을 입력해주세요"/></div>
						</li>
					</ul>
					<p class="btn_line">
						<button type="submit" class="btn_baseColor">수정</button>
						<button type="button" class="btn_baseColor btn-primary">목록</button>
					</p>
				</fieldset>
			</form>
			<!-- //appForm -->			
		</div>
		<!-- //bodytext_area -->

	</div>
	<!-- //container -->
<script>				
	$(document).ready(function(){
		
		var formObj = $("form[role='form']");
		
		console.log(formObj);
		
		$(".btn-warning").on("click", function(){
			formObj.submit();
		});
		
		$(".btn-primary").on("click", function(){
			self.location = "/board/listAll";
		});
		
	});
</script>
    
<%@include file="../include/footer.jsp" %>