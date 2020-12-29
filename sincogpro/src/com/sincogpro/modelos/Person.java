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
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
/**
 *
 * @author Alejandro Gonzalez
 */
public class Person {
    
    private int genderIndex;
    private int tyPersonIndex;
    String Identificador="OBJ-Person", table="persona";
    
    private String sql;
    ConexionMySQL cnx = new ConexionMySQL();
    
    
    public ArrayList<String> getPersonById (String idperson){
        
        ArrayList<String> person = new ArrayList<>();
        cnx.abrirConexionMySQL();
        int NUMBER_COLUMNS = 12;
        
        sql = "SELECT  "
                + "P.idpersona, "
                + "P.cedula, "
                + "P.nombre, "
                + "P.apellido, "
                + "TP.idtipopersona, "
                + "P.pais, "
                + "P.ciudad, "
                + "P.direccion, "
                + "P.telefono, "
                + "P.correo, "
                + "IF(P.cliente = 1, IF(P.empleado = 1, 'CLIENTE/EMPLEADO', 'CLIENTE'), 'EMPLEADO') AS relacion, "
                + "G.idsexo, "
                + "G.sexo, "
                + "TP.tipo "
                + "FROM persona as  P\n" 
                + "INNER JOIN sexo AS G ON G.idsexo = P.fk_sexo\n"
                + "INNER JOIN tipopersona AS TP ON TP.idtipopersona = P.fk_tipopersona\n"
                + "HAVING P.idpersona = "+idperson;
        
        System.out.println(sql);
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   for (int i = 0; i < NUMBER_COLUMNS; i++) 
                        person.add(res.getString(i+1));                                                    
                } 
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
                
        cnx.cerrarConexionMySQL();
        
        return person;
        
    }
    
    public void createPerson (ArrayList<String> data){
        
        cnx.abrirConexionMySQL();
        
        try 
        {
          
            sql="INSERT INTO "+table+" "
            + "(idpersona, cedula, nombre, apellido, fk_tipopersona, pais, ciudad, direccion, telefono, correo, cliente, empleado, fk_sexo) "
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
            JOptionPane.showMessageDialog(null,"Ocurrio un Error al ejecutar la consulta", "¡Error de Ejecucion!",JOptionPane.ERROR_MESSAGE);
        }
        
        cnx.cerrarConexionMySQL();
    }
    
    public void updatePerson(ArrayList<String> data, String idperson){
        
       
       cnx.abrirConexionMySQL();
       
        try 
        {
            sql="UPDATE "+table
               + " SET cedula = ?,"
               + "nombre = ?,"
               + "apellido = ?,"
               + "fk_tipopersona = ?,"
               + "pais = ?,"
               + "ciudad = ?,"
               + "direccion = ?,"
               + "telefono = ?,"
               + "correo = ?,"
               + "cliente = ?,"
               + "empleado = ?,"
               + "fk_sexo = ?"
               + " WHERE "+table+".idpersona = "+idperson;
            
            PreparedStatement pstm=(PreparedStatement)ConexionMySQL.conn.prepareStatement(sql);
            
            for (int i = 0; i < data.size(); i++) {
                pstm.setString(i+1, data.get(i));
            }
          
            pstm.executeUpdate();
            JOptionPane.showMessageDialog(null, "El registro se ha creado exitosamente","System Information",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Ocurrio un error al ejecutar la consulta", "¡Error de ejecucion!",JOptionPane.ERROR_MESSAGE);
        }
       
        cnx.cerrarConexionMySQL();
    }
    
    public DefaultTableModel getPerson(boolean columns[], String filter){
    
        String query = "SELECT ";
        DefaultTableModel personDT = new DefaultTableModel();
        
        if (columns[0]==true) {personDT.addColumn("#");       query+=" P.idpersona,";}
        if (columns[1]==true) {personDT.addColumn("CEDULA");        query+=" P.cedula,";}
        if (columns[2]==true) {personDT.addColumn("NOMBRES");         query+=" P.nombre,";}
        if (columns[3]==true) {personDT.addColumn("APELLIDOS");        query+=" P.apellido,";}
        if (columns[4]==true) {personDT.addColumn("TIPO");        query+=" TP.tipo,";}
        if (columns[5]==true) {personDT.addColumn("PAIS");        query+=" P.pais,";}    
        if (columns[6]==true) {personDT.addColumn("CIUDAD");        query+=" P.ciudad,";}  
        if (columns[7]==true) {personDT.addColumn("DIRECCION");        query+=" P.direccion,";}
        if (columns[8]==true) {personDT.addColumn("TEL");     query+=" P.telefono,";}
        if (columns[9]==true) {personDT.addColumn("CORREO");     query+=" P.correo,";}  
        if (columns[10]==true) {personDT.addColumn("RELACION");   query+=" IF(P.cliente = 1, IF(P.empleado = 1, 'CLIENTE/EMPLEADO', 'CLIENTE'), 'EMPLEADO') AS relacion,";}
        if (columns[11]==true) {personDT.addColumn("FK_SEXO");   query+=" P.fk_sexo,";}       
        if (columns[12]==true) {personDT.addColumn("SEXO");        query+=" G.sexo,";}
        
        sql= query.substring(0, query.length()-1)+" FROM "+table+" as  P\n" +
        "INNER JOIN sexo AS G ON G.idsexo = P.fk_sexo\n" +
        "INNER JOIN tipopersona AS TP ON TP.idtipopersona = P.fk_tipopersona\n" +
        "HAVING "+ filter;
        System.out.println(sql);
        
        
        cnx.abrirConexionMySQL();
        
        String data[]=new String[personDT.getColumnCount()];        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                    for (int i = 0; i < personDT.getColumnCount(); i++) 
                        data[i]=res.getString(i+1);                                                    
                    personDT.addRow(data);                   
                } 
                
                res.close();
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        
        return personDT;
    
    }
    
    
    public DefaultComboBoxModel getComboGenderList(){
         
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT * FROM `sexo` WHERE 1";
        System.out.println(sql);
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   model.addElement(res.getString("sexo")); 
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
    
    public DefaultComboBoxModel getComboTyPerson(){
         
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT * FROM `tipopersona` WHERE 1";
        System.out.println(sql);
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   model.addElement(res.getString("tipo")); 
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
    
    
    /*
    RETORNA EL INDICE DEL GENERO SELECCIONADO
    */
    public int getGenderIndexByDesc(String gender){
        
        sql = "SELECT sexo.idsexo FROM `sexo` WHERE sexo.sexo = '"+gender+"'";
        
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   genderIndex = res.getInt("idsexo");
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        return genderIndex;
    }
    
    
    public int getTyPersonIndexByDesc(String tyPerson){
        
        sql = "SELECT tipopersona.idtipopersona FROM `tipopersona` WHERE tipopersona.tipo = '"+tyPerson+"'";
        
        cnx.abrirConexionMySQL();
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) ConexionMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   tyPersonIndex = res.getInt("idtipopersona");
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.cerrarConexionMySQL();
        return tyPersonIndex;
    }
    
    
}
