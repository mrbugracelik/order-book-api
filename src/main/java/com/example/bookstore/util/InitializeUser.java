package com.example.bookstore.util;

import com.example.bookstore.model.Privelage;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Service
public class InitializeUser {

    private final UserRepository userRepository;

    public InitializeUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initUsers() {
        List<User> users = List.of(new User(UUID.randomUUID(), "bugra.celik", "bugra.celik", "mrbugracelik@gmail.com", Privelage.ADMIN),
                new User(UUID.randomUUID(), "alper.eren", "123456", "alper@gmail.com", Privelage.USER));
        userRepository.saveAll(users);
    }
}
