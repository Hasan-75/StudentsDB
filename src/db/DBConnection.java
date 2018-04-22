/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import gui.Form;

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hasan
 */
public class DBConnection {
    Connection conn;
    Statement stmt;
    LoginF login;
    String user;
    String pass;
    DBConnection dc;
    public DBConnection(){
        dc = this;
        loginPage();
        //setConn();
    }

    void getInfo(){
        
        user = login.usrTF.getText();
        pass = new String(login.passTF.getPassword());
    }

    void setConn(){
        try {
            getInfo();
            Class.forName("com.mysql.jdbc.Driver");

            conn =  (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/ict", user, pass);

            stmt=(Statement) conn.createStatement();
            String tempSql = "SELECT COUNT(*) FROM students";
            ResultSet rs = stmt.executeQuery(tempSql);
            if(rs.next()){
                Form.numOfRows = rs.getInt(1);
            }
            
            showMainPage();
            login.dispose();
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if(e.getMessage().contains("Communications link failure")){
                JOptionPane.showMessageDialog(null, "Server is down!","Not found", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Wrong username or password\nTry Again?",
                                                                "Error", dialogButton, JOptionPane.ERROR_MESSAGE);
            if(dialogResult == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
           // wrongUser(e);
        }
    }

    void wrongUser(SQLException e){
        if(e.getMessage().contains("Access denied for user ")){
            setConn();
        }
    }

    public String[][] operation(String sql, boolean isToUpdate){
        String res[][] = new String[1][3];
        try {
            if(isToUpdate){
                stmt.executeUpdate(sql);
                res = null;
            }
            else{
                ResultSet rs=stmt.executeQuery(sql);
                while(rs.next()){  
                    System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                    res[0][0] = rs.getString(1);
                    res[0][1] = rs.getString(2);
                    res[0][2] = rs.getString(3);
                    Form.currentId = rs.getInt(4);
                }
           }
            //Form.sql = " ";
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unknown Colum","Not found", JOptionPane.ERROR_MESSAGE);

        }
        return res;
    }
    
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loginPage() {
        try {
            
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                login = new LoginF(dc);
                login.setLocationRelativeTo(null);
                login.setResizable(false);
                login.setVisible(true);
                
            }
        });
    }

    private void showMainPage() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Form f = new Form();
                f.setLocationRelativeTo(null);
                f.setResizable(false);
                f.setVisible(true);
            }
        });
    }
}
