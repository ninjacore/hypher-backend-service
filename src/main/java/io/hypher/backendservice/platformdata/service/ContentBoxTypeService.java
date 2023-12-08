package io.hypher.backendservice.platformdata.service;

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

    public Optional<ContentBoxType> findById(UUID contentBoxTypeId){
        return contentBoxTypeRepository.findById(contentBoxTypeId);
    }


}
