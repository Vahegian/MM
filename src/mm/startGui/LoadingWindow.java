/*
Shows the progress of how much have the program loaded
 */

package mm.startGui;

import mm.customObjects.CustFrame;
import mm.customObjects.Colors;
import mm.Main;

import javax.swing.*;
import java.awt.*;

public class LoadingWindow <T extends CustFrame> extends InfoFrame implements Runnable {
//    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//    private Main main;
    private int [] window = new int[2];
    private T t = null;
    private Graphics2D g;
    private JPanel panel;

    public LoadingWindow(T t){
        super("Please Wait!", Toolkit.getDefaultToolkit().getScreenSize().width/2-180,Toolkit.getDefaultToolkit().getScreenSize().height/2-60,360,60);
        this.t = t;

        panel = new JPanel();
        add(panel);

        System.err.println("loading window loaded");
    }


    @Override
    public void paint(Graphics graphics) {
        try {
            panel.paint(graphics);
        }catch (Exception e){}
        g = (Graphics2D)graphics;
//        g.fillRect(0,0,10,10);
        paintProgress(g);
        paintMessage(g);

//        System.out.println(t.progress[0]);
//        synchronized (t.progress){

//        }
    }

    private void paintMessage(Graphics2D g){
        g.setColor(Colors.white);
        g.drawString("Please Wait! "+t.progress[0]+"%", 140, 35);
    }

    private void paintProgress(Graphics2D g){
        g.setColor(Colors.black);
        g.fillRect(0,0,360,60);
        g.setColor(Colors.blue);
        synchronized (progress) {
            g.fillRect(0, 0, (int) ((3.6 * t.progress[0]) + 0.5), 60);
        }
    }

    @Override
    public void run() {
        Main.plusThread();
        while(t.progress[0] <100) {
            repaint();
            try {
                repaint();
//                System.out.println(t.progress[0]);
                Thread.sleep(30);
//                Main.progress[0]+=1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        repaint();
//        t.progress[0]=0;
        dispose();
        Main.minusThread();
    }
}
