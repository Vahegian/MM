package mm.customObjects;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class CustFrame extends JFrame {
    public JPanel toolPanel;
    public int sx,sy, yGap;
    protected JScrollPane sp;
//    public static boolean state = true;
    protected final int [] windowNumber = new int[1];
    public boolean killAllWindows = false;
    CustFrame I = this;

    protected final boolean[] windowsStates = {false,false,false,false};

    public final int[] progress = {0};

    public CustFrame(String title, int lx, int ly, int sX, int sY) {
        this.sx = sX; this.sy = sY; this.yGap = 30;
//        setOpacity(0.9f);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setUndecorated(true);
        toolPanel = new JPanel();
        toolPanel.setLayout(null);
        toolPanel.setBackground(Colors.white);
        toolPanel.setBounds(0,0,sx,yGap);
        CustButton exitBut = new CustButton("X", 0, 0, 50, yGap);
        exitBut.setForeground(Colors.red);
        exitBut.setBorder(BorderFactory.createEmptyBorder());
        toolPanel.add(exitBut);
        add(toolPanel);
        exitBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(killAllWindows) System.exit(1);
                else I.dispose();
            }
        });
        setLayout(null);
//        setContentPane(sp);
        setBounds(lx,ly,sX,sY);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setBackground(Colors.white);
        setTitle(title);
//        setVisible(true);

//        repaint();

//        state = this.hasFocus();

    }

    protected void checkState(CustFrame o){
//        System.out.println("checked");
        if(!this.isShowing()) {
            synchronized (windowsStates) { // stop the threads when window is closed
//                OtherButtonsPanel.mainWindowsState[window/Number[0]] = false;
                o.windowsStates[windowNumber[0]] = false;
            }
        }
    }

    protected void threadStateToDefault(CustFrame o){
        synchronized (windowsStates){
//            OtherButtonsPanel.mainWindowsState[windowNumber[0]]=true;
            o.windowsStates[windowNumber[0]] = true;
        }

    }
//    public abstract void paintComponents(Graphics g);
}
