package com.ams.repo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.ams.model.User;

public class UserRepoImpl implements UserRepo {
    

    @Override
    public int insert(String tableName, Map<String, Object> columnValue) throws SQLException {
        Connection con = Repo.getConnection();
        int generatedKey = -1;
        if (con != null) {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Map.Entry<String, Object> entry : columnValue.entrySet()) {
                if (columns.length() > 0) {
                    columns.append(",");
                    values.append(",");
                }
                columns.append(entry.getKey());
                values.append("?");
            }

            String insertQuery = "INSERT INTO " + tableName + "(" + columns + ") VALUES (" + values + ")";

            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            int index = 1;
            for (Object value : columnValue.values()) {
                ps.setObject(index++, value);
            }

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getInt(1);
                }
            }

            ps.close();
        }

        return generatedKey;
    }
    @Override
    public User findByUsername(String userName) throws SQLException {
        String query = "SELECT * FROM user WHERE user_name = ?";
        try (Connection con = Repo.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));
                    user.setRole_id(rs.getInt("role_id"));
                    return user;
                } else {
                    throw new IllegalArgumentException("username not found");
                }
            }
        }
    }

   
   
	@Override
	public String findRole(int role_id) throws SQLException {
		Connection con = Repo.getConnection();
        String query = "SELECT role_name FROM roles WHERE role_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,role_id);
        ResultSet rs = ps.executeQuery();
        String role_name = null;
        if(rs.next()) {
         role_name =  rs.getString("role_name");
        	
        }
        return role_name;
       
      
	
	}

   
}
