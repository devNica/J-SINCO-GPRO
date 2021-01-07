/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.controlador;

import com.sincogpro.administration.SocioPanel;
import com.sincogpro.consultas.ConsultaModeloSocio;
import com.sincogpro.consultas.ConsultaModeloTipoSocio;
import com.sincogpro.consultas.consultaModeloDescuento;
import com.sincogpro.consultas.consultaRegCobroPago;
import com.sincogpro.modelos.Descuento;
import com.sincogpro.modelos.RegimenCobro;
import com.sincogpro.modelos.RegimenPago;
import com.sincogpro.modelos.Socio;
import com.sincogpro.modelos.TipoSocio;
import com.sincogpro.utils.JTableHelper;
import com.sincogpro.utils.RowsRenderer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author Alejandro Gonzalez
 */
public class CRUDSOCIO{
    
    private final SocioPanel SP;
    private final Socio MS;
    private final ConsultaModeloSocio CS;
    private final RegimenCobro RC = new RegimenCobro();
    private final RegimenPago RP = new RegimenPago();
    private final Descuento DE = new Descuento();
    private final TipoSocio TS = new TipoSocio();
    
    EventosTeclado EventoTeclado = new EventosTeclado();
    EventosMouse EventoMouse = new EventosMouse();
    EventosCombo EventoCombo = new EventosCombo();
    consultaRegCobroPago regimen = new consultaRegCobroPago();
    consultaModeloDescuento descuento = new consultaModeloDescuento();
    ConsultaModeloTipoSocio tiposocio = new ConsultaModeloTipoSocio();
    Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    
    String IDSOCIO=null;
    boolean EDICION=false;
    
    public CRUDSOCIO(Socio MS, ConsultaModeloSocio CS, SocioPanel SP){
        this.MS = MS;
        this.CS = CS;
        this.SP = SP;
        
        /*REGISTRAR CAMPOS DE LA TABLA DE BUSQUEDA*/
        SP.CampoBusquedaSocio.addKeyListener(EventoTeclado);
        SP.CampoBusquedaSocio.addMouseListener(EventoMouse);
        //SP.tablaSocioDT.addMouseListener(EventoMouse);
        //SP.tablaSocioDT.getSelectionModel().addListSelectionListener(EventoTabla);
        SP.tablaSocioDT.addMouseListener(EventoMouse);
        
        /*REGISTRAR CAMPOS DEL FORMULARIO*/
        SP.entidadCmb.addItemListener(EventoCombo);
        SP.tipoSocioCmb.addItemListener(EventoCombo);
        SP.codigoSocioTxt.addKeyListener(EventoTeclado);
        SP.razonSocialTxt.addKeyListener(EventoTeclado);
        SP.razonComercialTXt.addKeyListener(EventoTeclado);
        SP.cedulaTxt.addKeyListener(EventoTeclado);
        SP.rucTxt.addKeyListener(EventoTeclado);
        SP.direccionTxt.addKeyListener(EventoTeclado);
        SP.telf1Txt.addKeyListener(EventoTeclado);
        SP.telf2Txt.addKeyListener(EventoTeclado);
        SP.email1Txt.addKeyListener(EventoTeclado);
        SP.email2Txt.addKeyListener(EventoTeclado);
        SP.creditoChk.addMouseListener(EventoMouse);
        SP.activoChk.addMouseListener(EventoMouse);
        SP.regimenCobroCmb.addItemListener(EventoCombo);
        SP.regimenPagoCmb.addItemListener(EventoCombo);
        SP.limiteCreditoTxt.addKeyListener(EventoTeclado);
        SP.descuentoCmb.addItemListener(EventoCombo);
        
        SP.editarSocio.addMouseListener(EventoMouse);
        
        /*REGISTRAR BOTONES*/
        SP.crearBtn.addMouseListener(EventoMouse);
        SP.editarBtn.addMouseListener(EventoMouse);
        SP.cancelarBtn.addMouseListener(EventoMouse);
        SP.guardarBtn.addMouseListener(EventoMouse);
        
        mostrarSocios(1);
        //seleccionarDatosFilaTabla();
        /*CARGAR ITEMS EN LOS COMBOBOX*/
        SP.regimenCobroCmb.setModel(regimen.itemsRegCobro());
        SP.regimenPagoCmb.setModel(regimen.itemsRegPago());
        SP.descuentoCmb.setModel(descuento.itemsDescuento());
        SP.entidadCmb.setModel(tiposocio.itemsEntidad());
        SP.tipoSocioCmb.setModel(tiposocio.itemsTipoSocio(TS.getENTIDAD()));
        
        /*INICIAR EL FORMULAIRO*/
        controlFormulario("INICIALIZAR-FORMULARIO");
        
        
        
    }
    
