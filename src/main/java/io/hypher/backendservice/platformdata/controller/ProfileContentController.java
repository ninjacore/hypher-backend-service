package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.ProfileContent;
import io.hypher.backendservice.platformdata.service.ProfileContentService;
import io.hypher.backendservice.platformdata.service.ProfileService;
import io.hypher.backendservice.platformdata.model.Profile;

import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class ProfileContentController {
    
    @Autowired
    ProfileContentService profileContentService;

    @Autowired
    ProfileService profileService;

    // @PostMapping("/profileContents")
    // public Optional<ProfileContent> create(@RequestBody ProfileContent profileContent) {
    //     return profileContentService.save(profileContent);        
    // }

    // TODO: build the DTO from there
    @GetMapping("/profilePage/{handle}")
    public Collection<ProfileContent> getProfilePage(@PathVariable(value = "handle") String profileHandle) throws ResourceNotFoundException{

        // once we get the data we can work with it
        Collection<Profile> profiles = profileService.findByHandle(profileHandle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Profile profile = profiles.iterator().next();

        UUID profileId = profile.getProfileId();
        
        return profileContentService.findByProfileId(profileId);
    }

    
    
    @GetMapping("/profileContents")
    public List<ProfileContent> getAll() {
        return profileContentService.findAll();   
    }

    @GetMapping("/profileContents/{id}")
    public Optional<ProfileContent> getById(@PathVariable(value = "id") UUID profileContentId) {
        
        return profileContentService.findById(profileContentId);
    }

}
