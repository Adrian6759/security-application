package com.codefellowship.d16Security.controllers;

import com.codefellowship.d16Security.models.Posts;
import com.codefellowship.d16Security.models.SiteUser;
import com.codefellowship.d16Security.repositories.PostsRepository;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Step 2: Create controller for SiteUser
@Controller
public class SiteUserController {
    @Autowired
    SiteUserRepository siteUserRepository;

    @Autowired
    PostsRepository postsRepository;
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
    @GetMapping("/myProfile")
    public String getMyProfile( Model m, Principal p) {
        SiteUser authenticatedUser = siteUserRepository.findByUsername(p.getName());
        m.addAttribute("authUser", authenticatedUser);
        //FInd User By ID from DB
//        SiteUser viewUser = siteUserRepository.findById(id).orElseThrow();
//        //Attach tje user to the model
//        m.addAttribute("viewUser", viewUser);
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

        //Add users I follow and userWhoFOlowMe to the page to be consumed
        m.addAttribute("usersIFollow", viewUser.getUsersIFollow());
        m.addAttribute("usersWhoFollowMe", viewUser.getUsersWhoFollowMe());

        return "user-info.html";

    }
    //Get route to /users/id to get One user -> sed this to user-info.html
    @PostMapping("/signup")
    public RedirectView createSiteUser(String username, String password, String firstName, String lastName , String dateOfBirth, String bio) {
        //has the PW
        String hashedPW = passwordEncoder.encode(password);
        String stringDate = dateOfBirth;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date formattedDate;
        try {
             formattedDate = format.parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        //Create the user
        SiteUser newUser = new SiteUser(username, hashedPW, firstName, lastName, formattedDate, bio);
        //Save the user
        siteUserRepository.save(newUser);
        //Auto login
        autoAuthWithHttpServletRequest(username, password);
        //Return redirectView
        return new RedirectView("/");
    }

    @PostMapping("/addPost")
    public RedirectView createPost(Principal p, String postBody, Model m) {
        if (p != null){
            String username = p.getName();
            SiteUser siteUser = siteUserRepository.findByUsername(username);
            m.addAttribute("addPost", siteUser.getListOfPosts());
            Date date = new Date();
        Posts newPost = new Posts(postBody, date);

        postsRepository.save(newPost);

//        autoAuthWithHttpServletRequest();

        }
        return new RedirectView("/myProfile");
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
    @PutMapping("/follow-user/{id}")
    public RedirectView followUser(Principal p, @PathVariable Long id) {
        SiteUser userToFollow = siteUserRepository.findById(id).orElseThrow(() -> new RuntimeException("Error reading" +
                " " +
                "user from the database with ID of: " + id));
        SiteUser browsingUser = siteUserRepository.findByUsername(p.getName());

        //Check user not trying to follow their self
        if(browsingUser.getUsername().equals(userToFollow.getUsername())) {
            throw new IllegalArgumentException("You can't follow yourself");
        }

        //access followers from browsing User and update with new userToFollow
        browsingUser.getUsersIFollow().add(userToFollow);

        //Save to the DB
        siteUserRepository.save(browsingUser);

    return new RedirectView("/user/" + id);
    }
}
