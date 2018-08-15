package mm.binance.binanceAPI;

import mm.database.DatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class dbBinance {
    private String TAG = "dbBinance";
    private DatabaseController db;
    public  String tableName;

    public dbBinance(DatabaseController db){
        this.db = db;
    }

    public void creatbinancePairsTable(String tableName){
        this.tableName = tableName;
        String[] columnNames = {"coinPair VARCHAR(25)"};
        synchronized (db){
            db.createTable(tableName, columnNames);
        }
    }

    public void dropbinancePairsTable(String tableName){
        synchronized (db){
            db.dropTable(tableName);
        }
    }

    public void addPermCoinsToDB(){
//        addPrantsToString("BTCUSDT");
        addPair(addPrantsToString("ETHBTC"));
        addPair(addPrantsToString("XRPBTC"));
        addPair(addPrantsToString("VENBTC"));
        addPair(addPrantsToString("IOTABTC"));
        addPair(addPrantsToString("EOSBTC"));
        addPair(addPrantsToString("XVGBTC"));

    }

    public void addCoinPair(String pair){
        addPair(addPrantsToString(pair));
    }

    private String addPrantsToString(String pair){
        String pairWithPrants = "'";// = pair.split("");
        for(String c: pair.split("")){
            pairWithPrants+=c;
        }
        pairWithPrants+="'";
        return pairWithPrants;
    }

    private void addPair(String coinpair){
        synchronized (db){
            db.addToTable(tableName,new String[]{"coinPair"}, new String[]{coinpair});
        }
    }

    public List<String> getCoinPairs(){
        ResultSet st = db.SelectAllFrom(tableName);
        List<String> cList = new LinkedList<>();
        try {
            while (st.next()){
                cList.add(st.getString(1));
            }
            return cList;
        } catch (SQLException e) {
            System.err.println(TAG+": getCoinPairs => "+e);
            return null;
        }
    }

    /*
    TradeHelper support
     */
    public void makeUserTradeTable(String tablename) {
        String[] columns = {"pairs VARCHAR(25)","amount VARCHAR(25)","price VARCHAR(25)","fee VARCHAR(25)"};
        db.createTable(tablename,columns);
    }

    public void addTradeDataToTable(String tablename, String pair,String amount,String price, String fee){
        String[] cols = {"pairs","amount","price","fee"};
        String[] vals = {addPrantsToString(pair), addPrantsToString(amount),addPrantsToString(price),addPrantsToString(fee)};
        addToTable(tablename,cols,vals);
    }

    private void addToTable(String tablename, String[] columnNames, String[] values){
        synchronized (db){
            db.addToTable(tablename,columnNames, values);
        }
    }

    public List<List<String>> getTradeTableContent(String tablename) {
        List<List<String>> content = new LinkedList<List<String>>();
        ResultSet st = db.SelectAllFrom(tablename);
        try {
            while (st.next()){
                List<String> data = new ArrayList<String>();
                data.add(st.getString(1));
                data.add(st.getString(2));
                data.add(st.getString(3));
                data.add(st.getString(4));
                content.add(data);
            }
        }catch (SQLException e){System.err.println(e);}
        return content;
    }

    public void dropTable(String tableName) {
        db.dropTable(tableName);
    }
}
