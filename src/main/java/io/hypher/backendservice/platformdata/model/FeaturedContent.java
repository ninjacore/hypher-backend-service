package io.hypher.backendservice.platformdata.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "featured_content", schema = "public")
public class FeaturedContent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID featuredContentId;

    @Column(name = "content_box_id")
    UUID contentBoxId;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "url")
    String url;

    @Column(name = "position")
    Integer position;

    @Column(name = "category")
    String category;

    public FeaturedContent(UUID featuredContentId, UUID contentBoxId, String title, String description, String url,
            Integer position, String category) {
        this.featuredContentId = featuredContentId;
        this.contentBoxId = contentBoxId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.position = position;
        this.category = category;
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
