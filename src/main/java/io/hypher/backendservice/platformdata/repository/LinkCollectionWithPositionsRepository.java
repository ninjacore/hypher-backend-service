package io.hypher.backendservice.platformdata.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hypher.backendservice.platformdata.model.LinkCollectionWithPositions;

public interface LinkCollectionWithPositionsRepository extends JpaRepository<LinkCollectionWithPositions, UUID>{
    List<LinkCollectionWithPositions> findAllByProfileHandleAndContentBoxPosition(String profileHandle, String contentBoxPosition);
}
