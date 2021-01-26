package org.example.app.exceptions;

public class BookShelfFileException extends Exception {

    private String message;

    public BookShelfFileException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
