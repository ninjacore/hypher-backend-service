package io.hypher.backendservice.platformdata.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.LinkCollection;
import io.hypher.backendservice.platformdata.repository.LinkCollectionRepository;

@Service
public class LinkCollectionService {
    
    @Autowired
    private LinkCollectionRepository linkCollectionRepository;

    public Optional<LinkCollection> findById(UUID linkCollectionId){
        return linkCollectionRepository.findById(linkCollectionId);
    }

}
