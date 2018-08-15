package mm.database;

import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController {
//    public static final int[] progress = {0};
    private String TAG = "DatabaseController";
    private Sql mySql ;
    private Statement statement;

    public DatabaseController (String dbName, String user, String password){
        mySql = new Sql(dbName,user,password);
        statement = mySql.creatStatement();
//        progress[0]=100;

    }

    public void createTable (String name, String[] columns) throws InvalidParameterException{
        String columNames = "(";
        if (columns.length>0){
            for(int i = 0; i<columns.length; i++){
                columNames+=columns[i];
                if(i<columns.length-1) columNames+=", ";
            }
            columNames+=")";
//            System.out.println("CREATE TABLE "+name+" "+columNames);
        }else throw new InvalidParameterException("Array has very few arguments!");
        try {
            statement.executeUpdate("CREATE TABLE "+name+" "+columNames);
        } catch (SQLException e) {
            System.err.println(TAG+": createTable => "+e);
        }
    }

    public void addToTable (String name, String[] names, String[] values) throws InvalidParameterException{
        String columnNames ="(";
        if (names.length>0){
//            rowNames.concat("(");
            for(int i = 0; i<names.length; i++){
                columnNames+=names[i];
                if(i<names.length-1)
                    columnNames+=", ";
            }
            columnNames+=")";
        }else throw new InvalidParameterException("Array has very few arguments!");

        String columnValues = "(";
        if (values.length>0){
//            rowValues.concat("(");
            for(int i = 0; i<values.length; i++){
                columnValues+=values[i];
                if(i<values.length-1)
                    columnValues+=", ";
            }
            columnValues+=")";
        }else throw new InvalidParameterException("Array has very few arguments!");
//        System.out.println("INSERT INTO "+name+" "+columnNames+" VALUES "+columnValues);
        try {
            statement.executeUpdate("INSERT INTO "+name+" "+columnNames+" VALUES "+columnValues);
        } catch (SQLException e) {
            System.err.println(TAG+": addToTable => "+e);
        }
    }

    public void dropTable (String name){
        try {
            statement.executeUpdate("DROP TABLE "+name);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public ResultSet SelectAllFrom (String tableName){
        ResultSet st;

        try {
            st = statement.executeQuery("SELECT * FROM "+tableName);
            return st;
        } catch (SQLException e) {
            System.err.println(TAG+": SelectAllFrom => "+e);
            return null;
        }
    }

    public void close(){mySql.closeConnection();}
}
