package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContent;
import io.hypher.backendservice.platformdata.featuredcontent.repository.FeaturedContentRepository;
import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContentWithProfileId;
import io.hypher.backendservice.platformdata.featuredcontent.repository.FeaturedContentWithProfileIdRepository;;

@Service
public class FeaturedContentService {
    
    @Autowired
    private FeaturedContentRepository featuredContentRepository;

    @Autowired
    private FeaturedContentWithProfileIdRepository featuredContentViewRepository;

    public Optional<FeaturedContent> save(FeaturedContent featuredContent){
        FeaturedContent savedFeaturedContent = featuredContentRepository.save(featuredContent);
        return Optional.of(savedFeaturedContent);
    }

    public Optional<FeaturedContent> findById(UUID featuredContentId){
        return featuredContentRepository.findById(featuredContentId);
    }

    public Optional<List<FeaturedContentWithProfileId>> findByProfileId(UUID profileId){
        return Optional.of(featuredContentViewRepository.findAllByProfileId(profileId));
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
