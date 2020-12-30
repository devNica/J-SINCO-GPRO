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
public class TipoSocio {
    private int IDTIPOSOCIO;
    private String DESCRIPCION, ENTIDAD;
    
    public TipoSocio(){
        this.IDTIPOSOCIO = 1;
        this.DESCRIPCION = "CLIENTE";
        this.ENTIDAD = "PERSONA";
    }

    public int getIDTIPOSOCIO() {
        return IDTIPOSOCIO;
    }

    public void setIDTIPOSOCIO(int IDTIPOSOCIO) {
        this.IDTIPOSOCIO = IDTIPOSOCIO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public String getENTIDAD() {
        return ENTIDAD;
    }

    public void setENTIDAD(String ENTIDAD) {
        this.ENTIDAD = ENTIDAD;
    }
    
}
