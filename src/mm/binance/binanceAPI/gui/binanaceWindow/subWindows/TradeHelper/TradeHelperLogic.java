package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.Colors;
import mm.customObjects.CustTextPane;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeHelperLogic implements Runnable {
    private String TAG = "TradeHelperLogic";
    private boolean[] stopTHLThread = {false};
    private BinanceController bc;
    private String tableName = "tradeHelper";
    private Map<String, Double> lastPrices;
    private Double updateTotalAmountNowtemp = 0.0;

    public TradeHelperLogic(BinanceController bc){

        this.bc = bc;
        lastPrices=bc.getLastPrices();
        getLastPrices();
    }

    public void makeTable(){
        bc.makeUserTradeTable(tableName);
    }

    public void putDataInTable(String pair, String amount, String price, String fee){
        bc.addToTradeTable(tableName,pair,amount,price,fee);
    }

    public List<List<String>> getTableContent(){
       return bc.getTradeTableContent(tableName);
    }

    public void dropTheTable(){
        bc.dropTable(tableName);
    }

    public void updateprofits(List<HashMap<String, Object>> listWithAddeditems) {
        for (HashMap<String,Object> l:listWithAddeditems) {
            String pair = ((JTextField)l.get("pairfield")).getText();
            Double amount=Double.parseDouble(((JTextField)l.get("amountfield")).getText()),
                    price = Double.parseDouble(((JTextField)l.get("curPricefield")).getText()),
                    fee= Double.parseDouble((String) l.get("fee"));
            updateProfitOfaRow(pair, amount, price, fee, (CustTextPane) l.get("profitpane"));
        }
    }

    private void updateProfitOfaRow(String pair, Double amount, Double price, Double fee, CustTextPane profit){
        Double newPrice = lastPrices.get(pair);
        Double value = (newPrice*amount)-(price*amount);
        Double amountOfFee = (fee/100)*value;
        Double total = value-amountOfFee;
//        System.out.println(pair+" : "+total+" FEE ->"+amountOfFee);
        if(total>0){
            profit.setPaneText(total+"");
            profit.setPaneColor(Colors.green, "green");
        }else if(total<0){
//            total*=-1;
            profit.setPaneText(total+"");
            profit.setPaneColor(Colors.red, "red");
        }
    }

    private void getLastPrices(){
        lastPrices = bc.getLastPrices();
    }

    public void stopTheThread(){stopTHLThread[0]=true;}

    public void updateTotalProfit(List<HashMap<String, Object>> listWithAddeditems, CustTextPane totalProfitPane) {
        Double totalProfit = 0.0;
        for(HashMap<String, Object> o: listWithAddeditems){
            String price = ((CustTextPane) o.get("profitpane")).getPlainText();
//            int length = price.length();
//            System.out.println(price);
            totalProfit+= Double.parseDouble(price);
        }
        totalProfitPane.setPaneText(totalProfit.toString());
        if(totalProfit>0) totalProfitPane.setPaneColor(Colors.green,"green");
        else if(totalProfit<0)totalProfitPane.setPaneColor(Colors.red,"red");
        else totalProfitPane.setBackground(Colors.black);

    }

    public void updateTotalAmountNow(List<HashMap<String, Object>> listWithAddeditems, CustTextPane totalAmountPane) {
        Double totalAmount = 0.0;
        for(HashMap<String, Object> o: listWithAddeditems){
            String amount = ((JTextField) o.get("amountfield")).getText();
            Double newPrice = 0.0;
            synchronized (lastPrices){
                newPrice = lastPrices.get(((JTextField) o.get("pairfield")).getText());
            }
//            int length = price.length();
//            System.out.println(price);
            totalAmount+= Double.parseDouble(amount)*newPrice;
        }
        totalAmountPane.setPaneText(totalAmount.toString());
        if(totalAmount>updateTotalAmountNowtemp) totalAmountPane.setPaneColor(Colors.green,"green");
        else if(totalAmount<updateTotalAmountNowtemp)totalAmountPane.setPaneColor(Colors.red,"red");
        else totalAmountPane.setPaneColor(Colors.black, "black");
        updateTotalAmountNowtemp = totalAmount;
    }

    public void updateTotalAmountBought(List<HashMap<String, Object>> listWithAddeditems, CustTextPane totalAmountBoughtPane) {
        Double totalAmountBought = 0.0;
        for(HashMap<String, Object> o: listWithAddeditems){
            String amount = ((JTextField) o.get("amountfield")).getText();
            String priceBought = ((JTextField) o.get("curPricefield")).getText();

            totalAmountBought+= Double.parseDouble(amount)*Double.parseDouble(priceBought);
        }
        totalAmountBoughtPane.setText(totalAmountBought.toString());
        totalAmountBoughtPane.setForeground(Colors.blue);
    }

    @Override
    public void run() {
        while (!stopTHLThread[0]) {
            try {
                getLastPrices();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.err.println(TAG+": "+e);
            }
        }
        System.err.println(TAG+" > Thread Stopped");
    }
}
