<%@page import="dao.GoodsDao"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%

	int goodsNo = Integer.parseInt(request.getParameter("goodsNo"));
	
	GoodsDao goodsDao = new GoodsDao();
	String filename = goodsDao.deleteGoods(goodsNo, request, response);
	
	// 파일삭제
	// String path = request.getServletContext().getRealPath("/upload");
	String path = "/Users/jkh/Desktop/DB/mall-gitRepository/mall/mall/src/main/webapp/upload";
	File f = new File(path+"/"+filename);
	if(f.exists()) {
		f.delete();
	}
	
	response.sendRedirect(request.getContextPath()+"/managerGoodsList.jsp");

%>