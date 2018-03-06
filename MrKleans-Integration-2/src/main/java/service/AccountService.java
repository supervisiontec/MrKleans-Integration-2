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
import model.account_model.MSupplier;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;
import model.account_model.TTypeIndexDetail;
import model.operation_model.Invoice;
import model.operation_model.InvoiceDetail;
import model.operation_model.Payment;
import model.operation_model.PaymentDetail;
import model.operation_model.PaymentInformation;
import model.operation_model.StockAdjustment;

/**
 *
 * @author chama
 */
public class AccountService {

    public static HashMap<Integer, Integer> saveSupplier(Grn grn, Connection connection) throws SQLException {
        Integer supplierSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.SUPPLIER_SUB_ACCOUNT_OF, connection);
        if (supplierSubAccountOf < 0 || supplierSubAccountOf == null) {
            throw new RuntimeException("Supplier Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Supplier");
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
                + ".01" : subAccountOf.getAccCode() + (subAccountOf.getSubAccountCount() < 9 ? (".0"
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
                String accCode = String.join(".", split);
                mAccAccount.setAccCode(accCode);
                AccountController.getInstance().updateAccAccount(mAccAccount, connection);
            }

        }
        //return number
        int lastCount = count + 1;
        return "." + lastCount;
    }

    public static String saveGrnDetail(GrnDetail detail, Grn grn, HashMap<Integer, Integer> map, Integer user, Connection accConnection) throws SQLException {
        detail.setItemNo(map.get(2) + "");
        AccountController.getInstance().saveGrnDetail(detail, accConnection);

        if (detail.getItemType().equals(Constant.ITEM_STOCK)) {
            saveStockLedger(detail, grn, accConnection);
        }

        return detail.getGrn() + "";
    }

    public static HashMap<Integer, Integer> saveItem(GrnDetail detail, Connection connection) throws SQLException {
        Integer itemSubAccountOf = -1;

        switch (detail.getItemType()) {
            case Constant.ITEM_STOCK:
                itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ITEM_SUB_ACCOUNT_OF, connection);
                break;
            case Constant.ITEM_NON_STOCK:
                itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.NON_STOCK_ITEM_SUB_ACCOUNT_OF, connection);
                break;
            default:
                break;
        }
        if (itemSubAccountOf < 0) {
            throw new RuntimeException("Item Sub Account of Setting was Empty !");
        }
//        MAccAccount mAccAccount = new MAccAccount();
//        mAccAccount.setIndexNo(null);
//        mAccAccount.setAccType("COMMON");
//        mAccAccount.setCop(false);
//        mAccAccount.setDescription("System Integoration new Item");
//        mAccAccount.setIsAccAccount(true);
//        mAccAccount.setName(detail.getItemName());
//        mAccAccount.setSubAccountOf(itemSubAccountOf);
//        mAccAccount.setUser(1);
//        Integer accAccountIndex = saveAccAccount(mAccAccount, connection);
//        if (accAccountIndex < 0) {
//            throw new RuntimeException("item account save fail !");
//        }

