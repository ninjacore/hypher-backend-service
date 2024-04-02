package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.dto.LinkCollectionUpdate;
import io.hypher.backendservice.platformdata.model.LinkCollection;
import io.hypher.backendservice.platformdata.model.LinkCollectionView;
import io.hypher.backendservice.platformdata.model.Profile;

import io.hypher.backendservice.platformdata.service.LinkCollectionService;
import io.hypher.backendservice.platformdata.service.ProfileService;

import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
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
public class LinkCollectionController {

    @Autowired
    private LinkCollectionService linkCollectionService;

    @Autowired
    private ProfileService profileService;    

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

    @GetMapping("/linkCollections/byHandle/{handle}")
    public List<LinkCollectionUpdate> getByHandle(@PathVariable(value = "handle") String handle) throws ResourceNotFoundException{

        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find linkCollections by profileId
        List<LinkCollectionView> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        if(linkCollections.isEmpty()) {
            throw new ResourceNotFoundException("LinkCollection not found");
        } else {
            List<LinkCollectionUpdate> linkCollectionDTO = new ArrayList<>();
            for (LinkCollectionView linkCollectionView : linkCollections) {
                LinkCollectionUpdate entity = new LinkCollectionUpdate();
                entity.setUrl(linkCollectionView.getUrl());
                entity.setText(linkCollectionView.getText());
                
                linkCollectionDTO.add(entity);
            }
            
            return linkCollectionDTO;
        }


    }

    @PutMapping("/linkCollections/{handle}/update")
    public ResponseEntity<LinkCollection> updateByHandle(@PathVariable (value = "handle") String handle, @RequestParam String position, @RequestBody LinkCollectionUpdate entity) throws ResourceNotFoundException, WrongBodyException{

        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find linkCollection by profileId
        List<LinkCollectionView> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        // get the one with the right position
        LinkCollectionView desiredLinkCollection = linkCollections.stream()
            .filter(lc -> lc.getPosition() == Integer.parseInt(position))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        // update the entity
        LinkCollection updatedLinkCollection = new LinkCollection();
        updatedLinkCollection.setLinkCollectionId(desiredLinkCollection.getLinkCollectionId());
        updatedLinkCollection.setContentBoxId(desiredLinkCollection.getContentBoxId());
        updatedLinkCollection.setPosition(desiredLinkCollection.getPosition());

        updatedLinkCollection.setUrl(entity.getUrl());
        updatedLinkCollection.setText(entity.getText());

        // save the updated entity
        linkCollectionService.save(updatedLinkCollection);

        return ResponseEntity.ok(updatedLinkCollection); 
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
