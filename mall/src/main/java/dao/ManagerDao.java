package dao;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vo.Manager;
import vo.ManagerPwHistory;

public class ManagerDao {
	
	public void managerRegister(Manager manager, ManagerPwHistory managerPwHistory) throws Exception{ 
		
		Class.forName("org.mariadb.jdbc.Driver");
		String url = "jdbc:mariadb://localhost:3306/mall";
		String dbuser = "root";
		String dbpw = "java1234";
		Connection conn = DriverManager.getConnection(url, dbuser, dbpw);
		conn.setAutoCommit(false);
		
		String sql1 = "INSERT INTO manager(manager_id, manager_pw, manager_name, createdate, updatedate, active) VALUES(?, password(?), ?, NOW(), NOW(), 'Y')";
		PreparedStatement stmt1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
		stmt1.setString(1, manager.getManagerId());
		stmt1.setString(2, manager.getManagerPw());
		stmt1.setString(3, manager.getManagerName());
		
		stmt1.executeUpdate();
		ResultSet rs1 = stmt1.getGeneratedKeys();
		
		int managerNo = 0;
		
		// AutoIncrement 값을 반환
		if(rs1.next()) {
			managerNo = rs1.getInt(1);
		} else {
			conn.rollback();
			return;
		}
				
		String sql2 = "INSERT INTO manager_pw_history(manager_no, manager_pw, createdate) VALUES(?, password(?), NOW() )";
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		stmt2.setInt(1, managerNo);
		stmt2.setString(2, manager.getManagerPw());
		
		int row2 = stmt2.executeUpdate();
		if(row2 != 1) {
			conn.rollback();
			return;
		}
		
		conn.commit();
		stmt1.close();
		rs1.close();
		stmt2.close();
	}
	
	public int managerIdCheck(String managerId) throws Exception {
		
		Class.forName("org.mariadb.jdbc.Driver");
		String url = "jdbc:mariadb://localhost:3306/mall";
		String dbuser = "root";
		String dbpw = "java1234";
		Connection conn = DriverManager.getConnection(url, dbuser, dbpw);
		
		String sql = "SELECT manager_id managerId FROM manager WHERE manager_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, managerId);
		ResultSet rs = stmt.executeQuery();
		
		int idCheck = 0;
		if(rs.next()) {
			idCheck = 0; // id가 이미 존재하는 경우. (생성 불가능)
		} else {
			idCheck = 1; // id가 존재하지 않는 경우. (생성 가능)
		}
		
		return idCheck;
		
	}
	
	public ResultSet managerLogin(Manager manager) throws Exception{
		
		Class.forName("org.mariadb.jdbc.Driver");
		String url = "jdbc:mariadb://localhost:3306/mall";
		String dbuser = "root";
		String dbpw = "java1234";
		Connection conn = DriverManager.getConnection(url, dbuser, dbpw);
		
		String sql = "SELECT manager_no managerNo FROM manager WHERE manager_id=? AND manager_pw = PASSWORD(?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, manager.getManagerId());
		stmt.setString(2, manager.getManagerPw());
		ResultSet rs = stmt.executeQuery();
		
		conn.close();
		stmt.close();
		rs.close();
		
		return rs;
	}
	
	public ArrayList<Manager> managerOne(int managerNo) throws Exception {
		
		Class.forName("org.mariadb.jdbc.Driver");
		String url = "jdbc:mariadb://localhost:3306/mall";
		String dbuser = "root";
		String dbpw = "java1234";
		Connection conn = DriverManager.getConnection(url, dbuser, dbpw);
		
		String sql = "SELECT manager_id managerId, manager_name managerName, createdate, updatedate FROM manager WHERE manager_no = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, managerNo);
		ResultSet rs = stmt.executeQuery();
		
		
		ArrayList<Manager> list = new ArrayList<>();
		if(rs.next()) {
			
			Manager m = new Manager();
			m.setManagerId(rs.getString("managerId"));
			m.setManagerName(rs.getString("managerName"));
			m.setCreatedate(rs.getString("createdate"));
			m.setUpdatedate(rs.getString("updatedate"));
			
			list.add(m);
		}
		
		return list;
	}
	
	public void updateManagerOne(int managerNo, String managerName) throws Exception {
		
		Class.forName("org.mariadb.jdbc.Driver");
		String url = "jdbc:mariadb://localhost:3306/mall";
		String dbuser = "root";
		String dbpw = "java1234";
		Connection conn = DriverManager.getConnection(url, dbuser, dbpw);
		
		String sql = "UPDATE manager SET manager_name = ?, updatedate = NOW() WHERE manager_no = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, managerName);
		stmt.setInt(2, managerNo);
		
		int row = stmt.executeUpdate();
		if(row != 1) {
			return;
		}
	}
	
