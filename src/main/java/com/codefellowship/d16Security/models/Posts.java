package com.codefellowship.d16Security.models;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String postBody;
    private Date timeStamp;
@ManyToOne
SiteUser userPosts;
//OneToMany(mappedBy ="posts", cascade = CascadeType.REMOVE)



    protected Posts() {}

    public Posts(String postBody, Date timeStamp) {
        this.postBody = postBody;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }
}