package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public List<Book> retreive(String filterValue) {
        if(filterValue == null || filterValue.isEmpty())
            return retreiveAll();
        List<Book> books = new ArrayList<>();
        for(Book book : repo) {
            if(book.getAuthor().matches(filterValue) || book.getTitle().matches(filterValue) || book.getSize().toString().matches(filterValue)) {
                books.add(book);
            }
        }
        logger.info("Find " + books.size() + " books");
        return  books;
    }

    @Override
    public void store(Book book) {
        if(book.getAuthor().isEmpty() && book.getTitle().isEmpty() && book.getSize() == null) {
            logger.info("Empty book");
        } else {
            book.setId(book.hashCode());
            logger.info("store new book: " + book);
            repo.add(book);
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeAllBook(String bookToRemove) {
        return repo.removeIf(book -> book.getAuthor().matches(bookToRemove)) ||
                repo.removeIf(book -> book.getTitle().matches(bookToRemove)) ||
                repo.removeIf(book -> book.getSize().toString().matches(bookToRemove));
    }
}
