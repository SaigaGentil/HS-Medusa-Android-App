package com.example.login_register_me1;

import java.util.Date;

public class User {
    String username;
    String lastname;
    String firstname;
    Date sessionExpiryDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public void setFirstname(String firstName) { this.firstname = firstName; }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname(){
        return firstname;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
