package com.ams.model;

public class Register {
	private String user_id;
    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private int role_id;

    // Default constructor
    public Register() {
    }

    // Parameterized constructor
    public Register(String user_id, String first_name, String last_name, String username, String password, int role_id) {
    	this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.role_id = role_id;
    }

    // Getters and Setters
    
    public String getFirst_name() {
        return first_name;
    }

    public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

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

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

}
