/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import common.Constant;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import service.TransactionService;
import sync_service.SyncService;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SystemIntegrationSyncGUI extends javax.swing.JFrame {

    Integer loginUser = -1;

    public SystemIntegrationSyncGUI() {
        initComponents();
        ImageIcon imageIcon = new ImageIcon("./images/task.png");
        setIconImage(imageIcon.getImage());

        initOthers();
        lblProcess.setText("");

        try {
            getDetailCount(lblDate.getText());
        } catch (SQLException ex) {
            System.out.println("get detail count function not support !");
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeGrn(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executeGrn(date, loginUser1);
        getDetailCount(date);
    }

    private void executeInvoice(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executeInvoice(date, loginUser1);
        getDetailCount(date);
    }

    private void executePayment(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executePayment(date, loginUser1);
        getDetailCount(date);
    }

    private void executeStockAdjustment(String date, Integer loginUser) throws SQLException {
        SyncService.getInstance().executeStockAdjustment(date, loginUser);
        getDetailCount(date);

    }

    private void getDetailCount(String date) throws SQLException {
        Integer stockAdjustmentCount = SyncService.getInstance().getStockAdjustmentCount(date);
        lblStockAdjust.setText("Stock Adjustment" + " - " + stockAdjustmentCount);

        Integer grnCount = SyncService.getInstance().getGrnCount(date);
        lblGrn.setText("GRN" + " - " + grnCount);

        Integer invoiceCount = SyncService.getInstance().getInvoiceCount(date);
        lblInvoice.setText("Invoice" + " - " + invoiceCount);

        Integer paymentCount = SyncService.getInstance().getPaymentCount(date);
        lblPayment.setText("Payment" + " - " + paymentCount);

    }

    @SuppressWarnings("unchecked")
    private void initOthers() {
        try {
            setTitle("Account Integration System");
            setLocationRelativeTo(null);
            txtLog.setEditable(false);
            String date = SyncService.getInstance().getTransactionDate();
            lblDate.setText(date);

            //set company
            String companyName = TransactionService.getInstance().getCompanyName();
            lblCompanyName.setText(companyName);

            TextAreaOutputStream textAreaOutputStream = new TextAreaOutputStream(txtLog);
            System.setOut(new PrintStream(textAreaOutputStream));
        } catch (SQLException ex) {
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLoginUser(Integer loginUser) {
        this.loginUser = loginUser;
        System.out.println("login User - " + loginUser);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblGrn = new javax.swing.JLabel();
        lblStockAdjust = new javax.swing.JLabel();
        lblInvoice = new javax.swing.JLabel();
        lblPayment = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        lblProcess = new javax.swing.JLabel();
        lblCompanyName = new javax.swing.JLabel();
        lblClear = new javax.swing.JLabel();
        lblClose = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblClose1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(550, 330));
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(249, 249, 249));
        jLabel1.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Date");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblGrn.setBackground(new java.awt.Color(91, 192, 222));
        lblGrn.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblGrn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrn.setText("GRN");
        lblGrn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblGrn.setOpaque(true);
        lblGrn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGrnMouseClicked(evt);
            }
        });

        lblStockAdjust.setBackground(new java.awt.Color(91, 192, 222));
        lblStockAdjust.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblStockAdjust.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStockAdjust.setText("Stock Adjustment");
        lblStockAdjust.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblStockAdjust.setOpaque(true);
        lblStockAdjust.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblStockAdjustMouseClicked(evt);
            }
        });

        lblInvoice.setBackground(new java.awt.Color(91, 192, 222));
        lblInvoice.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblInvoice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInvoice.setText("Invoice");
        lblInvoice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblInvoice.setOpaque(true);
        lblInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblInvoiceMouseClicked(evt);
            }
        });

        lblPayment.setBackground(new java.awt.Color(91, 192, 222));
        lblPayment.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblPayment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPayment.setText("Payment");
        lblPayment.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblPayment.setOpaque(true);
        lblPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPaymentMouseClicked(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(91, 192, 222));
        jLabel6.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Next Date");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setOpaque(true);
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        lblDate.setBackground(new java.awt.Color(249, 249, 249));
        lblDate.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDate.setText("2018-02-02");
        lblDate.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGrn, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStockAdjust, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGrn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblStockAdjust, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 243, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, -2, 190, 500));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtLog.setColumns(20);
        txtLog.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        txtLog.setRows(5);
        jScrollPane2.setViewportView(txtLog);

        lblProcess.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        lblProcess.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProcess.setText("Processing . . .");

        lblCompanyName.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        lblCompanyName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompanyName.setText("Branch name goes here");

        lblClear.setBackground(new java.awt.Color(91, 192, 222));
        lblClear.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClear.setText("Clear");
        lblClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblClear.setOpaque(true);
        lblClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClearMouseClicked(evt);
            }
        });

        lblClose.setBackground(new java.awt.Color(217, 83, 79));
        lblClose.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblClose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClose.setText("X");
        lblClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblClose.setOpaque(true);
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        jLabel7.setText("Last Updated Date  :  2018-09-07");

        jLabel8.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Software By Supervision Technology (PVT), Ltd");

        lblClose1.setBackground(new java.awt.Color(255, 136, 0));
        lblClose1.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblClose1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClose1.setText("_");
        lblClose1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblClose1.setOpaque(true);
        lblClose1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClose1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lblProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblClear, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblClose, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(13, 13, 13))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblCompanyName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblProcess, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 690, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setDefaultCloseOperation(SystemIntegrationSyncGUI.EXIT_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_lblCloseMouseClicked

    private void lblClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClearMouseClicked
        dataClear();

    }//GEN-LAST:event_lblClearMouseClicked

    private void lblGrnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGrnMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.GRN);
        }
    }//GEN-LAST:event_lblGrnMouseClicked

    private void lblStockAdjustMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStockAdjustMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.ADJUSTMENT);
        }
    }//GEN-LAST:event_lblStockAdjustMouseClicked

    private void lblInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblInvoiceMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.INVOICE);
        }
    }//GEN-LAST:event_lblInvoiceMouseClicked

    private void lblPaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPaymentMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.PAYMENT);
        }
    }//GEN-LAST:event_lblPaymentMouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        if (optionPain() == 0) {
            try {
                String date = SyncService.getInstance().getNextDate(lblDate.getText());
                lblDate.setText(date);
                dataClear();
            } catch (SQLException | ParseException ex) {
                Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void lblClose1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClose1MouseClicked
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lblClose1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//   
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblClear;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblClose1;
    private javax.swing.JLabel lblCompanyName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblGrn;
    private javax.swing.JLabel lblInvoice;
    private javax.swing.JLabel lblPayment;
    private javax.swing.JLabel lblProcess;
    private javax.swing.JLabel lblStockAdjust;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables

    private int optionPain() {
        return JOptionPane.showConfirmDialog(null, "Are you sure ?", "Warning", JOptionPane.YES_OPTION);
    }

    private void dataClear() {
        txtLog.setText("");
        try {
            getDetailCount(lblDate.getText());
        } catch (SQLException ex) {
            System.out.println("get detail count function not support !");
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loader(String date, Integer loginUser, String type) {
        lblProcess.setText("Loading...");
//        lblProcess.setIcon(new ImageIcon("../images/loader.gif"));
        txtLog.setText("");
//        proBarLoading.setValue(0);
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (null == type) {
                        throw new RuntimeException("transaction type doesn't match into doInBackground method !");
                    } else {
                        switch (type) {
                            case Constant.ADJUSTMENT:
                                executeStockAdjustment(date, loginUser);
                                break;
                            case Constant.GRN:
                                executeGrn(date, loginUser);
                                break;
                            case Constant.INVOICE:
                                executeInvoice(date, loginUser);
                                break;
                            case Constant.PAYMENT:
                                executePayment(date, loginUser);
                                break;
                            default:
                                throw new RuntimeException("transaction type doesn't match into doInBackground method !");
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    lblProcess.setText("");
                    Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }

            @Override
            protected void done() {
                lblProcess.setText("");
            }
        }.execute();
    }

}
