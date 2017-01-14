package com.example.baydi.flickergallery.Model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by baydi on 1/14/17.
 */


public class FlickerFeed {

    private  String title,link,media,dateTaken,description,published,author,authorID,tag;

     public FlickerFeed(){
        title = link = media = dateTaken = description = published = author = authorID = tag = null;
    }

    public FlickerFeed(JSONObject jsonObject){
        try{
            setTitle(jsonObject.getString("title"));
            setLink(jsonObject.getString("link"));
            JSONObject mediaObject = jsonObject.getJSONObject("media");
            setMedia(mediaObject.getString("m"));
            setDateTaken(jsonObject.getString("date_taken"));
            setDescription(jsonObject.getString("description"));
            setPublished(jsonObject.getString("published"));
            setAuthor(jsonObject.getString("author"));
            setAuthorID(jsonObject.getString("author_id"));
            setTag(jsonObject.getString("tags"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    public String getMedia(){
        return media;
    }

    public String getDateTaken(){
        return dateTaken;
    }

    public String getDescription(){
        return description;
    }

    public String getPublished(){
        return published;
    }

    public String getAuthor(){
        return author;
    }

    public String getAuthorID(){
        return authorID;
    }

    public String getTag(){
        return tag;
    }

    public void setTitle(String string){
        this.title = string;
    }

    public void setLink(String string){
        this.link = string;
    }

    public void setMedia(String string){
        this.media = string;
    }

    public void setDateTaken(String string){
        this.dateTaken = string;
    }

    public void setDescription(String string){
        this.description = string;
    }

    public void setPublished(String string){
        this.published = string;
    }

    public void setAuthor(String string){
        this.author = string;
    }

    public void setAuthorID(String string){
        this.authorID = string;
    }

    public void setTag(String string){
        this.tag = string;
    }
}
