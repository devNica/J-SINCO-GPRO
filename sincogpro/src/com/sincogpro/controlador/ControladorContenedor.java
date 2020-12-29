/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sincogpro.controlador;

import com.sincogpro.administration.SocioPanel;
import com.sincogpro.contenedor.FrmPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Alejandro Gonzalez
 */
public class ControladorContenedor implements ActionListener{
    
    private final FrmPrincipal frmPrincipal;
    private final SocioPanel sociopanel;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == frmPrincipal.versociosBtn){
            habilitarVistaSocio();
        }
        if(e.getSource() == frmPrincipal.ocultarSociosBtn){
            inhabilitarVistaSocio();
        }
    }
    
    public ControladorContenedor(FrmPrincipal frmPrincipal, SocioPanel sociopanel){
        
        this.frmPrincipal = frmPrincipal;
        this.sociopanel = sociopanel;
        
        frmPrincipal.PanelContenedor.add(sociopanel);
        frmPrincipal.versociosBtn.addActionListener(this);
        frmPrincipal.ocultarSociosBtn.addActionListener(this);
        
        sociopanel.setVisible(false);
    }
    
    private void habilitarVistaSocio(){
       sociopanel.setVisible(true);
    }
    
    private void inhabilitarVistaSocio(){
       sociopanel.setVisible(false);
    }
    
    
   
}
