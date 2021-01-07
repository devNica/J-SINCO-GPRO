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
public class Socio {
    
  private int IDSOCIO;
  private String CODIGO, RAZONSOCIAL, RAZONCOMERCIAL, RUC, CEDULA;
  private String DIRECCION, TELF1, TELF2, EMAIL1, EMAIL2;
  private boolean CREDITO, ACTIVO;
  private double LIMITECREDITO;
  private int FK_TIPOSOCIO, FK_REGPAGO, FK_REGCOBRO, FK_DESCUENTO;

    public Socio() {
        this.CODIGO ="SC-XXXX";
        this.RAZONSOCIAL = "RAZON SOCIAL O NOMBRE DE LA PERSONA";
        this.RAZONCOMERCIAL = "NOMBRE DE LA EMPRESA";
        this.RUC = "N° RUC O NIT";
        this.CEDULA = "CEDULA DE INDETIDAD";
        this.DIRECCION = "DIRECCION DE LA PERSONA O DE LA EMPRESA";
        this.TELF1 = "0000-0000";
        this.TELF2 = "0000-0000";
        this.EMAIL1 = "correo1@sincogpro.com";
        this.EMAIL2 = "correo2@sincogpro.com";
        this.CREDITO = true;
        this.ACTIVO = true;
        this.LIMITECREDITO = 0.00;
        this.FK_DESCUENTO = 1;
        this.FK_REGPAGO = 1;
        this.FK_REGCOBRO = 1;
    }

    public Socio(int IDSOCIO, String CODIGO, 
            String RAZONSOCIAL, 
            String RAZONCOMERCIAL, 
            String RUC, String CEDULA, String DIRECCION, 
            String TELF1, String TELF2, String EMAIL1, 
            String EMAIL2, boolean CREDITO, boolean ACTIVO, 
            double LIMITECREDITO, int FK_TIPOSOCIO, 
            int FK_REGPAGO, int FK_REGCOBRO, int FK_DESCUENTO) {
        
        this.IDSOCIO = IDSOCIO;
        this.CODIGO = CODIGO;
        this.RAZONSOCIAL = RAZONSOCIAL;
        this.RAZONCOMERCIAL = RAZONCOMERCIAL;
        this.RUC = RUC;
        this.CEDULA = CEDULA;
        this.DIRECCION = DIRECCION;
        this.TELF1 = TELF1;
        this.TELF2 = TELF2;
        this.EMAIL1 = EMAIL1;
        this.EMAIL2 = EMAIL2;
        this.CREDITO = CREDITO;
        this.ACTIVO = ACTIVO;
        this.LIMITECREDITO = LIMITECREDITO;
        this.FK_TIPOSOCIO = FK_TIPOSOCIO;
        this.FK_REGPAGO = FK_REGPAGO;
        this.FK_REGCOBRO = FK_REGCOBRO;
        this.FK_DESCUENTO = FK_DESCUENTO;
    }
    
                
    
    public int getIDSOCIO() {
        return IDSOCIO;
    }

    public void setIDSOCIO(int IDSOCIO) {
        this.IDSOCIO = IDSOCIO;
    }

    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }
    
    public String getRAZONSOCIAL() {
        return RAZONSOCIAL;
    }

    public void setRAZONSOCIAL(String RAZONSOCIAL) {
        this.RAZONSOCIAL = RAZONSOCIAL;
    }

    public String getRAZONCOMERCIAL() {
        return RAZONCOMERCIAL;
    }

    public void setRAZONCOMERCIAL(String RAZONCOMERCIAL) {
        this.RAZONCOMERCIAL = RAZONCOMERCIAL;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getCEDULA() {
        return CEDULA;
    }

    public void setCEDULA(String CEDULA) {
        this.CEDULA = CEDULA;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getTELF1() {
        return TELF1;
    }

    public void setTELF1(String TELF1) {
        this.TELF1 = TELF1;
    }

    public String getTELF2() {
        return TELF2;
    }

    public void setTELF2(String TELF2) {
        this.TELF2 = TELF2;
    }

    public String getEMAIL1() {
        return EMAIL1;
    }

    public void setEMAIL1(String EMAIL1) {
        this.EMAIL1 = EMAIL1;
    }

    public String getEMAIL2() {
        return EMAIL2;
    }

    public void setEMAIL2(String EMAIL2) {
        this.EMAIL2 = EMAIL2;
    }

    public boolean isCREDITO() {
        return CREDITO;
    }

    public void setCREDITO(boolean CREDITO) {
        this.CREDITO = CREDITO;
    }

    public boolean isACTIVO() {
        return ACTIVO;
    }

    public void setACTIVO(boolean ACTIVO) {
        this.ACTIVO = ACTIVO;
    }

    public double getLIMITECREDITO() {
        return LIMITECREDITO;
    }

    public void setLIMITECREDITO(double LIMITECREDITO) {
        this.LIMITECREDITO = LIMITECREDITO;
    }

    public int getFK_DESCUENTO() {
        return FK_DESCUENTO;
    }

    public void setFK_DESCUENTO(int FK_DESCUENTO) {
        this.FK_DESCUENTO = FK_DESCUENTO;
    }

    public int getFK_TIPOSOCIO() {
        return FK_TIPOSOCIO;
    }

    public void setFK_TIPOSOCIO(int FK_TIPOSOCIO) {
        this.FK_TIPOSOCIO = FK_TIPOSOCIO;
    }

    public int getFK_REGPAGO() {
        return FK_REGPAGO;
    }

    public void setFK_REGPAGO(int FK_REGPAGO) {
        this.FK_REGPAGO = FK_REGPAGO;
    }

    public int getFK_REGCOBRO() {
        return FK_REGCOBRO;
    }

    public void setFK_REGCOBRO(int FK_REGCOBRO) {
        this.FK_REGCOBRO = FK_REGCOBRO;
    }
    
    @Override
    public String toString(){
        return "\nIDSOCIO: "+IDSOCIO+"\n"
                +"CODIGO: "+CODIGO+"\n"
                +"RAZONSOCIAL: "+RAZONSOCIAL+"\n"
                +"RAZONCOMERCIAL: "+RAZONCOMERCIAL+"\n"
                +"RUC: "+RUC+"\n"
                +"CEDULA: "+CEDULA+"\n"
                +"DIRECCION: "+DIRECCION+"\n"
                +"TELF1: "+TELF1+"\n"
                +"TELF2: "+TELF2+"\n"
                +"EMAIL1: "+EMAIL1+"\n"
                +"EMAIL2: "+EMAIL2+"\n"
                +"CREDITO: "+CREDITO+"\n"
                +"LIMITECREDITO: "+LIMITECREDITO+"\n"
                +"FK_DESCUENTO: "+FK_DESCUENTO+"\n"
                +"FK_TIPOSOCIO: "+FK_TIPOSOCIO+"\n"
                +"FK_REGCOBRO: "+FK_REGCOBRO+"\n"
                +"FK_REGPAGO: "+FK_REGPAGO+"\n"
                +"ACTIVO: "+ACTIVO+"\n";
    }
    
}
