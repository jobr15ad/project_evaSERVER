package dal;

import logic.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kasper on 17/10/2016.
 */
public class MYSQLDriver {
    private static Connection dbConnection = null;
    private static final String URL = ConfigLoader.DB_TYPE+ConfigLoader.DB_HOST+ConfigLoader.DB_PORT+"/"+ConfigLoader.DB_NAME+"?autoReconnect=true&useSSL=false";
    private static final String USERNAME = ConfigLoader.DB_USER;
    private static final String PASSWORD = ConfigLoader.DB_PASS;


    public MYSQLDriver(){
        try{
            dbConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }

        catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    public ResultSet executeSQL(String sql){
        try {
            return dbConnection.prepareStatement(sql).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertSQL(String sql){
        try{
            dbConnection.prepareStatement(sql).executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}