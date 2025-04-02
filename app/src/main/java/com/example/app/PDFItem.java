package com.example.app;

class PDFItem {
    private String name;
    private String url;

    public PDFItem() {
        // Default constructor required for Firebase
    }

    public PDFItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}