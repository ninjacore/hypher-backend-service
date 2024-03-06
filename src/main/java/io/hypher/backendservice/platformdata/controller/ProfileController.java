package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.dto.TagsUpdate;
import io.hypher.backendservice.platformdata.model.Profile;
import io.hypher.backendservice.platformdata.service.ProfileService;
import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/profiles")
    public Optional<Profile> create(@RequestBody Profile profile) {
        return profileService.save(profile);        
    }
    
    
    @GetMapping("/profiles")
    public List<Profile> getAll() {
        return profileService.findAll();   
    }

    @GetMapping("/profiles/{id}")
    public Optional<Profile> getById(@PathVariable(value = "id") UUID profileId) {
        
        return profileService.findById(profileId);
    }

    @GetMapping("/profiles/handle/{handle}")
    public Optional<Collection<Profile>> getByHandle(@PathVariable(value = "handle") String profileHandle) {
        
        return profileService.findByHandle(profileHandle);
    }

    // special endpoint to get only tags for a profile
    @GetMapping("/profiles/{handle}/tags")
    public Optional<String> getTagsByHandle(@PathVariable(value = "handle") String profileHandle) throws ResourceNotFoundException {
        
        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(profileHandle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Profile profile = profiles.iterator().next();

        return Optional.of(profile.getTags());
    }


    @PutMapping("/profiles/{id}")
    public ResponseEntity<Profile> update(@PathVariable(value = "id") UUID profileId, @RequestBody Profile updatedProfile) throws ResourceNotFoundException, WrongBodyException{

        // security: only update if profile exists
        profileService.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        // security: only save if body and path variable are the same
        if (updatedProfile.getProfileId().toString().equals(profileId.toString())) {
            Profile updatedProfileFromDatabase = profileService.save(updatedProfile)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
            return ResponseEntity.ok(updatedProfileFromDatabase);

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }

    @PutMapping("/profiles/{handle}/tags")
    public ResponseEntity<String> updateTags(@PathVariable(value = "handle") String profileHandle, @RequestBody TagsUpdate updatedTags) throws ResourceNotFoundException, WrongBodyException{

        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(profileHandle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        // define target profile
        Profile profile = profiles.iterator().next();

        // security: only save if body and path variable are the same
        if (profile.getProfileHandle().equals(profileHandle)) {
            
            // convert updatedTags from client to string
            String tags = "";
            for (String tag : updatedTags.getTags()) {
                tags += tag + ", ";
            }
            // remove trailing ", "
            tags = tags.substring(0, tags.length() - 2);

            profile.setTags(tags);
            Profile updatedProfileFromDatabase = profileService.save(profile)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
            return ResponseEntity.ok(updatedProfileFromDatabase.getTags());

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }


    @DeleteMapping("/profiles/{id}")
    public String delete(@PathVariable(value = "id") UUID profileId) throws ResourceNotFoundException{

        Profile profileToDelete = profileService.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return profileService.delete(profileToDelete);
    }
    
    
    
}
