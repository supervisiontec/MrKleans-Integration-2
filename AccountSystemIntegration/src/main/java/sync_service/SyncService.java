/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sync_service;

import java.sql.SQLException;
import java.util.ArrayList;
import model.operation_model.Grn;
import service.OperationService;
import service.TransactionService;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SyncService {

    private final OperationService operationService;

    private static SyncService instance;
//    private static final Logger LOGGER = Logger.getLogger(SyncService.class);

    public static SyncService getInstance() throws SQLException {
        if (instance == null) {
            instance = new SyncService();
        }

        return instance;
    }

    private SyncService() throws SQLException {
        this.operationService = new OperationService();
    }

    public void executeGrn(String date) throws SQLException {

        ArrayList<Grn> grnList = operationService.getNotCheckGrnList(date);
        if (grnList.isEmpty()) {
            System.out.println("Integration Grn is empty!");
        } else {
            System.out.println("Finded " + grnList.size() + " Grn to Integrate with account System !");
        }
        for (Grn grn : grnList) {
            TransactionService.getInstance().saveGrn(grn);
        }
    }

    public void executeInvoice(String date) throws SQLException {
//        ArrayList<Grn> invoiceList = operationService.getNotCheckGrnList(date);
//        if (invoiceList.isEmpty()) {
//            System.out.println("Integration Invoice is empty!");
//        } else {
//            System.out.println("Finded " + invoiceList.size() + " Invoice to Integrate with account System !");
//        }
//        for (Grn grn : invoiceList) {
//            TransactionService.getInstance().saveGrn(grn);
//        }

    }
}
