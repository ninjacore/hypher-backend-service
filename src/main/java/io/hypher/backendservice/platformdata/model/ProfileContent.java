package io.hypher.backendservice.platformdata.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "profile_content", schema = "public")
public class ProfileContent {

    // unique_id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "unique_id")
    Long uniqueId;

    // profile_id
    @Column(name = "profile_id")
    UUID profileId;

    // content_box_id
    @Column(name = "content_box_id")
    UUID contentBoxId;

    // content_box_short_title
    @Column(name = "content_box_short_title")
    String contentBoxShortTitle;

    // content_box_position
    @Column(name = "content_box_position")
    Integer contentBoxPosition;

    // content_box_type_id
    @Column(name = "content_box_type_id")
    UUID contentBoxTypeId;

    // content_box_type_short_title
    @Column(name = "content_box_type_short_title")
    String contentBoxTypeShortTitle;

    // content_box_type_guiding_description
    @Column(name = "content_box_type_guiding_description")
    String contentBoxTypeGuidingDescription;

    // content_box_type_color_coding
    @Column(name = "content_box_type_color_coding")
    String contentBoxTypeColorCoding;

    // featured_content_id
    @Column(name = "featured_content_id")
    UUID featuredContentId;

    // featured_content_title
    @Column(name = "featured_content_title")
    String featuredContentTitle;

    // featured_content_description
    @Column(name = "featured_content_description")
    String featuredContentDescription;

    // featured_content_url
    @Column(name = "featured_content_url")
    String featuredContentUrl;

    // position_in_featured_content
    @Column(name = "position_in_featured_content")
    Integer positionInFeaturedContent;

    // featured_content_category
    @Column(name = "featured_content_category")
    String featuredContentCategory;

    // link_collection_id
    @Column(name = "link_collection_id")
    UUID linkCollectionId;

    // position_in_link_collection
    @Column(name = "position_in_link_collection")
    Integer positionInLinkCollection;

    // link_collection_url
    @Column(name = "link_collection_url")
    String linkCollectionUrl;

    // link_collection_alt_link_text
    @Column(name = "link_collection_alt_link_text")
    String linkCollectionAltLinkText;

    // default constructor (hibernate.InstantiationException)
    public ProfileContent(){}

    public Long getUniqueId() {
        return uniqueId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public UUID getContentBoxId() {
        return contentBoxId;
    }

    public String getContentBoxShortTitle() {
        return contentBoxShortTitle;
    }

    public Integer getContentBoxPosition() {
        return contentBoxPosition;
    }

    public UUID getContentBoxTypeId() {
        return contentBoxTypeId;
    }

    public String getContentBoxTypeShortTitle() {
        return contentBoxTypeShortTitle;
    }

    public String getContentBoxTypeGuidingDescription() {
        return contentBoxTypeGuidingDescription;
    }

    public String getContentBoxTypeColorCoding() {
        return contentBoxTypeColorCoding;
    }

    public UUID getFeaturedContentId() {
        return featuredContentId;
    }

    public String getFeaturedContentTitle() {
        return featuredContentTitle;
    }

    public String getFeaturedContentDescription() {
        return featuredContentDescription;
    }

    public String getFeaturedContentUrl() {
        return featuredContentUrl;
    }

    public Integer getPositionInFeaturedContent() {
        return positionInFeaturedContent;
    }

    public String getFeaturedContentCategory() {
        return featuredContentCategory;
    }

    public UUID getLinkCollectionId() {
        return linkCollectionId;
    }

    public Integer getPositionInLinkCollection() {
        return positionInLinkCollection;
    }

    public String getLinkCollectionUrl() {
        return linkCollectionUrl;
    }

    public String getLinkCollectionAltLinkText() {
        return linkCollectionAltLinkText;
    }

    
        
}
