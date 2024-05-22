package io.hypher.backendservice.platformdata.featuredcontent.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContent;

@Repository
public interface FeaturedContentRepository extends JpaRepository<FeaturedContent, UUID>{

    List<FeaturedContent> deleteByContentBoxId(UUID contentBoxId);
    
}
