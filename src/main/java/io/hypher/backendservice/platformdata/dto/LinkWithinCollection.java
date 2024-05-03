package io.hypher.backendservice.platformdata.dto;

public class LinkWithinCollection {

    String url;

    String text;

    Integer position;

    // non-database id for client-side use
    // Long uniqueId;
    String uniqueId;

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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    
}
