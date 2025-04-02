package com.example.app;

public class College {
    private String college_name;
    private String dte_code;
    private String Link;

    // Default constructor required for Firebase
    public College() {}

    public College(String college_name, String dte_code, String Link) {
        this.college_name = college_name;
        this.dte_code = dte_code;
        this.Link = Link;
    }

    public String getCollege_name() {
        return college_name;
    }

    public String getDte_code() {
        return dte_code;
    }

    public String getLink() {
        return Link;
    }
}
