package io.hypher.backendservice.platformdata.service;

import java.util.List;
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

    public Optional<LinkCollection> save(LinkCollection linkCollection){
        LinkCollection savedLinkCollection = linkCollectionRepository.save(linkCollection);
        return Optional.of(savedLinkCollection);
    }

    public Optional<LinkCollection> findById(UUID linkCollectionId){
        return linkCollectionRepository.findById(linkCollectionId);
    }

    public List<LinkCollection> findAll(){
        return linkCollectionRepository.findAll();
    }

    public String delete(LinkCollection linkCollection){

        boolean linkCollectionDeleted;

        try {
            linkCollectionRepository.delete(linkCollection);
            linkCollectionDeleted = true;
        } catch (Exception e) {
            linkCollectionDeleted = false;
        }

        return "LinkCollection deleted? -> " + linkCollectionDeleted;
    }

}
