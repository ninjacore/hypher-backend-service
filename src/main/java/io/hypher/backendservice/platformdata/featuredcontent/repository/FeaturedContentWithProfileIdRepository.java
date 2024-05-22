package io.hypher.backendservice.platformdata.featuredcontent.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContentWithProfileId;

public interface FeaturedContentWithProfileIdRepository extends JpaRepository<FeaturedContentWithProfileId, Long>{
    List findAllByProfileId(UUID profileId);
    List findAllByProfileIdOrderByPosition(UUID profileId);
}
