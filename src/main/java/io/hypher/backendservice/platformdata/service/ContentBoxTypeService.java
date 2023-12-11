package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.ContentBoxType;
import io.hypher.backendservice.platformdata.repository.ContentBoxTypeRepository;

@Service
public class ContentBoxTypeService {
    
    @Autowired
    private ContentBoxTypeRepository contentBoxTypeRepository;

    public Optional<ContentBoxType> save(ContentBoxType contentBoxType){
        ContentBoxType savedContentBoxType = contentBoxTypeRepository.save(contentBoxType);
        return Optional.of(savedContentBoxType);
    }

    public Optional<ContentBoxType> findById(UUID contentBoxTypeId){
        return contentBoxTypeRepository.findById(contentBoxTypeId);
    }

    public List<ContentBoxType> findAll(){
        return contentBoxTypeRepository.findAll();
    }

    public String delete(ContentBoxType contentBoxType){

        boolean contentBoxTypeDeleted;

        try {
            contentBoxTypeRepository.delete(contentBoxType);
            contentBoxTypeDeleted = true;
        } catch (Exception e) {
            contentBoxTypeDeleted = false;
        }

        return "ContentBoxType deleted? -> " + contentBoxTypeDeleted;
    }

}
