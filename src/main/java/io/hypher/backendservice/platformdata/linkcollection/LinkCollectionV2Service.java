package io.hypher.backendservice.platformdata.linkcollection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.linkcollection.model.LinkCollection;
import io.hypher.backendservice.platformdata.linkcollection.model.LinkCollectionWithProfileId;
import io.hypher.backendservice.platformdata.linkcollection.repository.LinkCollectionRepository;
import io.hypher.backendservice.platformdata.linkcollection.repository.LinkCollectionWithProfileIdRepository;
import jakarta.transaction.Transactional;

@Service
public class LinkCollectionV2Service {

    @Autowired
    private LinkCollectionRepository linkCollectionRepository;

    @Autowired
    private LinkCollectionWithProfileIdRepository linkCollectionWithProfileIdRepository;

    public Optional<LinkCollection> save(LinkCollection linkCollection){
        
        LinkCollection savedLinkCollection = linkCollectionRepository.save(linkCollection);
        return Optional.of(savedLinkCollection);
    }

    public Optional<List<LinkCollection>> saveAll(List<LinkCollection> linkCollections){
        return Optional.of(linkCollectionRepository.saveAll(linkCollections));
    }

    public Optional<LinkCollection> findById(UUID linkCollectionId){
        return linkCollectionRepository.findById(linkCollectionId);
    }

    public Optional<List<LinkCollectionWithProfileId>> findByProfileId(UUID profileId){        
        // always return ordered by position
        return Optional.of(linkCollectionWithProfileIdRepository.findAllByProfileIdOrderByPosition(profileId));
    }

    public Boolean delete(LinkCollection linkCollection){

        boolean linkCollectionDeleted;

        try {
            linkCollectionRepository.delete(linkCollection);
            linkCollectionDeleted = true;
        } catch (Exception e) {
            linkCollectionDeleted = false;
        }

        return linkCollectionDeleted;
    }

    @Transactional
    public Optional<List<LinkCollection>> deleteAllByContentBoxId(UUID contentBoxId){
        return Optional.of(linkCollectionRepository.deleteByContentBoxId(contentBoxId));
    }

}
