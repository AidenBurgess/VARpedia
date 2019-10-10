package app;
import javafx.scene.control.Tooltip;

public class HoverToolTip {
    private String desc;

    public HoverToolTip(String desc) {
        this.desc=desc;
    }

    protected Tooltip getToolTip() {
        Tooltip tip = new Tooltip(desc);
        tip.setWidth(200.0);
        tip.setWrapText(true);
        return tip;
    }
}
