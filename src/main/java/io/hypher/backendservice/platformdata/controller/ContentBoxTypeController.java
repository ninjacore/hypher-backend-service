package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.ContentBoxType;
import io.hypher.backendservice.platformdata.service.ContentBoxTypeService;
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
public class ContentBoxTypeController {

    @Autowired
    private ContentBoxTypeService contentBoxTypeService;

    @PostMapping("/contentBoxTypes")
    public Optional<ContentBoxType> create(@RequestBody ContentBoxType contentBoxType) {
        return contentBoxTypeService.save(contentBoxType);        
    }
    
    
    @GetMapping("/contentBoxTypes")
    public List<ContentBoxType> getAll() {
        return contentBoxTypeService.findAll();   
    }

    @GetMapping("/contentBoxTypes/{id}")
    public Optional<ContentBoxType> getById(@PathVariable(value = "id") UUID contentBoxTypeId) {
        
        return contentBoxTypeService.findById(contentBoxTypeId);
    }


    @PutMapping("/contentBoxTypes/{id}")
    public ResponseEntity<ContentBoxType> update(@PathVariable(value = "id") UUID contentBoxTypeId, @RequestBody ContentBoxType updatedContentBoxType) throws ResourceNotFoundException, WrongBodyException{

        // security: only update if contentBoxType exists
        contentBoxTypeService.findById(contentBoxTypeId).orElseThrow(() -> new ResourceNotFoundException("ContentBoxType not found"));

        // security: only save if body and path variable are the same
        if (updatedContentBoxType.getContentBoxTypeId().toString().equals(contentBoxTypeId.toString())) {
            ContentBoxType updatedContentBoxTypeFromDatabase = contentBoxTypeService.save(updatedContentBoxType)
                .orElseThrow(() -> new ResourceNotFoundException("ContentBoxType not found"));
            return ResponseEntity.ok(updatedContentBoxTypeFromDatabase);

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }


    @DeleteMapping("/contentBoxTypes/{id}")
    public String delete(@PathVariable(value = "id") UUID contentBoxTypeId) throws ResourceNotFoundException{

        ContentBoxType contentBoxTypeToDelete = contentBoxTypeService.findById(contentBoxTypeId).orElseThrow(() -> new ResourceNotFoundException("ContentBoxType not found"));

        return contentBoxTypeService.delete(contentBoxTypeToDelete);
    }
    
    
    
}
