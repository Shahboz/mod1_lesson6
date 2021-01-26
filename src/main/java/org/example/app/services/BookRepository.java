package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.exceptions.BookShelfFileException;
import org.example.web.dto.Book;
import org.example.web.dto.BookField;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    // private final List<Book> repo = new ArrayList<>();
    private ApplicationContext context;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rownum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> retreive(BookField bookField) {
        if(bookField == null || bookField.getField() == null || bookField.getField().isEmpty())
            return retreiveAll();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", bookField.getField(), Types.VARCHAR);
        parameterSource.addValue("title", bookField.getField(), Types.VARCHAR);
        parameterSource.addValue("size", bookField.getField(), Types.VARCHAR);
        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE REGEXP_LIKE(author, :author) or REGEXP_LIKE(title, :title) or REGEXP_LIKE(size, :size)", parameterSource, (ResultSet rs, int rownum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author, title, size) VALUES (:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);
        //book.setId(context.getBean(IdProvider.class).providerId(book));
        // repo.add(book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        jdbcTemplate.update("DELETE FROM books where id = :id", parameterSource);
        logger.info("remove book completed");
        return true;
    }

    @Override
    public boolean removeAllBook(BookField bookField) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", bookField.getField(), Types.VARCHAR);
        parameterSource.addValue("title", bookField.getField(), Types.VARCHAR);
        parameterSource.addValue("size", bookField.getField(), Types.VARCHAR);
        jdbcTemplate.update("DELETE FROM books where REGEXP_LIKE(author, :author) or REGEXP_LIKE(title, :title) or REGEXP_LIKE(size, :size)", parameterSource);
        logger.info("remove all book completed");
        return true;
    }

    @Override
    public void saveFile(MultipartFile file, String savePath) throws BookShelfFileException, IOException {
        if(file.isEmpty()) {
            throw new BookShelfFileException("Empty file");
        }
        String fileName = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        // create dir
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile));
        outputStream.write(bytes);
        outputStream.close();
        logger.info("file saved at: " + serverFile.getAbsolutePath());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
