package io.hypher.backendservice.platformdata.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hypher.backendservice.platformdata.model.LinkCollectionWithProfileId;

public interface LinkCollectionWithProfileIdRepository extends JpaRepository<LinkCollectionWithProfileId, UUID> {
    List findAllByProfileId(UUID profileId);
    List findAllByProfileIdOrderByPosition(UUID profileId);
}
