package com.example.myapplication;

public class Articles {
    String title, description, urlToimage, publishedAt;

    public Articles() {
    }

    @Override
    public String toString() {
        return "Articles{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", urlToimage='" + urlToimage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }
}
