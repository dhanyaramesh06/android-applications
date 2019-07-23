package com.example.inclass07;

import java.io.Serializable;

public class Expense implements Serializable {
    String name,category, date;
    double amount;

    public Expense() {
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
