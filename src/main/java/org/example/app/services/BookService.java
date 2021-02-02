package org.example.app.services;


import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.BookField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public List<Book> getBooks(String filterValue) {
        return bookRepo.retreive(filterValue);
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public boolean removeAllBook(BookField bookField) {
        return bookRepo.removeAllBook(bookField);
    }

    public void defaultInit() {
        logger.info("default INIT in book service");
    }

    public void defaultDestroy() {
        logger.info("default DESTROY in book service");
    }

    public List<String> getFiles() {
        return bookRepo.getFiles();
    }

    public void addFile(String fileName) {
        bookRepo.addFile(fileName);
    }
}
