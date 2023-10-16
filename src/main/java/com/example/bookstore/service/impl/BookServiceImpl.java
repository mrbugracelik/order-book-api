package com.example.bookstore.service.impl;

import com.example.bookstore.exception.BadRequestException;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Privelage;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.util.JsonWebTokenUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final JsonWebTokenUtil jwtUtil;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository, JsonWebTokenUtil jwtUtil) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void add(Book book, HttpServletRequest request) throws BadRequestException, ServletException, IOException {
        if (isUserAdmin(request)) {
            applyBookValidation(book);
            book.setCreatedAt(LocalDateTime.now());
            bookRepository.save(book);
        } else {
            throw new BadRequestException("only admin users can be added a book");
        }
    }

    @Override
    public void updateStockCount(UUID bookId, int newStockCount, HttpServletRequest request) throws Exception {
        if (isUserAdmin(request)) {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isEmpty()) {
                throw new Exception();
            }
            Book dbBook = book.get();
            dbBook.setQuantity(newStockCount);
            dbBook.setUpdatedAt(LocalDateTime.now());
            bookRepository.save(dbBook);
        } else {
            throw new BadRequestException("only admin users can be updated a book");
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Book findBookById(UUID id) {
        boolean present = bookRepository.findById(id).isPresent();
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            return bookOptional.get();
        }
        throw new RuntimeException("book is not found");
    }

    @Override
    public void deleteBook(UUID isbn, HttpServletRequest request) throws ServletException, IOException {
        if (isUserAdmin(request)) {
            Optional<Book> byId = bookRepository.findById(isbn);
            if (byId.isPresent()) {
                bookRepository.deleteById(isbn);
            } else {
                throw new BadRequestException("book is not exists you can not delete");
            }
        } else {
            throw new BadRequestException("only admin users can be deleted a book");
        }
    }

    public void applyBookValidation(Book book) throws BadRequestException {
        if (book.getPrice() < 0.0) {
            throw new BadRequestException("Book price must be greater than 0!");
        } else if (book.getQuantity() < 0) {
            throw new BadRequestException("Quantity of the book must be either 0 or greater than 0!");
        }
    }


    protected boolean isUserAdmin(HttpServletRequest httpServletRequest) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String userName = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
            User byUserName = userRepository.findByUserName(userName);
            Privelage privelage = byUserName.getPrivelage();
            return privelage == Privelage.ADMIN;
        }

        return Boolean.FALSE;
    }
}
