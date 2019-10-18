package app;
import javafx.scene.control.Tooltip;

/**
* Represents a tooltip with text that appears when hovering over a button the tooltip is attached to.
* Styled according to target user (children ages 7-10)
*/
public class HoverToolTip {
    private String desc;

    // Create the tooltip with a given description
    public HoverToolTip(String desc) {
        this.desc=desc;
    }

    // Create tooltip base
    public Tooltip getToolTip() {
        Tooltip tip = new Tooltip(desc);
        tip.setWidth(200.0);
        tip.setWrapText(true);
        tip.setStyle("-fx-font-size: 16");
        return tip;
    }
}
