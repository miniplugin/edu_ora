<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
				</ul>
			</div>
		</div>	
		<!-- //location_area -->

		<!-- bodytext_area -->
		<div class="bodytext_area box_inner">
			<form action="#" class="minisrch_form">
				<fieldset>
					<legend>검색</legend>
					<input type="text" class="tbox" title="검색어를 입력해주세요" placeholder="검색어를 입력해주세요">
					<a href="javascript:;" class="btn_srch">검색</a>
				</fieldset>
			</form>
			<table class="bbsListTbl" summary="번호,제목,조회수,작성일 등을 제공하는 표">
				<caption class="hdd">공지사항  목록</caption>
				<thead>
					<tr>
						<th scope="col">번호</th>
						<th scope="col">제목</th>
						<th scope="col">조회수</th>
						<th scope="col">작성일</th>
					</tr>
				</thead>
				<tbody>
				
<c:forEach items="${list}" var="boardVO">

					<tr>
						<td>${boardVO.bno}</td>
						<td class="tit_notice"><a href='/board/read?bno=${boardVO.bno}'>${boardVO.title}&nbsp;<strong>[ ${boardVO.reply_count} ]</strong></a></td>
						<td>${boardVO.view_count }</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
							value="${boardVO.regdate}" /></td>
					</tr>

</c:forEach>

				</tbody>
			</table>
			<!-- pagination -->
			<div class="pagination">
				<a href="notice_list" class="firstpage  pbtn"><img src="/resources/home/img/btn_firstpage.png" alt="첫 페이지로 이동"></a>
				<a href="notice_list" class="prevpage  pbtn"><img src="/resources/home/img/btn_prevpage.png" alt="이전 페이지로 이동"></a>
				<a href="notice_list"><span class="pagenum currentpage">1</span></a>
				<a href="notice_list"><span class="pagenum">2</span></a>
				<a href="notice_list"><span class="pagenum">3</span></a>
				<a href="notice_list"><span class="pagenum">4</span></a>
				<a href="notice_list"><span class="pagenum">5</span></a>
				<a href="notice_list" class="nextpage  pbtn"><img src="/resources/home/img/btn_nextpage.png" alt="다음 페이지로 이동"></a>
				<a href="notice_list" class="lastpage  pbtn"><img src="/resources/home/img/btn_lastpage.png" alt="마지막 페이지로 이동"></a>
			</div>
			<!-- //pagination -->
			<p class="btn_line"><a href="/board/create" class="btn_baseColor">등록</a></p>
		</div>
		<!-- //bodytext_area -->
		
	</div>
	<!-- //container -->
<script>
	var msg = '${msg}';
	if(msg == "success"){
		alert("처리가 완료되었습니다.");
	}
	$(document).ready(function(){
	
		$(".btn-primary").on("click", function(){
			self.location = "/board/create";
		});
		
	});
</script>
    
<%@include file="../include/footer.jsp" %>