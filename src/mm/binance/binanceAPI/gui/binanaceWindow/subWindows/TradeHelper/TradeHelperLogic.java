package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import mm.Main;
import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.Colors;
import mm.customObjects.CustTextPane;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
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
    private Map<JTextField, Double> tempHowMuchIsTotal = new HashMap<JTextField, Double>() ;

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
            String pair = ((JTextField)l.get(TradeHelperWindow.PAIRKEY)).getText();
            Double amount=Double.parseDouble(((JTextField)l.get(TradeHelperWindow.AMOUNTKEY)).getText()),
                    price = Double.parseDouble(((JTextField)l.get(TradeHelperWindow.PRICEKEY)).getText()),
                    fee= Double.parseDouble((String) l.get(TradeHelperWindow.FEEKEY));
            updateProfitOfaRow(pair, amount, price, fee, (CustTextPane) l.get(TradeHelperWindow.PROFITKEY));
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
            String price = ((CustTextPane) o.get(TradeHelperWindow.PROFITKEY)).getPlainText();
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
            String amount = ((JTextField) o.get(TradeHelperWindow.AMOUNTKEY)).getText();
            Double newPrice = 0.0;
            synchronized (lastPrices){
                newPrice = lastPrices.get(((JTextField) o.get(TradeHelperWindow.PAIRKEY)).getText());
            }
//            int length = price.length();
//            System.out.println(price);
            totalAmount+= Double.parseDouble(amount)*newPrice;
        }
        String price = totalAmount.toString();
        totalAmountPane.setPaneText(price);
        if(totalAmount>updateTotalAmountNowtemp) totalAmountPane.setPaneColor(Colors.green,"green");
        else if(totalAmount<updateTotalAmountNowtemp)totalAmountPane.setPaneColor(Colors.red,"red");
        else totalAmountPane.setPaneColor(Colors.black, "black");
        updateTotalAmountNowtemp = totalAmount;
    }

    public void updateTotalAmountBought(List<HashMap<String, Object>> listWithAddeditems, CustTextPane totalAmountBoughtPane) {
        Double totalAmountBought = 0.0;
        for(HashMap<String, Object> o: listWithAddeditems){
            String amount = ((JTextField) o.get(TradeHelperWindow.AMOUNTKEY)).getText();
            String priceBought = ((JTextField) o.get(TradeHelperWindow.PRICEKEY)).getText();

            totalAmountBought+= Double.parseDouble(amount)*Double.parseDouble(priceBought);
        }
        totalAmountBoughtPane.setText("$"+new DecimalFormat("#0.00000").format(totalAmountBought));
        totalAmountBoughtPane.setForeground(Colors.blue);
    }

    public void updateHowManyCanBuy(List<HashMap<String, Object>> listWithAddeditems) {
        for(HashMap<String, Object> o : listWithAddeditems) {
            Double profit = Double.parseDouble(((CustTextPane)o.get(TradeHelperWindow.PROFITKEY)).getPlainText());
            Double currentPrice = lastPrices.get(((JTextField)o.get(TradeHelperWindow.PAIRKEY)).getText());
            Double fee = Double.parseDouble((String) o.get(TradeHelperWindow.FEEKEY));
            Double profitAfterFee = profit - ((fee/100)*profit);
            Double total = (profitAfterFee/currentPrice);
            setHowManyCanBuyPaneText(o.get(TradeHelperWindow.HOWMANYPANEKEY),total);
        }
    }

    private void setHowManyCanBuyPaneText(Object o, Double total) {
        CustTextPane quantity =(CustTextPane) o;
        if(total>0){
            quantity.setPaneText(total+"");
            quantity.setPaneColor(Colors.green, "green");
        }else if(total<0){
//            total*=-1;
            quantity.setPaneText("0");
            quantity.setPaneColor(Colors.red, "red");
        }
    }

    public void updateTotalOfaRow(List<HashMap<String, Object>> listWithAddeditems) {
        for(HashMap<String,Object> o:listWithAddeditems){
            String pairkey = ((JTextField)o.get(TradeHelperWindow.PAIRKEY)).getText();
            Double amount = Double.parseDouble(((JTextField)o.get(TradeHelperWindow.AMOUNTKEY)).getText());
            Double total = amount*lastPrices.get(pairkey);
            setHowMuchIsTotalPaneText(o.get(TradeHelperWindow.HOWMUCHISTOTALPANEKEY), (JTextField)o.get(TradeHelperWindow.PAIRKEY), total);
        }
    }

    private void setHowMuchIsTotalPaneText(Object o, JTextField pairkey, Double total) {
        CustTextPane totalOfaRow =(CustTextPane) o;
        if(!(tempHowMuchIsTotal.containsKey(pairkey))){
            tempHowMuchIsTotal.put(pairkey,0.0);
        }

        if(total>tempHowMuchIsTotal.get(pairkey)){
            totalOfaRow.setPaneText(total+"");
            totalOfaRow.setPaneColor(Colors.green, "green");
        }else if(total< tempHowMuchIsTotal.get(pairkey)){
//            total*=-1;
            totalOfaRow.setPaneText(total+"");
            totalOfaRow.setPaneColor(Colors.red, "red");
        }else totalOfaRow.setPaneColor(Colors.black,"black");
        tempHowMuchIsTotal.replace(pairkey, total);
    }

    @Override
    public void run() {
        Main.plusThread();
        while (!stopTHLThread[0]) {
            try {
                getLastPrices();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.err.println(TAG+": "+e);
            }
        }
        System.err.println(TAG+" > Thread Stopped");
        Main.minusThread();
    }
}
