/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.PrintStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sync_service.SyncService;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SystemIntegrationSyncGUI extends javax.swing.JFrame {

    Integer loginUser=-1;
    public SystemIntegrationSyncGUI() {
        initComponents();

        initOthers();
        lblProcess.setVisible(false);
        
        try {
            getDetailCount(txtDate.getText());
        } catch (SQLException ex) {
            System.out.println("get detail count function not support !");
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeGrn(String date,Integer loginUser1) throws SQLException {
        SyncService.getInstance().executeGrn(date,loginUser1);
        getDetailCount(date);
    }

    private void executeInvoice(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executeInvoice(date,loginUser1);
        getDetailCount(date);
    }
    private void executePayment(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executePayment(date,loginUser1);
        getDetailCount(date);
    }
    private void getDetailCount(String date) throws SQLException {
        Integer grnCount=SyncService.getInstance().getGrnCount(date);
        btnGrn.setText("GRN"+" - "+grnCount);
        
        Integer invoiceCount=SyncService.getInstance().getInvoiceCount(date);
        btnInvoice.setText("Invoice"+" - "+invoiceCount);
        
        Integer paymentCount=SyncService.getInstance().getPaymentCount(date);
        btnPayment.setText("Payment"+" - "+paymentCount);
    }

    @SuppressWarnings("unchecked")
    private void initOthers() {
        setTitle("Account Integration System");
        setLocationRelativeTo(null);
        txtLog.setEditable(false);
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        TextAreaOutputStream textAreaOutputStream = new TextAreaOutputStream(txtLog);
        System.setOut(new PrintStream(textAreaOutputStream));
    }
    public void setLoginUser(Integer loginUser) {
        this.loginUser=loginUser;
        System.out.println("login User - "+loginUser);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        txtDate = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        btnGrn = new javax.swing.JButton();
        btnInvoice = new javax.swing.JButton();
        btnPayment = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblProcess = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(550, 330));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane2.setViewportView(txtLog);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnGrn.setText("GRN");
        btnGrn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrnActionPerformed(evt);
            }
        });

        btnInvoice.setText("Invoice");
        btnInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvoiceActionPerformed(evt);
            }
        });

        btnPayment.setText("Payment");
        btnPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Date :");

        lblProcess.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblProcess.setForeground(new java.awt.Color(0, 0, 0));
        lblProcess.setText("processing . . .");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGrn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClear)
                        .addComponent(lblProcess)))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGrn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInvoice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayment))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setDefaultCloseOperation(SystemIntegrationSyncGUI.EXIT_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtLog.setText("");
        try {
            getDetailCount(txtDate.getText());
        } catch (SQLException ex) {
            System.out.println("get detail count function not support !");
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvoiceActionPerformed
        try {
            lblProcess.setVisible(true);
            executeInvoice(txtDate.getText(),loginUser);
            lblProcess.setVisible(false);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnInvoiceActionPerformed

    private void btnGrnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrnActionPerformed
        try {
            lblProcess.setVisible(true);
            executeGrn(txtDate.getText(),loginUser);
            lblProcess.setVisible(false);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnGrnActionPerformed

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        try {
            lblProcess.setVisible(true);
            executePayment(txtDate.getText(),loginUser);
            lblProcess.setVisible(false);
        } catch (SQLException ex) {
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPaymentActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//   
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnGrn;
    private javax.swing.JButton btnInvoice;
    private javax.swing.JButton btnPayment;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblProcess;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables

}
