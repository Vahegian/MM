package mm.loginpkg;

import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.mainWindow.MainFrame;
import mm.startGui.InfoFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginWindow extends InfoFrame {
    private CustButton exitBut;
    private JPanel mainPanel;
    private CustButton enterBut;
    private JTextField dbName;
    private JTextField dbUser;
    private JPasswordField dbPassword;
    private LoginWindow me = this;
    private int fields = 1;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();


    public LoginWindow(String title, int lx, int ly) {
        super(title, lx-120, ly-100, 240, 200);
        setOpacity(0.9f);
        setAlwaysOnTop(false);
        killAllWindows = true;
        dontShowTitle();
        makeTopButtons();
        makeMainPanel();
        addListenersTobjects();

        add(toolPanel);
        add(mainPanel);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dbName.requestFocus();
        repaint();
    }

    private void addListenersTobjects() {
        enterBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        dbName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    dbUser.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        dbUser.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    dbPassword.requestFocus();
                }else if(e.getKeyCode()==KeyEvent.VK_UP){
                    dbName.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        dbPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    login();
                }else if(e.getKeyCode() == KeyEvent.VK_UP){
                    dbUser.requestFocus();
                }

//                if(e.getKeyCode() == KeyEvent.VK_DOWN){
//                    if(fields ==1){
//                        dbName.requestFocus();
//                    }else if (fields == 2){
//                        dbUser.requestFocus();
//                    }else if (fields == 3){
//                        dbPassword.requestFocus();
//                    }
//                    if(fields<3) fields++;
//                    else fields = 1;
//                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    private void login() {
        String user=dbUser.getText();
        String passw=dbPassword.getText();
        String dbname=dbName.getText();
        if(checkUserData(user,passw,dbname)){
            try {
                MainFrame frame = new MainFrame(dim.width/2, dim.height/2, dim);
                if (frame.getDBnUser(dbname,user,passw)) {
                    frame.makeTheFrame();
                    me.dispose();
                }else showDialog();
            }catch (Exception e1){System.err.println(e1+"");}
        }
    }

    private boolean checkUserData(String user, String pass, String dbname) {
        String[] data = {user,pass,dbname};
        for(String d:data){
//            System.err.println(d);
            if(d.length()<1){
                showDialog();
                return false;
            }
        }
        return true;
    }

    private void showDialog() {
        JOptionPane info = new JOptionPane();
        info.setMessage("Incomplete data");
        info.createDialog("INFO").show();
    }

    private void makeMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBounds(0,50,sx,150);
        mainPanel.setLayout(null);

        JTextArea namehint = new JTextArea("\ndb Name:");
        namehint.setBackground(Colors.white);
        namehint.setForeground(Colors.blue);
        namehint.setEditable(false);
        namehint.setBounds(0,0,100,50);
        JTextArea userhint = new JTextArea("\nUser:");
        userhint.setBackground(Colors.white);
        userhint.setForeground(Colors.blue);
        userhint.setEditable(false);
        userhint.setBounds(0,50,100,50);
        JTextArea passhint = new JTextArea("\nPassword:");
        passhint.setBackground(Colors.white);
        passhint.setForeground(Colors.blue);
        passhint.setEditable(false);
        passhint.setBounds(0,100,100,50);


        dbName = new JTextField();
        dbName.setBounds(100,0,140,50);


        dbUser = new JTextField();
        dbUser.setBounds(100,50,140,50);
        dbPassword = new JPasswordField();
        dbPassword.setBounds(100,100,140,50);

        mainPanel.add(namehint);
        mainPanel.add(dbName);
        mainPanel.add(userhint);
        mainPanel.add(dbUser);
        mainPanel.add(dbPassword);
        mainPanel.add(passhint);
    }

    private void makeTopButtons() {
//        toolPanel = new JPanel();
//        toolPanel.setBackground(Colors.white);
//        toolPanel.setBounds(0,0,sx,50);
//        exitBut = new CustButton("X",0,0,100,50);
//        exitBut.setForeground(Colors.red);
        enterBut = new CustButton("$", 100,0,140,yGap);
        enterBut.setForeground(Colors.green);
        enterBut.setBorder(BorderFactory.createEmptyBorder());
        toolPanel.add(enterBut);
    }
}
