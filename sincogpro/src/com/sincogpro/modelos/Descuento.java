/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.modelos;

/**
 *
 * @author Alejandro Gonzalez
 */
public class Descuento {
    
    private int IDDESCUENTO;
    private String DESCUENTO;
    private double PORCENTAJE;
    
    public Descuento(){
        this.IDDESCUENTO = 1;
    }
    
    public String getDESCUENTO() {
        return DESCUENTO;
    }

    public void setDESCUENTO(String DESCUENTO) {
        this.DESCUENTO = DESCUENTO;
    }

    public double getPORCENTAJE() {
        return PORCENTAJE;
    }

    public void setPORCENTAJE(double PORCENTAJE) {
        this.PORCENTAJE = PORCENTAJE;
    }

    public int getIDDESCUENTO() {
        return IDDESCUENTO;
    }

    public void setIDDESCUENTO(int IDDESCUENTO) {
        this.IDDESCUENTO = IDDESCUENTO;
    }
    
}
