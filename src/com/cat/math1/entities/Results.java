package com.cat.math1.entities;

public class Results {

    private final int size;
    private final double[] table;

    public Results(int size) {
        this.size = size;
        table = new double[size];
    }

    public boolean isValid() {
        for (double i : table)
            if (Double.isInfinite(i) || Double.isNaN(i)) return false;
        return true;
    }

    public void print() {
        if (!isValid()) {
            System.out.println("No solution found.");
            return;
        }
        for (int i = 0; i < size; i++)
            System.out.format("x" + (i+1) + " = %15.4f%n", table[i]);
    }

    public void setAt(int id, double value) {
        table[id] = value;
    }

    public double getAt(int id) {
        return table[id];
    }
}
