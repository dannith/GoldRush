package hi.verkefni5.vidmot;

import hi.verkefni5.vinnsla.Animations;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Random;

public class Gull extends Pane {

    @FXML
    Rectangle fxRect;
    @FXML
    Rectangle fxShadow;

    Random rand = new Random();
    int red;
    int green;
    int blue;
    private boolean pickedUp = false;
    public static final double BAR_WIDTH = 80;


    RotateTransition idleRotate = new RotateTransition();

    ScaleTransition idleScale = new ScaleTransition();

    Leikbord leikbord;

    boolean active = false;

    public Gull(){
        FXML_Lestur.lesa(this, "bar-view.fxml");
        red = 235 + rand.nextInt(20);
        green = 200 + rand.nextInt(20);
        blue = 70 + rand.nextInt(20);
        fxRect.setWidth(BAR_WIDTH);
        fxRect.setScaleX(0);
        fxRect.setFill(Color.rgb(red,green,blue));
        fxShadow.xProperty().bind(fxRect.xProperty());
        fxShadow.yProperty().bind(fxRect.yProperty());
        fxShadow.rotateProperty().bind(fxRect.rotateProperty());
        fxShadow.scaleXProperty().bind(fxRect.scaleXProperty());
        fxShadow.scaleYProperty().bind(fxRect.scaleYProperty());
    }
    public Gull(double posX, double posY, Leikbord leikbord){
        this();
        fxRect.setX(posX);
        fxRect.setY(posY);
        spawnAnim();
        this.leikbord = leikbord;
        glowAnim(null);
    }

    private void spawnAnim(){
        FadeTransition fade = new FadeTransition();
        ScaleTransition scale = new ScaleTransition();
        fade.setNode(fxRect);
        scale.setNode(fxRect);
        scale.setDuration(Duration.millis(500));
        fade.setDuration(Duration.millis(500));
        fade.setFromValue(0);
        fade.setToValue(1);
        scale.setFromX(0.1);
        scale.setToX(1);
        fade.setCycleCount(1);
        scale.setCycleCount(1);
        fade.play();
        scale.play();
        this.setVisible(true);
        scale.setOnFinished(this::startAnim);
    }

    private void startAnim(ActionEvent actionEvent){
        active = true;
        idleRotate = Animations.wiggle(fxRect);
        idleScale = Animations.stretchSquish(fxRect);
        idleRotate.play();
        idleScale.play();
    }

    public void pickUpAnim(Rectangle player){
        if(pickedUp) return;
        pickedUp = true;
        fxShadow.setWidth(0);
        TranslateTransition position = new TranslateTransition();
        double xMidjaGulls = fxRect.getX() + fxRect.getWidth() / 2;
        double yMidjaGulls = fxRect.getY() + fxRect.getHeight() / 2;
        double xMidjaSpilara = player.getX() + player.getWidth() / 2;
        double yMidjaSpilara = player.getY() + player.getHeight() / 2;
        position.setToX(-xMidjaGulls + xMidjaSpilara);
        position.setToY(-yMidjaGulls + yMidjaSpilara);
        position.setDuration(Duration.millis(100));
        position.setCycleCount(1);
        FadeTransition fade = new FadeTransition();
        RotateTransition rotate = new RotateTransition();
        ScaleTransition scale = new ScaleTransition();
        idleScale.stop();
        idleRotate.stop();
        position.setNode(fxRect);
        scale.setNode(fxRect);
        fade.setNode(fxRect);
        rotate.setNode(fxRect);
        scale.setByX(-0.9);
        scale.setDuration(Duration.millis(100));
        fade.setDuration(Duration.millis(100));
        rotate.setDuration(Duration.millis(100));
        scale.setCycleCount(1);
        rotate.setCycleCount(1);
        fade.setCycleCount(1);
        rotate.setAxis(Rotate.Z_AXIS);
        int angle = 40;
        if(xMidjaGulls < xMidjaSpilara) angle *= -1;
        if (yMidjaGulls > yMidjaSpilara) angle *= -1;
        rotate.setByAngle(angle);
        fade.setToValue(0);
        fade.play();
        rotate.play();
        scale.play();
        position.play();
        scale.setOnFinished((ActionEvent event)->{
            leikbord.removeGull(this);
        });
    }

    private void glowAnim(ActionEvent actionEvent){
        int delay = rand.nextInt(13000) + 1000;
        FillTransition fill = new FillTransition();
        fill.setShape(fxRect);
        fill.setDelay(Duration.millis(delay));
        fill.setDuration(Duration.millis(420));
        fill.setCycleCount(2);
        fill.setAutoReverse(true);
        fill.setFromValue(Color.rgb(red, green, blue));
        fill.setToValue(Color.rgb(255, 255, 255));
        fill.play();
        fill.setOnFinished(this::glowAnim);
    }

    public Rectangle getRect() {
        return fxRect;
    }

    public boolean isEnabled() {
        return pickedUp;
    }

    public boolean isActive() {
        return active;
    }
}
