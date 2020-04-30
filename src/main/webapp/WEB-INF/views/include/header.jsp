<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<title> Edu실습테스트사이트 </title>
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/resources/home/css/swiper.min.css">
<link rel="stylesheet" href="/resources/home/css/common.css">
<script src="/resources/home/js/jquery-1.11.3.min.js"></script>
<script src="/resources/home/js/rollmain.js"></script>
<script src="/resources/home/js/jquery.easing.js"></script>	
<script src="/resources/home/js/common.js"></script>  
<script src="/resources/home/js/jquery.smooth-scroll.min.js"></script> 
<!--[if lte IE 9]>
    <script src="/resources/home/js/html5shiv.js"></script>
	<script src="/resources/home/js/placeholders.min.js"></script>
<![endif]-->
</head>
<body>
<ul class="skipnavi">
    <li><a href="#container">본문내용</a></li>
</ul>
<!-- wrap -->
<div id="wrap">

	<header id="header">
		<div class="header_area box_inner clear">	
			<h1><a href="/">스프링 in EDU</a></h1>
			<p class="openMOgnb"><a href="#"><b class="hdd">메뉴열기</b> <span></span><span></span><span></span></a></p>
			<!-- header_cont -->
			<div class="header_cont">
				<ul class="util clear">
				<c:choose>
				    <c:when test="${session_enabled eq 'true'}">
				        <li><span style="color:#fff">${session_username}님 환영합니다!</span></li>
				        <li><a href="/logout">로그아웃</a></li>
				        <c:if test="${session_levels eq 'ROLE_ADMIN' }">
				            <li><a href="/admin/board/listAll">관리자</a></li>
				        </c:if>	
				    </c:when>
				    <c:otherwise>
				        <li><a href="/login">로그인</a></li>
				        <li><a href="javascript:;">회원가입</a></li>
				    </c:otherwise>
				</c:choose>
				</ul>	
				<nav>
				<ul class="gnb clear">
					<li><a href="/board/listAll" class="openAll2">고객센터</a>
				        <div class="gnb_depth gnb_depth2_2">
                            <ul class="submenu_list">
                                <li><a href="/board/listAll">공지사항</a></li>
                            </ul>
                        </div>
					</li>
				</ul>
                </nav>
				<p class="closePop"><a href="javascript:;">닫기</a></p>
			</div>
			<!-- //header_cont -->
		</div>
	</header>