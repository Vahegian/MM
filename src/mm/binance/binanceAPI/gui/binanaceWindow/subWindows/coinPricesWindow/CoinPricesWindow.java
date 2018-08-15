package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.coinPricesWindow;

import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.customObjects.CustTextPane;
import mm.customObjects.SideFrame;
import mm.startGui.LoadingWindow;
//import mm.binance.binanceAPI.gui.coinCapWindows.AddCoinWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CoinPricesWindow extends SideFrame implements Runnable{
    private String TAG = "BinanceWindow\\subWindows\\CoinPricesWindow";
    private CoinPricesWindow me = this;

    private BinanceController bc;
    private Map<String, Double> market;
    private HashMap<String, Double> prevPricesMap;
    private HashMap<String, CustTextPane> coinNpaneMap;
    private JPanel mainPanel;
    private boolean showNetworkDialgue;

//    private boolean isloaded = false;

    public CoinPricesWindow(String title, int lx, int ly, BinanceController bc) {
        super(title, lx, ly, 240, 720);
        windowNumber[0] = 1;
        new Thread(new LoadingWindow<>(this)).start();
        this.bc = bc;

        CustButton addCoinsBut = new CustButton("Add Coins",0,0,240,30);
        CustButton refreshBut = new CustButton("Refresh",0,35,240,30);

        putCoins();
        updateCoins();

        addButtonAction(addCoinsBut);
        addButtonAction(refreshBut);
        add(addCoinsBut);
        add(refreshBut);

        updateSPView();

        System.out.println("CoinPricesWindow Loaded!");
        bc.isloaded = true;

        setVisible(true);
        progress[0]=110;
        windowsStates[windowNumber[0]] = true;


    }

    private void addButtonAction(JButton bt){
        if(bt.getText().equals("Add Coins")) {
            bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    new Thread(new AddCoinWindow(me, bc)).start();
                }
            });
        }else if(bt.getText().equals("Refresh")){
            bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println(actionEvent.paramString());
                    updateCoins();
                    repaint();
                    System.out.println("Refreshed!");
                }
            });
        }
    }

    void updateSPView(){
        remove(sp);
        System.out.println("refreshed");
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

        market = bc.getLastPrices();
        for (String coin : coinNpaneMap.keySet()){
            double price = market.get(coin);
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
            if(progress[0]<100)
                progress[0]+=50/coinNpaneMap.size();
        }
//        System.out.println("Updated");

    }

    /*
    gets coin map from "BinanceController"
    puts coin name and current price in different "JTextPane"s
    puts pane's into jpanel which is placed in JScrollPane
     */
    private void putCoins(){

        prevPricesMap = new HashMap<String, Double>();
        coinNpaneMap = new HashMap<String, CustTextPane>();

        mainPanel = new JPanel();
        mainPanel.setVisible(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        market = bc.getLastPrices();

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
                CustTextPane pText = new CustTextPane(market.get(coin) + "", 1, "LEFT");
                pText.setBounds(120, 0, 120, 40);
                jp.add(pText);
                coinNpaneMap.put(coin, pText);
                if(progress[0]<100)
                    progress[0]+=50/market.keySet().size();
            }catch (Exception e){
                System.err.println("CoinCapWindow: putCoin- pText=> "+e);
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
        sp.setBounds(0,70,240,620);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(sp);

        System.err.println(market.keySet().size());


    }

    /*
    on seperate thread updates the coins;
     */
    @Override
    public void run() {
//        boolean run = true;
        while (windowsStates[windowNumber[0]]) {
            if(bc.isloaded) {
                try {
                    updateCoins();
                    repaint();
                    checkState(this);
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.err.println(TAG+" > Thread Stopped");
        threadStateToDefault(this);

    }
}
