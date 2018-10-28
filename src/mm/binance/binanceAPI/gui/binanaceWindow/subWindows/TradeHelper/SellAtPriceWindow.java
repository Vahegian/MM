package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.customObjects.Colors;
import mm.customObjects.CustFrame;

import javax.swing.*;
import java.awt.*;

public class SellAtPriceWindow extends CustFrame {
    private JTextField profitWant;
    private JTextField buyPrice;

    public SellAtPriceWindow(String title, int lX, int lY) {
        super(title, lX, lY, 200, 200);

        JTextArea profitWantHint = new JTextArea();
        profitWantHint.setBounds(0,yGap,200,yGap);
        profitWantHint.setBackground(Colors.white);
        profitWantHint.setForeground(Colors.blue);
        profitWantHint.setText("\nThe Profit to be $");
        profitWantHint.setEditable(false);
        profitWantHint.setVisible(true);

        profitWant = new JTextField();
        profitWant.setBounds(0,yGap*2,200,50);
        profitWant.setBackground(Colors.white);
        profitWant.setVisible(true);

        JTextArea buyPriceHint = new JTextArea();
        buyPriceHint.setBounds(0,(yGap*2)+50,200,yGap);
        buyPriceHint.setBackground(Colors.white);
        buyPriceHint.setForeground(Colors.blue);
        buyPriceHint.setText("\nPrice to sell At $");
        buyPriceHint.setEditable(false);
        buyPriceHint.setVisible(true);

        buyPrice = new JTextField();
        buyPrice.setBounds(0,(yGap*3)+50,200,50);
        buyPrice.setBackground(Colors.white);
        buyPrice.setForeground(Colors.green);
        buyPrice.setEditable(false);
        buyPrice.setVisible(true);

        add(profitWantHint);
        add(profitWant);
        add(buyPriceHint);
        add(buyPrice);
        setVisible(true);
    }

    public JTextField getProfitWant(){return profitWant;}
    public JTextField getBuyPrice(){return buyPrice;}
}