    private void controlFormulario(String opc){
        
        switch(opc){
            case "INICIALIZAR-FORMULARIO":
                accesoCampos(false, 0);
                inicioCampos();
                SP.crearBtn.setEnabled(true);
                SP.editarBtn.setEnabled(false);
                SP.cancelarBtn.setEnabled(false);
                SP.guardarBtn.setEnabled(false);
                
            break;
            
            case "INICIAR-CREACION":
                accesoCampos(true, 0);
                limpiarCampos();
                SP.codigoSocioTxt.setText(CS.calcularCodigoSocio());
                SP.crearBtn.setEnabled(false);
                SP.cancelarBtn.setEnabled(true);
                SP.guardarBtn.setEnabled(true);
            break;
            
            case "CREAR":
                MS.setIDSOCIO(Integer.parseInt(CS.calcularIdSocio()));
                crearNuevoSocio();
                reestablecerSocio();
                limpiarCampos();
                SP.codigoSocioTxt.setText(CS.calcularCodigoSocio());
            break;
            
            case "INICIAR-EDICION":
                CS.obtenerSocio(IDSOCIO, MS, RC, RP, TS, DE);
                establecerCampos();
                SP.FichaSocio.setSelectedIndex(1);
                SP.crearBtn.setEnabled(false);
                SP.editarBtn.setEnabled(true);
            break;
            
            case "HABILITAR-EDICION-CAMPOS":
                SP.editarBtn.setEnabled(false);
                SP.cancelarBtn.setEnabled(true);
                SP.guardarBtn.setEnabled(true);
                accesoCampos(true, 1);
            break;
            
            case "EDITAR":
                edicionSocio();
                //reestablecerSocio();
                //limpiarCampos();
                controlFormulario("INICIALIZAR-FORMULARIO");
            break;
            
        }
        
    }
    
    
    private void accesoCampos(boolean acceso, int opc){
        
        SP.entidadCmb.setEnabled(acceso);
        SP.tipoSocioCmb.setEnabled(acceso);
        SP.codigoSocioTxt.setEnabled(acceso);
        SP.codigoSocioTxt.setEditable(false);
        SP.razonSocialTxt.setEnabled(acceso);
        SP.razonComercialTXt.setEnabled(acceso);
        SP.cedulaTxt.setEnabled(acceso);
        SP.rucTxt.setEnabled(acceso);
        SP.direccionTxt.setEnabled(acceso);
        SP.telf1Txt.setEnabled(acceso);
        SP.telf2Txt.setEnabled(acceso);
        SP.email1Txt.setEnabled(acceso);
        SP.email2Txt.setEnabled(acceso);
        SP.activoChk.setEnabled(acceso);
        SP.creditoChk.setEnabled(acceso);
        
        if(opc<1){
            SP.limiteCreditoTxt.setEnabled(acceso);
        }
        else{
            SP.limiteCreditoTxt.setEnabled(MS.isCREDITO());
        }
        
        SP.regimenCobroCmb.setEnabled(acceso);
        SP.regimenPagoCmb.setEnabled(acceso);
        SP.descuentoCmb.setEnabled(acceso);
    }
    
    private void inicioCampos(){
        
        SP.codigoSocioTxt.setText(MS.getCODIGO());
        SP.razonSocialTxt.setText(MS.getRAZONSOCIAL());
        SP.razonComercialTXt.setText(MS.getRAZONCOMERCIAL());
        SP.cedulaTxt.setText(MS.getCEDULA());
        SP.rucTxt.setText(MS.getRUC());
        SP.direccionTxt.setText(MS.getDIRECCION());
        SP.telf1Txt.setText(MS.getTELF1());
        SP.telf2Txt.setText(MS.getTELF2());
        SP.email1Txt.setText(MS.getEMAIL1());
        SP.email2Txt.setText(MS.getEMAIL2());
        SP.creditoChk.setSelected(MS.isCREDITO());
        SP.limiteCreditoTxt.setText(String.valueOf(MS.getLIMITECREDITO()));
        SP.activoChk.setSelected(MS.isACTIVO());
        
        SP.descuentoCmb.setSelectedIndex(0);
        SP.regimenCobroCmb.setSelectedIndex(0);
        SP.regimenPagoCmb.setSelectedIndex(0);
        
    }
    
