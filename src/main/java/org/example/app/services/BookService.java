package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.exceptions.BookShelfFileException;
import org.example.web.dto.Book;
import org.example.web.dto.BookField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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

    public List<Book> getBooks(BookField bookField) {
        return bookRepo.retreive(bookField);
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

    public void saveFile(MultipartFile file, String savePath) throws BookShelfFileException, IOException {
        bookRepo.saveFile(file, savePath);
    }

}
