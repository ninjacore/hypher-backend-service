package io.hypher.backendservice.platformdata.featuredcontent;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.featuredcontent.dto.FeaturedWithinCollection;
import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContent;
import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContentWithProfileId;
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
public class FeaturedContentV2Controller{

    @Autowired
    private FeaturedContentV2Service featuredContentService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ContentBoxService contentBoxService;


    // api endpoints /.
    @GetMapping("/featuredContent")
    public ResponseEntity<List<FeaturedWithinCollection>> getFeaturedContentByHandle(
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

        // find featuredContent entries
        List<FeaturedContentWithProfileId> extendedFeaturedContent =
            featuredContentService.findByProfileId(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("FeaturedContent not found"));

        List<FeaturedWithinCollection> featuredContentDtoForClient = new ArrayList<>();
        for (FeaturedContentWithProfileId entry : extendedFeaturedContent) {
            FeaturedWithinCollection contentForClient = getDtoFromFeaturedContentWithProfileId(entry);
            featuredContentDtoForClient.add(contentForClient);
        }

        // sort by position (descending)
        featuredContentDtoForClient.sort((FeaturedWithinCollection a, FeaturedWithinCollection b) -> a.getPosition().compareTo(b.getPosition()));

        return ResponseEntity.ok(featuredContentDtoForClient);
    }

