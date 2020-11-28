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

/**
 *
 * @author RESAINVENTARIO
 */
public class Employee {
    private String EmpNumber;
    private String sql;
    connectionToMySQL cnx = new connectionToMySQL();
    private final String Identificador="OBJ-Employee";
    
    public String searchEmpNumber(){
    
        sql="SELECT FN_SUGERIR_NUMEROEMPLEADO() as NE";
        
        System.out.println(sql);
        cnx.openConnectionToMySQL(Identificador);
        
        try
        {
            PreparedStatement pstm = (PreparedStatement) connectionToMySQL.conn.prepareStatement(sql);
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
        
        cnx.closeConnectionToMySQL(Identificador);
        
        return EmpNumber;
    
    }
}
