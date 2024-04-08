package hi.verkefni5.vidmot;

import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BottomInfoView extends VBox {
    @FXML
    Label fxStig;

    @FXML
    IntegerProperty stig = new SimpleIntegerProperty(0);

    private boolean anim = false;

    public BottomInfoView(){
        FXML_Lestur.lesa(this, "bottom-info-view.fxml");

        fxStig.textProperty().bind(Bindings.createStringBinding(() -> {
            String strengur = String.valueOf(stig.get());
            return strengur;
        }, stig));
    }

    public void updateStig(int stig){
        this.stig.set(this.stig.get() + stig);
        if(anim) return;
        anim = true;
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(fxStig);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setDuration(Duration.millis(300));
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
        scale.setOnFinished((actionEvent) -> {
            anim = false;
        });
    }

    public void reset() {
        stig.set(0);
    }

    public int getStig(){
        return stig.get();
    }
}
