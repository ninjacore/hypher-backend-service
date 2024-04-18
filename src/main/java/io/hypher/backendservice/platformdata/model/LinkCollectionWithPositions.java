package io.hypher.backendservice.platformdata.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "link_collection_with_handle_by_positions", schema = "public")
public class LinkCollectionWithPositions {
    // row_number() OVER (ORDER BY content_box.id) AS unique_id,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "unique_id")
    Long uniqueId;

    // profile.handle AS profile_handle,
    @Column(name = "profile_handle")
    String profileHandle;
    
    // content_box.profile_id,
    @Column(name = "profile_id")
    UUID profileId;
    
    // content_box.id AS content_box_id,
    @Column(name = "content_box_id")
    UUID contentBoxId;
    
    // content_box.position AS content_box_position,
    @Column(name = "content_box_position")
    String contentBoxPosition;
    
    // link_collection.id AS link_collection_id,
    @Column(name = "link_collection_id")
    UUID linkCollectionId;
    
    // link_collection."position" AS position_in_link_collection,
    @Column(name = "position_in_link_collection")
    Integer position;
    
    // link_collection.url AS link_collection_url,
    @Column(name = "link_collection_url")
    String url;
    
    // link_collection.text AS link_collection_alt_link_text
    @Column(name = "link_collection_alt_link_text")
    String text;

    // default constructor (hibernate.InstantiationException)
    public LinkCollectionWithPositions() {
    }
    
    public LinkCollectionWithPositions(String profileHandle, UUID profileId, UUID contentBoxId, String contentBoxPosition, UUID linkCollectionId, Integer position, String url, String text) {
        this.profileHandle = profileHandle;
        this.profileId = profileId;
        this.contentBoxId = contentBoxId;
        this.contentBoxPosition = contentBoxPosition;
        this.linkCollectionId = linkCollectionId;
        this.position = position;
        this.url = url;
        this.text = text;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getProfileHandle() {
        return profileHandle;
    }

    public void setProfileHandle(String profileHandle) {
        this.profileHandle = profileHandle;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public UUID getContentBoxId() {
        return contentBoxId;
    }

    public void setContentBoxId(UUID contentBoxId) {
        this.contentBoxId = contentBoxId;
    }

    public String getContentBoxPosition() {
        return contentBoxPosition;
    }

    public void setContentBoxPosition(String contentBoxPosition) {
        this.contentBoxPosition = contentBoxPosition;
    }

    public UUID getLinkCollectionId() {
        return linkCollectionId;
    }

    public void setLinkCollectionId(UUID linkCollectionId) {
        this.linkCollectionId = linkCollectionId;
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

}