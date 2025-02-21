package com.game.university_platformer_game;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerStats {
    private int healthPoints;
    private int knowledgePoints;
    private int experiencePoints;
    private int reputationPoints;

    private static final int MAX_POINTS = 100;
    private static final int MAX_HP = 100000;
    private static final int BAR_WIDTH = 300;  // Width of the progress bar
    private static final int BAR_HEIGHT = 30; // Height of the progress bar

    private Label hpLabel, kpLabel, epLabel, rpLabel;
    private Rectangle hpBar, kpBar, epBar, rpBar;

    public PlayerStats(Pane root) {
        this.healthPoints = MAX_HP;
        this.knowledgePoints = 0;
        this.experiencePoints = 0;
        this.reputationPoints = 0;

        initUI(root);
    }

    private void initUI(Pane root) {
        // Dark container for the stats
        StackPane container = new StackPane();
        container.setStyle("-fx-background-color: #333333; -fx-padding: 20px; -fx-background-radius: 10px;");
        container.setLayoutX(20);
        container.setLayoutY(20);

        // VBox for vertical layout
        VBox statsContainer = new VBox(15); // Increased space between stats
        statsContainer.setAlignment(Pos.TOP_LEFT);

        // Create stats rows
        HBox hpRow = createStatRow(FontAwesomeIcon.HEART, "HP", Color.RED);
        HBox kpRow = createStatRow(FontAwesomeIcon.BOOK, "KP", Color.LIMEGREEN);
        HBox epRow = createStatRow(FontAwesomeIcon.COGS, "EP", Color.CORNFLOWERBLUE);
        HBox rpRow = createStatRow(FontAwesomeIcon.STAR, "RP", Color.CRIMSON);

        // Add rows to stats container
        statsContainer.getChildren().addAll(hpRow, kpRow, epRow, rpRow);

        // Add stats container to the dark container
        container.getChildren().add(statsContainer);

        // Add the dark container to the root pane
        root.getChildren().add(container);
    }

    private HBox createStatRow(FontAwesomeIcon icon, String labelText, Color barColor) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        // Icon
        FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
        iconView.setFill(Color.WHITE);
        iconView.setGlyphSize(24);

        // Label
        Label label = new Label(labelText + ": ");
        label.setStyle("-fx-font-family: Arial; -fx-font-size: 18px; -fx-text-fill: white;");

        // Bar Container
        StackPane barContainer = new StackPane();
        barContainer.setStyle("-fx-background-color: darkgray; -fx-background-radius: 15px;");
        barContainer.setPrefSize(BAR_WIDTH, BAR_HEIGHT);

        // Dynamic Bar
        Rectangle bar = new Rectangle(0, BAR_HEIGHT, barColor);
        bar.setArcWidth(15);
        bar.setArcHeight(15);

        // Align bar to the left
        bar.setTranslateX(-BAR_WIDTH / 2.0); // Keeps the bar anchored to the left

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(10);
        bar.setEffect(shadow);

        barContainer.getChildren().add(bar);

        // Save references for updating bars
        switch (labelText) {
            case "HP":
                hpBar = bar;
                break;
            case "KP":
                kpBar = bar;
                break;
            case "EP":
                epBar = bar;
                break;
            case "RP":
                rpBar = bar;
                break;
        }

        // Add components to the row
        row.getChildren().addAll(iconView, label, barContainer);
        return row;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void decreaseHealthPoints(int amount) {
        healthPoints = Math.max(0, healthPoints - amount);
        updateBars();
    }

    public void increaseKnowledgePoints(int amount) {
        knowledgePoints = Math.min(MAX_POINTS, knowledgePoints + amount);
        updateBars();
    }

    public void increaseExperiencePoints(int amount) {
        experiencePoints = Math.min(MAX_POINTS, experiencePoints + amount);
        updateBars();
    }

    public void increaseHealthPoints(int amount) {
        healthPoints = Math.min(MAX_HP, healthPoints + amount); // Use MAX_HP as the cap
        updateBars();
    }

    public void increaseReputationPoints(int amount) {
        reputationPoints = Math.min(MAX_POINTS, reputationPoints + amount);
        updateBars();
    }

    public boolean isPlayerDead() {
        return healthPoints <= 0;
    }

    private void updateBars() {
        hpBar.setWidth((double) healthPoints / MAX_HP * BAR_WIDTH);
        hpBar.setTranslateX(-(BAR_WIDTH - hpBar.getWidth()) / 2.0); // Fix left alignment

        kpBar.setWidth((double) knowledgePoints / MAX_POINTS * BAR_WIDTH);
        kpBar.setTranslateX(-(BAR_WIDTH - kpBar.getWidth()) / 2.0); // Fix left alignment

        epBar.setWidth((double) experiencePoints / MAX_POINTS * BAR_WIDTH);
        epBar.setTranslateX(-(BAR_WIDTH - epBar.getWidth()) / 2.0); // Fix left alignment

        rpBar.setWidth((double) reputationPoints / MAX_POINTS * BAR_WIDTH);
        rpBar.setTranslateX(-(BAR_WIDTH - rpBar.getWidth()) / 2.0); // Fix left alignment
    }

    public int getScore(){
        int maxExperiencePoints = 100;  // Replace with the actual maximum value
        int maxHealthPoints = MAX_HP;     // Replace with the actual maximum value
        int maxKnowledgePoints = 100;  // Replace with the actual maximum value
        int maxReputationPoints = 100; // Replace with the actual maximum value

        int maxScore = maxExperiencePoints + maxHealthPoints + maxKnowledgePoints + maxReputationPoints;
        int rawScore = this.experiencePoints + this.healthPoints + this.knowledgePoints + this.reputationPoints;

        // Scale the raw score to be out of 100
        return (int) Math.round((rawScore / (double) maxScore) * 100);
    }
}
