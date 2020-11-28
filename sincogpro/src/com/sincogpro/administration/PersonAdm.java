/*
Date: 2020/11/19
Company: QueryBirdCode
Developed by Lucas Andres Marsell

 */
package com.sincogpro.administration;

import com.sincogpro.obj.Coin;
import com.sincogpro.obj.Degree;
import com.sincogpro.obj.Employee;
import com.sincogpro.obj.ErrorHandler;
import com.sincogpro.obj.Organization;
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
    Organization organization = new Organization();
    Degree degree = new Degree();
    Coin coin = new Coin();
    Employee employee = new Employee();
    String IDPERSON, typeQuery, FULLNAME;
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
        officeSearchCmb.setModel(organization.getCmbOfficeList());
        degreeSearchCmb.setModel(degree.getComboDegreeList());
        coinSearchCmb.setModel(coin.getComboCoinList());
        
        AutoCompleteDecorator.decorate(positionSearchCmb);
        AutoCompleteDecorator.decorate(officeSearchCmb);
        AutoCompleteDecorator.decorate(degreeSearchCmb);
        AutoCompleteDecorator.decorate(coinSearchCmb);
        
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
            
            if(ROW!=-1)
            {
                IDPERSON = personDT.getValueAt(ROW, 0).toString();
                FULLNAME = personDT.getValueAt(ROW, 2).toString()+" "+personDT.getValueAt(ROW, 3).toString();
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
              addPersonBtn.setEnabled(true);
              editPersonBtn.setEnabled(false);
              savePersonBtn.setEnabled(false);
              cancelPersonBtn.setEnabled(false);
              
              
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
                addPersonBtn.setEnabled(false);
                editPersonBtn.setEnabled(false);
                savePersonBtn.setEnabled(true);
                cancelPersonBtn.setEnabled(true);
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
                addPersonBtn.setEnabled(true);
                editPersonBtn.setEnabled(false);
                savePersonBtn.setEnabled(false);
                cancelPersonBtn.setEnabled(false);
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
                addPersonBtn.setEnabled(false);
                editPersonBtn.setEnabled(true);
                savePersonBtn.setEnabled(false);
                cancelPersonBtn.setEnabled(true);
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
                addPersonBtn.setEnabled(false);
                editPersonBtn.setEnabled(false);
                savePersonBtn.setEnabled(true);
                cancelPersonBtn.setEnabled(true);
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
        dniText.setText("   -      -     ");
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
        dniText.setText("   -      -     ");
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
        savePersonBtn = new javax.swing.JButton();
        addPersonBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        logText = new javax.swing.JTextArea();
        cancelPersonBtn = new javax.swing.JButton();
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
        editPersonBtn = new javax.swing.JButton();
        tyPersonCmb = new javax.swing.JComboBox<>();
        personToEmployeePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        positionSearchCmb = new javax.swing.JComboBox<>();
        positionLabel = new javax.swing.JLabel();
        employeeNameLabel = new javax.swing.JLabel();
        employeeNameText = new javax.swing.JTextField();
        oficeLabel = new javax.swing.JLabel();
        officeSearchCmb = new javax.swing.JComboBox<>();
        dniEmployeeLabel = new javax.swing.JLabel();
        dniEmployeeText = new javax.swing.JTextField();
        degreeLabel = new javax.swing.JLabel();
        degreeSearchCmb = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        coinSearchCmb = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        addEmployeeBtn = new javax.swing.JButton();
        editEmployeeBtn = new javax.swing.JButton();
        cancelEmployeeBtn = new javax.swing.JButton();
        saveEmployeeBtn = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        optionList = new javax.swing.JList<>();
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addContainerGap())
        );

        optionsPanel.addTab("REGISTROS", recordsPanel);

        registrationPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FORMULARIO PARA REGISTRO DE DATOS DE PERSONAS");

        jSeparator1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        dniLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        dniLabel.setForeground(new java.awt.Color(255, 0, 0));
        dniLabel.setText("CEDULA:");

        firstnameLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        firstnameLabel.setForeground(new java.awt.Color(255, 0, 0));
        firstnameLabel.setText("NOMBRE:");

        lastnameLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lastnameLabel.setForeground(new java.awt.Color(255, 0, 0));
        lastnameLabel.setText("APELLIDO:");

        lastnameText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lastnameText.setText("APELLIDOS");
        lastnameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastnameTextActionPerformed(evt);
            }
        });

        phoneLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        phoneLabel.setForeground(new java.awt.Color(0, 51, 153));
        phoneLabel.setText("TELEFONO:");

        birthdayLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        birthdayLabel.setForeground(new java.awt.Color(0, 51, 153));
        birthdayLabel.setText("TIPO:");

        genderLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        genderLabel.setForeground(new java.awt.Color(255, 0, 0));
        genderLabel.setText("SEXO:");

        staffChk.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        staffChk.setForeground(new java.awt.Color(0, 51, 153));
        staffChk.setText("ES EMPLEADO");
        staffChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffChkActionPerformed(evt);
            }
        });

        customerChk.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        customerChk.setForeground(new java.awt.Color(0, 51, 153));
        customerChk.setText("ES CLIENTE");

        relationShipLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        relationShipLabel.setForeground(new java.awt.Color(255, 0, 0));
        relationShipLabel.setText("RELACION:");

        savePersonBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/save32.png"))); // NOI18N
        savePersonBtn.setText("SALVAR");
        savePersonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePersonBtnActionPerformed(evt);
            }
        });

        addPersonBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/add32.png"))); // NOI18N
        addPersonBtn.setText("CREAR");
        addPersonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPersonBtnActionPerformed(evt);
            }
        });

        logText.setColumns(20);
        logText.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        logText.setRows(5);
        logText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 51)));
        jScrollPane3.setViewportView(logText);

        cancelPersonBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/cancel32.png"))); // NOI18N
        cancelPersonBtn.setText("CANCELAR");
        cancelPersonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelPersonBtnActionPerformed(evt);
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

        countryLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        countryLabel.setForeground(new java.awt.Color(0, 51, 153));
        countryLabel.setText("PAIS:");

        countryText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        countryText.setText("PAIS DE ORIGEN");
        countryText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryTextActionPerformed(evt);
            }
        });

        cityLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cityLabel.setForeground(new java.awt.Color(0, 51, 153));
        cityLabel.setText("CIUDAD:");

        cityText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cityText.setText("CIUDADE DE ORIGEN");
        cityText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cityTextActionPerformed(evt);
            }
        });

        cityLabel1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cityLabel1.setForeground(new java.awt.Color(0, 51, 153));
        cityLabel1.setText("DIRECCION:");

        addressText.setColumns(20);
        addressText.setRows(5);
        addressText.setText("DIRECCION FISICA O PERSONAL");
        jScrollPane4.setViewportView(addressText);

        emailLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
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

        editPersonBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/edit32.png"))); // NOI18N
        editPersonBtn.setText("EDITAR");
        editPersonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPersonBtnActionPerformed(evt);
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
                                .addComponent(addPersonBtn)
                                .addGap(18, 18, 18)
                                .addComponent(editPersonBtn)
                                .addGap(18, 18, 18)
                                .addComponent(cancelPersonBtn)
                                .addGap(18, 18, 18)
                                .addComponent(savePersonBtn))
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
                            .addComponent(dniLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dniText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(firstnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstnameText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lastnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastnameText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(countryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(countryText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cityText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phoneText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthdayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tyPersonCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(genderCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staffChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(relationShipLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelPersonBtn)
                            .addComponent(savePersonBtn)
                            .addComponent(addPersonBtn)
                            .addComponent(editPersonBtn)))
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane3))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        optionsPanel.addTab("FICHA", registrationPanel);

        personToEmployeePanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("FORMULARIO PARA REGISTRO DE EMPLEADOS");

        positionSearchCmb.setEditable(true);

        positionLabel.setText("PUESTO:");

        employeeNameLabel.setText("NOMBRE:");

        employeeNameText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        employeeNameText.setText("NOMBREL EMPLEADO");

        oficeLabel.setText("OFICINA:");

        officeSearchCmb.setEditable(true);

        dniEmployeeLabel.setText("DNI:");

        dniEmployeeText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dniEmployeeText.setText("NUMERO DE EMPLEADO");

        degreeLabel.setText("GRADO:");

        degreeSearchCmb.setEditable(true);

        jLabel3.setText("SALARIO:");

        jLabel4.setText("MONEDA");

        coinSearchCmb.setEditable(true);

        addEmployeeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/add32.png"))); // NOI18N
        addEmployeeBtn.setText("CREAR");

        editEmployeeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/edit32.png"))); // NOI18N
        editEmployeeBtn.setText("EDITAR");

        cancelEmployeeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/cancel32.png"))); // NOI18N
        cancelEmployeeBtn.setText("CANCELAR");
        cancelEmployeeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelEmployeeBtnActionPerformed(evt);
            }
        });

        saveEmployeeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sincogpro/stf/save32.png"))); // NOI18N
        saveEmployeeBtn.setText("SALVAR");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 102)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 294, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField1.setText("0.00");

        javax.swing.GroupLayout personToEmployeePanelLayout = new javax.swing.GroupLayout(personToEmployeePanel);
        personToEmployeePanel.setLayout(personToEmployeePanelLayout);
        personToEmployeePanelLayout.setHorizontalGroup(
            personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personToEmployeePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, personToEmployeePanelLayout.createSequentialGroup()
                        .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(personToEmployeePanelLayout.createSequentialGroup()
                                .addComponent(addEmployeeBtn)
                                .addGap(18, 18, 18)
                                .addComponent(editEmployeeBtn)
                                .addGap(18, 18, 18)
                                .addComponent(cancelEmployeeBtn)
                                .addGap(18, 18, 18)
                                .addComponent(saveEmployeeBtn))
                            .addGroup(personToEmployeePanelLayout.createSequentialGroup()
                                .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(positionLabel)
                                    .addComponent(employeeNameLabel)
                                    .addComponent(dniEmployeeLabel)
                                    .addComponent(oficeLabel)
                                    .addComponent(degreeLabel)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(employeeNameText)
                                    .addComponent(dniEmployeeText)
                                    .addComponent(officeSearchCmb, javax.swing.GroupLayout.Alignment.TRAILING, 0, 448, Short.MAX_VALUE)
                                    .addComponent(degreeSearchCmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personToEmployeePanelLayout.createSequentialGroup()
                                        .addComponent(jFormattedTextField1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(coinSearchCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(positionSearchCmb, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        personToEmployeePanelLayout.setVerticalGroup(
            personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personToEmployeePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(personToEmployeePanelLayout.createSequentialGroup()
                        .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(employeeNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(employeeNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dniEmployeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dniEmployeeText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(positionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(positionSearchCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oficeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(officeSearchCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(degreeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(degreeSearchCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(coinSearchCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(personToEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addEmployeeBtn)
                    .addComponent(editEmployeeBtn)
                    .addComponent(cancelEmployeeBtn)
                    .addComponent(saveEmployeeBtn))
                .addContainerGap(170, Short.MAX_VALUE))
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(optionsPanel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(optionsPanel)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void addPersonBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPersonBtnActionPerformed
        initOptions(1);
        clearForm();
        typeQuery="INSERT";
    }//GEN-LAST:event_addPersonBtnActionPerformed

    private void cancelPersonBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelPersonBtnActionPerformed
        initOptions(2);
    }//GEN-LAST:event_cancelPersonBtnActionPerformed

    private void countryTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countryTextActionPerformed

    private void cityTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cityTextActionPerformed

    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextActionPerformed

    private void savePersonBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePersonBtnActionPerformed
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
            else person.updatePerson(data, IDPERSON);
            
            resetInitForm();
        
        }
        else{
            JOptionPane.showMessageDialog(null,"¡Algunos campos requeridos del formulario estan vacios!", "Error",JOptionPane.ERROR_MESSAGE);
            
            for (int i = 0; i < errors.size(); i++) {
                log = log + errors.get(i).getFormErrors()+"\n\n";
            }
            
            logText.setText(log);
        }
        
    }//GEN-LAST:event_savePersonBtnActionPerformed

    private void searchTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextKeyReleased
        fetchPersons(2);
    }//GEN-LAST:event_searchTextKeyReleased

    private void searchTextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTextMousePressed
        searchText.setText("");
    }//GEN-LAST:event_searchTextMousePressed

    private void editItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemActionPerformed
        
        optionsPanel.setSelectedIndex(1);
        
        ArrayList<String> data = new ArrayList<>();
        data = person.getPersonById(IDPERSON);
        
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

    private void editPersonBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPersonBtnActionPerformed
        typeQuery="UPDATE";
        initOptions(4);
    }//GEN-LAST:event_editPersonBtnActionPerformed

    private void personToEmployeeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_personToEmployeeItemActionPerformed
       optionsPanel.setSelectedIndex(2);
       employeeNameText.setText(FULLNAME);
       dniEmployeeText.setText(employee.searchEmpNumber());
       
    }//GEN-LAST:event_personToEmployeeItemActionPerformed

    private void staffChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffChkActionPerformed

    private void cancelEmployeeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelEmployeeBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelEmployeeBtnActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
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
    private javax.swing.JButton addEmployeeBtn;
    private javax.swing.JButton addPersonBtn;
    private javax.swing.JTextArea addressText;
    private javax.swing.JLabel birthdayLabel;
    private javax.swing.JButton cancelEmployeeBtn;
    private javax.swing.JButton cancelPersonBtn;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JLabel cityLabel1;
    private javax.swing.JTextField cityText;
    private javax.swing.JComboBox<String> coinSearchCmb;
    private javax.swing.JLabel countryLabel;
    private javax.swing.JTextField countryText;
    private javax.swing.JCheckBox customerChk;
    private javax.swing.JLabel degreeLabel;
    private javax.swing.JComboBox<String> degreeSearchCmb;
    private javax.swing.JLabel dniEmployeeLabel;
    private javax.swing.JTextField dniEmployeeText;
    private javax.swing.JLabel dniLabel;
    private javax.swing.JFormattedTextField dniText;
    private javax.swing.JButton editEmployeeBtn;
    private javax.swing.JMenuItem editItem;
    private javax.swing.JButton editPersonBtn;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailText;
    private javax.swing.JLabel employeeNameLabel;
    private javax.swing.JTextField employeeNameText;
    private javax.swing.JLabel firstnameLabel;
    private javax.swing.JTextField firstnameText;
    private javax.swing.JComboBox<String> genderCmb;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lastnameLabel;
    private javax.swing.JTextField lastnameText;
    private javax.swing.JTextArea logText;
    private javax.swing.JComboBox<String> officeSearchCmb;
    private javax.swing.JLabel oficeLabel;
    private javax.swing.JList<String> optionList;
    private javax.swing.JTabbedPane optionsPanel;
    private javax.swing.JTable personDT;
    private javax.swing.JMenuItem personToEmployeeItem;
    private javax.swing.JPanel personToEmployeePanel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JFormattedTextField phoneText;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JComboBox<String> positionSearchCmb;
    private javax.swing.JPanel recordsPanel;
    private javax.swing.JPanel registrationPanel;
    private javax.swing.JLabel relationShipLabel;
    private javax.swing.JButton saveEmployeeBtn;
    private javax.swing.JButton savePersonBtn;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JTextField searchText;
    private javax.swing.JCheckBox staffChk;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JComboBox<String> tyPersonCmb;
    // End of variables declaration//GEN-END:variables

    
}
