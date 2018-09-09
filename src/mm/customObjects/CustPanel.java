package mm.customObjects;

import javax.swing.*;
import java.awt.*;

public class CustPanel extends JPanel {
    private int w;
    private int h;

    public CustPanel (int w, int h){
        this.w = w;
        this.h = h;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }
}
