package hi.verkefni5.vidmot;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Grafari extends Rectangle {

    private final double speed = 200;
    private double currentSpeed = speed;
private boolean anim =false;
    private final double diagonalSpeed = speed / Math.sqrt(speed * speed + speed * speed) * speed;

    private double currentDiagonalSpeed = diagonalSpeed;
    DoubleProperty xPos = new SimpleDoubleProperty(5000);
    DoubleProperty yPos = new SimpleDoubleProperty(5000);

    Leikbord leikbord = null;
    public Grafari(){
        FXML_Lestur.lesa(this, "grafari-view.fxml");
        xProperty().bind(xPos);
        yProperty().bind(yPos);
    }

    public void setLeikbord(Leikbord leikbord){
        this.leikbord = leikbord;
    }

    public void animPickup(){
        if(anim) return;
        anim = true;
        currentSpeed *= 0.75;
        currentDiagonalSpeed *= 0.75;
        ScaleTransition scale = new ScaleTransition();
        scale.setToX(1.5);
        scale.setToY(1.2);
        scale.setNode(this);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.setDuration(Duration.millis(300));
        scale.play();
        scale.setOnFinished((actionEvent)->{
            anim = false;
            currentSpeed = speed;
            currentDiagonalSpeed = diagonalSpeed;
        });
    }

    /**
     * Uppfærir staðsetningu grafara m.v hvaða takkar eru virkir
     * @param deltaTime tími í sec síðan frá síðasta update
     */
    public void update(double deltaTime){
        // Ef leikmaður er að færa sig annað hvort til hægri eða vinstri, þá á láreitt hreyfing sér stað
        // Þetta er til þess að vera ekki færa leikmann til hægri og vinstri á sama tíma.
        boolean moveX = leikbord.getTakkar().get(KeyCode.LEFT) != leikbord.getTakkar().get(KeyCode.RIGHT);
        // Sama og fyrir ofan, ef leikmaður er annað hvort að halda inni upp eða niður, þá á lóðreitt hreyfing sér stað.
        boolean moveY = leikbord.getTakkar().get(KeyCode.UP) != leikbord.getTakkar().get(KeyCode.DOWN);
        if(moveX){
            // Ef leikmaður er einnig að hreyfast lóðrétt, nota minni láréttan hraða þar sem hann er að hreyfast á ská
            if(moveY){
                // Vinstri
                if(leikbord.getTakkar().get(KeyCode.LEFT)) xPos.set(xPos.get() - currentDiagonalSpeed * deltaTime);
                // Hægri
                else xPos.set(xPos.get() + currentDiagonalSpeed * deltaTime);
            }
            // Leikmaður hreyfist aðeins lárétt - fullan hraða lárétt
            else {
                // Vinstri
                if(leikbord.getTakkar().get(KeyCode.LEFT)) xPos.set(xPos.get() - currentSpeed * deltaTime);
                // Hægri
                else xPos.set(xPos.get() + currentSpeed * deltaTime);
            }
        }
        if(moveY){
            // Ef leikmaður er einnig að hreyfast lárétt, nota minni lóðréttan hraða þar sem hann er að hreyfast á ská
            if(moveX){
                if(leikbord.getTakkar().get(KeyCode.UP)) yPos.set(yPos.get() - currentDiagonalSpeed * deltaTime);
                else yPos.set(yPos.get() + currentDiagonalSpeed * deltaTime);
            } else {
                if(leikbord.getTakkar().get(KeyCode.UP)) yPos.set(yPos.get() - currentSpeed * deltaTime);
                else yPos.set(yPos.get() + currentSpeed * deltaTime);
            }
        }
        // Athuga limits á leikborði
        // Magic tölur en 500 er breidd og hæð leikborðsins
        if(yPos.get() < 0) yPos.set(0);
        if(yPos.get() + getHeight() > 500) yPos.set(500 - getHeight());
        if(xPos.get() < 0) xPos.set(0);
        if(xPos.get() + getWidth() > 500) xPos.set(500 - getWidth());
    }

    public void reset() {
        xPos.set(250 - getWidth() / 2);
        yPos.set(250 - getHeight() / 2);
        FadeTransition fade = new FadeTransition();
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDuration(Duration.millis(600));
        fade.setCycleCount(1);
        fade.setNode(this);
        fade.play();
    }
}
