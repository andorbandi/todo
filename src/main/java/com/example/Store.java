package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Store {

    private final File file = new File("todo.txt");

    public List<String> readTodos() throws IOException {
        List<String> todos = new ArrayList<>();

        if (!file.exists()) {
            file.createNewFile();
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                todos.add(scanner.nextLine());
            }
        }

        return todos;
    }

    public void saveTodos(List<String> todos) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            for (String todo : todos) {
                writer.write(todo);
                writer.write(System.lineSeparator());
            }
        }
    }
}
