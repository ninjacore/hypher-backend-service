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
import io.hypher.backendservice.platformdata.dto.GildedProfilePage;
import io.hypher.backendservice.platformdata.dto.LinkCollectionContentBox;
import io.hypher.backendservice.platformdata.dto.MainContentBox;
import io.hypher.backendservice.platformdata.model.Profile;

import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;
import java.util.Collection;

// import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Arrays;
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
public class ProfileContentController {
    
    @Autowired
    ProfileContentService profileContentService;

    @Autowired
    ProfileService profileService;

    // @PostMapping("/profileContents")
    // public Optional<ProfileContent> create(@RequestBody ProfileContent profileContent) {
    //     return profileContentService.save(profileContent);        
    // }

    // TODO: build the DTO from there
    @GetMapping("/profilePage/{handle}")
    public GildedProfilePage getProfilePage(@PathVariable(value = "handle") String profileHandle) throws ResourceNotFoundException{

        // once we get the data we can work with it
        Collection<Profile> profiles = profileService.findByHandle(profileHandle).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Profile profile = profiles.iterator().next();

        UUID profileId = profile.getProfileId();

        Collection<ProfileContent> profileContent = profileContentService.findByProfileId(profileId).orElseThrow(() -> new ResourceNotFoundException("ProfileContent not found"));

        /**
         * provide main content
         */
        MainContentBox mainContentBox = new MainContentBox("", "", null);
        mainContentBox.setName(profile.getDisplayedName());
        mainContentBox.setBio(profile.getUserBio());
        // turn tags into array
        String[] items = profile.getTags().split(",\\s*");
        List<String> list = new ArrayList<>(Arrays.asList(items));
        mainContentBox.setTags(list);
    
        /**
         * provide extra content
         */
        LinkCollectionContentBox linkCollectionContentBox = new LinkCollectionContentBox("",1);
        FeaturedContentBox featuredContentBox = new FeaturedContentBox("",2,"");

        for (ProfileContent content : profileContent) {

            if (content.getLinkCollectionId() != null && !content.getLinkCollectionId().toString().equals("") && !content.getLinkCollectionId().toString().equals("NULL")) {

                // TODO: don't repeat these three every time... use contentboxid to get it once
                linkCollectionContentBox.setPosition(content.getContentBoxPosition());
                // TODO: turn into actual type
                linkCollectionContentBox.setContentType(content.getContentBoxTypeShortTitle());

                // link collection types
                Integer position = content.getPositionInLinkCollection();
                String url = content.getLinkCollectionUrl();
                String text = content.getLinkCollectionAltLinkText(); 
                                
                if (linkCollectionContentBox.getContentBox() != null) {
                    linkCollectionContentBox.addContent(position, url, text);                    
                }

            }

            if (content.getFeaturedContentId() != null && !content.getFeaturedContentId().toString().equals("") && !content.getFeaturedContentId().toString().equals("NULL")) {

                // TODO: don't repeat these three every time... use contentboxid to get it once
                featuredContentBox.setShortTitle(content.getContentBoxShortTitle());
                featuredContentBox.setPosition(content.getContentBoxPosition());
                // TODO: turn into actual type
                featuredContentBox.setContentType(content.getContentBoxTypeShortTitle());

                // featured types
                Integer position = content.getPositionInFeaturedContent();
                String url = content.getFeaturedContentUrl();
                String description = content.getFeaturedContentDescription(); // can be null
                String category = content.getFeaturedContentCategory();


                if (featuredContentBox.getContentBox() != null) {
                    featuredContentBox.addContent(position, url, Objects.toString(description, ""), category);                    
                }

            }


        }

        // combine
        GildedProfilePage gildedProfilePage = new GildedProfilePage(mainContentBox, linkCollectionContentBox, featuredContentBox);
        
        // TODO: return gildedProfilePage
        // return profileContent;
        return gildedProfilePage;
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
