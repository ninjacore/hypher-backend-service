package io.hypher.backendservice.platformdata.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile", schema = "public")
public class Profile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID profileId;

    @Column(name = "displayed_name")
    String displayedName;

    @Column(name = "handle")
    String profileHandle;

    @Column(name = "bio")
    String userBio;

    @Column(name = "tags")
    String tags;

    // default constructor (hibernate.InstantiationException)
    public Profile(){}

    public Profile(UUID profileId, String displayedName, String profileHandle, String userBio, String tags) {
        this.profileId = profileId;
        this.displayedName = displayedName;
        this.profileHandle = profileHandle;
        this.userBio = userBio;
        this.tags = tags;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    public String getProfileHandle() {
        return profileHandle;
    }

    public void setProfileHandle(String profileHandle) {
        this.profileHandle = profileHandle;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }    


}
