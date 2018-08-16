package mm.mainWindow;

import mm.database.DatabaseController;
import mm.customObjects.Colors;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private DatabaseController db;
//    private CoinCollector coinCollector;
//    public static final int[] progress = {0};

//    combines all jPanel classes into one frame and updates the frame every 50ms
    public Frame() {
//        Colors colors = new Colors("");
//        this.coinCollector = coinCollector;
        getDBnUser();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setBackground(Colors.white);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        System.out.println(dim);
        int[] centerDim = {dim.width / 2, dim.height / 2};
        setBounds(centerDim[0] - 225, 0, 450, 70);
        System.out.println(getBounds());
        setTitle("Money Maker");

        OtherButtonsPanel otherButtonsPanel = new OtherButtonsPanel(db, centerDim);
        otherButtonsPanel.setBounds(0, 0, 840, 30);
        add(otherButtonsPanel);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        repaint();



//        Thread refreshThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    repaint();
//                    try {
//                        Thread.sleep(50);
////                        System.out.println("Refreshed");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
////        refreshThread.start();
    }
    private void getDBnUser(){
        String user= JOptionPane.showInputDialog("Input db user: ");
        String pass = JOptionPane.showInputDialog("Input password: ");
        String dbname= JOptionPane.showInputDialog("Input DB name: ");
        if(dbname.length()<1) System.exit(1);
        try {
            db = new DatabaseController(dbname, user, pass);
        }catch (Exception e){
            getDBnUser();
        }
    }
}
