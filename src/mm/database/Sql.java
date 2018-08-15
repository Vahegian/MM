/*
connects to the DBMS and local mySql server
 */

package mm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Sql {
    private String TAG = "Sql";
    private Connection connection;
    private Statement statement;
    public Sql(String database, String user, String pass){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+database, user,pass);
//            Main.progress[0]=10;

//            statement.executeUpdate("DROP TABLE coins");
//            statement.executeUpdate("CREATE TABLE coins (coin VARCHAR(4), coin_amount DOUBLE(9,8), usd_price DOUBLE(7,2), btc_price DOUBLE(9,8))");
//            statement.executeUpdate("INSERT INTO coins (coin, coin_amount, usd_price, btc_price) VALUES ('btc', 0.04169128, 105.63, 0.04169128)");
//            statement.executeUpdate("CREATE TABLE customers (custid SMALLINT, name VARCHAR(30), email VARCHAR(30))");

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void closeConnection (){
        try {
            connection.close();
        }catch (SQLException e){
            System.err.println(TAG+": CloseConnection =>"+e);
        }
    }

    public Statement creatStatement(){
        try {
            statement = connection.createStatement();
//            Main.progress[0] = 30;
            return statement;
        } catch (SQLException e) {
            System.err.println(TAG+": createStatement"+e);
        }
        return null;
    }
}
