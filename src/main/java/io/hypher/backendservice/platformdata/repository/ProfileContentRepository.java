package io.hypher.backendservice.platformdata.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hypher.backendservice.platformdata.model.ProfileContent;

public interface ProfileContentRepository extends JpaRepository<ProfileContent, UUID> {
    // Collection findAllByProfileId(UUID profileId);
    List findAllByProfileId(UUID profileId);
}
