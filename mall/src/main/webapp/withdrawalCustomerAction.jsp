<%@page import="dao.CustomerDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int customerNo = (Integer) session.getAttribute("customerNo");
	String customerPw = request.getParameter("customerPw");

	CustomerDao customerDao = new CustomerDao();
	int result = customerDao.withdrawalCustomer(customerNo, customerPw, session);
	
	out.write(result + "");
%>
