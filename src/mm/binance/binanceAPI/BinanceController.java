package mm.binance.binanceAPI;

import com.binance.api.client.*;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.market.OrderBook;
import mm.database.DatabaseController;

import javax.management.openmbean.InvalidKeyException;
import java.util.*;

public class BinanceController {
    private String TAG = "BinanceController";
    public boolean threadState = true;

    private BinanceApiClientFactory clientFactory;
    private BinanceApiRestClient restClient;
    private BinanceApiAsyncRestClient asyncRestClient;
    private BinanceApiWebSocketClient webSocketClient;

    private final Map<String,OrderBook> orderBookMap = new HashMap<String, OrderBook>();
    private final Map<String, Double> lastPrices = new HashMap<String, Double>() {};
    private Double BTCpriceInUSD = 0.0;
    private dbBinance db;

    public boolean isloaded= false;

    public BinanceController(DatabaseController controller){
        db = new dbBinance(controller);
//        lastPrices = new HashMap<String, Double>() {};

    }

    public BinanceController(DatabaseController controller, String str){
        if(str.equals("DEFAULT")){
            makeClientFactory("API-KEY", "SECRET");
            makeRestClient();
//            lastPrices = new HashMap<String, Double>() {};
            db = new dbBinance(controller);
            db.tableName = "binancePairs";

//            addPairsFromDbToMap(10);
        }
    }

    public void makeDefaultPairsTable(){
        db.dropbinancePairsTable("binancePairs");
        db.creatbinancePairsTable("binancePairs");
        db.addPermCoinsToDB();
    }

    public void addPairsFromDbToMap(int quantity){
        for (String s: db.getCoinPairs()){
            makeOrderBook(s,quantity);
        }
    }

    public boolean doesCoinPairExist(String coinPair){
        try {
            addCoinPair(coinPair);
        }catch (Exception e){
            System.err.println(TAG+" > doesCoinPairExist: "+e);
            return false;
        }
        return true;
    }

    public void addCoinPair(String pair) {
        db.addCoinPair(pair);
    }

    public void makeClientFactory(String key, String secret){
        clientFactory = BinanceApiClientFactory.newInstance(key, secret);
    }

    public void makeRestClient(){
        restClient= clientFactory.newRestClient();
    }

    public void makeAsyncRestClient(){
        asyncRestClient = clientFactory.newAsyncRestClient();
    }

    public void makeWebSocketClient(){
        webSocketClient = clientFactory.newWebSocketClient();
    }

    public long restServerTime(){
        return restClient.getServerTime();
    }

    public void makeOrderBook(String pair, int quantity){
        pair = pair.toUpperCase();
        if(orderBookMap.keySet().size()<1){
//            orderBookMap = new HashMap<String, OrderBook>();
            orderBookMap.put(pair,restClient.getOrderBook(pair, quantity));
        }else {
            if(!orderBookMap.containsKey(pair))
                orderBookMap.put(pair,restClient.getOrderBook(pair, quantity));
        }
    }

    public Map<String,OrderBook> getMarkets(){
        if(orderBookMap.keySet().size()<1)
            return orderBookMap;
        else throw new EmptyStackException();
    }

    public Map<String,Double> getLastPrices(){
        if(orderBookMap.keySet().size()<1 || lastPrices.keySet().size()<1) throw new InvalidKeyException("Empty Map!");
        else return lastPrices;
    }

    public void  updateLastPrices(){
        setBTCPrice();
        synchronized (lastPrices) {
            if (orderBookMap.keySet() != null && lastPrices.keySet() != null) {
                for (String s : orderBookMap.keySet()) {
                    if (!lastPrices.containsKey(s)) {
//                    synchronized (lastPrices) {
                        lastPrices.put(s, Double.parseDouble(restClient.get24HrPriceStatistics(s).getLastPrice()) * BTCpriceInUSD);
//                    }
                    } else {
//                    synchronized (lastPrices) {
                        lastPrices.replace(s, Double.parseDouble(restClient.get24HrPriceStatistics(s).getLastPrice()) * BTCpriceInUSD);
//                    }
                    }
                }
            } else throw new InvalidKeyException("Empty Map!");
        }
    }

    private Account getAccount(){
        return restClient.getAccount();
    }

    private void setBTCPrice(){
        BTCpriceInUSD = Double.parseDouble(restClient.get24HrPriceStatistics("BTCUSDT").getLastPrice());
    }

    public Double getLastpriceInUSD(String coin) {
        setBTCPrice();
        coin = coin.toUpperCase();
        if(coin.equals("BTC"))return BTCpriceInUSD;
        return (Double.parseDouble(restClient.get24HrPriceStatistics(coin+"BTC").getLastPrice())*BTCpriceInUSD);
    }

    public Double getAccountBalance(){
        setBTCPrice();
        double balance = 0.0;
        for(AssetBalance b : getAccount().getBalances()){
            balance+=((Double.parseDouble(b.getFree())+Double.parseDouble(restClient.get24HrPriceStatistics(b.getAsset()+"BTC").getLastPrice()))*BTCpriceInUSD);

        }
        return balance;
    }


    /*
    Trade Helper support
     */

    public void makeUserTradeTable(String name){
        db.makeUserTradeTable(name);
    }

    public void addToTradeTable(String tablename, String pair, String amount, String price, String fee){
        db.addTradeDataToTable(tablename,pair,amount,price,fee);
    }



    public Thread updateThread = new Thread(new Runnable() {
//        boolean t = true;
        @Override
        public void run() {
            while (threadState) {
                try {
                    synchronized (orderBookMap) {
                        updateLastPrices();
                    }
                    Thread.sleep(2000);
//                    if(t){isloaded = true; t=false;}
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.err.println(TAG+" > update Thread stopped");
        }
    });

    public List<List<String>> getTradeTableContent(String tablename) {
        return db.getTradeTableContent(tablename);
    }

    public void dropTable(String tableName) {
        db.dropTable(tableName);
    }
}
