package com.game.university_platformer_game;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Objectives {

    private final VBox objectivesContainer;
    private final Map<String, Label> objectivesMap = new HashMap<>();
    private final Map<String, Boolean> objectivesStatus = new HashMap<>();
    private final Button toggleButton;
    private final Label titleLabel;
    private boolean isExpanded = false;

    public Objectives(double windowWidth) {
        // Create a container for the objectives
        objectivesContainer = new VBox();
        objectivesContainer.setSpacing(10); // Space between objectives
        objectivesContainer.setLayoutX(windowWidth - 550); // Adjusted position for better alignment
        objectivesContainer.setLayoutY(20); // Adjusted vertical position
        objectivesContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #2e2e2e, #1a1a1a); " + // Gradient background
                "-fx-padding: 10px; " +
                "-fx-border-radius: 8px; -fx-border-color: #4CAF50; -fx-border-width: 2px; " + // Rounded border
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 6, 0, 0, 4);");   // Shadow effect

        // Title label for the objectives section
        titleLabel = new Label("Objectives");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 5px;");
        objectivesContainer.getChildren().add(titleLabel);

        // Create a toggle button for expanding the objectives
        toggleButton = new Button("Show All");
        toggleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5px 10px;");
        toggleButton.setOnAction(event -> toggleObjectives());
        objectivesContainer.getChildren().add(toggleButton);
    }

    public VBox getObjectivesContainer() {
        return objectivesContainer;
    }

    public void addObjective(String id, String description) {
        if (objectivesMap.containsKey(id)) {
            throw new IllegalArgumentException("Objective with ID '" + id + "' already exists.");
        }

        // Create a label for the objective
        Label objectiveLabel = new Label(description);
        objectiveLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Arial';");
        objectivesMap.put(id, objectiveLabel);
        objectivesContainer.getChildren().add(objectiveLabel);
    }

    public void markObjectiveComplete(String id) {
        Label objectiveLabel = objectivesMap.get(id);
        if (objectiveLabel == null) {
            throw new IllegalArgumentException("Objective with ID '" + id + "' does not exist.");
        }

        // Update the objective text and style
        objectiveLabel.setText("✔ " + objectiveLabel.getText());
        objectiveLabel.setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");

        // Add a fade animation to indicate completion
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), objectiveLabel);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    public void resetObjectives() {
        objectivesMap.clear();
        objectivesContainer.getChildren().clear();
        objectivesContainer.getChildren().add(titleLabel); // Keep the title label
        objectivesContainer.getChildren().add(toggleButton); // Keep the toggle button
    }

    public boolean areAllObjectivesCompleted() {
        return objectivesStatus.values().stream().allMatch(Boolean::booleanValue); // Check if all are true
    }

    private void toggleObjectives() {
        isExpanded = !isExpanded;

        // Determine the number of visible objectives based on whether expanded or collapsed
        int visibleCount = isExpanded ? objectivesMap.size() : Math.min(3, objectivesMap.size()); // Show all or first 3

        int count = 0;
        for (Label label : objectivesMap.values()) {
            label.setVisible(count < visibleCount); // Show or hide objectives based on visible count
            count++;
        }

        // Dynamically calculate the height based on the number of visible objectives
        double newHeight = 30 + 40 + (visibleCount * 30); // 30px per visible objective

        // Apply a minimum height for when collapsed to avoid the VBox disappearing
        if (!isExpanded) {
            newHeight = Math.max(newHeight, 70); // Ensure the height is not too small
        }

        // Set the new preferred height for the VBox container
        objectivesContainer.setPrefHeight(newHeight);

        // Smoothly animate the container's expansion/collapse
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), objectivesContainer);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(isExpanded ? 0.8 : 1); // Start from a smaller scale when collapsing
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.play();

        // Change the button text based on the current state
        toggleButton.setText(isExpanded ? "Show Less" : "Show All");
    }


}
