package io.hypher.backendservice.platformdata.featuredcontent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContent;
import io.hypher.backendservice.platformdata.featuredcontent.model.FeaturedContentWithProfileId;
import io.hypher.backendservice.platformdata.featuredcontent.repository.FeaturedContentRepository;
import io.hypher.backendservice.platformdata.featuredcontent.repository.FeaturedContentWithProfileIdRepository;
import jakarta.transaction.Transactional;

@Service
public class FeaturedContentV2Service {

    @Autowired
    private FeaturedContentRepository featuredContentRepository;

    @Autowired
    private FeaturedContentWithProfileIdRepository featuredContentWithProfileIdRepository;

    public Optional<FeaturedContent> save(FeaturedContent featuredContent){
        
        FeaturedContent savedFeaturedContent = featuredContentRepository.save(featuredContent);
        return Optional.of(savedFeaturedContent);
    }

    public Optional<List<FeaturedContent>> saveAll(List<FeaturedContent> featuredContents){
        return Optional.of(featuredContentRepository.saveAll(featuredContents));
    }

    public Optional<FeaturedContent> findById(UUID featuredContentId){
        return featuredContentRepository.findById(featuredContentId);
    }

    public Optional<List<FeaturedContentWithProfileId>> findByProfileId(UUID profileId){        
        // always return ordered by position
        return Optional.of(featuredContentWithProfileIdRepository.findAllByProfileIdOrderByPosition(profileId));
    }

    public Boolean delete(FeaturedContent featuredContent){

        boolean featuredContentDeleted;

        try {
            featuredContentRepository.delete(featuredContent);
            featuredContentDeleted = true;
        } catch (Exception e) {
            featuredContentDeleted = false;
        }

        return featuredContentDeleted;
    }

    @Transactional
    public Optional<List<FeaturedContent>> deleteAllByContentBoxId(UUID contentBoxId){
        return Optional.of(featuredContentRepository.deleteByContentBoxId(contentBoxId));
    }

}
