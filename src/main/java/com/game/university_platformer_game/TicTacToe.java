package com.game.university_platformer_game;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import static com.game.university_platformer_game.Main.*;

public class TicTacToe {
    private final char[][] board = new char[3][3]; // Game board: 'X', 'O', or '\0'
    private Button[][] cells;                     // GUI cells

    public void show(Pane root) {
        // Create a semi-transparent overlay
        Pane overlay = new Pane();
        overlay.setPrefSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        // Create the Tic-Tac-Toe grid
        GridPane grid = new GridPane();
        grid.setPrefSize(300, 300);
        grid.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
        grid.setLayoutX((Main.WINDOW_WIDTH - 300) / 2);
        grid.setLayoutY((Main.WINDOW_HEIGHT - 300) / 2);

        cells = new Button[3][3];

        // Initialize the cells
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = createCell(i, j, grid, root, overlay);
            }
        }

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16px;");
        closeButton.setOnAction(e -> root.getChildren().remove(overlay)); // Close GUI

        VBox popup = new VBox(10, grid, closeButton);
        popup.setStyle("-fx-alignment: center;");
        popup.setLayoutX((Main.WINDOW_WIDTH - 300) / 2);
        popup.setLayoutY((Main.WINDOW_HEIGHT - 350) / 2);

        overlay.getChildren().add(popup);
    }

    private Button createCell(int row, int col, GridPane grid, Pane root, Pane overlay) {
        Button cell = new Button();
        cell.setPrefSize(100, 100);
        cell.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        cell.setOnAction(e -> {
            if (cell.getText().isEmpty()) {
                cell.setText("X"); // Player's move
                board[row][col] = 'X';

                if (checkWinner('X')) {
                    showToast(root, "You Win!");
                    updateObjectives("objective5");
                    showToast(root, "+10 Experience Points! They are your friends now");
                    PlayerStats playerStats = new PlayerStats(root);
                    playerStats.increaseExperiencePoints(10);
                    root.getChildren().remove(overlay);
                    return;
                }

                if (isBoardFull()) {
                    showToast(root, "It's a Draw!");
                    root.getChildren().remove(overlay);
                    return;
                }

                // Delay bot move
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(ev -> {
                    botMove(root, overlay);
                });
                delay.play();
            }
        });

        grid.add(cell, col, row);
        return cell;
    }

    private void botMove(Pane root, Pane overlay) {
        int[] move = findBestMove();
        if (move != null) {
            int row = move[0];
            int col = move[1];
            board[row][col] = 'O';
            cells[row][col].setText("O");

            if (checkWinner('O')) {
                showToast(root, "Friend Wins!");
                root.getChildren().remove(overlay);
            } else if (isBoardFull()) {
                showToast(root, "It's a Draw!");
                root.getChildren().remove(overlay);
            }
        }
    }

    private int[] findBestMove() {
        // Try to win
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = 'O';
                    if (checkWinner('O')) {
                        board[i][j] = '\0';
                        return new int[]{i, j};
                    }
                    board[i][j] = '\0';
                }
            }
        }

        // Try to block the player
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = 'X';
                    if (checkWinner('X')) {
                        board[i][j] = '\0';
                        return new int[]{i, j};
                    }
                    board[i][j] = '\0';
                }
            }
        }

        // Pick the first available cell
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return new int[]{i, j};
                }
            }
        }

        return null;
    }

    private boolean checkWinner(char player) {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') return false;
            }
        }
        return true;
    }
}
