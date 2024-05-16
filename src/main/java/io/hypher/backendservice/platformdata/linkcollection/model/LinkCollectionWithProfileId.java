package io.hypher.backendservice.platformdata.linkcollection.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "link_collection_with_profile_id", schema = "public")
public class LinkCollectionWithProfileId {

    // unique_id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "unique_id")
    Long uniqueId;

    @Column(name = "link_collection_frontend_id")
    String frontendId;

    @Column(name = "profile_id")
    UUID profileId;

    @Column(name = "link_collection_id")
    UUID linkCollectionId;

    @Column(name = "content_box_id")
    UUID contentBoxId;

    @Column(name = "position_in_link_collection")
    Integer position;

    @Column(name = "link_collection_url")
    String url;

    @Column(name = "link_collection_alt_link_text")
    String text;

    // default constructor (hibernate.InstantiationException)
    public LinkCollectionWithProfileId(){}

    public LinkCollectionWithProfileId(UUID linkCollectionId, UUID contentBoxId, Integer position, String url, String text, String frontendId) {
        this.linkCollectionId = linkCollectionId;
        this.contentBoxId = contentBoxId;
        this.position = position;
        this.url = url;
        this.text = text;
        this.frontendId = frontendId;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
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