	public void updateManagerPw(int managerNo, String oldPw, String newPw, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Class.forName("org.mariadb.jdbc.Driver");
		String url = "jdbc:mariadb://localhost:3306/mall";
		String dbuser = "root";
		String dbpw = "java1234";
		Connection conn = DriverManager.getConnection(url, dbuser, dbpw);
		conn.setAutoCommit(false);
		
		// 변경할 비밀번호가 이전에 사용했던 비밀번호와 다른지 검사
		// 같다면 비밀번호 변경 화면으로 redirect해서 메시지 출력
		String sql0 = "SELECT manager_pw managerPw FROM manager_pw_history WHERE manager_no = ? AND manager_pw = PASSWORD(?)";
		PreparedStatement stmt0 = conn.prepareStatement(sql0);
		stmt0.setInt(1, managerNo);
		stmt0.setString(2, newPw);
		ResultSet rs = stmt0.executeQuery();
		if(rs.next()) { // 변경할 비밀번호가 이전에 사용했던 비밀번호와 같다면
			conn.rollback();
			String msg = URLEncoder.encode("변경할 비밀번호가 이전의 비밀번호와 같습니다. 다른 비밀번호를 입력해주세요.");
			response.sendRedirect(request.getContextPath()+"/updateManagerPw.jsp?msg="+msg);
			return;
		}
		
			
		// 입력한 비밀번호가 원래 비밀번호와 일치하는지 대조하고 일치한다면 customer 테이블 데이터 수정(비밀번호 수정)
		String sql1 = "UPDATE manager SET manager_pw = password(?), updatedate = NOW() WHERE manager_no = ? AND manager_pw = password(?)";
		PreparedStatement stmt1 = conn.prepareStatement(sql1);
		stmt1.setString(1, newPw);
		stmt1.setInt(2, managerNo);
		stmt1.setString(3, oldPw);
	
		int row1 = stmt1.executeUpdate();
		
		if(row1 != 1) {
			conn.rollback();
			String msg = URLEncoder.encode("비밀번호를 확인하세요.");
			response.sendRedirect(request.getContextPath()+"/updateManagerPw.jsp?msg="+msg);
			return;
		}
		
		// 비밀번호를 수정하고 customer_pw_history 테이블에 비밀번호 변경 내역 추가
		String sql2 = "INSERT INTO manager_pw_history(manager_no, manager_pw, createdate) VALUES(?, PASSWORD(?), NOW())";
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		stmt2.setInt(1, managerNo);
		stmt2.setString(2, newPw);
		int row2 = stmt2.executeUpdate();
		
		if(row2 != 1) {
			conn.rollback();
			return;
		}
		
		conn.commit();
		
		response.sendRedirect(request.getContextPath()+"/managerOne.jsp");
	}
	
	public void withdrawalManager(int managerNo, String managerPw, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		Class.forName("org.mariadb.jdbc.Driver");
		String url = "jdbc:mariadb://localhost:3306/mall" ;
		String dbuser = "root";
		String dbpw = "java1234";
		Connection conn = DriverManager.getConnection(url, dbuser, dbpw);
		conn.setAutoCommit(false);
		
		String sql1 = "SELECT manager_no FROM manager WHERE manager_no = ? AND manager_pw = PASSWORD(?)";
		PreparedStatement stmt1 = conn.prepareStatement(sql1);
		stmt1.setInt(1, managerNo);
		stmt1.setString(2, managerPw);
		
		ResultSet rs = stmt1.executeQuery();
		if(!rs.next()) {
			conn.rollback();
			String msg = URLEncoder.encode("비밀번호를 확인하세요."); // 한글 깨짐 방지
			response.sendRedirect(request.getContextPath()+"/withdrawalManager.jsp?msg="+msg);
			return;
		}
		
		// 비밀번호가 일치하면 active를 'N'으로 변경
		String sql2 = "UPDATE manager SET active = 'N' WHERE manager_no = ?";
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		stmt2.setInt(1, managerNo);
		
		int row = stmt2.executeUpdate();
		if(row != 1) {
			conn.rollback();
			return;
		} 
		
		conn.commit();
		
		session.invalidate();
		response.sendRedirect(request.getContextPath()+"/managerLogin.jsp");
		
		conn.close();
		stmt1.close();
		rs.close();
		stmt2.close();
	}
 }
