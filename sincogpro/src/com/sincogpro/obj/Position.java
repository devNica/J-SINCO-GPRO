/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.obj;

import com.sincogpro.conn.connectionToMySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Alejandro Gonzalez
 */
public class Position {
    private int idposition;
    private String position;
    private String sql;
    connectionToMySQL cnx = new connectionToMySQL();

    public int getIdposition() {
        return idposition;
    }

    public void setIdposition(int idposition) {
        this.idposition = idposition;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    public DefaultComboBoxModel getPositionList(String filter){
        
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT * FROM `position` WHERE position.position LIKE '%"+filter+"%'";
        System.out.println(sql);
        cnx.openConnectionToMySQL("POSTION");
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   model.addElement(res.getString("position")); 
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.closeConnectionToMySQL("position");
        
        return model;
    }
    
    public String [] searchPosition(String filter){
    
        String data [] = new String[4];
        
        
        sql = "SELECT * FROM `position` WHERE position.position LIKE '"+filter+"%'";
        
        cnx.openConnectionToMySQL("POSTION");
        
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   for (int i = 0; i < data.length; i++) {
                        data[i] = res.getString(i+1);
                    }
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
       
        
        cnx.closeConnectionToMySQL("position");
        
        
        return data;
    }
    
}
