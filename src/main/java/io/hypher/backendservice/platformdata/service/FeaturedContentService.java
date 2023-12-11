package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.FeaturedContent;
import io.hypher.backendservice.platformdata.repository.FeaturedContentRepository;

@Service
public class FeaturedContentService {
    
    @Autowired
    private FeaturedContentRepository featuredContentRepository;

    public Optional<FeaturedContent> save(FeaturedContent featuredContent){
        FeaturedContent savedFeaturedContent = featuredContentRepository.save(featuredContent);
        return Optional.of(savedFeaturedContent);
    }

    public Optional<FeaturedContent> findById(UUID featuredContentId){
        return featuredContentRepository.findById(featuredContentId);
    }

    public List<FeaturedContent> findAll(){
        return featuredContentRepository.findAll();
    }

    public String delete(FeaturedContent featuredContent){

        boolean featuredContentDeleted;

        try {
            featuredContentRepository.delete(featuredContent);
            featuredContentDeleted = true;
        } catch (Exception e) {
            featuredContentDeleted = false;
        }

        return "FeaturedContent deleted? -> " + featuredContentDeleted;
    }

}
