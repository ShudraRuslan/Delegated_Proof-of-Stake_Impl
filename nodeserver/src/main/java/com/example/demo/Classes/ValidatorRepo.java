package com.example.demo.Classes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ValidatorRepo extends CrudRepository<Validator, Integer> {

    @Query("SELECT v.reputation FROM Validator v WHERE v.validatorId=?1")
    int getReputationByValidatorId(Integer id);

    @Query("SELECT MAX(v.reputation) FROM Validator v")
    int getMaxReputation();

    @Query("SELECT v.validatorId FROM Validator v")
    Set<Integer> getIdList();
}
