package io.hypher.backendservice.platformdata.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.ProfileOwnership;
import io.hypher.backendservice.platformdata.repository.ProfileOwnershipRepository;

@Service
public class ProfileOwnershipService {
    
    @Autowired
    private ProfileOwnershipRepository profileOwnwershipRepository;

    public Optional<ProfileOwnership> findById(UUID profileOwnwershipId){
        return profileOwnwershipRepository.findById(profileOwnwershipId);
    }

}
