package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {

    List<T> retreiveAll();

    List<T> retreive(String filterValue);

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeAllBook(String bookToRemove);
}
