package org.example.app.services;

import org.example.app.exceptions.BookShelfFileException;
import org.example.web.dto.BookField;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    List<T> retreive(BookField bookField);

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeAllBook(BookField bookField);

    void saveFile(MultipartFile file, String savePath) throws BookShelfFileException, IOException;
}
