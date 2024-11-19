package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    void delete(Long id);
    User findByUsername(String email);

    void saveUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void adminRedactor(User user, Long id);

    User getUserByLogin(String email);

    User findOne(Long id);


}