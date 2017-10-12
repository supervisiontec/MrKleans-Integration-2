/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import db_connections.DataSourceWrapper;
import model.TFingerPrint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author 'Kasun Chamara'
 */
public class MsSQLController {

    private final DataSourceWrapper mssqlDataSourceWrapper;
    private static final Logger LOGGER = Logger.getLogger(MsSQLController.class);

    public MsSQLController() throws SQLException {
        this.mssqlDataSourceWrapper = new DataSourceWrapper("jdbc:sqlserver://localhost:1433;databaseName=RIMS_Attendance;", "sa", "123");
    }

    public ArrayList<TFingerPrint> getFingerPrintList() throws SQLException {
        LOGGER.debug("reading fingerprint data");
        try (Connection connection = mssqlDataSourceWrapper.getConnection()) {
            String sql = "select ID,DN,DIN,Clock from dbo.ras_AttRecord";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<TFingerPrint> list = new ArrayList<>();
            while (resultSet.next()) {
                TFingerPrint fingerPrintData = new TFingerPrint();
                fingerPrintData.setId(resultSet.getInt(1));
                fingerPrintData.setDn(resultSet.getInt(2));
//                fingerPrintData.setBranch(resultSet.getInt(3));
                fingerPrintData.setDin(resultSet.getInt(3));
                fingerPrintData.setClock(resultSet.getTimestamp(4));
                list.add(fingerPrintData);
            }
            LOGGER.debug(list.size() + " fingerprint data found");
            return list;
        }
    }

    public int deleteFingerPrint(ArrayList<TFingerPrint> list) throws SQLException {
        LOGGER.debug("deleting fingerprint data");
        try (Connection connection = mssqlDataSourceWrapper.getConnection()) {
            String deleteSql = "delete from ras_AttRecord where ID=?";
            PreparedStatement preparedStatementDelete = connection.prepareStatement(deleteSql);
            for (TFingerPrint data : list) {
                preparedStatementDelete.setInt(1, data.getId());
                preparedStatementDelete.execute();
                LOGGER.debug("fingerprint data deleted for " + data.getId());
            }
            return list.size();
        }
    }
}
