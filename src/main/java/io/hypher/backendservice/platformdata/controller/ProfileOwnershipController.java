package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.ProfileOwnership;
import io.hypher.backendservice.platformdata.service.ProfileOwnershipService;
import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;

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
public class ProfileOwnershipController {

    @Autowired
    ProfileOwnershipService profileOwnershipService;

    @PostMapping("/profileOwnerships")
    public Optional<ProfileOwnership> create(@RequestBody ProfileOwnership profileOwnership) {
        return profileOwnershipService.save(profileOwnership);        
    }
    
    
    @GetMapping("/profileOwnerships")
    public List<ProfileOwnership> getAll() {
        return profileOwnershipService.findAll();   
    }

    @GetMapping("/profileOwnerships/{id}")
    public Optional<ProfileOwnership> getById(@PathVariable(value = "id") UUID profileOwnershipId) {
        
        return profileOwnershipService.findById(profileOwnershipId);
    }


    @PutMapping("/profileOwnerships/{id}")
    public ResponseEntity<ProfileOwnership> update(@PathVariable(value = "id") UUID profileOwnershipId, @RequestBody ProfileOwnership updatedProfileOwnership) throws ResourceNotFoundException, WrongBodyException{

        // security: only update if profileOwnership exists
        profileOwnershipService.findById(profileOwnershipId).orElseThrow(() -> new ResourceNotFoundException("ProfileOwnership not found"));

        // security: only save if body and path variable are the same
        if (updatedProfileOwnership.getProfileOwnershipId().toString().equals(profileOwnershipId.toString())) {
            ProfileOwnership updatedProfileOwnershipFromDatabase = profileOwnershipService.save(updatedProfileOwnership)
                .orElseThrow(() -> new ResourceNotFoundException("ProfileOwnership not found"));
            return ResponseEntity.ok(updatedProfileOwnershipFromDatabase);

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }


    @DeleteMapping("/profileOwnerships/{id}")
    public String delete(@PathVariable(value = "id") UUID profileOwnershipId) throws ResourceNotFoundException{

        ProfileOwnership profileOwnershipToDelete = profileOwnershipService.findById(profileOwnershipId).orElseThrow(() -> new ResourceNotFoundException("ProfileOwnership not found"));

        return profileOwnershipService.delete(profileOwnershipToDelete);
    }
    
    
    
}
