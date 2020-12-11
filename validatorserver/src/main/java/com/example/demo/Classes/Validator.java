package com.example.demo.Classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer validatorId;
    private double electionCash;
    private int reputation;

    public Validator() {
        this.reputation = (int) (Math.random() * 6);
    }

    public Integer getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(Integer validatorId) {
        this.validatorId = validatorId;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public double getElectionCash() {
        return electionCash;
    }

    public void setElectionCash(double electionCash) {
        this.electionCash = electionCash;
    }
}
