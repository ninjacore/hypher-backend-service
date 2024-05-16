package io.hypher.backendservice.platformdata.linkcollection.repository;

import java.util.UUID;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hypher.backendservice.platformdata.linkcollection.model.LinkCollection;

@Repository
public interface LinkCollectionRepository extends JpaRepository<LinkCollection, UUID>{

    List<LinkCollection> deleteByContentBoxId(UUID contentBoxId);
    // List<LinkCollection> saveAllOrderByPositionDesc(List<LinkCollection> linkCollections);
    // List<LinkCollection> findAllOrderByPosition();
    
}
