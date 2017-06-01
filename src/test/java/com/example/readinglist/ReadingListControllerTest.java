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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReadingListControllerTest {
  @Autowired private WebApplicationContext webContext;
  @Autowired private ReadingListRepository readinglistRepository;
  @Autowired private ReaderRepository readerRepository;
  private String expectedReader;
  private MockMvc mockMvc;

  @Before
  public void beforeEach() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(webContext)
      .apply(springSecurity())
      .build();
    expectedReader = "craig";
  }

  @Test
  @WithUserDetails("craig")
  public void readinglistPage_authenticatedUser_showsAllBooksForReader() throws Exception {

    Reader otherReader = readerRepository.save(new Reader("Other Reader","otherReader",  "password"));
    Reader expectedReaderObject = readerRepository.findByUsername(expectedReader);
    readinglistRepository.save(new Book(BookCategories.NonFiction, expectedReaderObject, "213213","Book 1", "Mark Twain", "Description 1"));
    readinglistRepository.save(new Book(BookCategories.NonFiction, expectedReaderObject, "213214","Book 2", "Mark Twain", "Description 2"));
    readinglistRepository.save(new Book(BookCategories.NonFiction, otherReader, "213215","Book 3", "Mark Twain", "Description 3"));

    mockMvc.perform(get("/readinglist"))
      .andExpect(status().isOk())
      .andExpect(view().name("readingList"))
      .andExpect(model().attribute("reader", samePropertyValuesAs(expectedReader)))
      .andExpect(model().attribute("books", hasSize(2)));
  }

  @Test
  @WithUserDetails("craig")
  public void addingBook_createsBook() throws Exception {
    assertEquals(0, readinglistRepository.findAll().size());

    mockMvc.perform(post("/readinglist")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("category", BookCategories.Reference.name())
      .param("title", "BOOK TITLE")
      .param("author", "BOOK AUTHOR")
      .param("isbn", "1234567890")
      .param("description", "DESCRIPTION"))
      .andExpect(status().is3xxRedirection())
      .andExpect(header().string("Location", "/readinglist"));

    List<Book> books = readinglistRepository.findAll();
    assertEquals(1, books.size());
    Book firstBook = books.get(0);

    assertNotNull(firstBook.getId());
    assertEquals(expectedReader, firstBook.getReader());
    assertEquals(BookCategories.Reference, firstBook.getCategory());
    assertEquals("BOOK TITLE", firstBook.getTitle());
    assertEquals("BOOK AUTHOR", firstBook.getAuthor());
    assertEquals("1234567890", firstBook.getIsbn());
    assertEquals("DESCRIPTION", firstBook.getDescription());
  }

  @Test
  @WithUserDetails("craig")
  public void addingBook_withMissingCategory_doesNotCreateBook() throws Exception {
    readinglistRepository.save( new Book(BookCategories.NonFiction, readerRepository.findByUsername(expectedReader), "213213","Book 1", "Mark Twain", "Description 1"));
    assertEquals(1, readinglistRepository.findAll().size());

    mockMvc.perform(
      post("/readinglist")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("category", "")
        .param("title", "Uncategorized BOOK")
        .param("author", "Some Author")
        .param("isbn", "1234567891")
        .param("description", "DESCRIPTION")
    ).andExpect(status().isOk())
      .andExpect(model().attribute("reader", samePropertyValuesAs(expectedReader)))
      .andExpect(model().attribute("books", hasSize(1)));

    assertEquals(1, readinglistRepository.findAll().size());
  }
}
