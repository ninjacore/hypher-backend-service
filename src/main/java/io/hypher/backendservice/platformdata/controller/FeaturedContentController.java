package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.dto.FeaturedContentUpdate;
import io.hypher.backendservice.platformdata.model.FeaturedContent;
import io.hypher.backendservice.platformdata.model.FeaturedContentView;
import io.hypher.backendservice.platformdata.model.Profile;

import io.hypher.backendservice.platformdata.service.FeaturedContentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class FeaturedContentController {

    @Autowired
    private FeaturedContentService featuredContentService;

    @Autowired
    private ProfileService profileService;

    @PostMapping("/featuredContents")
    public Optional<FeaturedContent> create(@RequestBody FeaturedContent featuredContent) {
        return featuredContentService.save(featuredContent);        
    }
    
    
    @GetMapping("/featuredContents")
    public List<FeaturedContent> getAll() {
        return featuredContentService.findAll();   
    }

    @GetMapping("/featuredContents/{id}")
    public Optional<FeaturedContent> getById(@PathVariable(value = "id") UUID featuredContentId) {
        
        return featuredContentService.findById(featuredContentId);
    }

    @PutMapping("featuredContents/{handle}/update")
    public ResponseEntity<FeaturedContent> updateFeaturedContent(@PathVariable (value = "handle") String handle, @RequestParam String position, @RequestBody FeaturedContentUpdate entity) throws ResourceNotFoundException {

        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find featured content view by profile id
        List<FeaturedContentView> featuredContents = featuredContentService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("FeaturedContent not found"));  

        // get the one with the right position
        FeaturedContentView desiredFeaturedContent = featuredContents.stream()
            .filter(featuredContent -> featuredContent.getPosition() == Integer.parseInt(position))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("FeaturedContent not found"));

        // update the entity
        FeaturedContent updatedFeaturedContent = new FeaturedContent();
        updatedFeaturedContent.setFeaturedContentId(desiredFeaturedContent.getFeaturedContentId());
        updatedFeaturedContent.setContentBoxId(desiredFeaturedContent.getContentBoxId());
        updatedFeaturedContent.setPosition(desiredFeaturedContent.getPosition());
        updatedFeaturedContent.setCategory("featuredMusic");

        updatedFeaturedContent.setTitle(entity.getTitle());
        updatedFeaturedContent.setDescription(entity.getDescription());
        updatedFeaturedContent.setUrl(entity.getUrl());

        // save the updated entity
        featuredContentService.save(updatedFeaturedContent);

        return ResponseEntity.ok(updatedFeaturedContent);
    }

    // default — to be replaced
    @PutMapping("/featuredContents/{id}")
    public ResponseEntity<FeaturedContent> update(@PathVariable(value = "id") UUID featuredContentId, @RequestBody FeaturedContent updatedFeaturedContent) throws ResourceNotFoundException, WrongBodyException{

        // security: only update if featuredContent exists
        featuredContentService.findById(featuredContentId).orElseThrow(() -> new ResourceNotFoundException("FeaturedContent not found"));

        // security: only save if body and path variable are the same
        if (updatedFeaturedContent.getFeaturedContentId().toString().equals(featuredContentId.toString())) {
            FeaturedContent updatedFeaturedContentFromDatabase = featuredContentService.save(updatedFeaturedContent)
                .orElseThrow(() -> new ResourceNotFoundException("FeaturedContent not found"));
            return ResponseEntity.ok(updatedFeaturedContentFromDatabase);

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }


    @DeleteMapping("/featuredContents/{id}")
    public String delete(@PathVariable(value = "id") UUID featuredContentId) throws ResourceNotFoundException{

        FeaturedContent featuredContentToDelete = featuredContentService.findById(featuredContentId).orElseThrow(() -> new ResourceNotFoundException("FeaturedContent not found"));

        return featuredContentService.delete(featuredContentToDelete);
    }
    

}
