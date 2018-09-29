package mm.startGui;

import mm.customObjects.Colors;

import javax.swing.*;
import java.awt.*;

public class RunTimeInfoWindow extends InfoFrame {
    private final JPanel mainPanel;
    private final JTextArea hint;
    private final JTextArea value;

    public RunTimeInfoWindow(int lx, int ly) {
        super("Info", lx, ly,240, 30);
        mainPanel = new JPanel();
        mainPanel.setBounds(0,0,sx,sy);
        mainPanel.setBackground(Colors.white);
        hint = new JTextArea();
        hint.setBackground(Colors.white);
        hint.setEditable(false);
        value = new JTextArea();
        value.setBackground(Colors.white);
        value.setEditable(false);
        mainPanel.add(hint);
        mainPanel.add(value);
        add(mainPanel);

    }

    public void setInfo(String hint, String value){
        this.hint.setText(hint);
        this.value.setText(value);
        repaint();
    }
}
