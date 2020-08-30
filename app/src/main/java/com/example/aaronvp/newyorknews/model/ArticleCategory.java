package com.example.aaronvp.newyorknews.model;

import java.util.List;

import lombok.Data;

@Data
public class ArticleCategory {

    public List<Article> articles;

    private String category;

    public ArticleCategory(String category, List<Article> articles) {
        this.articles = articles;
        this.category = category;
    }
}
