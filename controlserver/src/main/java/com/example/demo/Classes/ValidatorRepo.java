package com.example.demo.Classes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ValidatorRepo extends CrudRepository<Validator, Integer> {

    @Query("SELECT v.validatorId from Validator  v ORDER BY v.electionCash DESC")
    List<Integer> getSortedIdList();

    Validator getValidatorByValidatorId(Integer id);
}
