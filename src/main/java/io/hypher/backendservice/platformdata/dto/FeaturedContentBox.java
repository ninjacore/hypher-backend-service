package io.hypher.backendservice.platformdata.dto;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class FeaturedContentBox {

    String contentType;
    Integer position;
    String shortTitle;
    List<Map<String, String>> contentBox;

    public void addContent(Integer position, String url, String title, String description, String category) {
        Map<String, String> content = Map.of("position", position.toString(), "url", url, "title", title, "description", description, "category", category);
        if (this.contentBox == null) {
            throw new NullPointerException("contentBox is null");
        }
        try {
            this.contentBox.add(content);            
        } catch (Exception e) {
            throw new NullPointerException("contentBox is null");
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public List<Map<String, String>> getContentBox() {
        return contentBox;
    }

    public void setContentBox(List<Map<String, String>> contentBox) {
        this.contentBox = contentBox;
    }

    public FeaturedContentBox(String contentType, Integer position, String shortTitle) {
        this.contentType = contentType;
        this.position = position;
        this.shortTitle = shortTitle;
        // content can be added after instantiation!
        this.contentBox = new ArrayList<>();

    }    


    
}
