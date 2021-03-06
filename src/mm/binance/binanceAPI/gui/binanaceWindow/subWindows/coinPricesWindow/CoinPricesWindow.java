package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.coinPricesWindow;

import javafx.print.Collation;
import mm.Main;
import mm.binance.binanceAPI.BinanceController;
import mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper.ComparePairItems;
import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.customObjects.CustTextPane;
import mm.customObjects.CustFrame;
import mm.startGui.LoadingWindow;
//import mm.binance.binanceAPI.gui.coinCapWindows.AddCoinWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CoinPricesWindow extends CustFrame implements Runnable{
    private String TAG = "BinanceWindow\\subWindows\\CoinPricesWindow";
    private CoinPricesWindow me = this;

    private BinanceController bc;
    private Map<String, Double> market;
    private HashMap<String, Double> prevPricesMap;
    private HashMap<String, CustTextPane> coinNpaneMap;
    private JPanel mainPanel;
    private boolean showNetworkDialgue;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

//    private boolean isloaded = false;

    public CoinPricesWindow(String title, int lx, int ly, BinanceController bc) {
        super(title, lx-620, ly-420, 240, 720);
        windowNumber[0] = 1;
        Main.workers.submit(new LoadingWindow<>(this));
//        new Thread(new LoadingWindow<>(this)).start();
        this.bc = bc;

        CustButton addCoinsBut = new CustButton("Add Coins",0,yGap,240,30);
        CustButton refreshBut = new CustButton("Refresh",0,yGap+35,240,30);

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
                    Main.workers.submit(new AddCoinWindow(dim.width/2, dim.height/2, me, bc));
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
                ctp.setFieldColor(Colors.green, "green");
            }else if(prevPricesMap.get(coin) > price){
                prevPricesMap.replace(coin, price);
                ctp.setFieldColor(Colors.red, "red");
            }
            else {
                prevPricesMap.replace(coin, price);
                ctp.setFieldColor(Colors.black, "black");
            }
            ctp.setFieldText(price+"");
            if(progress[0]<100)
                progress[0]+=50/coinNpaneMap.size();
        }
//        System.out.println("Updated");

    }

    private List<String> sortPairs(Set<String> keySet) {
        List<String> sl = new LinkedList<String>(keySet);
        Collections.sort(sl);
        return sl;
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
        try {
            for (String coin : sortPairs(market.keySet())) {
                JPanel jp = new JPanel();
                jp.setBackground(Colors.white);
//            jp.setLayout(new FlowLayout(FlowLayout.LEFT, 40,0));
//                jp.setLayout(null);
                CustTextPane cText = new CustTextPane(coin, 2, "LEFT");
//                cText.setBounds(0, 0, 120, 40);
                cText.setFieldColor(Colors.blue, "blue");
                jp.add(cText);

                try {
                    CustTextPane pText = new CustTextPane(market.get(coin) + "", 1, "LEFT");
                    pText.withDollarSign = true;
//                    pText.setBounds(120, 0, 120, 40);
                    pText.copyOnClick();
                    jp.add(pText);
                    coinNpaneMap.put(coin, pText);
                    if (progress[0] < 100)
                        progress[0] += 50 / market.keySet().size();
                } catch (Exception e) {
                    System.err.println("CoinCapWindow: putCoin- pText=> " + e);
                    showNetworkDialgue = true;
                }

                mainPanel.add(jp);
                prevPricesMap.put(coin, 0.0);
            }
        }catch (Exception e){
            System.err.println(TAG+"/ putCoins > "+e);
            progress[0]=110;
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

        System.err.println(market.keySet().size());


    }

    /*
    on seperate thread updates the coins;
     */
    @Override
    public void run() {
        Main.plusThread();
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

        Main.minusThread();
    }
}
