/*
cutomised button for CoinCapWindow
 */
package mm.customObjects;

import javax.swing.*;

public class CustButton extends JButton {

    public CustButton(String text , int x, int y, int w, int h){
        setLayout(null);
        setBackground(Colors.white);
        setForeground(Colors.blue);
        setBounds(x,y,w,h);
        setText(text);
    }
}
