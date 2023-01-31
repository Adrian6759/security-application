package com.codefellowship.d16Security.controllers;

import com.codefellowship.d16Security.models.SiteUser;
import com.codefellowship.d16Security.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

//Step 2: Create controller for SiteUser
@Controller
public class SiteUserController {
    @Autowired
    SiteUserRepository siteUserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    //This is how you do auto login
    @Autowired
    HttpServletRequest request;


    //POST route to create new SiteUser
    @PostMapping("/signup")
    public RedirectView createSiteUser(String username, String password, String firstName) {
        //has the PW
        String hashedPW = passwordEncoder.encode(password);
        //Create the user
        SiteUser newUser = new SiteUser(username, hashedPW, firstName, new Date());
        //Save the user
        siteUserRepository.save(newUser);
        //Auto login
        autoAuthWithHttpServletRequest(username, password);
        //Return redirectView
        return new RedirectView("/");
    }
    public void autoAuthWithHttpServletRequest(String username, String password){
        try{
            request.login(username, password);
        } catch (ServletException se) {
            se.printStackTrace();
        }
    }
    @GetMapping("/login")
    public String getLoginPage(){
        return "login.html";
    }
    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup.html";
    }
    //GET route to get ONE SiteUser

    //GET route /
    //Principal = Http Session = current user
    @GetMapping("/")
    public String getHome(Model m, Principal p) {
        if (p != null) {
            String username = p.getName();
            SiteUser dbUser = siteUserRepository.findByUsername(username);
            m.addAttribute("username", username);
            m.addAttribute("First Name", dbUser.getFirstName());
        }
        return "index.html";
    }

    //GET route to /secret -> secret sauce
    @GetMapping("/secret")
    public String getSecretSauce() {
        return "secretSauce.html";
    }
}
