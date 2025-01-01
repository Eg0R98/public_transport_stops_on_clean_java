package com.dmdev.junit.service;

import org.junit.jupiter.api.*;


import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
//    private UserService userService;

    @BeforeAll
    void init() {
        System.out.println("Before all: ");
    }

//    @BeforeEach
//    void prepare() {
//        System.out.println("Before each: " + this);
//        userService = new UserService();
//    }
//
//    @Test
//    void usersEmptyIfNotUsersAdd() {
//        System.out.println("Test 1: " + this);
//        UserService us = new UserService();
//        var users = us.getAll();
//        Assertions.assertTrue(users.isEmpty(), () -> "UserList should be empty");
//    }
//
//    @Test
//    void userSizeIfUserAdd() {
//        System.out.println("Test 2: " + this);
//        UserService us = new UserService();
//        us.add(new User());
//        us.add(new User());
//
//        List<User> users = us.getAll();
//        Assertions.assertEquals(2, users.size());
//    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);

    }

    @AfterAll
    void closeConnectionPool() {
        System.out.println("After all: ");
    }
}
