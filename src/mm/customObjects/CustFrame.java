package mm.customObjects;

import javax.swing.*;
import java.awt.event.*;

public abstract class CustFrame extends JFrame {
    private final String title;
    public JPanel toolPanel;
    public int lx, ly, sx, sy, yGap;
    protected JScrollPane sp;
//    public static boolean state = true;
    protected final int [] windowNumber = new int[1];
    public boolean killAllWindows = false;
    CustFrame I = this;

    protected final boolean[] windowsStates = {false,false,false,false};

    public final int[] progress = {0};
    private boolean mouseisClicked = false;
    private CustButton exitBut;
    private CustTextPane frameName;

    public CustFrame(String title, int lX, int lY, int sX, int sY) {
        this.title = title; this.lx = lY; this.ly = lY; this.sx = sX; this.sy = sY; this.yGap = 30;
//        setOpacity(0.9f);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setUndecorated(true);
        makeTheRootPanel();
        addListenersToRootPanel();
        setLayout(null);
//        setContentPane(sp);
        setBounds(lX,lY,sX,sY);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setBackground(Colors.white);
        setTitle(title);
//        setVisible(true);

//        repaint();

//        state = this.hasFocus();

    }

    private void addListenersToRootPanel() {
        exitBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(killAllWindows) System.exit(1);
                else I.dispose();
            }
        });


        toolPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                lx = e.getXOnScreen(); ly = e.getYOnScreen();
                setBounds(lx, ly, sx, sy);
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    private void makeTheRootPanel() {
        toolPanel = new JPanel();
        toolPanel.setLayout(null);
        toolPanel.setBackground(Colors.white);
        toolPanel.setBounds(0,0,sx,yGap);

        exitBut = new CustButton("X", 0, 0, 50, yGap);
        exitBut.setForeground(Colors.red);
        exitBut.setBorder(BorderFactory.createEmptyBorder());

        frameName = new CustTextPane(this.title, 2, "LEFT");
        frameName.setBounds(sx-150,yGap-25,150, yGap);

        toolPanel.add(frameName);
        toolPanel.add(exitBut);

        add(toolPanel);


    }

    public void dontShowTitle(){
        toolPanel.remove(frameName);
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
