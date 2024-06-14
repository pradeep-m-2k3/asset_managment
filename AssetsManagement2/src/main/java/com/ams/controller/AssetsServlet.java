package com.ams.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.ams.repo.UserRepo;
import com.ams.repo.UserRepoImpl;
import com.ams.service.AssetsService;
import com.ams.service.AssetsServiceImpl;
import com.ams.service.UserService;
import com.ams.service.UserServiceImpl;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class PermissionServlet
 */	

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 10,  // 10 MB
	    maxFileSize = 1024 * 1024 * 50,        // 50 MB
	    maxRequestSize = 1024 * 1024 * 100     // 100 MB
	)
public class AssetsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
	 private UserService userService;
	    
	    private AssetsService assetsService;
	    
	    
	    public void init() throws ServletException {
	        super.init();
	   
	        UserRepo userRepo = new UserRepoImpl();
	        userService = new UserServiceImpl(userRepo);
	        assetsService = new AssetsServiceImpl();
	    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getPathInfo();
		
		   response.setContentType("text/html");  
		    PrintWriter out = response.getWriter();  
		      
		    ServletConfig config=getServletConfig();  
		    String servletName=config.getServletName();
		    String name = config.getInitParameter("projectName");
		    out.println("servletName is: "+servletName);  	
		    out.println("projectName is: "+name);  
		    
		    ServletContext context=getServletContext();  
		    String contextDbnameName=context.getInitParameter("Dbname");
		    out.println(" DB Name  : "+contextDbnameName); 
		    
		    if(path.equals("/warrenty")) {
		    	assetsService.warrentyFile(request,response);
		    }
		    
		   
	}

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		
		 HttpSession returnSession =request.getSession(false);
		 Object roleName = returnSession.getAttribute("role");
		 response.getWriter().println(roleName);
         System.out.println(roleName);
         
         
         //setAttribute
         ServletContext context = getServletContext();
         context.setAttribute("db_username", "root");
         
		if(roleName.equals("admin")) {
			//response.sendRedirect(request.getContextPath() + "/addAssets");
			addAssets(request, response);
        }
		else {
			response.getWriter().println("You are not allowed!");
		}
	}
	
	
	 private void addAssets(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 System.out.println("Asset added");
		 response.getWriter().println("assets add");
	    	try {
				assetsService.addAssets(request, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  	}

}
