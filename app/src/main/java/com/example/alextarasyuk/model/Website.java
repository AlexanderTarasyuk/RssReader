package com.example.alextarasyuk.model;

public class Website {

    Integer mInteger;
    String mTitle;
    String mLink;
    String mRssLink;
    String mDescription;

    // constructor
    public Website(){

    }

    // constructor with parameters
    public Website(String title, String link, String rss_link, String description){
        this.mTitle = title;
        this.mLink = link;
        this.mRssLink = rss_link;
        this.mDescription = description;
    }

    /**
     * All set methods
     * */
    public void setId(Integer id){
        this.mInteger = id;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public void setLink(String link){
        this.mLink = link;
    }

    public void setRSSLink(String rss_link){
        this.mRssLink = rss_link;
    }

    public void setDescription(String description){
        this.mDescription = description;
    }

    /**
     * All get methods
     * */
    public Integer getId(){
        return this.mInteger;
    }

    public String getTitle(){
        return this.mTitle;
    }

    public String getLink(){
        return this.mLink;
    }

    public String getRSSLink(){
        return this.mRssLink;
    }

    public String getDescription(){
        return this.mDescription;
    }
}
