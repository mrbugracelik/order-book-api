package com.example.bookstore.service.impl;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderRequest;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.service.OrderService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    public OrderServiceImpl(OrderRepository orderRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public void addNewOrder(OrderRequest orderRequest) throws Exception {
        try {
            stockCheck(orderRequest.getBooks());
            Order order = processOrderRequest(orderRequest);
            orderRepository.save(order);
            updateStock(orderRequest.getBooks());
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public Order getOrderById(UUID orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new Exception();
        }
        return order.get();
    }

    @Override
    public List<Order> getOrderByUserId(String userId) {
        List<Order> ordersByUserId = orderRepository.findOrdersByUserId(userId);
        return ordersByUserId;
    }

    private Order processOrderRequest(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUser(orderRequest.getUser());
        order.setOrderTime(LocalDate.now());
        order.setCreatedAt(LocalDate.now());
        List<Book> books = new ArrayList<>();
        double totalPrice = 0d;
        for (Map.Entry entry : orderRequest.getBooks().entrySet()) {
            Optional<Book> book = bookRepository.findById(UUID.fromString(entry.getKey().toString()));
            if (book.isPresent()) {
                books.add(book.get());
                totalPrice += book.get().getPrice();
            }
        }
        order.setBookList(books);
        order.setTotalPrice(totalPrice);
        return order;
    }

    private void stockCheck(Map<String, Integer> books) throws Exception {
        for (Map.Entry entry : books.entrySet()) {
            Optional<Book> book = bookRepository.findById(UUID.fromString(entry.getKey().toString()));
            if (!book.isPresent()) {
                throw new Exception();
            }
            if ((Integer) entry.getValue() < 1 || book.get().getQuantity() < (Integer) entry.getValue()) {
                throw new Exception();
            }
        }
    }

    private void updateStock(Map<String, Integer> books) throws Exception {
        for (Map.Entry entry : books.entrySet()) {
            Optional<Book> book = bookRepository.findById(UUID.fromString(entry.getKey().toString()));
            if (!book.isPresent()) {
                throw new Exception();
            }
            book.get().setQuantity(book.get().getQuantity() - (Integer) entry.getValue());
            bookRepository.save(book.get());
        }
    }

}
