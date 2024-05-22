package com.nucleus.picpay.repositories;

import com.nucleus.picpay.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByDocument(String document);
    Optional<User> findByEmail(String Email);
}
