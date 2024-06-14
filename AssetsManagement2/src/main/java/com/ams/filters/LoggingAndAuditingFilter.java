package com.ams.filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.ams.repo.Repo;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class LoggingAndAuditingFilter implements Filter {

    

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Capture request details
        String method = httpRequest.getMethod();
        String requestURI = httpRequest.getRequestURI();
        String clientIP = httpRequest.getRemoteAddr();
        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        String username = httpRequest.getParameter("username");

        // Proceed with the next filter or the target servlet
       chain.doFilter(request, response);

        // Capture response details
        int statusCode = httpResponse.getStatus();
        Timestamp responseTime = new Timestamp(System.currentTimeMillis());

        // Log details to the database
        try {
        	Repo.saveLoginLog(requestTime, clientIP, method, requestURI, responseTime, statusCode);
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException("Database error", ex);
        }
    }

   
    

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}