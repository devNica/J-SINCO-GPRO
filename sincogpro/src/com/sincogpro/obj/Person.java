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
    String Identificador="OBJ-Person";
    private String sql;
    connectionToMySQL cnx = new connectionToMySQL();
    
    
    public ArrayList<String> getPersonById (String idperson){
        ArrayList<String> person = new ArrayList<>();
        cnx.openConnectionToMySQL(Identificador);
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
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
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
                
        cnx.closeConnectionToMySQL(Identificador);
        
        return person;
        
    }
    
    public void createPerson (ArrayList<String> data){
        
        cnx.openConnectionToMySQL(Identificador);
        sql = "INSERT INTO "
                + "`persona` "
                + "(`idpersona`, `cedula`, `nombre`, `apellido`, "
                + "`fk_tipopersona`, `pais`, `ciudad`, `direccion`, `telefono`, "
                + "`correo`, `cliente`, `empleado`, `fk_sexo`) "
                + "VALUES (NULL, '"+data.get(0)+"', '"+data.get(1)+"', '"+data.get(2)+"', "
                + "'"+data.get(3)+"', '"+data.get(4)+"', '"+data.get(5)+"', '"+data.get(6)+"', "
                + "'"+data.get(7)+"', '"+data.get(8)+"', '"+data.get(9)+"', '"+data.get(10)+"', "
                + "'"+data.get(11)+"'); ";
        
        System.out.println(sql);
        
        try 
        {
          PreparedStatement pstm=(PreparedStatement)connectionToMySQL.conn.prepareStatement(sql);
          pstm.executeUpdate();
          JOptionPane.showMessageDialog(null, "Los datos se han registrado satisfactoriamente","System Information",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Ocurrio un Error al ejecutar la consulta", "¡Error de Ejecucion!",JOptionPane.ERROR_MESSAGE);
        }
        
        cnx.closeConnectionToMySQL(Identificador);
    }
    
    public void updatePerson(ArrayList<String> data, String idperson){
       sql="UPDATE `persona` "
               + "SET `cedula` = '"+data.get(0)+"',"
               + "`nombre` = '"+data.get(1)+"',"
               + "`apellido` = '"+data.get(2)+"',"
               + "`fk_tipopersona` = '"+data.get(3)+"',"
               + "`pais` = '"+data.get(4)+"',"
               + "`ciudad` = '"+data.get(5)+"',"
               + "`direccion` = '"+data.get(6)+"',"
               + "`telefono` = '"+data.get(7)+"',"
               + "`correo` = '"+data.get(8)+"',"
               + "`cliente` = '"+data.get(9)+"',"
               + "`empleado` = '"+data.get(10)+"',"
               + "`fk_sexo` = '"+data.get(11)+"'"
               + " WHERE `persona`.`idpersona` = "+idperson;
       
       cnx.openConnectionToMySQL(Identificador);
       
       System.out.println(sql);
        
        try 
        {
          PreparedStatement pstm=(PreparedStatement)connectionToMySQL.conn.prepareStatement(sql);
          pstm.executeUpdate();
          JOptionPane.showMessageDialog(null, "El registro se ha creado exitosamente","System Information",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Ocurrio un error al ejecutar la consulta", "¡Error de ejecucion!",JOptionPane.ERROR_MESSAGE);
        }
       
        cnx.closeConnectionToMySQL(Identificador);
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
        
        sql= query.substring(0, query.length()-1)+" FROM persona as  P\n" +
        "INNER JOIN sexo AS G ON G.idsexo = P.fk_sexo\n" +
        "INNER JOIN tipopersona AS TP ON TP.idtipopersona = P.fk_tipopersona\n" +
        "HAVING "+ filter;
        System.out.println(sql);
        
        
        cnx.openConnectionToMySQL(Identificador);
        
        String data[]=new String[personDT.getColumnCount()];        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                    for (int i = 0; i < personDT.getColumnCount(); i++) 
                        data[i]=res.getString(i+1);                                                    
                    personDT.addRow(data);                   
                } 
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.closeConnectionToMySQL(Identificador);
        
        return personDT;
    
    }
    
    
    public DefaultComboBoxModel getComboGenderList(){
         
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT * FROM `sexo` WHERE 1";
        System.out.println(sql);
        cnx.openConnectionToMySQL(Identificador);
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
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
        
        cnx.closeConnectionToMySQL(Identificador);
        
        return model;
     
    }
    
    public DefaultComboBoxModel getComboTyPerson(){
         
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        sql = "SELECT * FROM `tipopersona` WHERE 1";
        System.out.println(sql);
        cnx.openConnectionToMySQL(Identificador);
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
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
        
        cnx.closeConnectionToMySQL(Identificador);
        
        return model;
    }
    
    
    /*
    RETORNA EL INDICE DEL GENERO SELECCIONADO
    */
    public int getGenderIndexByDesc(String gender){
        
        sql = "SELECT sexo.idsexo FROM `sexo` WHERE sexo.sexo = '"+gender+"'";
        
        cnx.openConnectionToMySQL(Identificador);
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
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
        
        cnx.closeConnectionToMySQL(Identificador);
        return genderIndex;
    }
    
    
    public int getTyPersonIndexByDesc(String tyPerson){
        
        sql = "SELECT tipopersona.idtipopersona FROM `tipopersona` WHERE tipopersona.tipo = '"+tyPerson+"'";
        
        cnx.openConnectionToMySQL(Identificador);
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
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
        
        cnx.closeConnectionToMySQL(Identificador);
        return tyPersonIndex;
    }
    
    
}
