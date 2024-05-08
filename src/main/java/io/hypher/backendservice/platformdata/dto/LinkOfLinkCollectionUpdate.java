package io.hypher.backendservice.platformdata.dto;

public class LinkOfLinkCollectionUpdate {

    String url;

    String text;

    String frontendId;

    public LinkOfLinkCollectionUpdate() {
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

    public String getFrontendId() {
        return frontendId;
    }

    public void setFrontendId(String frontendId) {
        this.frontendId = frontendId;
    }
    
}
