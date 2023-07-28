package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User save(User user);

    Optional<User> findByUsername(String userName);

    User findByEmail(String email);
}
