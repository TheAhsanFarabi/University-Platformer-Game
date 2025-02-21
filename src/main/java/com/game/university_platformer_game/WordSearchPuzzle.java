package com.game.university_platformer_game;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static com.game.university_platformer_game.Main.*;

public class WordSearchPuzzle extends Application {

    private final String[][] grid = {
            {"W", "O", "R", "D", "S", "E", "A", "R"},
            {"E", "A", "R", "C", "H", "G", "A", "M"},
            {"D", "I", "S", "C", "R", "E", "T", "E"},
            {"P", "U", "Z", "Z", "M", "E", "I", "J"},
            {"K", "L", "M", "N", "A", "P", "Q", "R"},
            {"S", "T", "U", "V", "T", "X", "Y", "Z"},
            {"G", "A", "M", "E", "H", "L", "A", "Y"},
            {"P", "U", "Z", "Z", "L", "E", "S", "E"}
    };

    private final List<String> wordsToFind = List.of("DISCRETE", "MATH");
    private final List<int[]> selectedCells = new ArrayList<>();
    private final List<String> foundWords = new ArrayList<>();

    private boolean isSelecting = false;
    private Label feedbackLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        openWordSearchPuzzle(root);

        primaryStage.setTitle("Word Search Puzzle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openWordSearchPuzzle(Pane root) {
        Pane overlay = new Pane();
        overlay.prefWidthProperty().bind(root.widthProperty());
        overlay.prefHeightProperty().bind(root.heightProperty());
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        VBox puzzleBox = new VBox(20);
        puzzleBox.setPrefSize(650, 650);
        puzzleBox.setLayoutX((800 - 650) / 2);
        puzzleBox.setLayoutY((600 - 650) / 2);
        puzzleBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; -fx-padding: 15px;");
        puzzleBox.setAlignment(Pos.CENTER);
        overlay.getChildren().add(puzzleBox);

        Label title = new Label("Word Search Puzzle");
        title.setFont(new Font(30));
        title.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        GridPane wordGrid = createGrid(root);
        VBox wordList = createWordList();

        feedbackLabel = new Label("Click cells to select words!");
        feedbackLabel.setFont(new Font(16));
        feedbackLabel.setStyle("-fx-text-fill: gray;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> root.getChildren().remove(overlay));

        puzzleBox.getChildren().addAll(title, feedbackLabel, wordGrid, wordList, closeButton);
    }

    private GridPane createGrid(Pane root) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Pane cell = createCell(gridPane, grid[row][col], row, col, root);
                gridPane.add(cell, col, row);
            }
        }

        return gridPane;
    }

    private Pane createCell(GridPane gridPane, String letter, int row, int col, Pane root) {
        StackPane cell = new StackPane();
        cell.setPrefSize(50, 50);

        Rectangle background = new Rectangle(50, 50);
        background.setFill(Color.LIGHTGRAY);
        background.setStroke(Color.BLACK);

        Label letterLabel = new Label(letter);
        letterLabel.setFont(new Font(22));
        letterLabel.setTextFill(Color.BLACK);

        cell.getChildren().addAll(background, letterLabel);
        cell.setOnMouseClicked(event -> handleCellSelection(gridPane, row, col, root));

        return cell;
    }

    private VBox createWordList() {
        VBox wordList = new VBox(10);
        wordList.setAlignment(Pos.CENTER_LEFT);
        wordList.setStyle("-fx-padding: 15px;");

        Label wordListTitle = new Label("Words to Find:");
        wordListTitle.setFont(new Font(20));
        wordListTitle.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        wordList.getChildren().add(wordListTitle);

        for (String word : wordsToFind) {
            Label wordLabel = new Label(word);
            wordLabel.setFont(new Font(18));
            wordLabel.setStyle("-fx-text-fill: black;");
            wordLabel.setId("wordLabel-" + word);
            wordList.getChildren().add(wordLabel);
        }

        return wordList;
    }

    private void handleCellSelection(GridPane gridPane, int row, int col, Pane root) {
        if (!isCellAlreadySelected(row, col)) {
            selectedCells.add(new int[]{row, col});
            highlightCell(gridPane, row, col, Color.LIGHTBLUE);
        }

        checkWordMatch(gridPane, root);
    }

    private boolean isCellAlreadySelected(int row, int col) {
        return selectedCells.stream().anyMatch(cell -> cell[0] == row && cell[1] == col);
    }

    private void highlightCell(GridPane gridPane, int row, int col, Color color) {
        int index = row * grid[0].length + col;
        Pane cell = (Pane) gridPane.getChildren().get(index);
        Rectangle background = (Rectangle) cell.getChildren().get(0);
        background.setFill(color);
    }

    private void checkWordMatch(GridPane gridPane, Pane root) {
        StringBuilder selectedWord = new StringBuilder();
        for (int[] cell : selectedCells) {
            selectedWord.append(grid[cell[0]][cell[1]]);
        }

        String forwardWord = selectedWord.toString();

        if (wordsToFind.contains(forwardWord) && !foundWords.contains(forwardWord)) {
            feedbackLabel.setText("Word Found: " + forwardWord);
            highlightWord(gridPane, selectedCells, Color.GREEN);
            foundWords.add(forwardWord);
            markWordAsFound(forwardWord);
            selectedCells.clear();

            if (foundWords.size() == wordsToFind.size()) {
                updateObjectives("objective3");
                showToast(root, "+10 Knowledge Points! You Found the book!!!");
                PlayerStats playerStats = new PlayerStats(root);
                playerStats.increaseKnowledgePoints(10);
            }
        } else if (!wordsToFind.contains(forwardWord)) {
            feedbackLabel.setText("Keep selecting!");
        }
    }

    private void highlightWord(GridPane gridPane, List<int[]> cells, Color color) {
        for (int[] cell : cells) {
            highlightCell(gridPane, cell[0], cell[1], color);
        }
    }

    private void markWordAsFound(String word) {
        Label wordLabel = (Label) feedbackLabel.getScene().lookup("#wordLabel-" + word);
        if (wordLabel != null) {
            wordLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold; text-decoration: line-through;");
        }
    }
}