    @PostMapping("/featuredContent/content")
    public ResponseEntity<FeaturedWithinCollection> createFeaturedByHandle(
        @RequestParam String handle,
        @RequestParam(required = false) String contentBoxPosition,
        @RequestBody FeaturedWithinCollection frontendFeaturedDTO
        )
    throws ResourceNotFoundException, DatabaseException{

        // check optional parameters
        if(contentBoxPosition == null){
            contentBoxPosition = "1"; // default
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

        // prepare featuredContent entry
        FeaturedContent featuredContent = new FeaturedContent();
        featuredContent.setContentBoxId(contentBoxId);
        featuredContent.setFrontendId(frontendFeaturedDTO.getFrontendId());
        featuredContent.setTitle(frontendFeaturedDTO.getTitle());
        featuredContent.setDescription(frontendFeaturedDTO.getDescription());
        featuredContent.setUrl(frontendFeaturedDTO.getUrl());
        featuredContent.setPosition(frontendFeaturedDTO.getPosition());
        featuredContent.setCategory(frontendFeaturedDTO.getCategory());

        // create database entry
        FeaturedContent createdFeatured = featuredContentService.save(featuredContent)
            .orElseThrow(() -> new DatabaseException("Could not create featured content entry"));

        FeaturedWithinCollection contentForClient = getDtoFromFeaturedContent(createdFeatured);

        return ResponseEntity.ok(contentForClient);
    }

    @PutMapping("/featuredContent/update")
    public ResponseEntity<List<FeaturedWithinCollection>> updateFeaturedContentByHandle
    (
        @RequestParam String handle,
        @RequestParam(required = false) String contentBoxPosition,
        @RequestBody List<FeaturedWithinCollection> listOfFrontendFeaturedDTOs
    )
    throws DatabaseException, ResourceNotFoundException
    {
        // check optional parameters
        if(contentBoxPosition == null){
            contentBoxPosition = "1"; // default
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
        List<FeaturedContent> featuredContentUpdate = new ArrayList<>();
        for (FeaturedWithinCollection content : listOfFrontendFeaturedDTOs) {

            // create the updatd featuredContent entry for that content box id
            FeaturedContent featuredContentEntry = new FeaturedContent();
            featuredContentEntry.setFrontendId(content.getFrontendId());
            featuredContentEntry.setContentBoxId(contentBoxId);
            featuredContentEntry.setTitle(content.getTitle());
            featuredContentEntry.setDescription(content.getDescription());
            featuredContentEntry.setUrl(content.getUrl());
            featuredContentEntry.setPosition(content.getPosition());
            featuredContentEntry.setCategory(content.getCategory());

            featuredContentUpdate.add(featuredContentEntry);
        }

        // all entries have to be deleted to avert inserts instead of updates
        List<FeaturedContent> deletedRecords = featuredContentService
            .deleteAllByContentBoxId(contentBoxId)
            .orElseThrow(
                () -> new DatabaseException("Could not complete featured content update")
            );
        // Note: if switching to multiple FeaturedContents per user this won't be feasible anymore

        // perform update
        List<FeaturedContent> updatedFeaturedContent = featuredContentService
            .saveAll(featuredContentUpdate)
            .orElseThrow( () -> new DatabaseException("Could not update featured content"));

        // prepare response
        List<FeaturedWithinCollection> listForClient = new ArrayList<>();

        updatedFeaturedContent.forEach(entry -> {
            FeaturedWithinCollection contentForClient = getDtoFromFeaturedContent(entry);
            listForClient.add(contentForClient);
        });

        // sort by position in descending order
        listForClient.sort((FeaturedWithinCollection a, FeaturedWithinCollection b) -> a.getPosition().compareTo(b.getPosition()));

        return ResponseEntity.ok(listForClient);
    }

    @PutMapping("/featuredContent/content/update")
    public ResponseEntity<FeaturedWithinCollection> updateFeaturedByHandle(
        @RequestParam String handle,
        @RequestParam(required = false) String contentBoxPosition,
        @RequestBody FeaturedWithinCollection frontendFeaturedDTO
    )
    throws DatabaseException, ResourceNotFoundException
    {

        // check optional parameters
        if(contentBoxPosition == null){
            contentBoxPosition = "1"; // default
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

        // prepare featuredContent entry
        FeaturedContent featuredContent = new FeaturedContent();
        featuredContent.setContentBoxId(contentBoxId);
        featuredContent.setFrontendId(frontendFeaturedDTO.getFrontendId());
        featuredContent.setTitle(frontendFeaturedDTO.getTitle());
        featuredContent.setDescription(frontendFeaturedDTO.getDescription());
        featuredContent.setUrl(frontendFeaturedDTO.getUrl());
        featuredContent.setPosition(frontendFeaturedDTO.getPosition());
        featuredContent.setCategory(frontendFeaturedDTO.getCategory());

        // create database entry
        FeaturedContent updatedFeatured = featuredContentService.save(featuredContent)
            .orElseThrow(() -> new DatabaseException("Could not update featured content entry"));

        FeaturedWithinCollection contentForClient = getDtoFromFeaturedContent(updatedFeatured);

        return ResponseEntity.ok(contentForClient);
    }

    @DeleteMapping("/featuredContent/content/delete")
    public ResponseEntity<FeaturedWithinCollection> deleteByHandleAndFrontendId(
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

        // find featuredContents by profileId
        List<FeaturedContentWithProfileId> extendedFeaturedContent =
            featuredContentService.findByProfileId(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("FeaturedContent not found"));

        // get the one with the right frontendId and handle (by profileId)
        FeaturedContentWithProfileId contentToDelete = extendedFeaturedContent.stream()
            .filter(lc -> lc.getFrontendId().equals(frontendId))
            .filter(lc -> lc.getProfileId().equals(profileId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("No content found with this frontendId"));

        // prepare item for deletion
        FeaturedContent featuredContentToDelete = getFeaturedContentFromFeaturedContentWithProfileId(contentToDelete);

        // Perform deletion and return result
        Boolean contentGotDeleted = featuredContentService.delete(featuredContentToDelete);

        if(contentGotDeleted){
            FeaturedWithinCollection contentForClient = getDtoFromFeaturedContentWithProfileId(contentToDelete);
            return ResponseEntity.ok(contentForClient);
        } else {
            throw new DatabaseException("Could not delete featured content entry");
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

        // find contentboxId by position of contentedCollection
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

    private FeaturedWithinCollection getDtoFromFeaturedContent(FeaturedContent featuredContentEntry){

        // prep and return created collection
        FeaturedWithinCollection contentForClient = new FeaturedWithinCollection();
        contentForClient.setFrontendId(featuredContentEntry.getFrontendId());
        contentForClient.setTitle(featuredContentEntry.getTitle());
        contentForClient.setDescription(featuredContentEntry.getDescription());
        contentForClient.setUrl(featuredContentEntry.getUrl());
        contentForClient.setPosition(featuredContentEntry.getPosition());
        contentForClient.setCategory(featuredContentEntry.getCategory());

        return contentForClient;
    }

    private FeaturedWithinCollection getDtoFromFeaturedContentWithProfileId(FeaturedContentWithProfileId featuredContentEntry){

        // prep and return created collection
        FeaturedWithinCollection contentForClient = new FeaturedWithinCollection();
        contentForClient.setFrontendId(featuredContentEntry.getFrontendId());
        contentForClient.setTitle(featuredContentEntry.getTitle());
        contentForClient.setDescription(featuredContentEntry.getDescription());
        contentForClient.setUrl(featuredContentEntry.getUrl());
        contentForClient.setPosition(featuredContentEntry.getPosition());
        contentForClient.setCategory(featuredContentEntry.getCategory());

        return contentForClient;
    }

    private FeaturedContent getFeaturedContentFromFeaturedContentWithProfileId(FeaturedContentWithProfileId featuredContentEntry){
        FeaturedContent targetFeaturedContent = new FeaturedContent();
        targetFeaturedContent.setContentBoxId(featuredContentEntry.getContentBoxId());
        targetFeaturedContent.setFeaturedContentId(featuredContentEntry.getFeaturedContentId());
        targetFeaturedContent.setFrontendId(featuredContentEntry.getFrontendId());
        targetFeaturedContent.setTitle(featuredContentEntry.getTitle());
        targetFeaturedContent.setDescription(featuredContentEntry.getDescription());
        targetFeaturedContent.setPosition(featuredContentEntry.getPosition());
        targetFeaturedContent.setUrl(featuredContentEntry.getUrl());
        targetFeaturedContent.setCategory(featuredContentEntry.getCategory());

        return targetFeaturedContent;
    }

    // Data Support Functions ./
}


