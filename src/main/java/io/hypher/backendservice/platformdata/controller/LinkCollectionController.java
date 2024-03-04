package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.LinkCollection;
import io.hypher.backendservice.platformdata.service.LinkCollectionService;
import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;

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
public class LinkCollectionController {

    @Autowired
    private LinkCollectionService linkCollectionService;

    @PostMapping("/linkCollections")
    public Optional<LinkCollection> create(@RequestBody LinkCollection linkCollection) {
        return linkCollectionService.save(linkCollection);        
    }
    
    
    @GetMapping("/linkCollections")
    public List<LinkCollection> getAll() {
        return linkCollectionService.findAll();   
    }

    @GetMapping("/linkCollections/{id}")
    public Optional<LinkCollection> getById(@PathVariable(value = "id") UUID linkCollectionId) {
        
        return linkCollectionService.findById(linkCollectionId);
    }


    @PutMapping("/linkCollections/{id}")
    public ResponseEntity<LinkCollection> update(@PathVariable(value = "id") UUID linkCollectionId, @RequestBody LinkCollection updatedLinkCollection) throws ResourceNotFoundException, WrongBodyException{

        // security: only update if linkCollection exists
        linkCollectionService.findById(linkCollectionId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        // security: only save if body and path variable are the same
        if (updatedLinkCollection.getLinkCollectionId().toString().equals(linkCollectionId.toString())) {
            LinkCollection updatedLinkCollectionFromDatabase = linkCollectionService.save(updatedLinkCollection)
                .orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
            return ResponseEntity.ok(updatedLinkCollectionFromDatabase);

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }


    @DeleteMapping("/linkCollections/{id}")
    public String delete(@PathVariable(value = "id") UUID linkCollectionId) throws ResourceNotFoundException{

        LinkCollection linkCollectionToDelete = linkCollectionService.findById(linkCollectionId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        return linkCollectionService.delete(linkCollectionToDelete);
    }
    
    
    
}
