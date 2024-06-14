package com.ams.repo;

import java.sql.SQLException;
import java.util.Map;

import com.ams.model.User;

public interface UserRepo {
    
    int insert(String tableName, Map<String, Object> columnValue) throws SQLException;
    User findByUsername(String userName) throws SQLException;
    String findRole(int role_id) throws SQLException;
}
