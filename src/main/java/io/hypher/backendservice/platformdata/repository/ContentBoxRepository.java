package io.hypher.backendservice.platformdata.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hypher.backendservice.platformdata.model.ContentBox;

@Repository
public interface ContentBoxRepository extends JpaRepository<ContentBox, UUID>{

    List<ContentBox> findByContentBoxPosition(String contentBoxPosition);
    
}
