package com.game.university_platformer_game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;

public class NPC extends ImageView {

    private static final double FRAME_WIDTH = 128; // Frame width in pixels
    private static final double FRAME_HEIGHT = 128; // Frame height in pixels
    private static final int TOTAL_FRAMES = 6; // Total number of frames
    private int currentFrame = 0;

    private final Timeline animation; // Animation timeline
    private Image spriteSheet; // The sprite sheet for the NPC

    public NPC(String spritePath) {
        // Load the specified sprite sheet
        spriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath)));

        // Set up the NPC sprite
        setImage(spriteSheet);
        setFitWidth(FRAME_WIDTH);
        setFitHeight(FRAME_HEIGHT);
        setViewport(new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT));

        // Set up the animation
        animation = new Timeline(new KeyFrame(Duration.millis(100), e -> updateFrame()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void updateFrame() {
        // Cycle through the sprite sheet frames
        currentFrame = (currentFrame + 1) % TOTAL_FRAMES;
        setViewport(new Rectangle2D(currentFrame * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT));
    }

    public void update() {
        // The NPC only animates (no movement logic needed)
    }

    public void setSpriteSheet(String spritePath) {
        // Change the sprite sheet dynamically
        spriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath)));
        setImage(spriteSheet);
        setViewport(new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT));
    }
}
