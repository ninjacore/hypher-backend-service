package io.hypher.backendservice.platformdata.featuredcontent.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "featured_content_with_profile_id", schema = "public")
public class FeaturedContentWithProfileId {
       
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "unique_id")
    Long uniqueId;

    @Column(name = "featured_content_frontend_id")
    String frontendId;

    @Column(name = "profile_id")
    UUID profileId;

    @Column(name = "featured_content_id")
    UUID featuredContentId;

    @Column(name = "content_box_id")
    UUID contentBoxId;

    @Column(name = "featured_content_title")
    String title;

    @Column(name = "featured_content_description")
    String description;

    @Column(name = "featured_content_url")
    String url;

    @Column(name = "position_in_featured_content")
    Integer position;

    @Column(name = "featured_content_category")
    String category;

    // default constructor (hibernate.InstantiationException)
    public FeaturedContentWithProfileId(){}

    public FeaturedContentWithProfileId(Long uniqueId, String frontendId, UUID profileId, UUID featuredContentId,
            UUID contentBoxId, String title, String description, String url, Integer position, String category) {
        this.uniqueId = uniqueId;
        this.frontendId = frontendId;
        this.profileId = profileId;
        this.featuredContentId = featuredContentId;
        this.contentBoxId = contentBoxId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.position = position;
        this.category = category;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getFrontendId() {
        return frontendId;
    }

    public void setFrontendId(String frontendId) {
        this.frontendId = frontendId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public UUID getFeaturedContentId() {
        return featuredContentId;
    }

    public void setFeaturedContentId(UUID featuredContentId) {
        this.featuredContentId = featuredContentId;
    }

    public UUID getContentBoxId() {
        return contentBoxId;
    }

    public void setContentBoxId(UUID contentBoxId) {
        this.contentBoxId = contentBoxId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    



}


