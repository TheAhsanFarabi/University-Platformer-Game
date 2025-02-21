package com.game.university_platformer_game;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import static com.game.university_platformer_game.Main.*;


public class Conversation {
    private Map<String, String[]> dialogueOptions;
    private boolean isDialogueOpen = false;
    Conversation(){
        initializeDialogueOptions();
    }

    private void initializeDialogueOptions() {
        dialogueOptions = new HashMap<>();
        dialogueOptions.put("Asslamu walaikum Sir", new String[]{"Walaikum Assalam", "start"});
        dialogueOptions.put("I am new here. Can you guide me?", new String[]{"Of course! What do you need help with?", "middle"});
        dialogueOptions.put("How can I improve my skills?", new String[]{"Participate in workshops and practice regularly.", "middle"});
        dialogueOptions.put("Thank you for your guidance!", new String[]{"You're welcome! All the best.", "end"});
    }

    public void showConversationWithProfessor(Pane root) {
        if (isDialogueOpen) return;
        isDialogueOpen = true;


        Label professorDialogue = new Label();
        professorDialogue.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-padding: 10px; -fx-font-size: 16px;");
        professorDialogue.setTranslateX(professor.getTranslateX() - 50);
        professorDialogue.setTranslateY(professor.getTranslateY() - 100);

        Label mainCharacterDialogue = new Label();
        mainCharacterDialogue.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-padding: 10px; -fx-font-size: 16px;");
        mainCharacterDialogue.setTranslateX(mainCharacter.getTranslateX() - 50);
        mainCharacterDialogue.setTranslateY(mainCharacter.getTranslateY() - 100);

        VBox dialogueBox = new VBox(10);
        dialogueBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10px; -fx-border-radius: 10px;");
        dialogueBox.setLayoutX(WINDOW_WIDTH / 2 - 150);
        dialogueBox.setLayoutY(WINDOW_HEIGHT - 200);

        for (Map.Entry<String, String[]> entry : new HashMap<>(dialogueOptions).entrySet()) {
            Button optionButton = new Button(entry.getKey());
            optionButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
            optionButton.setOnAction(e -> {
                mainCharacterDialogue.setText(entry.getKey());
                String[] response = dialogueOptions.remove(entry.getKey());
                String professorResponse = response[0];
                String stage = response[1];

                FadeTransition fadeInMain = createFadeTransition(mainCharacterDialogue, 0.0, 1.0);

                root.getChildren().add(mainCharacterDialogue);
                fadeInMain.play();
                root.getChildren().remove(dialogueBox);
            });
            dialogueBox.getChildren().add(optionButton);
        }

        root.getChildren().add(dialogueBox);
    }

    private FadeTransition createFadeTransition(Label label, double fromValue, double toValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), label);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        return fadeTransition;
    }
}
