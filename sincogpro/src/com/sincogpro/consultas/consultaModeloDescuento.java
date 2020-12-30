/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.consultas;

import com.sincogpro.conn.ConexionMySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Alejandro Gonzalez
 */
public class consultaModeloDescuento {
    private String sql=null;
    private final String TABLA = "descuento";
    ConexionMySQL cnx = new ConexionMySQL(); 
    
    public DefaultComboBoxModel itemsDescuento(){
         
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT descuento.DESCUENTO FROM `"+TABLA+"` WHERE 1";
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   model.addElement(res.getString("DESCUENTO")); 
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
