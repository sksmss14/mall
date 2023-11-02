<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<header class="header_area">
    <div class="main_menu">
      <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container">
          <a class="navbar-brand logo_h" href="<%=request.getContextPath()%>/adminLogin.jsp">관리자 페이지</a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <div class="collapse navbar-collapse offset" id="navbarSupportedContent">
            <ul class="nav navbar-nav menu_nav ml-auto mr-auto">
           
              	<li class="nav-item submenu dropdown">  
					<a href="<%=request.getContextPath()%>/customerList.jsp" class="nav-link" role="button" aria-haspopup="true"
	                  aria-expanded="false">고객 list</a>        
				</li>
				
            </ul>

            <ul>
            	<% 
            	if(session.getAttribute("adminId") != null) {		
            	%>
            		<li class="nav-item active"><a class="nav-link" href="<%=request.getContextPath()%>/adminLogoutAction.jsp">로그아웃</a></li>
            	<% 	
            	} else {
            	%>
            		<li class="nav-item active"><a class="nav-link" href="<%=request.getContextPath()%>/adminLogin.jsp">관리자 로그인</a></li>
            	<% 	
            	}
            	%>       	
            </ul>
          
          </div>
        </div>
      </nav>
    </div>
  </header>