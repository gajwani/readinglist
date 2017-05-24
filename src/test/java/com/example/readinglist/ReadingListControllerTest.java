package com.example.readinglist;

import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReadingListControllerTest {

  @Autowired
  private WebApplicationContext webContext;

  private MockMvc mockMvc;

  Reader expectedReader;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webContext)
        .apply(springSecurity())
        .build();

    expectedReader = new Reader();
    expectedReader.setUsername("craig");
    expectedReader.setPassword("password");
    expectedReader.setFullname("Craig Walls");
  }

  @Test
  public void homePage_unauthenticatedUser() throws Exception {
    mockMvc.perform(get("/readinglist"))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "http://localhost/login"));
  }

  @Test
  @WithUserDetails("craig")
  public void homePage_authenticatedUser() throws Exception {
    mockMvc.perform(get("/readinglist"))
        .andExpect(status().isOk())
        .andExpect(view().name("readingList"))
        .andExpect(model().attribute("reader", samePropertyValuesAs(expectedReader)))
        .andExpect(model().attribute("books", hasSize(0)));
  }

  @Test
  @WithUserDetails("craig")
  @Ignore
  public void postBook() throws Exception {
    mockMvc.perform(post("/readinglist")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("title", "BOOK TITLE")
        .param("author", "BOOK AUTHOR")
        .param("isbn", "1234567890")
        .param("description", "DESCRIPTION"))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/readinglist"));

    Book expectedBook = new Book();
    expectedBook.setId(1L);
    expectedBook.setReader(expectedReader);
    expectedBook.setTitle("BOOK TITLE");
    expectedBook.setAuthor("BOOK AUTHOR");
    expectedBook.setIsbn("1234567890");
    expectedBook.setDescription("DESCRIPTION");

    mockMvc.perform(get("/readinglist"))
        .andExpect(status().isOk())
        .andExpect(view().name("readingList"))
        .andExpect(model().attributeExists("books"))
        .andExpect(model().attribute("reader", samePropertyValuesAs(expectedReader)))
        .andExpect(model().attribute("books", hasSize(1)));
  }
  }
