package hi.verkefni5.vidmot;

import javafx.fxml.FXML;

public class GoldController {
    @FXML
    MenuController fxMenuStyringController;
    @FXML
    Leikbord fxLeikbord;
    private int erfidleiki = 1;

    @FXML
    public void initialize(){
        fxMenuStyringController.setController(this);
    }

    public void setErfidleikaStig(int id) {
        erfidleiki = id;
    }

    private void orvatakkar(){
        fxLeikbord.initControls();
    }

    /**
     * Stilla tíma m.v erfiðleikastig
     * Upphafstilla staðsetningu spilara
     * Stilla takka
     */
    public void hefjaLeik() {
        orvatakkar();
        System.out.println("Leikur hafin með erfiðleika: " + erfidleiki);
        fxLeikbord.setErfidleikaStig(erfidleiki);
        fxLeikbord.initGameLoop();
    }
}
