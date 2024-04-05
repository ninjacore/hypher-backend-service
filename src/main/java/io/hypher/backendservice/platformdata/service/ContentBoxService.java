package io.hypher.backendservice.platformdata.service;

import java.util.List;
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

    public Optional<ContentBox> save(ContentBox contentBox){
        ContentBox savedContentBox = contentBoxRepository.save(contentBox);
        return Optional.of(savedContentBox);
    }

    public Optional<ContentBox> findById(UUID contentBoxId){
        return contentBoxRepository.findById(contentBoxId);
    }

    public Optional<List<ContentBox>> findByPosition(String position){
        return Optional.of(contentBoxRepository.findByContentBoxPosition(position));
    }

    public List<ContentBox> findAll(){
        return contentBoxRepository.findAll();
    }

    public String delete(ContentBox contentBox){

        boolean contentBoxDeleted;

        try {
            contentBoxRepository.delete(contentBox);
            contentBoxDeleted = true;
        } catch (Exception e) {
            contentBoxDeleted = false;
        }

        return "ContentBox deleted? -> " + contentBoxDeleted;
    }



}
