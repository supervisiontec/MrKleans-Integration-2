/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import common.Constant;
import controller.AccountController;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.account_model.MAccAccount;
import model.account_model.MBranch;
import model.account_model.MClient;
import model.operation_model.MMaster;
import model.account_model.MSupplier;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;

/**
 *
 * @author chama
 */
public class AccountService {

    static Integer saveCustomer(MMaster master, Connection connection) throws SQLException {
        //save to m_acc_account
        Integer customerSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.CUSTOMER_SUB_ACCOUNT_OF, connection);
        if (customerSubAccountOf < 0 || customerSubAccountOf == null) {
            throw new RuntimeException("Customer Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Customer Save !");
        mAccAccount.setIsAccAccount(true);
        mAccAccount.setName(master.getOther1() + " " + master.getName());
        mAccAccount.setSubAccountOf(customerSubAccountOf);
        mAccAccount.setUser(1);
        Integer accAccountIndex = saveAccAccount(mAccAccount, connection);
        if (accAccountIndex < 0) {
            throw new RuntimeException("Customer account save fail !");
        }
        //save m_client
        MClient client = new MClient();
        client.setAccAccount(accAccountIndex);
        client.setBranch(master.getBranch());
        client.setDate(master.getEnterDate());
        client.setIsNew(true);
        client.setMobile(master.getOther2());
        client.setName(master.getName());
        client.setResident(master.getOther1());

        AccountController.getInstance().saveClientMaster(client, connection);

        return accAccountIndex;
    }

