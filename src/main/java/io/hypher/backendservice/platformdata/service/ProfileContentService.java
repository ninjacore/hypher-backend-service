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


    public Optional<ProfileContent> findById(UUID profileContentId){
        return profileContentRepository.findById(profileContentId);
    }

    public List<ProfileContent> findAll(){
        return profileContentRepository.findAll();
    }

    public Optional<List<ProfileContent>> findAllByProfileId(UUID profileId){
        return Optional.of(profileContentRepository.findAllByProfileId(profileId));
    }
    
}