package io.hypher.backendservice.platformdata.linkcollection;

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

    public Optional<LinkCollection> save(LinkCollection linkCollection){
        
        LinkCollection savedLinkCollection = linkCollectionRepository.save(linkCollection);
        return Optional.of(savedLinkCollection);
    }

    public Optional<List<LinkCollectionWithProfileId>> findByProfileId(UUID profileId){        
        // always return ordered by position
        return Optional.of(linkCollectionWithProfileIdRepository.findAllByProfileIdOrderByPosition(profileId));
    }




}
