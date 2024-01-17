package io.hypher.backendservice.platformdata.dto;

import java.util.List;
import java.util.ArrayList;

public class MainContentBox {

    String contentType;
    Integer position;
    List<MainContent> contentBox;

    public void addContent(MainContent content) {
        if (this.contentBox == null) {
            throw new NullPointerException("contentBox is null");
        }

        try {
            this.contentBox.add(content);
        } catch (Exception e) {
            throw new NullPointerException("content is null");
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

    public List<MainContent> getContentBox() {
        return contentBox;
    }

    public void setContentBox(List<MainContent> contentBox) {
        this.contentBox = contentBox;
    }

    public MainContentBox(){
        this.position = 0; // is always first
        this.contentType = "main";
        // content can be added after instantiation!
        this.contentBox = new ArrayList<>();
    }
    
}
