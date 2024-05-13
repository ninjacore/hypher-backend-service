package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.dto.LinkOfLinkCollectionUpdate;
import io.hypher.backendservice.platformdata.model.LinkCollection;
import io.hypher.backendservice.platformdata.dto.LinkWithinCollection;
import io.hypher.backendservice.platformdata.model.LinkCollectionWithProfileId;
import io.hypher.backendservice.platformdata.model.LinkCollectionWithPositions;
import io.hypher.backendservice.platformdata.model.Profile;
import io.hypher.backendservice.platformdata.model.ContentBox;

import io.hypher.backendservice.platformdata.service.ContentBoxService;
import io.hypher.backendservice.platformdata.service.LinkCollectionService;
import io.hypher.backendservice.platformdata.service.ProfileService;
import io.hypher.backendservice.platformdata.utillity.error.DatabaseException;
import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;

import javax.swing.text.html.Option;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Data;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Error;
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


@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
// @CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping("/linkCollection")
    public Optional<List<LinkWithinCollection>> createCollectionByHandle(
        @RequestParam String handle, 
        @RequestParam String contentBoxPosition,
        @RequestBody List<LinkWithinCollection> listOfFrontendLinkDTOs
        ) 
        throws ResourceNotFoundException, DatabaseException{
        
        // find profile by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // TODO: show error if already a content box at this position for this profile
        // contentBoxService.findByPositionAndProfileId
        
        // 1. create content box entry
        ContentBox contentBox = new ContentBox();
        contentBox.setContentBoxPosition(contentBoxPosition);
        contentBox.setProfileId(profileId);
        ContentBox newContentBoxEntry = contentBoxService.save(contentBox).orElseThrow(() -> new DatabaseException("Could not create content box entry"));

        // 2. create link collection entries
        List<LinkCollection> linkCollections = new ArrayList<>();
        for (LinkWithinCollection link : listOfFrontendLinkDTOs) {
            LinkCollection linkCollection = new LinkCollection();

            linkCollection.setContentBoxId(newContentBoxEntry.getContentBoxId());
            linkCollection.setFrontendId(link.getFrontendId());
            linkCollection.setPosition(link.getPosition());
            linkCollection.setUrl(link.getUrl());
            linkCollection.setText(link.getText());

            linkCollections.add(linkCollection);
        }

        // return linkCollectionService.saveAll(linkCollections);

        // prep and return created collection
        List<LinkWithinCollection> listForClient = new ArrayList<>();


        for(Integer counter = 0; counter < linkCollections.size(); counter++) {
            LinkCollection link = linkCollections.get(counter);
            LinkWithinCollection entity = new LinkWithinCollection();

            entity.setFrontendId(link.getFrontendId());
            entity.setUrl(link.getUrl());
            entity.setText(link.getText());
            entity.setPosition(link.getPosition());
            // entity.setUniqueId(Long.valueOf(counter));
            entity.setUniqueId(UUID.randomUUID().toString()); // only for frontend use
            
            listForClient.add(entity);
        }
        
        // sort by position in descending order
        listForClient.sort((LinkWithinCollection a, LinkWithinCollection b) -> a.getPosition().compareTo(b.getPosition()));        
        
        return Optional.of(
            listForClient
        );

    }

    @PostMapping("/linkCollection/link")
    public Optional<LinkWithinCollection> createLinkByHandle(
        @RequestParam String handle, 
        @RequestParam String contentBoxPosition,
        @RequestBody LinkWithinCollection frontendLinkDTO
        )
    throws ResourceNotFoundException, DatabaseException{
            
        // find user by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        List<ContentBox> matchingContentBoxes = new ArrayList<>();
        try {
            // find contentboxId by position of linkedCollection
            matchingContentBoxes = contentBoxService.findByPosition(contentBoxPosition).orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));

        } catch (ResourceNotFoundException e) {
            // if doesn't exist, create content box, then linkCollection
            ContentBox contentBox = new ContentBox();
            contentBox.setContentBoxPosition(contentBoxPosition);
            contentBox.setProfileId(profileId);
            ContentBox newContentBoxEntry = contentBoxService.save(contentBox).orElseThrow(() -> new DatabaseException("Could not create content box entry"));

            // create new linkCollection entry for that content box id
            LinkCollection linkCollection = new LinkCollection();

            linkCollection.setContentBoxId(newContentBoxEntry.getContentBoxId());
            linkCollection.setFrontendId(frontendLinkDTO.getFrontendId());
            linkCollection.setPosition(frontendLinkDTO.getPosition());
            linkCollection.setUrl(frontendLinkDTO.getUrl());
            linkCollection.setText(frontendLinkDTO.getText());

            // return Optional.of(linkCollectionService.save(linkCollection));    
                
            // save the new linkCollection entry
            LinkCollection createdLink = linkCollectionService.save(linkCollection).orElseThrow(() -> new DatabaseException("Could not create link collection entry"));

            // prep and return created collection
            LinkWithinCollection linkForClient = new LinkWithinCollection();
            linkForClient.setFrontendId(createdLink.getFrontendId());
            linkForClient.setUrl(createdLink.getUrl());
            linkForClient.setText(createdLink.getText());
            linkForClient.setPosition(createdLink.getPosition());
            linkForClient.setUniqueId(UUID.randomUUID().toString()); // only for frontend use

            return Optional.of(
                linkForClient
            );


        }

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
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
        Integer numberOfEntries;
        if(linkCollections.size() > 0) {
            numberOfEntries = linkCollections.size();
        }else {

            // if first addition, create new linkCollection entry for that content box id
            LinkCollection linkCollection = new LinkCollection();

            linkCollection.setContentBoxId(contentBoxId);
            linkCollection.setFrontendId(frontendLinkDTO.getFrontendId());
            linkCollection.setPosition(frontendLinkDTO.getPosition());
            linkCollection.setUrl(frontendLinkDTO.getUrl());
            linkCollection.setText(frontendLinkDTO.getText());

            LinkCollection createdLink = linkCollectionService.save(linkCollection).orElseThrow(() -> new DatabaseException("Could not create link collection entry"));

            // prep and return created collection
            LinkWithinCollection linkForClient = new LinkWithinCollection();
            linkForClient.setFrontendId(createdLink.getFrontendId());
            linkForClient.setUrl(createdLink.getUrl());
            linkForClient.setText(createdLink.getText());
            linkForClient.setPosition(createdLink.getPosition());
            linkForClient.setUniqueId(UUID.randomUUID().toString()); // only for frontend use
    
            return Optional.of(
                linkForClient
            );

        }

        // create new linkCollection entry for that content box id
        LinkCollection linkCollection = new LinkCollection();
        linkCollection.setContentBoxId(contentBoxId);
        linkCollection.setFrontendId(frontendLinkDTO.getFrontendId());
        // linkCollection.setPosition(numberOfEntries);
        linkCollection.setPosition(frontendLinkDTO.getPosition());
        linkCollection.setUrl(frontendLinkDTO.getUrl());
        linkCollection.setText(frontendLinkDTO.getText());

        // save the new linkCollection entry
        // return Optional.of(linkCollectionService.save(linkCollection));
        LinkCollection createdLink = linkCollectionService.save(linkCollection).orElseThrow(() -> new DatabaseException("Could not create link collection entry"));

        // prep and return created collection
        LinkWithinCollection linkForClient = new LinkWithinCollection();
        linkForClient.setFrontendId(createdLink.getFrontendId());
        linkForClient.setUrl(createdLink.getUrl());
        linkForClient.setText(createdLink.getText());
        linkForClient.setPosition(createdLink.getPosition());
        linkForClient.setUniqueId(UUID.randomUUID().toString()); // only for frontend use

        return Optional.of(
            linkForClient
        );
    }


    @GetMapping("/linkCollection")
    public List<LinkWithinCollection> getByProfileHandle(
        @RequestParam String handle,
        @RequestParam Optional<Integer> contentBoxPosition
    )
    throws ResourceNotFoundException{

        List<LinkWithinCollection> linkCollectionDTO = new ArrayList<>();

        Integer positionOfContentBox = contentBoxPosition.orElse(null);
        if(positionOfContentBox == null){
            // assume default link collection position of '0'
            // TODO: use new View

            // find user by handle
            Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
            Profile profile = profiles.iterator().next();
            UUID profileId = profile.getProfileId();

            // find linkCollections by profileId
            // return linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
            List<LinkCollectionWithProfileId> view = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
            
            for (LinkCollectionWithProfileId linkCollectionView : view) {
                LinkWithinCollection entity = new LinkWithinCollection();

                entity.setFrontendId(linkCollectionView.getFrontendId());
                entity.setUrl(linkCollectionView.getUrl());
                entity.setText(linkCollectionView.getText());
                entity.setPosition(linkCollectionView.getPosition());
                // entity.setUniqueId(linkCollectionView.getUniqueId());
                entity.setUniqueId(UUID.randomUUID().toString()); // only for frontend use
                
                linkCollectionDTO.add(entity);
            }
        }else{
            // future feat: customizable position for context boxes
            // meanwhile: used for new View that goes by positon

            // find link collection at this position
            List<LinkCollectionWithPositions> view = linkCollectionService.findByHandleAndPosition(handle, positionOfContentBox).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
            
            for (LinkCollectionWithPositions linkCollectionView : view) {
                LinkWithinCollection entity = new LinkWithinCollection();
                entity.setFrontendId(linkCollectionView.getFrontendId());
                entity.setUrl(linkCollectionView.getUrl());
                entity.setText(linkCollectionView.getText());
                entity.setPosition(linkCollectionView.getPosition());
                // entity.setUniqueId(linkCollectionView.getUniqueId());
                entity.setUniqueId(UUID.randomUUID().toString()); // only for frontend use
                
                linkCollectionDTO.add(entity);
            }
            
        }

        // return linkCollectionDTO;

        // sort by position (descending)
        linkCollectionDTO.sort((LinkWithinCollection a, LinkWithinCollection b) -> a.getPosition().compareTo(b.getPosition()));        

        return linkCollectionDTO;



    }

    @PutMapping("/linkCollection/update")
    public Optional<List<LinkWithinCollection>> updateLinkCollectionByHandle(
        @RequestParam String handle, 
        @RequestParam String contentBoxPosition,
        @RequestBody List<LinkWithinCollection> listOfFrontendLinkDTOs
    )
    throws ResourceNotFoundException, DatabaseException{

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
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
        Integer numberOfEntries;
        if(linkCollections.size() > 0 ) {   
            numberOfEntries = linkCollections.size();
        }else {
            throw new ResourceNotFoundException("LinkCollection not found");
        }


        // prepare bulk update
        List<LinkCollection> updatedLinkCollections = new ArrayList<>();
        for (LinkWithinCollection link : listOfFrontendLinkDTOs) {
            
            // create the updatd linkCollection entry for that content box id
            LinkCollection linkCollectionEntry = new LinkCollection();
            // linkCollectionEntry.setLinkCollectionId(linkCollectionToUpdate.getLinkCollectionId());
            linkCollectionEntry.setFrontendId(link.getFrontendId());
            linkCollectionEntry.setContentBoxId(contentBoxId);
            linkCollectionEntry.setPosition(link.getPosition());
            linkCollectionEntry.setUrl(link.getUrl());
            linkCollectionEntry.setText(link.getText());

            updatedLinkCollections.add(linkCollectionEntry);
        }


        // all entries have to be deleted to avert inserts instead of updates
        linkCollectionService.deleteByContentBoxId(contentBoxId);

        // return linkCollectionService.saveAll(updatedLinkCollections);
        List<LinkCollection> updatedLinkCollection = linkCollectionService.saveAll(updatedLinkCollections).orElseThrow( () -> new DatabaseException("Could not update link collection"));
        
        // prep and return updated collection
        List<LinkWithinCollection> listForClient = new ArrayList<>();
        

        for(Integer counter = 0; counter < updatedLinkCollection.size(); counter++) {
            LinkCollection link = updatedLinkCollection.get(counter);
            LinkWithinCollection entity = new LinkWithinCollection();

            entity.setFrontendId(link.getFrontendId());
            entity.setUrl(link.getUrl());
            entity.setText(link.getText());
            entity.setPosition(link.getPosition());
            // entity.setUniqueId(Long.valueOf(counter));
            entity.setUniqueId(UUID.randomUUID().toString()); // only for frontend use
            
            listForClient.add(entity);
        }

        // sort by position in descending order
        listForClient.sort((LinkWithinCollection a, LinkWithinCollection b) -> a.getPosition().compareTo(b.getPosition()));        
        
        // return in descending order
        return Optional.of(
            listForClient
        );
    }


    @PutMapping("/linkCollection/link/update")
    public List<LinkWithinCollection> updateLinkByHandle(
        @RequestParam String handle, 
        @RequestParam String contentBoxPosition,
        @RequestBody LinkWithinCollection frontendLinkDTO
        ) 
    throws ResourceNotFoundException, DatabaseException{

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
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
        Integer numberOfEntries;
        if(linkCollections.size() > 0 ) {   
            numberOfEntries = linkCollections.size();
        }else {
            throw new ResourceNotFoundException("LinkCollection not found");
        }

        // check if the position is valid -- skipping for now since 'positions' aren't running from 0 atm
        // if(frontendLinkDTO.getPosition() > numberOfEntries-1) {
        //     String answer = "Link position is invalid. Number of entries = " + numberOfEntries;
        //     throw new ResourceNotFoundException(answer);
        // }

        // get the linkCollection entry to update
        LinkCollectionWithProfileId linkCollectionToUpdate = linkCollections.stream()
            .filter(lc -> lc.getPosition() == frontendLinkDTO.getPosition())
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("No position found. Ary you trying to add a new entry instead of updating one?")); 

        // create the updatd linkCollection entry for that content box id
        LinkCollection linkCollection = new LinkCollection();

        linkCollection.setLinkCollectionId(linkCollectionToUpdate.getLinkCollectionId());
        linkCollection.setContentBoxId(contentBoxId);
        linkCollection.setFrontendId(frontendLinkDTO.getFrontendId());
        linkCollection.setPosition(frontendLinkDTO.getPosition());
        linkCollection.setUrl(frontendLinkDTO.getUrl());
        linkCollection.setText(frontendLinkDTO.getText());

        // update link in this linkCollection
        try {
            // continue once the entry has been updated...
            LinkCollection updatedEntry = linkCollectionService.save(linkCollection).orElseThrow(() -> new DatabaseException("Could not update link"));

            // prep and return updated collection
            List<LinkCollectionWithProfileId> updatedLinkCollection = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("Updated LinkCollection not found"));

            List<LinkWithinCollection> listForClient = new ArrayList<>();
            updatedLinkCollection.forEach(link -> {

                LinkWithinCollection entity = new LinkWithinCollection();

                // to ensure we send back the updated entry
                if(link.getLinkCollectionId() == updatedEntry.getLinkCollectionId()){
                    
                    entity.setFrontendId(updatedEntry.getFrontendId());
                    entity.setUrl(updatedEntry.getUrl());
                    entity.setText(updatedEntry.getText());
                    entity.setPosition(updatedEntry.getPosition());
                    // entity.setUniqueId(link.getUniqueId());
                    entity.setUniqueId(UUID.randomUUID().toString()); // only for frontend use

                }else{
                    entity.setFrontendId(link.getFrontendId());
                    entity.setUrl(link.getUrl());
                    entity.setText(link.getText());
                    entity.setPosition(link.getPosition());
                    // entity.setUniqueId(link.getUniqueId());
                    entity.setUniqueId(UUID.randomUUID().toString()); // only for frontend use

                }
                
                listForClient.add(entity);
            });

            
            return listForClient;


        } catch (Exception e) {
            throw new DatabaseException("Could not update link");
        }


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
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
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
        linkCollection.setFrontendId(linkCollectionDTO.getFrontendId());
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
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        if(linkCollections.isEmpty()) {
            throw new ResourceNotFoundException("LinkCollection not found");
        } else {
            List<LinkOfLinkCollectionUpdate> linkCollectionDTO = new ArrayList<>();
            for (LinkCollectionWithProfileId linkCollectionView : linkCollections) {
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
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        // get the one with the right position
        LinkCollectionWithProfileId desiredLinkCollection = linkCollections.stream()
            .filter(lc -> lc.getPosition() == Integer.parseInt(position))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        // update the entity
        LinkCollection updatedLinkCollection = new LinkCollection();
        updatedLinkCollection.setLinkCollectionId(desiredLinkCollection.getLinkCollectionId());
        updatedLinkCollection.setContentBoxId(desiredLinkCollection.getContentBoxId());
        updatedLinkCollection.setPosition(desiredLinkCollection.getPosition());

        updatedLinkCollection.setFrontendId(entity.getFrontendId());
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

    @DeleteMapping("/linkCollection/link/delete")
    public LinkWithinCollection deleteByHandleAndFrontendId(
        @RequestParam String handle, 
        @RequestParam String frontendId
    )
    throws ResourceNotFoundException, DatabaseException{

        // find profile by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find linkCollections by profileId
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("No LinkCollection found for this profile"));

        // get the one with the right frontendId and handle (by profileId)
        LinkCollectionWithProfileId linkCollectionToDelete = linkCollections.stream()
            .filter(lc -> lc.getFrontendId().equals(frontendId))
            .filter(lc -> lc.getProfileId().equals(profileId))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("No link found with this frontendId"));

        
        LinkCollection actualLinkCollectionToDelete = linkCollectionService.findById(linkCollectionToDelete.getLinkCollectionId()).orElseThrow(() -> new ResourceNotFoundException("Cannot delete: LinkCollection not found"));

        // Perform deletion and return result
        Boolean linkGotDeleted = linkCollectionService.delete(actualLinkCollectionToDelete);
        if(linkGotDeleted){
            LinkWithinCollection deletedLink = new LinkWithinCollection();

            deletedLink.setFrontendId(actualLinkCollectionToDelete.getFrontendId());
            deletedLink.setUrl(actualLinkCollectionToDelete.getUrl());
            deletedLink.setText(actualLinkCollectionToDelete.getText());
            deletedLink.setPosition(actualLinkCollectionToDelete.getPosition());
            
            return deletedLink;
        }else{
            throw new DatabaseException("Could not delete link");
        } 


    }

    @DeleteMapping("/linkCollection/link")
    public LinkWithinCollection deleteLinkByHandle(
        @RequestParam String handle, 
        @RequestParam String position
    )
    throws ResourceNotFoundException, DatabaseException{

        // find profile by handle
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Profile profile = profiles.iterator().next();
        UUID profileId = profile.getProfileId();

        // find linkCollections by profileId
        List<LinkCollectionWithProfileId> linkCollections = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("No LinkCollection found for this profile"));

        // get the one with the right position
        LinkCollectionWithProfileId linkCollectionToDelete = linkCollections.stream()
            .filter(lc -> lc.getPosition() == Integer.parseInt(position))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("No link found at this position"));

        LinkCollection actualLinkCollectionToDelete = linkCollectionService.findById(linkCollectionToDelete.getLinkCollectionId()).orElseThrow(() -> new ResourceNotFoundException("Cannot delete: LinkCollection not found"));


        // return linkCollectionService.delete(actualLinkCollectionToDelete);
        Boolean linkGotDeleted = linkCollectionService.delete(actualLinkCollectionToDelete);
        if(linkGotDeleted){
            LinkWithinCollection deletedLink = new LinkWithinCollection();

            deletedLink.setFrontendId(actualLinkCollectionToDelete.getFrontendId());
            deletedLink.setUrl(actualLinkCollectionToDelete.getUrl());
            deletedLink.setText(actualLinkCollectionToDelete.getText());
            deletedLink.setPosition(actualLinkCollectionToDelete.getPosition());
            
            return deletedLink;
        }else{
            throw new DatabaseException("Could not delete link");
        } 
    }


    @DeleteMapping("/linkCollections/{id}")
    public String delete(@PathVariable(value = "id") UUID linkCollectionId) throws ResourceNotFoundException, DatabaseException{

        LinkCollection linkCollectionToDelete = linkCollectionService.findById(linkCollectionId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        // return linkCollectionService.delete(linkCollectionToDelete);
        Boolean linkGotDeleted = linkCollectionService.delete(linkCollectionToDelete);
        if(linkGotDeleted){
            return "{}";
        }else{
            throw new DatabaseException("Could not delete link");
        } 

    }
    
    
    
}
