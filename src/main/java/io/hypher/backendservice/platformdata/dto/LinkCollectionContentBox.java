package io.hypher.backendservice.platformdata.dto;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

// only used for full ProfilePage load — will become obsolete
public class LinkCollectionContentBox {

    String contentType;
    Integer position;
    List<Map<String, String>> contentBox;

    public void addContent(Integer position, String url, String text) {
        Map<String, String> content = Map.of("position", position.toString(), "url", url, "text", text);
        if (this.contentBox == null) {
            throw new NullPointerException("contentBox is null");
        }

        try {
            this.contentBox.add(content);
        } catch (Exception e) {
            throw new NullPointerException("contentBox is null");
        }
    }

    public List<Map<String, String>> getContentBox() {
        return contentBox;
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

    public void setContentBox(List<Map<String, String>> contentBox) {
        this.contentBox = contentBox;
    }

    public LinkCollectionContentBox(String contentType, Integer position) {
        this.contentType = contentType;
        this.position = position;
        // content can be added after instantiation!
        this.contentBox = new ArrayList<>();
    }

}
