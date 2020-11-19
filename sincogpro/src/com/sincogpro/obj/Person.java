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
import javax.swing.JOptionPane;
/**
 *
 * @author Alejandro Gonzalez
 */
public class Person {
    
    private int genderIndex;
    private int a; //SE UTILIZA PARA IR RECORRIENDO LOS ELEMENTOS DE LAS LISTAS;
    private String [] genderList;
    private String sql;
    ArrayList<String>cluster=new ArrayList<>(); 
    connectionToMySQL cnx = new connectionToMySQL();
    
    public void createPerson (ArrayList<String> data){
        
        cnx.openConnectionToMySQL("Person");
        sql = "INSERT INTO "
                + "`person` "
                + "(`idperson`, `dni`, `firstname`, `lastname`, "
                + "`birthday`, `country`, `city`, `address`, `phone`, "
                + "`email`, `is_customer`, `is_staff`, `fk_gender`) "
                + "VALUES (NULL, '"+data.get(0)+"', '"+data.get(1)+"', '"+data.get(2)+"', "
                + "'"+data.get(3)+"', '"+data.get(4)+"', '"+data.get(5)+"', '"+data.get(6)+"', "
                + "'"+data.get(7)+"', '"+data.get(8)+"', '"+data.get(9)+"', '"+data.get(10)+"', "
                + "'"+data.get(11)+"'); ";
        
        try 
        {
          PreparedStatement pstm=(PreparedStatement)connectionToMySQL.conn.prepareStatement(sql);
          pstm.executeUpdate();
          JOptionPane.showMessageDialog(null, "Person has been created sucessfull","System Information",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null,"An error has ocurred", "Execute Error !!!",JOptionPane.ERROR_MESSAGE);
        }
        
        cnx.closeConnectionToMySQL("Person");
    }
    
    public DefaultTableModel getPerson(boolean columns[], String filter){
    
        String query = "SELECT ";
        DefaultTableModel personDT = new DefaultTableModel();
        
        if (columns[0]==true) {personDT.addColumn("#");       query+=" P.idperson,";}
        if (columns[1]==true) {personDT.addColumn("DNI");        query+=" P.dni,";}
        if (columns[2]==true) {personDT.addColumn("FIRSTNAME");         query+=" P.firstname,";}
        if (columns[3]==true) {personDT.addColumn("LASTNAME");        query+=" P.lastname,";}
        if (columns[4]==true) {personDT.addColumn("BIRTHDAY");        query+=" P.birthday,";}
        if (columns[5]==true) {personDT.addColumn("COUNTRY");        query+=" P.country,";}    
        if (columns[6]==true) {personDT.addColumn("CITY");        query+=" P.city,";}  
        if (columns[7]==true) {personDT.addColumn("ADDRESS");        query+=" P.address,";}
        if (columns[8]==true) {personDT.addColumn("PHONE");     query+=" P.phone,";}
        if (columns[9]==true) {personDT.addColumn("EMAIL");     query+=" P.email,";}  
        if (columns[10]==true) {personDT.addColumn("TYPE");   query+=" IF(P.is_customer = 1, IF(P.is_staff = 1, 'CUSTOMER/STAFF', 'CUSTOMER'), 'STAFF') AS type,";}
        if (columns[11]==true) {personDT.addColumn("FK_GENDER");   query+=" P.fk_gender,";}       
        if (columns[12]==true) {personDT.addColumn("GENDER");        query+=" G.gender,";}
        
        sql= query.substring(0, query.length()-1)+" FROM person as  P\n" +
        "INNER JOIN gender AS G ON G.idgender = P.fk_gender\n" +
        "HAVING "+ filter;
        System.out.println(sql);
        
        
        cnx.openConnectionToMySQL("Person");
        
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
        
        cnx.closeConnectionToMySQL("Person");
        
        return personDT;
    
    }
    
    public int getGenderIndex() {
        return genderIndex;
    }
    
    /*
    RETORNA EL INDICE DEL GENERO SELECCIONADO
    */
    public int getGenderIndexByDesc(String gender){
        
        sql = "SELECT gender.idgender FROM `gender` WHERE gender.gender = '"+gender+"'";
        
        cnx.openConnectionToMySQL("Person");
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) cnx.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   genderIndex = res.getInt("idgender");
                }
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.closeConnectionToMySQL("Person");
        return genderIndex;
    }
    
    public void setGenderIndex(int genderIndex) {
        this.genderIndex = genderIndex;
    }
    
    /*
    RETORNA UNA LISTA CON LOS RESULTADOS DE LA CONSULTA REALIZADA 
    DENTRO DEL METODO
    */
    public String [] getGenderList (){
    
        genderIndex=0;
        sql= "SELECT gender.gender FROM gender WHERE 1 ORDER BY gender.idgender ASC";
        cnx.openConnectionToMySQL("Person");
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) cnx.conn.prepareStatement(sql);
            try (ResultSet res = pstm.executeQuery()) 
            {                            
                while(res.next())
                {                                    
                   cluster.add(genderIndex, res.getString("gender"));
                   genderIndex++;
                }
                
                genderList = cluster.toArray(new String[cluster.size()]);
            }                        
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        
        cnx.closeConnectionToMySQL("Person");
        
        return genderList;
    }
    
    /*
    RETORNA DE LA LISTA EL ELEMENTO UBICADO EN LA POSICION
    ULTIMA ESTABLECIDA PARA EL INDICE
    */
    public String getGenderListItem() {
        return genderList[a];
    }

    public void setGenderList(String[] genderList) {
        this.genderList = genderList;
    }
    
    /*
    RETORNA EL VALOR ULTIMO DEL INDICE UTILIZADO 
    PARA RECORRER LA LISTA DE GENEROS
    */
    public int getIndexGenderList() {
        return a;
    }
    /*
    ESTABLECE EL VALOR DEL INCIDE DONDE SE ENCUENTRA 
    EL ELEMENTO QUE SE DESEA RECUPERAR DE LA LISTA DE GENEROS
    */
    public void setIndexGenderList(int a) {
        this.a = a;
    }
   
    
}
