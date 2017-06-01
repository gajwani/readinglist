package com.example.readinglist;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookTest {
  @Test
  public void book_storesTheCorrectCateogry() {
    Reader reader = new Reader("Bob Smith", "bob", "secret");
    assertEquals(BookCategories.Magazine, new Book(BookCategories.Magazine, reader, null, "someTitle", null, null).getCategory());
    assertEquals(BookCategories.Fiction, new Book(BookCategories.Fiction, reader, null, "someTitle", null, null).getCategory());
  }
}
