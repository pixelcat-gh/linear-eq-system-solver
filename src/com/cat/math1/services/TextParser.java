package com.cat.math1.services;

import com.cat.math1.entities.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TextParser {

    public static Matrix fileEntry(String filename) throws NoSuchElementException, FileNotFoundException {
        Scanner scn = new Scanner(new File(filename));
        int size = scn.nextInt();

        if (size < 0 || size > 20) throw new IncorrectSizeException();

        double[][] table = new double[size][size+1];

        for (int y = 0; y < size; y++)
        for (int x = 0; x < size + 1; x++)
            table[y][x] = scn.nextDouble();

        Matrix res = new Matrix(size);
        res.setTable(table);

        return res;
    }

    public static double[][] consoleEntry(int size) throws NoSuchElementException {
        double[][] res = new double[size][size+1];
        System.out.println("Please input " + size + "*" + (size+1) +
                " doubles (using comma as a decimal separator), separated by spaces and/or newlines:");
        Scanner scn = new Scanner(System.in);

        for (int y = 0; y < size; y++)
        for (int x = 0; x < size + 1; x++)
            res[y][x] = scn.nextDouble();

        return res;
    }
}