    private void limpiarCampos(){
        SP.entidadCmb.setSelectedIndex(0);
        SP.tipoSocioCmb.setSelectedIndex(0);
        
        SP.razonSocialTxt.setText("");
        SP.razonComercialTXt.setText("");
        SP.cedulaTxt.setText("");
        SP.rucTxt.setText("");
        SP.direccionTxt.setText("");
        SP.telf1Txt.setText(MS.getTELF1());
        SP.telf2Txt.setText(MS.getTELF2());
        SP.email1Txt.setText("");
        SP.email2Txt.setText("");
        SP.limiteCreditoTxt.setText(String.valueOf(0.0));
        SP.activoChk.setSelected(true);
        
        SP.descuentoCmb.setSelectedIndex(0);
        SP.regimenCobroCmb.setSelectedIndex(0);
        SP.regimenPagoCmb.setSelectedIndex(0);
    }
    
    private void establecerCampos(){
        SP.codigoSocioTxt.setText(MS.getCODIGO());
        SP.razonSocialTxt.setText(MS.getRAZONSOCIAL());
        SP.razonComercialTXt.setText(MS.getRAZONCOMERCIAL());
        SP.cedulaTxt.setText(MS.getCEDULA());
        SP.rucTxt.setText(MS.getRUC());
        SP.direccionTxt.setText(MS.getDIRECCION());
        SP.telf1Txt.setText(MS.getTELF1());
        SP.telf2Txt.setText(MS.getTELF2());
        SP.email1Txt.setText(MS.getEMAIL1());
        SP.email2Txt.setText(MS.getEMAIL2());
        SP.creditoChk.setSelected(MS.isCREDITO());
        SP.limiteCreditoTxt.setText(String.valueOf(MS.getLIMITECREDITO()));
        SP.activoChk.setSelected(MS.isACTIVO());
        
        SP.descuentoCmb.setSelectedIndex(MS.getFK_DESCUENTO()-1);        
        SP.regimenCobroCmb.setSelectedIndex(MS.getFK_REGCOBRO()-1);
        SP.regimenPagoCmb.setSelectedIndex(MS.getFK_REGPAGO()-1);
        
        if(MS.getFK_TIPOSOCIO() == 1 || MS.getFK_TIPOSOCIO() == 4 || MS.getFK_TIPOSOCIO() == 5 || MS.getFK_TIPOSOCIO() == 7 || MS.getFK_TIPOSOCIO() == 8){
            SP.entidadCmb.setSelectedIndex(0);
            SP.tipoSocioCmb.setModel(tiposocio.itemsTipoSocio(SP.entidadCmb.getSelectedItem().toString()));
            SP.tipoSocioCmb.setSelectedItem(tiposocio.tipoSocio(MS.getFK_TIPOSOCIO()));
        }
        else if(MS.getFK_TIPOSOCIO() == 2 || MS.getFK_TIPOSOCIO() == 3 || MS.getFK_TIPOSOCIO() == 6){
            SP.entidadCmb.setSelectedIndex(1);
            SP.tipoSocioCmb.setModel(tiposocio.itemsTipoSocio(SP.entidadCmb.getSelectedItem().toString()));
            SP.tipoSocioCmb.setSelectedItem(tiposocio.tipoSocio(MS.getFK_TIPOSOCIO()));
        }
    }
    
    
    private boolean revisarFormatosCorreos(String email1, String email2){
        
        boolean revision = false;
        
        if(email1.equals("") && email2.equals("")){
            revision = true;
        }
        else if(email1.length()> 0 && email2.equals("")){
            Matcher mather = pattern.matcher(email1);
            revision = mather.find();
        }
        else if(email1.equals("") && email2.length() > 0){
            Matcher mather = pattern.matcher(email2);
            revision = mather.find();
        }
        else if(email1.length() > 0 && email2.length() > 0){
            Matcher mather1 = pattern.matcher(email1);
            Matcher mather2 = pattern.matcher(email2);
            revision = mather1.find() && mather2.find();
        }
        
        return  revision;
       
    }
    
