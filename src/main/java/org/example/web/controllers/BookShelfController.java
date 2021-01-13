package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model, @RequestParam(value = "filter", required = false) String filterValue) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getBooks(filterValue));
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdToRemove) {
        if (bookService.removeBookById(bookIdToRemove)) {
            logger.info("Books id=" + bookIdToRemove + " is deleted");
        } else {
            logger.info("Book id = " + bookIdToRemove + " is not find");
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/removeAll")
    public String removeAll(@RequestParam(value = "booksToRemove") String booksToRemove) {
        if (bookService.removeAllBook(booksToRemove)) {
            logger.info("Books with template " + booksToRemove + " is deleted");
        } else {
            logger.info("Book with template " + booksToRemove + " is not find");
        }
        return "redirect:/books/shelf";
    }
}
