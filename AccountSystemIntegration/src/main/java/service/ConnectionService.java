/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import db_connections.DataSourceWrapper;
import java.sql.SQLException;

/**
 *
 * @author chama
 */
public class ConnectionService {

    private static ConnectionService instance;
    private final DataSourceWrapper operationDataSourceWrapper;
    private final DataSourceWrapper accountDataSourceWrapper;

    public ConnectionService() throws SQLException {
        
        
//        Properties prop = new Properties();
//        InputStream input = null;
//        String dbName = null;
//        String user = null;
//        String pswd = null;
//        try {
//
//            input = new FileInputStream("config.properties");
//
//            // load a properties file
//            prop.load(input);
//
//            // get the property value and print it out
//            System.out.println("operation database name : " + "jdbc:mysql://localhost:3306/integration_test");
//            System.out.println("account database name : " + "jdbc:mysql://localhost:3306/care_point_account");
//            // set value into variable
////            dbName = prop.getProperty("getDataSourceName");
////            user = prop.getProperty("getDataSourceUser");
////            pswd = prop.getProperty("getDataSourcePswd");
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        
        
        this.operationDataSourceWrapper = new DataSourceWrapper("jdbc:mysql://192.168.7.253:3306/integration_test", "root", "mysql");
        this.accountDataSourceWrapper = new DataSourceWrapper("jdbc:mysql://localhost:3306/care_point_account", "root", "mysql");
    }
    
     public static ConnectionService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionService();
        }
        return instance;
    }

    public DataSourceWrapper getOperationDataSourceWrapper() {
        return operationDataSourceWrapper;
    }

    public DataSourceWrapper getAccuntDataSourceWrapper() {
        return accountDataSourceWrapper;
    }

}
