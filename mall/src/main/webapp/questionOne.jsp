<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="vo.*" %>
<%@ page import="dao.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
  	<meta charset="UTF-8">
  	<meta name="viewport" content="width=device-width, initial-scale=1.0">
  	<meta http-equiv="X-UA-Compatible" content="ie=edge">
  	<title>문의사항 상세보기</title>	
  	<link rel="icon" href="img/Fevicon.png" type="image/png">
  	<link rel="stylesheet" href="vendors/bootstrap/bootstrap.min.css">
  	<link rel="stylesheet" href="vendors/fontawesome/css/all.min.css">	
  	<link rel="stylesheet" href="vendors/themify-icons/themify-icons.css">
	<link rel="stylesheet" href="vendors/linericon/style.css">
  	<link rel="stylesheet" href="vendors/owl-carousel/owl.theme.default.min.css">
  	<link rel="stylesheet" href="vendors/owl-carousel/owl.carousel.min.css">
  	<link rel="stylesheet" href="css/style.css">
  
  	<!--구글폰트 -->
  	<link rel="preconnect" href="https://fonts.googleapis.com">
  	<link rel="preconnect" href="https://fonts.gstatic.com" >
  	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Gowun+Dodum&display=swap" >
  	<link rel="stylesheet" href="css/font.css">
  
  
  	<!-- BootStrap5 -->
	<!-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script> -->	
</head>
<body>
  <!--================ Start Header Menu Area ===============-->
   <%
  	if(session.getAttribute("customerNo") != null) {
  %>
  		<jsp:include page="/inc/customerLoginMenu.jsp"></jsp:include>
  <% 	
  	} else {
  %>
  		<jsp:include page="/inc/customerLogoutMenu.jsp"></jsp:include>
  <% 	
  	}
  %>
  <!--================ End Header Menu Area =================-->
  <% 
	
	// contact.jsp에서 넘어온 파라미터
  	int questionNo = Integer.parseInt(request.getParameter("questionNo"));
  	System.out.println(questionNo + "<-- questionNo");	// questionNo 디버깅
	
	// moder 호출 코드(controller code)
	QuestionDao qDao = new QuestionDao();
	ArrayList<HashMap<String, Object>> list = qDao.selectQuestionOne(questionNo);
	
	QuestionCommentDao qcDao = new QuestionCommentDao();
	int comment = qcDao.selectQuestionComment(questionNo);
	
	//end controller code
%>
	<div class="container">
	<table class="table">
<%
	for(HashMap<String, Object> qOne : list){
%>
		<!--<tr><th>번호</th><td><%=qOne.get("questionNo") %></td></tr>-->
		<tr><th>제목</th><td><%=qOne.get("questionTitle") %></td></tr>
		<tr><th>작성자</th><td><%=qOne.get("customerId") %></td></tr>
		<tr><th>작성일</th><td><%=qOne.get("createdate") %></td></tr>
		<tr><th>상품명</th><td><%=qOne.get("goodsTitle") %></td></tr>
		<tr><th>내용</th><td><%=qOne.get("questionContent") %></td></tr>	
<%
		}
%>
	</table>
<%
	if((int)session.getAttribute("customerNo") == qDao.askCustomerNo(questionNo)) {
%>
	<a href="<%=request.getContextPath() %>/updateQuestionForm.jsp?questionNo=<%=questionNo%>" class="btn btn-primary">수정</a>
	<a href="<%=request.getContextPath() %>/deleteQuestionAction.jsp?questionNo=<%=questionNo%>" class="btn btn-primary">삭제</a>
<%
	}
%>	
	</div>
	
	<div class="container">
<%

%>	
	<h3>답변</h3>
	<table class="table">
<%
	if(comment == 1){
		//답변 내용이 있다면 comment == 1, 없다면 comment == 0
		ArrayList<HashMap<String, Object>> list2 = qcDao.selectQuestionCommentOne(questionNo);
		

		for(HashMap<String, Object> qcOne : list2){
%>
			<tr><th>담당자</th><td><%=qcOne.get("managerName") %></td></tr>
			<tr><th>답변</th><td><%=qcOne.get("commentContent") %></td></tr>
			<tr><th>작성일</th><td><%=qcOne.get("createdate") %></td></tr>
<%
		}
		}else if(comment == 0){
%>
			
			<tr><th>담당자</th><td></td></tr>
			<tr><th>답변</th><td>매니져 답변 대기중</td></tr>
			<tr><th>작성일</th><td></td></tr>
<% 
		}
%>

	</table>
	</div>
	
	
	
	
	<div class="container">
<% 
	//로그인이 되어 있다면
	if(session.getAttribute("customerNo") != null) {
	// 로그인 한 customerNo 와 문의사항 작성자의 customerNo가 같으면 수정, 삭제 가능
	System.out.println((int)session.getAttribute("customerNo") + "<-- 로그인된 고객번호");
	System.out.println(qDao.askCustomerNo(questionNo) + "<-- 작성자 고객번호");
	}	
%>

	</div>

  <script src="vendors/jquery/jquery-3.2.1.min.js"></script>
  <script src="vendors/bootstrap/bootstrap.bundle.min.js"></script>
  <script src="vendors/skrollr.min.js"></script>
  <script src="vendors/owl-carousel/owl.carousel.min.js"></script>
  <script src="vendors/nice-select/jquery.nice-select.min.js"></script>
  <script src="vendors/jquery.form.js"></script>
  <script src="vendors/jquery.validate.min.js"></script>
  <script src="vendors/contact.js"></script>
  <script src="vendors/jquery.ajaxchimp.min.js"></script>
  <script src="vendors/mail-script.js"></script>
  <script src="js/main.js"></script>
</body>
</html>
