package com.example.bookstore.controller;

import com.example.bookstore.exception.BadRequestException;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<java.util.List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable UUID id) {
        return new ResponseEntity<>(bookService.findBookById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addNewBook(@RequestBody @NotNull Book book, HttpServletRequest request) {
        try {

            bookService.add(book, request);
            return new ResponseEntity<>("Book successfully saved.", HttpStatus.OK);
        } catch (BadRequestException badRequestException) {
            return new ResponseEntity<>(badRequestException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Book request failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateStockCount(@RequestParam @NotNull UUID bookId,
                                                   @RequestParam @Min(0) int newStockCount,
                                                   HttpServletRequest request) {
        try {
            bookService.updateStockCount(bookId, newStockCount, request);
            return new ResponseEntity<>("Stock count updated successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Stock count could not updated!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBook(@RequestParam @NotNull UUID isbn,
                                             HttpServletRequest request) {
        try {
            bookService.deleteBook(isbn, request);
            return new ResponseEntity<>("Deleted a book successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Deleted a book not deleted!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
