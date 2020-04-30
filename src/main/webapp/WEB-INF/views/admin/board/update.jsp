<!-- ...201p. -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@include file="../include/header.jsp"%>
<!-- 첨부파일 업로드 css -->
<style>
.fileDrop {
  width: 80%;
  height: 100px;
  border: 1px dotted gray;
  background-color: lightslategrey;
  margin: auto;
  
}
   .popup {position: absolute;}
   .back { background-color: gray; opacity:0.5; width: 100%; height: 300%; overflow:hidden;  z-index:1101;}
   .front { 
      z-index:1110; opacity:1; boarder:1px; margin: auto; 
     }
    .show{
      position:relative;
      max-width: 1200px; 
      max-height: 800px; 
      overflow: auto;       
    }   	
</style>
<div class='popup back' style="display:none;"></div>
<div id="popup_front" class='popup front' style="display:none;">
 <img id="popup_img">
</div>
<!-- Main content -->
<section class="content">
	<div class="row">
		<!-- left column -->
		<div class="col-md-12">
			<!-- general form elements -->
			<div class="box box-primary">
				<div class="box-header">
					<h3 class="box-title">UPDATE BOARD</h3>
				</div>
				<!-- /.box-header -->

<!-- 202p. form::action 속성이 지정되지 않으면 현재 경로를 그대로 action의 대상 경로로 잡음. -->
<form role="form" method="post">
	<input type='hidden' name='bno' value="${boardVO.bno}" readonly="readonly">
	<!-- 페이지 넘버링 처리 -->
	<input type='hidden' name='page' value="${cri.page}">
	<input type='hidden' name='perPageNum' value="${cri.perPageNum}">
	<!-- 페이지 검색 처리 
	<input type='hidden' name='searchType' value="${cri.searchType}">
	<input type='hidden' name='keyword' value="${cri.keyword}">
	-->
	<div class="box-body">
		<div class="form-group">
			<label for="exampleInputEmail1">Title</label> 
			<input type="text"
				name='title' class="form-control" placeholder="Enter Title" value="${boardVO.title}">
		</div>
		<div class="form-group">
			<label for="exampleInputPassword1">Content</label>
			<textarea class="form-control" name="content" rows="3"
				placeholder="Enter ...">${boardVO.content}</textarea>
		</div>
		<div class="form-group">
			<label for="exampleInputEmail1">Writer</label> 
			<input type="text"
				name="writer" class="form-control" placeholder="Enter Writer" value="${boardVO.writer}" readonly="readonly">
		</div>
		
		<div class="form-group">
			<label for="exampleInputEmail1">아래 영역에 파일을 드래그 해서 업로드 가능</label>
			<div class="fileDrop"></div>
			아래 기존 파일 업로드 창 사용가능<input type="file">
		</div>
	</div>
	<!-- /.box-body -->

	<div class="box-footer">
		<div>
			<hr>
		</div>
		<ul class="mailbox-attachments clearfix uploadedList">
		</ul>
		<button type="submit" class="btn btn-warning">Submit</button>
		<button type="button" class="btn btn-primary">LIST ALL</button>
	</div>
</form>


			</div>
			<!-- /.box -->
		</div>
		<!--/.col (left) -->

	</div>
	<!-- /.row -->
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script type="text/javascript" src="/resources/js/upload.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>

<script id="template" type="text/x-handlebars-template">
<li>
  <span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}" alt="Attachment"></span>
  <div class="mailbox-attachment-info">
	<a href="{{getLink}}" class="mailbox-attachment-name">{{fileName}}</a>
	<a href="{{fullName}}" 
     class="btn btn-default btn-xs pull-right delbtn"><i class="fa fa-fw fa-remove"></i></a>
	</span>
  </div>
</li>                
</script> 

<script>				
	$(document).ready(function(){
		
		var formObj = $("form[role='form']");
		
		console.log(formObj);
		
		/* $(".btn-warning").on("click", function(){
			formObj.submit();
		}); */
		formObj.submit(function(event){
			event.preventDefault();
			var that = $(this);
			var str ="";
			$(".uploadedList .delbtn").each(function(index){
				 str += "<input type='hidden' name='files["+index+"]' value='"+$(this).attr("href") +"'> ";
			});
			that.append(str);
			console.log(str);
			that.get(0).submit();
		});
		/*
		$(".btn-primary").on("click", function(){
			self.location = "/admin/board/listAll";
		});
		*/
		$(".btn-primary").on("click", function(){
			self.location = "/admin/board/listAll?page=${cri.page}&perPageNum=${cri.perPageNum}"
					+ "&searchType=${cri.searchType}&keyword=${cri.keyword}";//검색기능 추가
		});
	});
	//첨부파일 버튼 선택으로 Ajax 사용시
	  var $fileupload = $('input[type="file"]');
	  $fileupload.each(function() {
	    var self = this;
	    var $dropfield = $('.fileDrop');
	    $(self).on("change", function(evt) {
	    	var files = $(self).prop("files");
			var file = files[0];
			console.log(file);
			var formData = new FormData();
			formData.append("file", file);	
			$.ajax({
				  url: '/uploadAjax',
				  data: formData,
				  dataType:'text',
				  processData: false,
				  contentType: false,
				  type: 'POST',
				  success: function(data){
					  var fileInfo = getFileInfo(data);
					  var html = template(fileInfo);
					  $(".uploadedList").append(html);
					  $(self).val('');
				  }
				});	
    	});
	 });

	//첨부파일 Ajax처리
	var template = Handlebars.compile($("#template").html());
	$(".fileDrop").on("dragenter dragover", function(event){
		event.preventDefault();
	});
	$(".fileDrop").on("drop", function(event){
		event.preventDefault();
		var files = event.originalEvent.dataTransfer.files;
		var file = files[0];
		//console.log(file);
		var formData = new FormData();
		formData.append("file", file);	
		$.ajax({
			  url: '/uploadAjax',
			  data: formData,
			  dataType:'text',
			  processData: false,
			  contentType: false,
			  type: 'POST',
			  success: function(data){
				  var fileInfo = getFileInfo(data);
				  var html = template(fileInfo);
				  $(".uploadedList").append(html);
			  }
			});	
	});
	$(".uploadedList").on("click", ".delbtn", function(event){
		event.preventDefault();
		var that = $(this);
		$.ajax({
		   url:"/deleteFile",
		   type:"post",
		   data: {fileName:$(this).attr("href")},
		   dataType:"text",
		   success:function(result){
			   if(result == 'deleted'){
				   that.closest("li").remove();
			   }
		   }
	   });
	});
	var bno = ${boardVO.bno};
	var template = Handlebars.compile($("#template").html());
	$.getJSON("/board/getAttach/"+bno,function(list){
		$(list).each(function(){
			var fileInfo = getFileInfo(this);
			var html = template(fileInfo);
			 $(".uploadedList").append(html);
		});
	});
	$(".uploadedList").on("click", ".mailbox-attachment-name", function(event){
		var fileLink = $(this).attr("href");
		if(checkImageType(fileLink)){
			event.preventDefault();
			var imgTag = $("#popup_img");
			imgTag.attr("src", fileLink);
			console.log('imgTag.attr = ' + imgTag.attr("src"));
			$(".popup").show('slow');
			imgTag.addClass("show");		
		}	
	});
	$("#popup_img").on("click", function(){
		$(".popup").hide('slow');
	});
</script>

<%@include file="../include/footer.jsp"%>