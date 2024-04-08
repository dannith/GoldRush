package hi.verkefni5.vinnsla;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {

    public static RotateTransition wiggle(Node shape) {
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(shape);
        rotate.setDuration(Duration.millis(300));
        rotate.setCycleCount(Timeline.INDEFINITE);
        rotate.setFromAngle(2);
        rotate.setToAngle(-2);
        rotate.setAutoReverse(true);
        return rotate;
    }

    public static ScaleTransition stretchSquish(Node shape) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(shape);
        scale.setDuration(Duration.millis(500));
        scale.setCycleCount(Timeline.INDEFINITE);
        scale.setAutoReverse(true);
        scale.setByX(0.1);
        scale.setByY(-0.05);
        return scale;
    }

    public static void pulse(Node shape, double intensity) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(shape);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(intensity);
        scale.setToY(intensity);
        scale.setDuration(Duration.millis(400));
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    };
}
