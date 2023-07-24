package com.debts.debtsappbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DebtPayment extends MongoRepository<DebtPayment, String> {

}
