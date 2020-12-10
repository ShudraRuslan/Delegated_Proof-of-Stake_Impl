package com.example.demo.Classes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ValidatorRepo extends CrudRepository<Validator, Integer> {

    @Query("SELECT v.reputation FROM Validator v WHERE v.validatorId=?1")
    int getReputationByValidatorId(Integer id);

    @Query("SELECT MAX(v.reputation) FROM Validator v")
    int getMaxReputation();
}
