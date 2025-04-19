package com.example.lab44;

public class Rating {
    private double rate;
    private int count;

    // Конструктор
    public Rating(double rate, int count) {
        this.rate = rate;
        this.count = count;
    }

    // Геттеры
    public double getRate() { return rate; }
    public int getCount() { return count; }
}
