/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.conn;

import com.sincogpro.credentials.Credentials;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Alejandro Gonzalez
 */
public class connectionToMySQL {
    
    public static Connection conn = null;
    private final String PORT = Credentials.PORT;
    private final String PASSWORD = Credentials.PASSWORD;
    private final String USERNAME = Credentials.USERNAME;
    private final String DATABASE = Credentials.DATABASE;
    private final String URL = Credentials.URL;
    
    public  void openConnectionToMySQL(String obj)
    {        
        try
        {         
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(URL,USERNAME,PASSWORD);
            if (conn!=null)
            {
                System.out.println("The connection to the database:\t"+DATABASE+"\nis Successful");
                System.out.println("the connection requested by the object was opened:\t"+obj);
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,
                        "The connection to the database has been lost",
                        "Connection Error...",
            JOptionPane.ERROR_MESSAGE);
           
        }    
    }
    
    //CERRAR LA CONEXION A LA BASE DE DATOS
    public void closeConnectionToMySQL(String obj)
    {
        try 
        {
            conn.close();
            System.out.println("The connection requested by the object was closed:\t"+obj);
        } 
        catch (SQLException e) 
        {
            System.out.println(e);
        }
    }
}