    static HashMap<Integer, Integer> saveSupplier(Grn grn, Connection connection) throws SQLException {
        Integer supplierSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.SUPPLIER_SUB_ACCOUNT_OF, connection);
        if (supplierSubAccountOf < 0 || supplierSubAccountOf == null) {
            throw new RuntimeException("Supplier Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Customer");
        mAccAccount.setIsAccAccount(true);
        mAccAccount.setName(grn.getSupName());
        mAccAccount.setSubAccountOf(supplierSubAccountOf);
        mAccAccount.setUser(1);
        Integer accAccountIndex = saveAccAccount(mAccAccount, connection);
        if (accAccountIndex < 0) {
            throw new RuntimeException("Supplier account save fail !");
        }

        MSupplier supplier = new MSupplier();
        supplier.setAccAccount(accAccountIndex);
        supplier.setContactName(grn.getSupName());
        supplier.setName(grn.getSupName());
        supplier.setContactNo(null);
        supplier.setType(Constant.SUPPLIER);

        Integer saveSupplierMaster = AccountController.getInstance().saveSupplierMaster(supplier, connection);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, accAccountIndex);
        map.put(2, saveSupplierMaster);
        return map;
    }

    public static Integer saveAccAccount(MAccAccount accAccount, Connection connection) throws SQLException {

        MAccAccount subAccountOf = AccountController.getInstance().getSubAccount(accAccount.getSubAccountOf(), connection);
        accAccount.setLevel(Integer.parseInt(subAccountOf.getLevel()) + 1 + "");
        accAccount.setAccMain(subAccountOf.getAccMain());
        accAccount.setSubAccountCount(0);
        accAccount.setAccMain(subAccountOf.getAccMain());
        accAccount.setAccCode(subAccountOf.getSubAccountCount() == 0 ? subAccountOf.getAccCode()
                + ".01" : subAccountOf.getAccCode() + (subAccountOf.getSubAccountCount() <= 9 ? (".0"
                + (subAccountOf.getSubAccountCount() + 1)) : subAccountOf.getSubAccountCount() <= 99 ? "." + (subAccountOf.getSubAccountCount() + 1)
                : getUpdate99(subAccountOf.getSubAccountCount(), accAccount.getSubAccountOf(), connection)));

        subAccountOf.setIsAccAccount(false);
        subAccountOf.setSubAccountCount(subAccountOf.getSubAccountCount() + 1);
        Integer index = AccountController.getInstance().updateAccAccount(subAccountOf, connection);
        if (index > 0) {
            return AccountController.getInstance().saveAccAccount(accAccount, connection);
        }
        return -1;
    }

    private static String getUpdate99(Integer count, Integer subAccOf, Connection connection) throws SQLException {
        //update
        if (count == 99) {

            List<MAccAccount> findBySubAccountOf = AccountController.getInstance().getSubAccountOfList(subAccOf, connection);
            for (MAccAccount mAccAccount : findBySubAccountOf) {
                Integer last = Integer.parseInt(mAccAccount.getLevel()) - 1;
                String[] split = new String[3];
                split = mAccAccount.getAccCode().split("\\.");

                int lastNo = Integer.parseInt(split[last]);
                if (lastNo < 10) {
                    split[last] = "00" + lastNo;
                } else if (lastNo < 100) {
                    split[last] = "0" + lastNo;

                }
//          
                String accCode = String.join(".", split);
                mAccAccount.setAccCode(accCode);
                AccountController.getInstance().updateAccAccount(mAccAccount, connection);
            }

        }
        //return number
        int lastCount = count + 1;
        return "." + lastCount;
    }

    static String saveGrnDetail(GrnDetail detail, Integer branch, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        detail.setItemNo(map.get(2) + "");
        AccountController.getInstance().saveGrnDetail(detail, accConnection);

        if (detail.getItemType().equals(Constant.ITEM_STOCK)) {
            saveStockLedger(detail, branch, accConnection);
        }

        return detail.getGrn() + "";

//        TSupplierLedger supplierLedger = new TSupplierLedger();
//        supplierLedger.setBranch(grn.getBranch());
//        supplierLedger.setCreditAmount(grn.getBalanceAmount());
//        supplierLedger.setDate(grn.getDate());
//        supplierLedger.setDebitAmount(new BigDecimal(0));
//        supplierLedger.setFormName(Constant.FORM_DIRECT_GRN);
//        supplierLedger.setGrn(saveObject.getIndexNo());
//        supplierLedger.setIsDelete(false);
//        supplierLedger.setPayment(null);
//        supplierLedger.setRefNumber(null);
//        supplierLedger.setReturn1(null);
//        supplierLedger.setSupplier(grn.getSupplier());
//
//        supplierLedgerRepository.save(supplierLedger);
    }

    static HashMap<Integer, Integer> saveItem(GrnDetail detail, Connection connection) throws SQLException {
        Integer itemSubAccountOf = -1;

        switch (detail.getItemType()) {
            case Constant.ITEM_STOCK:
                itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ITEM_SUB_ACCOUNT_OF, connection);
                break;
            case Constant.ITEM_SERVICE:
                itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.SERVICE_ITEM_SUB_ACCOUNT_OF, connection);
                break;
            case Constant.ITEM_NON_STOCK:
                itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.SERVICE_ITEM_SUB_ACCOUNT_OF, connection);
                break;
            default:
                break;
        }
        if (itemSubAccountOf < 0) {
            throw new RuntimeException("Item Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Item");
        mAccAccount.setIsAccAccount(true);
        mAccAccount.setName(detail.getItemName());
        mAccAccount.setSubAccountOf(itemSubAccountOf);
        mAccAccount.setUser(1);
        Integer accAccountIndex = saveAccAccount(mAccAccount, connection);
        if (accAccountIndex < 0) {
            throw new RuntimeException("item account save fail !");
        }

//
        Integer saveItemMaster = AccountController.getInstance().saveItemMaster(detail, accAccountIndex, connection);
        Integer saveItemUnit = AccountController.getInstance().saveItemUnitMaster(detail, saveItemMaster, connection);
        if (saveItemUnit <= 0) {
            throw new RuntimeException("Item Unit save fail !");
        }

        if (Constant.ITEM_STOCK.equals(detail.getItemType()) || Constant.ITEM_NON_STOCK.equals(detail.getItemType())) {
            saveReOrderLevel(saveItemMaster, detail, connection);
        }

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, accAccountIndex);
        hashMap.put(2, saveItemMaster);
        return hashMap;
    }

    static Integer saveGrn(Grn grn, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        Integer grnIndex = AccountController.getInstance().saveGrn(grn, map, accConnection);
        if (grnIndex < 0) {
            throw new RuntimeException("Grn Save fail !");
        }
        return grnIndex;
    }

    private static void saveReOrderLevel(Integer item, GrnDetail detail, Connection connection) throws SQLException {
        ArrayList<MBranch> branchList = getBranchList(connection);
        for (MBranch branch : branchList) {
            int saveReOrderLevel = AccountController.getInstance().saveReOrderLevel(item, branch.getIndexNo(), detail.getReorderMax(), detail.getReorderMin(), connection);
            if (saveReOrderLevel < 0) {
                throw new RuntimeException("ReOrder Level Save Fail !");
            }
        }
    }

    public static ArrayList<MBranch> getBranchList(Connection connection) throws SQLException {
        return AccountController.getInstance().getBranchList(connection);
    }

    private static void saveStockLedger(GrnDetail detail, Integer branch, Connection accConnection) throws SQLException {
        Integer saveStock = saveStock(branch, accConnection);

        AccountController.getInstance().saveStockLedger(detail, branch, saveStock, accConnection);

    }

    private static Integer saveStock(Integer branch, Connection accConnection) throws SQLException {
        Integer stockIndex = AccountController.getInstance().findStock(branch, Constant.STOCK_MAIN, accConnection);
        if (stockIndex < 0) {
            //save new default stock
            stockIndex = AccountController.getInstance().saveStock(branch, Constant.STOCK_MAIN, accConnection);
            if (stockIndex < 0) {
                throw new RuntimeException("Default Stock Save Fail !");
            }
        }
        return stockIndex;

    }

    static void updateSupplier(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        updateSupplierMaster(grn, supplierMap, accConnection);
        updateSupplierAccount(grn, supplierMap, accConnection);
    }

    private static void updateSupplierMaster(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateSupplierMaster(grn, supplierMap, accConnection);

    }

    private static void updateSupplierAccount(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateSupplierAccount(grn, supplierMap, accConnection);

    }

    static void updateItem(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        updateItemMaster(detail, map, accConnection);
        updateItemAccount(detail, map, accConnection);
        updateItemUnitMaster(detail, map, accConnection);

    }

    private static void updateItemMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemMaster(detail, map, accConnection);

    }

    private static void updateItemAccount(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemAccount(detail, map, accConnection);

    }

    private static void updateItemUnitMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemUnitMaster(detail, map, accConnection);

    }

    static Integer saveSupplierLedger(Grn grn, Integer grnIndex, Integer supplierIndex, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveSupplierLedger(grn, grnIndex, supplierIndex, accConnection);

    }

    static HashMap<Integer, Object> saveAccountLedgerWithSupplierNbtVat(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex,Connection accConnection) throws SQLException {
        HashMap<Integer, Object> ledgerMap=AccountController.getInstance().saveSupplierAccountLedger(grn, supplierMap,grnIndex, accConnection);
        if (grn.getNbtValue().doubleValue()>0) {
            //save nbt account
            AccountController.saveNbtForLedger(grn, supplierMap,grnIndex, accConnection);
        }
        if (grn.getVatValue().doubleValue()>0) {
            //save vat account
            AccountController.saveVatForLedger(grn, supplierMap,grnIndex, accConnection);
            
        }
        return ledgerMap;
    }

    static Integer saveAccLedgerWithItem(GrnDetail detail, int branch, HashMap<Integer, Integer> map, HashMap<Integer, Object> ledgerMap, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveAccLedgerItem(detail,branch,map,ledgerMap,accConnection);
    }

}
