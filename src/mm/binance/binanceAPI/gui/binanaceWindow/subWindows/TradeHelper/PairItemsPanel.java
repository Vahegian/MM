package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.customObjects.Colors;
import mm.customObjects.CustTextPane;

import javax.swing.*;

public class PairItemsPanel extends JPanel {
    private final int fieldwidth = 7;
    public JTextField p1;
    public JTextField p2;
    public JTextField p3;
    public JTextField p4;
    public CustTextPane p5;
    public CustTextPane p6;
    public CustTextPane p7;
    public JButton p8;

    public PairItemsPanel(int width, int height, String s1, String s2, String s3, String s4){
        setBounds(0, 0, width, height);
//        setLayout(null);
//                    items.setLayout(null);
        setBackground(Colors.white);
//                    FlowLayout fl = new FlowLayout();
//                    fl.setHgap(5);
//                    fl.setAlignment(FlowLayout.LEFT);
//                    items.setLayout(fl);
//                    ((JTextField)listWithAddeditems.get(i).get("pairfield")).setColumns(10);
        p1 = new JTextField();
        p1.setEditable(false);
        p1.setBackground(Colors.white);
        p1.setForeground(Colors.blue);
        p1.setText(s1);
        p1.setColumns(fieldwidth);
//        p1.setBounds((width-(8*90)),0,90,30);
        p1.setBorder(BorderFactory.createEmptyBorder());
//        p1.setBounds(0,0,width/7,height);
//                    p1.setColumns(cols);
        add(p1);
//                System.out.println(l.get(0).getText());
//                    ((JTextField)listWithAddeditems.get(i).get("amountfield")).setColumns(10);
        p2 = new JTextField();
        p2.setEditable(false);
        p2.setBackground(Colors.white);
        p2.setForeground(Colors.blue);
        p2.setText(s2);
        p2.setColumns(fieldwidth);
//        p2.setBounds((width-(7*90)),0,90,30);
        p2.setBorder(BorderFactory.createEmptyBorder());
//        p2.setBounds((width/7),0,width/7,height);

        add(p2);
//                    ((JTextField)listWithAddeditems.get(i).get("curPricefield")).setColumns(10);
        p3 = new JTextField();
        p3.setEditable(false);
        p3.setBackground(Colors.white);
        p3.setForeground(Colors.blue);
        p3.setText(s3);
        p3.setColumns(fieldwidth);
//        p3.setBounds((width-(6*90)),0,90,30);
        p3.setBorder(BorderFactory.createEmptyBorder());
//        p3.setBounds((width/7)*2,0,width/7,height);
//                    p3.setColumns(cols);
        add(p3);

        p4 = new JTextField();
        p4.setEditable(false);
        p4.setBackground(Colors.white);
        p4.setForeground(Colors.blue);
        p4.setText(s4);
        p4.setColumns(3);
//        p4.setBounds((width-(5*90)),0,90,30);
        p4.setBorder(BorderFactory.createEmptyBorder());
//        p3.setBounds((width/7)*2,0,width/7,height);
//                    p3.setColumns(cols);
        add(p4);

//                    ((CustTextPane)listWithAddeditems.get(i).get("profitpane")).setMaximumSize(panedim);
        p5 = new CustTextPane("0",1,"LEFT");
        p5.setForeground(Colors.blue);
        p5.withDollarSign = true;
//        p5.setBounds((width-(4*90)),0,90,30);
//        p4.setBounds((width/7)*3,0,width/7,height);
        add(p5);
//                    ((JButton)listWithAddeditems.get(i).get("removebutton"))
        p6 = new CustTextPane("0", 1, "LEFT");
        p6.setForeground(Colors.blue);
//        p6.setBounds((width-(3*90)),0,90,30);
//        p5.withDollarSign = true;
//        p5.setBounds((width/7)*4,0,width/7,height);
        add(p6);

        p7 = new CustTextPane("0", 1,"LEFT");
        p7.setForeground(Colors.blue);
        p7.withDollarSign = true;
//        p7.setBounds((width-(2*90)),0,90,30);
//                    p6.setBounds((sx/7)*5,0,sx/7,height);
        add(p7);

        p8 = new JButton();
//        remove.setEditable(false);
        p8.setForeground(Colors.red);
        p8.setBackground(Colors.white);
        p8.setText("Remove");
//        remove.setColumns(10);
//        p8.setBounds((width-(90)),0,90,30);

        p8.setBorder(BorderFactory.createEmptyBorder());
//                    p7.setBounds((sx/7)*6,0,sx/7,height);
        add(p8);
    }

    public PairItemsPanel(){}
}
