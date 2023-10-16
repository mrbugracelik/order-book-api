package com.example.bookstore.service;

import com.example.bookstore.exception.BadRequestException;
import com.example.bookstore.model.Book;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BookService {

    void add(Book book, HttpServletRequest request) throws BadRequestException, ServletException, IOException;

    void updateStockCount(UUID uuid, int newStockCount, HttpServletRequest request) throws Exception;

    List<Book> getAllBooks();

    Book findBookById(UUID id);

    void deleteBook(UUID isbn, HttpServletRequest request) throws ServletException, IOException;
}
