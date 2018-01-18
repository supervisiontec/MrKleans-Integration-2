/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import db_connections.DataSourceWrapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import model.TEmployeeAssingment;
import model.TFingerPrint;

/**
 *
 * @author 'Kasun Chamara'
 */
public class MySQLController {

    private final DataSourceWrapper mysqlDataSourceWrapper;

    public MySQLController() throws SQLException {
        Properties prop = new Properties();
        InputStream input = null;
        String dbName = null;
        String user = null;
        String pswd = null;
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println("set database name : "+prop.getProperty("setDataSourceName"));
            // set value into variable
            dbName = prop.getProperty("setDataSourceName");
            user = prop.getProperty("setDataSourceUser");
            pswd = prop.getProperty("setDataSourcePswd");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.mysqlDataSourceWrapper = new DataSourceWrapper(dbName, user, pswd);
//        this.mysqlDataSourceWrapper = new DataSourceWrapper("jdbc:mysql://localhost:3306/care_point", "root", "mysql");
//        this.mysqlDataSourceWrapper = new DataSourceWrapper("jdbc:mysql://123.231.62.166:3306/care_point", "kavishmanjitha", "kavishmanjitha");
    }

    public Integer saveFingerPrint(ArrayList<TFingerPrint> list) throws SQLException {
        try (Connection connection = mysqlDataSourceWrapper.getConnection()) {
            String insertSql = "insert into ras_AttRecord (ID,DN,DIN,Clock) values (?,?,?,?)";
            PreparedStatement preparedStatementInsert = connection.prepareStatement(insertSql);
            int value = 0;
            for (int i = 0; i < list.size(); i++) {
                preparedStatementInsert.setInt(1, list.get(i).getId());
                preparedStatementInsert.setInt(2, list.get(i).getDn());
                preparedStatementInsert.setInt(3, list.get(i).getDin());
                preparedStatementInsert.setTimestamp(4, list.get(i).getClock());

                //finger print save
                int execute = preparedStatementInsert.executeUpdate();

                //employee assingment save
                TEmployeeAssingment employeeAssingment = new TEmployeeAssingment();
                String[] dateTime = list.get(i).getClock().toString().split(" ");

                Integer branchWaitingBay = getBranchWaitingBay(list.get(i).getDn(), "EMPLOYEE_WAITING_BAY");
                employeeAssingment.setBay(branchWaitingBay);

                employeeAssingment.setDate(dateTime[0]);
                employeeAssingment.setEmployee(list.get(i).getDin());

                employeeAssingment.setInTime(list.get(i).getClock().toString());
                employeeAssingment.setIsOut(false);
                employeeAssingment.setOutTime(null);
                employeeAssingment.setStatus("PENDING");

                try {
                    saveEmployeeAssignment(employeeAssingment);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (execute > 0) {
                    value++;
                }

            }
            if (value == list.size()) {
                return value;

            }
            return -1;
        }

    }

    private Integer getBranchWaitingBay(Integer Dn, String type) throws SQLException {
        try (Connection conn = mysqlDataSourceWrapper.getConnection()) {
            int waitingBranch = -1;
            String query = "select m_bay.index_no from m_bay where m_bay.branch="
                    + "(select t_finger_print_mashine.branch from t_finger_print_mashine "
                    + "where t_finger_print_mashine.finger_print=? limit 1) "
                    + "and m_bay.`type`=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, Dn);
            preparedStatement.setString(2, type);
            ResultSet rst = preparedStatement.executeQuery();
            if (rst.next()) {
                waitingBranch = rst.getInt("index_no");
            }
            return waitingBranch;
        }
    }

    private int saveEmployeeAssignment(TEmployeeAssingment employeeAssingment) throws SQLException {
        try (Connection conn = mysqlDataSourceWrapper.getConnection()) {
            Integer count = 0;
//            check employee type
            String type = getEmployeeType(employeeAssingment.getEmployee());
            if ("worker".equals(type)) {

                //check employee is pending
                count = checkEmployeeIsPending(employeeAssingment.getDate(), employeeAssingment.getEmployee());
                if (count == 0) {

                    String sql = "insert into t_employee_assingment (employee,bay,in_time,out_time,status,date,is_out)"
                            + " values (?,?,?,?,?,?,?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, employeeAssingment.getEmployee());
                    preparedStatement.setInt(2, employeeAssingment.getBay());
                    preparedStatement.setString(3, employeeAssingment.getInTime());
                    preparedStatement.setString(4, employeeAssingment.getOutTime());
                    preparedStatement.setString(5, employeeAssingment.getStatus());
                    preparedStatement.setString(6, employeeAssingment.getDate());
                    preparedStatement.setBoolean(7, employeeAssingment.isIsOut());

                    int employeeAssignment = preparedStatement.executeUpdate();

                    return employeeAssignment;
                }
            }
            return 0;
        }
    }

    private Integer checkEmployeeIsPending(String date, Integer employee) throws SQLException {
        try (Connection conn = mysqlDataSourceWrapper.getConnection()) {
            Integer count = 0;
            String checkSql = "select count(t_employee_assingment.index_no) as count "
                    + "from t_employee_assingment where t_employee_assingment.date=? "
                    + "and t_employee_assingment.`status`='PENDING' \n"
                    + "and t_employee_assingment.employee=?";
            PreparedStatement pst = conn.prepareStatement(checkSql);
            pst.setString(1, date);
            pst.setInt(2, employee);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                count = rst.getInt("count");
            }
            return count;
        }
    }

    private String getEmployeeType(Integer employee) throws SQLException {
        try (Connection conn = mysqlDataSourceWrapper.getConnection()) {
            String checkSql = "select m_employee.`type` from m_employee where m_employee.index_no=?";
            PreparedStatement pst = conn.prepareStatement(checkSql);
            pst.setInt(1, employee);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                return rst.getString("type");
            }
            return null;
        }
    }

}
