package io.hypher.backendservice.platformdata.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile_ownership", schema = "public")
public class ProfileOwnership {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID profileOwnershipId;

    @Column(name = "account_id")
    UUID accountId;

    @Column(name = "profile_id")
    UUID profileId;

    public ProfileOwnership(UUID profileOwnershipId, UUID accountId, UUID profileId) {
        this.profileOwnershipId = profileOwnershipId;
        this.accountId = accountId;
        this.profileId = profileId;
    }

    public UUID getProfileOwnershipId() {
        return profileOwnershipId;
    }

    public void setProfileOwnershipId(UUID profileOwnershipId) {
        this.profileOwnershipId = profileOwnershipId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

}
