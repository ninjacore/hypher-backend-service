package io.hypher.backendservice.platformdata.dto;

import java.util.List;

public class MainContentBox {

    // main types
    String bio; 
    String name;
    List<String> tags;

    String contentType;
    Integer position;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public MainContentBox(String bio, String name, List<String> tags) {
        this.position = 0; // is always first
        this.contentType = "main";
        this.bio = bio;
        this.name = name;
        this.tags = tags;
    }
    
}
