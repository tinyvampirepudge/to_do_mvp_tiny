package com.tiny.todomvptiny;

/**
 * Created by tiny on 17/1/20.
 */

public class Calculator {
    public int add(int one, int another) {
        return one + another;
    }

    public int multiply(int one, int another) {
        return one * another;
    }

    public double divide(double dividend, double divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Divisor cannot be 0");
        }
        return dividend / divisor;
    }
}
