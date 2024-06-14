package com.ams.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.ams.DTO.AssetsDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AssetsService {
	void addAssets (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException;

	void viewLimit(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;
	List<AssetsDTO> getAllAssets() throws SQLException;
	void warrentyFile (HttpServletRequest request, HttpServletResponse response) throws IOException;
	
}
