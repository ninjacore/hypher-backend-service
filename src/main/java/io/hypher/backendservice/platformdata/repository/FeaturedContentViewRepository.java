package io.hypher.backendservice.platformdata.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hypher.backendservice.platformdata.model.ProfileContent;

public interface FeaturedContentViewRepository extends JpaRepository<ProfileContent, UUID> {
    List findAllByProfileId(UUID profileId);
}
