package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.Main;
import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.Colors;
import mm.customObjects.CustButton;
import mm.customObjects.CustTextPane;
import mm.customObjects.CustFrame;
import mm.startGui.LoadingWindow;

import javax.swing.*;
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
    private List<PairItemsPanel> listWithAddeditems;
    private JPanel items;
    private int numperOfItems=-1;
    private TradeHelperLogic thl;
    private CustButton updateBut;
    private int numperOfItemListeners=-1;
    private int ipos;
    private CustTextPane totalProfitPane;
    private JPanel middlePanelHintsPanel;
    private CustTextPane totalAmountNowPane;
    private CustTextPane totalAmountBoughtPane;

//    static final String PAIRKEY="pairfield";
//    static final String AMOUNTKEY="amountfield";
//    static final String PRICEKEY="curPricefield";
//    static final String PROFITKEY="profitpane";
//    static final String REMOVEBUTKEY="removebutton";
//    static final String FEEKEY="fee";
//    static final String HOWMANYPANEKEY = "howmanymore";
//    static final String HOWMUCHISTOTALPANEKEY = "rowtotal";
    private CustTextPane totalProfitPaneGBP;
    private CustTextPane totalAmountNowPaneGBP;
    private CustTextPane totalAmountBoughtGBP;
    private int id;
    private boolean stopUpdate = false;
    private TradeHelperWindow I = this;

    public TradeHelperWindow(String title, int lx, int ly,BinanceController bc) {
        super(title, lx-380, ly-300, 760, 600);
        windowNumber[0]=2;
        this.bc=bc;
        Main.workers.submit(new LoadingWindow<>(this));
        thl = new TradeHelperLogic(bc);

        listWithAddeditems = new LinkedList<PairItemsPanel>();

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
        addItemsFromDBtoList();
        System.err.println("1");
        addItemsToMiddlePanel();
        System.err.println("2");
        addHintsToMiddlePanelHintPanel();
        System.err.println("3");
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
        updateBut = new  CustButton("UPDATE",0,0,sx,40);
        updateBut.setBorder(BorderFactory.createEmptyBorder());
        updateBut.setBounds(0,0,sx,40);
        updateBut.setForeground(Colors.blue);
        updateBut.setVisible(true);

        JPanel blPanel = new JPanel();
        blPanel.setBackground(Colors.white);
        blPanel.setBounds(0,yGap+sy-60,sx,40);
        blPanel.add(updateBut);

        add(blPanel);
    }

    private void makeBottomPanelOne() {
        JTextArea totalProfitHintArea = new JTextArea();
        totalProfitHintArea.setText("Profit:");
        totalProfitHintArea.setBorder(BorderFactory.createEmptyBorder());
        totalProfitHintArea.setEditable(false); totalProfitHintArea.setVisible(true);

        totalProfitPane = new CustTextPane("0",1,"LEFT" );
        totalProfitPane.setFieldColor(Colors.green,"green");
        totalProfitPane.withDollarSign = true;

        totalProfitPaneGBP = new CustTextPane("0",1,"LEFT");
        totalProfitPaneGBP.setFieldColor(Colors.green,"green");
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
//        bottomPanel.add(updateBut);
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

                    addItemsToList(id, coinTypeCombo.getSelectedItem().toString(), amount.getText(), buyPrice.getText(), fee.getText());
                    id++;
                    addItemsFromlistToDB();
                    addItemsFromDBtoList();
                    addItemsToMiddlePanel();

                    System.err.println(listWithAddeditems.size());
                    amount.setText("");
                    buyPrice.setText("");
                    stopUpdate = true;
                    updateValues();
                    stopUpdate = false;
                    repaint();

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

        updateBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopUpdate = true;
                updateValues();
                stopUpdate = false;
                repaint();
            }
        });
    }


    private void addItemsFromlistToDB(){
        thl.dropTheTable();
        thl.makeTable();
        for(PairItemsPanel l : listWithAddeditems){
            thl.putDataInTable(l.p1.getText(), l.p2.getText(), l.p3.getText(),l.p4.getText());
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
        PairItemsPanel middlePanelHint = new PairItemsPanel(this,10000, sx, 40,"Pair", "Amount", "Price/$", "Fee/%", Colors.white, Colors.blue);
        String gap = "     ";
        middlePanelHint.p5.setText(gap+"Profit");
        middlePanelHint.p6.setText(gap+"New Buy");
        middlePanelHint.p7.setText(gap+"Total Now");
        middlePanelHint.p8.setText("Remove All");
        middlePanelHint.p8.removeActionListener(middlePanelHint.p8.getActionListeners()[0]);
        middlePanelHint.p8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middlePanel.removeAll();
                listWithAddeditems = new LinkedList<PairItemsPanel>();
                addItemsFromlistToDB();
                stopUpdate = true;
                updateValues();
                stopUpdate = false;
                repaint();
            }
        });
        middlePanelHint.setBounds(0,yGap+40,sx,40);
        add(middlePanelHint);
        repaint();
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
        repaint();
    }

    private void addItemsFromDBtoList(){
        listWithAddeditems = new LinkedList<PairItemsPanel>();
        List<List<String>> dbitems = thl.getTableContent();
        id = 0;
        for(List<String> l:dbitems){
            addItemsToList(id,l.get(0),l.get(1),l.get(2), l.get(3));
            id++;
        }
        sortListWithAddeditems();
        correctIDs();
    }

    private void sortListWithAddeditems() {
        listWithAddeditems.sort(new ComparePairItems());
    }

    private void correctIDs() {
        id = 0;
        for(PairItemsPanel l : listWithAddeditems){
            l.setID(id);
            id++;
        }
    }

    private void addItemsToList(int id, String pairs, String camount, String oprice, String tfee){
//        listWithAddeditems = new LinkedList<List<Object>>();
        listWithAddeditems.add(new PairItemsPanel(this, id, sx,30,pairs,camount,oprice,tfee,Colors.white, Colors.blue));
    }

    private void addItemsToMiddlePanel(){
        if(listWithAddeditems !=null){
//            System.out.println(listWithAddeditems.size());
            middlePanel.removeAll();
            for (PairItemsPanel p : listWithAddeditems) {
//                if(i>numperOfItems) {
                middlePanel.add(p);
//                    numperOfItems++;
//                }
            }
        }

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

    void removePanelFromMiddlePanel(int id) {
//        middlePanel.remove(listWithAddeditems.get(id));
        listWithAddeditems.remove(id);
        addItemsFromlistToDB();
        addItemsFromDBtoList();
        addItemsToMiddlePanel();
        amount.setText("G");
        amount.setText("");
        stopUpdate = true;
        updateValues();
        stopUpdate = false;
        repaint();
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
                if(!stopUpdate) {
                    updateValues();
                }
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
