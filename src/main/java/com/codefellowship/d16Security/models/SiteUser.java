package com.codefellowship.d16Security.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//Step 1b: Make user model (DO NOT CALL IT USER)
@Entity
public class SiteUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String bio;

    //Create Default Constructor

    protected SiteUser() {}

    @ManyToMany
    @JoinTable (
            name = "followers_to_followees",
            joinColumns =  {@JoinColumn(name = "userWhoIsFollowing")},
            inverseJoinColumns = {@JoinColumn(name = "FollowedUser")}
    )
    Set<SiteUser> usersIFollow = new HashSet<>();

    @ManyToMany(mappedBy = "usersIFollow")
    Set<SiteUser> usersWhoFollowMe = new HashSet<>();

    public Set<SiteUser> getUsersIFollow() {
        return usersIFollow;
    }

    public Set<SiteUser> getUsersWhoFollowMe() {
        return usersWhoFollowMe;
    }
    @OneToMany(mappedBy = "postsIveMade")
    Set<Posts> listOfPosts = new HashSet<>();

    public Set<Posts> getListOfPosts() {
        return listOfPosts;
    }

    public void setListOfPosts(Set<Posts> listOfPosts) {
        this.listOfPosts = listOfPosts;
    }

    public SiteUser(String username, String password, String firstName, String lastName, Date dateOfBirth, String bio) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
