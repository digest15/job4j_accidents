package ru.job4j.accidents.service;

import ru.job4j.accidents.model.User;

import java.util.Optional;

public interface RegistrationService {
    Optional<User> createUser(User user);

    boolean userExist(User user);
}
