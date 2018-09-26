package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;

import com.tunyk.currencyconverter.BankUaCom;
import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
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
    private BankUaCom currencyConverter;

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

    public void updateprofits(List<PairItemsPanel> listWithAddeditems) {
        for (PairItemsPanel l:listWithAddeditems) {
            String pair = l.p1.getText();
            Double amount=Double.parseDouble(l.p2.getText()),
                    price = Double.parseDouble(l.p3.getText()),
                    fee= Double.parseDouble(l.p4.getText());
            updateProfitOfaRow(pair, amount, price, fee, l.p5);
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

    public void updateTotalProfit(List<PairItemsPanel> listWithAddeditems, CustTextPane totalProfitPane) {
        Double totalProfit = 0.0;
        for(PairItemsPanel o: listWithAddeditems){
            String price = o.p5.getPlainText();
//            int length = price.length();
//            System.out.println(price);
            totalProfit+= Double.parseDouble(price);
        }
        totalProfitPane.setPaneText(totalProfit.toString());
        if(totalProfit>0) totalProfitPane.setPaneColor(Colors.green,"green");
        else if(totalProfit<0)totalProfitPane.setPaneColor(Colors.red,"red");
        else totalProfitPane.setBackground(Colors.black);

    }

    public void updateTotalAmountNow(List<PairItemsPanel> listWithAddeditems, CustTextPane totalAmountPane) {
        Double totalAmount = 0.0;
        for(PairItemsPanel o: listWithAddeditems){
            String amount = o.p2.getText();
            Double newPrice = 0.0;
            synchronized (lastPrices){
                newPrice = lastPrices.get(o.p1.getText());
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

    public void updateTotalAmountBought(List<PairItemsPanel> listWithAddeditems, CustTextPane totalAmountBoughtPane, CustTextPane totalboughtGBP) {
        Double totalAmountBought = 0.0;
        for(PairItemsPanel o: listWithAddeditems){
            String amount = o.p2.getText();
            String priceBought = o.p3.getText();

            totalAmountBought+= Double.parseDouble(amount)*Double.parseDouble(priceBought);
        }
        totalAmountBoughtPane.setText("$"+new DecimalFormat("#0.00000").format(totalAmountBought));
        totalAmountBoughtPane.setForeground(Colors.blue);
        try {
            totalboughtGBP.setPaneText(currencyConverter.convertCurrency(totalAmountBought.floatValue()).toString());
            totalboughtGBP.setForeground(Colors.blue);
        } catch (CurrencyConverterException e) {
            e.printStackTrace();
        }
    }

    public void updateHowManyCanBuy(List<PairItemsPanel> listWithAddeditems) {
        for(PairItemsPanel o : listWithAddeditems) {
            Double profit = Double.parseDouble(o.p5.getPlainText());
            Double currentPrice = lastPrices.get(o.p1.getText());
            Double fee = Double.parseDouble(o.p4.getText());
            Double profitAfterFee = profit - ((fee/100)*profit);
            Double total = (profitAfterFee/currentPrice);
            setHowManyCanBuyPaneText(o.p6,total);
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

    public void updateTotalOfaRow(List<PairItemsPanel> listWithAddeditems) {
        for(PairItemsPanel o:listWithAddeditems){
            String pairkey = o.p1.getText();
            Double amount = Double.parseDouble(o.p2.getText());
            Double total = amount*lastPrices.get(pairkey);
            setHowMuchIsTotalPaneText(o.p7, o.p1, total);
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

    public void updateUSDtoGBP(CustTextPane totalProfitPane, CustTextPane totalprofitgbp, CustTextPane totalAmountNowPane, CustTextPane totalamountgbp) {
        float totalprofit = (float) Double.parseDouble(totalProfitPane.getPlainText());
        float totalamount = (float) Double.parseDouble(totalAmountNowPane.getPlainText());
        try {
            totalprofitgbp.setPaneText(currencyConverter.convertCurrency(totalprofit).toString());
            totalprofitgbp.setPaneColor((Color)totalProfitPane.getColors()[0], (String) totalProfitPane.getColors()[1]);
            totalamountgbp.setPaneText(currencyConverter.convertCurrency(totalamount).toString());
            totalamountgbp.setPaneColor((Color)totalAmountNowPane.getColors()[0], (String) totalAmountNowPane.getColors()[1]);
        } catch (CurrencyConverterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Main.plusThread();
        try {
            currencyConverter = new BankUaCom(Currency.USD, Currency.GBP);
        } catch (CurrencyConverterException e) {
            e.printStackTrace();
        }
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
