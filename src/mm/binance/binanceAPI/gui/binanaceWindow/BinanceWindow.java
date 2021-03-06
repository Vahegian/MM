package mm.binance.binanceAPI.gui.binanaceWindow;

import mm.Main;
import mm.binance.binanceAPI.BinanceController;
import mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper.TradeHelperWindow;
import mm.database.DatabaseController;
import mm.customObjects.CustButton;
import mm.customObjects.CustFrame;
import mm.binance.binanceAPI.gui.binanaceWindow.subWindows.coinPricesWindow.CoinPricesWindow;
import mm.startGui.LoadingWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BinanceWindow extends CustFrame implements Runnable {
    private String TAG = "BinanceWindow";
    private BinanceController bc;
    private BinanceWindow me =this;
    private CoinPricesWindow cpw;
    private TradeHelperWindow thw;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

//    public boolean[] binanceWindowsStates = {false,false,false};

    public BinanceWindow(String title, int lx, int ly, DatabaseController database) {
        super(title, lx, ly, 450, 70);
        windowNumber[0]=0;
        Main.workers.submit(new LoadingWindow<>(this));
//        new Thread(new LoadingWindow<>(this)).start();
        bc = new BinanceController(database, "DEFAULT");
        progress[0]=50;
        bc.addPairsFromDbToMap(10);
        progress[0]=90;
        Main.workers.submit(bc);
//        bc.updateThread.start();
        progress[0]=100;
        JPanel butPanel = new JPanel();
        butPanel.setLayout(null);
        CustButton coinPricesBut = new CustButton("Coin Prices", 10,0,140,30);
        CustButton tradeHelpBut = new CustButton("Trade Helper", 155,0,140,30);
        CustButton But3 = new CustButton("Auto Trade", 300,0,140,30);

        butPanel.add(coinPricesBut);
        butPanel.add(tradeHelpBut);
        butPanel.add(But3);
        butPanel.setBounds(0,yGap, sx, 40);
        add(butPanel);
        setVisible(true);

        coinPricesBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        Main.plusThread();
                        openCoinPricesWindow();
                        Main.minusThread();
                    }
                });
            }
        });

        tradeHelpBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        Main.plusThread();
                        openCoinPricesWindow();
                        if(thw==null || !thw.isShowing()) {
                            Main.workers.submit(thw = new TradeHelperWindow("Trade Helper", (dim.width/2), (dim.height/2), bc));
//                            new Thread(thw = new TradeHelperWindow("Trade Helper", (dim.width/2), (dim.height/2), bc)).start();
                        }else thw.requestFocus();
                        Main.minusThread();
                    }
                });
            }
        });




        progress[0]=110;
        windowsStates[windowNumber[0]] = true;
        repaint();
//        System.out.println(bc.getAccountBalance());


    }

    private void openCoinPricesWindow() {
        if(cpw==null || !cpw.isShowing()) {
            Main.workers.submit(cpw = new CoinPricesWindow("Binance Coin Prices", (dim.width/2), (dim.height/2), bc));
//            new Thread(cpw = new CoinPricesWindow("Binance Coin Prices", 0, 0, bc)).start();
        }else cpw.requestFocus();
    }

    @Override
    public void run() {
        Main.plusThread();
        while (windowsStates[windowNumber[0]]) {
            try {
//                for (String s : bc.getLastPrices().keySet()) System.out.println(bc.getLastPrices().get(s));
                Thread.sleep(1500);
                checkState(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.err.println(TAG+" > Main Thread Stopped");
        threadStateToDefault(this);
        if(cpw!=null) cpw.dispose();
        if(thw!=null) thw.dispose();
        bc.threadState=false;
        Main.minusThread();
    }
}
