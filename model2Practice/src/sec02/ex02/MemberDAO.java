package sec02.ex02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	private DataSource datafactory;
	private Connection con;
	private PreparedStatement pstmt;
	
	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:/comp/env");
			datafactory = (DataSource) envCtx.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<MemberVO> listmembers(){
		List<MemberVO> membersList = new ArrayList<MemberVO>();
		
		try {
			con = datafactory.getConnection();
			String SQL = "SELECT * FROM t_member order by joinDate desc";
			System.out.println("SQL: " + SQL);
			pstmt = con.prepareStatement(SQL);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				
				MemberVO member = new MemberVO(id, pwd, name, email, joinDate);
				membersList.add(member);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return membersList;
	}
	
	public void addmember(MemberVO member) {
		try {
			con = datafactory.getConnection();
			String SQL = "INSERT INTO t_member VALUES(?, ?, ?, ?, sysdate)";
			pstmt = con.prepareStatement(SQL);
			
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPwd());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MemberVO findmember(String _id) {
		MemberVO member = null;
		try {
			con = datafactory.getConnection();
			String SQL = "SELECT * FROM t_member where id=?";
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, _id);
			System.out.println(SQL);
			
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			String id = rs.getString("id");
			String pwd = rs.getString("pwd");
			String name = rs.getString("name");
			String email = rs.getString("email");
			Date joinDate = rs.getDate("joinDate");
			
			member = new MemberVO(id, pwd, name, email, joinDate);
			pstmt.close();
			rs.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return member;
	}
	
	public void modmember(MemberVO member) {
		String id = member.getId();
		String pwd = member.getPwd();
		String name = member.getName();
		String email = member.getEmail();
		
		try {
			con = datafactory.getConnection();
			String SQL = "UPDATE t_member SET pwd=?, name=?, email=? WHERE id=?";
			System.out.println(SQL);
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, pwd);
			pstmt.setString(2, name);
			pstmt.setString(3, email);
			pstmt.setString(4, id);
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delmember(String id) {
		try {
			con = datafactory.getConnection();
			String SQL = "DELETE FROM t_member WHERE id=?";
			System.out.println(SQL);
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
