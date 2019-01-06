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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class CoinCapWindow extends CustFrame implements Runnable {
    private CustButton addCoinsBut;
    private CustButton refreshBut;
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

    private boolean stopScrolling = false;

    public CoinCapWindow(String title, int x, int y, int Sx, DatabaseController database) {
        super(title, x, y, Sx,60);
        windowNumber[0]=0;

        addCoinsBut = new CustButton("Add",sx-70,yGap,70,30);
//        refreshBut = new CustButton("R",sx-70,yGap,70,30);

        add(addCoinsBut);
//        add(refreshBut);


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
        setBackground(Colors.white);
        setVisible(true);
        repaint();

        System.out.println("CoinCapWindow Loaded!");
//        progress[0]=50;

        addListeners();
        scrollThroughCoins();
    }

    private void scrollThroughCoins() {
        Main.workers.submit(new Runnable() {
            @Override
            public void run() {
                Main.plusThread();
                while (!stopScrolling) {
//                    int max = sp.getHorizontalScrollBar().getMaximum();
//                    System.out.println(max);
                    int max = 800;
                    int delay = 30;
                    for(int i=0; i<max; i++) {
                        try {
//                            System.out.println(i);
                            if(!stopScrolling) {
                                sp.getHorizontalScrollBar().setValue(i);
                                Thread.sleep(delay);
                            }else break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    for(int j=max; j>0; j--) {
                        try {
                            if(!stopScrolling) {
                                sp.getHorizontalScrollBar().setValue(j);
                                if (j == 1) Thread.sleep(2000);
                                else Thread.sleep(delay);
                            }else break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Main.minusThread();
            }
        });
    }

    private void addListeners(){
        addCoinsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.workers.submit(new AddCoinWindow(dim.width/2, dim.height/2,me, cc));
            }
        });

//        refreshBut.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                updateCoins();
//                repaint();
//                System.out.println("Refreshed!");
//            }
//        });

        sp.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                stopScrolling = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                stopScrolling = false;
                scrollThroughCoins();

            }
        });
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
        }
//        System.out.println("Updated");

    }

    private java.util.List<String> sortPairs(Set<String> keySet) {
        List<String> sl = new LinkedList<String>(keySet);
        Collections.sort(sl);
        return sl;
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
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        market = cc.getMarkets();

        for (String coin : sortPairs(market.keySet())) {
            JPanel jp = new JPanel();
            jp.setBackground(Colors.white);
//            jp.setLayout(new FlowLayout(FlowLayout.LEFT, 40,0));
//            jp.setLayout(null);
            JTextField cText = new JTextField();
//            cText.setBounds(0,0,120,40);
            cText.setText(coin);
            cText.setEditable(false);
            cText.setBackground(Colors.white);
            cText.setForeground(Colors.blue);
            cText.setBorder(BorderFactory.createEmptyBorder());
            jp.add(cText);

            try {
                CustTextPane pText = new CustTextPane(market.get(coin).getPriceUSD() + "", 1, "LEFT");
                pText.withDollarSign = true;
//                pText.setBounds(120, 0, 120, 40);
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
        sp.setBounds(0,yGap,sx-70,sy);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp);
        repaint();
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.err.println(TAG+" > Main Thread Stopped");
        threadStateToDefault(this);
        cc.state=false;
        stopScrolling = true;
//        this.dispose();
        Main.minusThread();
    }
}
