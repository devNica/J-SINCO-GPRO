/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.controlador;

import com.sincogpro.administration.SocioPanel;
import com.sincogpro.consultas.ConsultaModeloSocio;
import com.sincogpro.consultas.consultaRegCobroPago;
import com.sincogpro.modelos.RegimenCobro;
import com.sincogpro.modelos.RegimenPago;
import com.sincogpro.modelos.Socio;
import com.sincogpro.utils.JTableHelper;
import com.sincogpro.utils.RowsRenderer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.SwingConstants;

/**
 *
 * @author Alejandro Gonzalez
 */
public class ControladorSocio{
    
    private final SocioPanel SP;
    private final Socio MS;
    private final ConsultaModeloSocio CS;
    private final RegimenCobro RC;
    private final RegimenPago RP;
    EventosTeclado EventoTeclado = new EventosTeclado();
    EventosMouse EventoMouse = new EventosMouse();
    EventosCombo EventoCombo = new EventosCombo();
    consultaRegCobroPago regimen = new consultaRegCobroPago();
    
    public ControladorSocio(Socio MS, ConsultaModeloSocio CS, SocioPanel SP, RegimenCobro RC, RegimenPago RP){
        this.MS = MS;
        this.CS= CS;
        this.SP = SP;
        this.RC = RC;
        this.RP = RP;
        
        /*REGISTRAR CAMPOS DE LA TABLA DE BUSQUEDA*/
        SP.CampoBusquedaSocio.addKeyListener(EventoTeclado);
        SP.CampoBusquedaSocio.addMouseListener(EventoMouse);
        SP.tablaSocioDT.addMouseListener(EventoMouse);
        
        /*REGISTRAR CAMPOS DEL FORMULARIO*/
        SP.personaRbt.addMouseListener(EventoMouse);
        SP.empresaRbt.addMouseListener(EventoMouse);
        SP.clienteChk.addMouseListener(EventoMouse);
        SP.proveedorChk.addMouseListener(EventoMouse);
        SP.empleadoChk.addMouseListener(EventoMouse);
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
        //SP.regimenCobroCmb.addMouseListener(EventoMouse);
        SP.regimenCobroCmb.addItemListener(EventoCombo);
        //SP.regimenPagoCmb.addMouseListener(EventoMouse);
        SP.regimenPagoCmb.addItemListener(EventoCombo);
        SP.limiteCreditoTxt.addKeyListener(EventoTeclado);
        SP.descuentoTxt.addKeyListener(EventoTeclado);
        
        /*REGISTRAR BOTONES*/
        SP.crearBtn.addMouseListener(EventoMouse);
        SP.editarBtn.addMouseListener(EventoMouse);
        SP.cancelarBtn.addMouseListener(EventoMouse);
        SP.guardarBtn.addMouseListener(EventoMouse);
        
        mostrarSocios(1);
        /*CARGAR ITEMS EN LOS COMBOBOX*/
        SP.regimenCobroCmb.setModel(regimen.itemsRegCobro());
        SP.regimenPagoCmb.setModel(regimen.itemsRegPago());
        /*INICIAR EL FORMULAIRO*/
        controlFormulario(1);
    }
    
    private void controlFormulario(int opc){
        
        switch(opc){
            case 1:
                accesoCampos(false);
                inicioCampos();
                SP.crearBtn.setEnabled(true);
                SP.editarBtn.setEnabled(false);
                SP.cancelarBtn.setEnabled(false);
                SP.guardarBtn.setEnabled(false);
                SP.regimenCobroCmb.setEnabled(false);
                SP.regimenPagoCmb.setEnabled(false);
            break;
            
            case 2:
                accesoCampos(true);
                limpiarCampos();
                SP.regimenCobroCmb.setEnabled(true);
                SP.regimenPagoCmb.setEnabled(true);
                SP.crearBtn.setEnabled(false);
                SP.cancelarBtn.setEnabled(true);
                SP.guardarBtn.setEnabled(true);
            break;
            
        }
        
    }
    
    private void accesoCampos(boolean acceso){
        
        SP.empresaRbt.setEnabled(acceso);
        SP.personaRbt.setEnabled(acceso);
        SP.clienteChk.setEnabled(acceso);
        SP.proveedorChk.setEnabled(acceso);
        SP.empleadoChk.setEnabled(acceso);
        
        SP.codigoSocioTxt.setEnabled(acceso);
        SP.codigoSocioTxt.setEditable(false);
        SP.limiteCreditoTxt.setEnabled(acceso);
        SP.descuentoTxt.setEnabled(acceso);
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
    }
    
    private void inicioCampos(){
        MS.setCODIGO(CS.calcularCodigoSocio());
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
        SP.descuentoTxt.setText(String.valueOf(MS.getDESCUENTO()));
        SP.activoChk.setSelected(MS.isACTIVO());
        
    }
    
    private void limpiarCampos(){
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
        SP.descuentoTxt.setText(String.valueOf(0.0));
        SP.activoChk.setSelected(true);
    }
    
    private void guardarInformacion(){
        MS.setRAZONCOMERCIAL(SP.razonComercialTXt.getText());
        MS.setRAZONSOCIAL(SP.razonSocialTxt.getText());
        MS.setCEDULA(SP.cedulaTxt.getText());
        MS.setRUC(SP.rucTxt.getText());
        MS.setDIRECCION(SP.direccionTxt.getText());
        MS.setTELF1(SP.telf1Txt.getText());
        MS.setTELF2(SP.telf2Txt.getText());
        MS.setEMAIL1(SP.email1Txt.getText());
        MS.setEMAIL2(SP.email2Txt.getText());
        MS.setCREDITO(SP.creditoChk.isSelected());
        MS.setLIMITECREDITO(Double.parseDouble(SP.limiteCreditoTxt.getText()));
        MS.setDESCUENTO(Double.parseDouble(SP.descuentoTxt.getText()));
        MS.setACTIVO(SP.activoChk.isSelected());
        MS.setFK_REGCOBRO(RC.getIDREGCOBRO());
        MS.setFK_REGPAGO(RP.getIDREGPAGO());
        /*EL FK_TIPO_SOCIO SE ESTABLECE EN LA FUNCION
        EVALUAR TIPOSOCIO*/
    }
    
