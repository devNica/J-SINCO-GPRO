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
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author RESAINVENTARIO
 */
public class Employee {
    private String EmpNumber;
    private String sql;
    ConexionMySQL cnx = new ConexionMySQL();
    private final String table="empleado";
    
    public String searchEmpNumber(){
    
        sql="SELECT FN_SUGERIR_NUMEROEMPLEADO() as NE";
        
        System.out.println(sql);
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                  EmpNumber = res.getString("NE");
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return EmpNumber;
    
    }
    
    
    public void createPerson (ArrayList<String> data){
        
        cnx.abrirConexionMySQL();
        
        try 
        {
            sql="INSERT INTO "+table+" "
            + "(idempleado, dni, fk_puesto, fk_organizacion, fk_nivel, foto, salario, fk_moneda, activo, usuario, fk_persona, creado_en, actualizado_en) "
            + "VALUES(NULL,?,?,?,?,?,?,?,?,?,?,?,?)";
          
            PreparedStatement pstm=(PreparedStatement)ConexionMySQL.conn.prepareStatement(sql);
            
            for (int i = 0; i < data.size(); i++) {
                pstm.setString(i+1, data.get(i));
            }
          
            pstm.executeUpdate();
            JOptionPane.showMessageDialog(null, "Los datos se han registrado satisfactoriamente","System Information",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Error: "+e, "¡Error de Ejecucion!",JOptionPane.ERROR_MESSAGE);
        }
        
        cnx.cerrarConexionMySQL();
    }
}
