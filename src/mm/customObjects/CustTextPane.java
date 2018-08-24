/*
This class is a custom JTextPane which puts text in the Pane with CENTER alignment
and makes it easy to change color of the text and the text itself by method calls;
 */

package mm.customObjects;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class CustTextPane extends JTextPane {
    private String color = "black";
    private String align = "";
    private String text;
    private String dir = "";
    private int type;
    public boolean withDollarSign = true;
    public CustTextPane(String tx, int type, String alignment){
        this.text = tx;
        this.type = type;
        this.align = alignment;
        setBackground(Colors.white);
        setEditable(false);
        setVisible(true);

        if(align.equals("CENTER"))
            setContentType("text/html");

        setPaneText(text);

    }

    public void setPaneText(String tx){
        this.text = tx;
        if(align.equals(new String ("CENTER"))) {
            if (type == 1) {
                text = new DecimalFormat("#0.00000").format(Double.parseDouble(text));
                this.setText("<HTML><BODY><CENTER><font color=\""+color+"\">"+ "\u0024"+text+"</font></CENTER></BODY></HTML>");
            } else if (type == 2)
                this.setText("<HTML><BODY><CENTER><font color=\"navy\">" + text + "</font></CENTER></BODY></HTML>");
            else throw new IllegalArgumentException("Wrong Type!");
        }else if (align.equals(new String ("LEFT"))){
            if (type == 1) {
                text = new DecimalFormat("#000.00000").format(Double.parseDouble(text));
                if(text.length()<10){
//                    System.err.println(text+" : "+text.length());
                    for(int i=0; i<10-text.length(); i++){
                        text+='0';
                    }
                }
                if(withDollarSign) this.setText("     \u0024" + text);
                else this.setText("     " + text);
            } else if (type == 2)
                this.setText("     "+text);
            else throw new IllegalArgumentException("Wrong Type!");
        }
    }

    public void setPaneColor(Color col, String color){
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(type==1)
            Triangles.draw(g,10,6,dir);

    }
}
