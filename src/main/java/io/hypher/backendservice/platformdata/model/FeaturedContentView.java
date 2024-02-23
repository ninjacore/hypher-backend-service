package io.hypher.backendservice.platformdata.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "featured_content_with_profile_id", schema = "public")
public class FeaturedContentView {

    // unique_id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "unique_id")
    Long uniqueId;

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
    public FeaturedContentView(){}

    public Long getUniqueId() {
        return uniqueId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public UUID getFeaturedContentId() {
        return featuredContentId;
    }

    public UUID getContentBoxId() {
        return contentBoxId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Integer getPosition() {
        return position;
    }

    public String getCategory() {
        return category;
    }

    

}