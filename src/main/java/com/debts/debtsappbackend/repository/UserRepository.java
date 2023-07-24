package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User save(User user);

    User findByUserName(String userName);

    User findByEmail(String email);
}
