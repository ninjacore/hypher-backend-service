package io.hypher.backendservice.platformdata.service;

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

    public Optional<Profile> findById(UUID profileId){
        return profileRepository.findById(profileId);
    }

}
