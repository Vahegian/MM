/*
Creates a window that contains textPanes containing coin information from coinMarketCap API with
custom class CustTextPane.
 */

package mm.coinCap.api.gui;

import mm.Main;
import mm.coinCap.api.CoinCollector;
import com.lucadev.coinmarketcap.model.CoinMarket;
import mm.database.DatabaseController;
import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.customObjects.CustTextPane;
import mm.customObjects.CustFrame;
import mm.startGui.LoadingWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CoinCapWindow extends CustFrame implements Runnable {
    private String TAG = "CoinCapWindows";
    private CoinCollector cc;
    private CoinCapWindow me = this;

    private Map<String, CoinMarket> market;
    private Map<String, CustTextPane> coinNpaneMap;
    private Map<String , Double> prevPricesMap;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    private JPanel mainPanel;

//    private boolean loaded = false;
    private boolean showNetworkDialgue = false;

    public CoinCapWindow(String title, int x, int y, DatabaseController database) {
        super(title, x, y, 240,720);
        windowNumber[0]=0;



        CustButton addCoinsBut = new CustButton("Add Coins",0,yGap,240,30);
        CustButton refreshBut = new CustButton("Refresh",0,yGap+35,240,30);



        addButtonAction(addCoinsBut);
        addButtonAction(refreshBut);
        add(addCoinsBut);
        add(refreshBut);


        Main.workers.submit(new LoadingWindow<>(me));
        Main.workers.submit(new Runnable() {
            @Override
            public void run() {
                Main.plusThread();
                while (progress[0]<100) {
                    try {
                        synchronized (progress) {
                            progress[0] = CoinCollector.progress[0];
                        }
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                progress[0]=100;
                System.err.println(TAG+"> progress update Thread stopped");
                Main.minusThread();
            }
        });

        this.cc = new CoinCollector(database);

        putCoins();

        windowsStates[windowNumber[0]]=true;
        setVisible(true);
        repaint();

        System.out.println("CoinCapWindow Loaded!");
//        progress[0]=50;

    }

    private void addButtonAction(JButton bt){
        if(bt.getText().equals("Add Coins")) {
            bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Main.workers.submit(new AddCoinWindow(dim.width/2, dim.height/2,me, cc));
                }
            });
        }else if(bt.getText().equals("Refresh")){
            bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    updateCoins();
                    repaint();
                    System.out.println("Refreshed!");
                }
            });
        }
    }

    void addNewCoin(){
        remove(sp);
        System.out.println("refrashed");
        try {
            Thread.sleep(3000);
            putCoins();
//                    updateCoins();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    Updates the coins and sets colors to triangles drown in custTextPane
    gets updated map of the coins from "CoinCollector"
     */
    private void updateCoins(){

        market = cc.getMarkets();
        for (String coin : coinNpaneMap.keySet()){
            double price = market.get(coin).getPriceUSD();
//            System.out.print(price+", ");
            CustTextPane ctp = coinNpaneMap.get(coin);

            if(prevPricesMap.get(coin) < price){
                prevPricesMap.replace(coin, price);
                ctp.setPaneColor(Colors.green, "green");
            }else if(prevPricesMap.get(coin) > price){
                prevPricesMap.replace(coin, price);
                ctp.setPaneColor(Colors.red, "red");
            }
            else {
                prevPricesMap.replace(coin, price);
                ctp.setPaneColor(Colors.black, "black");
            }
            ctp.setPaneText(price+"");
        }
//        System.out.println("Updated");

    }

    /*
    gets coin map from "CoinCollector"
    puts coin name and current price in different "JTextPane"s
    puts pane's into jpanel which is placed in JScrollPane
     */
    private void putCoins(){

        prevPricesMap = new HashMap<String, Double>();
        coinNpaneMap = new HashMap<String, CustTextPane>();

        mainPanel = new JPanel();
        mainPanel.setVisible(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        market = cc.getMarkets();

        for (String coin : market.keySet()) {
            JPanel jp = new JPanel();
            jp.setBackground(Colors.white);
//            jp.setLayout(new FlowLayout(FlowLayout.LEFT, 40,0));
            jp.setLayout(null);
            CustTextPane cText = new CustTextPane(coin,2, "LEFT");
            cText.setBounds(0,0,120,40);
            cText.setPaneColor(Colors.blue, "blue");
            jp.add(cText);

            try {
                CustTextPane pText = new CustTextPane(market.get(coin).getPriceUSD() + "", 1, "LEFT");
                pText.withDollarSign = true;
                pText.setBounds(120, 0, 120, 40);
                jp.add(pText);
                coinNpaneMap.put(coin, pText);
//                synchronized (progress){
//                    progress[0]+=100/market.keySet().size();
//                }
            }catch (Exception e){
                System.err.println("CoinCapWindow: putCoins pText=> "+e);
                showNetworkDialgue = true;
            }

            mainPanel.add(jp);
            prevPricesMap.put(coin,0.0);

        }

        if(showNetworkDialgue){
            this.dispose();
            JOptionPane info = new JOptionPane();
            info.setMessage("No Connection");
            info.createDialog("INFO").show();

        }

        sp = new JScrollPane(mainPanel);
        sp.setBounds(0,yGap+70,240,620);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(sp);

    }

    /*
    on seperate thread updates the coins;
     */
    @Override
    public void run() {
        Main.plusThread();
        Main.workers.submit(cc);
        while (windowsStates[windowNumber[0]]) {
            try {


//                synchronized (market) {
                updateCoins();
//                }
                repaint();
//            System.out.println("btc");
                checkState(this);
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.err.println(TAG+" > Main Thread Stopped");
        threadStateToDefault(this);
        cc.state=false;
//        this.dispose();
        Main.minusThread();
    }
}
