package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.LinkCollection;
import io.hypher.backendservice.platformdata.model.LinkCollectionWithProfileId;
import io.hypher.backendservice.platformdata.model.LinkCollectionWithPositions;
import io.hypher.backendservice.platformdata.repository.LinkCollectionRepository;
import io.hypher.backendservice.platformdata.repository.LinkCollectionWithProfileIdRepository;
import jakarta.transaction.Transactional;
import io.hypher.backendservice.platformdata.repository.LinkCollectionWithPositionsRepository;

@Service
public class LinkCollectionService {
    
    @Autowired
    private LinkCollectionRepository linkCollectionRepository;

    @Autowired
    private LinkCollectionWithProfileIdRepository linkCollectionWithProfileIdRepository;

    @Autowired
    private LinkCollectionWithPositionsRepository linkCollectionWithPositionsRepository;

    public LinkCollection save(LinkCollection linkCollection){
        LinkCollection savedLinkCollection = linkCollectionRepository.save(linkCollection);
        // return Optional.of(savedLinkCollection);
        return savedLinkCollection;
    }

    public Optional<List<LinkCollection>> saveAll(List<LinkCollection> linkCollections){
        return Optional.of(linkCollectionRepository.saveAll(linkCollections));
    }

    public Optional<LinkCollection> findById(UUID linkCollectionId){
        return linkCollectionRepository.findById(linkCollectionId);
    }

    public Optional<List<LinkCollectionWithProfileId>> findByProfileId(UUID profileId){
        return Optional.of(linkCollectionWithProfileIdRepository.findAllByProfileId(profileId));
    }

    public Optional<List<LinkCollectionWithPositions>> 
    findByHandleAndPosition(String profileHandle, Integer contentBoxPosition){
        return Optional.of(linkCollectionWithPositionsRepository.findAllByProfileHandleAndContentBoxPosition(profileHandle, String.valueOf(contentBoxPosition)));
    }


    public List<LinkCollection> findAll(){
        return linkCollectionRepository.findAll();
    }

    public Boolean delete(LinkCollection linkCollection){

        boolean linkCollectionDeleted;

        try {
            linkCollectionRepository.delete(linkCollection);
            linkCollectionDeleted = true;
        } catch (Exception e) {
            linkCollectionDeleted = false;
        }

        // return "LinkCollection deleted? -> " + linkCollectionDeleted;
        return linkCollectionDeleted;
    }

    @Transactional
    public List<LinkCollection> deleteByContentBoxId(UUID contentBoxId){
        return linkCollectionRepository.deleteByContentBoxId(contentBoxId);
    }

}
