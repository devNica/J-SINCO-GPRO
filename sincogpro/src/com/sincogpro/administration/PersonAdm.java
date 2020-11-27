/*
Date: 2020/11/19
Company: QueryBirdCode
Developed by Lucas Andres Marsell

 */
package com.sincogpro.administration;

import com.sincogpro.obj.ErrorHandler;
import com.sincogpro.obj.Person;
import com.sincogpro.obj.Position;
import com.sincogpro.utils.JTableHelper;
import com.sincogpro.utils.RowsRenderer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Alejandro Gonzalez
 */
public class PersonAdm extends javax.swing.JFrame{

    Person person = new Person();
    Position position = new  Position();
    String idperson, typeQuery;
    ArrayList<ErrorHandler> errors = new ArrayList<>();
   
    
    private String filter;
    Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    
    public PersonAdm() {
        initComponents();
        
       
        fetchPersons(1);
        
        tyPersonCmb.setModel(person.getComboTyPerson());
        genderCmb.setModel(person.getComboGenderList());
        positionSearchCmb.setModel(position.getComboPositionList());
        AutoCompleteDecorator.decorate(positionSearchCmb);
        
        initOptions(0);
        
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer) optionList.getCellRenderer(); 
        renderer.setHorizontalAlignment(JLabel.CENTER);
        selectDataFromTable();
        
    }
    
    
    private void fetchPersons(int opc)
    {
        /*
        COLUMNS ES UN ARREGLO DE TIPO BOLEANO QUE SE UTILIZA PARA
        CONTROLAR QUE COLUMNAS (TRUE) Y CUALES NO (FALSE) SERAN 
        VISIBLES EN LA TABLA DE DATOS DEL MODELO PERSONAS
        EL METODO "GETPERSON" RECIBE DOS PARAMETROS UN ARREGLO BOLEANO
        QUE DEFINE LAS COLUMNAS ACTIVAS E INACTIVAS DEL MODELO Y UN STRING
        QUE ES UNA CADENA PARA BUSQUEDAS RECURSIVAS
        EL TAMAÑO DEL ARREGLO PUEDE SER MAYOR PERO NUNCA MENOR AL NUMERO 
        DE COLUMNA DE DATOS DEL OBJETO QUE SERA CONSULTADO POR SUS ATRIBUTOS 
        O PROPIEDADES
        */
        boolean[] columns= new boolean[13];
        columns[0]=columns[1]=columns[2]=columns[3]=true;
        columns[4]=columns[5]=columns[6]=columns[7]=false; 
        columns[8]= true; columns[9]=columns[10]= true; columns[11]=false;
        columns[12]=false;
        
        if(opc == 1) filter = "1";
        else{
            String keyType= "'%"+searchText.getText()+"%'";
            filter = "P.nombre like "+keyType+" OR P.apellido like "+keyType+"OR relacion like "+keyType;
        }
       
        /*
        JTableHelper ES UNA CLASE QUE SE UTILIZA PARA CALCULAR EL TAMAÑO IDEAL
        DE LAS COLUMNAS AJUSTADO A SU CONTENIDO
        */
        personDT.setModel(person.getPerson(columns, filter));
        JTableHelper.setOptimalColumnWidth(personDT);
        /*
        RowsRenderer ES UTILIZADO PARA CENTRAR EL CONTENIDO DE CADA COLUMNA EN 
        EL DATATABLE
        */
        RowsRenderer rr = new RowsRenderer();
        personDT.setDefaultRenderer(Object.class, rr);
        rr.setHorizontalAlignment(SwingConstants.CENTER);
        personDT.getColumnModel().getColumn(0).setCellRenderer(rr);  
    }
    
    private void selectDataFromTable(){
        personDT.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int ROW=personDT.getSelectedRow();
            int COLUMN =0;
            
            if(ROW!=-1)
            {
                idperson = personDT.getValueAt(ROW, COLUMN).toString();
            }
        });
    
    }
    
    private void initOptions(int opc){
        /*OPCIONES DE EJECUCION*/
        switch(opc){
            /*
            CUANDO ENTRA POR PRIMERA VEZ AL PROGRAMA
            DESHABILITA LOS ELEMENTOS DEL FORMULARIO
            Y SOLO HABILITA EL BOTON PARA AGREGAR UN 
            NUEVO REGISTRO
            */
            case 0:
              disableTextField();
              disableCheckBox();
              disableComboBox();
              addBtn.setEnabled(true);
              editBtn.setEnabled(false);
              saveBtn.setEnabled(false);
              cancelBtn.setEnabled(false);
              
              
            break;
            
            /*
            ESTA  OPCION SE HABILITA CUANDOSE HA HECHO
            CLIC EN EL BOTON DE AGREGAR UN NUEVO REGISTRO
            HABILITANDO LOS CAMPOS DEL FORMULARIO
            */
            case 1:
                enableTextField();
                enableCheckBox();
                enabledComboBox();
                addBtn.setEnabled(false);
                editBtn.setEnabled(false);
                saveBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
            break;
            
            
            /*
            ESTA OPCION SE EJECUTA CUANDO SE HA CANCELADO CUALQUIERA
            ACCION TANTO DE CREACION COMO DE EDICION.
            */
            case 2:
                resetInitForm();
                disableTextField();
                disableCheckBox();
                disableComboBox();
                addBtn.setEnabled(true);
                editBtn.setEnabled(false);
                saveBtn.setEnabled(false);
                cancelBtn.setEnabled(false);
            break;
            
            
            /*
            ESTA OPCION SE EJECUTA CUANDO SE HACE CLIC
            A LA OPCION DE EDICION EN EL DATA TABLE
            EN ESTA OPCION SOLO SE HABILITA EL 
            BOTON DE EDICION, MIENTRAS EL FORMULARIO
            CARGA LOS DATOS DE LA FILA SELECCIONADA
            MANTENIENDO LOS CAMPOS EN MODO NO EDITABLES
            */
            case 3:
                addBtn.setEnabled(false);
                editBtn.setEnabled(true);
                saveBtn.setEnabled(false);
                cancelBtn.setEnabled(true);
                disableTextField();
                disableCheckBox();
                disableComboBox();
            break;
            
            /*
            ESTA OPCION SE EJECUTA CUANDO SE HACE CLIC EN EL BOTON
            EDITAR, CON LO CUAL SE HABILITAN LOS CAMPOS PARA
            SU POSTERIOR EDICION DE LOS DATOS
            */
            case 4:
                enableTextField();
                enableCheckBox();
                enabledComboBox();
                addBtn.setEnabled(false);
                editBtn.setEnabled(false);
                saveBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
             break;
                
        }
        
    }
    
    private void disableTextField(){
        dniText.setEditable(false);
        firstnameText.setEditable(false);
        lastnameText.setEditable(false);
        countryText.setEditable(false);
        cityText.setEditable(false);
        addressText.setEditable(false);
        phoneText.setEditable(false);
        emailText.setEditable(false);
        ///TYPE//
    }
    
    private void enableTextField(){
        dniText.setEditable(true);
        //dniText.setBackground(Color.BLACK);
        //dniText.setForeground(new Color(102, 204, 255));
        firstnameText.setEditable(true);
        lastnameText.setEditable(true);
        countryText.setEditable(true);
        cityText.setEditable(true);
        addressText.setEditable(true);
        phoneText.setEditable(true);
        emailText.setEditable(true);
        //RELATION//
    }
    
    private void disableCheckBox(){
        staffChk.setSelected(false);
        staffChk.setEnabled(false);
        customerChk.setSelected(false);
        customerChk.setEnabled(false);
        
    }
    
    private void enableCheckBox(){
        staffChk.setEnabled(true);
        customerChk.setEnabled(true);
    }
    
    private void disableComboBox(){
        genderCmb.setSelectedIndex(0);
        genderCmb.setEnabled(false);
        tyPersonCmb.setSelectedIndex(0);
        tyPersonCmb.setEnabled(false);
        
    }
    
    private void enabledComboBox(){
        genderCmb.setEnabled(true);
        tyPersonCmb.setEnabled(true);
    }
    
    private void clearForm(){
        firstnameText.setText("");
        lastnameText.setText("");
        phoneText.setText("");
        addressText.setText("");
        countryText.setText("");
        cityText.setText("");
        emailText.setText("");
        //RELATION//
    }
    
    private void resetInitForm(){
        firstnameText.setText("NOMBRES DE LA PERSONA");
        lastnameText.setText("APELLIDOS");
        phoneText.setText("");
        addressText.setText("DIRECCION FISICA O PERSONAL");
        countryText.setText("PAIS DE ORIGEN");
        cityText.setText("CIUDAD DE ORIGEN");
        emailText.setText("CORREO PERSONAL O DE LA INSTITUCION");
        //////TIPO///
        logText.setText("");
    }
    
    private void checkFields(){
       
        ArrayList<String> err = new ArrayList<>();
        String checkEmailFormat;

        if(dniText.getText().isEmpty() || dniText.getText().equals("   -      -     ")) err.add("¡El Campo Cedula Esta vacio!");
        if(firstnameText.getText().isEmpty() || firstnameText.getText().equals("")) err.add("¡El Campo Nombre Esta Vacio!");
        if(lastnameText.getText().isEmpty() || lastnameText.getText().equals("")) err.add("¡El Campo Apellido Esta vacio!");
        
        if(emailText.getText().isEmpty() || emailText.getText().equals(""))
        {
             err.add("¡El Campo Correo Esta vacio!");
        }
        else
        {
             Matcher mather = pattern.matcher(emailText.getText());
             checkEmailFormat = (mather.find() == true) ? "-" : "¡Formato de Correo Incorrecto!";
             if(!checkEmailFormat.equals("-"))err.add(checkEmailFormat);
        }
        
        if(!customerChk.isSelected() && !staffChk.isSelected()) err.add("¿Es Cliente o Empleado?");

        errors.clear();
       
        for (int i = 0; i < err.size(); i++) {
            ErrorHandler error = new ErrorHandler();
            error.setFormErrors(err.get(i));
            errors.add(error);
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DT_POPUP_OPT = new javax.swing.JPopupMenu();
        jMenu4 = new javax.swing.JMenu();
        editItem = new javax.swing.JMenuItem();
        personToEmployeeItem = new javax.swing.JMenuItem();
        optionsPanel = new javax.swing.JTabbedPane();
        recordsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        personDT = new javax.swing.JTable();
        titleLabel = new javax.swing.JLabel();
        searchLabel = new javax.swing.JLabel();
        searchText = new javax.swing.JTextField();
        registrationPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        dniLabel = new javax.swing.JLabel();
        firstnameLabel = new javax.swing.JLabel();
        lastnameLabel = new javax.swing.JLabel();
        lastnameText = new javax.swing.JTextField();
        phoneLabel = new javax.swing.JLabel();
        birthdayLabel = new javax.swing.JLabel();
        genderLabel = new javax.swing.JLabel();
        genderCmb = new javax.swing.JComboBox<>();
        staffChk = new javax.swing.JCheckBox();
        customerChk = new javax.swing.JCheckBox();
        relationShipLabel = new javax.swing.JLabel();
        saveBtn = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        logText = new javax.swing.JTextArea();
        cancelBtn = new javax.swing.JButton();
        dniText = new javax.swing.JFormattedTextField();
        phoneText = new javax.swing.JFormattedTextField();
        countryLabel = new javax.swing.JLabel();
        countryText = new javax.swing.JTextField();
        cityLabel = new javax.swing.JLabel();
        cityText = new javax.swing.JTextField();
        cityLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        addressText = new javax.swing.JTextArea();
        emailLabel = new javax.swing.JLabel();
        emailText = new javax.swing.JTextField();
        firstnameText = new javax.swing.JTextField();
        editBtn = new javax.swing.JButton();
        tyPersonCmb = new javax.swing.JComboBox<>();
        personToEmployeePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        positionSearchCmb = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        optionList = new javax.swing.JList<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        DT_POPUP_OPT.setToolTipText("Edit");

        jMenu4.setText("options");

        editItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/edit16.png"))); // NOI18N
        editItem.setText("Editar Info");
        editItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemActionPerformed(evt);
            }
        });
        jMenu4.add(editItem);

        personToEmployeeItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/person_to_emp16.png"))); // NOI18N
        personToEmployeeItem.setText("Promover");
        personToEmployeeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                personToEmployeeItemActionPerformed(evt);
            }
        });
        jMenu4.add(personToEmployeeItem);

        DT_POPUP_OPT.add(jMenu4);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        recordsPanel.setBackground(new java.awt.Color(255, 255, 255));

        personDT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        personDT.setComponentPopupMenu(DT_POPUP_OPT);
        jScrollPane1.setViewportView(personDT);

        titleLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("TABLA DE DATOS DE PERSONAS REGISTRADAS");

        searchLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/search16.png"))); // NOI18N
        searchLabel.setText("SEARCH:");

        searchText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchText.setText("ESCRIBE AQUI, SI DESEAS REALIZAR UNA BUSQUEDA");
        searchText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchTextMousePressed(evt);
            }
        });
        searchText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextActionPerformed(evt);
            }
        });
        searchText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout recordsPanelLayout = new javax.swing.GroupLayout(recordsPanel);
        recordsPanel.setLayout(recordsPanelLayout);
        recordsPanelLayout.setHorizontalGroup(
            recordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(recordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, recordsPanelLayout.createSequentialGroup()
                        .addComponent(searchLabel)
                        .addGap(18, 18, 18)
                        .addComponent(searchText)))
                .addContainerGap())
        );
        recordsPanelLayout.setVerticalGroup(
            recordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(18, 18, 18)
                .addGroup(recordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addContainerGap())
        );

        optionsPanel.addTab("REGISTROS", recordsPanel);

        registrationPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FORMULARIO PARA REGISTRO DE DATOS DE PERSONAS");

        jSeparator1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        dniLabel.setForeground(new java.awt.Color(255, 0, 0));
        dniLabel.setText("CEDULA:");

        firstnameLabel.setForeground(new java.awt.Color(255, 0, 0));
        firstnameLabel.setText("NOMBRE:");

        lastnameLabel.setForeground(new java.awt.Color(255, 0, 0));
        lastnameLabel.setText("APELLIDO:");

        lastnameText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lastnameText.setText("APELLIDOS");
        lastnameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastnameTextActionPerformed(evt);
            }
        });

        phoneLabel.setText("TELEFONO:");

        birthdayLabel.setText("TIPO:");

        genderLabel.setForeground(new java.awt.Color(255, 0, 0));
        genderLabel.setText("SEXO:");

        staffChk.setText("ES EMPLEADO");
        staffChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffChkActionPerformed(evt);
            }
        });

        customerChk.setText("ES CLIENTE");

        relationShipLabel.setForeground(new java.awt.Color(255, 0, 0));
        relationShipLabel.setText("RELACION:");

        saveBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/save32.png"))); // NOI18N
        saveBtn.setText("SALVAR");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        addBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/add32.png"))); // NOI18N
        addBtn.setText("CREAR");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        logText.setColumns(20);
        logText.setRows(5);
        jScrollPane3.setViewportView(logText);

        cancelBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/cancel32.png"))); // NOI18N
        cancelBtn.setText("CANCELAR");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        try {
            dniText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###-######-AAAAU")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dniText.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        try {
            phoneText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("+505-####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        phoneText.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        countryLabel.setText("PAIS:");

        countryText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        countryText.setText("PAIS DE ORIGEN");
        countryText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryTextActionPerformed(evt);
            }
        });

        cityLabel.setText("CIUDAD:");

        cityText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cityText.setText("CIUDADE DE ORIGEN");
        cityText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cityTextActionPerformed(evt);
            }
        });

        cityLabel1.setText("DIRECCION:");

        addressText.setColumns(20);
        addressText.setRows(5);
        addressText.setText("DIRECCION FISICA O PERSONAL");
        jScrollPane4.setViewportView(addressText);

        emailLabel.setForeground(new java.awt.Color(255, 0, 0));
        emailLabel.setText("EMAIL:");

        emailText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        emailText.setText("CORREO PERSONAL O DE LA INSTITUCION");
        emailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextActionPerformed(evt);
            }
        });

        firstnameText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        firstnameText.setText("NOMBRES DE LA PERSONA");

        editBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/edit32.png"))); // NOI18N
        editBtn.setText("EDITAR");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout registrationPanelLayout = new javax.swing.GroupLayout(registrationPanel);
        registrationPanel.setLayout(registrationPanelLayout);
        registrationPanelLayout.setHorizontalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(registrationPanelLayout.createSequentialGroup()
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, registrationPanelLayout.createSequentialGroup()
                                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lastnameLabel)
                                    .addComponent(dniLabel)
                                    .addComponent(countryLabel)
                                    .addComponent(cityLabel)
                                    .addComponent(cityLabel1)
                                    .addComponent(emailLabel)
                                    .addComponent(phoneLabel)
                                    .addComponent(firstnameLabel))
                                .addGap(18, 18, 18)
                                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, registrationPanelLayout.createSequentialGroup()
                                        .addComponent(phoneText, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(birthdayLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(tyPersonCmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(emailText)
                                    .addComponent(countryText)
                                    .addComponent(lastnameText)
                                    .addComponent(dniText)
                                    .addComponent(cityText)
                                    .addComponent(jScrollPane4)
                                    .addComponent(firstnameText)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, registrationPanelLayout.createSequentialGroup()
                                .addGap(0, 30, Short.MAX_VALUE)
                                .addComponent(addBtn)
                                .addGap(18, 18, 18)
                                .addComponent(editBtn)
                                .addGap(18, 18, 18)
                                .addComponent(cancelBtn)
                                .addGap(18, 18, 18)
                                .addComponent(saveBtn))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, registrationPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(relationShipLabel)
                                .addGap(18, 18, 18)
                                .addComponent(staffChk)
                                .addGap(18, 18, 18)
                                .addComponent(customerChk, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(genderLabel)
                                .addGap(18, 18, 18)
                                .addComponent(genderCmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        registrationPanelLayout.setVerticalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(registrationPanelLayout.createSequentialGroup()
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dniLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dniText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstnameText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lastnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastnameText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(countryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(countryText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cityText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phoneText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthdayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tyPersonCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(genderCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staffChk, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerChk, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(relationShipLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelBtn)
                            .addComponent(saveBtn)
                            .addComponent(addBtn)
                            .addComponent(editBtn)))
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane3))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        optionsPanel.addTab("FICHA", registrationPanel);

        personToEmployeePanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("PROMOTE REGISTRATION FROM PERSON TO EMPLOYEE");

        positionSearchCmb.setEditable(true);

        javax.swing.GroupLayout personToEmployeePanelLayout = new javax.swing.GroupLayout(personToEmployeePanel);
        personToEmployeePanel.setLayout(personToEmployeePanelLayout);
        personToEmployeePanelLayout.setHorizontalGroup(
            personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personToEmployeePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(personToEmployeePanelLayout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(positionSearchCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(248, Short.MAX_VALUE))
        );
        personToEmployeePanelLayout.setVerticalGroup(
            personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personToEmployeePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(positionSearchCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(428, Short.MAX_VALUE))
        );

        optionsPanel.addTab("PERSONA -> EMPLEADO", personToEmployeePanel);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        optionList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        optionList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(optionList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("LMARSELL-ADMINISTRADOR");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(optionsPanel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(optionsPanel))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextActionPerformed

    private void lastnameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastnameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastnameTextActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        initOptions(1);
        clearForm();
        typeQuery="INSERT";
    }//GEN-LAST:event_addBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        initOptions(2);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void countryTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countryTextActionPerformed

    private void cityTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cityTextActionPerformed

    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        ArrayList<String> data =  new ArrayList<>();
        String log="";
        String readField;
        
        int fk_gender = person.getGenderIndexByDesc(genderCmb.getSelectedItem().toString());
        int fk_tyPerson = person.getTyPersonIndexByDesc(tyPersonCmb.getSelectedItem().toString());
        
        
        checkFields();
        
        if(errors.size() < 1){
            
            data.add(dniText.getText());
            data.add(firstnameText.getText());
            data.add(lastnameText.getText());
            data.add(String.valueOf(fk_tyPerson));
            readField = countryText.getText().isEmpty() ? "n/a" : countryText.getText();
            data.add(readField);
            readField = cityText.getText().isEmpty() ? "n/a" : cityText.getText();
            data.add(readField);
            readField = addressText.getText().isEmpty() ? "n/a" : addressText.getText();
            data.add(readField);
            readField = phoneText.getText().isEmpty() ? "n/a" : phoneText.getText();
            data.add(readField);
            data.add(emailText.getText());
            
            if(customerChk.isSelected() && staffChk.isSelected()){
                data.add("1");
                data.add("1");
            }
            if(!customerChk.isSelected() && staffChk.isSelected()){
                data.add("0");
                data.add("1");
            }
            if(customerChk.isSelected() && !staffChk.isSelected()){
                data.add("1");
                data.add("0");
            }
            
            data.add(String.valueOf(fk_gender));
            
            if(typeQuery.equals("INSERT")) person.createPerson(data);
            else person.updatePerson(data, idperson);
            
            resetInitForm();
        
        }
        else{
            JOptionPane.showMessageDialog(null,"¡Algunos campos requeridos del formulario estan vacios!", "Error",JOptionPane.ERROR_MESSAGE);
            
            for (int i = 0; i < errors.size(); i++) {
                log = log + errors.get(i).getFormErrors()+"\n";
            }
            
            logText.setText(log);
        }
        
    }//GEN-LAST:event_saveBtnActionPerformed

    private void searchTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextKeyReleased
        fetchPersons(2);
    }//GEN-LAST:event_searchTextKeyReleased

    private void searchTextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTextMousePressed
        searchText.setText("");
    }//GEN-LAST:event_searchTextMousePressed

    private void editItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemActionPerformed
        
        optionsPanel.setSelectedIndex(1);
        
        ArrayList<String> data = new ArrayList<>();
        data = person.getPersonById(idperson);
        
        initOptions(3);
        
        dniText.setText(data.get(1));
        firstnameText.setText(data.get(2));
        lastnameText.setText(data.get(3));
        tyPersonCmb.setSelectedIndex(Integer.parseInt(data.get(4))-1);
        countryText.setText(data.get(5));
        cityText.setText(data.get(6));
        addressText.setText(data.get(7));
        phoneText.setText(data.get(8));
        emailText.setText(data.get(9));
        if(data.get(10).equals("CLIENTE/EMPLEADO")) {
            staffChk.setSelected(true);
            customerChk.setSelected(true);
        } 
        if(data.get(10).equals("EMPLEADO")) {
            staffChk.setSelected(true);
            customerChk.setSelected(false);
        } 
        if(data.get(10).equals("CLIENTE")) {
            staffChk.setSelected(false);
            customerChk.setSelected(true);
        }
        genderCmb.setSelectedIndex(Integer.parseInt(data.get(11))-1);
                
        
        
        
    }//GEN-LAST:event_editItemActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        typeQuery="UPDATE";
        initOptions(4);
    }//GEN-LAST:event_editBtnActionPerformed

    private void personToEmployeeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_personToEmployeeItemActionPerformed
       optionsPanel.setSelectedIndex(2);
    }//GEN-LAST:event_personToEmployeeItemActionPerformed

    private void staffChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffChkActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Person.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Person.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Person.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Person.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PersonAdm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu DT_POPUP_OPT;
    private javax.swing.JButton addBtn;
    private javax.swing.JTextArea addressText;
    private javax.swing.JLabel birthdayLabel;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JLabel cityLabel1;
    private javax.swing.JTextField cityText;
    private javax.swing.JLabel countryLabel;
    private javax.swing.JTextField countryText;
    private javax.swing.JCheckBox customerChk;
    private javax.swing.JLabel dniLabel;
    private javax.swing.JFormattedTextField dniText;
    private javax.swing.JButton editBtn;
    private javax.swing.JMenuItem editItem;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailText;
    private javax.swing.JLabel firstnameLabel;
    private javax.swing.JTextField firstnameText;
    private javax.swing.JComboBox<String> genderCmb;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lastnameLabel;
    private javax.swing.JTextField lastnameText;
    private javax.swing.JTextArea logText;
    private javax.swing.JList<String> optionList;
    private javax.swing.JTabbedPane optionsPanel;
    private javax.swing.JTable personDT;
    private javax.swing.JMenuItem personToEmployeeItem;
    private javax.swing.JPanel personToEmployeePanel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JFormattedTextField phoneText;
    private javax.swing.JComboBox<String> positionSearchCmb;
    private javax.swing.JPanel recordsPanel;
    private javax.swing.JPanel registrationPanel;
    private javax.swing.JLabel relationShipLabel;
    private javax.swing.JButton saveBtn;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JTextField searchText;
    private javax.swing.JCheckBox staffChk;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JComboBox<String> tyPersonCmb;
    // End of variables declaration//GEN-END:variables

    
}
