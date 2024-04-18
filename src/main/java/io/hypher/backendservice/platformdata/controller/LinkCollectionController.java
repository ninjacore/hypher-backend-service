package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.dto.LinkOfLinkCollectionUpdate;
import io.hypher.backendservice.platformdata.model.LinkCollection;
import io.hypher.backendservice.platformdata.dto.LinkWithinCollection;
import io.hypher.backendservice.platformdata.model.LinkCollectionView;
import io.hypher.backendservice.platformdata.model.Profile;
import io.hypher.backendservice.platformdata.model.ContentBox;

import io.hypher.backendservice.platformdata.service.ContentBoxService;
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

    @Autowired
    private ContentBoxService contentBoxService;

    @PostMapping("/linkCollections")
    public Optional<LinkCollection> create(@RequestBody LinkCollection linkCollection) {
        return linkCollectionService.save(linkCollection);        
    }

    @PostMapping("/linkCollection/link")
    public Optional<LinkCollection> createLinkByHandle(
        @RequestParam String handle, 
        @RequestParam String contentBoxPosition,
        @RequestBody LinkWithinCollection frontendLinkDTO
        )
    throws ResourceNotFoundException{
            
        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find contentboxId by position of linkedCollection
        List<ContentBox> matchingContentBoxes = contentBoxService.findByPosition(contentBoxPosition).orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));

        List<ContentBox> targetContentBoxes = new ArrayList<>();

        matchingContentBoxes.forEach(box -> {
            if(box.getProfileId().equals(profileId)) {
                targetContentBoxes.add(box);
            }
        });

        if(targetContentBoxes.size() > 1){
            throw new ResourceNotFoundException("More than one matching content box found");
        }
        
        UUID contentBoxId = targetContentBoxes.get(0).getContentBoxId();

        // verify the linkcollection exists and get the number of entries
        List<LinkCollectionView> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
        Integer numberOfEntries;
        if(linkCollections.size() > 0) {
            numberOfEntries = linkCollections.size();
        }else {
            throw new ResourceNotFoundException("LinkCollection not found");
        }

        // create new linkCollection entry for that content box id
        LinkCollection linkCollection = new LinkCollection();
        linkCollection.setContentBoxId(contentBoxId);
        linkCollection.setPosition(numberOfEntries);
        linkCollection.setUrl(frontendLinkDTO.getUrl());
        linkCollection.setText(frontendLinkDTO.getText());

        // save the new linkCollection entry
        return linkCollectionService.save(linkCollection);
    }


    @GetMapping("/linkCollection")
    public List<LinkWithinCollection> getByProfileHandle(
        @RequestParam String handle,
        @RequestParam Optional<String> contentBoxPosition
    )
    throws ResourceNotFoundException{

        List<LinkWithinCollection> linkCollectionDTO = new ArrayList<>();

        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();


        String positionOfContentBox = contentBoxPosition.orElse(null);
        if(positionOfContentBox == null){
            // assume default link collection...
        

            // find linkCollections by profileId
            // return linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
            List<LinkCollectionView> view = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
            
            for (LinkCollectionView linkCollectionView : view) {
                LinkWithinCollection entity = new LinkWithinCollection();
                entity.setUrl(linkCollectionView.getUrl());
                entity.setText(linkCollectionView.getText());
                entity.setPosition(linkCollectionView.getPosition());
                
                linkCollectionDTO.add(entity);
            }
        }else{
            // future feat: customizable position for context boxes
            // find link collection at this position
        }

        return linkCollectionDTO;

    }


    @PutMapping("/linkCollection/link/update")
    public Optional<LinkCollection> updateLinkByHandle(
        @RequestParam String handle, 
        @RequestParam String contentBoxPosition,
        @RequestBody LinkWithinCollection frontendLinkDTO
        ) 
    throws ResourceNotFoundException{

        // find profile by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find contentboxId by position of linkedCollection
        List<ContentBox> matchingContentBoxes = contentBoxService.findByPosition(contentBoxPosition).orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));   

        // filter for contentBoxes that belong to this profile
        List<ContentBox> targetContentBoxes = new ArrayList<>();
        matchingContentBoxes.forEach(box -> {
            if(box.getProfileId().equals(profileId)) {
                targetContentBoxes.add(box);
            }
        });
        if(targetContentBoxes.size() > 1){
            throw new ResourceNotFoundException("More than one matching content box found");
        }

        // basic mode: only update the first matching content box
        UUID contentBoxId = targetContentBoxes.get(0).getContentBoxId();

        // verify the linkcollection exists and get the number of entries
        List<LinkCollectionView> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
        Integer numberOfEntries;
        if(linkCollections.size() > 0 ) {   
            numberOfEntries = linkCollections.size();
        }else {
            throw new ResourceNotFoundException("LinkCollection not found");
        }

        // check if the position is valid
        if(frontendLinkDTO.getPosition() > numberOfEntries-1) {
            throw new ResourceNotFoundException("Link position is invalid");
        }

        // get the linkCollection entry to update
        LinkCollectionView linkCollectionToUpdate = linkCollections.stream()
            .filter(lc -> lc.getPosition() == frontendLinkDTO.getPosition())
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("No position found. Ary you trying to add a new entry instead of updating one?")); 

        // create the updatd linkCollection entry for that content box id
        LinkCollection linkCollection = new LinkCollection();
        linkCollection.setLinkCollectionId(linkCollectionToUpdate.getLinkCollectionId());
        linkCollection.setContentBoxId(contentBoxId);
        linkCollection.setPosition(frontendLinkDTO.getPosition());
        linkCollection.setUrl(frontendLinkDTO.getUrl());
        linkCollection.setText(frontendLinkDTO.getText());

        // update link in this linkCollection
        return linkCollectionService.save(linkCollection);
    }



    @PutMapping("/linkCollections/byHandle/{handle}")
    public Optional<LinkCollection> createByHandle(
        @PathVariable(value = "handle") String handle, 
        @RequestParam String contentBoxPosition,
        @RequestBody LinkOfLinkCollectionUpdate linkCollectionDTO
        ) 
    throws ResourceNotFoundException{

        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find contentboxId by position of linkedCollection
        List<ContentBox> matchingContentBoxes = contentBoxService.findByPosition(contentBoxPosition).orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));

        List<ContentBox> targetContentBoxes = new ArrayList<>();

        matchingContentBoxes.forEach(box -> {
            if(box.getProfileId().equals(profileId)) {
                targetContentBoxes.add(box);
            }
        });

        // TODO: better Exception type
        if(targetContentBoxes.size() > 1){
            throw new ResourceNotFoundException("More than one matching content box found");
        }
        
        UUID contentBoxId = targetContentBoxes.get(0).getContentBoxId();


        // verify the linkcollection exists and get the number of entries
        List<LinkCollectionView> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
        Integer numberOfEntries;
        if(linkCollections.size() > 0) {
            numberOfEntries = linkCollections.size();
        }else {
            throw new ResourceNotFoundException("LinkCollection not found");
        }


        // create new linkCollection entry for that content box id
        LinkCollection linkCollection = new LinkCollection();
        // UUID linkCollectionId = UUID.randomUUID();
        // linkCollection.setLinkCollectionId(linkCollectionId);
        linkCollection.setContentBoxId(contentBoxId);
        linkCollection.setPosition(numberOfEntries);
        linkCollection.setUrl(linkCollectionDTO.getUrl());
        linkCollection.setText(linkCollectionDTO.getText());

        // save the new linkCollection entry
        return linkCollectionService.save(linkCollection);

        //////////////////// OLD CODE ///////////////////

        // set profileId
        // linkCollection.setProfileId(profileId);

        // LinkCollection linkCollection = new LinkCollection();
        // linkCollection.setProfileId(profileId);
        // linkCollection.setUrl(linkCollectionDTO.getUrl());
        // linkCollection.setText(linkCollectionDTO.getText());
        
        // linkCollection.setPosition(linkCollectionDTO.getPosition());
        
        // // TODO: get contentboxId by position
        // linkCollection.setContentBoxId(linkCollectionDTO.getContentBoxId());
        

        // return linkCollectionService.save(linkCollection);        
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
    public List<LinkOfLinkCollectionUpdate> getByHandle(@PathVariable(value = "handle") String handle) throws ResourceNotFoundException{

        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find linkCollections by profileId
        List<LinkCollectionView> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        if(linkCollections.isEmpty()) {
            throw new ResourceNotFoundException("LinkCollection not found");
        } else {
            List<LinkOfLinkCollectionUpdate> linkCollectionDTO = new ArrayList<>();
            for (LinkCollectionView linkCollectionView : linkCollections) {
                LinkOfLinkCollectionUpdate entity = new LinkOfLinkCollectionUpdate();
                entity.setUrl(linkCollectionView.getUrl());
                entity.setText(linkCollectionView.getText());
                
                linkCollectionDTO.add(entity);
            }
            
            return linkCollectionDTO;
        }


    }

    @PutMapping("/linkCollections/{handle}/update")
    public ResponseEntity<LinkCollection> updateByHandle(
        @PathVariable (value = "handle") String handle, 
        @RequestParam String position, 
        @RequestBody LinkOfLinkCollectionUpdate entity
    ) throws ResourceNotFoundException, WrongBodyException{

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

    @DeleteMapping("/linkCollection/link")
    public String deleteLinkByHandle(
        @RequestParam String handle, 
        @RequestParam String position
    )
    throws ResourceNotFoundException{

        // find profile by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find linkCollections by profileId
        List<LinkCollectionView> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("No LinkCollection found for this profile"));

        // get the one with the right position
        LinkCollectionView linkCollectionToDelete = linkCollections.stream()
            .filter(lc -> lc.getPosition() == Integer.parseInt(position))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("No link found at this position"));

        LinkCollection actualLinkCollectionToDelete = linkCollectionService.findById(linkCollectionToDelete.getLinkCollectionId()).orElseThrow(() -> new ResourceNotFoundException("Cannot delete: LinkCollection not found"));


        return linkCollectionService.delete(actualLinkCollectionToDelete);
        }


    @DeleteMapping("/linkCollections/{id}")
    public String delete(@PathVariable(value = "id") UUID linkCollectionId) throws ResourceNotFoundException{

        LinkCollection linkCollectionToDelete = linkCollectionService.findById(linkCollectionId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        return linkCollectionService.delete(linkCollectionToDelete);
    }
    
    
    
}
