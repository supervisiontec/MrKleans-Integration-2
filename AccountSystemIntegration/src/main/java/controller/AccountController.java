/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import common.Constant;
import db_connections.DataSourceWrapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import model.account_model.MAccAccount;
import model.account_model.MBranch;
import model.account_model.MClient;
import model.account_model.MSupplier;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;

/**
 *
 * @author 'Kasun Chamara'
 */
public class AccountController {

    private static AccountController instance;

    public AccountController() throws SQLException {
    }

    public static AccountController getInstance() throws SQLException {
        if (instance == null) {
            instance = new AccountController();
        }
        return instance;
    }

    public static Integer getSubAccountOf(String name, Connection connection) throws SQLException {
        String query = "select m_acc_setting.acc_account\n"
                + "from m_acc_setting where m_acc_setting.name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public List<MAccAccount> getSubAccountOfList(Integer subAccOf, Connection connection) throws SQLException {
        String query = "select m_acc_account.`*`\n"
                + "from m_acc_account\n"
                + "where m_acc_account.sub_account_of=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, subAccOf);
        ResultSet rst = preparedStatement.executeQuery();
        ArrayList<MAccAccount> list = new ArrayList<>();
        while (rst.next()) {
            MAccAccount mAccAccount = new MAccAccount();
            mAccAccount.setIndexNo(rst.getInt(1));
            mAccAccount.setName(rst.getString(2));
            mAccAccount.setLevel(rst.getString(3));
            mAccAccount.setAccCode(rst.getString(4));
            mAccAccount.setCop(rst.getBoolean(5));
            mAccAccount.setAccMain(rst.getInt(6));
            mAccAccount.setUser(rst.getInt(7));
            mAccAccount.setAccType(rst.getString(8));
            mAccAccount.setSubAccountOf(rst.getInt(9));
            mAccAccount.setIsAccAccount(rst.getBoolean(10));
            mAccAccount.setDescription(rst.getString(11));
            mAccAccount.setSubAccountCount(rst.getInt(12));
            list.add(mAccAccount);
        }
        return list;
    }

    public MAccAccount getSubAccount(Integer subAccountOf, Connection connection) throws SQLException {
        String query = "select m_acc_account.`*`\n"
                + "from m_acc_account\n"
                + "where m_acc_account.index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, subAccountOf);
        ResultSet rst = preparedStatement.executeQuery();
        MAccAccount mAccAccount = new MAccAccount();
        if (rst.next()) {
            mAccAccount.setIndexNo(rst.getInt(1));
            mAccAccount.setName(rst.getString(2));
            mAccAccount.setLevel(rst.getString(3));
            mAccAccount.setAccCode(rst.getString(4));
            mAccAccount.setCop(rst.getBoolean(5));
            mAccAccount.setAccMain(rst.getInt(6));
            mAccAccount.setUser(rst.getInt(7));
            mAccAccount.setAccType(rst.getString(8));
            mAccAccount.setSubAccountOf(rst.getInt(9));
            mAccAccount.setIsAccAccount(rst.getBoolean(10));
            mAccAccount.setDescription(rst.getString(11));
            mAccAccount.setSubAccountCount(rst.getInt(12));
            return mAccAccount;
        }
        return null;
    }

    public int updateAccAccount(MAccAccount mAccAccount, Connection connection) throws SQLException {
        String insertSql = "UPDATE m_acc_account set name=? , `level`=? , acc_code=? , cop=? , acc_main=? , `user`=?\n"
                + ", acc_type=? , sub_account_of=? , is_acc_account=? , description=? , sub_account_count=?\n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, mAccAccount.getName());
        preparedStatement.setString(2, mAccAccount.getLevel());
        preparedStatement.setString(3, mAccAccount.getAccCode());
        preparedStatement.setBoolean(4, mAccAccount.getCop());
        preparedStatement.setInt(5, mAccAccount.getAccMain());
        preparedStatement.setInt(6, mAccAccount.getUser());
        preparedStatement.setString(7, mAccAccount.getAccType());
        preparedStatement.setInt(8, mAccAccount.getSubAccountOf());
        preparedStatement.setBoolean(9, mAccAccount.getIsAccAccount());
        preparedStatement.setString(10, mAccAccount.getDescription());
        preparedStatement.setInt(11, mAccAccount.getSubAccountCount());
        preparedStatement.setInt(12, mAccAccount.getIndexNo());

        return preparedStatement.executeUpdate();

    }

