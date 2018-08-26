package mm.mainWindow;

import mm.customObjects.CustButton;
import mm.customObjects.SideFrame;
import sun.security.krb5.internal.PAData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackupWindow extends SideFrame {
    private String pass = "";
    private Runtime r;
    private Process p;
    public BackupWindow(String title, int lx, int ly, int sX, int sY) {
        super(title, lx, ly, sX, sY);

        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0,0,sx,sy);
        JTextArea userhint = new JTextArea("User");
        userhint.setEditable(false);
        JTextArea passhint = new JTextArea("Password");
        passhint.setEditable(false);
        JTextArea namehint = new JTextArea("db Name");

        JTextField dbUser = new JTextField();
        dbUser.setColumns(5);
        JTextField dbPassword = new JTextField();
        dbPassword.setColumns(5);
        dbPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });
        JTextField dbName = new JTextField();
        dbName.setColumns(5);
        CustButton backupBut = new CustButton("BACKUP",0,0,112,70);
        mainPanel.add(userhint);
        mainPanel.add(dbUser);
        mainPanel.add(passhint);
        mainPanel.add(dbPassword);
        mainPanel.add(namehint);
        mainPanel.add(dbName);
        mainPanel.add(backupBut);
        add(mainPanel);
        setVisible(true);

        r = Runtime.getRuntime();
        whenBackupButtonIspressed(backupBut,dbUser,dbPassword, dbName);

    }

    private void whenBackupButtonIspressed(CustButton backupBut, JTextField userf, JTextField passf, JTextField dbnamef) {

        backupBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String user=userf.getText();
                String passw=passf.getText();
                String dbname=dbnamef.getText();
                if(checkUserData(user,passw,dbname)){
                    try {
                        p=r.exec("mysqldump -u "+user+" -p"+passw+" "+dbname+" > mm.sql");
                        Thread.sleep(1000);
                        p.destroy();
                    }catch (Exception e){System.err.println(e+"");}
                }
            }
        });

    }

    private boolean checkUserData(String user, String pass, String dbname) {
        String[] data = {user,pass,dbname};
        for(String d:data){
//            System.err.println(d);
            if(d.length()<1){
                JOptionPane info = new JOptionPane();
                info.setMessage("Incomplete data");
                info.createDialog("INFO").show();
                return false;
            }
        }
        return true;
    }
}
