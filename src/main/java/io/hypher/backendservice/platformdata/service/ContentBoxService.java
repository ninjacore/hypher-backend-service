package io.hypher.backendservice.platformdata.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.ContentBox;
import io.hypher.backendservice.platformdata.repository.ContentBoxRepository;

@Service
public class ContentBoxService {
    
    @Autowired
    private ContentBoxRepository contentBoxRepository;

    
    public Optional<ContentBox> findById(UUID contentBoxId){
        return contentBoxRepository.findById(contentBoxId);
    }



}
