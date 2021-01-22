package org.example.web.dto;

import javax.validation.constraints.NotEmpty;

public class BookToRemove {

    @NotEmpty
    private String bookToRemove;

    public void setBookToRemove(String bookToRemove) {
        this.bookToRemove = bookToRemove;
    }

    public String getBookToRemove() {
        return bookToRemove;
    }

}