//
        Integer saveItemMaster = AccountController.getInstance().saveItemMaster(detail, itemSubAccountOf, connection);
        Integer saveItemUnit = AccountController.getInstance().saveItemUnitMaster(detail, saveItemMaster, connection);
        if (saveItemUnit <= 0) {
            throw new RuntimeException("Item Unit save fail !");
        }

        if (Constant.ITEM_STOCK.equals(detail.getItemType()) || Constant.ITEM_NON_STOCK.equals(detail.getItemType())) {
            if (detail.getReorderMax().doubleValue() > 0 || detail.getReorderMin().doubleValue() > 0) {
                saveReOrderLevel(saveItemMaster, detail, connection);
            }
        }

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, itemSubAccountOf);
        hashMap.put(2, saveItemMaster);
        return hashMap;
    }

    public static Integer saveGrn(Grn grn, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
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

    private static void saveStockLedger(GrnDetail detail, Grn grn, Connection accConnection) throws SQLException {
        Integer saveStock = saveStock(grn.getBranch(), accConnection);

        AccountController.getInstance().saveStockLedger(detail, grn, saveStock, accConnection);

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

    public static void updateSupplier(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        updateSupplierMaster(grn, supplierMap, accConnection);
        updateSupplierAccount(grn, supplierMap, accConnection);
    }

    private static void updateSupplierMaster(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateSupplierMaster(grn, supplierMap, accConnection);

    }

    private static void updateSupplierAccount(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateSupplierAccount(grn, supplierMap, accConnection);

    }

    public static void updateItem(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        updateItemMaster(detail, map, accConnection);
        updateItemUnitMaster(detail, map, accConnection);

    }

    private static void updateItemMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemMaster(detail, map, accConnection);
    }

    private static void updateItemUnitMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemUnitMaster(detail, map, accConnection);
    }

    public static Integer saveSupplierLedger(Grn grn, Integer grnIndex, Integer supplierIndex, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveSupplierLedger(grn, grnIndex, supplierIndex, accConnection);
    }

    public static HashMap<Integer, Object> saveAccountLedgerWithSupplierNbtVat(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, Integer user, Connection accConnection) throws SQLException {
        HashMap<Integer, Object> ledgerMap = AccountController.getInstance().saveSupplierAccountLedger(grn, supplierMap, grnIndex, user, accConnection);
        if (grn.getNbtValue().doubleValue() > 0) {
            //save nbt account
            AccountController.getInstance().saveNbtForLedger(grn, supplierMap, grnIndex, ledgerMap, user, accConnection);
        }
        if (grn.getVatValue().doubleValue() > 0) {
            //save vat account
            AccountController.getInstance().saveVatForLedger(grn, supplierMap, grnIndex, ledgerMap, user, accConnection);

        }
        return ledgerMap;
    }

    public static Integer saveAccLedgerWithItem(GrnDetail detail, int branch, HashMap<Integer, Integer> map, HashMap<Integer, Object> ledgerMap, Integer user, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveAccLedgerItem(detail, branch, map, ledgerMap, user, accConnection);
    }

    public static Integer saveTypeIndexDetail(String masterRef, String type, Integer accIndex, Integer accountIndex, Connection operaConnection) throws SQLException {
        TTypeIndexDetail typeIndexDetail = new TTypeIndexDetail();
        typeIndexDetail.setAccountRefId(accIndex);
        typeIndexDetail.setMasterRefId(masterRef);
        typeIndexDetail.setAccountIndex(accountIndex);
        typeIndexDetail.setType(type);
        return AccountController.getInstance().saveTypeIndexDetail(typeIndexDetail, operaConnection);
    }

    public static TTypeIndexDetail CheckTypeIndexDetail(String type, String typeIndex, Connection operaConnection) throws SQLException {
        return AccountController.getInstance().CheckTypeIndexDetail(type, typeIndex, operaConnection);
    }

    static HashMap<Integer, Integer> saveCustomer(Invoice invoice, Connection accConnection) throws SQLException {
        Integer supplierSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.CUSTOMER_SUB_ACCOUNT_OF, accConnection);
        if (supplierSubAccountOf < 0) {
            throw new RuntimeException("Customer Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Customer");
        mAccAccount.setIsAccAccount(true);
        mAccAccount.setName(invoice.getClientName() + "(" + invoice.getClientNo() + ")");
        mAccAccount.setSubAccountOf(supplierSubAccountOf);
        mAccAccount.setUser(1);
        Integer accAccountIndex = saveAccAccount(mAccAccount, accConnection);
        if (accAccountIndex < 0) {
            throw new RuntimeException("Supplier account save fail !");
        }

        MClient client = new MClient();
        client.setAccAccount(accAccountIndex);
        client.setName(invoice.getClientName());
        client.setBranch(invoice.getBranch());
        client.setResident(invoice.getClientRecident());

        Integer saveCustomerMaster = AccountController.getInstance().saveCustomerMaster(client, accConnection);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, accAccountIndex);
        map.put(2, saveCustomerMaster);
        return map;
    }

    static void updateCustomer(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        updateCustomerMaster(invoice, customerMap, accConnection);
        updateCustomerAccount(invoice, customerMap, accConnection);
    }

    private static void updateCustomerMaster(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateCustomerMaster(invoice, customerMap, accConnection);
    }

    private static void updateCustomerAccount(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateCustomerAccount(invoice, customerMap, accConnection);

    }

    static HashMap<Integer, Integer> saveInvoice(Invoice invoice, List<InvoiceDetail> invDetailList, HashMap<Integer, Integer> customerMap, Integer vehicle, Integer user, Connection accConnection) throws SQLException {
        HashMap<Integer, Integer> map = AccountController.getInstance().saveInvoice(invoice, invDetailList, customerMap, vehicle, user, accConnection);
        if (map.size() <= 0) {
            throw new RuntimeException("Grn Save fail !");
        }
        return map;
    }

    public static Integer saveVehicle(Invoice invoice, Integer customer, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveVehicleMaster(invoice, customer, accConnection);
    }

    public static void updateItemFromInvoice(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        updateItemMaster(detail, map, accConnection);
        updateItemUnitMaster(detail, map, accConnection);
    }

    private static void updateItemMaster(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemMaster(detail, map, accConnection);
    }

    private static void updateItemUnitMaster(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemUnitMaster(detail, map, accConnection);
    }

    static HashMap<Integer, Integer> saveItem(InvoiceDetail detail, Connection connection) throws SQLException {
        Integer itemSubAccountOf = -1;
//
        itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.SERVICE_ITEM_SUB_ACCOUNT_OF, connection);
