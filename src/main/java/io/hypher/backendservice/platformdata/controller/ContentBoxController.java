package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.ContentBox;
import io.hypher.backendservice.platformdata.service.ContentBoxService;
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
public class ContentBoxController {

    @Autowired
    private ContentBoxService contentBoxService;

    @PostMapping("/contentBoxes")
    public Optional<ContentBox> create(@RequestBody ContentBox contentBox) {
        return contentBoxService.save(contentBox);        
    }
    
    
    @GetMapping("/contentBoxes")
    public List<ContentBox> getAll() {
        return contentBoxService.findAll();   
    }

    @GetMapping("/contentBoxes/{id}")
    public Optional<ContentBox> getById(@PathVariable(value = "id") UUID contentBoxId) {
        
        return contentBoxService.findById(contentBoxId);
    }


    @PutMapping("/contentBoxes/{id}")
    public ResponseEntity<ContentBox> update(@PathVariable(value = "id") UUID contentBoxId, @RequestBody ContentBox updatedContentBox) throws ResourceNotFoundException, WrongBodyException{

        // security: only update if contentBox exists
        contentBoxService.findById(contentBoxId).orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));

        // security: only save if body and path variable are the same
        if (updatedContentBox.getContentBoxId().toString().equals(contentBoxId.toString())) {
            ContentBox updatedContentBoxFromDatabase = contentBoxService.save(updatedContentBox)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));
            return ResponseEntity.ok(updatedContentBoxFromDatabase);

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }


    @DeleteMapping("/contentBoxes/{id}")
    public String delete(@PathVariable(value = "id") UUID contentBoxId) throws ResourceNotFoundException{

        ContentBox contentBoxToDelete = contentBoxService.findById(contentBoxId).orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));

        return contentBoxService.delete(contentBoxToDelete);
    }
    
    
    
}
