package hi.verkefni5.vidmot;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioMenuItem;

public class MenuController {

    GoldController goldController;
    public void onErfidleikaStig(ActionEvent actionEvent) {
        goldController.setErfidleikaStig(Integer.parseInt(((RadioMenuItem) actionEvent.getSource()).getId()));
    }

    public void setController(GoldController goldController) {
        this.goldController = goldController;
    }

    public void onHefjaleik(ActionEvent actionEvent) {
        goldController.hefjaLeik();
    }

    public void onLokaleik(ActionEvent actionEvent) {
        Platform.exit();
    }
}
