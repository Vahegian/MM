/*
Class makes it easy to communicate with sql and specifically
with table "CoinTicker" created for storing coins gathered from
CoinMarketCap API
 */

package mm.coinCap.api;

import mm.database.DatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class dbCoinCap {
    private String TAG = "dbCoinCap";
    private DatabaseController db;
    private String tableName = "coinTicker";

    public dbCoinCap(DatabaseController db){
        this.db = db;
//        Main.progress[0] = 40;
    }

    public void creatCoinTcikerTable(){
        String[] columnNames = {"coin VARCHAR(8)", "ticker VARCHAR(30)"};
        synchronized (db){
            db.createTable(tableName, columnNames);
        }
    }

    public void dropCoinTickerTable(){
        synchronized (db){
            db.dropTable(tableName);
        }
    }

    public void addPermCoinsToDB(){
        addCapCoins("'BTC'", "'Bitcoin'");
        addCapCoins("'ETC'", "'Ethereum'");
        addCapCoins("'XRP'", "'Ripple'");
        addCapCoins("'VEN'", "'VeChain'");
        addCapCoins("'IOTA'", "'IOTA'");
        addCapCoins("'EOS'", "'EOS'");
        addCapCoins("'TPAY'", "'TokenPay'");
//        addCapCoins("'XVG'", "'Verge'");

    }

    public void addCapCoins(String coin, String ticker){
        synchronized (db){
            db.addToTable(tableName,new String[]{"coin","ticker"}, new String[]{coin, ticker});
        }
    }

    public Map<String,String> getCoinAndTicker(){
        ResultSet st = db.SelectAllFrom(tableName);
        Map<String ,String> ctMap = new HashMap<String, String>();
        try {
            while (st.next()){
                ctMap.put(st.getString(1),st.getString(2));
            }
            return ctMap;
        } catch (SQLException e) {
           System.err.println(TAG+": getCoinAndTicker => "+e);
           return null;
        }
    }

}
