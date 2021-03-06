/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.modelos;

import com.sincogpro.conn.ConexionMySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Alejandro Gonzalez
 */
public class Coin {
    private String sql;
    ConexionMySQL cnx = new ConexionMySQL();
    public DefaultComboBoxModel getComboCoinList(){
        
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT * FROM `moneda` WHERE 1";
        System.out.println(sql);
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   model.addElement(res.getString("descripcion")); 
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return model;
    }
}
