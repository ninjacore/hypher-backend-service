package io.hypher.backendservice.platformdata.linkcollection;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.linkcollection.dto.LinkWithinCollection;
import io.hypher.backendservice.platformdata.linkcollection.model.LinkCollection;
import io.hypher.backendservice.platformdata.linkcollection.model.LinkCollectionWithProfileId;
import io.hypher.backendservice.platformdata.model.Profile;
import io.hypher.backendservice.platformdata.model.ContentBox;

// TODO: replace with domain service
import io.hypher.backendservice.platformdata.service.ContentBoxService;

import io.hypher.backendservice.platformdata.service.ProfileService;
import io.hypher.backendservice.platformdata.utillity.error.DatabaseException;
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


    // api endpoints /.
    @GetMapping("/linkCollection")
    public ResponseEntity<List<LinkWithinCollection>> getLinkCollectionByHandle(
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
        List<LinkCollectionWithProfileId> extendedLinkCollection =
            linkCollectionService.findByProfileId(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        List<LinkWithinCollection> linkCollectionDtoForClient = new ArrayList<>();
        for (LinkCollectionWithProfileId entry : extendedLinkCollection) {
            LinkWithinCollection linkForClient = getDtoFromLinkCollectionWithProfileId(entry);
            linkCollectionDtoForClient.add(linkForClient);
        }

        // sort by position (descending)
        linkCollectionDtoForClient.sort((LinkWithinCollection a, LinkWithinCollection b) -> a.getPosition().compareTo(b.getPosition()));

        return ResponseEntity.ok(linkCollectionDtoForClient);
    }

    @PostMapping("/linkCollection/link")
    public ResponseEntity<LinkWithinCollection> createLinkByHandle(
        @RequestParam String handle,
        @RequestParam(required = false) String contentBoxPosition,
        @RequestBody LinkWithinCollection frontendLinkDTO
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
        LinkCollection createdLink = linkCollectionService.save(linkCollection)
            .orElseThrow(() -> new DatabaseException("Could not create link collection entry"));

        LinkWithinCollection linkForClient = getDtoFromLinkCollection(createdLink);

        return ResponseEntity.ok(linkForClient);
    }

    @PutMapping("/linkCollection/update")
    public ResponseEntity<List<LinkWithinCollection>> updateLinkCollectionByHandle
    (
        @RequestParam String handle,
        @RequestParam(required = false) String contentBoxPosition,
        @RequestBody List<LinkWithinCollection> listOfFrontendLinkDTOs
    )
    throws DatabaseException, ResourceNotFoundException
    {
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

        // find content box (if none is found a 404 will be thrown)
        UUID contentBoxId = getContentBoxId(profileId, contentBoxPosition);

        // prepare bulk update
        List<LinkCollection> linkCollectionUpdate = new ArrayList<>();
        for (LinkWithinCollection link : listOfFrontendLinkDTOs) {

            // create the updatd linkCollection entry for that content box id
            LinkCollection linkCollectionEntry = new LinkCollection();
            linkCollectionEntry.setFrontendId(link.getFrontendId());
            linkCollectionEntry.setContentBoxId(contentBoxId);
            linkCollectionEntry.setPosition(link.getPosition());
            linkCollectionEntry.setUrl(link.getUrl());
            linkCollectionEntry.setText(link.getText());

            linkCollectionUpdate.add(linkCollectionEntry);
        }

        // all entries have to be deleted to avert inserts instead of updates
        List<LinkCollection> deletedRecords = linkCollectionService
            .deleteAllByContentBoxId(contentBoxId)
            .orElseThrow(
                () -> new DatabaseException("Could not complete link collection update")
            );
        // Note: if switching to multiple LinkCollections per user this won't be feasible anymore

        // perform update
        List<LinkCollection> updatedLinkCollection = linkCollectionService
            .saveAll(linkCollectionUpdate)
            .orElseThrow( () -> new DatabaseException("Could not update link collection"));

        // prepare response
        List<LinkWithinCollection> listForClient = new ArrayList<>();

        updatedLinkCollection.forEach(entry -> {
            LinkWithinCollection linkForClient = getDtoFromLinkCollection(entry);
            listForClient.add(linkForClient);
        });

        // sort by position in descending order
        listForClient.sort((LinkWithinCollection a, LinkWithinCollection b) -> a.getPosition().compareTo(b.getPosition()));

        return ResponseEntity.ok(listForClient);
    }

    @PutMapping("/linkCollection/link/update")
    public ResponseEntity<LinkWithinCollection> updateLinkByHandle(
        @RequestParam String handle,
        @RequestParam(required = false) String contentBoxPosition,
        @RequestBody LinkWithinCollection frontendLinkDTO
    )
    throws DatabaseException, ResourceNotFoundException
    {

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

        // find content box (if none is found a 404 will be thrown)
        UUID contentBoxId = getContentBoxId(profileId, contentBoxPosition);

        // prepare linkCollection entry
        LinkCollection linkCollection = new LinkCollection();
        linkCollection.setContentBoxId(contentBoxId);
        linkCollection.setFrontendId(frontendLinkDTO.getFrontendId());
        linkCollection.setPosition(frontendLinkDTO.getPosition());
        linkCollection.setUrl(frontendLinkDTO.getUrl());
        linkCollection.setText(frontendLinkDTO.getText());

        // create database entry
        LinkCollection updatedLink = linkCollectionService.save(linkCollection)
            .orElseThrow(() -> new DatabaseException("Could not update link collection entry"));

        LinkWithinCollection linkForClient = getDtoFromLinkCollection(updatedLink);

        return ResponseEntity.ok(linkForClient);
    }

    @DeleteMapping("/linkCollection/link/delete")
    public ResponseEntity<LinkWithinCollection> deleteByHandleAndFrontendId(
        @RequestParam String handle,
        @RequestParam String frontendId
    )
    throws ResourceNotFoundException, DatabaseException{

        // find profile for given handle
        UUID profileId;
        try {
            profileId = getProfileIdFromHandle(handle);
        } catch (ResourceNotFoundException e) {
            throw new DatabaseException(e.getMessage());
        }

        // find linkCollections by profileId
        List<LinkCollectionWithProfileId> extendedLinkCollection =
            linkCollectionService.findByProfileId(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("LinkCollection not found"));

        // get the one with the right frontendId and handle (by profileId)
        LinkCollectionWithProfileId linkToDelete = extendedLinkCollection.stream()
            .filter(lc -> lc.getFrontendId().equals(frontendId))
            .filter(lc -> lc.getProfileId().equals(profileId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("No link found with this frontendId"));

        // prepare item for deletion
        LinkCollection linkCollectionToDelete = getLinkCollectionFromLinkCollectionWithProfileId(linkToDelete);

        // Perform deletion and return result
        Boolean linkGotDeleted = linkCollectionService.delete(linkCollectionToDelete);

        if(linkGotDeleted){
            LinkWithinCollection linkForClient = getDtoFromLinkCollectionWithProfileId(linkToDelete);
            return ResponseEntity.ok(linkForClient);
        } else {
            throw new DatabaseException("Could not delete link collection entry");
        }


    }

    // api endpoints ./


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

    private LinkWithinCollection getDtoFromLinkCollection(LinkCollection linkCollectionEntry){

        // prep and return created collection
        LinkWithinCollection linkForClient = new LinkWithinCollection();
        linkForClient.setFrontendId(linkCollectionEntry.getFrontendId());
        linkForClient.setUrl(linkCollectionEntry.getUrl());
        linkForClient.setText(linkCollectionEntry.getText());
        linkForClient.setPosition(linkCollectionEntry.getPosition());

        return linkForClient;
    }

    private LinkWithinCollection getDtoFromLinkCollectionWithProfileId(LinkCollectionWithProfileId linkCollectionEntry){

        // prep and return created collection
        LinkWithinCollection linkForClient = new LinkWithinCollection();
        linkForClient.setFrontendId(linkCollectionEntry.getFrontendId());
        linkForClient.setUrl(linkCollectionEntry.getUrl());
        linkForClient.setText(linkCollectionEntry.getText());
        linkForClient.setPosition(linkCollectionEntry.getPosition());

        return linkForClient;
    }

    private LinkCollection getLinkCollectionFromLinkCollectionWithProfileId(LinkCollectionWithProfileId linkCollectionEntry){
        LinkCollection targetLinkCollection = new LinkCollection();
        targetLinkCollection.setContentBoxId(linkCollectionEntry.getContentBoxId());
        targetLinkCollection.setLinkCollectionId(linkCollectionEntry.getLinkCollectionId());
        targetLinkCollection.setFrontendId(linkCollectionEntry.getFrontendId());
        targetLinkCollection.setPosition(linkCollectionEntry.getPosition());
        targetLinkCollection.setUrl(linkCollectionEntry.getUrl());
        targetLinkCollection.setText(linkCollectionEntry.getText());

        return targetLinkCollection;
    }

    // Data Support Functions ./
}


