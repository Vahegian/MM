package mm.mainWindow;

import mm.coinCap.api.CoinCollector;
import mm.customObjects.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CoinBuyPanle extends JPanel {
    private Colors colors;
    private final int fontSize = 18;
    private CoinCollector coinCollector;
//    allows user to add amount to selected coin

    public CoinBuyPanle(CoinCollector coinCollector, Colors colors){
        this.coinCollector = coinCollector;
        this.colors = colors;
        setLayout(null);
        setBackground(colors.white);
        makeComponents();


    }

    private void makeComponents(){
        JTextArea select = new JTextArea();
        select.setLayout(null);
        select.setBounds(0,0,100,25);
        select.setText("Select Coin");
        select.setBackground(colors.white);
        select.setEditable(false);
        select.setFont(new Font("Serif", Font.PLAIN, fontSize));
        add(select);
        JComboBox<String> coinSelect = new JComboBox<String>();
        for(String coin: coinCollector.getMarkets().keySet()) {
            coinSelect.addItem(coin);
        }
        coinSelect.setLayout(null);
        coinSelect.setBounds(110,0,100,25);
        add(coinSelect);

        JTextArea amount = new JTextArea();
        amount.setLayout(null);
        amount.setBounds(220,0,100,25);
        amount.setText("Amount");
        amount.setBackground(colors.white);
        amount.setEditable(false);
        amount.setFont(new Font("Serif", Font.PLAIN, fontSize));
        add(amount);

        JTextArea amountGet = new JTextArea();
        amountGet.setLayout(null);
        amountGet.setBounds(330,0,100,25);
        amountGet.setEditable(true);
        amountGet.setFont(new Font("Serif", Font.PLAIN, fontSize));
        add(amountGet);

        JTextArea btc = new JTextArea();
        btc.setLayout(null);
        btc.setBounds(440,0,100,25);
        btc.setEditable(false);
        btc.setText("BTC Price");
        btc.setBackground(colors.white);
        btc.setFont(new Font("Serif", Font.PLAIN, fontSize));
        add(btc);

        JTextArea btcGet = new JTextArea();
        btcGet.setLayout(null);
        btcGet.setBounds(550,0,100,25);
        btcGet.setEditable(true);
        btcGet.setFont(new Font("Serif", Font.PLAIN, fontSize));
        add(btcGet);

        JButton add = new JButton();
        add.setLayout(null);
        add.setBounds(660,0,100,25);
        add.setText("ADD");
        add.setBackground(colors.white);
        add(add);
    }

    private void setButtonHendler (Button but, JComboBox<String> cb,JTextArea amount,JTextArea btcP){
        but.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

}
