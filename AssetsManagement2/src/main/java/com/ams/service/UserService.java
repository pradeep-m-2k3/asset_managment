package com.ams.service;

import java.io.IOException;
import java.sql.SQLException;

import com.ams.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    int registerUser(String firstName, String lastName, String userName, String password, int roleId) throws SQLException;
    User login(HttpServletRequest request, HttpServletResponse response) throws SQLException;
    void fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
    
}
