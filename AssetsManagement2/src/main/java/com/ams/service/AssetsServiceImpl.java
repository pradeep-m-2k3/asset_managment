package com.ams.service;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.ams.DTO.AssetsDTO;
import com.ams.repo.Repo;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class AssetsServiceImpl implements  AssetsService{
	
	 private Repo repo = new Repo();
	@Override
	public void addAssets(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		  LinkedHashMap<String, Object> assetData = new LinkedHashMap<>();
		 
			  if (repo.recordExists("assets", "serial_number", request.getParameter("serial_number"))) {
				  response.getWriter().write("serial_number already exists");
				  System.out.println("serial_number already exists");

				 
		        }
		 
		  
		  
	        
	        // Retrieve form fields and store in LinkedHashMap
	        assetData.put("name", request.getParameter("name"));
	        assetData.put("category_id", request.getParameter("category_id"));
	        assetData.put("status_id", request.getParameter("status_id"));
	        assetData.put("location_id", request.getParameter("location_id"));
	        
	        assetData.put("purchase_date", request.getParameter("purchase_date"));
	        assetData.put("price", request.getParameter("price"));
	        assetData.put("notes", request.getParameter("notes"));
	        assetData.put("serial_number", request.getParameter("serial_number"));
	        
	        
	        //file upload using part
	        
//	     
//	        Part filePart = request.getPart("warrenty");
//			InputStream fileContent = null;
//			try {
//				fileContent = filePart.getInputStream();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//          assetData.put("warranty", fileContent);
	        
	        InputStream fileContent = request.getInputStream();
	        assetData.put("warranty", fileContent);
		    int inserted = repo.insert("assets", assetData);
		    
		    // Send response based on insertion result
		    if (inserted > 0) {
		        response.getWriter().write("Data inserted successfully.");
		    } else {
		        response.getWriter().write("Failed to insert data.");
		    }
}

	  public void viewLimit(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
	        int page = 1;
	        if (request.getParameter("page") != null) {
	            page = Integer.parseInt(request.getParameter("page"));
	        }
	        int itemsPerPage = 3;
	        int offset = (page - 1) * itemsPerPage;

	        Connection con = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        try {
	            con = repo.getConnection();
	            String sql = "SELECT * FROM assets LIMIT ? OFFSET ?";
	            stmt = con.prepareStatement(sql);
	            stmt.setInt(1, itemsPerPage);
	            stmt.setInt(2, offset);
	            rs = stmt.executeQuery();

	            StringBuilder json = new StringBuilder();
	            json.append("{\"assets\": [");
	           
	            while (rs.next()) {
	                json.append("{\"AsstesName\": \"" + rs.getString("name") + "\"},");
	            }

	            if (json.charAt(json.length() - 1) == ',') {
	                json.deleteCharAt(json.length() - 1); // Remove the last comma
	            }

	            json.append("]}");
	            response.setContentType("application/json");
	            response.getWriter().write(json.toString());
	        } finally {
	            if (rs != null) {
	                rs.close();
	            }
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (con != null) {
	                con.close();
	            }
	        }
	  }

	  public List<AssetsDTO> getAllAssets() throws SQLException {
	        return repo.findAll();
	    }

	@Override
	public void warrentyFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String assetStr = request.getParameter("asset_id");
		int asset_id = Integer.parseInt(assetStr);
		InputStream inputStream = repo.warrentyFileDownload(asset_id);

		if (inputStream != null) {
		    try (ServletOutputStream outputStream = response.getOutputStream()) {
		        // Set response headers for file download
		        response.setContentType("application/octet-stream");
		        response.setHeader("Content-Disposition", "attachment; filename=\"warranty_file.ext\"");

		        // Copy file data from InputStream to ServletOutputStream
		        int bytesRead;
		        byte[] buffer = new byte[4096];
		        while ((bytesRead = inputStream.read(buffer)) != -1) {
		            outputStream.write(buffer, 0, bytesRead);
		        }
		    } catch (IOException e) {
		        e.printStackTrace(); // Handle IO error, if necessary
		    } finally {
		        try {
		            inputStream.close(); // Close the InputStream
		        } catch (IOException e) {
		            e.printStackTrace(); // Handle IO error, if necessary
		        }
		    }
		} else {
		    response.getWriter().write("Warranty file not found.");
		}

	}
}
