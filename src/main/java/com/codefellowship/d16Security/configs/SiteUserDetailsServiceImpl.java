package com.codefellowship.d16Security.configs;

import com.codefellowship.d16Security.models.SiteUser;
import com.codefellowship.d16Security.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Step 3: UserDetailsServiceImpl implements UserDetailsService
//Add the @Service here
@Service //Spring autodetects and loads
public class SiteUserDetailsServiceImpl implements UserDetailsService {
    //Step 3a Autowire our site user repo
    @Autowired
    SiteUserRepository siteUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Add a sout
        System.out.println("Site User Details Server Calls For User(From Database)");
        return (UserDetails) siteUserRepository.findByUsername(username);
    }
}
