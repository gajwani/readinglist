package com.example.readinglist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoutingTest {
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
  public void homePage_unauthenticatedUser_redirectsToReadingList() throws Exception {
    mockMvc.perform(get("/"))
      .andExpect(status().is3xxRedirection())
      .andExpect(header().string("Location", "/readinglist"));
  }

  @Test
  @WithUserDetails("craig")
  public void homePage_authenticatedUser_redirectsToReadingList() throws Exception {
    mockMvc.perform(get("/"))
      .andExpect(status().is3xxRedirection())
      .andExpect(header().string("Location", "/readinglist"));
  }
}
