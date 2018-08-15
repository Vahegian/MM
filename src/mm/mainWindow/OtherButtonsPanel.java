package mm.mainWindow;

import mm.database.DatabaseController;
import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.binance.binanceAPI.gui.binanaceWindow.BinanceWindow;
import mm.coinCap.api.gui.CoinCapWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// shows current prices of coins
public class OtherButtonsPanel extends JPanel {
//    public static final boolean[] mainWindowsState = {false, false, false, false};

    private CustButton coinCapBut;
    private CustButton hitBTCbut;
    private CustButton binanceBut;
    private CustButton qryptosBut;

    private int[] screenDim;

    private CoinCapWindow coinCap;
    private  BinanceWindow binance;

    public OtherButtonsPanel(DatabaseController db, int[] dim){
//        setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
        screenDim=dim;
        setLayout(null);
        setBackground(Colors.white);
        coinCapBut = new CustButton("CoinCap",10,0,100,30);
        binanceBut = new CustButton("Binance", 120,0,100,30);
        hitBTCbut = new CustButton("HITBTC", 230,0,100,30);
        qryptosBut = new CustButton("Qryptos", 340,0,100,30);
//        Main.progress[0] = 75;

        makeCoinCapBut(coinCapBut, db);
        makeBinanceBut(binanceBut,db);
        addAllToPanel();

    }

    private void makeCoinCapBut(JButton bt, DatabaseController db){
//        Main.progress[0] = 80;
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(coinCap==null || !coinCap.isShowing()) {
                            coinCap = new CoinCapWindow("CoinMarketCap", screenDim[0] + (screenDim[0] - 240), 0, db);
                            new Thread(coinCap).start();
                        }else coinCap.requestFocus();
                    }
                }).start();
            }
        });
    }

    private void makeBinanceBut(JButton bt, DatabaseController db){
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(binance == null || !binance.isShowing()) {
                            binance = new BinanceWindow("Binanace", screenDim[0] - 225, screenDim[1] - (screenDim[1] - 70), 450, 70, db);
                            new Thread(binance).start();
                        }else binance.requestFocus();
                    }
                }).start();
            }
        });
    }

    private void addAllToPanel(){

        add(coinCapBut);
        add(binanceBut);
        add(hitBTCbut);
        add(qryptosBut);
    }

//    public Thread StopClosedWindowsThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    });
}
