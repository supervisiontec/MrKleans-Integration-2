/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import db_connections.DataSourceWrapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;
import model.operation_model.MMaster;
import model.operation_model.TTypeIndexDetail;
import org.apache.log4j.Logger;
import service.ConnectionService;

/**
 *
 * @author 'Kasun Chamara'
 */
public class OperationController {

    private static OperationController instance;
    private final DataSourceWrapper operationDataSourceWrapper;
    private static final Logger LOGGER = Logger.getLogger(OperationController.class);

    public OperationController() throws SQLException {

        this.operationDataSourceWrapper = new ConnectionService().getInstance().getOperationDataSourceWrapper();
    }

    public static OperationController getInstance() throws SQLException {
        if (instance == null) {
            instance = new OperationController();
        }
        return instance;
    }

    public ArrayList<Grn> getNotCheckGrnList(String date) throws SQLException {
        try (Connection connection = operationDataSourceWrapper.getConnection()) {
            String query = "select grn.*\n"
                    + "from grn where grn.enter_date <= ? and grn.`check`=0\n"
                    + "and grn.branch=1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Grn> list = new ArrayList<>();
            while (resultSet.next()) {
                Grn grn = new Grn();
                grn.setIndexNo(resultSet.getInt(1));
                grn.setGrnNo(resultSet.getString(2));
                grn.setEnterDate(resultSet.getString(3));
                grn.setEnterTime(resultSet.getString(4));
                grn.setUpdatedDate(resultSet.getString(5));
                grn.setUpdatedTime(resultSet.getString(6));
                grn.setSupNo(resultSet.getString(7));
                grn.setSupName(resultSet.getString(8));
                grn.setRefNo(resultSet.getString(9));
                grn.setTotalValue(resultSet.getBigDecimal(10));
                grn.setNbt(resultSet.getBigDecimal(11));
                grn.setNbtValue(resultSet.getBigDecimal(12));
                grn.setVat(resultSet.getBigDecimal(13));
                grn.setVatValue(resultSet.getBigDecimal(14));
                grn.setFinalValue(resultSet.getBigDecimal(15));
                grn.setBranch(resultSet.getInt(16));
                grn.setCheck(resultSet.getBoolean(17));
                grn.setCreditPeriod(resultSet.getInt(18));

                list.add(grn);
            }
            LOGGER.debug(list.size() + "data found");
            return list;
        }
    }

    public List<GrnDetail> getGrnDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "select grn_detail.*\n"
                + "from grn_detail where grn_detail.grn=?";
        PreparedStatement preparedStatement = operaConnection.prepareStatement(query);
        preparedStatement.setInt(1, indexNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<GrnDetail> list = new ArrayList<>();
        while (resultSet.next()) {
            GrnDetail detail = new GrnDetail();
            detail.setIndexNo(resultSet.getInt(1));
            detail.setGrn(resultSet.getInt(2));
            detail.setItemNo(resultSet.getString(3));
            detail.setItemName(resultSet.getString(4));
            detail.setItemType(resultSet.getString(5));
            detail.setItemUnit(resultSet.getString(6));
            detail.setItemBarcode(resultSet.getString(7));
            detail.setReorderMax(resultSet.getBigDecimal(8));
            detail.setReorderMin(resultSet.getBigDecimal(9));
            detail.setCost(resultSet.getBigDecimal(10));
            detail.setQty(resultSet.getBigDecimal(11));
            detail.setValue(resultSet.getBigDecimal(12));
            detail.setDiscount(resultSet.getBigDecimal(13));
            detail.setDiscountValue(resultSet.getBigDecimal(14));
            detail.setNetValue(resultSet.getBigDecimal(15));
            detail.setSalesPrice(resultSet.getBigDecimal(16));
            list.add(detail);
        }
        return list;

    }

    public TTypeIndexDetail CheckTypeIndexDetail(String type, String typeIndex, Connection operaConnection) throws SQLException {
        String query = "select t_type_index_detail.*\n"
                + "from t_type_index_detail\n"
                + "WHERE t_type_index_detail.master_ref_id = ? and t_type_index_detail.`type` = ?";
        PreparedStatement preparedStatement = operaConnection.prepareStatement(query);
        preparedStatement.setString(1, typeIndex);
        preparedStatement.setString(2, type);
        ResultSet resultSet = preparedStatement.executeQuery();
        TTypeIndexDetail typeIndexDetail = new TTypeIndexDetail();
        if (resultSet.next()) {
            typeIndexDetail.setAccountIndex(resultSet.getInt(1));
            typeIndexDetail.setMasterRefId(resultSet.getString(2));
            typeIndexDetail.setType(resultSet.getString(3));
            typeIndexDetail.setAccountRefId(resultSet.getInt(4));
            typeIndexDetail.setAccountIndex(resultSet.getInt(5));
        }
        return typeIndexDetail;
    }

    public Integer saveTypeIndexDetail(TTypeIndexDetail tTypeIndexDetail, Connection operaConnection) throws SQLException {

        String sql = "insert into t_type_index_detail (master_ref_id,type,account_ref_id,account_index)"
                + " values (?,?,?,?)";
        PreparedStatement preparedStatement = operaConnection.prepareStatement(sql);
        preparedStatement.setString(1, tTypeIndexDetail.getMasterRefId());
        preparedStatement.setString(2, tTypeIndexDetail.getType());
        preparedStatement.setInt(3, tTypeIndexDetail.getAccountRefId());
        preparedStatement.setInt(4, tTypeIndexDetail.getAccountIndex());
        int save = preparedStatement.executeUpdate();

        return save;
    }

    public Integer updateMaster(MMaster master) throws SQLException {
        try (Connection connection = operationDataSourceWrapper.getConnection()) {
            String insertSql = "UPDATE m_master set `check` = true ,updated_date=? and updated_time=?  \n"
                    + "WHERE index_no=?";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            preparedStatement.setString(2, new SimpleDateFormat("kk:ss:mm").format(new Date()));
            preparedStatement.setInt(3, master.getIndexNo());

            return preparedStatement.executeUpdate();

        }
    }

    public Integer updateGrn(Grn grn, Connection connection) throws SQLException {
        String insertSql = "UPDATE grn set `check` = true ,updated_date=? , updated_time=?\n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(2, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(3, grn.getIndexNo());

        return preparedStatement.executeUpdate();

    }

}
