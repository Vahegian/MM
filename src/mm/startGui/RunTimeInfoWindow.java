package mm.startGui;

import mm.Main;
import mm.customObjects.Colors;

import javax.swing.*;
import java.awt.*;

public class RunTimeInfoWindow extends InfoFrame {
    private final JPanel mainPanel;
    private final JTextArea hint;
    private final JTextArea value;
    private final RunTimeInfoWindow I = this;

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

    public void goAway(int i) {
        Main.workers.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.plusThread();
                    Thread.sleep(i);
                    I.dispose();
                    Main.minusThread();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
