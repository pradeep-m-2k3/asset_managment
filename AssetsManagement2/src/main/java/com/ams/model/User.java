package com.ams.model;


public class User {
    private int user_id;
    private String username;
    private String password;
    private int role_id;

    // Default constructor
    public User() {
    }

    public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	// Parameterized constructor
    public User( String username, String password) {
        
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
  
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   
}
