package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.Profile;
import io.hypher.backendservice.platformdata.repository.ProfileRepository;

@Service
public class ProfileService {
    
    @Autowired
    private ProfileRepository profileRepository;

    public Optional<Profile> save(Profile profile){
        Profile savedProfile = profileRepository.save(profile);
        return Optional.of(savedProfile);
    }

    public Optional<Profile> findById(UUID profileId){
        return profileRepository.findById(profileId);
    }

    public List<Profile> findAll(){
        return profileRepository.findAll();
    }

    public String delete(Profile profile){

        boolean profileDeleted;

        try {
            profileRepository.delete(profile);
            profileDeleted = true;
        } catch (Exception e) {
            profileDeleted = false;
        }

        return "Profile deleted? -> " + profileDeleted;
    }

}
