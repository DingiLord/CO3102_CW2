package com.example.co3102_cw2.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    String email;
    String fullName;
    String dob;
    String homeAdress;
    String sni;
    List<String> answeredQuestions = new ArrayList<>();

    public User(){

    }

    public User(String email, String fullName, String dob, String homeAdress, String sni){
        this.email = email;
        this.fullName = fullName;
        this.dob = dob;
        this.homeAdress = homeAdress;
        this.sni = sni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHomeAdress() {
        return homeAdress;
    }

    public void setHomeAdress(String homeAdress) {
        this.homeAdress = homeAdress;
    }

    public String getSni() {
        return sni;
    }

    public void setSni(String sni) {
        this.sni = sni;
    }

    public List<String> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(List<String> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }
}
