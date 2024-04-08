package hi.verkefni5.vidmot;

import hi.verkefni5.vinnsla.Animations;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Random;

public class Leikbord extends Pane {
    @FXML
    Grafari fxGrafari;
    @FXML
    Rectangle fxDarkness;
    @FXML
    BottomInfoView fxInfo;
    @FXML
    Label fxTimeleft;
    @FXML
    Label fxGoldText;
    @FXML
    Label fxScoreLabel;

    private Timeline timeline;
    private int maxNodur = 20;
    private int timer = 60;
    private int[] timers = {60, 30, 15};
    private int erfidleikasStig = 0;
    private Random rand = new Random();
    private ObservableList<Gull> gullNodur = FXCollections.observableArrayList();
    private boolean playing = false;
    private boolean shade = false;
    Timeline t;

    private HashMap<KeyCode, Boolean> takkar = new HashMap<>();

    private void spawnGull(){
        if(rand.nextInt(30) > 7 && gullNodur.size() < maxNodur && playing){
            double xPos = rand.nextDouble() * (getWidth() - Gull.BAR_WIDTH * 2) + Gull.BAR_WIDTH;
            double yPos = rand.nextDouble() * (getHeight() - 50) + 25;
            Gull nyttGull = new Gull(xPos, yPos, this);
            while(nyttGull.getRect().intersects(fxGrafari.getBoundsInParent())){
                xPos = rand.nextDouble() * (getWidth() - Gull.BAR_WIDTH * 2) + Gull.BAR_WIDTH;
                yPos = rand.nextDouble() * (getHeight() - 50) + 25;
                nyttGull = new Gull(xPos, yPos, this);
            }
            gullNodur.add(nyttGull);
            getChildren().add(2,nyttGull);
        }
    }

    public Leikbord(){
        FXML_Lestur.lesa(this, "leikbord-view.fxml");
        fxTimeleft.setOpacity(0);
        fxDarkness.setOpacity(0.7);
        fxScoreLabel.setOpacity(0);
    }

    public HashMap<KeyCode, Boolean> getTakkar(){
        return takkar;
    }

    public void initGameLoop() {
        fxGrafari.setLeikbord(this);
        if(timeline == null) {
            KeyFrame k = new KeyFrame(Duration.millis(20),
                    e -> {
                        gameLoop();
                    });

            timeline = new Timeline(k);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            k = new KeyFrame(Duration.millis(500),
                    e -> {
                        spawnGull(); // á að setja meira gull
                    });
            Timeline t = new Timeline(k);    // búin til tímalína fyrir leikinn
            t.setCycleCount(Timeline.INDEFINITE);   // leikurinn leikur endalaust
            t.play();
        }
        playing = true;
        resetGull();
        startTimer();
        fxGrafari.reset();
        fxInfo.reset();
    }

    private void startTimer() {
        FadeTransition fade = new FadeTransition();
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxTimeleft);
        fade.play();
        fade = new FadeTransition();
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxInfo);
        fade.play();
        if(!shade){
            shade = true;
            fade = new FadeTransition();
            fade.setFromValue(0.7);
            fade.setToValue(1);
            fade.setDuration(Duration.millis(600));
            fade.setCycleCount(1);
            fade.setNode(fxDarkness);
            fade.play();
        }
        fade = new FadeTransition();
        fade.setToValue(0);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxGoldText);
        fade.play();
        fade = new FadeTransition();
        fade.setToValue(0);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxScoreLabel);
        fade.play();
        timer = timers[erfidleikasStig];
        fxTimeleft.setText(timer +"");
        if(t != null) t.stop();
        KeyFrame k = new KeyFrame(Duration.seconds(1),
                e -> {
                    tic();
                });
        t = new Timeline(k);
        t.setCycleCount(timer);
        t.play();
        t.setOnFinished((actionEvent)->{
            fxScoreLabel.setText("Stig: " + fxInfo.getStig());
            this.stopGame();
        });
    }

    private void tic(){
        timer = timer-1;
        fxTimeleft.setText(timer +"");
        if(timer < 11){
            if(timer < 6) {
                Animations.pulse(fxTimeleft, 1.75);
            } else {
                Animations.pulse(fxTimeleft, 1.25);
            }
        }
    }

    private void stopGame() {
        for(Gull gull : gullNodur)
        {
            ScaleTransition scale = new ScaleTransition();
            scale.setNode(gull.getRect());
            scale.setFromX(1);
            scale.setToX(0);
            scale.setDuration(Duration.millis(250));
            scale.setCycleCount(1);
            scale.play();
            scale.setOnFinished((actionEvent)->{
                getChildren().remove(gull);
                gullNodur.remove(gull);
            });
        }
        playing = false;
        FadeTransition fade = new FadeTransition();
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxTimeleft);
        fade.play();
        fade = new FadeTransition();
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxGrafari);
        fade.play();
        fade = new FadeTransition();
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxInfo);
        fade.play();
        if(shade) {
            shade = false;
            fade = new FadeTransition();
            fade.setFromValue(1);
            fade.setToValue(0.7);
            fade.setDuration(Duration.millis(600));
            fade.setCycleCount(1);
            fade.setNode(fxDarkness);
            fade.play();
        }
        fade = new FadeTransition();
        fade.setToValue(1);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(fxGoldText);
        fade.play();
        fade.setOnFinished((actionEvent -> {
            FadeTransition fadet = new FadeTransition();
            fadet.setToValue(1);
            fadet.setDuration(Duration.millis(600));
            fadet.setCycleCount(1);
            fadet.setNode(fxScoreLabel);
            fadet.play();
        }));
    }

    private void resetGull() {
        for(Gull gull : gullNodur){
            getChildren().remove(gull);
        }
        gullNodur.clear();
    }

    private double deltaTime;
    private double lastTime;
    private void gameLoop() {
        // deltaTime sér til þess að allar hreyfingar séu jafnar sem nota tíma
        long time = System.nanoTime();
        deltaTime = (time - lastTime) / 1000000000.0; // Seconds
        lastTime = time;
        if(!playing) return;
        fxGrafari.update(deltaTime);
        for(Gull gull : gullNodur){
            if(gull.getRect().intersects(fxGrafari.getBoundsInParent()) && gull.isActive()){
                if(!gull.isEnabled()) {
                    fxInfo.updateStig(10);
                }
                gull.pickUpAnim(fxGrafari);
                fxGrafari.animPickup();
            }
        }
    }

    public void removeGull(Gull gull){
        gullNodur.remove(gull);
        getChildren().remove(gull);
        System.out.println(gullNodur.size());
    }

    public void initControls() {
        fxGrafari.requestFocus();
        takkar = new HashMap<>();
        takkar.put(KeyCode.UP, false);
        takkar.put(KeyCode.DOWN, false);
        takkar.put(KeyCode.LEFT, false);
        takkar.put(KeyCode.RIGHT, false);
        fxGrafari.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (takkar.containsKey(keyEvent.getCode())){
                    takkar.put(keyEvent.getCode(), true);
                }
            }
        });
        fxGrafari.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (takkar.containsKey(keyEvent.getCode())){
                    takkar.put(keyEvent.getCode(), false);
                }
            }
        });
    }

    public void setErfidleikaStig(int erfidleiki) {
        this.erfidleikasStig = erfidleiki;
    }
}
