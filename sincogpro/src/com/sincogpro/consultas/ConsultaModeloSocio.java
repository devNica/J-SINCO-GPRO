/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.consultas;

import com.sincogpro.conn.ConexionMySQL;
import com.sincogpro.modelos.Socio;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alejandro Gonzalez
 */
public class ConsultaModeloSocio{
    
    private final String TABLA = "Socio";
    private String sql=null;
    ConexionMySQL cnx = new ConexionMySQL();
    Socio MS = new Socio();
    
    public String calcularCodigoSocio(){
        
        sql = "SELECT FN_CALCULARCODIGOSOCIO()";
        String codigo=null;
        
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   codigo = res.getString(1);                  
                } 
                
                res.close();
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return codigo;
    }
    
    public DefaultTableModel obtenerSocios(boolean columns[], String filter){
    
        String query = "SELECT ";
        DefaultTableModel TablaSocio = new DefaultTableModel();
        
        if (columns[0]==true) {TablaSocio.addColumn("#");       query+=" S.IDSOCIO,";}
        if (columns[1]==true) {TablaSocio.addColumn("CODIGO");       query+=" S.CODIGO,";}
        if (columns[2]==true) {TablaSocio.addColumn("NOMBRE");         query+=" S.RAZONSOCIAL,";}
        if (columns[3]==true) {TablaSocio.addColumn("COMERCIAL");        query+=" S.RAZONCOMERCIAL,";}
        if (columns[4]==true) {TablaSocio.addColumn("TIPO");        query+=" TP.DESCRIPCION,";}
        if (columns[5]==true) {TablaSocio.addColumn("ESTADO");        query+=" IF(S.ACTIVO = 1, 'ACTIVO', 'INACTIVO') AS ESTADO,";} 
        
        sql= query.substring(0, query.length()-1)+" FROM "+TABLA+" as  S\n" +
        "INNER JOIN tiposocio AS TP ON TP.IDTIPOSOCIO = S.FK_TIPOSOCIO\n" +
        "HAVING S.RAZONSOCIAL like ?";
        System.out.println(sql);
        
       
        cnx.abrirConexionMySQL();
        
        String data[]=new String[TablaSocio.getColumnCount()];        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            pstm.setString(1,"%"+filter+"%");
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                    for (int i = 0; i < TablaSocio.getColumnCount(); i++) 
                        data[i]=res.getString(i+1);                                                    
                    TablaSocio.addRow(data);                   
                } 
                
                //res.close();
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return TablaSocio;
    
    }
    
    public void editarSocio(ArrayList<String> data){
        
        cnx.abrirConexionMySQL();
       
        try 
        {
            sql="UPDATE "+TABLA
                + " SET RAZONSOCIAL = ?,"
                + "RAZONCOMERCIAL = ?,"
                + "RAZONCOMERCIAL = ?,"
                + "RUC = ?,"
                + "CEDULA = ?,"
                + "DIRECCION = ?,"
                + "TELF1 = ?,"
                + "TELF2 = ?,"
                + "EMAIL1 = ?,"
                + "EMAIL2 = ?,"
                + "CREDITO = ?,"
                + "LIMITECREDITO = ?,"
                + "DESCUENTO = ?,"
                + "FK_TIPOSOCIO = ?,"
                + "FK_REGPAGO = ?,"
                + "FK_REGCOBRO = ?,"
                + "ACTIVO = ?"
                    
               + " WHERE IDSOCIO = ?";
            
            PreparedStatement pstm=(PreparedStatement)ConexionMySQL.conn.prepareStatement(sql);
            
            for (int i = 0; i < data.size(); i++) {
                pstm.setString(i+1, data.get(i));
            }
          
            pstm.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro editado con exito","System Information",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Error al ejecutar la consulta", "¡Error de ejecucion!",JOptionPane.ERROR_MESSAGE);
        }
       
        cnx.cerrarConexionMySQL();
    }
    
}