//        if (itemSubAccountOf < 0) {
//            throw new RuntimeException("Item Sub Account of Setting was Empty !");
//        }
//        MAccAccount mAccAccount = new MAccAccount();
//        mAccAccount.setIndexNo(null);
//        mAccAccount.setAccType("COMMON");
//        mAccAccount.setCop(false);
//        mAccAccount.setDescription("System Integoration new Item");
//        mAccAccount.setIsAccAccount(true);
//        mAccAccount.setName(detail.getItemName());
//        mAccAccount.setSubAccountOf(itemSubAccountOf);
//        mAccAccount.setUser(1);
//        Integer accAccountIndex = saveAccAccount(mAccAccount, connection);
//        if (accAccountIndex < 0) {
//            throw new RuntimeException("item account save fail !");
//        }

        Integer saveItemMaster = AccountController.getInstance().saveItemMaster(detail, itemSubAccountOf, connection);
        if (saveItemMaster <= 0) {
            throw new RuntimeException("Item master save fail !");
        }
        Integer saveItemUnit = AccountController.getInstance().saveItemUnitMaster(detail, saveItemMaster, connection);
        if (saveItemUnit <= 0) {
            throw new RuntimeException("Item Unit save fail !");
        }

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, itemSubAccountOf);
        hashMap.put(2, saveItemMaster);
        hashMap.put(3, saveItemUnit);

        return hashMap;
    }

    static String saveInvoiceDetail(InvoiceDetail detail, Invoice invoice, HashMap<Integer, Integer> itemMap, HashMap<Integer, Integer> invoiceMap, Connection accConnection) throws SQLException {
        Integer saveStock = saveStock(invoice.getBranch(), accConnection);
        AccountController.getInstance().saveInvoiceDetail(detail, invoice, itemMap, invoiceMap, saveStock, accConnection);
        return invoice.getInvoiceNo();
    }

    static Integer savePayment(Payment payment, Connection accConnection) throws SQLException {
        return AccountController.getInstance().savePayment(payment, accConnection);
    }

    static void saveCustomerLedger(PaymentDetail paymentDetail1, Integer paymentIndex, Payment payment, TTypeIndexDetail customerTypeIndexDetail, HashMap<Integer, Object> numberMap, Integer user, Connection accConnection) throws SQLException {
        TTypeIndexDetail invTypeIndexDetail = CheckTypeIndexDetail(Constant.INVOICE, paymentDetail1.getInvoice() + "", accConnection);
        if (invTypeIndexDetail.getType() == null) {
            throw new RuntimeException("Can't find invoice (" + paymentDetail1.getInvoice() + ") from Account System !");
        }
        Integer saveCustomerLedger = AccountController.getInstance().saveCustomerLedger(paymentDetail1, paymentIndex, payment, invTypeIndexDetail, customerTypeIndexDetail, accConnection);
        if (saveCustomerLedger <= 0) {
            throw new RuntimeException("Customer Ledger Save fail !");
        }
        Integer saveAccountLedger = AccountController.getInstance().saveAccountLedgerCustomer(paymentDetail1, paymentIndex, payment, invTypeIndexDetail, customerTypeIndexDetail, numberMap, user, accConnection);
        if (saveAccountLedger <= 0) {
            throw new RuntimeException("Account Ledger Customer Save fail !");
        }
    }

    static HashMap<Integer, Object> getAccLedgerNumber(Integer branch, Connection accConnection) throws SQLException {
        return AccountController.getAccLedgerNumber(branch,Constant.SYSTEM_INTEGRATION_PAYMENT, accConnection);

    }

    static void savePaymentInformation(PaymentInformation paymentInformation, Integer paymentIndex, Payment payment, TTypeIndexDetail customerTypeIndexDetail, HashMap<Integer, Object> numberMap, Integer user, Connection accConnection) throws SQLException {
        //save payment information table
        Integer savePaymentInformation = savePaymentInformation(paymentInformation, paymentIndex, payment, accConnection);
        if (savePaymentInformation <= 0) {
            throw new RuntimeException("Payment Information Save Fail !");
        }
        // payment save acc ledger
        Integer savePaymentAccLedger = AccountController.getInstance().savePaymentAccLedger(paymentInformation, paymentIndex, payment, customerTypeIndexDetail, numberMap, user, accConnection);
    }

    private static Integer savePaymentInformation(PaymentInformation paymentInformation, Integer paymentIndex, Payment payment, Connection accConnection) throws SQLException {
        if (paymentInformation.getType().equals(Constant.PAYMENT_CASH)) {
            paymentInformation.setBank(null);
            paymentInformation.setBankBranch(null);
            paymentInformation.setCardReader(null);
            paymentInformation.setCardType(null);
            paymentInformation.setChequeDate(null);
            paymentInformation.setPayment(paymentIndex);

            return AccountController.getInstance().savePaymentInformation(paymentInformation, accConnection);

        } else if (paymentInformation.getType().equals(Constant.PAYMENT_CHEQUE)) {
            HashMap<Integer, Integer> map = saveBankAndBankBranch(paymentInformation, accConnection);
            paymentInformation.setBank(map.get(1) + "");
            paymentInformation.setBankBranch(map.get(2) + "");
            paymentInformation.setCardReader(null);
            paymentInformation.setCardType(null);
            paymentInformation.setPayment(paymentIndex);
            paymentInformation.setNumber(paymentInformation.getNumber());
            return AccountController.getInstance().savePaymentInformation(paymentInformation, accConnection);

        } else if (paymentInformation.getType().equals(Constant.PAYMENT_CARD)) {
            HashMap<Integer, Integer> map = saveBankAndBankBranch(paymentInformation, accConnection);
            Integer cardTypeIndex = saveCardType(paymentInformation.getCardType(), accConnection);
            paymentInformation.setBank(map.get(1) + "");
            paymentInformation.setBankBranch(map.get(2) + "");
            paymentInformation.setCardType(cardTypeIndex + "");
            paymentInformation.setPayment(paymentIndex);
            paymentInformation.setNumber(paymentInformation.getNumber());
            Integer cardReaderIndex = AccountController.getInstance().checkCardReader(paymentInformation.getCardReader(), payment.getBranch(), accConnection);
            if (cardReaderIndex <= 0) {
                throw new RuntimeException("Card Reader Setting is empty !");
            }
            return AccountController.getInstance().savePaymentInformation(paymentInformation, accConnection);

        } else {
            throw new RuntimeException("Payment type is Invalided. Available types are CASH,CHEQUE,CARD !");
        }
    }

    private static HashMap<Integer, Integer> saveBankAndBankBranch(PaymentInformation paymentInformation, Connection accConnection) throws SQLException {
        HashMap<Integer, Integer> map = new HashMap<>();
        map = AccountController.getInstance().checkBankAndBankBranch(paymentInformation, accConnection);
        if (map == null) {
            Integer bankIndex = AccountController.getInstance().checkBank(paymentInformation.getBank(), accConnection);
            if (bankIndex <= 0) {
                //save bank and branch
                return AccountController.getInstance().saveBankAndBankBranch(paymentInformation.getBank(), paymentInformation.getBankBranch(), accConnection);
            } else {
                //save only bank branch
                Integer bankBranchIndex = AccountController.getInstance().saveBankBranch(paymentInformation.getBankBranch(), bankIndex, accConnection);
                map.put(1, bankIndex);
                map.put(2, bankBranchIndex);
                return map;
            }
        } else {
            return map;
        }
    }

    private static Integer saveCardType(String cardType, Connection accConnection) throws SQLException {
        Integer checkCardType = AccountController.getInstance().checkCardType(cardType, accConnection);
        if (checkCardType > 0) {
            return checkCardType;
        }
        return AccountController.getInstance().saveCardType(cardType, accConnection);
    }

    static Integer checkLoginUser(String name, String pswd, Connection accConnection) throws SQLException {
        return AccountController.getInstance().checkLoginUser(name, pswd, accConnection);
    }

    static HashMap<Integer, Integer> saveItem(StockAdjustment adjustment, Connection connection) throws SQLException {
        Integer itemSubAccountOf = -1;

        itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ITEM_SUB_ACCOUNT_OF, connection);

        if (itemSubAccountOf < 0) {
            throw new RuntimeException("Item Sub Account of Setting was Empty !");
        }
        Integer saveItemMaster = AccountController.getInstance().saveItemMaster(adjustment, itemSubAccountOf, connection);
        Integer saveItemUnit = AccountController.getInstance().saveItemUnitMaster(adjustment, saveItemMaster, connection);
        if (saveItemUnit <= 0) {
            throw new RuntimeException("Item Unit save fail !");
        }

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, itemSubAccountOf);
        hashMap.put(2, saveItemMaster);
        return hashMap;
    }

    static int saveStockAdjustment(StockAdjustment adjustment, Connection accConnection) throws SQLException {
        return AccountController.saveStockAdjustment(adjustment, accConnection);
    }

    static Integer saveStockAdjustmentToAccount(StockAdjustment adjustment,Integer user,Integer formIndexNo, Connection accConnection) throws SQLException {
        Integer stockAccount = -1;
        Integer stockAdjustmentAccount = -1;

        stockAccount = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ITEM_SUB_ACCOUNT_OF, accConnection);
        stockAdjustmentAccount = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ADJUSTMENT_CONTROL_ACCOUNT, accConnection);

        if (stockAccount < 0) {
            throw new RuntimeException("Stock Account Setting was Empty !");
        }
        if (stockAdjustmentAccount < 0) {
            throw new RuntimeException("Stock Adjustment Account Setting was Empty !");
        }
        if (adjustment.getQty().doubleValue() > 0) {
            // plus qty     
            return AccountController.getInstance().saveStockAdjustmentToAccountPlus(adjustment,stockAccount,stockAdjustmentAccount,user,formIndexNo,accConnection);
        } else {
            // minus qty     
            return AccountController.getInstance().saveStockAdjustmentToAccountMinus(adjustment,stockAccount,stockAdjustmentAccount,user,formIndexNo,accConnection);

        }
    }

}