    public final void mostrarSocios(int opc){
        
        boolean[] columns= new boolean[6];
        columns[0]=columns[1]=columns[2]=columns[3]=true;
        columns[4]=columns[5]=true;
        
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
    
    private void evaluarEntidad(MouseEvent e, int opc){
        
        switch(opc){
            case 1:
                SP.empleadoChk.setEnabled(false);
                SP.empleadoChk.setSelected(false);

                if(!SP.clienteChk.isSelected() && SP.proveedorChk.isSelected()){
                    evaluarTipoSocio();
                    System.out.println(MS.getFK_TIPOSOCIO());
                }
                else if(SP.clienteChk.isSelected() && !SP.proveedorChk.isSelected()){
                    evaluarTipoSocio();
                    System.out.println(MS.getFK_TIPOSOCIO());
                }
                else if(SP.clienteChk.isSelected() && SP.proveedorChk.isSelected()){
                    evaluarTipoSocio();
                    System.out.println(MS.getFK_TIPOSOCIO());
                }
            break;
            
            case 2:
                SP.empleadoChk.setEnabled(true);
                
                if(SP.proveedorChk.isSelected() && e.getSource() == SP.empleadoChk){
                SP.proveedorChk.setSelected(false);
                evaluarTipoSocio();
                    System.out.println(MS.getFK_TIPOSOCIO());
                }
                else if(SP.empleadoChk.isSelected() && e.getSource() == SP.proveedorChk){
                    SP.empleadoChk.setSelected(false);
                    evaluarTipoSocio();
                    System.out.println(MS.getFK_TIPOSOCIO());
                }
                else if(e.getSource() == SP.personaRbt || e.getSource() == SP.empresaRbt){
                    evaluarTipoSocio();
                    System.out.println(MS.getFK_TIPOSOCIO());
                }

                else if(e.getSource() == SP.clienteChk || e.getSource() == SP.proveedorChk || e.getSource() == SP.empleadoChk){
                    evaluarTipoSocio();
                    System.out.println(MS.getFK_TIPOSOCIO());
                }
                
            break;
        }
        
    }
    
    private void evaluarTipoSocio(){
        if(SP.personaRbt.isSelected()){
            //CLIENTE-PERSONA
            if(SP.clienteChk.isSelected() && !SP.proveedorChk.isSelected() && !SP.empleadoChk.isSelected()){
                MS.setFK_TIPOSOCIO(1);
            }
            //SOCIOEXTERNO
            else if(SP.clienteChk.isSelected() && SP.proveedorChk.isSelected() && !SP.empleadoChk.isSelected()){
                 MS.setFK_TIPOSOCIO(5);
            }
            //SOCIOINTERNO
            else if(SP.clienteChk.isSelected() && !SP.proveedorChk.isSelected() && SP.empleadoChk.isSelected()){
                 MS.setFK_TIPOSOCIO(8);
            }
            //PROVEEDOR-PERSONA
            else if(!SP.clienteChk.isSelected() && SP.proveedorChk.isSelected() && !SP.empleadoChk.isSelected()){
                 MS.setFK_TIPOSOCIO(4);
            }
            //EMPLEADO-PERSONA
            else if(!SP.clienteChk.isSelected() && !SP.proveedorChk.isSelected() && SP.empleadoChk.isSelected()){
                 MS.setFK_TIPOSOCIO(7);
            }
        }
        else if(SP.empresaRbt.isSelected()){
            //CLIENTE-EMPRESA
            if(SP.clienteChk.isSelected() && !SP.proveedorChk.isSelected() && !SP.empleadoChk.isSelected()){
                 MS.setFK_TIPOSOCIO(2);
            }
            //PROVEEDOR-EMPRESA
            else if(!SP.clienteChk.isSelected() && SP.proveedorChk.isSelected() && !SP.empleadoChk.isSelected()){
                 MS.setFK_TIPOSOCIO(3);
            }
            //SOCIOEXTERNO-EMPRESA
            else if(SP.clienteChk.isSelected() && SP.proveedorChk.isSelected() && !SP.empleadoChk.isSelected()){
                 MS.setFK_TIPOSOCIO(6);
            }
        
        }
       
    }
    
    private void edicionSocio(){
        ArrayList<String> socio = new ArrayList<>();
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
        socio.add(String.valueOf(MS.getLIMITECREDITO()));
        socio.add(String.valueOf(MS.getDESCUENTO()));
        socio.add(String.valueOf(MS.getFK_TIPOSOCIO()));
        socio.add(String.valueOf(MS.getFK_REGPAGO()));
        socio.add(String.valueOf(MS.getFK_REGCOBRO()));
        socio.add(String.valueOf(MS.isACTIVO()));
        socio.add(String.valueOf(MS.getIDSOCIO()));
        
        CS.editarSocio(socio);
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
            if(SP.empresaRbt.isSelected()){
                  evaluarEntidad(e, 1);
            }
            if(SP.personaRbt.isSelected()){
                evaluarEntidad(e, 2);
            }
            
            if(e.getSource() == SP.crearBtn){
                controlFormulario(2);
            }
            if(e.getSource() == SP.cancelarBtn){
                controlFormulario(1);
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
        }
    }
    
}




