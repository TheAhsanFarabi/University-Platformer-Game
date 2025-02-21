package com.game.university_platformer_game;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Objects;

import static com.game.university_platformer_game.Main.*;

public class QueensPuzzle {
    private static final double WINDOW_WIDTH = 1920;
    private static final double WINDOW_HEIGHT = 820;
    private boolean isValidPosition(boolean[][] board, int row, int col, int size) {
        // Check column
        for (int i = 0; i < row; i++) {
            if (board[i][col]) return false;
        }

        // Check diagonal (upper-left to bottom-right)
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j]) return false;
        }

        // Check diagonal (upper-right to bottom-left)
        for (int i = row - 1, j = col + 1; i >= 0 && j < size; i--, j++) {
            if (board[i][j]) return false;
        }

        return true;
    }

    private boolean isPuzzleComplete(boolean[][] board, int size) {
        int queenCount = 0;

        // Count queens and validate their placement
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col]) {
                    queenCount++;
                    if (!isValidPosition(board, row, col, size)) return false;
                }
            }
        }

        // Ensure exactly 4 queens are placed and all are valid
        return queenCount == 4;
    }


    public void openPuzzleGUI(Pane root) {
        // Lock interactions
        Pane overlay = new Pane();
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        // Instruction Popup
        Pane instructionPopup = new Pane();
        instructionPopup.setPrefSize(500, 300);
        instructionPopup.setLayoutX((WINDOW_WIDTH - 500) / 2);
        instructionPopup.setLayoutY((WINDOW_HEIGHT - 300) / 2);
        instructionPopup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
        overlay.getChildren().add(instructionPopup);

        Label instructions = new Label(
                "Welcome to the 4 Queens Puzzle!\n\n" +
                        "Rules:\n" +
                        "1. Place 4 queens on the chessboard.\n" +
                        "2. No two queens should threaten each other.\n" +
                        "   - Queens cannot share the same row, column, or diagonal.\n\n" +
                        "Click a cell to place or remove a queen. Good luck!"
        );
        instructions.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        instructions.setWrapText(true);
        instructions.setLayoutX(20);
        instructions.setLayoutY(20);
        instructions.setPrefWidth(460);
        instructionPopup.getChildren().add(instructions);

        // Start Button
        Button startButton = new Button("Start Puzzle");
        startButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        startButton.setLayoutX(200);
        startButton.setLayoutY(250);
        instructionPopup.getChildren().add(startButton);

        startButton.setOnAction(event -> {
            root.getChildren().remove(instructionPopup);
            createPuzzlePopup(root, overlay); // Call the puzzle creation method
        });
    }

    private void createPuzzlePopup(Pane root, Pane overlay) {
        // Puzzle Popup
        Pane puzzlePopup = new Pane();
        puzzlePopup.setPrefSize(600, 600);
        puzzlePopup.setLayoutX((WINDOW_WIDTH - 600) / 2);
        puzzlePopup.setLayoutY((WINDOW_HEIGHT - 600) / 2);
        puzzlePopup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
        overlay.getChildren().add(puzzlePopup);

        // Title
        Label title = new Label("4 Queens Puzzle");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
        title.setLayoutX(200);
        title.setLayoutY(10);
        puzzlePopup.getChildren().add(title);

        // Chessboard Grid
        int size = 4; // 4x4 grid
        boolean[][] board = new boolean[size][size];
        GridPane chessBoard = new GridPane();
        chessBoard.setLayoutX(50);
        chessBoard.setLayoutY(50);
        chessBoard.setPrefSize(400, 400);
        puzzlePopup.getChildren().add(chessBoard);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(100, 100);
                cell.setStyle((row + col) % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: gray;");

                // Click to toggle queen placement
                int finalRow = row;
                int finalCol = col;
                cell.setOnMouseClicked(event -> {
                    if (board[finalRow][finalCol]) {
                        // Remove queen
                        board[finalRow][finalCol] = false;
                        cell.getChildren().clear();
                    } else {
                        // Check for conflicts
                        if (isValidPosition(board, finalRow, finalCol, size)) {
                            board[finalRow][finalCol] = true;
                            ImageView queen = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/objects/queen.png"))));
                            queen.setFitWidth(80);
                            queen.setFitHeight(80);
                            cell.getChildren().add(queen);
                        }
                    }

                    // Check for completion
                    if (isPuzzleComplete(board, size)) {
                        showToast(root, "+10 Experience Points! Task Complete.");
                        updateObjectives("objective1");
                        queensPuzzleCompleted = true;
                        PlayerStats playerStats = new PlayerStats(root);
                        playerStats.increaseExperiencePoints(10);
                        root.getChildren().remove(overlay);
                    }
                });

                chessBoard.add(cell, col, row);
            }
        }

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        closeButton.setLayoutX(250);
        closeButton.setLayoutY(500);
        puzzlePopup.getChildren().add(closeButton);

        closeButton.setOnAction(event -> root.getChildren().remove(overlay));
    }

}
