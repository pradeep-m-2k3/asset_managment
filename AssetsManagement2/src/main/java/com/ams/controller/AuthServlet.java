package com.ams.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.ams.DTO.AssetsDTO;
import com.ams.model.User;
import com.ams.repo.UserRepo;
import com.ams.repo.UserRepoImpl;
import com.ams.service.AssetsService;
import com.ams.service.AssetsServiceImpl;
import com.ams.service.UserService;
import com.ams.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/api/*")

public class AuthServlet extends HttpServlet {
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
//    public AuthServlet() {
//        super();
//        userService = new UserService(); 
//    }
    private UserService userService;
    
    private AssetsService assetsService;
    
    public void init() throws ServletException {
        super.init();
        UserRepo userRepo = new UserRepoImpl();
        userService = new UserServiceImpl(userRepo);
        assetsService = new AssetsServiceImpl();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path.equals("/login")) {
            handleLogin(request, response);
        } else if (path.equals("/register")) {
            handleRegistration(request, response);
        }
            
            else if(path.equals("/logout")) {
            	handleLogout(request, response);
            }
         else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        
       // response.sendRedirect("https://www.google.com");  
       
        
        if(path.equals("/assets")) {
        	viewAssetsLimit(request,response);
        }
        else if(path.equals("/allassets")) {
        	allAssets(request,response);
        }
        else if(path.equals("/cookie")) {
        	cookie(request,response);
        }
        else if (path.equals("/getAttribute")) {  // New endpoint to set attribute
            getAttribute(request, response);
        }
    }
  
    private void cookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
    	Cookie userCookie = new Cookie("username", "hari");
    	 response.addCookie(userCookie);
    	 response.setContentType("text/html");
    	 response.getWriter().println("Cookie has been set!");
	}
	private void allAssets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve all assets from the service
            List<AssetsDTO> assets = assetsService.getAllAssets();
            
            // Set response content type
            response.setContentType("application/json");

            // Use ObjectMapper to write assets as JSON to the response writer
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), assets);
        } catch (SQLException e) {
            // Handle database error by throwing a ServletException
            throw new ServletException("Error retrieving assets from the database", e);
        }
    }

	private void viewAssetsLimit(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			assetsService.viewLimit(request,response);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
       

        try {
            User user = userService.login(request, response);
            
            response.getWriter().write("Login successful.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred during login");
        } catch (IllegalArgumentException e) {
            response.getWriter().write(e.getMessage());
        }
    }
	private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the session
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Logout successful\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"You are not logged in\"}");
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
       
        int roleId = Integer.parseInt(request.getParameter("roleId"));

      

        try {
            int userId = userService.registerUser(firstName, lastName, userName, password, roleId);
            response.getWriter().write("User registered with ID: " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred during registration");
        } catch (IllegalArgumentException e) {
            response.getWriter().write(e.getMessage());
        }
    }
   
    private void fileUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            userService.fileUpload(request, response);
            response.getWriter().write("File uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred during file upload.");
        }
    }
    
    private void getAttribute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext context = getServletContext();
        String dbUsername = (String) context.getAttribute("db_username");
        response.getWriter().println("DB User Name: " + dbUsername);
    }
//	

}
