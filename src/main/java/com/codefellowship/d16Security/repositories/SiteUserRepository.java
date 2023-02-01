package com.codefellowship.d16Security.repositories;

import com.codefellowship.d16Security.models.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

//Step 1:  Create SiteUser Repo
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    public SiteUser findByUsername(String username);
//    public SiteUser findById(id);
}
