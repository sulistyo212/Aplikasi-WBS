package com.example.myapplication;

public class User {
    private int id;
    private String  email, namapelapor, textView, status_laporan;

    public User(int id, String textView, String status_laporan, String email, String namapelapor) {
        this.id = id;
        this.textView = textView;
        this.status_laporan = status_laporan;
        this.email = email;
        this.namapelapor = namapelapor;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNamapelapor() {
        return namapelapor;
    }

    public String getTextView() {
        return textView;
    }

    public String getStatus_laporan() {
        return status_laporan;
    }

}
