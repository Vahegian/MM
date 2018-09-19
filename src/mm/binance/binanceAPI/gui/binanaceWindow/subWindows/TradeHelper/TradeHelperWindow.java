package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.Main;
import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.customObjects.CustTextPane;
import mm.customObjects.CustFrame;
import mm.startGui.LoadingWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class TradeHelperWindow extends CustFrame implements Runnable{
    private String TAG = "Binance/TradeHelperWindow";
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

    static final String PAIRKEY="pairfield";
    static final String AMOUNTKEY="amountfield";
    static final String PRICEKEY="curPricefield";
    static final String PROFITKEY="profitpane";
    static final String REMOVEBUTKEY="removebutton";
    static final String FEEKEY="fee";
    static final String HOWMANYPANEKEY = "howmanymore";
    static final String HOWMUCHISTOTALPANEKEY = "rowtotal";
    private CustTextPane totalProfitPaneGBP;
    private CustTextPane totalAmountNowPaneGBP;
    private CustTextPane totalAmountBoughtGBP;

    public TradeHelperWindow(String title, int lx, int ly,BinanceController bc) {
        super(title, lx-380, ly-300, 760, 600);
        windowNumber[0]=2;
        this.bc=bc;
        Main.workers.submit(new LoadingWindow<>(this));
        thl = new TradeHelperLogic(bc);

        listWithAddeditems = new LinkedList<HashMap<String, Object>>();

//        thl.makeTable();
//        thl.dropTheTable();
        try {
            makeGui();
        }catch (Exception e){
            System.err.println(TAG+"/constructor > "+e);
            progress[0]=110;
        }
        try {
            progress[0]=90;
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addListenersToObjects();
        setVisible(true);
//        progress[0]=110;
        windowsStates[windowNumber[0]]=true;
        Main.workers.submit(thl);
        repaint();
        progress[0]=110;
    }

    private void makeGui(){
        setBackground(Colors.white);
        makeTopPanel();
        makeMiddlePanel();
        System.err.println("halfway");
        addItemsFromDBtoMiddlePanel();
        addHintsToMiddlePanelHintPanel();
        putMiddlePanlelInScrollView();
        makeBottomPanelOne();
        makeBottomPanelTwo();
        makeLastBottomPanel();
        repaint();
    }

    private void makeBottomPanelTwo() {
        JPanel b2Panel = new JPanel();
        b2Panel.setBackground(Colors.white);
        b2Panel.setBounds(0,yGap+sy-100,sx,40);

        add(b2Panel);
    }

    private void makeLastBottomPanel() {
        removeAll = new  CustButton("Remove All",0,0,sx,40);
        removeAll.setBorder(BorderFactory.createEmptyBorder());
        removeAll.setBounds(0,0,sx,40);
        removeAll.setForeground(Colors.red);
        removeAll.setVisible(true);

        JPanel blPanel = new JPanel();
        blPanel.setBackground(Colors.white);
        blPanel.setBounds(0,yGap+sy-60,sx,40);
        blPanel.add(removeAll);

        add(blPanel);
    }

    private void makeBottomPanelOne() {
        JTextArea totalProfitHintArea = new JTextArea();
        totalProfitHintArea.setText("Profit:");
        totalProfitHintArea.setBorder(BorderFactory.createEmptyBorder());
        totalProfitHintArea.setEditable(false); totalProfitHintArea.setVisible(true);

        totalProfitPane = new CustTextPane("0",1,"LEFT" );
        totalProfitPane.setPaneColor(Colors.green,"green");
        totalProfitPane.withDollarSign = true;

        totalProfitPaneGBP = new CustTextPane("0",1,"LEFT");
        totalProfitPaneGBP.setPaneColor(Colors.green,"green");
        totalProfitPaneGBP.withPoundSign = true;

        JTextArea totalAmountNowHintArea = new JTextArea();
        totalAmountNowHintArea.setText("Total Now:");
        totalAmountNowHintArea.setBorder(BorderFactory.createEmptyBorder());
        totalAmountNowHintArea.setEditable(false); totalAmountNowHintArea.setVisible(true);

        totalAmountNowPane = new CustTextPane("0", 1,"LEFT");
        totalAmountNowPane.withDollarSign =true;

        totalAmountNowPaneGBP = new CustTextPane("0",1,"LEFT");
        totalAmountNowPaneGBP.withPoundSign = true;


        JTextArea totalAmountBoughtHintArea = new JTextArea();
        totalAmountBoughtHintArea.setText("Total Bought");
        totalAmountBoughtHintArea.setBorder(BorderFactory.createEmptyBorder());
        totalAmountBoughtHintArea.setEditable(false); totalAmountBoughtHintArea.setVisible(true);

        totalAmountBoughtPane = new CustTextPane("0", 1,"LEFT");
        totalAmountBoughtPane.withDollarSign = true;
        totalAmountBoughtGBP = new CustTextPane("0",1,"LEFT");
        totalAmountBoughtGBP.withPoundSign = true;


        JPanel bottomPanel = new JPanel();
//        bottomPanel.add(removeAll);
        bottomPanel.add(totalProfitHintArea);
        bottomPanel.add(totalProfitPane);
        bottomPanel.add(totalProfitPaneGBP);
        bottomPanel.add(totalAmountNowHintArea);
        bottomPanel.add(totalAmountNowPane);
        bottomPanel.add(totalAmountNowPaneGBP);
        bottomPanel.setBounds(0,yGap+sy-140,sx,40);
        bottomPanel.setBackground(Colors.white);

        JPanel bottomPanel2 = new JPanel();
        bottomPanel2.add(totalAmountBoughtHintArea);
        bottomPanel2.add(totalAmountBoughtPane);
        bottomPanel2.add(totalAmountBoughtGBP);
        bottomPanel2.setBounds(0,yGap+sy-100,sx,40);
        bottomPanel2.setBackground(Colors.white);

        add(bottomPanel);
        add(bottomPanel2);
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
                    System.err.println(TAG+"/addListeners../ "+e);
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
//        middlePanelHintsPanel.setLayout(null);
        middlePanelHintsPanel.setBounds(0,yGap+40,sx,40);


        middlePanel = new JPanel();
        middlePanel.setVisible(true);
//        middlePanel.setBackground(Colors.lightBlue);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
    }

    private void addHintsToMiddlePanelHintPanel() {
//        System.err.println(((JTextField)listWithAddeditems.get(0).get(PAIRKEY)).getColumns());
//        System.err.println(((JTextField)listWithAddeditems.get(0).get(PRICEKEY)).getColumns());
//        System.err.println(((CustTextPane)listWithAddeditems.get(0).get(PROFITKEY)).getWidth());

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
        profitHint.setColumns(colNum+2);
        profitHint.setBorder(BorderFactory.createEmptyBorder());

        JTextField howManyHint = new JTextField();
        howManyHint.setEditable(false);
        howManyHint.setBackground(Colors.white);
        howManyHint.setForeground(Colors.blue);
        howManyHint.setText("Can Buy");
        howManyHint.setColumns(colNum+2);
        howManyHint.setBorder(BorderFactory.createEmptyBorder());

        JTextField totalHint = new JTextField();
        totalHint.setEditable(false);
        totalHint.setBackground(Colors.white);
        totalHint.setForeground(Colors.blue);
        totalHint.setText("Total $");
        totalHint.setColumns(colNum+2);
        totalHint.setBorder(BorderFactory.createEmptyBorder());

        JTextField removeHint = new JTextField();
        removeHint.setEditable(false);
        removeHint.setBackground(Colors.white);
        removeHint.setForeground(Colors.blue);
        removeHint.setText("      ");
        removeHint.setColumns(colNum);
        removeHint.setBorder(BorderFactory.createEmptyBorder());

//        pairHint.setBounds(0,0,sx/7,40);
        middlePanelHintsPanel.add(pairHint);
//        amountHint.setBounds((sx/7),0,sx/7,40);
        middlePanelHintsPanel.add(amountHint);
//        priceOfOneHint.setBounds((sx/7)*2,0,sx/7,40);
        middlePanelHintsPanel.add(priceOfOneHint);
//        profitHint.setBounds((sx/7)*3,0,sx/7,40);
        middlePanelHintsPanel.add(profitHint);
//        howManyHint.setBounds((sx/7)*4,0,sx/7,40);
        middlePanelHintsPanel.add(howManyHint);
//        totalHint.setBounds((sx/7)*5,0,sx/7,40);
        middlePanelHintsPanel.add(totalHint);
//        middlePanelHintsPanel.add(removeHint);
        add(middlePanelHintsPanel);
    }

    private void putMiddlePanlelInScrollView(){
        sp = new JScrollPane(middlePanel);
        sp.setBackground(Colors.white);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setBounds(0,yGap+80,sx,380);
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
            int height = 40;
            int cols = 7;
//            System.out.println(listWithAddeditems.size());
            for (int i =0; i<listWithAddeditems.size(); i++){
                if(i>numperOfItems) {
                    JPanel items = new JPanel();
                    items.setBounds(0, 0, sx, height);
//                    items.setLayout(null);
                    items.setBackground(Colors.white);
//                    FlowLayout fl = new FlowLayout();
//                    fl.setHgap(5);
//                    fl.setAlignment(FlowLayout.LEFT);
//                    items.setLayout(fl);
//                    ((JTextField)listWithAddeditems.get(i).get("pairfield")).setColumns(10);
                    JTextField p1 = (JTextField)listWithAddeditems.get(i).get(PAIRKEY);
//                    p1.setBounds(0,0,sx/7,height);
//                    p1.setColumns(cols);
                    items.add(p1);
//                System.out.println(l.get(0).getText());
//                    ((JTextField)listWithAddeditems.get(i).get("amountfield")).setColumns(10);
                    JTextField p2 = (JTextField)listWithAddeditems.get(i).get(AMOUNTKEY);
//                    p2.setBounds((sx/7),0,sx/7,height);
//                    p2.setColumns(cols);
                    items.add(p2);
//                    ((JTextField)listWithAddeditems.get(i).get("curPricefield")).setColumns(10);
                    JTextField p3 = (JTextField)listWithAddeditems.get(i).get(PRICEKEY);
//                    p3.setBounds((sx/7)*2,0,sx/7,height);
//                    p3.setColumns(cols);
                    items.add(p3);

//                    ((CustTextPane)listWithAddeditems.get(i).get("profitpane")).setMaximumSize(panedim);
                    CustTextPane p4 = (CustTextPane)listWithAddeditems.get(i).get(PROFITKEY);
                    p4.withDollarSign = true;
//                    p4.setBounds((sx/7)*3,0,sx/7,height);
                    items.add(p4);
//                    ((JButton)listWithAddeditems.get(i).get("removebutton"))
                    CustTextPane p5 = (CustTextPane)listWithAddeditems.get(i).get(HOWMANYPANEKEY);
                    p5.withDollarSign = true;
//                    p5.setBounds((sx/7)*4,0,sx/7,height);
                    items.add(p5);

                    CustTextPane p6 = (CustTextPane)listWithAddeditems.get(i).get(HOWMUCHISTOTALPANEKEY);
                    p6.withDollarSign = true;
//                    p6.setBounds((sx/7)*5,0,sx/7,height);
                    items.add(p6);

                    JButton p7 = (JButton)listWithAddeditems.get(i).get(REMOVEBUTKEY);
//                    p7.setBounds((sx/7)*6,0,sx/7,height);
                    items.add(p7);
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

        CustTextPane howManyCanBuy = new CustTextPane("0", 1, "LEFT");
        howManyCanBuy.withDollarSign=false;
        map.put(HOWMANYPANEKEY, howManyCanBuy);

        CustTextPane howMuchTotalIs = new CustTextPane("0", 1,"LEFT");
        map.put(HOWMUCHISTOTALPANEKEY, howMuchTotalIs);

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
        userInputPanel.setBounds(0,yGap, sx,40);
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
        thl.updateHowManyCanBuy(listWithAddeditems);
        thl.updateTotalOfaRow(listWithAddeditems);
        thl.updateUSDtoGBP(totalProfitPane, totalProfitPaneGBP, totalAmountNowPane, totalAmountNowPaneGBP);
        thl.updateTotalAmountBought(listWithAddeditems, totalAmountBoughtPane, totalAmountBoughtGBP);
    }



    @Override
    public void run() {
        Main.plusThread();
        try {
            Thread.sleep(3000);
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
        Main.minusThread();
    }
}
