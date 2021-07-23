package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

/**
 * panel s hrou obsahujúci logiku hry
 */
class MarbleShooterPane extends Pane {
    /**
     * širka hracieho panelu
     */
    public static final double PANE_WIDTH = 800;
    /**
     * výška hracieho panelu
     */
    public static final double PANE_HEIGHT = 600;
    /**
     * veľkosť loptičky
     */
    public static final double MARBLE_SIZE = 40;
    /**
     * polovica veľkosti loptičky
     */
    public static final double HALF_MARBLE_SIZE = MARBLE_SIZE / 2;

    private final List<Marble> marbles = new ArrayList<>();
    private final List<Marble> visitedMarbles = new ArrayList<>();
    private final MarbleCreator marbleCreator = new MarbleCreator();

    private ShootingMarble shootingMarble;
    private ImageView lastCircle;
    private Timeline animation;
    private Level[] levels;

    private int shotsPerRow = 0;
    private int level = 0;
    private double dx = 0;
    private double dy = 0;
    private boolean shootingInProgress = false;
    private boolean connectedToTop;

    public MarbleShooterPane() {
        setupAnimation();
        startLevel();
    }

    private void setupAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(10),
                e -> {
                    shootingMarble.update(getWidth() - MARBLE_SIZE, getHeight());
                    shootingMarbleMoved();
                }));
        animation.setCycleCount(Timeline.INDEFINITE);
    }

    private void startLevel() {
        setupLevels();
        animation.stop();
        shotsPerRow = 0;
        shootingInProgress = false;
        shootingMarble = null;
        marbles.clear();

        getChildren().clear();
        BackgroundImage myBI = new BackgroundImage(new Image(levels[level].getBackgroundUrl(), 800, 600, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        setBackground(new Background(myBI));

        initializeStartingMarbles();
        setupShooter();
        createNewShootingMarble();
    }

    /**
     * mozeme nastavit kolko levelov chceme a ich vlastnosti
     * aplikacia sa s tym vysporiada sama a bude vediet pracovat aj keby sme tu dali 10 levelov
     */
    private void setupLevels() {
        levels = new Level[]{
                new Level(4, new MarbleColor[]{MarbleColor.RED, MarbleColor.GREEN, MarbleColor.BLUE}, 5, "file:resources/background1.jpg"),
                new Level(6, new MarbleColor[]{MarbleColor.RED, MarbleColor.GREEN, MarbleColor.BLUE, MarbleColor.PURPLE}, 4, "file:resources/background2.jpg"),
                new Level(8, new MarbleColor[]{MarbleColor.RED, MarbleColor.GREEN, MarbleColor.BLUE, MarbleColor.PURPLE, MarbleColor.YELLOW}, 3, "file:resources/background3.jpg")};
    }

    private void initializeStartingMarbles() {
        for (int i = 0; i < levels[level].getInitialNumberOfRows(); i++) {
            addNewRow();
        }
        reprintMarbles();
        recalculateMarbleNeighbors();
    }

    private void addNewRow() {
        List<Marble> generatedMarbleRow = marbleCreator.generateMarbleRow(levels[level].getColors());
        for (Marble marble : marbles) {
            marble.setRow(marble.getRow() + 1);
        }
        marbles.addAll(generatedMarbleRow);
    }

    private void reprintMarbles() {
        for (Marble marble : marbles) {
            getChildren().remove(marble.getImageView());

            ImageView marbleImageView = getImageViewFromMarble(marble.getColumn() * MARBLE_SIZE, marble.getRow() * MARBLE_SIZE, marble.getColor());
            marble.setImageView(marbleImageView);
            getChildren().add(marbleImageView);
        }
    }

    private ImageView getImageViewFromMarble(double x, double y, MarbleColor color) {
        ImageView marbleImageView = new ImageView(new Image("file:resources/" + String.valueOf(color).toLowerCase() + ".png"));
        marbleImageView.setFitWidth(MarbleShooterPane.MARBLE_SIZE);
        marbleImageView.setFitHeight(MarbleShooterPane.MARBLE_SIZE);
        marbleImageView.setX(x);
        marbleImageView.setY(y);
        return marbleImageView;
    }

    private void shootingMarbleMoved() {
        ImageView marbleImageView = getImageViewFromMarble(shootingMarble.getX(), shootingMarble.getY(), shootingMarble.getColor());
        getChildren().remove(lastCircle);
        getChildren().add(marbleImageView);
        lastCircle = marbleImageView;

        for (Marble marble : marbles) {
            if (shootingMarbleTouchedMarble(marble)) {
                getChildren().remove(marbleImageView);
                shootingMarbleFoundPlace(marbleCreator.createNewMarble(shootingMarble, marble));
                return;
            }
        }

        if (shootingMarble.getY() < 20) {
            getChildren().remove(marbleImageView);
            shootingMarbleFoundPlace(new Marble(shootingMarble.getColor(), (int) ((shootingMarble.getX() + 20) / MARBLE_SIZE), 0));
        }
    }

    private void createNewShootingMarble() {
        if (levels[level].getColors().length > 0) {
            shootingMarble = new ShootingMarble(MarbleShooterPane.PANE_WIDTH / 2 - HALF_MARBLE_SIZE, MarbleShooterPane.PANE_HEIGHT - MARBLE_SIZE, dx, dy, 5, levels[level].getColors()[new Random().nextInt(levels[level].getColors().length)]);
            ImageView marbleImageView = getImageViewFromMarble(shootingMarble.getX(), shootingMarble.getY(), shootingMarble.getColor());
            getChildren().remove(lastCircle);
            getChildren().add(marbleImageView);
            lastCircle = marbleImageView;
        }
    }

    private void setupShooter() {
        Line line = new Line();
        line.setStrokeWidth(10);
        line.setStroke(Color.rgb(220, 0, 0, 0.8));
        getChildren().add(line);

        setOnMouseMoved(event -> {
            if (event.getSceneY() > getHeight() - MARBLE_SIZE) return;

            double canvasWidth = getWidth();
            double canvasHeight = getHeight();
            double clickX = event.getSceneX();
            double clickY = event.getSceneY();

            line.setStartX(canvasWidth / 2);
            line.setStartY(canvasHeight - 20);

            double xdiff = clickX - (canvasWidth / 2);
            double ydiff = canvasHeight - 20 - clickY;
            double angle = Math.atan(ydiff / xdiff);

            if (angle < 0) {
                line.setEndX(canvasWidth / 2 - 100 * Math.cos(angle));
                dx = -Math.cos(angle) * 12;
            } else {
                line.setEndX(canvasWidth / 2 + 100 * Math.cos(angle));
                dx = Math.cos(angle) * 12;


            }
            line.setEndY(canvasHeight - 20 - 100 * Math.abs(Math.sin(angle)));
            dy = -Math.abs(Math.sin(angle)) * 12;
        });

        setOnMouseClicked(event -> {
            if (!shootingInProgress) {
                playSound("marble-shoot.wav");
                shootingMarble.setDx(dx);
                shootingMarble.setDy(dy);
                animation.play();
                shootingInProgress = true;
            }
        });
    }

    private void shootingMarbleFoundPlace(Marble newMarble) {
        animation.stop();
        marbles.add(newMarble);
        reprintMarbles();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100),
                        event -> {
                            marbleHit(newMarble);
                            reprintMarbles();
                            checkRemovedMarbles();
                            checkGameState();
                            createNewShootingMarble();
                            shootingInProgress = false;
                        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void checkRemovedMarbles() {
        List<MarbleColor> colors = new ArrayList<>(Arrays.asList(levels[level].getColors()));
        List<MarbleColor> colorsToBeRemovedFromGame = new ArrayList<>();

        for (MarbleColor color : colors) {
            boolean colorIsStillInGame = false;
            for (Marble marble : marbles) {
                if (marble.getColor() == color) {
                    colorIsStillInGame = true;
                    break;
                }
            }
            if (!colorIsStillInGame) {
                colorsToBeRemovedFromGame.add(color);
            }
        }
        colors.removeAll(colorsToBeRemovedFromGame);
        levels[level].setColors(colors.toArray(new MarbleColor[0]));
    }

    private void handleRowAddition() {
        shotsPerRow += 1;
        if (shotsPerRow == levels[level].getTriesPerRow()) {
            shotsPerRow = 0;
            addNewRow();
        }
    }

    private boolean shootingMarbleTouchedMarble(Marble marble) {
        return inBetween(marble.getColumn() * MARBLE_SIZE, (marble.getColumn() + 1) * MARBLE_SIZE, shootingMarble.getX() + 20) &&
                inBetween(marble.getRow() * MARBLE_SIZE, (marble.getRow() + 1) * MARBLE_SIZE, shootingMarble.getY() + 20);
    }

    private boolean inBetween(double a, double b, double p) {
        return p >= a && p <= b;
    }

    private void recalculateMarbleNeighbors() {
        Map<Integer, Map<Integer, Marble>> rows = new HashMap<>();

        for (Marble marble : marbles) {
            Map<Integer, Marble> row = rows.getOrDefault(marble.getRow(), new HashMap<>());
            row.put(marble.getColumn(), marble);
            rows.put(marble.getRow(), row);
        }

        for (Marble marble : marbles) {
            Map<Integer, Marble> previousRow = rows.getOrDefault(marble.getRow() - 1, null);
            Map<Integer, Marble> currentRow = rows.getOrDefault(marble.getRow(), null);
            Map<Integer, Marble> nextRow = rows.getOrDefault(marble.getRow() + 1, null);

            if (previousRow == null) {
                marble.setTopLeft(null);
                marble.setTop(null);
                marble.setTopRight(null);
            } else {
                marble.setTopLeft(previousRow.getOrDefault(marble.getColumn() - 1, null));
                marble.setTop(previousRow.getOrDefault(marble.getColumn(), null));
                marble.setTopRight(previousRow.getOrDefault(marble.getColumn() + 1, null));
            }

            marble.setLeft(currentRow.getOrDefault(marble.getColumn() - 1, null));
            marble.setRight(currentRow.getOrDefault(marble.getColumn() + 1, null));

            if (nextRow == null) {
                marble.setBottomLeft(null);
                marble.setBottom(null);
                marble.setBottomRight(null);
            } else {
                marble.setBottomLeft(nextRow.getOrDefault(marble.getColumn() - 1, null));
                marble.setBottom(nextRow.getOrDefault(marble.getColumn(), null));
                marble.setBottomRight(nextRow.getOrDefault(marble.getColumn() + 1, null));
            }
        }
    }

    private void marbleHit(Marble marble) {
        recalculateMarbleNeighbors();
        visitedMarbles.clear();
        popAffectedMarbles(marble);

        if (visitedMarbles.size() > 2) {
            playSound("marble-pop.wav");
            for (Marble marble1 : visitedMarbles) {
                getChildren().remove(marble1.getImageView());
                marbles.remove(marble1);
            }

            removeDetachedMarbles();
        } else {
            playSound("marble-attached.wav");
            handleRowAddition();
        }
    }

    private void removeDetachedMarbles() {
        recalculateMarbleNeighbors();
        List<Marble> marblesToDelete = new ArrayList<>();

        for (Marble marble : marbles) {
            connectedToTop = false;
            visitedMarbles.clear();
            checkWhetherDetached(marble);
            if (!connectedToTop) {
                marblesToDelete.add(marble);
            }
        }

        for (Marble marble : marblesToDelete) {
            getChildren().remove(marble.getImageView());
            marbles.remove(marble);
        }
    }

    private void checkWhetherDetached(Marble marble) {
        if (marble.getRow() == 0) {
            connectedToTop = true;
            return;
        }

        visitedMarbles.add(marble);
        for (Marble neighbor : marble.getAllNeighbors()) {
            if (!visitedMarbles.contains(neighbor) && neighbor != null) {
                checkWhetherDetached(neighbor);
            }
        }
    }

    private void popAffectedMarbles(Marble marble) {
        visitedMarbles.add(marble);

        List<Marble> allNeighbors = marble.getAllNeighbors();
        for (Marble neighbor : allNeighbors) {
            if (neighbor != null && !visitedMarbles.contains(neighbor) && neighbor.getColor() == marble.getColor()) {
                popAffectedMarbles(neighbor);
            }
        }
    }

    private void checkGameState() {
        if (marbles.isEmpty()) { // level victory
            playSound("game-won.wav");
            if (level == levels.length - 1) { // game victory
                displayGameWonOptions();
                return;
            }
            moveToNextLevel();
        }

        boolean lost = false;
        for (Marble marble : marbles) {
            if (marble.getRow() == (PANE_HEIGHT / MARBLE_SIZE) - 1) {
                lost = true;
                break;
            }
        }

        if (lost) {
            playSound("game-over.wav");
            restartGame();
        }
    }

    private void restartGame() {
        level = 0;
        startLevel();
    }

    private void moveToNextLevel() {
        level = level + 1;
        startLevel();
    }

    private void displayGameWonOptions() {
        setOnMouseClicked(null);
        setOnMouseMoved(null);
        getChildren().clear();
        Text text = new Text();
        text.setText("Congratulations, you've won the game!");
        text.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 40));
        text.setFill(Color.WHITE);
        text.setX(400);
        text.setY(250);
        text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
        text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);

        getChildren().add(text);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));


        Button buttonPlayAgain = new Button("Play again");
        buttonPlayAgain.setMinWidth(50);

        buttonPlayAgain.setOnMouseClicked(event -> {
            restartGame();
        });
        Button buttonQuit = new Button("Quit game");
        buttonQuit.setMinWidth(50);
        buttonQuit.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });


        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.getChildren().add(buttonPlayAgain);
        hBox.getChildren().add(buttonQuit);
        hBox.setLayoutX(320);
        hBox.setLayoutY(300);

        getChildren().add(hBox);
    }

    private void playSound(String soundName) {
        Media sound = new Media(new File("resources/" + soundName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