    private void guardarInformacionCampos(boolean opc){
        
        String direccion, razoncomercial, cedula, ruc, telf1, telf2;
        boolean revisioncorreo;
        direccion = SP.direccionTxt.getText().equals("") ? "-" : SP.direccionTxt.getText().trim();
        razoncomercial = SP.razonComercialTXt.getText().equals("") ? "-" : SP.razonComercialTXt.getText().trim();
        cedula = SP.cedulaTxt.getText().equals("") ? "-" : SP.cedulaTxt.getText().trim();
        ruc = SP.rucTxt.getText().equals("") ? "-" : SP.rucTxt.getText().trim();
        telf1 = SP.telf1Txt.getText().equals("0000-0000") ? "(505)-0000-0000" : "(505)-"+SP.telf1Txt.getText().trim();
        telf2 = SP.telf2Txt.getText().equals("0000-0000") ? "(505)-0000-0000" : "(505)-"+SP.telf2Txt.getText().trim();
        
        System.out.println("telf1: "+telf1+" telf2: "+telf2);
        
        revisioncorreo = revisarFormatosCorreos(SP.email1Txt.getText().trim(), SP.email2Txt.getText().trim());
        
        if(SP.razonSocialTxt.getText().trim().equals("")){
               JOptionPane.showMessageDialog(null,
                            "No se puede crear el registro, no se ha proporcionado un Nombre o Razon Social para identificar a la persona o empresa",
                            "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        else{
            
            if(revisioncorreo){
            
            MS.setRAZONCOMERCIAL(razoncomercial);
            MS.setRAZONSOCIAL(SP.razonSocialTxt.getText().trim());
            MS.setCEDULA(cedula);
            MS.setRUC(ruc);
            MS.setDIRECCION(direccion);
            MS.setTELF1(telf1);
            MS.setTELF2(telf2);
            MS.setEMAIL1(SP.email1Txt.getText());
            MS.setEMAIL2(SP.email2Txt.getText());
            MS.setCREDITO(SP.creditoChk.isSelected());
            MS.setLIMITECREDITO(Double.parseDouble(SP.limiteCreditoTxt.getText()));
            MS.setFK_DESCUENTO(DE.getIDDESCUENTO());
            MS.setACTIVO(SP.activoChk.isSelected());
            MS.setFK_REGCOBRO(RC.getIDREGCOBRO());
            MS.setFK_REGPAGO(RP.getIDREGPAGO());
            MS.setFK_TIPOSOCIO(TS.getIDTIPOSOCIO());
            
            if(opc){
                controlFormulario("EDITAR");
            }
            else{
                controlFormulario("CREAR");
                MS.setCODIGO(CS.calcularCodigoSocio());
            }
            
        
            }
            else{

                JOptionPane.showMessageDialog(null,
                            "El formato de una de las direcciones de correo es incorrecto",
                            "Error de formato de correo",
                JOptionPane.ERROR_MESSAGE);
            }
        }
        
        
        
    }
    
    public final void mostrarSocios(int opc){
        
        boolean[] columns= new boolean[8];
        columns[0]=columns[1]=columns[2]=columns[3]=true;
        columns[4]=columns[5]=columns[6]=columns[7]=true;
        
        String filter;
        
        if (opc == 1) filter = "";
        else  filter = SP.CampoBusquedaSocio.getText();
        
        SP.tablaSocioDT.setModel(CS.obtenerSocios(columns, filter));
        JTableHelper.setOptimalColumnWidth(SP.tablaSocioDT);
        
        RowsRenderer rr = new RowsRenderer();
        SP.tablaSocioDT.setDefaultRenderer(Object.class, rr);
        rr.setHorizontalAlignment(SwingConstants.CENTER);
        SP.tablaSocioDT.getColumnModel().getColumn(0).setCellRenderer(rr);  
        
        
    }
    
    private void evaluarTipoSocio(){
        /*PERSONA-CLIENTE*/
        if(SP.entidadCmb.getSelectedIndex() == 0 && SP.tipoSocioCmb.getSelectedIndex() == 0){
            TS.setIDTIPOSOCIO(1);
            System.out.println(TS.getIDTIPOSOCIO());
        }
        /*PERSONA-PROVEEDOR*/
        if(SP.entidadCmb.getSelectedIndex() == 0 && SP.tipoSocioCmb.getSelectedIndex() == 1){
            TS.setIDTIPOSOCIO(4);
            System.out.println(TS.getIDTIPOSOCIO());
        }
        /*PERSONA-CLIENTE/PROVEEDOR*/
        if(SP.entidadCmb.getSelectedIndex() == 0 && SP.tipoSocioCmb.getSelectedIndex() == 2){
            TS.setIDTIPOSOCIO(5);
            System.out.println(TS.getIDTIPOSOCIO());
        }
        /*PERSONA-EMPLEADO*/
        if(SP.entidadCmb.getSelectedIndex() == 0 && SP.tipoSocioCmb.getSelectedIndex() == 3){
            TS.setIDTIPOSOCIO(7);
            System.out.println(TS.getIDTIPOSOCIO());
        }
        /*PERSONA-CLIENTE/EMPLEADO*/
        if(SP.entidadCmb.getSelectedIndex() == 0 && SP.tipoSocioCmb.getSelectedIndex() == 4){
            TS.setIDTIPOSOCIO(8);
            System.out.println(TS.getIDTIPOSOCIO());
        }
        /*EMPRESA-CLIENTE*/
        if(SP.entidadCmb.getSelectedIndex() == 1 && SP.tipoSocioCmb.getSelectedIndex() == 0){
            TS.setIDTIPOSOCIO(2);
            System.out.println(TS.getIDTIPOSOCIO());
        }
        /*EMPRESA-PROVEEDOR*/
        if(SP.entidadCmb.getSelectedIndex() == 1 && SP.tipoSocioCmb.getSelectedIndex() == 1){
            TS.setIDTIPOSOCIO(3);
            System.out.println(TS.getIDTIPOSOCIO());
        }
        /*EMPRESA-CLIENTE/PROVEEDOR*/
        if(SP.entidadCmb.getSelectedIndex() == 1 && SP.tipoSocioCmb.getSelectedIndex() == 2){
            TS.setIDTIPOSOCIO(6);
            System.out.println(TS.getIDTIPOSOCIO());
        }
    }
    
    
    
    private void crearNuevoSocio(){
        ArrayList<String> socio = new ArrayList<>();
        String isCREDITO,isACTIVO;
        isCREDITO = MS.isCREDITO() ? "1" : "0";
        isACTIVO = MS.isACTIVO()? "1" : "0";
        
        socio.add(String.valueOf(MS.getIDSOCIO()));
        socio.add(MS.getCODIGO());
        socio.add(MS.getRAZONSOCIAL());
        socio.add(MS.getRAZONCOMERCIAL());
        socio.add(MS.getRUC());
        socio.add(MS.getCEDULA());
        socio.add(MS.getDIRECCION());
        socio.add(MS.getTELF1());
        socio.add(MS.getTELF2());
        socio.add(MS.getEMAIL1());
        socio.add(MS.getEMAIL2());
        socio.add(isCREDITO);
        socio.add(String.valueOf(MS.getLIMITECREDITO()));
        socio.add(String.valueOf(MS.getFK_DESCUENTO()));
        socio.add(String.valueOf(MS.getFK_TIPOSOCIO()));
        socio.add(String.valueOf(MS.getFK_REGPAGO()));
        socio.add(String.valueOf(MS.getFK_REGCOBRO()));
        socio.add(isACTIVO);
        
        CS.crearSocio(socio);
    }
    
    private void edicionSocio(){
        ArrayList<String> socio = new ArrayList<>();
        socio.add(MS.getRAZONSOCIAL());
        socio.add(MS.getRAZONCOMERCIAL());
        socio.add(MS.getRUC());
        socio.add(MS.getCEDULA());
        socio.add(MS.getDIRECCION());
        socio.add(MS.getTELF1());
        socio.add(MS.getTELF2());
        socio.add(MS.getEMAIL1());
        socio.add(MS.getEMAIL2());
        if(MS.isCREDITO()) socio.add("1");
        else socio.add("0");
        socio.add(String.valueOf(MS.getLIMITECREDITO()));
        socio.add(String.valueOf(MS.getFK_DESCUENTO()));
        socio.add(String.valueOf(MS.getFK_TIPOSOCIO()));
        socio.add(String.valueOf(MS.getFK_REGPAGO()));
        socio.add(String.valueOf(MS.getFK_REGCOBRO()));
        if(MS.isACTIVO()) socio.add("1");
        else socio.add("0");
        socio.add(String.valueOf(MS.getIDSOCIO()));
        
        CS.editarSocio(socio);
    }
    
    private void reestablecerSocio(){
        MS.setCODIGO("SC-XXXX");
        MS.setRAZONSOCIAL("RAZON SOCIAL O NOMBRE DE LA PERSONA");
        MS.setRAZONCOMERCIAL("NOMBRE DE LA EMPRESA");
        MS.setRUC("N° RUC O NIT");
        MS.setCEDULA("CEDULA DE INDETIDAD");
        MS.setDIRECCION("DIRECCION DE LA PERSONA O DE LA EMPRESA");
        MS.setTELF1("0000-0000");
        MS.setTELF2("0000-0000");
        MS.setEMAIL1("correo1@sincogpro.com");
        MS.setEMAIL2("correo2@sincogpro.com");
        MS.setCREDITO(false);
        MS.setACTIVO(true);
        MS.setLIMITECREDITO(0.00);
        MS.setFK_DESCUENTO(1);
        MS.setFK_REGPAGO(1);
        MS.setFK_REGCOBRO(1);
    }
    
    private void limpiarCampoBusqueda(){
       SP.CampoBusquedaSocio.setText("");
    }
    
    private class EventosTeclado extends KeyAdapter{
        
        @Override
        public void keyReleased(KeyEvent e){
            if(e.getSource() == SP.CampoBusquedaSocio){
                 mostrarSocios(2);
            }
           
        }
        
    }
    
    private class EventosMouse extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource() == SP.CampoBusquedaSocio){
                limpiarCampoBusqueda();
            }
            
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            
            if(e.getSource() == SP.crearBtn && SP.crearBtn.isEnabled()){
                EDICION = false;
                controlFormulario("INICIAR-CREACION");
            }
            if(e.getSource() == SP.cancelarBtn && SP.cancelarBtn.isEnabled()){
                EDICION = false;
                controlFormulario("INICIALIZAR-FORMULARIO");
            }
            if(e.getSource() == SP.guardarBtn && SP.guardarBtn.isEnabled()){
                guardarInformacionCampos(EDICION);
                System.out.println("EDICION: "+EDICION);
            }
            
