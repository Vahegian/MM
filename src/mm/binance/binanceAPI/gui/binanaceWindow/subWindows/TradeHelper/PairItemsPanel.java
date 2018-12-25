package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.customObjects.Colors;
import mm.customObjects.CustTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PairItemsPanel extends JPanel {
    private final int fieldwidth = 7;
    private Color colors_background;
    private Color colors_foreground;
    public CustTextPane p1;
    public CustTextPane p2;
    public CustTextPane p3;
    public CustTextPane p4;
    public CustTextPane p5;
    public CustTextPane p6;
    public CustTextPane p7;
    public JButton p8;
    private PairItemsPanel I = this;
    private int ID;
    private TradeHelperWindow thw;
    public PairItemsPanel(TradeHelperWindow thw, int id, int width, int height, String s1, String s2, String s3, String s4, Color back, Color fore){
        this.ID = id;
        this.thw = thw;
        this.colors_background = back;
        this.colors_foreground = fore;
        setBounds(0, 0, width, height);
//        setLayout(null);
//                    items.setLayout(null);
        setBackground(Colors.white);
//                    FlowLayout fl = new FlowLayout();
//                    fl.setHgap(5);
//                    fl.setAlignment(FlowLayout.LEFT);
//                    items.setLayout(fl);
//                    ((JTextField)listWithAddeditems.get(i).get("pairfield")).setColumns(10);
        p1 = new CustTextPane("0",2,"LEFT");
        p1.setEditable(false);
        p1.setBackground(colors_background);
        p1.setForeground(colors_foreground);
        p1.setText(s1);
        p1.setColumns(fieldwidth);
        p1.moreOnClick(this);
//        p1.setBounds((width-(8*90)),0,90,30);
        p1.setBorder(BorderFactory.createEmptyBorder());
//        p1.setBounds(0,0,width/7,height);
//                    p1.setColumns(cols);
        add(p1);
//                System.out.println(l.get(0).getText());
//                    ((JTextField)listWithAddeditems.get(i).get("amountfield")).setColumns(10);
        p2 = new CustTextPane("0",2,"LEFT");
        p2.setEditable(false);
        p2.setBackground(colors_background);
        p2.setForeground(colors_foreground);
        p2.setText(s2);
        p2.setColumns(fieldwidth);
//        p2.setBounds((width-(7*90)),0,90,30);
        p2.setBorder(BorderFactory.createEmptyBorder());
//        p2.setBounds((width/7),0,width/7,height);

        add(p2);
//                    ((JTextField)listWithAddeditems.get(i).get("curPricefield")).setColumns(10);
        p3 = new CustTextPane("0",2,"LEFT");
        p3.setEditable(false);
        p3.setBackground(colors_background);
        p3.setForeground(colors_foreground);
        p3.setText(s3);
        p3.setColumns(fieldwidth);
//        p3.setBounds((width-(6*90)),0,90,30);
        p3.setBorder(BorderFactory.createEmptyBorder());
//        p3.setBounds((width/7)*2,0,width/7,height);
//                    p3.setColumns(cols);
        add(p3);

        p4 = new CustTextPane("0",2,"LEFT");
        p4.setEditable(false);
        p4.setBackground(colors_background);
        p4.setForeground(colors_foreground);
        p4.setText(s4);
        p4.setColumns(3);
//        p4.setBounds((width-(5*90)),0,90,30);
        p4.setBorder(BorderFactory.createEmptyBorder());
//        p3.setBounds((width/7)*2,0,width/7,height);
//                    p3.setColumns(cols);
        add(p4);

//                    ((CustTextPane)listWithAddeditems.get(i).get("profitpane")).setMaximumSize(panedim);
        p5 = new CustTextPane("0",1,"LEFT");
        p5.setForeground(colors_foreground);
        p5.withDollarSign = true;
        p5.copyOnClick();
//        p5.setBounds((width-(4*90)),0,90,30);
//        p4.setBounds((width/7)*3,0,width/7,height);
        add(p5);
//                    ((JButton)listWithAddeditems.get(i).get("removebutton"))
        p6 = new CustTextPane("0", 1, "LEFT");
        p6.setForeground(colors_foreground);
        p6.copyOnClick();
//        p6.setBounds((width-(3*90)),0,90,30);
//        p5.withDollarSign = true;
//        p5.setBounds((width/7)*4,0,width/7,height);
        add(p6);

        p7 = new CustTextPane("0", 1,"LEFT");
        p7.setForeground(colors_foreground);
        p7.withDollarSign = true;
        p7.copyOnClick();
//        p7.setBounds((width-(2*90)),0,90,30);
//                    p6.setBounds((sx/7)*5,0,sx/7,height);
        add(p7);

        p8 = new JButton();
//        remove.setEditable(false);
        p8.setForeground(Colors.red);
        p8.setBackground(colors_background);
        p8.setText("Remove");
//        remove.setColumns(10);
//        p8.setBounds((width-(90)),0,90,30);

        p8.setBorder(BorderFactory.createEmptyBorder());
//                    p7.setBounds((sx/7)*6,0,sx/7,height);
        add(p8);

        p8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thw.removePanelFromMiddlePanel(I.ID);
            }
        });
    }

    public CustTextPane getAmountPane(){return p2;}
    public CustTextPane getPricePane(){return p3;}
    public CustTextPane getFeePane(){return p4;}
    public PairItemsPanel(){}

    public void setID(int id) {
        I.ID = id;
    }
}
