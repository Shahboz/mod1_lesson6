package org.example.web.controllers;


import org.apache.log4j.Logger;
import org.example.app.exceptions.BookShelfFileException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookField;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
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
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookField", new BookField());
        model.addAttribute("bookList", bookService.getBooks(filterValue));
        model.addAttribute("fileList", bookService.getFiles());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookField", new BookField());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("fileList", bookService.getFiles());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookField", new BookField());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("fileList", bookService.getFiles());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removeAll")
    public String removeAll(@Valid BookField bookField, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("fileList", bookService.getFiles());
            return "book_shelf";
        } else {
            bookService.removeAllBook(bookField);
            logger.info("Books with template " + bookField.getField() + " is deleted");
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws BookShelfFileException, IOException {
        String rootPath = System.getProperty("catalina.home") + File.separator + "external_uploads";

        if(file.isEmpty()) {
            throw new BookShelfFileException("Empty file");
        }
        String fileName = file.getOriginalFilename();
        bookService.addFile(fileName);
        byte[] bytes = file.getBytes();

        // create dir
        File dir = new File(rootPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile));
        outputStream.write(bytes);
        outputStream.close();
        logger.info("file saved at: " + serverFile.getAbsolutePath());

        return "redirect:/books/shelf";
    }

    @PostMapping("/downloadFile")
    public String downloadFile(@RequestParam(value = "fileName", required = false) String fileName) throws BookShelfFileException, IOException {
        if(fileName == null || fileName.isEmpty()) {
            throw new BookShelfFileException("Choose file to download");
        }
        String inputFileAbsolutePath = System.getProperty("catalina.home") + File.separator + "external_uploads" + File.separator + fileName;
        String outputFilePath = System.getProperty("catalina.home") + File.separator + "external_downloads";

        File inputFile = new File(inputFileAbsolutePath);
        if(!inputFile.exists() || inputFile.isDirectory()) {
            throw new BookShelfFileException("File '" + inputFileAbsolutePath + "' can not find");
        }

        // create dir
        File dir = new File(outputFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileChannel src = new FileInputStream(inputFile).getChannel();
        FileChannel dest = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileName).getChannel();
        dest.transferFrom(src, 0, src.size());
        dest.close();
        logger.info("File '" + fileName + "' copied");
        return "redirect:/books/shelf";
    }

    @ExceptionHandler(BookShelfFileException.class)
    public String handlerError(Model model, BookShelfFileException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/500";
    }

    @ExceptionHandler(IOException.class)
    public String handlerError(Model model, IOException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/500";
    }

}
