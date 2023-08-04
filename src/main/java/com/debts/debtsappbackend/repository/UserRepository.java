package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User save(User user);

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    @Query("{'email': ?0}")
    @Update("{ '$set': {'password': ?1, 'resetPassword':  true} }")
    void recoverUserPasswordByEmail(@Param("email") String email, @Param("password") String password);

    @Query("{'_id': ?0}")
    @Update("{ '$set': {'password': ?1, 'resetPassword':  false} }")
    void updateUserPasswordById(String email, String password);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set': { 'firstName': ?1, 'lastName': ?2, 'secondName': ?3, 'secondLastName': ?4, 'email': ?5, 'updatedAt': ?6, 'phone': ?7, 'salary': ?8 } }")
    void updateUserById(String id, String firstName, String lastName, String secondName, String secondLastName, String email, LocalDateTime updatedAt, String phone, BigDecimal salary);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set': { 'lastLogin': ?1 } }")
    void updateLasLoginById(String id, LocalDateTime lastLogin);
}
