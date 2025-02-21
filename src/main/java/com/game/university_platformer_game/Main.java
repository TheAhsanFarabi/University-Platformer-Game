package com.game.university_platformer_game;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Main extends Application {
    public static boolean queensPuzzleCompleted = false;
    private MediaPlayer backgroundMusic;
    public static final double WINDOW_WIDTH = 1920;
    public static final double WINDOW_HEIGHT = 820;
    private PlayerStats playerStats;
    private ImageView background;
    private Image bg1, bg2, bg3, bg4, bg5, bg6, bg7, bg8;
    private int currentScene = 1;

    public static Character mainCharacter;
    private final Map<Integer, Character> otherPlayers = new HashMap<>();
    private PrintWriter out;

    private NPC security;
    public static NPC professor;
    private NPC lecturer;
    private NPC friend;
    private NPC friend2;

    private Label Prompt;
    private Label foodPrompt;
    private Map<String, String[]> dialogueOptions;

    private Computer computer;
    private static Objectives objectives;
    private Button notebookButton = null;
    private static final List<String> lessons = new ArrayList<>();

    private static boolean allObjectivesCompleted = false;
    private boolean isDialogueOpen = false;
    private Label classTask;// Dialogue box
    private ImageView computer1;
    private ImageView computer2;
    private ImageView computer3;
    private Label computerPrompt;
    private Label bookPrompt;
    private Label shopPrompt;
    private Label libraryPrompt;
    private Label friendPrompt;
    private Label lecturerPromt;


    private TextField chatInputBox;
    private TextArea chatDisplayBox;
    private boolean isChatOpen = false;
    private PrintWriter outChat;
    private boolean examGUIShown = false;

    private static final Map<String, String> scoreboardUpdates = new ConcurrentHashMap<>();


    @Override
    public void start(Stage stage) {
        playBackgroundMusic();
        showMainMenu(stage);
    }

    private void showMainMenu(Stage stage) {

        StackPane menuRoot = new StackPane();

        ImageView background = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/menu.png"))
        ));
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);

        VBox menuContent = new VBox(20);
        menuContent.setStyle("-fx-alignment: center; -fx-padding: 50px;");

        Label title = new Label("University Platformer Game");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 48px; -fx-font-weight: bold;");
        menuContent.getChildren().add(title);

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 24px; -fx-padding: 10px 20px;");
        playButton.setOnAction(e -> showGameScene(stage));

        Button aboutButton = new Button("About");
        aboutButton.setStyle("-fx-font-size: 24px; -fx-padding: 10px 20px;");
        aboutButton.setOnAction(e -> showAboutScene(stage));

        Button quitButton = new Button("Quit");
        quitButton.setStyle("-fx-font-size: 24px; -fx-padding: 10px 20px;");
        quitButton.setOnAction(e -> stage.close());

        menuContent.getChildren().addAll(playButton, aboutButton, quitButton);

        menuRoot.getChildren().addAll(background, menuContent);

        Scene menuScene = new Scene(menuRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(menuScene);
        stage.setTitle("University Platformer - Main Menu");
        stage.show();
    }

    private void showAboutScene(Stage stage) {
        Pane aboutRoot = new Pane();
        aboutRoot.setStyle("-fx-background-color: black;");

        Label aboutText = new Label("University Platformer\nVersion 1.0\nCreated by Team Pixel Artisans");
        aboutText.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-alignment: center;");
        aboutText.setLayoutX(WINDOW_WIDTH / 2 - 200);
        aboutText.setLayoutY(WINDOW_HEIGHT / 2 - 100);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        backButton.setLayoutX(WINDOW_WIDTH / 2 - 50);
        backButton.setLayoutY(WINDOW_HEIGHT / 2 + 50);
        backButton.setOnAction(e -> showMainMenu(stage));

        aboutRoot.getChildren().addAll(aboutText, backButton);

        Scene aboutScene = new Scene(aboutRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(aboutScene);
    }


    private void playBackgroundMusic() {
        try {
            // Replace the path below with your audio file path
            String musicPath = Objects.requireNonNull(getClass()
                    .getResource("/com/game/university_platformer_game/music.mp3")).toExternalForm();
            Media media = new Media(musicPath);
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
            backgroundMusic.setVolume(1); // Set the volume (range: 0.0 to 1.0)
            backgroundMusic.play(); // Start playing
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }
    private void showGameScene(Stage stage) {
        Pane gameRoot = new Pane();
        gameRoot.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        bg1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/1.png")));
        bg2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/2.png")));
        bg3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/3.png")));
        bg4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/4.png")));
        bg5 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/5.png")));
        bg6 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/6.png")));
        bg7 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/7.png")));
        bg8 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/scenes/8.png")));

        background = new ImageView(bg1);
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);
        gameRoot.getChildren().add(background);

        // Add navigation buttons
        createNavigationButtons(gameRoot);
        playerStats = new PlayerStats(gameRoot);
        // Lessons Button
        Button lessonsButton = createLessonsButton(gameRoot);
        gameRoot.getChildren().add(lessonsButton);

        initializeDialogueOptions();

        security = new NPC("/com/game/university_platformer_game/sprites/NPC_idle3.png");
        security.setTranslateX(950);
        security.setTranslateY(220);
        security.setScaleX(4.5);
        security.setScaleY(4.5);
        security.setVisible(false);
        gameRoot.getChildren().add(security);

        professor = new NPC("/com/game/university_platformer_game/sprites/npcf2.png");
        professor.setTranslateX(1200);
        professor.setTranslateY(370);
        professor.setScaleX(-6);
        professor.setScaleY(6);
        professor.setVisible(false);
        gameRoot.getChildren().add(professor);

        lecturer = new NPC("/com/game/university_platformer_game/sprites/NPC_idle.png");
        lecturer.setTranslateX(500);
        lecturer.setTranslateY(220);
        lecturer.setScaleX(4.5);
        lecturer.setScaleY(4.5);
        lecturer.setVisible(false);
        gameRoot.getChildren().add(lecturer);

        computer = new Computer();


        // Create prompt
        Prompt = new Label("You must enter the university using your ID card. Press Q to enter");
        Prompt.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-padding: 25px; -fx-font-size: 20px;");
        Prompt.setVisible(false); // Hide by default
        Prompt.setTranslateX(security.getTranslateX()-300);
        Prompt.setTranslateY(security.getTranslateY() - 50);
        gameRoot.getChildren().add(Prompt);

        setupChatUI(gameRoot);
        // Create "Press F to eat food" prompt
        foodPrompt = new Label("Press F to eat food");
        foodPrompt.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px; -fx-font-size: 14px;");
        foodPrompt.setVisible(false);
        foodPrompt.setLayoutX(900);
        foodPrompt.setLayoutY(380);
        gameRoot.getChildren().add(foodPrompt);

        bookPrompt = new Label("Press B to find discrete math book");
        bookPrompt.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px; -fx-font-size: 14px;");
        bookPrompt.setVisible(false);
        bookPrompt.setLayoutX(200);
        bookPrompt.setLayoutY(380);
        gameRoot.getChildren().add(bookPrompt);

        shopPrompt = new Label("Press B to buy the notebook");
        shopPrompt.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px; -fx-font-size: 14px;");
        shopPrompt.setVisible(false);
        shopPrompt.setLayoutX(500);
        shopPrompt.setLayoutY(380);
        gameRoot.getChildren().add(shopPrompt);

        libraryPrompt = new Label("Press L to enter the library");
        libraryPrompt.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px; -fx-font-size: 14px;");
        libraryPrompt.setVisible(false);
        libraryPrompt.setLayoutX(950);
        libraryPrompt.setLayoutY(380);
        gameRoot.getChildren().add(libraryPrompt);

        friendPrompt = new Label("Hi There! Do you wanna play TicTacToe. Press T to play");
        friendPrompt .setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px; -fx-font-size: 14px;");
        friendPrompt .setVisible(false);
        friendPrompt .setLayoutX(980);
        friendPrompt .setLayoutY(380);
        gameRoot.getChildren().add(friendPrompt);

        lecturerPromt = new Label("Students! Welcome to the class. Let's start coding!");
        lecturerPromt .setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px; -fx-font-size: 14px;");
        lecturerPromt .setVisible(false);
        lecturerPromt .setLayoutX(lecturer.getTranslateX());
        lecturerPromt .setLayoutY(lecturer.getTranslateY()-10);
        gameRoot.getChildren().add(lecturerPromt);


// Initialize Objectives
        objectives = new Objectives(WINDOW_WIDTH);
        gameRoot.getChildren().add(objectives.getObjectivesContainer());

        // Add initial objectives
        objectives.addObjective("objective1", "Enter the University");
        objectives.addObjective("objective2", "Talk to the professor");
        objectives.addObjective("objective3", "Find Books in Library");
        objectives.addObjective("objective4", "Buy a notebook");
        objectives.addObjective("objective5", "Make a new friend");
        objectives.addObjective("objective6", "Eat a healthy food");
        objectives.addObjective("objective7", "Solve the coding problem");





        // Create Computer Object
        computer1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/objects/computer.png"))));
        computer1.setFitWidth(340);
        computer1.setFitHeight(250);
        computer1.setTranslateX(800);
        computer1.setTranslateY(480);
        computer1.setVisible(false);
        gameRoot.getChildren().add(computer1);

        computer2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/objects/computer.png"))));
        computer2.setFitWidth(340);
        computer2.setFitHeight(250);
        computer2.setTranslateX(1200);
        computer2.setTranslateY(480);
        computer2.setVisible(false);
        gameRoot.getChildren().add(computer2);

        computer3 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/objects/computer.png"))));
        computer3.setFitWidth(340);
        computer3.setFitHeight(250);
        computer3.setTranslateX(100);
        computer3.setTranslateY(480);
        computer3.setVisible(false);
        gameRoot.getChildren().add(computer3);

        classTask = new Label("Task: Print Hello World!");
        classTask.setLayoutX(850);
        classTask.setLayoutY(300);
        classTask.setStyle("-fx-text-fill: black; -fx-padding: 5px; -fx-font-size: 30px;");
        classTask.setVisible(false); // Hide by default
        gameRoot.getChildren().add(classTask);

        computerPrompt = new Label("Press Y to use computer");
        computerPrompt.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px; -fx-font-size: 14px;");
        computerPrompt.setVisible(false);
        computerPrompt.setLayoutX(750);
        computerPrompt.setLayoutY(380);
        gameRoot.getChildren().add(computerPrompt);


        friend = new NPC("/com/game/university_platformer_game/sprites/idle_friend.png");
        friend.setTranslateX(1200);
        friend.setTranslateY(370);
        friend.setScaleX(-6);
        friend.setScaleY(6);
        friend.setVisible(false);
        gameRoot.getChildren().add(friend);


        friend2 = new NPC("/com/game/university_platformer_game/sprites/NPC_idle2.png");
        friend2.setTranslateX(1300);
        friend2.setTranslateY(370);
        friend2.setScaleX(-6);
        friend2.setScaleY(6);
        friend2.setVisible(false);
        gameRoot.getChildren().add(friend2);


        mainCharacter = new Character();
        mainCharacter.setScaleX(6);
        mainCharacter.setScaleY(6);
        mainCharacter.setTranslateY(370);
        gameRoot.getChildren().add(mainCharacter);


        Scene gameScene = new Scene(gameRoot);
        stage.setScene(gameScene);
        stage.setTitle("University Platformer Game");
        stage.setResizable(true);



        gameScene.setOnKeyPressed(event -> {
            mainCharacter.handleKeyPress(event.getCode());


            if (currentScene == 1 && event.getCode() == KeyCode.Q) {
                QueensPuzzle queens = new QueensPuzzle();
                queens.openPuzzleGUI(gameRoot);
            }

            if (currentScene == 2 && Math.abs(professor.getTranslateX()- mainCharacter.getTranslateX() ) < 250) {
                  showConversationWithProfessor(gameRoot);
            }

            if ( (currentScene==3 || currentScene == 8) && event.getCode() == KeyCode.L) {
                handleSceneTransition(gameRoot);
            }

            if (currentScene == 8 && event.getCode() == KeyCode.B) {
                WordSearchPuzzle puzzle = new WordSearchPuzzle();
                puzzle.openWordSearchPuzzle(gameRoot);
            }

            if (currentScene == 4 && event.getCode() == KeyCode.B) {
                showNotebookGUI(gameRoot); // Show notebook GUI
            }

            // Open computer GUI when near and pressing Y
            if (currentScene == 7 && event.getCode().toString().equals("Y") && computerPrompt.isVisible()) {
                openComputerGUI(gameRoot);
            }

            if (currentScene == 5 && event.getCode() == KeyCode.T) {
                TicTacToe ticTacToe = new TicTacToe();
                ticTacToe.show(gameRoot);
            }

            if (currentScene == 6 && event.getCode().toString().equals("F")) {
                showFoodGUI(gameRoot);
            }


            if(event.getCode() == KeyCode.S){
                startRedLightGreenLight(gameRoot);
            }

        });


        gameScene.setOnKeyReleased(event -> mainCharacter.handleKeyRelease(event.getCode()));


        new AnimationTimer() {
            @Override
            public void handle(long now) {
                mainCharacter.update();
                playerStats.decreaseHealthPoints(5);
                checkProximityAndShowPrompt(gameRoot);
            }
        }.start();

        stage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGameLogic(gameRoot);
            }
        };
        gameLoop.start();
    }

    private boolean isPlayerNearby() {
        for (Character other : otherPlayers.values()) {
            double distance = Math.sqrt(
                    Math.pow(mainCharacter.getTranslateX() - other.getTranslateX(), 2) +
                            Math.pow(mainCharacter.getTranslateY() - other.getTranslateY(), 2)
            );
            System.out.println("Distance to player " + other.hashCode() + ": " + distance); // Debug
            if (distance < 100) { // Proximity threshold
                System.out.println("Player is nearby."); // Debug
                return true;
            }
        }
        System.out.println("No players nearby."); // Debug
        return false;
    }

    private void checkProximityAndShowPrompt(Pane root) {
        boolean nearby = isPlayerNearby();
        if (nearby && !isChatOpen) {
            showChatPrompt(root, "Press C to chat with nearby player!");
        } else {
            hideChatPrompt(root);
        }
    }

    private void showChatPrompt(Pane root, String message) {
        Label prompt = (Label) root.lookup("#chatPrompt");
        if (prompt == null) {
            prompt = new Label(message);
            prompt.setId("chatPrompt");
            prompt.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white; -fx-padding: 10px;");
            prompt.setLayoutX(WINDOW_WIDTH / 2 - 100);
            prompt.setLayoutY(WINDOW_HEIGHT - 100);
            root.getChildren().add(prompt);
        }
        prompt.setVisible(true);
    }

    private void hideChatPrompt(Pane root) {
        Label prompt = (Label) root.lookup("#chatPrompt");
        if (prompt != null) {
            prompt.setVisible(false);
        }
    }

    private void setupChatUI(Pane root) {
        // Chat display box
        chatDisplayBox = new TextArea();
        chatDisplayBox.setEditable(false);
        chatDisplayBox.setWrapText(true);
        chatDisplayBox.setPrefSize(400, 200);
        chatDisplayBox.setLayoutX(1200);
        chatDisplayBox.setLayoutY(500);
        chatDisplayBox.setVisible(false); // Hidden by default
        root.getChildren().add(chatDisplayBox);
        System.out.println("Chat display box added to the root."); // Debug

        // Chat input box
        chatInputBox = new TextField();
        chatInputBox.setPromptText("Type your message...");
        chatInputBox.setPrefWidth(400);
        chatInputBox.setLayoutX(1200);
        chatInputBox.setLayoutY(650);
        chatInputBox.setVisible(false); // Hidden by default
        root.getChildren().add(chatInputBox);
        System.out.println("Chat input box added to the root."); // Debug
    }

    private void updateGameLogic(Pane root) {
        if (currentScene == 1 && !queensPuzzleCompleted && mainCharacter.getTranslateX() > WINDOW_WIDTH-400) {
            showToast(root, "Press Q and Complete the Puzzle to Enter the university");
        } else if (mainCharacter.getTranslateX() > WINDOW_WIDTH) {
            transitionToScene(root, true); // Move to the next scene
        } else if (mainCharacter.getTranslateX() < 0) {
            transitionToScene(root, false); // Move to the previous scene
        }


        int healthPoints = playerStats.getHealthPoints();
        if (healthPoints <= 0 && !mainCharacter.isDead()) {
            mainCharacter.triggerDeath();
        }

        security.setVisible(currentScene == 1);
        Prompt.setVisible(currentScene==1 && mainCharacter.getTranslateX() > 800);
        libraryPrompt.setVisible(currentScene==3 && mainCharacter.getTranslateX() > 800);

        bookPrompt.setVisible(currentScene==8 && mainCharacter.getTranslateX() > 300);
        shopPrompt.setVisible(currentScene==4 && mainCharacter.getTranslateX() > 400);

        foodPrompt.setVisible(currentScene==6 && mainCharacter.getTranslateX() > 800);



        professor.setVisible(currentScene == 2);



        friend.setVisible(currentScene == 5);
//        friend2.setVisible(currentScene == 5);
        friendPrompt.setVisible(currentScene == 5 && mainCharacter.getTranslateX() > 800);



lecturerPromt.setVisible(currentScene==7 && mainCharacter.getTranslateX() > 100);

        lecturer.setVisible(currentScene == 7);
        computer1.setVisible(currentScene == 7);
        computer2.setVisible(currentScene == 7);
        computer3.setVisible(currentScene == 7);
        classTask.setVisible(currentScene == 7);
        friend2.setVisible(currentScene == 7);
        computerPrompt.setVisible(currentScene == 7 && mainCharacter.getTranslateX() > 800);




        for (Map.Entry<Integer, Character> entry : otherPlayers.entrySet()) {
            Character otherPlayer = entry.getValue();
            otherPlayer.setScaleX(6);
            otherPlayer.setScaleY(6);
            if (otherPlayer.isVisible()) { // Only update visible players
                otherPlayer.update();
            }
        }
    }

    private void handleSceneTransition(Pane root) {
        if (currentScene == 3) {
            currentScene = 8;
            background.setImage(bg8);
            mainCharacter.setTranslateX(0);
        } else if (currentScene == 8) {
            currentScene = 3;
            background.setImage(bg3);

        }


    }



    private Button createLessonsButton(Pane root) {
        // Create a button with FontAwesome icon
        Button lessonsButton = new Button(" Lessons");
        lessonsButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        lessonsButton.setLayoutX(20); // Bottom-left corner
        lessonsButton.setLayoutY(WINDOW_HEIGHT - 60);

        // Add FontAwesome icon
        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setGlyphName("BOOK");
        icon.setFill(Color.WHITE);
        icon.setSize("16");
        lessonsButton.setGraphic(icon);

        // Button click event
        lessonsButton.setOnAction(event -> {
                showLessonsGUI(root);
        });

        return lessonsButton;
    }

    private void showLessonsGUI(Pane root) {
        // Create a semi-transparent overlay background
        Pane overlay = new Pane();
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        // Create the popup container
        VBox popup = new VBox(20); // Use VBox for a clean, vertical layout
        popup.setPrefSize(600, 400);
        popup.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f0f0f0); " +
                "-fx-border-color: #cccccc; -fx-border-width: 1px; " +
                "-fx-border-radius: 15px; -fx-background-radius: 15px; -fx-padding: 20px;");
        popup.setLayoutX((WINDOW_WIDTH - 600) / 2);
        popup.setLayoutY((WINDOW_HEIGHT - 400) / 2);
        popup.setAlignment(Pos.CENTER);

        // Title Label
        Label titleLabel = new Label("Lessons");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Generate lesson content dynamically
        VBox lessonsContainer = new VBox(10); // Vertical box for lessons
        lessonsContainer.setAlignment(Pos.CENTER_LEFT);
        lessonsContainer.setPrefWidth(550);
        lessonsContainer.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; " +
                "-fx-border-width: 1px; -fx-border-radius: 10px; -fx-padding: 15px;");

        for (String lesson : lessons) {
            Label lessonLabel = new Label(lesson);
            lessonLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");
            lessonsContainer.getChildren().add(lessonLabel);
        }

        // Add a scrollable container if lessons exceed a certain number
        ScrollPane scrollPane = new ScrollPane(lessonsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; " +
                "-fx-border-color: transparent;");

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; -fx-border-radius: 15px; -fx-background-radius: 15px;");
        closeButton.setOnAction(event -> root.getChildren().remove(overlay));

        // Add components to the popup
        popup.getChildren().addAll(titleLabel, scrollPane, closeButton);

        // Add the popup to the overlay
        overlay.getChildren().add(popup);
    }







    private void transitionToScene(Pane root, boolean forward) {
        // Update current scene based on the direction
        currentScene = forward ? (currentScene % 9) + 1 : (currentScene == 1 ? 9 : currentScene - 1);




        if (currentScene != 9) {
            examGUIShown = false; // Reset the flag when leaving Scene 9
        }
        // Update the background image based on the current scene
        switch (currentScene) {
            case 1 -> background.setImage(bg1);
            case 2 -> background.setImage(bg2);
            case 3 -> background.setImage(bg3);
            case 4 -> background.setImage(bg4);
            case 5 -> background.setImage(bg5);
            case 6 -> background.setImage(bg6);
            case 7 -> background.setImage(bg7);
            case 8 -> background.setImage(bg8);
        }

        // Reset character position based on transition direction
        if (forward) {
            mainCharacter.setTranslateX(0); // Start at the leftmost position for forward transitions
        } else {
            mainCharacter.setTranslateX(WINDOW_WIDTH - mainCharacter.getBoundsInParent().getWidth()); // Start at the rightmost position for backward transitions
        }

    }

    public static void showToast(Pane root, String message) {
        // Create the toast label
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white; -fx-padding: 20px; " +
                "-fx-font-size: 40px; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 15, 0.5, 0, 2);");

        // Set dynamic width based on text size
        double toastWidth = Math.max(400, message.length() * 25); // Adjust width based on text length (minimum 400px)
        toast.setMinWidth(toastWidth);
        toast.setMaxWidth(toastWidth);
        toast.setAlignment(Pos.CENTER); // Center text horizontally and vertically

        // Center the toast on the screen
        toast.setLayoutX((WINDOW_WIDTH - toastWidth) / 2);
        toast.setLayoutY(WINDOW_HEIGHT / 2 - 50);

        // Add the toast to the root pane
        root.getChildren().add(toast);

        // Add fade-in and fade-out transitions
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), toast);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), toast);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> root.getChildren().remove(toast));

        // Play fade-in followed by fade-out
        SequentialTransition sequentialTransition = new SequentialTransition(fadeIn, fadeOut);
        sequentialTransition.play();
    }


    static void updateObjectives(String objectiveId) {
        objectives.markObjectiveComplete(objectiveId);
        allObjectivesCompleted = objectives.areAllObjectivesCompleted(); // Update the flag

        // Add lessons dynamically based on the completed objective
        switch (objectiveId) {
            case "objective1":
                lessons.add("1. I should always bring my ID");
                break;
            case "objective2":
                lessons.add("2. I should make a good relationship with the faculties");
                break;
            case "objective3":
                lessons.add("3. I should Borrow books from library");
                break;

            case "objective4":
                lessons.add("4. Notebooks are necessary for before attending class");
                break;

            case "objective5":
                lessons.add("5. I should choose friend precisely");
                break;
            case "objective6":
                lessons.add("6. I should eat healthy foods before class");
                break;
            case "objective7":
                lessons.add("7. I can write code in C programming");
                break;
        }

//        if (allObjectivesCompleted) {
//            showToast(root, "All objectives completed! Lessons unlocked.");
//        }
    }


    private void createNavigationButtons(Pane root) {
        // Create "Lessons" Button
        Button lessonsButton = new Button(" Lessons");
        lessonsButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        lessonsButton.setLayoutX(20);
        lessonsButton.setLayoutY(WINDOW_HEIGHT - 60);

        FontAwesomeIconView lessonsIcon = new FontAwesomeIconView();
        lessonsIcon.setGlyphName("BOOK");
        lessonsIcon.setFill(Color.WHITE);
        lessonsIcon.setSize("16");
        lessonsButton.setGraphic(lessonsIcon);

        lessonsButton.setOnAction(event -> {
            if (!lessons.isEmpty()) {
                showLessonsGUI(root);
            } else {
                showToast(root, "No lessons unlocked yet. Complete objectives first!");
            }
        });

        // Create "Show Map" Button
        Button mapButton = new Button(" Show Map");
        mapButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        mapButton.setLayoutX(140); // Position beside "Lessons" button
        mapButton.setLayoutY(WINDOW_HEIGHT - 60);

        FontAwesomeIconView mapIcon = new FontAwesomeIconView();
        mapIcon.setGlyphName("MAP");
        mapIcon.setFill(Color.WHITE);
        mapIcon.setSize("16");
        mapButton.setGraphic(mapIcon);

        mapButton.setOnAction(event -> showMapGUI(root));

        // Add both buttons to the root
        root.getChildren().addAll(lessonsButton, mapButton);
    }

    private void showNotebookGUI(Pane root) {
        // Create a semi-transparent overlay background
        Pane overlay = new Pane();
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        // Create the popup container
        VBox popup = new VBox(20);
        popup.setPrefSize(500, 300);
        popup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; " +
                "-fx-border-radius: 10px; -fx-background-radius: 10px;");
        popup.setLayoutX((WINDOW_WIDTH - 500) / 2);
        popup.setLayoutY((WINDOW_HEIGHT - 300) / 2);
        popup.setAlignment(Pos.CENTER);

        // Add the notebook image
        ImageView notebookImage = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/game/university_platformer_game/objects/notebook.png"))
        ));
        notebookImage.setFitWidth(200);
        notebookImage.setFitHeight(150);

        // Add description label
        Label description = new Label("Buy this notebook to take notes for your lessons.");
        description.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 10px; -fx-alignment: center;");

        // Add buttons
        Button buyButton = new Button("Buy");
        buyButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        buyButton.setOnAction(event -> {
            updateObjectives("objective4"); // Mark objective complete
            showToast(root, "Notebook Purchased! +5 Knowledge Points");
            addNotebookButton(root); // Add the notebook button
            root.getChildren().remove(overlay); // Close GUI
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        cancelButton.setOnAction(event -> root.getChildren().remove(overlay)); // Close GUI

        // Add elements to the popup
        popup.getChildren().addAll(notebookImage, description, buyButton, cancelButton);

        // Add the popup to the overlay
        overlay.getChildren().add(popup);
    }

    private void addNotebookButton(Pane root) {
        // Add the notebook button above the lessons button
        if (notebookButton == null) {
            notebookButton = new Button("Notebook");
            notebookButton.setStyle("-fx-background-color: #ff9900; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
            notebookButton.setLayoutX(20);
            notebookButton.setLayoutY(WINDOW_HEIGHT - 120); // Position above the lessons button
            notebookButton.setOnAction(event -> showDrawingGUI(root)); // Show the drawing GUI when clicked
            root.getChildren().add(notebookButton);
        }
    }

    private void showDrawingGUI(Pane root) {
        // Create a semi-transparent overlay
        Pane overlay = new Pane();
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        // Create a popup for the drawing canvas
        VBox popup = new VBox(10);
        popup.setPrefSize(600, 500);
        popup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; " +
                "-fx-border-radius: 10px; -fx-background-radius: 10px;");
        popup.setLayoutX((WINDOW_WIDTH - 600) / 2);
        popup.setLayoutY((WINDOW_HEIGHT - 500) / 2);
        popup.setAlignment(Pos.CENTER);

        // Create the canvas
        Canvas canvas = new Canvas(500, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Default drawing color
        final ObjectProperty<Color> currentColor = new SimpleObjectProperty<>(Color.BLACK);

        // Enable drawing on the canvas
        canvas.setOnMousePressed(e -> gc.beginPath());
        canvas.setOnMouseDragged(e -> {
            gc.setStroke(currentColor.get());
            gc.setLineWidth(2);
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Add color buttons
        HBox colorPicker = new HBox(5);
        colorPicker.setAlignment(Pos.CENTER);

        Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.PURPLE};
        for (Color color : colors) {
            Button colorButton = new Button();
            colorButton.setStyle("-fx-background-color: " + toRGBCode(color) + "; -fx-border-color: black; -fx-border-width: 1px;");
            colorButton.setPrefSize(30, 30);
            colorButton.setOnAction(e -> currentColor.set(color));
            colorPicker.getChildren().add(colorButton);
        }

        // Add a clear button
        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        clearButton.setOnAction(e -> {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });

        // Add a close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        closeButton.setOnAction(e -> root.getChildren().remove(overlay)); // Close the drawing GUI

        // Add components to the popup
        popup.getChildren().addAll(canvas, colorPicker, clearButton, closeButton);

        // Add the popup to the overlay
        overlay.getChildren().add(popup);
    }

    // Utility method to convert Color to RGB code
    private String toRGBCode(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("rgb(%d, %d, %d)", r, g, b);
    }

    private void showMapGUI(Pane root) {
        // Create a semi-transparent overlay background
        Pane overlay = new Pane();
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        // Create the popup container
        VBox popup = new VBox(20);
        popup.setPrefSize(900, 700);
        popup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; " +
                "-fx-border-radius: 10px; -fx-background-radius: 10px;");
        popup.setLayoutX((WINDOW_WIDTH - 900) / 2);
        popup.setLayoutY((WINDOW_HEIGHT - 700) / 2);
        popup.setAlignment(Pos.CENTER);

        // Add title
        Label title = new Label("Map of Scenes");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 24px; -fx-font-weight: bold;");
        popup.getChildren().add(title);

        // Create a GridPane for the scene previews
        GridPane grid = new GridPane();
        grid.setHgap(20); // Horizontal gap between previews
        grid.setVgap(20); // Vertical gap between previews
        grid.setAlignment(Pos.CENTER);

        // Load scene preview images
        Image[] scenePreviews = new Image[]{
                bg1, bg2, bg3, bg4, bg5, bg6, bg7, bg8
        };

        // Define scene names
        String[] sceneNames = {
                "Entry Gate", "Ground Floor 1", "Ground Floor 2", "Book Shop", "Field",
                "Canteen", "Class", "Library"
        };

        // Add previews and buttons to the grid
        for (int i = 0; i < scenePreviews.length; i++) {
            int sceneNumber = i + 1; // Scene numbers start at 1
            StackPane scenePane = new StackPane();
            scenePane.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-radius: 10px;");

            // Create and add the scene image
            ImageView sceneImage = new ImageView(scenePreviews[i]);
            sceneImage.setFitWidth(200); // Thumbnail size
            sceneImage.setFitHeight(150);
            scenePane.getChildren().add(sceneImage);

            // Add a button overlay for navigation
            Button goToSceneButton = new Button("Go to " + sceneNames[i]);
            goToSceneButton.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            goToSceneButton.setOnAction(event -> {
                currentScene = sceneNumber;
                updateScene(root);
                root.getChildren().remove(overlay); // Close the map
            });
            StackPane.setAlignment(goToSceneButton, Pos.BOTTOM_CENTER);
            scenePane.getChildren().add(goToSceneButton);

            // Add the scene pane to the grid
            grid.add(scenePane, i % 3, i / 3); // Arrange in a 3-column grid
        }

        // Add the grid to the popup
        popup.getChildren().add(grid);

        // Add a close button to exit the map
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        closeButton.setOnAction(event -> root.getChildren().remove(overlay));
        popup.getChildren().add(closeButton);

        // Add the popup to the overlay
        overlay.getChildren().add(popup);
    }



    private void updateScene(Pane root) {
        switch (currentScene) {
            case 1 -> background.setImage(bg1);
            case 2 -> background.setImage(bg2);
            case 3 -> background.setImage(bg3);
            case 4 -> background.setImage(bg4);
            case 5 -> background.setImage(bg5);
            case 6 -> background.setImage(bg6);
            case 7 -> background.setImage(bg7);
            case 8 -> background.setImage(bg8);
        }

        mainCharacter.setTranslateX(0); // Reset character position
        mainCharacter.setTranslateY(WINDOW_HEIGHT / 2); // Center vertically
    }





    private void openComputerGUI(Pane root) {
        computer.open(root, playerStats, () -> {
            updateObjectives("objective7");
            showToast(root, "+30 Knowledge Points! All Task Completed Successfully!");
            PlayerStats playerStats = new PlayerStats(root);
            playerStats.increaseKnowledgePoints(30);

        });
    }

    private String currentStage = "start"; // Tracks the current stage of the conversation

    private void initializeDialogueOptions() {
        dialogueOptions = new HashMap<>();
        // Initial dialogue options for the "start" stage
        dialogueOptions.put("Assalamu Alaikum Sir", new String[]{"Walaikum Assalam", "middle"});
    }

    // Dynamically updates dialogue options based on the current stage
    private void updateDialogueOptions(String stage) {
        dialogueOptions.clear(); // Clear existing options

        switch (stage) {
            case "start":
                dialogueOptions.put("Assalamu Alaikum Sir", new String[]{"Walaikum Assalam", "middle"});
                break;

            case "middle":
                dialogueOptions.put("I am new here. Can you tell how to improve my skills?", new String[]{"Practice everyday and participate in coding challenges", "advice"});
                dialogueOptions.put("How can I maintain good CGPA?", new String[]{"Don't miss any classes and always take notes.", "advice"});
                break;

            case "advice":
                dialogueOptions.put("Thank you for your guidance!", new String[]{"You're welcome! All the best.", "end"});
                dialogueOptions.put("Can you share more tips?", new String[]{"Always stay focused and work consistently.", "end"});
                break;

            case "end":
                // Final stage, optional options to close the conversation or add a goodbye.
                dialogueOptions.put("Goodbye, Sir!", new String[]{"Goodbye! Take care.", "close"});
                break;

            case "close":
                // No more options, optionally clear the dialogue or exit the conversation.
                dialogueOptions.clear(); // Empty options to end the conversation
                break;

            default:
                // Handle unknown stages if necessary
                System.out.println("Unknown dialogue stage: " + stage);
                break;
        }
    }


    public void showConversationWithProfessor(Pane root) {
        if (isDialogueOpen) return;
        isDialogueOpen = true;

        Label professorDialogue = new Label();
        professorDialogue.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333; -fx-padding: 15px; -fx-font-size: 16px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 2);");
        professorDialogue.setTranslateX(professor.getTranslateX());
        professorDialogue.setTranslateY(professor.getTranslateY() - 60);

        Label mainCharacterDialogue = new Label();
        mainCharacterDialogue.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333; -fx-padding: 15px; -fx-font-size: 16px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 2);");
        mainCharacterDialogue.setTranslateX(mainCharacter.getTranslateX() - 50);
        mainCharacterDialogue.setTranslateY(mainCharacter.getTranslateY() - 60);

        VBox dialogueBox = new VBox(10);
        dialogueBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-padding: 15px; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 15, 0, 0, 5);");
        dialogueBox.setLayoutX(WINDOW_WIDTH / 2 - 200);
        dialogueBox.setLayoutY(WINDOW_HEIGHT - 250);

        for (Map.Entry<String, String[]> entry : new HashMap<>(dialogueOptions).entrySet()) {
            Button optionButton = new Button(entry.getKey());
            optionButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            optionButton.setOnMouseEntered(e -> optionButton.setStyle("-fx-background-color: #005f99; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
            optionButton.setOnMouseExited(e -> optionButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
            optionButton.setOnAction(e -> {
                mainCharacterDialogue.setText(entry.getKey());
                String[] response = dialogueOptions.remove(entry.getKey());
                String professorResponse = response[0];
                String nextStage = response[1];

                FadeTransition fadeInMain = createFadeTransition(mainCharacterDialogue, 0.0, 1.0);
                fadeInMain.setOnFinished(event -> {
                    FadeTransition fadeOutMain = createFadeTransition(mainCharacterDialogue, 1.0, 0.0);
                    fadeOutMain.setOnFinished(event1 -> {
                        root.getChildren().remove(mainCharacterDialogue);
                        professorDialogue.setText(professorResponse);
                        FadeTransition fadeInProfessor = createFadeTransition(professorDialogue, 0.0, 1.0);
                        fadeInProfessor.setOnFinished(event2 -> {
                            FadeTransition fadeOutProfessor = createFadeTransition(professorDialogue, 1.0, 0.0);
                            fadeOutProfessor.setOnFinished(event3 -> {
                                root.getChildren().remove(professorDialogue);
                                isDialogueOpen = false;
                                if (nextStage.equals("end")) {
                                    updateObjectives("objective2");
                                    showToast(root, "+10 Reputation Points! Task Complete.");
                                    PlayerStats playerStats = new PlayerStats(root);
                                    playerStats.increaseReputationPoints(10);
                                } else {
                                    currentStage = nextStage; // Update the stage
                                    updateDialogueOptions(currentStage); // Refresh options
                                    showConversationWithProfessor(root); // Show next dialogue
                                }
                            });
                            fadeOutProfessor.play();
                        });
                        root.getChildren().add(professorDialogue);
                        fadeInProfessor.play();
                    });
                    fadeOutMain.play();
                });
                root.getChildren().add(mainCharacterDialogue);
                fadeInMain.play();
                root.getChildren().remove(dialogueBox);
            });
            dialogueBox.getChildren().add(optionButton);
        }

        root.getChildren().add(dialogueBox);
    }



    private FadeTransition createFadeTransition(Label label, double fromValue, double toValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.2), label);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        return fadeTransition;
    }





    private void showFoodGUI(Pane root) {
        // Overlay Background
        Pane overlay = new Pane();
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        root.getChildren().add(overlay);

        // Popup Container
        Pane foodPopup = new Pane();
        foodPopup.setPrefSize(800, 500);
        foodPopup.setLayoutX((WINDOW_WIDTH - 800) / 2);
        foodPopup.setLayoutY((WINDOW_HEIGHT - 500) / 2);
        foodPopup.setStyle("-fx-background-color: #2C3E50; -fx-border-color: white; -fx-border-width: 2px; " +
                "-fx-border-radius: 10px; -fx-background-radius: 10px;");
        overlay.getChildren().add(foodPopup);

        // Add Menu Title
        Label menuTitle = new Label("Khan's Kitchen");
        menuTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        menuTitle.setLayoutX(300);
        menuTitle.setLayoutY(20);
        foodPopup.getChildren().add(menuTitle);

        String[][] foods = {
                {"Burger", "/com/game/university_platformer_game/foods/burger.png", "10", "5", "0", "15"},
                {"Pizza", "/com/game/university_platformer_game/foods/pizza.png", "8", "2", "3", "10"},
                {"Apple", "/com/game/university_platformer_game/foods/apple.png", "8", "2", "3", "10"},
                {"Rice", "/com/game/university_platformer_game/foods/rice.png", "8", "2", "3", "10"},
        };

        // Create Food Buttons
        for (int i = 0; i < foods.length; i++) {
            int col = i % 3; // Column (0, 1, 2)
            int row = i / 3; // Row (0, 1)

            String foodName = foods[i][0];
            String foodImagePath = foods[i][1];
            int hpEffect = Integer.parseInt(foods[i][2]);


            Button foodButton = new Button();
            foodButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2px; " +
                    "-fx-border-radius: 10px;");
            foodButton.setLayoutX(50 + col * 250);
            foodButton.setLayoutY(80 + row * 180);
            foodButton.setPrefSize(200, 150);

            // Add food image
            ImageView foodImage = new ImageView(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(foodImagePath))
            ));
            foodImage.setFitWidth(180);
            foodImage.setFitHeight(100);
            foodButton.setGraphic(foodImage);

            // Add food name below the image
            Label foodLabel = new Label(foodName);
            foodLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            foodLabel.setLayoutX(50 + col * 250 + 90);
            foodLabel.setLayoutY(80 + row * 180 + 120);
            foodPopup.getChildren().add(foodLabel);

            foodPopup.getChildren().add(foodButton);

            foodButton.setOnAction(event -> {
                // Update stats
                playerStats.increaseHealthPoints(10);
                updateObjectives("objective6");
                showToast(root, "+10 Health Points! Task Complete. You ate " + foodName + "!");
                root.getChildren().remove(overlay); // Close GUI after selection
            });
        }

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        closeButton.setLayoutX(350);
        closeButton.setLayoutY(420);
        foodPopup.getChildren().add(closeButton);

        closeButton.setOnAction(event -> root.getChildren().remove(overlay)); // Close GUI
    }














    private void startRedLightGreenLight(Pane root) {
        // Create circles for lights
        Circle redLight = new Circle(20, Color.RED);
        Circle yellowLight = new Circle(20, Color.YELLOW);
        Circle greenLight = new Circle(20, Color.GRAY); // Off state

        redLight.setLayoutX(500);
        redLight.setLayoutY(50);
        yellowLight.setLayoutX(540);
        yellowLight.setLayoutY(50);
        greenLight.setLayoutX(580);
        greenLight.setLayoutY(50);

        root.getChildren().addAll(redLight, yellowLight, greenLight);

        // Light state tracker
        final boolean[] greenLightOn = {false};

        // Light switching logic
        Timeline lightSwitcher = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            if (greenLightOn[0]) {
                // Switch to Red Light
                redLight.setFill(Color.RED);
                yellowLight.setFill(Color.GRAY);
                greenLight.setFill(Color.GRAY);
                greenLightOn[0] = false;
            } else {
                // Switch to Green Light
                redLight.setFill(Color.GRAY);
                yellowLight.setFill(Color.GRAY);
                greenLight.setFill(Color.GREEN);
                greenLightOn[0] = true;
            }
        }));
        lightSwitcher.setCycleCount(Timeline.INDEFINITE);
        lightSwitcher.play();

        // Detect movement during red light
        AnimationTimer movementChecker = new AnimationTimer() {
            private double lastX = mainCharacter.getTranslateX();
            private double lastY = mainCharacter.getTranslateY();

            @Override
            public void handle(long now) {
                if (!greenLightOn[0]) { // During Red Light
                    if (mainCharacter.getTranslateX() != lastX || mainCharacter.getTranslateY() != lastY) {
                        // Player moved during Red Light
                        playerStats.decreaseHealthPoints(100000); // Set health to zero
                        showToast(root, "You moved during Red Light! Game Over.");
                        this.stop();
                    }
                }

                // Update last position
                lastX = mainCharacter.getTranslateX();
                lastY = mainCharacter.getTranslateY();
            }
        };
        movementChecker.start();

        // Check if player successfully crossed the scene
        AnimationTimer successChecker = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (mainCharacter.getTranslateX() > WINDOW_WIDTH - 50) {
                    // Player successfully crossed
                    lightSwitcher.stop();
                    movementChecker.stop();
                    playerStats.increaseKnowledgePoints(10); // Award 10 points
                    showToast(root, "You crossed successfully! +10 Points!");
                    this.stop();
                }
            }
        };
        successChecker.start();
    }





    public static void main(String[] args) {
        launch(args);
    }
}
