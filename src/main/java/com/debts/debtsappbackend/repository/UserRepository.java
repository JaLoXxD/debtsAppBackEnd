package com.debts.debtsappbackend.repository;

import com.debts.debtsappbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.email = :value OR u.username = :value")
    Optional<User> findByEmailOrUsername(String value);
    User save(User user);
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :id")
    void updateLastLoginById(@Param("id") Long id, @Param("lastLogin") LocalDateTime lastLogin);
    @Modifying
    @Query("UPDATE User u SET u.password = :password, u.resetPassword = true WHERE u.email = :email")
    void recoverUserPasswordByEmail(@Param("email") String email, @Param("password") String password);
    @Modifying
    @Query("UPDATE User u SET u.password = :password, u.resetPassword = false WHERE u.id = :id")
    void updateUserPasswordById(Long id, String password);
    @Modifying
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.secondName = :secondName, u.secondLastName = :secondLastName, u.email = :email, u.updatedAt = :updatedAt, u.phone = :phone, u.salary = :salary WHERE u.id = :id")
    void updateUserById(Long id, String firstName, String lastName, String secondName, String secondLastName, String email, LocalDateTime updatedAt, String phone, BigDecimal salary);
}
