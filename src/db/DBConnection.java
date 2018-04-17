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
    String user;
    String pass;
    public DBConnection(){
        setConn();
    }

    void getInfo(){
        user = JOptionPane.showInputDialog("Input MySQL Username");
        JPasswordField passwordField = new JPasswordField();
        //passwordField.setEchoChar('X');
        Object[] obj = {"Input MySQL Password", passwordField};
        Object stringArray[] = {"OK"};
        JOptionPane.showOptionDialog(null, obj, "Password",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj);
        pass = new String(passwordField.getPassword());
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
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Wrong username or password\nTry Again?",
                                                                "Error", dialogButton, JOptionPane.ERROR_MESSAGE);
            if(dialogResult == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            wrongUser(e);
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
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
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
}