    public int saveAccAccount(MAccAccount mAccAccount, Connection connection) throws SQLException {
        String insertSql = "insert into m_acc_account (name,`level`,acc_code,cop,acc_main,`user`\n"
                + ",acc_type,sub_account_of,is_acc_account,description,sub_account_count)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, mAccAccount.getName());
        preparedStatement.setString(2, mAccAccount.getLevel());
        preparedStatement.setString(3, mAccAccount.getAccCode());
        preparedStatement.setBoolean(4, mAccAccount.getCop());
        preparedStatement.setInt(5, mAccAccount.getAccMain());
        preparedStatement.setInt(6, mAccAccount.getUser());
        preparedStatement.setString(7, mAccAccount.getAccType());
        preparedStatement.setInt(8, mAccAccount.getSubAccountOf());
        preparedStatement.setBoolean(9, mAccAccount.getIsAccAccount());
        preparedStatement.setString(10, mAccAccount.getDescription());
        preparedStatement.setInt(11, mAccAccount.getSubAccountCount());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveClientMaster(MClient client, Connection connection) throws SQLException {
        String insertSql = "insert into m_client (acc_account,branch,date,is_new,mobile,name,resident)\n"
                + " values (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, client.getAccAccount());
        preparedStatement.setInt(2, client.getBranch());
        preparedStatement.setString(3, client.getDate());
        preparedStatement.setBoolean(4, client.isIsNew());
        preparedStatement.setString(5, client.getMobile());
        preparedStatement.setString(6, client.getName());
        preparedStatement.setString(7, client.getResident());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveSupplierMaster(MSupplier supplier, Connection connection) throws SQLException {
        String insertSql = "insert into m_supplier (acc_account,name,contact_name,contact_no,type)\n"
                + " values (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, supplier.getAccAccount());
        preparedStatement.setString(2, supplier.getName());
        preparedStatement.setString(3, supplier.getContactName());
        preparedStatement.setString(4, supplier.getContactNo());
        preparedStatement.setString(5, supplier.getType());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveGrn(Grn grn, HashMap<Integer, Integer> map, Connection connection) throws SQLException {
        String insertSql = "insert into t_grn (supplier,number,date,amount,ref_number,branch,nbt,nbt_value,vat,vat_value,\n"
                + "grand_amount,status,type,is_nbt,is_vat,credit_period)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, map.get(2));
        preparedStatement.setString(2, grn.getGrnNo());
        preparedStatement.setString(3, grn.getEnterDate());
        preparedStatement.setBigDecimal(4, grn.getTotalValue());
        preparedStatement.setString(5, grn.getRefNo());
        preparedStatement.setInt(6, grn.getBranch());
        preparedStatement.setBigDecimal(7, grn.getNbt());
        preparedStatement.setBigDecimal(8, grn.getNbtValue());
        preparedStatement.setBigDecimal(9, grn.getVat());
        preparedStatement.setBigDecimal(10, grn.getVatValue());
        preparedStatement.setBigDecimal(11, grn.getFinalValue());
        preparedStatement.setString(12, Constant.STATUS_APPROVED);
        preparedStatement.setString(13, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setBoolean(14, (grn.getNbtValue().doubleValue() > 0));
        preparedStatement.setBoolean(15, (grn.getVatValue().doubleValue() > 0));
        preparedStatement.setInt(16, grn.getCreditPeriod());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public int saveGrnDetail(GrnDetail detail, Connection connection) throws SQLException {
        String insertSql = "insert into t_grn_item (grn,item,price,qty,value,discount,discount_value,net_value)\n"
                + " values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, detail.getGrn());
        preparedStatement.setInt(2, Integer.parseInt(detail.getItemNo()));
        preparedStatement.setBigDecimal(3, detail.getCost());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, detail.getValue());
        preparedStatement.setBigDecimal(6, detail.getDiscount());
        preparedStatement.setBigDecimal(7, detail.getDiscountValue());
        preparedStatement.setBigDecimal(8, detail.getNetValue());

        return preparedStatement.executeUpdate();

    }

    public Integer saveItemMaster(GrnDetail detail, Integer accAccountIndex, Connection connection) throws SQLException {
        String insertSql = "insert into m_item (name,barcode,print_description,cost_price,\n"
                + "type,re_order_max,re_order_min,account,sale_price_normal,sale_price_register,unit)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, detail.getItemBarcode());
        preparedStatement.setString(3, detail.getItemName());
        preparedStatement.setBigDecimal(4, detail.getCost());
        preparedStatement.setString(5, detail.getItemType());
        preparedStatement.setBigDecimal(6, detail.getReorderMax());
        preparedStatement.setBigDecimal(7, detail.getReorderMin());
        preparedStatement.setInt(8, accAccountIndex);
        preparedStatement.setBigDecimal(9, detail.getSalesPrice());
        preparedStatement.setBigDecimal(10, detail.getSalesPrice());
        preparedStatement.setString(11, detail.getItemUnit());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveItemUnitMaster(GrnDetail detail, Integer item, Connection connection) throws SQLException {

        String insertSql = "insert into m_item_units (item,name,unit,qty,sale_price_normal,sale_price_register,cost_price,item_unit_type)\n"
                + " values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, item);
        preparedStatement.setString(2, detail.getItemName());
        preparedStatement.setString(3, detail.getItemUnit());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, detail.getSalesPrice());
        preparedStatement.setBigDecimal(6, detail.getSalesPrice());
        preparedStatement.setBigDecimal(7, detail.getCost());
        preparedStatement.setString(8, Constant.ITEM_UNIT_MAIN);

        return preparedStatement.executeUpdate();

    }

    public ArrayList<MBranch> getBranchList(Connection connection) throws SQLException {
        String query = "select m_branch.* from m_branch";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rst = preparedStatement.executeQuery();
        ArrayList<MBranch> list = new ArrayList<MBranch>();
        while (rst.next()) {
            MBranch branch = new MBranch();
            branch.setIndexNo(rst.getInt(1));
            branch.setBranchCode(rst.getString(2));
            branch.setRegNumber(rst.getString(3));
            branch.setName(rst.getString(4));
            branch.setAddressLine1(rst.getString(5));
            branch.setAddressLine2(rst.getString(6));
            branch.setAddressLine3(rst.getString(7));
            branch.setTelephoneNumber(rst.getString(8));
            branch.setColor(rst.getString(9));
            branch.setType(rst.getString(10));
            list.add(branch);
        }
        return list;
    }

    public int saveReOrderLevel(Integer item, Integer branch, BigDecimal reorderMax, BigDecimal reorderMin, Connection connection) throws SQLException {
        String insertSql = "insert into m_re_order_level (item,branch,re_order_max,re_order_min)\n"
                + " values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, item);
        preparedStatement.setInt(2, branch);
        preparedStatement.setBigDecimal(3, reorderMax);
        preparedStatement.setBigDecimal(4, reorderMin);
        return preparedStatement.executeUpdate();

    }

    public Integer findStock(Integer branch, String type, Connection connection) throws SQLException {
        String query = "select m_store.index_no from m_store\n"
                + "where m_store.branch =? and m_store.`type`=? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        preparedStatement.setString(2, type);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer saveStock(Integer branch, String type, Connection connection) throws SQLException {
        String insertSql = "insert into m_store (name,number,type,branch)\n"
                + " values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, type);
        preparedStatement.setInt(2, 1);
        preparedStatement.setString(3, type);
        preparedStatement.setInt(4, branch);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public int saveStockLedger(GrnDetail detail, Integer branch, Integer saveStock, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_stock_ledger (item,store,date,in_qty,out_qty,avarage_price_in,avarage_price_out,\n"
                + "form_index_no,form,branch,type)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(detail.getItemNo()));
        preparedStatement.setInt(2, saveStock);
        preparedStatement.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, new BigDecimal(0));
        preparedStatement.setBigDecimal(6, detail.getCost());
        preparedStatement.setBigDecimal(7, new BigDecimal(0));
        preparedStatement.setInt(8, detail.getGrn());
        preparedStatement.setString(9, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(10, branch);
        preparedStatement.setString(11, Constant.SYSTEM_INTEGRATION_GRN);

        return preparedStatement.executeUpdate();
    }

    public void updateSupplierMaster(Grn grn, HashMap<Integer, Integer> supplierMap, Connection connection) throws SQLException {
        String insertSql = "UPDATE m_supplier set name=? , contact_name=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, grn.getSupName());
        preparedStatement.setString(2, grn.getSupName());
        preparedStatement.setInt(3, supplierMap.get(2));

        preparedStatement.executeUpdate();
    }

    public void updateSupplierAccount(Grn grn, HashMap<Integer, Integer> supplierMap, Connection connection) throws SQLException {
        String insertSql = "UPDATE m_acc_account set name=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, grn.getSupName());
        preparedStatement.setInt(2, supplierMap.get(1));

        preparedStatement.executeUpdate();
    }

    public void updateItemMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_item set name=? , print_description=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, detail.getItemName());
        preparedStatement.setInt(3, map.get(2));

        preparedStatement.executeUpdate();
    }

    public void updateItemAccount(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_acc_account set name=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setInt(2, map.get(1));

        preparedStatement.executeUpdate();
    }

    public void updateItemUnitMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_item_units set name=?\n"
                + "WHERE item_unit_type=? and item=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, Constant.ITEM_UNIT_MAIN);
        preparedStatement.setInt(3, map.get(2));

        preparedStatement.executeUpdate();
    }

    public Integer saveSupplierLedger(Grn grn, Integer grnIndex, Integer supplierIndex, Connection connection) throws SQLException {
        String insertSql = "insert into t_supplier_ledger (branch,date,form_name,grn,supplier,credit_amount,debit_amount,\n"
                + "ref_number,is_delete)\n"
                + " values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, grn.getBranch());
        preparedStatement.setString(2, grn.getEnterDate());
        preparedStatement.setString(3, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(4, grnIndex);
        preparedStatement.setInt(5, supplierIndex);
        preparedStatement.setBigDecimal(6, grn.getFinalValue());
        preparedStatement.setBigDecimal(7, new BigDecimal(0));
        preparedStatement.setInt(8, grnIndex);
        preparedStatement.setBoolean(9, false);

        return preparedStatement.executeUpdate();
    }

    public HashMap<Integer, Object> saveSupplierAccountLedger(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, Connection accConnection) throws SQLException {
//        get branch
        MBranch branch = getBranch(grn.getBranch(), accConnection);
        if (branch == null) {
            throw new RuntimeException("Can't find Branch !");
        }

//        getNumber
        Integer nextNumber = getNextNumber(grn.getBranch(), Constant.SYSTEM_INTEGRATION_GRN, accConnection);
        if (nextNumber < 0) {
            throw new RuntimeException("Next number generate fail !");
        }

//        getDeleteFrfNumber
        Integer deleteRefNumber = getDeleteRefNumber(accConnection);
        if (deleteRefNumber < 0) {
            throw new RuntimeException("Delete Ref number generate fail !");
        }

        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, nextNumber);
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_GRN + "/" + branch.getBranchCode() + "/" + nextNumber);
        preparedStatement.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, grn.getBranch());
        preparedStatement.setInt(7, grn.getBranch());
        preparedStatement.setInt(8, 1);
        preparedStatement.setBigDecimal(9, new BigDecimal(0));
        preparedStatement.setBigDecimal(10, grn.getFinalValue());
        preparedStatement.setInt(11, supplierMap.get(1));
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setString(13, grnIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(15, grnIndex);
        preparedStatement.setInt(16, deleteRefNumber);
        preparedStatement.setString(17, "System Integration GRN save Supplier Account !");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, true);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            updateReConcileGroup(resultSet.getInt(1), accConnection);
            HashMap<Integer, Object> map = new HashMap<>();
            map.put(1, nextNumber);
            map.put(2, deleteRefNumber);
            map.put(3, resultSet.getInt(1));
            map.put(4, branch.getBranchCode());
            map.put(5, grn.getBranch());
            return map;
        }
        throw new RuntimeException("Supplier Ledger Account Save Fail !");
    }

