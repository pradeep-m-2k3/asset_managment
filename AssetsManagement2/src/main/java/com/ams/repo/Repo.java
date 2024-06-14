package com.ams.repo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ams.DTO.AssetsDTO;
import com.ams.model.User;

import jakarta.persistence.EntityManager;

public class Repo {
	
	private static final String JDBC_URL = DBConfig.getProperty("db.url");
	private static final String JDBC_USER = DBConfig.getProperty("db.username");
	private static final String JDBC_PASSWORD = DBConfig.getProperty("db.password");
	private static final String JDBC_DRIVER = DBConfig.getProperty("db.driverClassName");
	static {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	
	private EntityManager entityManager;
	private User user = new User();

	
	public User findByUsername(String userName) throws SQLException {
		Connection con =getConnection();
		String query = "SELECT * FROM user where user_name = ?";
		PreparedStatement presmt = con.prepareStatement(query);
		presmt.setString(1, userName);
		ResultSet rs = presmt.executeQuery();
		if(rs.next())
		{ 
			user.setUser_id(rs.getInt("user_id"));
			user.setUsername(rs.getString("user_name"));
			user.setPassword(rs.getString("password"));
			user.setRole_id(rs.getInt("role_id"));
			
		 }
		else 
		{
			
		}
		return user;

	
	}
public static void permissionDetail(String role) throws SQLException {
		
		LinkedList<Integer> lk = new LinkedList<>();
		
		String permissionQuery = "SELECT DISTINCT roles.role_name, permissions.permission_id, permissions.permission_name "
                + "FROM role_permissions "
                + "JOIN roles ON role_permissions.role_id = roles.role_id "
                + "JOIN permissions ON role_permissions.permission_id = permissions.permission_id "
                + "ORDER BY permissions.permission_id";

		String Query = null;
		Connection con = getConnection();
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
	 
	 Connection con = getConnection();
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
				 permissionDetail(role);
			}
			
			
		}
 }

 public static ResultSet select(String tbName) throws SQLException {
		String query = null;
		Connection con = getConnection();
		Statement smt = con.createStatement();
		query = " SELECT * FROM "+tbName;
		ResultSet rs = smt.executeQuery(query);
		 int columnCount = rs.getMetaData().getColumnCount();
		 System.out.println("|----------------------------------|");
			
		 while (rs.next()) {
			
			    for (int i = 1; i <= columnCount; i++) {
			        Object value = rs.getObject(i);
			       
			        System.out.print(rs.getMetaData().getColumnName(i) + " : " + value );
			       System.out.println();
			    }
			    System.out.println("|----------------------------------|");
			}
		 return rs;
	}
 public static void saveLoginLog(Timestamp requestTime, String clientIP, String method, String requestURI,
         Timestamp responseTime, int statusCode)
throws SQLException, ClassNotFoundException {

Connection con =getConnection();

PreparedStatement statement = con.prepareStatement(
"INSERT INTO api_logs (request_time, client_ip, http_method, request_uri, response_time, status_code) " +
"VALUES (?, ?, ?, ?, ?, ?)"); 

statement.setTimestamp(1, requestTime);
statement.setString(2, clientIP);
statement.setString(3, method);
statement.setString(4, requestURI);
statement.setTimestamp(5, responseTime);
statement.setInt(6, statusCode);

statement.executeUpdate();
}
 public static void fileUpload(String fileName, InputStream fileContent) throws SQLException {
	    Connection con = null;
	    PreparedStatement statement = null;
	    
	    try {
	        con = getConnection();
	        String sql = "INSERT INTO files (name, content) VALUES (?, ?)";
	        statement = con.prepareStatement(sql);
	        statement.setString(1, fileName);
	        statement.setBlob(2, fileContent);
	        
	        int rowsAffected = statement.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            System.out.println("File uploaded successfully.");
	        } else {
	            System.out.println("Failed to upload file.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; // Re-throw the exception to the caller for handling
	    } finally {
	        // Close resources in a finally block to ensure they are always closed
	        if (statement != null) {
	            statement.close();
	        }
	        if (con != null) {
	            con.close();
	        }
	    }
	}
 public int insert(String tableName, Map<String, Object> columnValue) throws SQLException {
     Connection con = getConnection();
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
 public boolean recordExists(String tableName, String columnName, String value) throws SQLException {
     Connection con = Repo.getConnection();
     String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
     PreparedStatement ps = con.prepareStatement(query);
     ps.setString(1, value);
     ResultSet rs = ps.executeQuery();
     rs.next();
     boolean exists = rs.getInt(1) > 0;
     rs.close();
     ps.close();
     return exists;
 }
 public List<AssetsDTO> findAll() throws SQLException {
     List<AssetsDTO> assets = new ArrayList<>();
     String sql = "SELECT * FROM assets";
     
     try (Connection con = getConnection(); 
          PreparedStatement stmt = con.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
             AssetsDTO asset = new AssetsDTO();
             asset.setName(rs.getString("name"));
             asset.setCategoryId(rs.getInt("category_id"));
             asset.setStatusId(rs.getInt("status_id"));
             asset.setLocationId(rs.getInt("location_id"));
             asset.setPurchaseDate(rs.getDate("purchase_date"));
             asset.setPrice(rs.getDouble("price"));
             asset.setNotes(rs.getString("notes"));
             asset.setSerialNumber(rs.getString("serial_number"));
             asset.setWarranty(rs.getString("warranty"));
             assets.add(asset);
         }
     }
     
     return assets;
     
    
 }
 
 
 public InputStream warrentyFileDownload(int assetId) {
     Connection connection = null;
     PreparedStatement statement = null;
     ResultSet resultSet = null;
     InputStream inputStream = null;

     try {
         // Get database connection
         connection =getConnection();

         // Prepare SQL statement to retrieve file data based on asset ID
         String sql = "SELECT warranty FROM assets WHERE asset_id = ?";
         statement = connection.prepareStatement(sql);
         statement.setInt(1, assetId);

         // Execute the query
         resultSet = statement.executeQuery();

         // Check if the result set has data
         if (resultSet.next()) {
             // Get the binary stream from the result set
             inputStream = resultSet.getBinaryStream("warranty");
         }
     } catch (SQLException e) {
         e.printStackTrace();
     } finally {
         // Close resources properly in the finally block
         try {
             if (resultSet != null) resultSet.close();
             if (statement != null) statement.close();
             if (connection != null) connection.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     return inputStream;
 }
// public static void fileUpload(String fileName,InputStream fileContent ) throws SQLException {
//	 Connection con = getConnection();
//	 String sql = "INSERT INTO files (name, content) VALUES (?, ?)";
//	 PreparedStatement statement = con.prepareStatement(sql);
//	 statement.setString(1, fileName);
//     statement.setBlob(2, fileContent);
//     
//    statement.executeQuery();
//        
// }
	
}

/*
 * public static void checkLogin() throws SQLException { Scanner sc = new
 * Scanner(System.in); String query = null; Connection con =getConnection();
 * 
 * if(con != null) {
 * 
 * query = "SELECT * FROM user where user_name = ?"; PreparedStatement presmt =
 * con.prepareStatement(query); presmt.setString(1, userName); ResultSet rs =
 * presmt.executeQuery(); if(rs.next()) { String storedPassword =
 * rs.getString("password"); int roleId = rs.getInt("role_id");
 * if(storedPassword.equals(password)) { GetRoleDetail.roledetail(roleId);
 * 
 * } else { System.out.println("Password incorrect!!!"); } } else {
 * System.out.println("Invalid User...!"); }
 * 
 * 
 * }
 * 
 * } }
 */