package io.hypher.backendservice.platformdata.linkcollection.dto;

public class LinkWithinCollection {
    
    String url;

    String text;

    Integer position;

    String frontendId;


    public LinkWithinCollection() {
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public Integer getPosition() {
        return position;
    }


    public void setPosition(Integer position) {
        this.position = position;
    }


    public String getFrontendId() {
        return frontendId;
    }


    public void setFrontendId(String frontendId) {
        this.frontendId = frontendId;
    }

    


}
