package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.ProfileOwnership;
import io.hypher.backendservice.platformdata.repository.ProfileOwnershipRepository;

@Service
public class ProfileOwnershipService {
    
    @Autowired
    private ProfileOwnershipRepository profileOwnershipRepository;

    public Optional<ProfileOwnership> save(ProfileOwnership profileOwnership){
        ProfileOwnership savedProfileOwnership = profileOwnershipRepository.save(profileOwnership);
        return Optional.of(savedProfileOwnership);
    }

    public Optional<ProfileOwnership> findById(UUID profileOwnershipId){
        return profileOwnershipRepository.findById(profileOwnershipId);
    }

    public List<ProfileOwnership> findAll(){
        return profileOwnershipRepository.findAll();
    }

    public String delete(ProfileOwnership profileOwnership){

        boolean profileOwnershipDeleted;

        try {
            profileOwnershipRepository.delete(profileOwnership);
            profileOwnershipDeleted = true;
        } catch (Exception e) {
            profileOwnershipDeleted = false;
        }

        return "ProfileOwnership deleted? -> " + profileOwnershipDeleted;
    }

}
