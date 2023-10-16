package com.example.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bookId")
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_id", columnDefinition = "uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID bookId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    @Min(0)
    private double price;

    @Column(name = "author")
    private String author;

    @Column(name = "quantity")
    @Min(0)
    private int quantity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
