package com.example.readinglist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SecurityConfigTest {
  @Autowired private WebApplicationContext webContext;
  private MockMvc mockMvc;

  @Before
  public void beforeEach() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(webContext)
      .apply(springSecurity())
      .build();
  }

  @Test
  public void loggingIn_redirectsToReadingList() throws Exception {
    mockMvc.perform(
      post("/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("username", "craig")
        .param("password", "password")
    ).andExpect(status().is3xxRedirection())
      .andExpect(header().string("Location", "/"));
  }

  @Test
  public void readinglistPage_unauthenticatedUser_redirectsToLogin() throws Exception {
    mockMvc.perform(get("/readinglist"))
      .andExpect(status().is3xxRedirection())
      .andExpect(header().string("Location", "http://localhost/login"));
  }
}
