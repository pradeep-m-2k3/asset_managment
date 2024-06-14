package com.ams.repo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;


public class GetRoleDetail {
	
	public static void permissionDetail(String role) throws SQLException {
		
		LinkedList<Integer> lk = new LinkedList<>();
		
		String permissionQuery = "SELECT DISTINCT roles.role_name, permissions.permission_id, permissions.permission_name "
                + "FROM role_permissions "
                + "JOIN roles ON role_permissions.role_id = roles.role_id "
                + "JOIN permissions ON role_permissions.permission_id = permissions.permission_id "
                + "ORDER BY permissions.permission_id";

		String Query = null;
		Connection con = Repo.getConnection();
		PreparedStatement presmt = con.prepareStatement(permissionQuery);
		ResultSet rs = presmt.executeQuery();
		int i = 1;
		while(rs.next()) {
			String currentRoleName = rs.getString("role_name");
			int currentId = rs.getInt("permission_id");
			
			if(currentRoleName.equals(role)) {
				lk.add(currentId);
			 System.out.println(rs.getString("permission_name")+"--->"+currentId);
			 i++;
			
			}
			
			
			//System.out.println(rs.getString("permission_name"));
		}
		//System.out.println("LIST :"+lk);
		System.out.println("|-------------------------------------------------------------|");
		AllowAccess.allowTheirOwnAccess(lk);
		
	}
 public static void roledetail(int role_id) throws SQLException {
	 
	 Connection con =Repo.getConnection();
		String query = null;
		String role = null;
		if(con != null) {
			query = "SELECT role_name,role_id FROM roles where role_id = ?";
			PreparedStatement presmt = con.prepareStatement(query);
			presmt.setInt(1,role_id);
			ResultSet rs = presmt.executeQuery();
			if(rs.next()) {
				 role = rs.getString("role_name");
				 System.out.println("|-------------------------------------------------------------|");
				 System.out.println("You logged as :"+ role);
				// permissionDetail(role);
			}
			
			
		}
 }
}
