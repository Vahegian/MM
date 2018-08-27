package mm.mainWindow;

import mm.Main;
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
    private CustButton backupBut;

    private int[] screenDim;

    private CoinCapWindow coinCap;
    private  BinanceWindow binance;
    private BackupWindow backupWindow;


    public OtherButtonsPanel(DatabaseController db, int[] dim){
//        setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
        screenDim=dim;
        setLayout(null);
        setBackground(Colors.white);
        coinCapBut = new CustButton("CoinCap",10,0,100,30);
        binanceBut = new CustButton("Binance", 120,0,100,30);
        hitBTCbut = new CustButton("HITBTC", 230,0,100,30);
        backupBut = new CustButton("BACK UP", 340,0,100,30);
//        Main.progress[0] = 75;

        addActionToCoinCapBut(coinCapBut, db, "FOCUS");
        addActionToBinanceBut(binanceBut,db, "FOCUS");
        addActionTobackupBut(backupBut, db, "FOCUS");
        addAllToPanel();

    }

    private void addActionTobackupBut(CustButton bt, DatabaseController db, String whatToDoIfOpen) {
        if(bt.getActionListeners()!=null) bt.removeActionListener(bt.getAction());
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        if(backupWindow==null || !backupWindow.isShowing()) {
                            if(coinCap!=null) coinCap.dispose();
                            if(binance!=null) binance.dispose();
                            backupWindow = new BackupWindow("Back Up", screenDim[0] - 225, screenDim[1] - (screenDim[1] - 100), 450, 70);
                        }else if(whatToDoIfOpen.equals("FOCUS")){ backupWindow.requestFocus();
                        }else if(whatToDoIfOpen.equals("KILL")){ backupWindow.dispose();}
                    }
                });
            }
        });
    }

    private void addActionToCoinCapBut(JButton bt, DatabaseController db, String whatToDoIfOpen){
//        Main.progress[0] = 80;
        if(bt.getActionListeners()!=null) bt.removeActionListener(bt.getAction());
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        if(coinCap==null || !coinCap.isShowing()) {
                            coinCap = new CoinCapWindow("CoinMarketCap", screenDim[0] + (screenDim[0] - 240), 0, db);
                            Main.workers.submit(coinCap);
//                            new Thread(coinCap).start();
                        }else if(whatToDoIfOpen.equals("FOCUS")){ coinCap.requestFocus();
                        }else if(whatToDoIfOpen.equals("KILL")){ coinCap.dispose();}
                    }
                });
            }
        });
    }

    private void addActionToBinanceBut(JButton bt, DatabaseController db, String whatToDoIfOpen){
        if(bt.getActionListeners()!=null) bt.removeActionListener(bt.getAction());
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        if(binance == null || !binance.isShowing()) {
                            binance = new BinanceWindow("Binanace", screenDim[0] - 225, screenDim[1] - (screenDim[1] - 100), 450, 70, db);
                            Main.workers.submit(binance);
                        }else if(whatToDoIfOpen.equals("FOCUS")) { binance.requestFocus();
                        }else if(whatToDoIfOpen.equals("KILL")){ binance.dispose();}
                    }
                });
            }
        });
    }

    private void addAllToPanel(){

        add(coinCapBut);
        add(binanceBut);
        add(hitBTCbut);
        add(backupBut);
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
