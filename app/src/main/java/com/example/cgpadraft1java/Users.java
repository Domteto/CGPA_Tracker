package com.example.cgpadraft1java;

public class Users {
    String tid;
    String tname;
    String temail;
    String tpassword;

    //user constructor to assign name, email and password
    public Users(String name,String email, String password ){
        tname = name;
        temail = email;
        tpassword = password;
    }

    // constructor includes all attributes including id+ (+ID)
    public Users(String id, String name,String email, String password ){
        tid = id;
        tname = name;
        temail = email;
        tpassword = password;
    }

    // getters
    public String getId() {
        return tid;
    }
    public String getName() {
        return tname;
    }
    public String getEmail() {
        return temail;
    }
    public String getPassword() {
        return tpassword;
    }

    // Setters
    public void setId(String id) {
        tid = id;
    }
    public void setName(String name) {
        tname = name;
    }
    public void setEmail(String email) {
        temail = email;
    }
    public void setPassword(String password) {
        tpassword = password;
    }








}
