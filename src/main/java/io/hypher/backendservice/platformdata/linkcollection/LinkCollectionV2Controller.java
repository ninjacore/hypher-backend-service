package io.hypher.backendservice.platformdata.linkcollection;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.dto.LinkOfLinkCollectionUpdate;
import io.hypher.backendservice.platformdata.dto.LinkWithinCollection;
import io.hypher.backendservice.platformdata.model.LinkCollection;
import io.hypher.backendservice.platformdata.linkcollection.LinkWithinCollectionDTO;
import io.hypher.backendservice.platformdata.model.LinkCollectionWithProfileId;
import io.hypher.backendservice.platformdata.model.LinkCollectionWithPositions;
import io.hypher.backendservice.platformdata.model.Profile;
import io.hypher.backendservice.platformdata.model.ContentBox;

// TODO: replace with domain service
import io.hypher.backendservice.platformdata.service.ContentBoxService;

import io.hypher.backendservice.platformdata.linkcollection.LinkCollectionV2Service;


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
@RestController
@RequestMapping("/api/v2")
public class LinkCollectionV2Controller{

    @Autowired
    private LinkCollectionV2Service linkCollectionService;

    @Autowired
    private ProfileService profileService;    

    @Autowired
    private ContentBoxService contentBoxService;


    // CRUD functions /.
    @PostMapping("/linkCollection/link")
    public ResponseEntity<LinkWithinCollectionDTO> createLinkByHandle(
        @RequestParam String handle, 
        @RequestParam(required = false) String contentBoxPosition,
        @RequestBody LinkWithinCollectionDTO frontendLinkDTO
        )
    throws ResourceNotFoundException, DatabaseException{
        
        // check optional parameters
        if(contentBoxPosition == null){
            contentBoxPosition = "0"; // default
        }

        // find profile for given handle
        UUID profileId;
        try {
            profileId = getProfileIdFromHandle(handle);
        } catch (ResourceNotFoundException e) {
            throw new DatabaseException(e.getMessage());
        }

        // find content box
        UUID contentBoxId;
        try {
            contentBoxId = getContentBoxId(profileId, contentBoxPosition);
        } catch (ResourceNotFoundException e) {
            
            ContentBox newContentBox = createContentBox(profileId, contentBoxPosition);
            contentBoxId = newContentBox.getContentBoxId();
        }

        // prepare linkCollection entry
        LinkCollection linkCollection = new LinkCollection();
        linkCollection.setContentBoxId(contentBoxId);
        linkCollection.setFrontendId(frontendLinkDTO.getFrontendId());
        linkCollection.setPosition(frontendLinkDTO.getPosition());
        linkCollection.setUrl(frontendLinkDTO.getUrl());
        linkCollection.setText(frontendLinkDTO.getText());

        // create database entry
        LinkCollection createdLink = linkCollectionService.save(linkCollection).orElseThrow(() -> new DatabaseException("Could not create link collection entry"));

        LinkWithinCollectionDTO linkForClient = getDtoFromLinkCollection(createdLink);

        return ResponseEntity.ok(linkForClient);
    }

    @GetMapping("/linkCollection")
    public ResponseEntity<List<LinkWithinCollectionDTO>> getLinkCollectionByHandle(
        @RequestParam String handle
    )
    throws DatabaseException, ResourceNotFoundException
    {
        
        // find profile for given handle
        UUID profileId;
        try {
            profileId = getProfileIdFromHandle(handle);
        } catch (ResourceNotFoundException e) {
            throw new DatabaseException(e.getMessage());
        }

        // find linkCollection entries
        List<LinkCollectionWithProfileId> extendedLinkCollection = linkCollectionService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));
        
        List<LinkWithinCollectionDTO> linkCollectionDtoForClient = new ArrayList<>();
        for (LinkCollectionWithProfileId entry : extendedLinkCollection) {
            LinkWithinCollectionDTO linkForClient = getDtoFromLinkCollectionWithProfileId(entry);
            linkCollectionDtoForClient.add(linkForClient);
        }

        // sort by position (descending)
        linkCollectionDtoForClient.sort((LinkWithinCollectionDTO a, LinkWithinCollectionDTO b) -> a.getPosition().compareTo(b.getPosition()));        

        return ResponseEntity.ok(linkCollectionDtoForClient);
    }
    // CRUD functions ./


    // Data Support Functions /.
    private UUID getProfileIdFromHandle(
        String handle
    )
    throws ResourceNotFoundException
    {
        Collection<Profile> profiles = profileService.findByHandle(handle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile profile = profiles.iterator().next();
        return profile.getProfileId();

    }

    private UUID getContentBoxId(
        UUID profileId,
        String contentBoxPosition
    )
    throws DatabaseException, ResourceNotFoundException
    {

        List<ContentBox> matchingContentBoxes = new ArrayList<>();

        // find contentboxId by position of linkedCollection
        matchingContentBoxes = contentBoxService.findByPosition(contentBoxPosition).orElseThrow(() -> new ResourceNotFoundException("ContentBox not found"));

        // check for profile id
        List<ContentBox> validContentBoxes = new ArrayList<>();
        matchingContentBoxes.forEach(box -> {
            if(box.getProfileId().equals(profileId)) {
                validContentBoxes.add(box);
            }
        });

        if(validContentBoxes.isEmpty()){
            throw new ResourceNotFoundException("No matching content box found");
        }
        
        // always go with the first eligible content box
        return validContentBoxes.get(0).getContentBoxId();
    }

    private ContentBox createContentBox(
        UUID profileId,
        String contentBoxPosition
    )
    throws DatabaseException
    {
        ContentBox contentBox = new ContentBox();
        contentBox.setContentBoxPosition(contentBoxPosition);
        contentBox.setProfileId(profileId);
        return contentBoxService.save(contentBox)
        .orElseThrow(() -> new DatabaseException("Could not create content box entry"));
    }

    private LinkWithinCollectionDTO getDtoFromLinkCollection(LinkCollection linkCollectionEntry){
        
        // prep and return created collection
        LinkWithinCollectionDTO linkForClient = new LinkWithinCollectionDTO();
        linkForClient.setFrontendId(linkCollectionEntry.getFrontendId());
        linkForClient.setUrl(linkCollectionEntry.getUrl());
        linkForClient.setText(linkCollectionEntry.getText());
        linkForClient.setPosition(linkCollectionEntry.getPosition());

        return linkForClient;
    }

    private LinkWithinCollectionDTO getDtoFromLinkCollectionWithProfileId(LinkCollectionWithProfileId linkCollectionEntry){
        
        // prep and return created collection
        LinkWithinCollectionDTO linkForClient = new LinkWithinCollectionDTO();
        linkForClient.setFrontendId(linkCollectionEntry.getFrontendId());
        linkForClient.setUrl(linkCollectionEntry.getUrl());
        linkForClient.setText(linkCollectionEntry.getText());
        linkForClient.setPosition(linkCollectionEntry.getPosition());

        return linkForClient;
    }

    // Data Support Functions ./
}


