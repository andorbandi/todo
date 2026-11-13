package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class MainController {

    private final Store store = new Store();
    private int editingIndex = -1;

    @FXML
    private TextArea NewToDoField;

    @FXML
    private Button modifyButton;

    @FXML
    private Label statusLabel;

    @FXML
    private ListView<String> todoList;

    @FXML
    void initialize() {
        try {
            todoList.getItems().setAll(store.readTodos());
        } catch (IOException e) {
            statusLabel.setText("Státusz: todo.txt beolvasása sikertelen: " + e.getMessage());
        }
    }

    @FXML
    void onClickAddButton(ActionEvent event) {
        if (editingIndex != -1) {
            statusLabel.setText("Státusz: előbb mentsd el a módosítást.");
            return;
        }

        String newTodo = NewToDoField.getText().trim();

        if (newTodo.isEmpty()) {
            statusLabel.setText("Státusz: írj be egy teendőt.");
            return;
        }

        if (newTodo.contains("\n") || newTodo.contains("\r")) {
            statusLabel.setText("Státusz: egy teendő csak egy sor lehet.");
            return;
        }

        List<String> todos = new ArrayList<>(todoList.getItems());
        todos.add(newTodo);

        if (saveTodos(todos)) {
            NewToDoField.clear();
        }
    }

    @FXML
    void onClickDeleteButton(ActionEvent event) {
        if (editingIndex != -1) {
            statusLabel.setText("Státusz: előbb mentsd el a módosítást.");
            return;
        }

        int selectedIndex = todoList.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            statusLabel.setText("Státusz: jelölj ki egy teendőt a törléshez.");
            return;
        }

        List<String> todos = new ArrayList<>(todoList.getItems());
        todos.remove(selectedIndex);
        saveTodos(todos);
    }

    @FXML
    void onClickModifyButton(ActionEvent event) {
        if (editingIndex == -1) {
            editingIndex = todoList.getSelectionModel().getSelectedIndex();

            if (editingIndex == -1) {
                statusLabel.setText("Státusz: jelölj ki egy teendőt a módosításhoz.");
                return;
            }

            NewToDoField.setText(todoList.getItems().get(editingIndex));
            modifyButton.setText("Mentés");
            statusLabel.setText("Státusz: írd át a teendőt, majd kattints a Mentés gombra.");
            return;
        }

        String modifiedTodo = NewToDoField.getText().trim();

        if (modifiedTodo.isEmpty()) {
            statusLabel.setText("Státusz: a teendő nem lehet üres.");
            return;
        }

        if (modifiedTodo.contains("\n") || modifiedTodo.contains("\r")) {
            statusLabel.setText("Státusz: egy teendő csak egy sor lehet.");
            return;
        }

        List<String> todos = new ArrayList<>(todoList.getItems());
        todos.set(editingIndex, modifiedTodo);

        if (saveTodos(todos)) {
            editingIndex = -1;
            NewToDoField.clear();
            modifyButton.setText("Módosítás");
        }
    }

    private boolean saveTodos(List<String> todos) {
        try {
            store.saveTodos(todos);
            todoList.getItems().setAll(todos);
            statusLabel.setText("Státusz: mentve.");
            return true;
        } catch (IOException e) {
            statusLabel.setText("Státusz: mentés sikertelen: " + e.getMessage());
            return false;
        }
    }

}
