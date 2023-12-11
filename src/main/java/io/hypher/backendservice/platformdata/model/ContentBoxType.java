package io.hypher.backendservice.platformdata.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "content_box_type", schema = "public")
public class ContentBoxType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID contentBoxTypeId;

    @Column(name = "short_title")
    String shortTitle;

    @Column(name = "guiding_description")
    String guidingDescription;

    @Column(name = "color_coding")
    String colorCoding;

    // default constructor (hibernate.InstantiationException)
    public ContentBoxType(){}

    public ContentBoxType(UUID contentBoxTypeId, String shortTitle, String guidingDescription, String colorCoding) {
        this.contentBoxTypeId = contentBoxTypeId;
        this.shortTitle = shortTitle;
        this.guidingDescription = guidingDescription;
        this.colorCoding = colorCoding;
    }


    public UUID getContentBoxTypeId() {
        return contentBoxTypeId;
    }

    public void setContentBoxTypeId(UUID contentBoxTypeId) {
        this.contentBoxTypeId = contentBoxTypeId;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getGuidingDescription() {
        return guidingDescription;
    }

    public void setGuidingDescription(String guidingDescription) {
        this.guidingDescription = guidingDescription;
    }

    public String getColorCoding() {
        return colorCoding;
    }

    public void setColorCoding(String colorCoding) {
        this.colorCoding = colorCoding;
    }


    

    
}
