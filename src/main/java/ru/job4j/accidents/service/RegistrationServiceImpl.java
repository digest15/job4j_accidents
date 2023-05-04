package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.AuthorityRepository;
import ru.job4j.accidents.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public Optional<User> createUser(User user) {
        User savedUser;
        try {
            user.setEnabled(true);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setAuthority(authorityRepository.findByAuthority("ROLE_USER"));
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                savedUser = null;
            } else {
                throw e;
            }
        }

        return Optional.ofNullable(savedUser);
    }

}
