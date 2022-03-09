package com.cat.math1.entities;

import com.cat.math1.exceptions.UnableToModifyMatrixException;

import java.util.Random;

public class Matrix {

    private final int size;
    private double[][] table;

    public Matrix(int size) {
        this.size = size;
        table = new double[size][size+1];
    }

    public void fillRandom() {
        for (int i = 0; i < size; i++)
            table[i] = new Random()
                    .doubles(size + 1, -20, 20)
                    .toArray();
    }

    public void setTable(double[][] table) {
        this.table = table;
    }

    public void print() {
        System.out.println("Size: " + size);
        for (double[] i : table) {
            for (int i2 = 0; i2 < size+1; i2++) {
                if (i2 == size) System.out.print("|");
                System.out.format("%15.4f ", i[i2]);
            }
            System.out.print('\n');
        }
    }

    public Results calcRes() {
        System.out.println("Checking if the main diagonal has zeroes on it...");

        if (hasZeroesOnMainDiagonal())
            try { modify(); }
            catch (UnableToModifyMatrixException e) {
                System.out.println("There is a column that's all zeroes.");
                return Results.returnInvalid();
            }
        else System.out.println("Zeroes not found, proceeding...");

        System.out.println("Triangulating the matrix...");
        triangulate();

        System.out.println("Calculating the results...");
        Results res = new Results(size);
        for (int i = size - 1; i >= 0; i--) {
            double subValue = 0;
            for (int ii = size - 1; ii > i; ii--)
                subValue += table[i][ii] * res.getAt(ii);

            //System.out.println("Processing... " + ((size - i)*100)/size + "%");
            res.setAt(i, table[i][size] - subValue);
        }
        return res;
    }

    private void normalizeRow(int row) {
        double del = table[row][row];
        for (int i = 0; i < size+1; i++)
            table[row][i] /= del;
    }

    private void subtractRows(int destination, int source, double multiplier) {
        for (int i = 0; i < size+1; i++)
            table[destination][i] = table[destination][i] - table[source][i]*multiplier;
    }

    private void addRows(int destination, int source) {
        for (int i = 0; i < size+1; i++)
            table[destination][i] = table[destination][i] + table[source][i];
    }

    private int findRowWithNoZeroInPlaceAtIndex(int index) throws UnableToModifyMatrixException {
        for (int i = 0; i < size; i++)
            if (table[i][index] != 0) return i;
        throw new UnableToModifyMatrixException();
    }

    private void cascadingSubtraction(int row) {
        for (int i = row + 1; i < size; i++)
            subtractRows(i, row, table[i][row]);
    }

    private void triangulate() {
        for (int i = 0; i < size; i++) {
            //System.out.println("Processing... " + (i*100)/size + "%");
            normalizeRow(i);
            cascadingSubtraction(i);
        }
    }

    private boolean hasZeroesOnMainDiagonal() {
        for (int i = 0; i < size; i++)
            if (table[i][i] == 0) return true;
        return false;
    }

    private void modify() throws UnableToModifyMatrixException {
        System.out.println("Zeroes found, attempting to modify the matrix...");

        for (int i = 0; i < size; i++)
            if (table[i][i] == 0)
                addRows(i, findRowWithNoZeroInPlaceAtIndex(i));
    }
}
