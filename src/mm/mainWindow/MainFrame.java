package mm.mainWindow;

import mm.database.DatabaseController;
import mm.customObjects.Colors;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private DatabaseController db;
//    private CoinCollector coinCollector;
//    public static final int[] progress = {0};

//    combines all jPanel classes into one frame and updates the frame every 50ms
    public MainFrame() { }

    public void makeTheFrame(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setBackground(Colors.white);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        System.out.println(dim);
        int[] centerDim = {dim.width / 2, dim.height / 2};
        setBounds(centerDim[0] - 225, centerDim[1]-440, 450, 70);
        System.out.println(getBounds());
        setTitle("Money Maker");

        OtherButtonsPanel otherButtonsPanel = new OtherButtonsPanel(db, centerDim);
        otherButtonsPanel.setBounds(0, 0, 450, 30);
        add(otherButtonsPanel);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        repaint();
    }

    public void getDBnUser(String dbname, String user, String pass){
        try {
            db = new DatabaseController(dbname, user, pass);
        }catch (Exception e){
            System.err.println("MainFrame/getDBnUser >"+e);
        }
    }
}
