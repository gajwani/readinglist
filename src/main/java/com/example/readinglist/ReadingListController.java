package com.example.readinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/readinglist")
public class ReadingListController {

  private ReadingListRepository readingListRepository;
  private AmazonProperties amazonProperties;

  @Autowired
  public ReadingListController(ReadingListRepository readingListRepository, AmazonProperties amazonProperties) {
    this.readingListRepository = readingListRepository;
    this.amazonProperties = amazonProperties;
  }

  @RequestMapping(method = RequestMethod.GET)
  public String readersBooks(Model model, Principal principal) {
    String reader = principal.getName();
    List<Book> readingList = readingListRepository.findByReader(reader);
    if (readingList != null) {
      model.addAttribute("books", readingList);
      model.addAttribute("reader", reader);
      model.addAttribute("amazonID", amazonProperties.getAssociateId());
    }
    return "readingList";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String addToReadingList(Model model, Principal principal, @Valid Book book, BindingResult bindingResult) {
    if(bindingResult.hasErrors()) {
      model.addAttribute("bookErrors", bindingResult.getAllErrors());
      return readersBooks(model, principal);
    }
    String reader = principal.getName();
    book.setReader(reader);
    readingListRepository.save(book);
    return "redirect:/readinglist";
  }
}
