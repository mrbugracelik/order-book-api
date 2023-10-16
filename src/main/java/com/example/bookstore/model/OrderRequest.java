package com.example.bookstore.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
public class OrderRequest {
    @NotNull
    private Map<String, Integer> books;
    @NotNull
    private User user;
}
