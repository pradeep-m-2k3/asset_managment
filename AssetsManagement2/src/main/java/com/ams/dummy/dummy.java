package com.ams.dummy;

public class dummy {

}
//protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	 String userName = request.getParameter("userName");
//       String password = request.getParameter("password");
//
//       try {
//           User user = userService.login(userName, password);
//           response.getWriter().write("Login successful. Welcome, " + user.getUsername() + "!");
//       } catch (SQLException e) {
//           e.printStackTrace();
//           response.getWriter().write("An error occurred during login");
//       } catch (IllegalArgumentException e) {
//           response.getWriter().write(e.getMessage());
//       }
//	
//}
//
///**
//* @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//*/
//protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//   String firstName = request.getParameter("firstName");
//   String lastName = request.getParameter("lastName");
//   String userName = request.getParameter("userName");
//   String password = request.getParameter("password");
//   String rePassword = request.getParameter("rePassword");
//   int roleId = Integer.parseInt(request.getParameter("roleId"));
//
//   if (!password.equals(rePassword)) {
//       response.getWriter().write("Password and Re-Password must be the same");
//       return;
//   }
//
//   try {
//       int userId = userService.registerUser(firstName, lastName, userName, password, roleId);
//       response.getWriter().write("User registered with ID: " + userId);
//   } catch (SQLException e) {
//       e.printStackTrace();
//       response.getWriter().write("An error occurred during registration");
//   } catch (IllegalArgumentException e) {
//       response.getWriter().write(e.getMessage());
//   }
//}