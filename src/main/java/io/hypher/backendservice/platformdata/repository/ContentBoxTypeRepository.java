package io.hypher.backendservice.platformdata.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hypher.backendservice.platformdata.model.ContentBoxType;

@Repository
public interface ContentBoxTypeRepository extends JpaRepository<ContentBoxType, UUID>{
    
}
