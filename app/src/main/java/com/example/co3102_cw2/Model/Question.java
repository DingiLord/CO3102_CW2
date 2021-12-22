package com.example.co3102_cw2.Model;

import java.util.ArrayList;

public class Question {
    private int id;
    private String question;
    private ArrayList<Option> options = new ArrayList<>();
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
