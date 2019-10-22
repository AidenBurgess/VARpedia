package app;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.lang.reflect.*;

/**
* Represents a tooltip with text that appears when hovering over a button the tooltip is attached to.
* Styled according to target user (children ages 7-10)
*/
public class HoverToolTip {

    // Field declarations
    private String desc;

    /**
     * Create the tooltip with a given description
     * @param desc
     */
    public HoverToolTip(String desc) {
        this.desc=desc;
    }

    /**
     * Create tooltip base with style etc. included, and return the created tooltip
     * @return tooltip
     */
    public Tooltip getToolTip() {
        Tooltip tip = new Tooltip(desc);
        tip.setWidth(200.0);
        tip.setWrapText(true);
        tip.setStyle("-fx-font-size: 16");
        tooltipStartTiming(tip);
        return tip;
    }

    /**
     * Shorten the delay time before the tooltip message appears on screen through Reflection
     */
    public static void tooltipStartTiming(Tooltip tip) {
        try {
            // Access the tooltip's behaviour field and save it's contents to a variable
            Field fieldBehaviour = tip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehaviour.setAccessible(true);
            Object objBehaviour = fieldBehaviour.get(tip);

            // Access the tooltip's timer field and save it's contents to a variable
            Field fieldTimer = objBehaviour.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehaviour);

            // Set the new delay time for the tooltip message appearance
            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(50)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
