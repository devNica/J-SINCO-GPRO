/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author agonzalezs
 */
public class RowsRenderer extends DefaultTableCellRenderer
{
//    
// private final  int columna ;
//
//public RowsRenderer(int Colpatron)
//{
//    this.columna = Colpatron;
//}
    
//@Override
@Override
public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocused, int row, int column)
{        
    //setBackground(Color.white);
    //table.setForeground(Color.black);
    super.getTableCellRendererComponent(table, value, isSelected, hasFocused, row, column);
    if(table.getValueAt(row,1).equals("DISPONIBLE"))
    {
        setForeground(Color.BLUE);
        if(isSelected){setBackground(Color.WHITE);}
    }else if(table.getValueAt(row,1).equals("RESERVADO")){
        setForeground(Color.RED);
        if(isSelected){setBackground(Color.WHITE);}
    }
    return this;
  }
  }
