package com.codefellowship.d16Security.repositories;

import com.codefellowship.d16Security.models.Posts;
import com.codefellowship.d16Security.models.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    public Posts findByTimeStamp(Date timeStamp);

}
