package com.codefellowship.d16Security.controllers;

import com.codefellowship.d16Security.models.SiteUser;
import com.codefellowship.d16Security.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            m.addAttribute("FirstName", dbUser.getFirstName());
        }
        try{

        }catch (RuntimeException runtimeException) {

        throw new RuntimeException("Not the gumdrop buttons!!!");
        }
        return "index.html";
    }

    //GET route to /secret -> secret sauce
    @GetMapping("/secret")
    public String getSecretSauce() {
        return "secretSauce.html";
    }
    @GetMapping("/myProfile/{id}")
    public String getMyProfile(@PathVariable Long id, Model m, Principal p) {
        SiteUser authenticatedUser = siteUserRepository.findByUsername(p.getName());
        m.addAttribute("authUser", authenticatedUser);
        //FInd User By ID from DB
        SiteUser viewUser = siteUserRepository.findById(id).orElseThrow();
        //Attach tje user to the model
        m.addAttribute("viewUser", viewUser);
        return "myProfile.html";
    }
    @GetMapping("/user/{id}")
    public String  getOneSiteUser(@PathVariable Long id, Model m, Principal p){
        SiteUser authenticatedUser = siteUserRepository.findByUsername(p.getName());
        m.addAttribute("authUser", authenticatedUser);
        //FInd User By ID from DB
        SiteUser viewUser = siteUserRepository.findById(id).orElseThrow();
        //Attach tje user to the model
        m.addAttribute("viewUser", viewUser);
        return "user-info.html";

    }
    //Get route to /users/id to get One user -> sed this to user-info.html
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
    @PutMapping("/user/{id}")
    public RedirectView editSiteUserInfo(@PathVariable Long id, String username, String firstName, Principal p, RedirectAttributes redir) throws ServletException {
        //Find the user we want to edit
        SiteUser userToBeEdited =  siteUserRepository.findById(id).orElseThrow();
        if(p.getName().equals(userToBeEdited.getUsername())) {
            userToBeEdited.setUsername(username);
            userToBeEdited.setFirstName(firstName);
            //save the edited user back to the DB to persist changes
            siteUserRepository.save(userToBeEdited);
            request.logout();
            autoAuthWithHttpServletRequest(username, userToBeEdited.getPassword());
        }else {
        redir.addFlashAttribute("errorMessage", "That's nacho cheese");
        }
        return new RedirectView("/user/" + id);
    }
}
