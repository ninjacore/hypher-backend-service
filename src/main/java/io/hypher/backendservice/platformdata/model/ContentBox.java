package io.hypher.backendservice.platformdata.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "content_box", schema = "public")
public class ContentBox {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID contentBoxId;

    @Column(name = "profile_id")
    UUID profileId;

    @Column(name = "position")
    String contentBoxPosition;

    @Column(name = "type_id")
    UUID typeId;

    public ContentBox(UUID contentBoxId, UUID profileId, String contentBoxPosition, UUID typeId) {
        this.contentBoxId = contentBoxId;
        this.profileId = profileId;
        this.contentBoxPosition = contentBoxPosition;
        this.typeId = typeId;
    }

    public UUID getContentBoxId() {
        return contentBoxId;
    }

    public void setContentBoxId(UUID contentBoxId) {
        this.contentBoxId = contentBoxId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public String getContentBoxPosition() {
        return contentBoxPosition;
    }

    public void setContentBoxPosition(String contentBoxPosition) {
        this.contentBoxPosition = contentBoxPosition;
    }

    public UUID getTypeId() {
        return typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    



}
