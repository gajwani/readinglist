package com.example.readinglist;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Book {

  @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
  private String reader;
  private String isbn;
  @Size(min = 1) private String title;
  private String author;
  private String description;
  @NotNull @Enumerated(EnumType.STRING) private BookCategories category;

  public Book(BookCategories category, Reader reader, String isbn, String title, String author, String description) {
    super();
    this.category = category;
    this.reader = reader.getUsername();
    this.isbn = isbn;
    this.title = title;
    this.author = author;
    this.description = description;
  }

  public Book() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReader() {
    return reader;
  }

  public void setReader(String reader) {
    this.reader = reader;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCategory(BookCategories category) {
    this.category = category;
  }

  public BookCategories getCategory() {
    return category;
  }
}
