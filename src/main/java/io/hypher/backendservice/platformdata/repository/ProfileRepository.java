package io.hypher.backendservice.platformdata.repository;

import java.util.UUID;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hypher.backendservice.platformdata.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,UUID>{

    Collection<Profile> findByProfileHandle(String profileHandle);
    
}
