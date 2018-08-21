package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.customObjects.CustTextPane;
import mm.customObjects.SideFrame;
import mm.startGui.LoadingWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class TradeHelperWindow extends SideFrame implements Runnable{
    private String TAG = "Binance/TradeHelper";
    private BinanceController bc;
    private Thread guiThread;
    private JComboBox<String> coinTypeCombo;
    private JTextArea buyPrice;
    private JTextArea amount;
    private JTextArea fee;
    private CustButton addBut;
    private JPanel middlePanel;
    private List<HashMap<String, Object>> listWithAddeditems;
    private JPanel items;
    private int numperOfItems=-1;
    private TradeHelperLogic thl;
    private CustButton removeAll;
    private int numperOfItemListeners=-1;
    private int ipos;
    private CustTextPane totalProfitPane;
    private JPanel middlePanelHintsPanel;
    private CustTextPane totalAmountNowPane;
    private CustTextPane totalAmountBoughtPane;

    public static final String PAIRKEY="pairfield";
    public static final String AMOUNTKEY="amountfield";
    public static final String PRICEKEY="curPricefield";
    public static final String PROFITKEY="profitpane";
    public static final String REMOVEBUTKEY="removebutton";
    public static final String FEEKEY="fee";

    public TradeHelperWindow(String title, int lx, int ly,BinanceController bc) {
        super(title, lx-320, ly-240, 640, 520);
        windowNumber[0]=2;
        this.bc=bc;
        new Thread(new LoadingWindow<>(this)).start();
        thl = new TradeHelperLogic(bc);

        listWithAddeditems = new LinkedList<HashMap<String, Object>>();

//        thl.makeTable();
//        thl.dropTheTable();
        makeGui();
        try {
            progress[0]=90;
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addListenersToObjects();
        setVisible(true);
        progress[0]=110;
        windowsStates[windowNumber[0]]=true;
        new Thread(thl).start();
        repaint();
        progress[0]=110;
    }

    private void makeGui(){
        setBackground(Colors.white);
        makeTopPanel();
        makeMiddlePanel();
        putMiddlePanlelInScrollView();
        addItemsFromDBtoMiddlePanel();
        makeBottomPanel();
        repaint();
    }

    private void makeBottomPanel() {
        removeAll = new  CustButton("Remove All",0,0,640,40);
        removeAll.setBorder(BorderFactory.createEmptyBorder());
        removeAll.setBounds(560,440,80,40);
        removeAll.setForeground(Colors.red);
        add(removeAll);
        removeAll.setVisible(true);

        JTextArea totalProfitHintArea = new JTextArea();
        totalProfitHintArea.setText("Total profit");
        totalProfitHintArea.setBorder(BorderFactory.createEmptyBorder());
        totalProfitHintArea.setEditable(false); totalProfitHintArea.setVisible(true);

        totalProfitPane = new CustTextPane("0",1,"LEFT" );
        totalProfitPane.setPaneColor(Colors.green,"green");

        JTextArea totalAmountNowHintArea = new JTextArea();
        totalAmountNowHintArea.setText("Total Amount Now");
        totalAmountNowHintArea.setBorder(BorderFactory.createEmptyBorder());
        totalAmountNowHintArea.setEditable(false); totalAmountNowHintArea.setVisible(true);

        totalAmountNowPane = new CustTextPane("0", 1,"LEFT");

        JTextArea totalAmountBoughtHintArea = new JTextArea();
        totalAmountBoughtHintArea.setText("Total Bought");
        totalAmountBoughtHintArea.setBorder(BorderFactory.createEmptyBorder());
        totalAmountBoughtHintArea.setEditable(false); totalAmountBoughtHintArea.setVisible(true);

        totalAmountBoughtPane = new CustTextPane("0", 1,"LEFT");


        JPanel bottomPanel = new JPanel();
//        bottomPanel.add(removeAll);
        bottomPanel.add(totalProfitHintArea);
        bottomPanel.add(totalProfitPane);
        bottomPanel.add(totalAmountNowHintArea);
        bottomPanel.add(totalAmountNowPane);
        bottomPanel.add(totalAmountBoughtHintArea);
        bottomPanel.add(totalAmountBoughtPane);
        bottomPanel.setBounds(0,440,560,40);
        bottomPanel.setBackground(Colors.white);

        add(bottomPanel);
    }

    private void addListenersToObjects(){
        addBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                System.out.println(actionEvent.getActionCommand());
                int textBox = 0;
                amount.setBackground(Colors.lightBlue);
                buyPrice.setBackground(Colors.lightBlue);
                fee.setBackground(Colors.lightBlue);
                try{
                    Double.parseDouble(amount.getText());
                    textBox = 1;
                    Double.parseDouble(buyPrice.getText());
                    textBox = 2;
                    Double.parseDouble(fee.getText());

                    addItemsToList(coinTypeCombo.getSelectedItem().toString(), amount.getText(), buyPrice.getText(), fee.getText());

                    addItemsFromlistToDB();
                    addItemsFromDBtoMiddlePanel();
                    System.err.println(listWithAddeditems.size());
                    amount.setText("");
                    buyPrice.setText("");
                    addListenersToMiddlePanelItems();

                }catch (Exception e){
                    if(textBox==0)amount.setBackground(Colors.redLite);
                    else if(textBox==1)buyPrice.setBackground(Colors.redLite);
                    else if(textBox==2)fee.setBackground(Colors.redLite);
                }
//                thl.putDataInTable(coinTypeCombo.getSelectedItem().toString(), amount.getText(), buyPrice.getText(),fee.getText());
//                addItemsToMiddlePanel();
                repaint();
            }
        });
        addListenersToMiddlePanelItems();
    }

    private void addListenersToMiddlePanelItems() {
        if(listWithAddeditems !=null) {
            System.out.println(listWithAddeditems.size());
            for (ipos = 0; ipos < listWithAddeditems.size(); ipos++) {
                ActionListener al = null;
                try {
                   al = ((JButton)listWithAddeditems.get(0).get(REMOVEBUTKEY)).getActionListeners()[0];
                }catch (Exception e){
                    System.err.println(TAG+" "+e);
                }


                JButton bt = (JButton)listWithAddeditems.get(ipos).get(REMOVEBUTKEY);

                if(al != null) {
                    bt.removeActionListener(al);
                }

                bt.addActionListener(new ActionListener() {
                    int i =ipos;
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(listWithAddeditems.size()>0) {
                            listWithAddeditems.remove(listWithAddeditems.get(i));
                        }

                        numperOfItems=-1;
                        numperOfItemListeners=-1;
                        middlePanel.removeAll();

                        addItemsFromlistToDB();
                        addItemsFromDBtoMiddlePanel();
                        amount.setText("");
                        buyPrice.setText("");
                        addListenersToMiddlePanelItems();
                        repaint();
                    }
                });
            }
        }else System.err.println(TAG+"  listWithAddeditems is Empty!");
    }

    private void addItemsFromlistToDB(){
        thl.dropTheTable();
        thl.makeTable();
        for(HashMap<String, Object> l : listWithAddeditems){
            thl.putDataInTable(((JTextField)(l.get(PAIRKEY))).getText(),((JTextField)(l.get(AMOUNTKEY))).getText(),((JTextField)(l.get(PRICEKEY))).getText(),(String) l.get(FEEKEY));
        }
    }

    private void makeMiddlePanel() {
        middlePanelHintsPanel = new JPanel();
        middlePanelHintsPanel.setBackground(Colors.white);
//        middlePanelHintsPanel.setLayout(new BoxLayout(middlePanelHintsPanel, BoxLayout.X_AXIS));
        middlePanelHintsPanel.setVisible(true);
        middlePanelHintsPanel.setBounds(0,40,640,40);
        addHintsToMiddlePanelHintPanel();
        add(middlePanelHintsPanel);


        middlePanel = new JPanel();
        middlePanel.setVisible(true);
        middlePanel.setBackground(Colors.lightBlue);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
    }

    private void addHintsToMiddlePanelHintPanel() {
        int colNum = 10;
        JTextField pairHint = new JTextField();
        pairHint.setEditable(false);
        pairHint.setBackground(Colors.white);
        pairHint.setForeground(Colors.blue);
        pairHint.setText("Pairs");
        pairHint.setColumns(colNum);
        pairHint.setBorder(BorderFactory.createEmptyBorder());

        JTextField amountHint = new JTextField();
        amountHint.setEditable(false);
        amountHint.setBackground(Colors.white);
        amountHint.setForeground(Colors.blue);
        amountHint.setText("Amount");
        amountHint.setColumns(colNum);
        amountHint.setBorder(BorderFactory.createEmptyBorder());

        JTextField priceOfOneHint = new JTextField();
        priceOfOneHint.setEditable(false);
        priceOfOneHint.setBackground(Colors.white);
        priceOfOneHint.setForeground(Colors.blue);
        priceOfOneHint.setText("Price bought/$");
        priceOfOneHint.setColumns(colNum);
        priceOfOneHint.setBorder(BorderFactory.createEmptyBorder());

        JTextField profitHint = new JTextField();
        profitHint.setEditable(false);
        profitHint.setBackground(Colors.white);
        profitHint.setForeground(Colors.blue);
        profitHint.setText("Profit - Loss");
        profitHint.setColumns(13);
        profitHint.setBorder(BorderFactory.createEmptyBorder());

        JTextField removeHint = new JTextField();
        removeHint.setEditable(false);
        removeHint.setBackground(Colors.white);
        removeHint.setForeground(Colors.blue);
        removeHint.setText("          ");
        removeHint.setColumns(1);
        removeHint.setBorder(BorderFactory.createEmptyBorder());

        middlePanelHintsPanel.add(pairHint);
        middlePanelHintsPanel.add(amountHint);
        middlePanelHintsPanel.add(priceOfOneHint);
        middlePanelHintsPanel.add(profitHint);
        middlePanelHintsPanel.add(removeHint);
    }

    private void putMiddlePanlelInScrollView(){
        sp = new JScrollPane(middlePanel);
        sp.setBackground(Colors.white);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setBounds(0,80,640,360);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        sp.setBackground(Colors.lightBlue);
        add(sp);
    }

    private void addItemsFromDBtoMiddlePanel(){
        listWithAddeditems = new LinkedList<HashMap<String, Object>>();
        List<List<String>> dbitems = thl.getTableContent();
        for(List<String> l:dbitems){
            addItemsToList(l.get(0),l.get(1),l.get(2), l.get(3));
        }
        addItemsToMiddlePanel();
    }

    private void addItemsToMiddlePanel(){
        if(listWithAddeditems !=null){
//            System.out.println(listWithAddeditems.size());
            for (int i =0; i<listWithAddeditems.size(); i++){
                if(i>numperOfItems) {
                    JPanel items = new JPanel();
                    items.setBounds(0, 0, 640, 40);
                    items.setBackground(Colors.white);
//                    FlowLayout fl = new FlowLayout();
//                    fl.setHgap(5);
//                    fl.setAlignment(FlowLayout.LEFT);
//                    items.setLayout(fl);
//                    ((JTextField)listWithAddeditems.get(i).get("pairfield")).setColumns(10);
                    items.add((JTextField)listWithAddeditems.get(i).get(PAIRKEY));
//                System.out.println(l.get(0).getText());
//                    ((JTextField)listWithAddeditems.get(i).get("amountfield")).setColumns(10);
                    items.add((JTextField)listWithAddeditems.get(i).get(AMOUNTKEY));
//                    ((JTextField)listWithAddeditems.get(i).get("curPricefield")).setColumns(10);
                    items.add((JTextField)listWithAddeditems.get(i).get(PRICEKEY));
//                    ((CustTextPane)listWithAddeditems.get(i).get("profitpane")).setMaximumSize(panedim);
                    items.add((CustTextPane)listWithAddeditems.get(i).get(PROFITKEY));
//                    ((JButton)listWithAddeditems.get(i).get("removebutton"))
                    items.add((JButton)listWithAddeditems.get(i).get(REMOVEBUTKEY), BorderLayout.LINE_END);
//                    items.setLayout(null);
                    middlePanel.add(items);
                    numperOfItems++;
                }

            }
        }

    }

    private void addItemsToList(String pairs, String camount, String oprice, String tfee){
//        listWithAddeditems = new LinkedList<List<Object>>();
        Map<String,Object> map = new HashMap<String, Object>();

        JTextField pair = new JTextField();
        pair.setEditable(false);
        pair.setBackground(Colors.white);
        pair.setText(pairs);
        pair.setColumns(10);
        pair.setBorder(BorderFactory.createEmptyBorder());

//        CustTextPane pair = new CustTextPane(pairs,2,"CENTER");
        map.put(PAIRKEY,pair);

        JTextField amount = new JTextField();
        amount.setEditable(false);
        amount.setBackground(Colors.white);
        amount.setText(camount);
        amount.setColumns(10);
        amount.setBorder(BorderFactory.createEmptyBorder());
//        CustTextPane amount = new CustTextPane(camount,2,"CENTER");

        map.put(AMOUNTKEY,amount);

        JTextField curPrice = new JTextField();
        curPrice.setEditable(false);
        curPrice.setBackground(Colors.white);
        curPrice.setText(oprice);
        curPrice.setColumns(10);
        curPrice.setBorder(BorderFactory.createEmptyBorder());

//        CustTextPane curPrice = new CustTextPane(oprice,2,"CENTER");

        map.put(PRICEKEY,curPrice);

//        JTextField profit = new JTextField();
//        profit.setEditable(false);
//        profit.setBackground(Colors.white);
//        profit.setText("Calculating");
//        profit.setColumns(10);

        CustTextPane profit = new CustTextPane("0",1,"LEFT");

        map.put(PROFITKEY,profit);

        JButton remove = new JButton();
//        remove.setEditable(false);
        remove.setForeground(Colors.red);
        remove.setBackground(Colors.white);
        remove.setText("Remove");
//        remove.setColumns(10);

        remove.setBorder(BorderFactory.createEmptyBorder());

//        CustTextPane remove = new CustTextPane("Remove",2,"CENTER");
        map.put(REMOVEBUTKEY,remove);
        map.put(FEEKEY,new String(tfee));
        listWithAddeditems.add((HashMap<String, Object>) map);
    }

    private void makeTopPanel() {
        JPanel userInputPanel = new JPanel();
        userInputPanel.setBounds(0,0,640,40);
        userInputPanel.setBackground(Colors.white);
        coinTypeCombo = new JComboBox<String>();
        coinTypeCombo.setBackground(Colors.white);
        coinTypeCombo.setBorder(BorderFactory.createEmptyBorder());
        putCoinsInCombo();

        JTextField amountHint = new JTextField();
        amountHint.setEditable(false);
        amountHint.setBackground(Colors.white);
        amountHint.setText("Amount");
        amountHint.setBorder(BorderFactory.createEmptyBorder());


        amount = new JTextArea();
        amount.setEditable(true);
        amount.setBackground(Colors.lightBlue);
        amount.setColumns(10);

        JTextField priceHint = new JTextField();
        priceHint.setEditable(false);
        priceHint.setBackground(Colors.white);
        priceHint.setText("Price $");
        priceHint.setBorder(BorderFactory.createEmptyBorder());

        buyPrice = new JTextArea();
        buyPrice.setEditable(true);
        buyPrice.setBackground(Colors.lightBlue);
        buyPrice.setColumns(10);

        JTextField feeHint = new JTextField();
        feeHint.setEditable(false);
        feeHint.setBackground(Colors.white);
        feeHint.setText("Fee%");
        feeHint.setBorder(BorderFactory.createEmptyBorder());

        fee = new JTextArea();
        fee.setEditable(true);
        fee.setBackground(Colors.lightBlue);
        fee.setColumns(10);

        addBut = new CustButton("ADD",0,0,40,40);
        addBut.setBorder(BorderFactory.createEmptyBorder());


        userInputPanel.add(coinTypeCombo);
        userInputPanel.add(amountHint);
        userInputPanel.add(amount);
        userInputPanel.add(priceHint);
        userInputPanel.add(buyPrice);
        userInputPanel.add(feeHint);
        userInputPanel.add(fee);
        userInputPanel.add(addBut);

        add(userInputPanel);
    }

    private void putCoinsInCombo(){
        for (String s:bc.getLastPrices().keySet()) {
            coinTypeCombo.addItem(s);
        }
    }

    private void updateValues(){
        thl.updateprofits(listWithAddeditems);
        thl.updateTotalProfit(listWithAddeditems, totalProfitPane);
        thl.updateTotalAmountNow(listWithAddeditems, totalAmountNowPane);
    }



    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            thl.updateTotalAmountBought(listWithAddeditems, totalAmountBoughtPane);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (windowsStates[windowNumber[0]]) {
            try {
                updateValues();
                checkState(this);
                repaint();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println(TAG+" > Thread Stopped");
        threadStateToDefault(this);
        thl.stopTheThread();

    }
}
