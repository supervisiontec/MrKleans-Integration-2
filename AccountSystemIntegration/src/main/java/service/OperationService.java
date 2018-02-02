/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import controller.OperationController;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.operation_model.MMaster;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;
import model.operation_model.TTypeIndexDetail;

/**
 *
 * @author chama
 */
public class OperationService {

    static List<GrnDetail> getGrnDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getGrnDetail(indexNo, operaConnection);
    }

    static Integer saveTypeIndexDetail(String masterRef, String type, Integer accIndex,Integer accountIndex, Connection operaConnection) throws SQLException {
        TTypeIndexDetail typeIndexDetail = new TTypeIndexDetail();
        typeIndexDetail.setAccountRefId(accIndex);
        typeIndexDetail.setMasterRefId(masterRef);
        typeIndexDetail.setAccountIndex(accountIndex);
        typeIndexDetail.setType(type);
        return OperationController.getInstance().saveTypeIndexDetail(typeIndexDetail,operaConnection);
    }

    static TTypeIndexDetail CheckTypeIndexDetail(String type, String typeIndex, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().CheckTypeIndexDetail(type, typeIndex,operaConnection);
    }

    public ArrayList<Grn> getNotCheckGrnList(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckGrnList(date);
    }

    public static Integer updateGrn(Grn grn,Connection connection) throws SQLException {
        return OperationController.getInstance().updateGrn(grn,connection);
    }
}
