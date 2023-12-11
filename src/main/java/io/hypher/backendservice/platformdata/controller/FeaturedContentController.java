package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.FeaturedContent;
import io.hypher.backendservice.platformdata.service.FeaturedContentService;
import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;

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
public class FeaturedContentController {

    @Autowired
    FeaturedContentService featuredContentService;

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
