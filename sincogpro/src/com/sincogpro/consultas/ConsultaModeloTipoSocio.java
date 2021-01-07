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
public class ConsultaModeloTipoSocio {
    private String sql=null;
    private final String TABLA = "tiposocio";
    ConexionMySQL cnx = new ConexionMySQL();
    
    public String tipoSocio(int idsocio){
        
        sql = "SELECT * FROM `"+TABLA+"` WHERE "+TABLA+".IDTIPOSOCIO = ?";
        String descripcion = null;
        cnx.abrirConexionMySQL();
        
        System.out.println(sql);
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            pstm.setInt(1, idsocio);
            
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   descripcion = res.getString("DESCRIPCION"); 
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return descripcion;
    }
    
    public DefaultComboBoxModel itemsEntidad(){
         
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT DISTINCT ENTIDAD FROM `"+TABLA+"` WHERE 1";
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   model.addElement(res.getString("ENTIDAD")); 
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
    
    public DefaultComboBoxModel itemsTipoSocio(String entidad){
         
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT * FROM `"+TABLA+"` WHERE "+TABLA+".ENTIDAD = ?";
        cnx.abrirConexionMySQL();
        
        System.out.println(sql);
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            pstm.setString(1, entidad);
            
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   model.addElement(res.getString("DESCRIPCION")); 
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
