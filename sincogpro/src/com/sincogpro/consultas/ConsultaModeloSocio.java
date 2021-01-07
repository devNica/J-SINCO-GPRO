/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.consultas;

import com.sincogpro.conn.ConexionMySQL;
import com.sincogpro.modelos.Descuento;
import com.sincogpro.modelos.RegimenCobro;
import com.sincogpro.modelos.RegimenPago;
import com.sincogpro.modelos.Socio;
import com.sincogpro.modelos.TipoSocio;
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
    Socio MS;
    RegimenCobro RC;
    RegimenPago RP;
    TipoSocio TS;
    Descuento De;
    
    public String calcularIdSocio(){
    
        sql = "SELECT (MAX(IDSOCIO)+1) AS IDSOCIO FROM "+TABLA+" WHERE 1";
        String IDSOCIO=null;
        
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   IDSOCIO = res.getString(1);                  
                } 
                
                res.close();
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return IDSOCIO;
        
    }
    
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
    
    public boolean obtenerSocio(String idsocio, Socio modelo, RegimenCobro RC, RegimenPago RP, TipoSocio TS, Descuento DE){
        boolean estado = false;
        sql = "SELECT * FROM `socio` WHERE IDSOCIO = ?";
        
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            pstm.setString(1, idsocio);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   
                    modelo.setIDSOCIO(res.getInt("IDSOCIO")); 
                    modelo.setCODIGO(res.getString("CODIGO")); 
                    modelo.setRAZONSOCIAL(res.getString("RAZONSOCIAL")); 
                    modelo.setRAZONCOMERCIAL(res.getString("RAZONCOMERCIAL")); 
                    modelo.setRUC(res.getString("RUC"));
                    modelo.setCEDULA(res.getString("CEDULA")); 
                    modelo.setDIRECCION(res.getString("DIRECCION")); 
                    modelo.setTELF1(res.getString("TELF1").substring(6)); 
                    modelo.setTELF2(res.getString("TELF2").substring(6)); 
                    modelo.setEMAIL1(res.getString("EMAIL1")); 
                    modelo.setEMAIL2(res.getString("EMAIL2")); 
                    modelo.setCREDITO(res.getBoolean("CREDITO")); 
                    modelo.setACTIVO(res.getBoolean("ACTIVO")); 
                    modelo.setLIMITECREDITO(res.getDouble("LIMITECREDITO")); 
                    modelo.setFK_TIPOSOCIO(res.getInt("FK_TIPOSOCIO")); 
                    modelo.setFK_REGPAGO(res.getInt("FK_REGPAGO")); 
                    modelo.setFK_REGCOBRO(res.getInt("FK_REGCOBRO")); 
                    modelo.setFK_DESCUENTO(res.getInt("FK_DESCUENTO"));
                    
                    RC.setIDREGCOBRO(res.getInt("FK_REGCOBRO"));
                    RP.setIDREGPAGO(res.getInt("FK_REGPAGO"));
                    DE.setIDDESCUENTO(res.getInt("FK_DESCUENTO"));
                    TS.setIDTIPOSOCIO(res.getInt("FK_TIPOSOCIO"));
                    
                } 
                
                res.close();
                estado = true;
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return estado;
    
    }
    
    public DefaultTableModel obtenerSocios(boolean columns[], String filter){
    
        String query = "SELECT ";
        DefaultTableModel TablaSocio = new DefaultTableModel();
        
        if (columns[0]==true) {TablaSocio.addColumn("#");       query+=" S.IDSOCIO,";}
        if (columns[1]==true) {TablaSocio.addColumn("CODIGO");       query+=" S.CODIGO,";}
        if (columns[2]==true) {TablaSocio.addColumn("NOMBRE");         query+=" S.RAZONSOCIAL,";}
        if (columns[3]==true) {TablaSocio.addColumn("RAZON COMERCIAL");        query+=" S.RAZONCOMERCIAL,";}
        if (columns[4]==true) {TablaSocio.addColumn("CEDULA/RUC");        query+=" IF(S.CEDULA = '-', S.RUC, IF(S.RUC = '-', S.CEDULA, S.RUC)) AS DOC,";}
        if (columns[5]==true) {TablaSocio.addColumn("TELF");        query+=" IF(S.TELF1 = '(505)-0000-0000', S.TELF2, IF(S.TELF2 = '(505)-0000-0000', S.TELF1, S.TELF2)) AS TELF,";}
        if (columns[6]==true) {TablaSocio.addColumn("TIPO");        query+=" TP.DESCRIPCION,";}
        if (columns[7]==true) {TablaSocio.addColumn("ESTADO");        query+=" IF(S.ACTIVO = 1, 'ACTIVO', 'INACTIVO') AS ESTADO,";} 
        
        sql= query.substring(0, query.length()-1)+" FROM "+TABLA+" as  S\n" +
        "INNER JOIN tiposocio AS TP ON TP.IDTIPOSOCIO = S.FK_TIPOSOCIO\n" +
        "WHERE S.RAZONSOCIAL LIKE ? OR S.CEDULA LIKE ? OR S.RUC LIKE ?";
        System.out.println(sql);
        
       
        cnx.abrirConexionMySQL();
        
        String data[]=new String[TablaSocio.getColumnCount()];        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            pstm.setString(1,"%"+filter+"%");
            pstm.setString(2,"%"+filter+"%");
            pstm.setString(3,"%"+filter+"%");
            
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
    
    public void crearSocio(ArrayList<String> data){
        cnx.abrirConexionMySQL();

        try 
        {
            sql="INSERT INTO "+TABLA
                + "(IDSOCIO, "
                + "CODIGO, "
                + "RAZONSOCIAL,"
                + "RAZONCOMERCIAL,"
                + "RUC,"
                + "CEDULA,"
                + "DIRECCION,"
                + "TELF1,"
                + "TELF2,"
                + "EMAIL1,"
                + "EMAIL2,"
                + "CREDITO,"
                + "LIMITECREDITO,"
                + "FK_DESCUENTO,"
                + "FK_TIPOSOCIO,"
                + "FK_REGPAGO,"
                + "FK_REGCOBRO,"
                + "ACTIVO)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pstm=(PreparedStatement)ConexionMySQL.conn.prepareStatement(sql);

            for (int i = 0; i < data.size(); i++) {
                pstm.setString(i+1, data.get(i));
            }

            pstm.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro creado con exito","System Information",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Error:\n"+e, "¡Error de ejecucion!",JOptionPane.ERROR_MESSAGE);
        }

        cnx.cerrarConexionMySQL();
    }
    
    public void editarSocio(ArrayList<String> data){
        
        cnx.abrirConexionMySQL();
       
        try 
        {
            sql="UPDATE "+TABLA
                + " SET RAZONSOCIAL = ?,"
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
                + "FK_DESCUENTO = ?,"
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
            JOptionPane.showMessageDialog(null,"Error:\n"+e, "¡Error de ejecucion!",JOptionPane.ERROR_MESSAGE);
        }
       
        cnx.cerrarConexionMySQL();
    }
    
}
