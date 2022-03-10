package com.cat.math1.services;

import com.cat.math1.entities.Matrix;
import com.cat.math1.entities.Results;
import com.cat.math1.exceptions.IncorrectSizeException;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Process {

    public Process() {
        startMessage();
        process();
        exitMessage();
    }

    private void process() {
        String temp;

        while (true) {
            temp = prompt("complab1>");
            if (temp.equals("q") || temp.equals("quit")) break;

            switch (temp) {
                case "h", "help"     -> help();
                case "inpf"          -> parseFile();
                case "inpc"          -> parseConsole(inputSize());
                case "inpr"          -> genRandom(inputSize());
                case "inprr"         -> genRandom(randomSize());
                default              -> err();
            }
        }
    }

    private void startMessage() {
        System.out.println("Welcome to the matrix solver. To see the list of commands, type \"h\". " +
                "To quit, type \"q\".");
        printLine();
    }

    private void exitMessage() {
        printLine();
        System.out.println("Exiting...");
    }

    private void printLine() {
        System.out.println("----------------------------------------");
    }

    private void help() {
        printLine();
        System.out.println("""
                Command list:
                inpf  -- input the matrix from file;
                inpc  -- input the matrix from console;
                inpr  -- generate a random matrix with an input size;
                inprr -- generate a random matrix with a random size;
                q     -- quit the program;
                h     -- display this message;""");
        printLine();
    }

    private void err() {
        System.out.println("Incorrect command. To see the list of commands, type \"h\".");
    }

    private void parseFile() {
        printLine();
        String filename = prompt("Input the filename:");
        printLine();

        Matrix m;
        try {
            m = TextParser.fileEntry(filename);
        }
        catch (IncorrectSizeException e) {
            System.out.println("Please provide a matrix with a valid size.");
            printLine();
            return;
        }
        catch (InputMismatchException e) {
            System.out.println("Encountered an error attempting to parse input from file.");
            printLine();
            return;
        }
        catch (NoSuchElementException e) {
            System.out.println("Not enough numbers in the file.");
            printLine();
            return;
        }
        catch (FileNotFoundException e) {
            System.out.println("Unable to read file.");
            printLine();
            return;
        }

        calculate(m);
    }

    private void parseConsole(int size) {
        printLine();

        Matrix m = new Matrix(size);
        double[][] table;
        try { table = TextParser.consoleEntry(size); }
        catch (InputMismatchException e) {
            System.out.println("Encountered an error attempting to parse your input.");
            printLine();
            return;
        }
        catch (NoSuchElementException e) { return; }

        m.setTable(table);

        calculate(m);
    }

    private void calculate(@NotNull Matrix m) {
        System.out.println("The matrix is:");
        m.print();

        if (confirm()) {
            Matrix dup = m.returnDuplicate();

            Results res = m.calcRes();
            System.out.println("The results are:");
            res.print();

            if (res.isValid()) {
                System.out.println("The residual error is:");
                dup.calcErr(res).print();
            }
        }
        printLine();
    }

    private void genRandom(int size) {
        printLine();

        Matrix m = new Matrix(size);
        System.out.println("Generating random matrix...");
        m.fillRandom();

        calculate(m);
    }

    private int inputSize() {
        printLine();

        String temp;
        int ret;

        while (true) {
            temp = prompt("Input matrix size (1..20):");
            try {
                ret = Integer.parseInt(temp);
                if (ret >= 1 && ret <= 20) break;
                else System.out.println("Please input an integer that is above 0 and below 20.");
            }
            catch (NumberFormatException e) { System.out.println("Please input an integer."); }
        }

        return ret;
    }

    private int randomSize() {
        return new Random().nextInt(20) + 1;
    }

    private boolean confirm() {
        printLine();

        String temp;

        while (true) {
            temp = prompt("Confirm computation (y/n):");
            switch (temp) {
                case "y":
                    return true;
                case "n":
                    System.out.println("Aborted.");
                    return false;
                default:
                    break;
            }
        }
    }

    private String prompt(String prompt) {
        System.out.print(prompt);
        return readFromScanner(new Scanner(System.in));
    }

    private String readFromScanner(@NotNull Scanner scanner) {
        try { return scanner.nextLine(); }
        catch (NoSuchElementException e) { return "q"; }
    }
}
