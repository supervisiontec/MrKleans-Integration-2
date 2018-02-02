/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import common.Constant;
import db_connections.DataSourceWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;
import model.operation_model.TTypeIndexDetail;

/**
 *
 * @author chama
 */
public class TransactionService {

    private static TransactionService instance;
    private final DataSourceWrapper operationDataSourceWrapper;
    private final DataSourceWrapper accountDataSourceWrapper;
//    private static final Logger LOGGER = Logger.getLogger(TransactionService.class);

    public TransactionService() throws SQLException {

//        
        this.operationDataSourceWrapper = ConnectionService.getInstance().getOperationDataSourceWrapper();
        this.accountDataSourceWrapper = ConnectionService.getInstance().getAccuntDataSourceWrapper();

    }

    public static TransactionService getInstance() throws SQLException {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public Integer saveGrn(Grn grn) {

        Connection operaConnection = null;
        Connection accConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accConnection.setAutoCommit(false);
            // Execute a query to create statment
            List<GrnDetail> grnDetail = OperationService.getGrnDetail(grn.getIndexNo(), operaConnection);
            if (grnDetail.isEmpty()) {
                throw new RuntimeException("Grn Detail was Empty !");
            }

            HashMap<Integer, Integer> supplierMap = new HashMap<>();
            TTypeIndexDetail typeIndexDetail = OperationService.CheckTypeIndexDetail(Constant.SUPPLIER, grn.getSupNo(), operaConnection);
            if (typeIndexDetail.getType() == null) {
                //type index detail save with supplier
                supplierMap = AccountService.saveSupplier(grn, accConnection);

                Integer typeIndexId = OperationService.saveTypeIndexDetail(grn.getSupNo(), Constant.SUPPLIER, supplierMap.get(1), supplierMap.get(2), operaConnection);

                if (typeIndexId < 0) {
                    throw new RuntimeException("Type Index detail save fail !");
                }
                System.out.println("New Supplier ( " + grn.getSupName() + " ) Save Success !");

            } else {
                supplierMap.put(1, typeIndexDetail.getAccountRefId());
                supplierMap.put(2, typeIndexDetail.getAccountIndex());
                AccountService.updateSupplier(grn, supplierMap, accConnection);

            }
//          save grn
            Integer grnIndex = AccountService.saveGrn(grn, supplierMap, accConnection);

//          save acc ledger    
            HashMap<Integer, Object> ledgerMap=AccountService.saveAccountLedgerWithSupplierNbtVat(grn,supplierMap,grnIndex,accConnection);
            
//          save item
            HashMap<Integer, Integer> map = new HashMap<>();
            for (GrnDetail detail : grnDetail) {
//                type index check from item
                TTypeIndexDetail typeIndexDetailItem = OperationService.CheckTypeIndexDetail(Constant.ITEM, detail.getItemNo(), operaConnection);
                if (typeIndexDetailItem.getType() == null) {

                    map = AccountService.saveItem(detail, accConnection);
                    //type index detail save with item
                    Integer typeIndexId = OperationService.saveTypeIndexDetail(detail.getItemNo(), Constant.ITEM, map.get(1), map.get(2), operaConnection);

                    if (typeIndexId < 0) {
                        throw new RuntimeException("Type Index detail save fail !");
                    }
                    System.out.println("New Item( " + detail.getItemName() + " ) Save Success !");

                } else {
                    map.put(1, typeIndexDetailItem.getAccountRefId());
                    map.put(2, typeIndexDetailItem.getAccountIndex());
                    AccountService.updateItem(detail, map, accConnection);
                }
                detail.setGrn(grnIndex);

                String grnNo = AccountService.saveGrnDetail(detail, grn.getBranch(), map, accConnection);
                if (null == grnNo) {
                    throw new RuntimeException("Grn Number was empty or Grn save failed !");
                }
                
                //save acc ledger with item
                Integer ledgerIndex= AccountService.saveAccLedgerWithItem(detail, grn.getBranch(), map,ledgerMap, accConnection);
                

            }
            Integer saveSupplierLedger = AccountService.saveSupplierLedger(grn, grnIndex, supplierMap.get(2), accConnection);
            if (saveSupplierLedger <= 0) {
                throw new RuntimeException("Supplier Ledger Save fail !");
            }

            Integer masterId = OperationService.updateGrn(grn, operaConnection);

            if (masterId > 0) {
                System.out.println(grn.getGrnNo() + " - " + grn.getFinalValue() + " - " + grn.getSupName() + " Grn Save Success !");
            } else {
                throw new RuntimeException("Grn Update fail !");
            }
            
            // save account ledger
            
            //commit
            operaConnection.commit();
            accConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accConnection != null) {
                    accConnection.rollback();
                }
                System.out.println("Transactions Rollbacked success !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }

        return grn.getIndexNo();
    }

}
