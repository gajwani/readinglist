package com.example.readinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private ReaderRepository readerRepository;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
          .antMatchers("/readinglist").hasRole("READER")
          .antMatchers("/**").permitAll()
        .and()
//          .csrf().disable()
        .formLogin()
          .loginPage("/login").failureUrl("/login?error=true")
          .defaultSuccessUrl("/readinglist")
        .and()
        .rememberMe()
          .tokenValiditySeconds(2419200)
          .key("myapp")
        .and()
        .logout()
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
          .logoutSuccessUrl("/");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//// In-memory user store example
//    auth
//        .inMemoryAuthentication()
//          .withUser("user").password("password").roles("READER")
//          .and()
//          .withUser("admin").password("password").roles("READER", "ADMIN");

//    UserDetailsService user store example
    auth
        .userDetailsService(readerService());
  }

  @Bean
  public UserDetailsService readerService() {
    return new UserDetailsService() {

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = readerRepository.findOne(username);
        if (userDetails != null) {
          return userDetails;
        }
        throw new UsernameNotFoundException("User '" + username + "' not found.");
      }
    };
  }
}

