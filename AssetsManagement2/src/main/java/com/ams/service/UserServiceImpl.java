package com.ams.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.ams.model.User;
import com.ams.repo.Repo;
import com.ams.repo.UserRepo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private Repo repo = new Repo();

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public int registerUser(String firstName, String lastName, String userName, String password, int roleId) throws SQLException {
        if (repo.recordExists("user", "user_name", userName)) {
            throw new IllegalArgumentException("Username already exists");
        }

        LinkedHashMap<String, Object> loginMap = new LinkedHashMap<>();
        loginMap.put("user_name", userName);
        loginMap.put("password", password);
        loginMap.put("role_id", roleId);

        int userId = userRepo.insert("user", loginMap);

        LinkedHashMap<String, Object> empDataMap = new LinkedHashMap<>();
        empDataMap.put("first_name", firstName);
        empDataMap.put("last_name", lastName);
        empDataMap.put("user_id", userId);

        userRepo.insert("employees", empDataMap);

        return userId;
    }

    public User login(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    	
    	 String userName = request.getParameter("userName");
         String password = request.getParameter("password");
         
        User user = userRepo.findByUsername(userName);
       
        String role_name = userRepo.findRole(user.getRole_id());
        
       if ( user!= null && user.getPassword().equals(password)) {
        	 HttpSession session = request.getSession(true);
             session.setAttribute("name",userName);
             session.setAttribute("role",role_name);
             RequestDispatcher rd=request.getRequestDispatcher("/WelcomeServlet");  
//             try {
//				response.sendRedirect("index.jsp?username=\" + username");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
             try {
				rd.include(request, response);
				//rd.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            return user;
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

	@Override
	public void fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
		Part filePart = request.getPart("file");
		
		String fileName = getFileName(filePart);
	    InputStream fileContent = null;
		try {
			fileContent = filePart.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			Repo.fileUpload(fileName, fileContent);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
