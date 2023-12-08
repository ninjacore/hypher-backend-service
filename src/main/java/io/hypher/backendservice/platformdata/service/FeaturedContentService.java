package io.hypher.backendservice.platformdata.service;

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

    public Optional<FeaturedContent> findById(UUID featuredContentId){
        return featuredContentRepository.findById(featuredContentId);
    }

}
