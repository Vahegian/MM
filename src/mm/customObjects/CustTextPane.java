/*
This class is a custom JTextPane which puts text in the Pane with CENTER alignment
and makes it easy to change color of the text and the text itself by method calls;
 */

package mm.customObjects;

import mm.Main;
import mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper.PairItemsPanel;
import mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper.SellAtPriceWindow;
import mm.startGui.RunTimeInfoWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.awt.datatransfer.Clipboard;


public class CustTextPane extends JTextField {
    private String color = "black";
    private String align;
    private String text;
    private String dir = "";
    private int type;
    public boolean withDollarSign = false;
    public boolean withPoundSign = false;
    private Color colorObject;
    private CustTextPane I = this;

    public CustTextPane(String tx, int type, String alignment){
        this.text = tx;
        this.type = type;
        this.align = alignment;
        setBackground(Colors.white);
        setEditable(false);
        setVisible(true);
        setColumns(10);
        setBorder(BorderFactory.createEmptyBorder());

//        if(align.equals("CENTER"))
//            setContentType("text/html");

        setFieldText(text);

    }

    public void setFieldText(String tx){
//        if(!withPoundSign && !withDollarSign) withDollarSign = true;

        this.text = tx;
        if(align.equals("CENTER")) {
            if (type == 1) {
                text = new DecimalFormat("#0.00000").format(Double.parseDouble(text));
                if(withDollarSign) this.setText("<HTML><BODY><CENTER><font color=\""+ color +"\">"+ "\u0024"+text+"</font></CENTER></BODY></HTML>");
                else this.setText("<HTML><BODY><CENTER><font color=\""+ color +"\">"+ ""+text+"</font></CENTER></BODY></HTML>");
            } else if (type == 2)
                this.setText("<HTML><BODY><CENTER><font color=\"navy\">" + text + "</font></CENTER></BODY></HTML>");
            else throw new IllegalArgumentException("Wrong Type!");
        }else if (align.equals("LEFT")){
            if (type == 1) {
                text = new DecimalFormat("#000.00000").format(Double.parseDouble(text));
                if(text.length()<10){
//                    System.err.println(text+" : "+text.length());
                    for(int i=0; i<10-text.length(); i++){
                        text+='0';
                    }
                }

                if(withDollarSign && !withPoundSign) this.setText("     \u0024" + text);
                else if (!withDollarSign && withPoundSign) this.setText("     \u00A3" + text);
                else this.setText("     " + text);
            } else if (type == 2)
                this.setText(""+text);
            else throw new IllegalArgumentException("Wrong Type!");
        }
    }

    public void setFieldColor(Color col, String color){
        colorObject = col;
        this.color = color;
        if(align.equals("LEFT")) {
            this.setForeground(col);
            if(col.equals(Colors.green))dir = "UP";
            else if(col.equals(Colors.red)) dir ="DOWN";
        }else if(align.equals("CENTER")){
            if (type == 1) {
                this.setText("<HTML><BODY><CENTER><font color=\""+color+"\">"+ "\u0024"+text+"</font></CENTER></BODY></HTML>");
            } else if (type == 2)
                this.setText("<HTML><BODY><CENTER><font color=\"navy\">" + text + "</font></CENTER></BODY></HTML>");

        }
        repaint();
    }

    public String getPlainText(){return text;}

    public Object[] getColors(){
        Object o[] =  {colorObject,color};
        return o;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(type==1)
            Triangles.draw(g,10,6,dir);

    }

    public void copyOnClick(){
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Clipboard clipboard = toolkit.getSystemClipboard();

                clipboard.setContents(new StringSelection(text), null);
                RunTimeInfoWindow rtiw = new RunTimeInfoWindow(0,0);
                rtiw.setInfo("Copied! ", text);
                rtiw.goAway(1000);

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                I.setBackground(Colors.lightBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                I.setBackground(Colors.white);
            }
        });
    }

    public void moreOnClick(PairItemsPanel items){
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Main.workers.execute(new Runnable() {
                    @Override
                    public void run() {
                        Main.plusThread();

                        SellAtPriceWindow sapw = new SellAtPriceWindow("Profit At Price",e.getXOnScreen(),e.getYOnScreen());
                        JTextField bp = sapw.getBuyPrice();
                        JTextField pw = sapw.getProfitWant();
                        pw.setText("11");
                        bp.setText(calculateSellPrice(items, 11.0).toString());
                        pw.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
//                                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                                try {
                                    bp.setText(calculateSellPrice(items, Double.parseDouble(pw.getText())).toString());
                                }catch (Exception d){
                                    System.err.println(d);
                                }
//                                }
                            }
                        });
//                        pw.addKeyListener(new KeyListener() {
//                            @Override
//                            public void keyTyped(KeyEvent e) {
//
//                            }
//
//                            @Override
//                            public void keyPressed(KeyEvent e) {
//                                if(e.getKeyCode() == KeyEvent.VK_ENTER){
//                                    bp.setText(calculateSellPrice(items,Double.parseDouble(pw.getText())).toString());
//                                }
//                            }
//
//                            @Override
//                            public void keyReleased(KeyEvent e) {
//
//                            }
//                        });

                        Main.minusThread();
                    }
                });


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                I.setBackground(Colors.lightBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) { I.setBackground(Colors.white); }
        });
    }

    private Double calculateSellPrice(PairItemsPanel items, Double profitWanted) {
        Double pAmount =  Double.parseDouble(items.getAmountPane().getText()) ;
        Double pPrice = Double.parseDouble(items.getPricePane().getText());
        Double pFee =  Double.parseDouble(items.getFeePane().getText());

        Double sellPrice = ((profitWanted+((pFee/100)*profitWanted)) + (pAmount*pPrice))/pAmount;
        return sellPrice;
    }
}
