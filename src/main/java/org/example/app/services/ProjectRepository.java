package org.example.app.services;

import org.example.web.dto.BookField;
import java.util.List;


public interface ProjectRepository<T> {
    List<T> retreiveAll();

    List<T> retreive(String filterValue);

    List<String> getFiles();

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeAllBook(BookField bookField);

    void addFile(String fileName);
}
