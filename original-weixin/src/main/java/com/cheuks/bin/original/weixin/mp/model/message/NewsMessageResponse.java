package com.cheuks.bin.original.weixin.mp.model.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description:回复图文消息
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午2:24:29
 *
 */
public class NewsMessageResponse extends BaseMessageResponse {

    private static final long serialVersionUID = 8721827746249731195L;

    public NewsMessageResponse() {
        super("news");
    }

    @Alias("ArticleCount")
    private int articleCount;// 图文消息个数，限制为8条以内

    @Alias("Articles")
    private List<Item> articles = new ArrayList<NewsMessageResponse.Item>(8);// 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应

    public static class Item {
        private NewsArticlesItem item;

        public NewsArticlesItem getItem() {
            return item;
        }

        public Item setItem(NewsArticlesItem item) {
            this.item = item;
            return this;
        }

        public Item(NewsArticlesItem item) {
            super();
            this.item = item;
        }

        public Item() {}

    }

    public static class NewsArticlesItem implements Serializable {

        private static final long serialVersionUID = -3750091904189390418L;

        @Alias("Title")
        private String title;// 图文消息标题

        @Alias("Description")
        private String description;// 图文消息描述

        @Alias("PicUrl")
        private String picUrl;// 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200

        @Alias("Url")
        private String url;// 点击图文消息跳转链接

        public String getTitle() {
            return title;
        }

        public NewsArticlesItem setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public NewsArticlesItem setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public NewsArticlesItem setPicUrl(String picUrl) {
            this.picUrl = picUrl;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public NewsArticlesItem setUrl(String url) {
            this.url = url;
            return this;
        }

        public NewsArticlesItem(String title, String description, String picUrl, String url) {
            super();
            this.title = title;
            this.description = description;
            this.picUrl = picUrl;
            this.url = url;
        }

        public NewsArticlesItem() {
            super();
        }

    }

    public int getArticleCount() {
        return articleCount;
    }

    public NewsMessageResponse setArticleCount(int articleCount) {
        this.articleCount = articleCount;
        return this;
    }

    public List<Item> getArticles() {
        return articles;
    }

    public NewsMessageResponse setArticles(List<Item> articles) {
        this.articles = articles;
        return this;
    }

    public NewsMessageResponse appendNewsArticlesItem(NewsArticlesItem newsArticlesItem) {
        this.articles.add(new Item(newsArticlesItem));
        this.articleCount++;
        return this;
    }

}
