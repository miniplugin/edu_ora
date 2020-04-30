<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@include file="../include/header.jsp" %>
<!-- Bootstrap 3.3.2 JS 댓글modal창 때문에 추가-->
<link href="/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<script src="/resources/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<!-- ...댓글 모달창 CSS. -->
<style>
	h1, h2, h3, h4, h5, h6 {
    font-size: inherit;
    font-family: inherit;
    font-weight: inherit;
    line-height: inherit;
    color: inherit;
    margin-top: inherit;
    margin-bottom: inherit;
	}
	p {
    margin: inherit;
	}
	ol, ul {
    margin-top: inherit;
    margin-bottom: inherit;
	}
	.pagination li {
	    list-style: inherit !important;
	    float:  inherit !important;
	    padding:  inherit !important;
	    border:  inherit !important;
	    margin:  inherit !important;
	}
	#modDiv {
		width: 300px;
		height: 100px;
		background-color: gray;
		position: absolute;
		top: 50%;
		left: 50%;
		margin-top: -50px;
		margin-left: -150px;
		padding: 10px;
		z-index: 1000;
	}
	
	.pagination {
	  width: 100%;
	}
	
	.pagination li{
	  list-style: none;
	  float: left; 
	  padding: 3px; 
	  border: 1px solid blue;
	  margin:3px;  
	}
	
	.pagination li a{
	  margin: 3px;
	  text-decoration: none;  
	}	
	</style>
<!-- ...433p.Mustach를 기반으로 작성된 handlebars.js 이용 댓글 Ajax . -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
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
			<ul class="bbsview_list">
				<li class="bbs_title">${boardVO.title}</li>
				<li class="bbs_hit">작성일 : <span><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
							value="${boardVO.regdate}" /></span></li>
				<li class="bbs_date">조회수 : <span>${boardVO.view_count}</span></li>
				<li class="bbs_content">
					<div class="editer_content">${boardVO.content}</div>
				</li>
			</ul>
			<p class="btn_line txt_right">
				<button type="submit" class="btn_bbs" id="btn-warning">수정</button>
				<button type="submit" class="btn_bbs" id="btn-danger">삭제</button>
				<button type="submit" class="btn_bbs" id="btn-primary">목록</button>
			</p>
			<ul class="near_list mt20">
				<li><h4 class="prev">다음글</h4><a href="notice_view">추석 연휴 티켓/투어 배송 및 직접 수령 안내</a></li>		
				<li><h4 class="next">이전글</h4><a href="notice_view">이번 여름 휴가 제주 갈까? 미션 투어 (스프링경비 50만원 지원)</a></li>
			</ul>
		</div>
		<!-- //bodytext_area -->
	
<form role="form" method="post">

	<input type='hidden' name='bno' value="${boardVO.bno}">

</form>
<script>				
	$(document).ready(function(){
		
		var formObj = $("form[role='form']");
		
		console.log(formObj);
		
		$("#btn-warning").on("click", function(){
			formObj.attr("action", "/board/update");
			formObj.attr("method", "get");		
			formObj.submit();
		});
		
		$("#btn-danger").on("click", function(){
			formObj.attr("action", "/board/delete");
			formObj.submit();
		});
		
		$("#btn-primary").on("click", function(){
			self.location = "/board/listAll";
		});
		
	});
