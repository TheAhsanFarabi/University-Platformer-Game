package com.game.university_platformer_game;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Computer {

    private boolean isOpen = false; // Prevent multiple GUIs from opening
    private Pane desktopPane;      // To hold the desktop GUI
    private Pane overlay;          // To hold the code editor GUI

    public void open(Pane root, PlayerStats playerStats, Runnable onTaskComplete) {
        if (isOpen) return; // Prevent reopening the GUI
        isOpen = true;

        // Create the desktop GUI
        desktopPane = createDesktop();

        // Add a "Close Desktop" button
        Button closeDesktopButton = new Button("Close Desktop");
        closeDesktopButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        closeDesktopButton.setLayoutX(350); // Position the button at the center bottom of the desktop
        closeDesktopButton.setLayoutY(550);
        desktopPane.getChildren().add(closeDesktopButton);

        // Handle "Close Desktop" button click
        closeDesktopButton.setOnAction(event -> close(root));

        // Add a button to open the code editor
        Button openEditorButton = new Button("Open Code Editor");
        openEditorButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        openEditorButton.setLayoutX(300); // Position the button in the center of the desktop
        openEditorButton.setLayoutY(500);
        desktopPane.getChildren().add(openEditorButton);

        // Handle "Open Code Editor" button click
        openEditorButton.setOnAction(event -> showCodeEditor(root, playerStats, onTaskComplete));

        // Add the desktop to the root
        root.getChildren().add(desktopPane);
    }

    private Pane createDesktop() {
        // Create a pane to represent the desktop window
        Pane desktop = new Pane();
        desktop.setPrefSize(800, 600); // Desktop GUI size
        desktop.setLayoutX((Main.WINDOW_WIDTH - 800) / 2); // Center horizontally
        desktop.setLayoutY((Main.WINDOW_HEIGHT - 600) / 2); // Center vertically
        desktop.setStyle("-fx-border-color: black; -fx-border-width: 2px;"); // Optional border styling

        // Set the Windows XP wallpaper
        ImageView wallpaper = new ImageView(new Image(getClass().getResource("/com/game/university_platformer_game/desktop.jpg").toExternalForm()));
        wallpaper.setFitWidth(800); // Fit wallpaper to desktop size
        wallpaper.setFitHeight(600);
        desktop.getChildren().add(wallpaper);

        return desktop;
    }

    private void showCodeEditor(Pane root, PlayerStats playerStats, Runnable onTaskComplete) {
        if (overlay != null) return; // Prevent multiple code editor GUIs from opening

        // Create a semi-transparent overlay background
        overlay = new Pane();
        overlay.setPrefSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);"); // Dimmed background
        root.getChildren().add(overlay);

        // Create the popup container for the code editor
        Pane popup = new Pane();
        popup.setPrefSize(700, 500); // Code editor size
        popup.setLayoutX((Main.WINDOW_WIDTH - 700) / 2); // Center horizontally
        popup.setLayoutY((Main.WINDOW_HEIGHT - 500) / 2); // Center vertically
        popup.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: white; -fx-border-width: 2px; " +
                "-fx-border-radius: 10px; -fx-background-radius: 10px;"); // Styled popup
        overlay.getChildren().add(popup);

        // Code Editor (TextArea)
        TextArea codeEditor = new TextArea();
        codeEditor.setStyle("-fx-control-inner-background: #252526; -fx-text-fill: #dcdcdc; -fx-font-family: Consolas; -fx-font-size: 14px;");
        codeEditor.setPrefSize(650, 250);
        codeEditor.setLayoutX(25); // Padding inside the popup
        codeEditor.setLayoutY(25);
        codeEditor.setText("""
            #include <stdio.h>

            int main() {
                printf();
                return 0;
            }
        """);
        popup.getChildren().add(codeEditor);

        // Output Console (TextArea)
        TextArea outputConsole = new TextArea();
        outputConsole.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-family: Consolas; -fx-font-size: 14px;");
        outputConsole.setEditable(false);
        outputConsole.setPrefSize(650, 150);
        outputConsole.setLayoutX(25);
        outputConsole.setLayoutY(300);
        popup.getChildren().add(outputConsole);

        // Run Button
        Button runButton = new Button("Run");
        runButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        runButton.setLayoutX(575); // Position near the bottom-right of the popup
        runButton.setLayoutY(475);
        popup.getChildren().add(runButton);

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        closeButton.setLayoutX(475); // Position near the bottom-right of the popup
        closeButton.setLayoutY(475);
        popup.getChildren().add(closeButton);

        // Handle Run Button Click
        runButton.setOnAction(event -> {
            String code = codeEditor.getText();
            simulateCompileAndRun(code, outputConsole, playerStats, onTaskComplete);
        });

        // Handle Close Button Click
        closeButton.setOnAction(event -> {
            root.getChildren().remove(overlay);
            overlay = null; // Reset overlay
        });
    }

    private void simulateCompileAndRun(String code, TextArea outputConsole, PlayerStats playerStats, Runnable onTaskComplete) {
        outputConsole.clear();

        if (!code.contains("int main()")) {
            outputConsole.appendText("Error: Missing main function.\nHint: Ensure you have 'int main()' defined.\n");
            return;
        }

        if (!code.contains("printf(")) {
            outputConsole.appendText("Error: Missing printf statement.\nHint: Use printf to display output.\n");
            return;
        }

        if (code.contains("printf(\"Hello World!\");")) {
            outputConsole.appendText("Compilation successful!\n");
            outputConsole.appendText("Output:\nHello, World!\n");
            playerStats.increaseKnowledgePoints(10);
            onTaskComplete.run(); // Notify that the task is complete
        } else {
            outputConsole.appendText("Compilation successful with warnings.\n");
            outputConsole.appendText("Output:\n" + simulateOutputFromCode(code) + "\n");
        }
    }

    private String simulateOutputFromCode(String code) {
        if (code.contains("printf(\"")) {
            int startIndex = code.indexOf("printf(\"") + 8;
            int endIndex = code.indexOf("\");", startIndex);
            if (startIndex > 0 && endIndex > startIndex) {
                return code.substring(startIndex, endIndex);
            }
        }
        return "No valid output.";
    }

    public void close(Pane root) {
        root.getChildren().remove(desktopPane);
        desktopPane = null; // Reset desktop pane
        isOpen = false;
    }
}
