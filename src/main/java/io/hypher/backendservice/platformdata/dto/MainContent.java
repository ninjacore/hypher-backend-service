package io.hypher.backendservice.platformdata.dto;

import java.util.List;

public class MainContent {

    // main types
    String name;
    String bio; 
    List<String> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MainContent(String name, String bio, List<String> tags) {
        this.name = name;
        this.bio = bio;
        this.tags = tags;
    }

    
    
}
