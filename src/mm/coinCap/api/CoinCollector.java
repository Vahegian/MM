/*
Collects coins using supplied coin name and ticker from CoinCapMarket
Adds collected coins to mm.database using "dbCoinCap" class
makes it easy to communicate with CoinCapMarket API, and Db.
 */
package mm.coinCap.api;


import com.lucadev.coinmarketcap.CoinMarketCap;
import com.lucadev.coinmarketcap.model.CoinMarket;
import mm.database.DatabaseController;

import java.util.HashMap;
import java.util.Map;


// Uses Coinmarketcup API to get coin values.
public class CoinCollector implements Runnable {
    private String TAG = "CoinCollector";
    public static final int[] progress = {0};
    private Map<String,CoinMarket> markets;
    private dbCoinCap db;
    private Map<String, String> ctMap;

    public boolean state = true;

    public CoinCollector(DatabaseController db) {
        this.db = new dbCoinCap(db);
//        this.db.dropCoinTickerTable();
//        this.db.creatCoinTcikerTable();
//        this.db.addPermCoinsToDB();
        markets = new HashMap<String,CoinMarket>();

        getCoinAndTickerMap();
//        progress[0]=50;
        updateCoinMarkets();
//        Main.progress[0]=70;

//        coinUpdateThread.start();
//        progress[0]=100;
//        System.out.println(CoinMarketCap.ticker("TokenPay").get().getPriceUSD());

    }
    public Double[] getPrices(){
        return getCoinPrices();
    }

    private Double[] getCoinPrices(){
        Double[] coins = new Double[6];
        int i =0;
        for(CoinMarket m:markets.values()){
            coins[i] = m.getPriceUSD();
            i++;
        }
        return coins;
    }

    public Map<String,CoinMarket> getMarkets(){
        return markets;
    }

    private void getCoinAndTickerMap(){
        synchronized (db) {
            ctMap = db.getCoinAndTicker();
//            Main.progress[0] = 40;
        }
    }

    public void reCheckDbTable(){getCoinAndTickerMap();}

    public Boolean doesCoinExist(String ticker){
        try {
            CoinMarketCap.ticker(ticker).get().getPriceUSD();
            return true;
        }catch (Exception e){
            System.err.println("CoinCollector: doesCoinExist => "+e);
            return false;
        }
    }

    private void updateCoinMarkets(){
//        System.out.println(markets.size()+" = "+ctMap.size());
        int progressSize = 100/ctMap.keySet().size();
        for (String coin : ctMap.keySet()) {
            if(markets.size()<ctMap.size()) {
                if (!markets.containsKey(coin)) markets.put(coin, CoinMarketCap.ticker(ctMap.get(coin)).get());
//                else System.out.println("Coin exists!");
//                if(!(Main.progress[0] < 71)) Main.progress[0]+=2;
            } else{
                try {
                    markets.replace(coin, CoinMarketCap.ticker(ctMap.get(coin)).get());
                }catch (Exception e){}
            }
            synchronized (progress) {
                if (progress[0] < 100) {
                    progress[0] += progressSize;
                }
            }
//            System.out.print(coin+" > "+CoinMarketCap.ticker(ctMap.get(coin)).get().getPriceUSD()+" :: "+markets.get(coin).getPriceUSD()+"  ");
        }
//        System.out.println(" :");
    }

    public void addCoin (String name, String ticker){
        db.addCapCoins(name,ticker);
//        getCoinAndTickerMap();
    }

//    public Thread coinUpdateThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            while(state) {
//                try {
//                    updateCoinMarkets();
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.err.println(TAG+" > update Thread stopped");
//        }
//    });


    @Override
    public void run() {
        while(state) {
            try {
                updateCoinMarkets();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println(TAG+" > update Thread stopped");
    }
}