    public MBranch getBranch(int branch, Connection connection) throws SQLException {
        String query = "select m_branch.* from m_branch where m_branch.index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        ResultSet rst = preparedStatement.executeQuery();
        MBranch branchModel = new MBranch();
        if (rst.next()) {
            branchModel.setIndexNo(rst.getInt(1));
            branchModel.setBranchCode(rst.getString(2));
            branchModel.setRegNumber(rst.getString(3));
            branchModel.setName(rst.getString(4));
            branchModel.setAddressLine1(rst.getString(5));
            branchModel.setAddressLine2(rst.getString(6));
            branchModel.setAddressLine3(rst.getString(7));
            branchModel.setTelephoneNumber(rst.getString(8));
            branchModel.setColor(rst.getString(9));
            branchModel.setType(rst.getString(10));
        }
        return branchModel;
    }

    private Integer getNextNumber(int branch, String type, Connection connection) throws SQLException {
        String query = "select ifnull( max(t_acc_ledger.number)+1,1) as number\n"
                + "from t_acc_ledger\n"
                + "where t_acc_ledger.current_branch=? and t_acc_ledger.`type` =?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        preparedStatement.setString(2, type);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private Integer getDeleteRefNumber(Connection connection) throws SQLException {
        String query = "select ifnull( max(t_acc_ledger.delete_ref_no)+1,1) as number\n"
                + "from t_acc_ledger";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private void updateReConcileGroup(int reconcileGroup, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE t_acc_ledger set reconcile_group=?\n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, reconcileGroup);
        preparedStatement.setInt(2, reconcileGroup);

        preparedStatement.executeUpdate();
    }

    public Integer saveAccLedgerItem(GrnDetail detail, int branch, HashMap<Integer, Integer> map, HashMap<Integer, Object> ledgerMap, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_GRN + "/" + ledgerMap.get(4).toString() + "/" + Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, Integer.parseInt(ledgerMap.get(5).toString()));
        preparedStatement.setInt(7, Integer.parseInt(ledgerMap.get(5).toString()));
        preparedStatement.setInt(8, 1);
        preparedStatement.setBigDecimal(9, detail.getNetValue());
        preparedStatement.setBigDecimal(10, new BigDecimal(0));
        preparedStatement.setInt(11, map.get(1));
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setString(13, map.get(2) + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(15, Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setInt(16, Integer.parseInt(ledgerMap.get(2).toString()));
        preparedStatement.setString(17, "System Integration GRN save Item !");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            updateReConcileGroup(resultSet.getInt(1), accConnection);
            return resultSet.getInt(1);
        }
        throw new RuntimeException("Supplier Ledger Account Save Fail !");
    }
    
    public static void saveVatForLedger(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, Connection accConnection) throws SQLException {
        getCustomerSubAccountOf("a", accConnection);
    }

    public static void saveNbtForLedger(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, Connection accConnection) {
        
    }

}
