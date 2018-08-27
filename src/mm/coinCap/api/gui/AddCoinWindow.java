/*
This class creates a window to get coin and ticker from user and add new coin into db for
"coinCapMarket"
 */

package mm.coinCap.api.gui;

import mm.Main;
import mm.coinCap.api.CoinCollector;
import com.lucadev.coinmarketcap.model.CoinMarket;
import mm.customObjects.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

class AddCoinWindow extends JFrame implements Runnable {
    private CoinCollector cc;
    private CoinCapWindow context;
    public AddCoinWindow(CoinCapWindow context, CoinCollector cc){
        this.cc = cc;
        this.context = context;

    }

    private void addNewCoin(String coin, String ticker){
        String newCoin = coin.toUpperCase();
        Map<String, CoinMarket> existingCoins = cc.getMarkets();
        if(existingCoins.containsKey(newCoin)){
            JOptionPane info = new JOptionPane();
            info.setMessage("Coin already exists!");
            info.createDialog("INFO").show();
            return;
        }else {
//        String newTicker = ticker.substring(0).toUpperCase();
            if(ticker.length()<1){
                JOptionPane info = new JOptionPane();
                info.setMessage("No Ticker is entered!");
                info.createDialog("INFO").show();
                return;
            }

            if (cc.doesCoinExist(ticker)) {
                cc.addCoin("'" + newCoin + "'", "'" + ticker + "'");
                cc.reCheckDbTable();
                context.addNewCoin();
            }else {
                JOptionPane info = new JOptionPane();
                info.setMessage("Ticker is Wrong!");
                info.createDialog("INFO").show();
            }
        }
    }

    @Override
    public void run() {
        Main.plusThread();
        JPanel inputPanle = new JPanel();

        JTextField coinText = new JTextField();
        coinText.setLayout(null);
        coinText.setBounds(0,0,120,40);
        coinText.setBackground(Colors.white);

        JTextField tickerText = new JTextField();
        tickerText.setLayout(null);
        tickerText.setBounds(120,0,120, 40);
        tickerText.setBackground(Colors.white);

        JButton addCoin = new JButton("ADD");
        addCoin.setLayout(null);
        addCoin.setBounds(240,0,120,40);
        addCoin.setForeground(Colors.blue);
        addCoin.setBackground(Colors.white);

        JTextField coinHint = new JTextField();
        setLayout(null);
        coinHint.setText("Coin"); coinHint.setForeground(Colors.blue); coinHint.setEditable(false);
        coinHint.setBounds(0,0,120,40);
        coinHint.setBackground(Colors.white);

        JTextField tickerHint = new JTextField();
        setLayout(null);
        tickerHint.setText("Ticker"); tickerHint.setForeground(Colors.blue); tickerHint.setEditable(false);
        tickerHint.setBounds(120,0,240,40);
        tickerHint.setBackground(Colors.white);

        JPanel hintPanle = new JPanel();
        hintPanle.setLayout(null);
        hintPanle.add(coinHint); hintPanle.add(tickerHint);
        hintPanle.setBackground(Colors.white);
        hintPanle.setBounds(0,0,360,40);

        inputPanle.setLayout(null);
        inputPanle.add(coinText); inputPanle.add(tickerText); inputPanle.add(addCoin);
        inputPanle.setBackground(Colors.white);
        inputPanle.setBounds(0,40,360,40);




        add(inputPanle);
        add(hintPanle);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        setResizable(false);
        setTitle("Add New Coin");
        setBackground(Colors.white);
        setBounds(dim.width/2-180,dim.height/2-60,360,120);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        repaint();

        addCoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addNewCoin(coinText.getText(), tickerText.getText());
            }
        });
        Main.minusThread();
    }
}
