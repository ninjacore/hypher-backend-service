package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.ProfileContent;
import io.hypher.backendservice.platformdata.repository.ProfileContentRepository;

@Service
public class ProfileContentService {

    @Autowired
    private ProfileContentRepository profileContentRepository;

    // public Optional<ProfileContent> save(ProfileContent profileContent){
    //     ProfileContent savedProfileContent = profileContentRepository.save(profileContent);
    //     return Optional.of(savedProfileContent);
    // }

    public Optional<ProfileContent> findById(UUID profileContentId){
        return profileContentRepository.findById(profileContentId);
    }

    public List<ProfileContent> findAll(){
        return profileContentRepository.findAll();
    }

    // public String delete(ProfileContent profileContent){

    //     boolean profileContentDeleted;

    //     try {
    //         profileContentRepository.delete(profileContent);
    //         profileContentDeleted = true;
    //     } catch (Exception e) {
    //         profileContentDeleted = false;
    //     }

    //     return "ProfileContent deleted? -> " + profileContentDeleted;
    // }
    
}