</script>
<!-- bodytext_area -->
		<div class="bodytext_area box_inner">
		<!-- ...426p. 댓글 등록에 필요한 div -->
			<div class="appForm">		
				<p class="info_pilsoo pilsoo_item">댓글 등록</p>
				<ul class="app_list">
					<li class="clear">
						<label for="newReplyWriter" class="tit_lbl pilsoo_item">사용자</label>
						<div class="app_content"><input type="text" class="w100p" id="newReplyWriter" placeholder="아이디를 입력해주세요"/></div>
					</li>
					<li class="clear">
						<label for="newReplyText" class="tit_lbl pilsoo_item">내용</label>
						<div class="app_content"><input type="text" class="w100p" id="newReplyText" placeholder="내용을 입력해주세요"/></div>
					</li>
				</ul>
				<p class="btn_line">
					<button type="button" id="replyAddBtn" class="btn_baseColor">댓글등록</button>
				</p>
			</div>
			<!-- ...426p. 댓글 목록과 페이징 처리에 필요한 div -->
			<ul class="timeline near_list">
				<!-- timeline time label -->
				<li class="time-label" id="repliesDiv">
					<button class="btn_bbs" id='reply_countSmall' style="width:100px">
						댓글목록
						<!-- ...510p.댓글목록갯수. -->  
						<small> [ ${boardVO.reply_count} ] </small>
					</button>
				</li>
			</ul>

			<div class='text-center'>
				<ul id="pagination" class="pagination pagination-sm no-margin ">

				</ul>
			</div>
		</div>
		<!-- //bodytext_area -->

		<!-- ...442p. 수정과 삭제를 위한 Modal 창 -->
		<div id="modifyModal" class="modal modal-primary fade" role="dialog">
		  <div class="modal-dialog">
		    <!-- Modal content-->
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal">&times;</button>
		        <h4 class="modal-title"></h4>
		      </div>
		      <div class="modal-body" data-rno>
		        <p><input type="text" id="replytext" class="form-control"></p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-info" id="replyModBtn">Modify</button>
		        <button type="button" class="btn btn-danger" id="replyDelBtn">DELETE</button>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div> 	
</div>
<!-- //container -->
<!-- 
	...435p. template코드는 화면상에서 하나의 댓글을 구성하는 부분임.
	...prettifyDate regdate 
