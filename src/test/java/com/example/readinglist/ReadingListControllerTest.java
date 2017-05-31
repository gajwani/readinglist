package com.example.readinglist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReadingListControllerTest {

  @Autowired
  private WebApplicationContext webContext;

  private MockMvc mockMvc;

  String expectedReader;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webContext)
        .apply(springSecurity())
        .build();
    expectedReader = "craig";
  }

  @Test
  @WithUserDetails("craig")
  public void readinglistPage_authenticatedUser_renders() throws Exception {
    mockMvc.perform(get("/readinglist"))
        .andExpect(status().isOk())
        .andExpect(view().name("readingList"))
        .andExpect(model().attribute("reader", samePropertyValuesAs(expectedReader)))
        .andExpect(model().attribute("books", hasSize(0)));
  }

  @Test
  @WithUserDetails("craig")
  public void addingBook_createsBook() throws Exception {
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
