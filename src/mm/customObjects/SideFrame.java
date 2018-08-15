package mm.customObjects;

import mm.mainWindow.OtherButtonsPanel;

import javax.swing.*;

public abstract class SideFrame extends JFrame {
//    private int x,y;
    protected JScrollPane sp;
//    public static boolean state = true;
    protected final int [] windowNumber = new int[1];

    protected final boolean[] windowsStates = {false,false,false,false};

    public final int[] progress = {0};

    public SideFrame(String title, int lx, int ly, int sX, int sY) {
//        this.x = lx; this.y = ly;
        setLayout(null);
//        setContentPane(sp);
        setBounds(lx,ly,sX,sY);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setBackground(Colors.white);
        setTitle(title);
//        setVisible(true);

//        repaint();

//        state = this.hasFocus();

    }

    protected void checkState(SideFrame o){
//        System.out.println("checked");
        if(!this.isShowing()) {
            synchronized (windowsStates) { // stop the threads when window is closed
//                OtherButtonsPanel.mainWindowsState[window/Number[0]] = false;
                o.windowsStates[windowNumber[0]] = false;
            }
        }
    }

    protected void threadStateToDefault(SideFrame o){
        synchronized (windowsStates){
//            OtherButtonsPanel.mainWindowsState[windowNumber[0]]=true;
            o.windowsStates[windowNumber[0]] = true;
        }

    }
//    public abstract void paintComponents(Graphics g);
}
