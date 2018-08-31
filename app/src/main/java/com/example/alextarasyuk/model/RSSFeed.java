package com.example.alextarasyuk.model;

import java.util.List;

public class RSSFeed {
    private String title;
    private String description;
    private String link;
    private String rssLink;
    private String language;
    private List<RSSItem> mRSSItems;

    public RSSFeed(String title, String description, String link, String rssLink, String language) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.rssLink = rssLink;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<RSSItem> getRSSItems() {
        return mRSSItems;
    }

    public void setRSSItems(List<RSSItem> RSSItems) {
        mRSSItems = RSSItems;
    }
}
