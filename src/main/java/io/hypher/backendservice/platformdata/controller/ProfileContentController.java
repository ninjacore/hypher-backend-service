package io.hypher.backendservice.platformdata.controller;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.ProfileContent;
import io.hypher.backendservice.platformdata.service.ProfileContentService;
import io.hypher.backendservice.platformdata.service.ProfileService;
import io.hypher.backendservice.platformdata.dto.FeaturedContentBox;
import io.hypher.backendservice.platformdata.dto.LinkCollectionContentBox;
import io.hypher.backendservice.platformdata.dto.MainContent;
import io.hypher.backendservice.platformdata.dto.MainContentBox;
import io.hypher.backendservice.platformdata.model.Profile;

import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;

import java.util.Optional;
import java.util.UUID;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


// @CrossOrigin("http://localhost:3000")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ProfileContentController {
    
    @Autowired
    private ProfileContentService profileContentService;

    @Autowired
    private ProfileService profileService;

    // @CrossOrigin("http://localhost:3000")
    @GetMapping("/profilePage/{handle}")
    public ResponseEntity<List<Object>> getProfilePage(@PathVariable(value = "handle") String profileHandle) throws ResourceNotFoundException{

        // once we get the data we can work with it
        Collection<Profile> profiles = profileService.findByHandle(profileHandle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Profile profile = profiles.iterator().next();

        UUID profileId = profile.getProfileId();

        List<ProfileContent> profileContent = profileContentService.findAllByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("ProfileContent not found"));

        /**
         * provide main content
         */
        MainContent mainContent = new MainContent("", "", null);
        mainContent.setName(profile.getDisplayedName());
        mainContent.setBio(profile.getUserBio());
        // turn tags into array
        String[] items = profile.getTags().split(",\\s*");
        List<String> list = new ArrayList<>(Arrays.asList(items));
        mainContent.setTags(list);

        MainContentBox mainContentBox = new MainContentBox(); // has defaults
        mainContentBox.addContent(mainContent);

    
        /**
         * provide extra content
         */
        LinkCollectionContentBox linkCollectionContentBox = new LinkCollectionContentBox("",1);

        FeaturedContentBox featuredContentBox = new FeaturedContentBox("",2,"");        

        for (ProfileContent content : profileContent) {

            if (content.getLinkCollectionId() != null && !content.getLinkCollectionId().toString().equals("") && !content.getLinkCollectionId().toString().equals("NULL")) {

                // happens on-repeat but information is not known before
                linkCollectionContentBox.setContentType(content.getContentBoxTypeShortTitle());

                // link collection types
                Integer position = content.getPositionInLinkCollection();
                String url = content.getLinkCollectionUrl();
                String text = content.getLinkCollectionAltLinkText(); // can be null
                                
                if (linkCollectionContentBox.getContentBox() != null) {
                    linkCollectionContentBox.addContent(position, url, Objects.toString(text, ""));                    
                }

            }

            if (content.getFeaturedContentId() != null && !content.getFeaturedContentId().toString().equals("") && !content.getFeaturedContentId().toString().equals("NULL")) {

                // happens on-repeat but information is not known before
                featuredContentBox.setShortTitle(content.getContentBoxShortTitle());
                featuredContentBox.setContentType(content.getContentBoxTypeShortTitle());        

                // featured types
                Integer position = content.getPositionInFeaturedContent();
                String url = content.getFeaturedContentUrl();
                String description = content.getFeaturedContentDescription(); // can be null
                String category = content.getFeaturedContentCategory();
                String title = content.getFeaturedContentTitle(); // can be null


                if (featuredContentBox.getContentBox() != null) {
                    featuredContentBox.addContent(position, url, Objects.toString(title, ""), Objects.toString(description, ""), category);                    
                }

            }
        }
        
        // combine
        // return List.of(mainContentBox, linkCollectionContentBox, featuredContentBox);

        return ResponseEntity.ok().body(List.of(mainContentBox, linkCollectionContentBox, featuredContentBox));
    }
    
    @GetMapping("/profileContents")
    public List<ProfileContent> getAll() {
        return profileContentService.findAll();   
    }

    @GetMapping("/profileContents/{id}")
    public Optional<ProfileContent> getById(@PathVariable(value = "id") UUID profileContentId) {
        
        return profileContentService.findById(profileContentId);
    }

}
