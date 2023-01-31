package com.codefellowship.d16Security.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Step 5: Create a WebSecurityConfig
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //Wire Up the SiteUserDetailsService Impl
    @Autowired
    SiteUserDetailsServiceImpl siteUserDetailsService;

    //PasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
    //Configure AuthenticationManagerBuilder

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Builder Pattern Method().config().config().config()
        auth.userDetailsService(siteUserDetailsService).passwordEncoder(passwordEncoder());
    }

//Configure HttpSecurity
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
                .cors().disable() //Cross Origin Resource Sharing
                .csrf().disable()
                //Request Section
                .authorizeRequests()
                .antMatchers("/", "/login", "/signup").permitAll()
              .anyRequest().authenticated()
                .and() //Separator
                    //Login Section
              .formLogin()
              .loginPage("/login")
              .and()
              //Logout Section
              .logout()
              .logoutSuccessUrl("/login");

    }
}
