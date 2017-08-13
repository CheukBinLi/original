package com.cheuks.bin.original.weixin.mp.model.customservice.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class NewsMessage extends BaseMessage {

    private static final long serialVersionUID = -14526741556805998L;

    public NewsMessage addArticles(String title, String description, String url, String picurl) {
        articles.put(title, new Article(title, description, url, picurl));
        return this;
    }

    private Map<String, Article> articles = new HashMap<String, NewsMessage.Article>();

    public NewsMessage() {
        super("news");
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getBody() {
        return super.getBody();
    }

    @JsonIgnore
    public Map<String, Article> getArticlesMap() {
        return articles;
    }

    public List<Article> getArticles() {
        return new ArrayList<NewsMessage.Article>(articles.values());
    }

    public NewsMessage setArticles(Map<String, Article> articles) {
        this.articles = articles;
        return this;
    }

    public static class Article {
        private String title;

        private String description;

        private String url;

        private String picurl;

        public Article() {}

        public Article(String title, String description, String url, String picurl) {
            super();
            this.title = title;
            this.description = description;
            this.url = url;
            this.picurl = picurl;
        }

        public String getTitle() {
            return title;
        }

        public Article setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Article setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public Article setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getPicurl() {
            return picurl;
        }

        public Article setPicurl(String picurl) {
            this.picurl = picurl;
            return this;
        }

    }

}