-->
<script id="template" type="text/x-handlebars-template">
	{{#each .}}
		<li class="replyLi near_list" data-rno={{rno}}>
		<i class="fa fa-comments bg-blue"></i>
		 <div class="timeline-item" >
		  	<span class="time">
		    	<i class="fa fa-clock-o"></i>{{prettifyDate regdate}}
		  	</span>
		  	<h3 class="timeline-header h4"><strong>{{rno}}</strong> -{{replyer}}</h3>
			  <div class="timeline-body">{{replytext}}</div>
		      <div class="timeline-footer">
		     	<a class="btn btn-primary btn-xs" 
			       data-toggle="modal" data-target="#modifyModal">
					Modify
				</a>
		      </div>
		  </div>			
		</li>
	{{/each}}
</script>

<!-- 
	...435p.prettifyDate에 대한 자바스크립트 처리. 
	...handlebars는 helper라는 기능을 이용해서 데이터의 상세한 처리에 필요한 기능들을 처리함.
	...만일 원하는 기능이 없는 경우에는 registerHelper()를 이용해서 사용자가 새로운 기능을 추가할
	...수 있음.
 -->
<script>
	Handlebars.registerHelper("prettifyDate", function(timeValue) {
		var dateObj = new Date(timeValue);
		var year = dateObj.getFullYear();
		var month = dateObj.getMonth() + 1;
		var date = dateObj.getDate();
		return year + "/" + month + "/" + date;
	});
	
	var printData = function(replyArr, target, templateObject) {
	
		var template = Handlebars.compile(templateObject.html());
	
		var html = template(replyArr);
		$(".replyLi").remove();
		target.after(html);
	
	}
	

	//...436p. 해당 게시물에 대한 번호.
	var bno = ${boardVO.bno};
	
	//...436p. 수정이나 삭제작업 이후 사용자가 보던 댓글의 페이지 번호를 가지고 다시 목록을
	//...출력하기 위해 유지되는 데이터임.
	var replyPage = 1;

	//...436p. getPage() : 특정한 게시물에 대한 페이징 처리를 위해 호출되는 함수.
	//...페이지번호를 전달받고, 댓글의 목록 데이터를 처리함.
	//...댓글의 목록 데이터는 'pageMaker'와 'list'로 구성되므로 이를 printPaging()과
	//...printData()에서 처리함.
	function getPage(pageInfo) {

		$.getJSON(pageInfo, function(data) {
			printData(data.list, $("#repliesDiv"), $('#template'));
			printPaging(data.pageMaker, $(".pagination"));
			
			$("#reply_countSmall small").html("[ " + data.pageMaker.totalCount +" ]"); //...added since 510p.
			console.log("댓글 총갯수: " + data.pageMaker.totalCount)
			$("#modifyModal").modal('hide');
			

		});
	}

	var printPaging = function(pageMaker, target) {

		var str = "";

		if (pageMaker.prev) {
			str += "<li><a href='" + (pageMaker.startPage - 1)
					+ "'> << </a></li>";
		}

		for (var i = pageMaker.startPage, len = pageMaker.endPage; i <= len; i++) {
			var strClass = pageMaker.cri.page == i ? 'class=active' : '';
			str += "<li "+strClass+"><a href='"+i+"'>" + i + "</a></li>";
		}

		if (pageMaker.next) {
			str += "<li><a href='" + (pageMaker.endPage + 1)
					+ "'> >> </a></li>";
		}

		target.html(str);
	};	
	
	
	//...437p.화면상에서 'Replies List'라는 버튼을 클릭했을때 댓글 목록을 가져와서 보임.
	//...★ /replies/로 요청경로를 한 이유를 알아야 함.
	//...ajax를 사용해서 JSON으로 값을 받아와서 jQuery로 화면에 표시하므로 JSON으로 값을
	//...처리해주는 별도의 @RestController ReplyController를 사용하고 있다.
	$("#repliesDiv").on("click", function() {

		if ($(".timeline li").size() > 1) {
			return;
		}
		getPage("/replies/" + bno + "/1");

	});
		
	
	//...438p.페이징 처리의 코드는 ul class = 'pagination' 에서 이뤄짐.
	$(".pagination").on("click", "li a", function(event){
		
		event.preventDefault();
		
		replyPage = $(this).attr("href");
		
		getPage("/replies/"+bno+"/"+replyPage);
		
	});

	//...440p. 댓글추가 이벤트 처리.
	$("#replyAddBtn").on("click",function(){
		 
		 var replyerObj = $("#newReplyWriter");
		 var replytextObj = $("#newReplyText");
		 var replyer = replyerObj.val();
		 var replytext = replytextObj.val();
		
		  
		  $.ajax({
				type:'post',
				url:'/replies/',
				headers: { 
				      "Content-Type": "application/json",
				      "X-HTTP-Method-Override": "POST" },
				dataType:'text',
				data: JSON.stringify({bno:bno, 
									  replyer:replyer, 
									  replytext:replytext}),
				success:function(result){
					console.log("replyAddBtn clicked result: " + result);
					if(result == 'SUCCESS'){
						alert("등록 되었습니다.");
						replyPage = 1;
						getPage("/replies/"+bno+"/"+replyPage );
						replyerObj.val("");
						replytextObj.val("");
					}
			}});
	});

	
	//...443p. 각 댓글의 버튼 이벤트 처리.
	//...실제 댓글목록에 관한 소스에는 다음과 같은 부분이 대상이 된다.
	/*
		div class = "timeline-footer"
		a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modifyModal"
		Modify
		a
		처럼 'data-'로 시작하는 커스텀 속성을 활용해서 'modifyModal'아이디에 속하는 
		div를 화면에 보이게 함.
	*/
	$(".timeline").on("click", ".replyLi", function(event){
		
		var reply = $(this);
		
		$("#replytext").val(reply.find('.timeline-body').text());
		$(".modal-title").html(reply.attr("data-rno"));
		
	});
	
	
	//...댓글 수정 버튼 이벤트.
	$("#replyModBtn").on("click",function(){
		  
		  var rno = $(".modal-title").html();
		  var replytext = $("#replytext").val();
		  
		  $.ajax({
				type:'put',
				url:'/replies/'+rno,
				headers: { 
				      "Content-Type": "application/json",
				      "X-HTTP-Method-Override": "PUT" },
				data:JSON.stringify({replytext:replytext}), 
				dataType:'text', 
				success:function(result){
					console.log("댓글 수정 result: " + result);
					if(result == 'SUCCESS'){
						alert("수정 되었습니다.");
						getPage("/replies/"+bno+"/"+replyPage );
					}
			}});
	});

	//...댓글 삭제 버튼 이벤트.
	$("#replyDelBtn").on("click",function(){
		  
		  var rno = $(".modal-title").html();
		  var replytext = $("#replytext").val();
		  
		  $.ajax({
				type:'delete',
				url:'/replies/'+rno,
				headers: { 
				      "Content-Type": "application/json",
				      "X-HTTP-Method-Override": "DELETE" },
				dataType:'text', 
				success:function(result){
					console.log("댓글 삭제 result: " + result);
					if(result == 'SUCCESS'){
						alert("삭제 되었습니다.");
						getPage("/replies/"+bno+"/"+replyPage );
					}
			}});
	});	
		
		
</script>
<%@include file="../include/footer.jsp" %>