package com.game.university_platformer_game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Character extends ImageView {

    // Constants for the character
    private static final double FRAME_WIDTH = 128;
    private static final double FRAME_HEIGHT = 128;
    private static final double WALK_SPEED = 4;
    private static final double RUN_SPEED = 8;

    private final Set<KeyCode> activeKeys = new HashSet<>();
    private Timeline animation;
    private Timeline attackAnimation;
    private Timeline deathAnimation;
    private int currentFrame = 0;

    // Spritesheets
    private final Image walkSpriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/sprites/walk.png")));
    private final Image runSpriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/sprites/run.png")));
    private final Image idleSpriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/sprites/idle.png")));
    private final Image deadSpriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/sprites/dead.png")));
    private final Image attackSpriteSheet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/sprites/attack.png")));

    private boolean isFacingRight = true;
    private boolean isDead = false;
    private boolean isAttacking = false;

    public boolean isDead() {
        return isDead;
    }

    // Return whether the character is currently attacking
    public boolean isAttacking() {
        return isAttacking;
    }


    public Character() {
        // Initialize with idle animation
        setImage(idleSpriteSheet);
        setFitWidth(FRAME_WIDTH);
        setFitHeight(FRAME_HEIGHT);
        setViewport(new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT));
        setTranslateX(300);
        setTranslateY(370);



        // Initialize animations
        initAnimation();
        initAttackAnimation();
        initDeathAnimation();
    }

    // Initialize the default animation
    private void initAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(100), e -> updateFrame()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void initAttackAnimation() {
        attackAnimation = new Timeline(
                new KeyFrame(Duration.millis(200), e -> updateAttackFrame(0)),
                new KeyFrame(Duration.millis(400), e -> updateAttackFrame(1)),
                new KeyFrame(Duration.millis(600), e -> updateAttackFrame(2)),
                new KeyFrame(Duration.millis(800), e -> updateAttackFrame(3))
        );
        attackAnimation.setCycleCount(1);
        attackAnimation.setOnFinished(event -> {
            isAttacking = false;
            setAnimationState(AnimationState.IDLE); // Return to IDLE
        });
    }

    private void initDeathAnimation() {
        int totalFrames = 6; // Assuming the death animation has 6 frames
        deathAnimation = new Timeline();

        for (int frame = 0; frame < totalFrames; frame++) {
            int finalFrame = frame;
            deathAnimation.getKeyFrames().add(
                    new KeyFrame(Duration.millis(frame * 200), e -> updateDeathFrame(finalFrame))
            );
        }

        deathAnimation.setCycleCount(1);
        deathAnimation.setOnFinished(event -> showGameOverPopup()); // Show "Game Over" popup when finished
    }



    private void updateFrame() {
        if (isDead || isAttacking) return;

        int totalFrames = 6;

        switch (currentState) {
            case WALK -> {
                setImage(walkSpriteSheet);
            }
            case RUN -> {
                setImage(runSpriteSheet);
                totalFrames = 10;
            }
            case IDLE -> {
                setImage(idleSpriteSheet);
            }
        }

        currentFrame = (currentFrame + 1) % totalFrames;
        setViewport(new Rectangle2D(currentFrame * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT));
    }


    // Update the frame for the death animation
    private void updateDeathFrame(int frame) {
        setImage(deadSpriteSheet);
        setViewport(new Rectangle2D(frame * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT));
    }

    // Update the frame for the attack animation
    private void updateAttackFrame(int frame) {
        setImage(attackSpriteSheet);
        setViewport(new Rectangle2D(frame * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT));
    }

    // Trigger the death animation
    public void triggerDeath() {
        if (isDead) return;
        isDead = true;
        animation.stop();
        attackAnimation.stop();
        setImage(deadSpriteSheet);
        deathAnimation.play();
    }

    // Trigger the attack animation
    public void triggerAttack() {
        if (isAttacking) return;

        isAttacking = true;
        attackAnimation.playFromStart();
    }
    public void handleKeyPress(KeyCode code) {
        if (isDead) return; // Ignore key presses if the character is dead

        activeKeys.add(code); // Add the pressed key to the active set

        // Check if the character should walk or run
        if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.LEFT)) {
            boolean isRunning = activeKeys.contains(KeyCode.SHIFT); // Check if running
            setAnimationState(isRunning ? AnimationState.RUN : AnimationState.WALK); // Set state to RUN or WALK
        }

        // Trigger attack if the 'X' key is pressed
        if (code == KeyCode.X) {
            triggerAttack(); // Start attack animation
        }
    }

    public void handleKeyRelease(KeyCode code) {
        activeKeys.remove(code); // Remove the released key from the active set

        // If no movement keys are active, set the state to IDLE
        if (!activeKeys.contains(KeyCode.RIGHT) && !activeKeys.contains(KeyCode.LEFT)) {
            setAnimationState(AnimationState.IDLE);
        } else if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.LEFT)) {
            // If SHIFT is released but movement keys are still pressed, switch to WALK
            boolean isRunning = activeKeys.contains(KeyCode.SHIFT);
            setAnimationState(isRunning ? AnimationState.RUN : AnimationState.WALK);
        }
    }




    public void update() {
        if (isDead) return; // Stop updating if the character is dead

        // Handle movement based on active keys
        double speed = activeKeys.contains(KeyCode.SHIFT) ? RUN_SPEED : WALK_SPEED;

        if (activeKeys.contains(KeyCode.RIGHT)) {
            if (!isFacingRight) {
                // Flip to face right
                setScaleX(6); // Adjust scale to match facing direction
                isFacingRight = true;
            }
            setTranslateX(getTranslateX() + speed); // Move right
        } else if (activeKeys.contains(KeyCode.LEFT)) {
            if (isFacingRight) {
                // Flip to face left
                setScaleX(-6); // Adjust scale to match facing direction
                isFacingRight = false;
            }
            setTranslateX(getTranslateX() - speed); // Move left
        }
    }

    private void showGameOverPopup() {
        Pane overlay = new Pane();
        overlay.setPrefSize(1920, 1080);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setStyle("-fx-text-fill: red; -fx-font-size: 64px; -fx-font-weight: bold;");
        gameOverLabel.setLayoutX(700);
        gameOverLabel.setLayoutY(300);

        overlay.getChildren().addAll(gameOverLabel);
        ((Pane) getScene().getRoot()).getChildren().add(overlay);
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }



    public enum AnimationState {
        IDLE, WALK, RUN, ATTACK, DEAD
    }

    private AnimationState currentState = AnimationState.IDLE;

    public void setAnimationState(AnimationState state) {
        if (currentState == state || isDead) return;
        currentState = state;

        switch (state) {
            case IDLE -> setImage(idleSpriteSheet);
            case WALK -> setImage(walkSpriteSheet);
            case RUN -> setImage(runSpriteSheet);
            case ATTACK -> triggerAttack(); // Use existing attack animation logic
            case DEAD -> triggerDeath();   // Use existing death animation logic
        }
    }

    public AnimationState getAnimationState() {
        return currentState;
    }

}