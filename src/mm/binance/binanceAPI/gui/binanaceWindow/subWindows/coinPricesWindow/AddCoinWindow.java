package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.coinPricesWindow;

/*
This class creates a window to get coin and ticker from user and add new coin into db for
"coinCapMarket"
 */

import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

class AddCoinWindow extends JFrame implements Runnable {
    private BinanceController bc;
    private CoinPricesWindow context;
//    private BinanceWindow context;
    public AddCoinWindow(CoinPricesWindow context, BinanceController bc){
        this.bc = bc;
        this.context = context;
//        this.context = context;

    }

    private void addNewCoin(String coin, String pair){
        String newCoin = (coin+pair).toUpperCase();
        Map<String, Double> existingCoins = bc.getLastPrices();
        if(existingCoins.containsKey(newCoin)){
            JOptionPane info = new JOptionPane();
            info.setMessage("Coin already exists!");
            info.createDialog("INFO").show();
            return;
        }else {
//        String newTicker = ticker.substring(0).toUpperCase();
            if(pair.length()<1){
                JOptionPane info = new JOptionPane();
                info.setMessage("No Pair is entered!");
                info.createDialog("INFO").show();
                return;
            }

            if (bc.doesCoinPairExist(newCoin)) {
                bc.addPairsFromDbToMap(10);
                context.updateSPView();
            }else {
                JOptionPane info = new JOptionPane();
                info.setMessage("Ticker is Wrong!");
                info.createDialog("INFO").show();
            }
        }
    }

    @Override
    public void run() {
        JPanel inputPanle = new JPanel();

        JTextField coinText = new JTextField();
        coinText.setLayout(null);
        coinText.setBounds(0,0,120,40);
        coinText.setBackground(Colors.white);

        JTextField pairText = new JTextField();
        pairText.setLayout(null);
        pairText.setEditable(false);
        pairText.setText("BTC");
        pairText.setBounds(120,0,120, 40);
        pairText.setBackground(Colors.white);

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

        JTextField pairHint = new JTextField();
        setLayout(null);
        pairHint.setText("Pair"); pairHint.setForeground(Colors.blue); pairHint.setEditable(false);
        pairHint.setBounds(120,0,240,40);
        pairHint.setBackground(Colors.white);

        JPanel hintPanle = new JPanel();
        hintPanle.setLayout(null);
        hintPanle.add(coinHint); hintPanle.add(pairHint);
        hintPanle.setBackground(Colors.white);
        hintPanle.setBounds(0,0,360,40);

        inputPanle.setLayout(null);
        inputPanle.add(coinText); inputPanle.add(pairText); inputPanle.add(addCoin);
        inputPanle.setBackground(Colors.white);
        inputPanle.setBounds(0,40,360,40);




        add(inputPanle);
        add(hintPanle);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        setResizable(false);
        setTitle("Add New Coin Pair");
        setBackground(Colors.white);
        setBounds(dim.width/2-180,dim.height/2-60,360,120);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        repaint();

        addCoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addNewCoin(coinText.getText(), pairText.getText());
            }
        });

    }
}

