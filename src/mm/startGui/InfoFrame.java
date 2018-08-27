package mm.startGui;

import mm.customObjects.Colors;
import mm.customObjects.SideFrame;

import javax.swing.*;

public class InfoFrame extends SideFrame {
    public InfoFrame(String title, int lx, int ly, int sX, int sY) {
        super(title, lx, ly, sX, sY);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setUndecorated(true);
        setBackground(Colors.black);
        setOpacity(0.8f);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);

        setVisible(true);
        repaint();
    }
}
