package app;
import javafx.scene.control.Tooltip;

public class HoverToolTip {
    private String desc;

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
