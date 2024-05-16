package io.hypher.backendservice.platformdata.linkcollection.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hypher.backendservice.platformdata.linkcollection.model.LinkCollectionWithProfileId;

public interface LinkCollectionWithProfileIdRepository extends JpaRepository<LinkCollectionWithProfileId, Long> {
    List findAllByProfileId(UUID profileId);
    List findAllByProfileIdOrderByPosition(UUID profileId);
}
