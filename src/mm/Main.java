package mm;

import mm.database.DatabaseController;
import mm.mainWindow.Frame;
import mm.startGui.LoadingWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        String user= JOptionPane.showInputDialog("Input db user: ");
        String pass = JOptionPane.showInputDialog("Input password: ");
        String dbname= JOptionPane.showInputDialog("Input DB name: ");


        DatabaseController database = new DatabaseController(dbname,user,pass);


        new Frame(database);

    }
}
