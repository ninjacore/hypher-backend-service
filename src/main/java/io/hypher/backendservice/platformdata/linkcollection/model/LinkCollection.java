package io.hypher.backendservice.platformdata.linkcollection.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "link_collection", schema = "public")
public class LinkCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID linkCollectionId;

    @Column(name = "content_box_id")
    UUID contentBoxId;

    @Column(name = "position")
    Integer position;

    @Column(name = "url")
    String url;

    @Column(name = "text")
    String text;

    @Column(name = "frontend_id")
    String frontendId;

    // default constructor (hibernate.InstantiationException)
    public LinkCollection(){}

    public LinkCollection(UUID linkCollectionId, UUID contentBoxId, Integer position, String url, String text, String frontendId) {
        this.linkCollectionId = linkCollectionId;
        this.contentBoxId = contentBoxId;
        this.position = position;
        this.url = url;
        this.text = text;
        this.frontendId = frontendId;
    }

    public UUID getLinkCollectionId() {
        return linkCollectionId;
    }

    public void setLinkCollectionId(UUID linkCollectionId) {
        this.linkCollectionId = linkCollectionId;
    }

    public UUID getContentBoxId() {
        return contentBoxId;
    }

    public void setContentBoxId(UUID contentBoxId) {
        this.contentBoxId = contentBoxId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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
