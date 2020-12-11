package com.example.demo.Classes;


import static java.lang.Math.random;


public class Node {
    private double cash;


    public Node() {
        this.cash = 10000 * random();
    }


    public double getCash() {
        return cash;
    }


    public void setCash(double cash) {
        this.cash = cash;
    }

}