            if(e.getSource() == SP.editarBtn && SP.editarBtn.isEnabled()){
                controlFormulario("HABILITAR-EDICION-CAMPOS");
            }
            
            if(e.getSource() == SP.creditoChk){
                if(SP.creditoChk.isSelected()){
                    SP.limiteCreditoTxt.setText("0.00");
                    SP.limiteCreditoTxt.setEnabled(true);
                }
                else{
                    SP.limiteCreditoTxt.setText("0.00");
                    SP.limiteCreditoTxt.setEnabled(false);
                }
            }
            
            if(e.getSource() == SP.tablaSocioDT){
                imprimirCodigo();
            }
            
            if(e.getSource() == SP.editarSocio){
                EDICION = true;
                controlFormulario("INICIAR-EDICION");
            }
            
        }
        
        
    }
    
    private class EventosCombo implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getSource() == SP.regimenCobroCmb && e.getStateChange() == 2){
               RC.setIDREGCOBRO(SP.regimenCobroCmb.getSelectedIndex()+1);
               RC.setREGIMENCOBRO(SP.regimenCobroCmb.getSelectedItem().toString());
            }
            if(e.getSource() == SP.regimenPagoCmb && e.getStateChange() == 2){
               RP.setIDREGPAGO(SP.regimenPagoCmb.getSelectedIndex()+1);
               RP.setREGIMENPAGO(SP.regimenPagoCmb.getSelectedItem().toString());
            }
            if(e.getSource() == SP.descuentoCmb && e.getStateChange()== 2){
                DE.setIDDESCUENTO(SP.descuentoCmb.getSelectedIndex()+1);
                DE.setDESCUENTO(SP.descuentoCmb.getSelectedItem().toString());
            }
            if(e.getSource() == SP.entidadCmb && e.getStateChange() == 2){
                SP.tipoSocioCmb.setModel(tiposocio.itemsTipoSocio(SP.entidadCmb.getSelectedItem().toString()));
                evaluarTipoSocio();
            }
            if(e.getSource() == SP.tipoSocioCmb && e.getStateChange() == 2){
                evaluarTipoSocio();
            }
        }
    }
    
//    private class EventosTabla implements ListSelectionListener{
//
//        @Override
//        public void valueChanged(ListSelectionEvent e) {
//            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
//        }
//    }
    
    private void imprimirCodigo(){
        int ROW=SP.tablaSocioDT.getSelectedRow();
        if(ROW!=-1)
        {
            IDSOCIO = SP.tablaSocioDT.getValueAt(ROW, 0).toString();
        }
    }
    
}




