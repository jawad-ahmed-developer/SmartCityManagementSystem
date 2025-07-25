import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.ResultSetMetaData;

class SmartCity{

    public JFrame frame;// Main Frame
    Connection connection ;

    public SmartCity() {
        frame =  new JFrame("Smart City Management System");
        frame.getContentPane().setBackground(new Color(240, 248, 255));
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       connection = JDBCConnector.getConnection(frame);
    }

    // Method to reset the frame
    void resetFrame(){
        frame.getContentPane().removeAll(); // clear previous screen if any
        frame.repaint();
    }

    // Component Methods: such as JButton, JLabel, JTextField

    // Method to create a JButton with customized bounds
    JButton addButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }

    // Method to create a JTextField with customized bounds
    JTextField addTextField(String text, int x, int y, int width, int height) {
        JTextField textField = new JTextField(text);
        textField.setBounds(x, y, width, height);
        return textField;
    }

    // Method to create a JLabel with customized bounds
    JLabel addLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        return label;
    }

    // Method to create a JComboBox with customized bounds
    JComboBox<String> addComboBox(String[] options, int x, int y, int width, int height){
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setBounds(x, y, width, height);
        comboBox.setBackground(Color.WHITE) ;
        comboBox.setFocusable(false);
        return comboBox;
    }

    void mainFramesHeader(String labelText){
        JLabel header = addLabel(labelText, 650, 120, 355, 50);
        header.setFont(new Font("Calibri", Font.BOLD, 25));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(30, 144, 255));
        header.setHorizontalAlignment(JTextField.CENTER);
        frame.add(header);
    }

    // Method to create hide or show button of JPasswordField
    void hideOrShowPasswordButton(JPasswordField password,int x,int y,int width,int height){
        final boolean[] isVisible = {false};
        JButton hideButton = addButton("Show",x,y,width,height) ;
        searchButtonChanges(hideButton);
        hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isVisible[0]) {
                    password.setEchoChar((char) 0); // Show password
                    hideButton.setText("Hide");
                } else {
                    password.setEchoChar('●'); // Mask password
                    hideButton.setText("Show");
                }
                isVisible[0] = !isVisible[0]; // toggle
            }
        });
        frame.add(hideButton);
    }

    // Method to create JPassword field with customized bounds
    JPasswordField createJPasswordField(String text,int x,int y,int width,int height){
       JPasswordField password = new JPasswordField(text);
        password.setBounds(x,y,width,height);
        password.setFont(new Font("Calibri", Font.PLAIN, 18));
        password.setForeground(Color.GRAY);
        password.setEchoChar((char) 0); // Show placeholder

        password.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(password.getPassword()).equals(text)) {
                    password.setText("");
                    password.setForeground(Color.BLACK);
                    password.setEchoChar('●'); // Hide characters
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(password.getPassword()).isEmpty()) {
                    password.setText(text);
                    password.setForeground(Color.GRAY);
                    password.setEchoChar((char) 0); // Show placeholder again
                }
            }
        });
        return password ;
    }

    void addFocusListenerToTextField(JTextField field,String text){
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    // Method to create a JLabel of customized bounds and message when needed for input validation
    JLabel invalidLabelPrompt1(boolean showError, JLabel label, String message, int x, int y, int width, int height) {
        if (showError) {
            if (label == null) {
                label = new JLabel(message);
                label.setForeground(Color.RED);
                label.setFont(new Font("Calibri", Font.BOLD, 13));
                label.setBounds(x, y, width, height);
                frame.add(label);
            } else {
                label.setText(message);
                label.setVisible(true);
            }
        } else {
            if (label != null) {
                label.setVisible(false);
            }
        }

        frame.revalidate();
        frame.repaint();
        return label;
    }


    static String adminRole ; // Role of Admin who logged into the system
    static String adminID ;// Admin ID who logged into the system
    String adminName ;
    JLabel adminLoginErrorLabel ; // The label shown when user enters invalid admin id or password

    // Method to create admin login window
    void adminLogin() {
        resetFrame();

        // Header
        mainFramesHeader("Admin Login");

        // ID Field
        JTextField username = addTextField("Enter Username", 650, 250, 355, 50);
        username.setFont(new Font("Calibri", Font.PLAIN, 18));
        username.setForeground(Color.GRAY);
        addFocusListenerToTextField(username,"Enter Username");
        frame.add(username);

        // Password Field
        JPasswordField password = createJPasswordField("Enter Password",650, 346, 280, 50) ;
        frame.add(password);

        hideOrShowPasswordButton(password,935, 346, 80, 50); // Hide or show button

        // Login Button
        JButton button = addButton("Login", 650, 450, 355, 50);
        button.setFont(new Font("Calibri", Font.BOLD, 20));
        button.setForeground(new Color(245, 245, 245));
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String admin_username = username.getText().trim();
                String admin_password = String.valueOf(password.getPassword());

                boolean validCheck = true;
                adminLoginErrorLabel = invalidLabelPrompt1(admin_username.isBlank() || admin_username.equals("Enter Username"), adminLoginErrorLabel,
                        "*Invalid Admin. Please Try Again!", 650, 400, 355, 20);
                if (adminLoginErrorLabel != null && adminLoginErrorLabel.isVisible()) {
                    validCheck = false;
                }

                adminLoginErrorLabel = invalidLabelPrompt1(admin_password.isBlank() || admin_password.equals("Enter Password"), adminLoginErrorLabel,
                        "*Invalid Admin. Please Try Again!", 650, 400, 355, 20);
                if (adminLoginErrorLabel != null && adminLoginErrorLabel.isVisible()) {
                    validCheck = false;
                }

                if (!validCheck) {
                    return;
                }

                String admin_role = JDBCConnector.getFieldByTwoPlaceHolders(connection,
                        "select admin_role from admin where admin_username = ? and admin_password = ?",
                        frame, admin_username, admin_password);

                if (admin_role != null){
                    adminName = JDBCConnector.getFieldByTwoPlaceHolders(connection,
                            "select admin_name from admin where admin_username = ? and admin_password = ?",
                            frame, admin_username, admin_password);

                    adminRole = admin_role ;

                    adminID =  JDBCConnector.getFieldByTwoPlaceHolders(connection,
                            "select admin_id from admin where admin_username = ? and admin_password = ?",
                            frame, admin_username, admin_password);

                    JDBCConnector.insertAdminHistory(frame,adminID,"Login",adminID);

                    showMainMenu();

                } else {
                    adminLoginErrorLabel = invalidLabelPrompt1(true, adminLoginErrorLabel,
                            "*Invalid Admin. Please Try Again!", 650, 400, 355, 20);
                    password.setText("Enter Password");
                    password.setForeground(Color.GRAY);
                    password.setEchoChar((char) 0);
                    return;
                }
            }
        });
        frame.add(button);

        frame.setVisible(true);
    }

    // Method To create the main menu after the user successfully Login
    void showMainMenu(){
        resetFrame();
        viewFrameHeader("Welcome " + adminName + "!");
        mainFramesHeader("Main Information Menu");

        JButton addInfo = addButton("Add Information", 650, 300, 355, 50) ;
        changesButtonView(addInfo) ;
        addInfo.addActionListener(e -> {
            if(!adminRole.equals("admin_manager")) {
                showAddInfoMenu();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        });
        frame.add(addInfo) ;

        JButton viewInfo = addButton("Manage Information",  650, 400, 355, 50) ;
        changesButtonView(viewInfo) ;
        viewInfo.addActionListener(e -> {
            if(!adminRole.equals("admin_manager")) {
                showViewInfoMenu();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to display sub menu
        frame.add(viewInfo) ;

        JButton adminsManager = addButton("Control Admin Information",650,500,355,50) ;
        changesButtonView(adminsManager);
        adminsManager.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("admin_manager")){
                showAdminInfoMenu();
            }else{
                JOptionPane.showMessageDialog(frame,"You cannot access Admin Records");
            }
        });
        frame.add(adminsManager) ;

        JButton logOut = addButton("Log out",250,650,150,40) ;
        changesButtonView(logOut) ;
        logOut.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Do you want logout?" , "Confirm Exit",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            ) ;
            if(choice == JOptionPane.OK_OPTION){
                JDBCConnector.insertAdminHistory(frame,adminID,"Log out",adminID);
                adminLogin() ;
            }
        }) ;
        frame.add(logOut) ;
    }

    // Method to create window that show add citizen, add vehicle, add building, add department buttons which nevigates to their respective registration forms
    void showAddInfoMenu(){
        resetFrame();

        mainFramesHeader("Add Information Menu");

        JButton addCitizen = addButton("Add Citizen", 650, 250, 355, 50) ;
        changesButtonView(addCitizen) ;
        addCitizen.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("citizen_admin")) {
                addCitizenInfo();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to add citizen
        frame.add(addCitizen) ;

        JButton addVehicle = addButton("Add Vehicle",  650, 346, 355, 50) ;
        changesButtonView(addVehicle) ;
        addVehicle.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("vehicle_admin")) {
                addVehicleInfo();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Vehicle Information");
            }
        }) ; // Code to add vehicle
        frame.add(addVehicle) ;

        JButton addBuilding = addButton("Add Building", 650, 450, 355, 50) ;
        changesButtonView(addBuilding) ;
        addBuilding.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("building_admin")) {
                addBuildingInfo();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to add building
        frame.add(addBuilding) ;

        JButton addDepartment = addButton("Add Department", 650, 550, 355, 50) ;
        changesButtonView(addDepartment) ;
        addDepartment.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("department_admin")) {
                addDepartmentInfo();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to add department
        frame.add(addDepartment) ;

        JButton back = addButton("BACK",250,650,150,40) ;
        changesButtonView(back); ;
        back.addActionListener(e -> showMainMenu()) ;
        frame.add(back) ;
    }

    // Method to create window that show buttons such as manage citizen, vehicle, building and department that navigate to their respective windows where
    // each entity being search and updated and deleted
    void showViewInfoMenu(){
        resetFrame();

        mainFramesHeader("Manage Information Menu");

        JButton viewCitizen = addButton("Manage Citizen", 650, 250, 355, 50) ;
        changesButtonView(viewCitizen) ;
        viewCitizen.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("citizen_admin")) {
                viewCitizenInfo();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to view citizen
        frame.add(viewCitizen) ;

        JButton viewVehicle = addButton("Manage Vehicle",  650, 346, 355, 50) ;
        changesButtonView(viewVehicle) ;
        viewVehicle.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("vehicle_admin")) {
                viewVehicleInfo();
            }
            else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to view vehicle
        frame.add(viewVehicle) ;

        JButton viewBuilding = addButton("Manage Building", 650, 450, 355, 50) ;
        changesButtonView(viewBuilding) ;
        viewBuilding.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("building_admin")) {
                viewBuildingInfo();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to view building
        frame.add(viewBuilding) ;

        JButton viewDepartment = addButton("Manage Department", 650, 550, 355, 50) ;
        changesButtonView(viewDepartment) ;
        viewDepartment.addActionListener(e -> {
            if(adminRole.equals("super_admin") || adminRole.equals("department_admin")) {
                viewDepartmentInfo();
            }else{
                JOptionPane.showMessageDialog(frame,"You can not access Citizen Information");
            }
        }) ; // Code to view department
        frame.add(viewDepartment) ;

        JButton back = addButton("BACK",250,650,150,40) ;
        changesButtonView(back);
        back.addActionListener(e -> showMainMenu()) ;
        frame.add(back) ;
    }

    // Method to create the window that show three buttons such as: add admin info which navigates to admin registration form, manage admin info that navigates to
    // the window where the records of admin updated and deleted and also have search boxes and manage admin history that navigates to window that provide the history
    // of admin actions
    void  showAdminInfoMenu(){
        resetFrame();

        mainFramesHeader("Control Admin Information");

        JButton addInfo = addButton("Add Admin Information", 650, 300, 355, 50) ;
        changesButtonView(addInfo) ;
        addInfo.addActionListener(e -> showAddAdminInfoMenu()) ; // Code to display sub menu
        frame.add(addInfo) ;

        JButton viewInfo = addButton("Manage Admin Information",  650, 400, 355, 50) ;
        changesButtonView(viewInfo) ;
        viewInfo.addActionListener(e -> showManageAdminInfoMenu()) ; // Code to display sub menu
        frame.add(viewInfo) ;

        JButton adminsManager = addButton("Admin Manipulation History",650,500,355,50) ;
        changesButtonView(adminsManager);
        adminsManager.addActionListener(e -> {
             manageAdminHistory();
        });
        frame.add(adminsManager) ;

        JButton back = addButton("BACK",250,650,120,40) ;
        changesButtonView(back) ;
        back.addActionListener(e -> showMainMenu()) ;
        frame.add(back) ;
    }

    JLabel adminNameErrorLabel ; // Label shown when user enters invalid admin name
    JLabel adminUserNameErrorLabel ; // Label shown when user enters invalid admin username
    JLabel adminPasswordErrorLabel ; // Label shown when user enters invalid admin password
    JLabel adminCitizenIDErrorLabel ; // Label shown when user enters invalid admin citizen id

    // Method to create the admin registration form
    void showAddAdminInfoMenu(){
        resetFrame();
        registrationFormHeader("Admin Registration Form") ;

        registrationIDLabel("Admin ID:") ;
        JLabel textID = addLabel("",200,75,100,20) ;
        textID.setFont(new Font("Calibri", Font.BOLD, 18));
        frame.add(textID) ;

        final String[] id = {IDGenerator.generateAdminID(connection,frame)} ;
        if( id[0] != null) {
            textID.setText(id[0]);
        }

        JLabel name = addLabel("Full Name:",540,97,150,20) ;
        name.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(name) ;

        JTextField adminName = addTextField("",700,87,350,30) ;
        frame.add(adminName) ;

        JLabel usernameLabel = addLabel("Username:",540,147,150,20) ;
        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(usernameLabel) ;

        JTextField  username = addTextField("",700,138,350,30) ;
        frame.add(username) ;

        JLabel roleLabel = addLabel("Admin Role:",540, 193, 150, 20) ;
        roleLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(roleLabel) ;

        String[] roleOptions = {
                "super_admin",       // Full control over the entire system
                "admin_manager",     // Can manage other admins, but not the super admin
                "citizen_admin",     // Manages citizen records (add/edit/delete)
                "building_admin",    // Manages building data
                "vehicle_admin",     // Manages vehicle-related information
                "department_admin"   // Handles department records
        };
        JComboBox<String> roleBox = addComboBox(roleOptions,700, 190, 120, 25) ;
        frame.add(roleBox);

        JLabel password = addLabel("Password",540,235,150,20) ;
        password.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(password) ;

        JPasswordField adminPassword = createJPasswordField("Enter Password", 700, 225, 270, 30) ;
        frame.add(adminPassword);

        hideOrShowPasswordButton(adminPassword, 970, 225, 80, 30);

        JLabel citizen_id = addLabel("Citizen ID:",540,282,150,20) ;
        citizen_id.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(citizen_id) ;

        JTextField citizenIDField = addTextField("",700,277,350,30) ;
        frame.add(citizenIDField) ;

        JButton submit = addButton("Submit",950,350,100,30) ;
        changesButtonView(submit) ;
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String admin_role = roleBox.getSelectedItem().toString().trim();
                if(adminRole.equals("admin_manager") && admin_role.equals("super_admin")){
                    JOptionPane.showMessageDialog(frame,"You can not add a super admin");
                    return ;
                }
                String full_name = adminName.getText().trim();
                String userName = username.getText().trim();
                String password = String.valueOf(adminPassword.getPassword()).trim();
                String citizenID = citizenIDField.getText().trim();

                boolean validCheck = true;

// Admin Name Validation
                adminNameErrorLabel = invalidLabelPrompt1(
                        full_name.isBlank() || !full_name.matches("[a-zA-Z .]{1,30}"),
                        adminNameErrorLabel,
                        "*Name must be alphabetic, max 30 chars",
                        700, 120, 350, 15
                );
                if (adminNameErrorLabel != null && adminNameErrorLabel.isVisible()) validCheck = false;

// Admin Username Validation
                boolean adminUserNameExists = JDBCConnector.search(connection, "select admin_username from admin where admin_username = ?", userName, frame);
                if (userName.isBlank() || !userName.matches("[a-zA-Z .0-9]{1,30}")) {
                    adminUserNameErrorLabel = invalidLabelPrompt1(true, adminUserNameErrorLabel,
                            "*Username must be alphabetic, max 30 chars",
                            700, 170, 350, 15);
                    validCheck = false;
                } else if (adminUserNameExists) {
                    adminUserNameErrorLabel = invalidLabelPrompt1(true, adminUserNameErrorLabel,
                            "*Username already exists. Choose another.",
                            700, 170, 350, 15);
                    validCheck = false;
                }

// Password Validation
                boolean checkPassword = password.length() == 8 &&
                        password.replaceAll("[^0-9]", "").length() >= 2 &&
                        password.replaceAll("[^a-zA-Z]", "").length() >= 2 &&
                        password.replaceAll("[^@#$&^*]", "").length() >= 2 &&
                        password.replaceAll("[^0-9a-zA-Z@#$&^*]", "").length() == 8;
                adminPasswordErrorLabel = invalidLabelPrompt1(
                        password.isBlank() || !checkPassword,
                        adminPasswordErrorLabel,
                        "*Password must be 8 chars: 2 digits, 2 lowercase, 2 special (@#$&^*)",
                        700, 258, 360, 15
                );
                if (adminPasswordErrorLabel != null && adminPasswordErrorLabel.isVisible()) {
                    validCheck = false;
                }

// Citizen ID Validation
                if (!citizenID.isBlank()) {
                    boolean citizenExists = JDBCConnector.search(connection, "select citizen_id from citizen where citizen_id = ?", citizenID, frame);
                    adminCitizenIDErrorLabel = invalidLabelPrompt1(!citizenExists,
                            adminCitizenIDErrorLabel,
                            "*Invalid Citizen ID. Please Try Again!",
                            700, 310, 350, 15
                    );
                    if (adminCitizenIDErrorLabel != null && adminCitizenIDErrorLabel.isVisible()) {
                        validCheck = false;
                    }
                }

                if (!validCheck) {
                    JOptionPane.showMessageDialog(frame, "is it still");
                    return;
                }

                Admin admin = new Admin(id[0], full_name, userName, admin_role, password, citizenID);
                int adminUpdate = JDBCConnector.insertAdmin(connection, frame, admin);
                if (adminUpdate > 0) {
                    JOptionPane.showMessageDialog(frame, "Admin added successfully!");
                    JDBCConnector.insertAdminHistory(frame,adminID,"Add",id[0]);
                    int choiceToAddAnother = JOptionPane.showConfirmDialog(frame, "Do you want to register another citizen?", "Confirm Add Admin", JOptionPane.OK_CANCEL_OPTION);
                    if (choiceToAddAnother == JOptionPane.OK_OPTION) {
                        id[0] = IDGenerator.generateAdminID(connection, frame);
                        if (id[0] != null) {
                            textID.setText(id[0]);
                        }
                        adminName.setText("");
                        username.setText("");
                        roleBox.setSelectedIndex(0);
                        adminPassword.setText("");
                        adminPassword.setEchoChar('●');
                        citizenIDField.setText("");
                    } else {
                        showAdminInfoMenu();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add Admin!", "Insertion Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        });
        frame.add(submit) ;

        JButton back = addButton("BACK",250,650,120,40) ;
        changesButtonView(back) ;
        back.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Do you want to cancel the form?\nNote: If you exit, your filled data" +
                            " will be deleted!",
                    "Confirm Exit",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if(choice == JOptionPane.OK_OPTION){
                showAdminInfoMenu();
            }
        }) ;
        frame.add(back) ;
    }

    JLabel adminIDSearchErrorLabel ; // Label shown when user enters invalid admin id in search box
    JLabel adminNameSearchErrorLabel ; // Label shown when user enters invalid admin name in search box
    JLabel adminUsernameSearchErrorLabel ; // Label shown when user enters invalid admin username in search box
    JLabel adminCitizenIDSearchErrorLabel ; // Label shown when user enters invalid admin citizen id in search box

    // Method to create the window to manage the records of admin such as search, edit, and delete
    void showManageAdminInfoMenu() {
        resetFrame();
        viewFrameHeader("********Manage Admin Records********");

        JLabel idLabel = addLabel("Search by Admin ID", 200, 50, 190, 30);
        idLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(idLabel);

        JTextField idSearch = addTextField("",200, 85, 178, 30);
        frame.add(idSearch);

        JButton idSearchButton = addButton("Search", 380, 85, 90, 30);
        searchButtonChanges(idSearchButton);

        idSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String adminID = idSearch.getText().trim();

                adminIDSearchErrorLabel = invalidLabelPrompt1(adminID.isBlank() || !adminID.matches("^A[0-9]{7}"),adminIDSearchErrorLabel,
                        "*Invalid Admin ID!", 200, 118, 200, 15) ;
                if(adminIDSearchErrorLabel != null && adminIDSearchErrorLabel.isVisible()){
                    return ;
                }
                showTableResult("SELECT admin_id, admin_name,admin_role,admin_username,admin_password,admin_citizen_id FROM admin WHERE admin_id = ?",adminID);
            }
        });
        frame.add(idSearchButton) ;

        JLabel nameLabel = addLabel("Search by Admin Name",510,50,230,30) ;
        nameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(nameLabel) ;

        JTextField nameSearch = addTextField("",510,85,178,30) ;
        frame.add(nameSearch) ;

        JButton nameSearchButton = addButton("Search",690,85,90,30) ;
        searchButtonChanges(nameSearchButton) ;
        nameSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String admin_name = nameSearch.getText().trim() ;
                adminNameSearchErrorLabel = invalidLabelPrompt1(admin_name.isBlank() || !admin_name.matches("[0-9]{13}"),adminNameSearchErrorLabel,
                        "*Invalid Admin Name!",510,118,175,15) ;
                if(adminNameSearchErrorLabel != null && adminNameSearchErrorLabel.isVisible()){
                    return;
                }
                showTableResult("SELECT admin_id, admin_name,admin_role,admin_username,admin_password,admin_citizen_id FROM admin WHERE admin_name = ?",admin_name);
            }
        });
        frame.add(nameSearchButton) ;

        JLabel usernameLabel = addLabel("Search by Username", 820, 50, 230, 30);
        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(usernameLabel);

        JTextField usernameSearch = addTextField("",820, 85, 178, 30);
        frame.add(usernameSearch);

        JButton usernameSearchButton = addButton("Search", 1000, 85, 90, 30);
        searchButtonChanges(usernameSearchButton);

        usernameSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameSearch.getText().trim();

                adminUsernameSearchErrorLabel = invalidLabelPrompt1(username.isBlank() || !username.matches("[0-9]{13}"),adminUsernameSearchErrorLabel,
                        "*Invalid Username!", 820, 118, 220, 15) ;

                if (adminUsernameSearchErrorLabel != null && adminUsernameSearchErrorLabel.isVisible()) {
                    return;
                }
                showTableResult("SELECT admin_id, admin_name,admin_role,admin_username,admin_password,admin_citizen_id FROM admin WHERE admin_username = ?",username);
            }
        });
        frame.add(usernameSearchButton);

        JLabel  adminCID = addLabel("Search by Admin Citizen ID",1130,50,250,30) ;
        adminCID.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(adminCID) ;

        JTextField CIDSearch = addTextField("",1130,85,178,30) ;
        frame.add(CIDSearch) ;

        JButton CIDSearchButton = addButton("Search",1310,85,90,30) ;
        searchButtonChanges(CIDSearchButton) ;
        nameSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String cid = CIDSearch.getText().trim() ;
                adminCitizenIDSearchErrorLabel = invalidLabelPrompt1(cid.isBlank() || !cid.matches("^C[0-9]{7}"),adminCitizenIDSearchErrorLabel,
                        "*Invalid Admin Citizen ID!",1130,118,200,15) ;
                if(adminCitizenIDSearchErrorLabel != null && adminCitizenIDSearchErrorLabel.isVisible()){
                    return ;
                }
                showTableResult("SELECT admin_id, admin_name,admin_role,admin_username,admin_password,admin_citizen_id FROM admin WHERE admin_citizen_id = ?",cid);
            }
        });
        frame.add(CIDSearchButton) ;

        int x = 200; // Starting x position for first button
        String[] searchOptionsByRole = {"All",
                "Super Admin",       // Full control over the entire system
                "Admin Manager",     // Can manage other admins, but not the super admin
                "Citizen Admin",     // Manages citizen records (add/edit/delete)
                "Building Admin",    // Manages building data
                "Vehicle Admin",     // Manages vehicle-related information
                "Department Admin"   // Handles department records
        };
        String query = "SELECT admin_id, admin_name,admin_role,admin_username,admin_password,admin_citizen_id FROM admin WHERE admin_role = ?" ;
        for (String type : searchOptionsByRole) {
            int width = type.equals("All") ? 50 : 150 ; // Narrower width for "All"

            JButton btn = addButton(type, x, 150, width, 20);
            btn.setFocusPainted(false);

            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // You can handle based on type
                    switch (type) {
                        case "All":
                            showTableResult("SELECT admin_id, admin_name,admin_role,admin_username,admin_password,admin_citizen_id FROM admin WHERE 1 = ?","1");
                            break;
                        case "Super Admin":
                            showTableResult(query,"super_admin");
                            break;
                        case "Admin Manager":
                            showTableResult(query,"admin_manager");
                            break;
                        case "Citizen Admin":
                            showTableResult(query,"citizen_admin");
                            break;
                        case "Vehicle Admin":
                            showTableResult(query,"vehicle_admin");
                            break;
                        case "Building Admin":
                            showTableResult(query,"building_admin");
                            break;
                        case "Department Admin":
                            showTableResult(query,"department_admin");
                            break;
                    }
                }
            });
            frame.add(btn);
            x += (width + 5); // Space between buttons
        }

    }

    JLabel historyAdminIDSearchErrorLabel ; // Label shown when user enters an invalid admin id in search box
    JLabel historyAdminUsernameSearchErrorLabel ; // Label shown when user enters an invalid admin username in search box
    JLabel historyAdminNameSearchErrorLabel ; // Label shown when user enters an invalid admin name in search box
    JLabel historyAdminActionTimeSearchErrorLabel ; // Label shown when user enters an invalid date in search box

    // Method to create window to show the history of admin actions such as add, update or delete information or login and logout
    void manageAdminHistory(){
        resetFrame() ;

        viewFrameHeader("********Manage Admin History********");

        JLabel idLabel = addLabel("Search by Admin ID",160, 50, 190, 30) ;
        idLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(idLabel) ;

        JTextField idSearch = addTextField("",160, 85, 178, 30) ;
        frame.add(idSearch) ;

        JButton adminIDSearchButton = addButton("Search",340, 85, 90, 30) ;
        searchButtonChanges(adminIDSearchButton) ;
        adminIDSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String admin_id = idSearch.getText().trim() ;

                historyAdminIDSearchErrorLabel = invalidLabelPrompt1(admin_id.isBlank() || !admin_id.matches("^A[0-9]{7}"),historyAdminIDSearchErrorLabel,
                        "*Invalid Admin ID!",160, 118, 200, 15) ;
                if(historyAdminIDSearchErrorLabel != null && historyAdminIDSearchErrorLabel.isVisible()){
                    return;
                }
                showAdminHistoryResult("select * from admin_history where admin_id = ?",admin_id);
            }
        });
        frame.add(adminIDSearchButton) ;


        JLabel usernameLabel = addLabel("Search by Username",450,50,210,30) ;
        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(usernameLabel) ;

        JTextField  usernameSearch = addTextField("",450,85,178,30) ;
        frame.add(usernameSearch) ;

        JButton usernameSearchButton = addButton("Search",630,85,90,30) ;
        searchButtonChanges(usernameSearchButton) ;
        usernameSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String username = usernameSearch.getText().trim() ;
                historyAdminUsernameSearchErrorLabel = invalidLabelPrompt1(username.isBlank() || !username.matches("[A-Za-z .0-9]{1,30}"),historyAdminUsernameSearchErrorLabel,
                        "*Invalid Admin Username!",450,118,175,15) ;
                if(historyAdminUsernameSearchErrorLabel != null && historyAdminUsernameSearchErrorLabel.isVisible()){
                    return ;
                }
                showAdminHistoryResult("select * from admin_history  WHERE admin_id = (select admin_id from admin where admin_username = ?)", username);
            }
        });
        frame.add(usernameSearchButton) ;

        JLabel adminName = addLabel("Search by Admin Name", 740, 50, 230, 30);
        adminName.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(adminName);

        JTextField nameSearch = addTextField("",740, 85, 178, 30);
        frame.add(nameSearch);

        JButton nameSearchButton = addButton("Search", 920, 85, 90, 30);
        searchButtonChanges(nameSearchButton);

        nameSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String admin_name = nameSearch.getText().trim();
                historyAdminNameSearchErrorLabel = invalidLabelPrompt1(admin_name.isBlank() || !admin_name.matches("[A-Za-z .]{1,50}"),historyAdminNameSearchErrorLabel,
                        "*Please Admin Name!", 740, 118, 220, 15) ;

                if (historyAdminNameSearchErrorLabel != null && historyAdminNameSearchErrorLabel.isVisible()) {
                    return;
                }
                showAdminHistoryResult("select * from admin_history where admin_id = (select admin_id from admin where admin_name = ?)",admin_name);
            }
        });

        frame.add(nameSearchButton);

        JLabel timeLabel = addLabel("Search by Time",1030,50,230,30 );
        timeLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(timeLabel);

        JTextField timeSearch = addTextField( "e.g:- YYYY-MM-DD",1030,85,178,30);
        addFocusListenerToTextField(timeSearch,"e.g:- YYYY-MM-DD");
        frame.add(timeSearch);

        JButton timeSearchButton = addButton("Search", 1200,85,90,30);
        searchButtonChanges(timeSearchButton);

        timeSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = timeSearch.getText().trim() ;
               historyAdminActionTimeSearchErrorLabel = invalidLabelPrompt1(date.isBlank() || date.equals("e.g:- YYYY-MM-DD"),historyAdminActionTimeSearchErrorLabel,
                      "*Invalid Admin Action Date!",1030,118,175,15) ;
                if(historyAdminActionTimeSearchErrorLabel != null && historyAdminActionTimeSearchErrorLabel.isVisible()){
                    return;
                }
                try{
                    LocalDate actionDate = LocalDate.parse(date) ;
                    historyAdminActionTimeSearchErrorLabel = invalidLabelPrompt1(actionDate.isAfter(LocalDate.now()),historyAdminActionTimeSearchErrorLabel,
                            "*Invalid Admin Action Date!",1030,118,175,15) ;
                    if(historyAdminActionTimeSearchErrorLabel != null && historyAdminActionTimeSearchErrorLabel.isVisible()){
                        return;
                    }
                }catch(DateTimeParseException te){
                    historyAdminActionTimeSearchErrorLabel = invalidLabelPrompt1(true,historyAdminActionTimeSearchErrorLabel,
                            "*Invalid Admin Action Date!",1030,118,175,15) ;
                    if(historyAdminActionTimeSearchErrorLabel != null && historyAdminActionTimeSearchErrorLabel.isVisible()){
                        return;
                    }
                }
                showAdminHistoryResult("select * from admin_history where date(date_of_action) = ?",date);
            }
        });
        frame.add(timeSearchButton);

        String[] searchTypes = {"All", "By Citizen Activity", "By Admin Activity", "By Vehicle Activity", "By Building Activity", "By Department Activity",
                "By Action: Added", "By Action: Deleted", "By Action: Updated" };

        String entitySearchQuery = "select * from admin_history where performed_on like ?" ;
        String actionSearchQuery = "select * from admin_history where action = ?" ;

        int x = 100 ;

        for (String type : searchTypes) {
            int width = type.equals("All") ? 50 : type.equals("By Department Activity")? 170 : 150 ; // Narrower width for "All"

            JButton btn = addButton(type, x, 150, width, 20);
            btn.setFocusPainted(false);

            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // You can handle based on type
                    switch (type) {
                        case "All":
                            showAdminHistoryResult("select * from admin_history where 1 = ?","1");
                            break;
                        case "By Admin Activity":
                            showAdminHistoryResult(entitySearchQuery,"A%");
                            break ;
                        case "By Citizen Activity":
                            showAdminHistoryResult(entitySearchQuery,"C%");
                            break;
                        case "By Vehicle Activity":
                            showAdminHistoryResult(entitySearchQuery,"V%");
                            break;
                        case "By Building Activity":
                            showAdminHistoryResult(entitySearchQuery,"B%");
                            break;
                        case "By Department Activity":
                            showAdminHistoryResult(entitySearchQuery,"D%");
                            break;
                        case "By Action: Added":
                            showAdminHistoryResult(actionSearchQuery,"Add");
                            break;
                        case "By Action: Deleted":
                            showAdminHistoryResult(actionSearchQuery,"Delete");
                            break;
                        case "By Action: Updated":
                            showAdminHistoryResult(actionSearchQuery,"Update");
                            break;
                    }
                }
            });
            frame.add(btn);
            x += (width + 5); // Space between buttons
        }

        showBackAdminBackButton();
    }

    JLabel nameErrorLabel; // show Label when user enters an invalid citizen name in the citizen registration form
    JLabel cnicErrorLabel; // show Label when user enters an invalid citizen cnic in the citizen registration form
    JLabel dobErrorLabel; // show Label when user enters an invalid citizen date of birth in the citizen registration form
    JLabel addressErrorLabel; // show Label when user enters an invalid citizen address in the citizen registration form
    JLabel phoneErrorLabel; // show Label when user enters an invalid citizen phone number in the citizen registration form
    JLabel emailErrorLabel; // show Label when user enters an invalid citizen email in the citizen registration form
    JLabel occupationErrorLabel; // show Label when user enters an invalid citizen occupation in the citizen registration form
    JLabel deptErrorLabel; // show Label when user enters an invalid citizen department in the citizen registration form

    // Method to display window of adding citizen info along with input validations
    void addCitizenInfo(){
        resetFrame() ;
        registrationFormHeader("Citizen Registration Form") ;

        registrationIDLabel("Citizen ID:") ;
        JLabel textID = addLabel("",200,75,100,20) ;
        textID.setFont(new Font("Calibri", Font.BOLD, 18));
        frame.add(textID) ;

        final String[] id = {IDGenerator.generateCitizenID(connection,frame)} ;
        if(id[0] != null) {
            textID.setText(id[0]);
        }

        JLabel name = addLabel("Full Name:",540,97,150,20) ;
        name.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(name) ;

        JTextField citizenName = addTextField("",700,87,350,30) ;
        frame.add(citizenName) ;

        JLabel cnic = addLabel("CNIC:",540,147,150,20) ;
        cnic.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(cnic) ;

        JTextField citizenCNIC = addTextField("e.g:- XXXXXXXXXXXXX",700,138,350,30) ;
        addFocusListenerToTextField(citizenCNIC,"e.g:- XXXXXXXXXXXXX");
        frame.add(citizenCNIC) ;

        JLabel gender = new JLabel("Gender:");
        gender.setBounds(540, 193, 150, 20);
        gender.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(gender);

        String[] genderOptions = {"male", "female", "other"};
        JComboBox<String> genderBox = addComboBox(genderOptions,700, 190, 120, 25) ;
        frame.add(genderBox);

        JLabel DOB = addLabel("Date of Birth:",540,235,150,20) ;
        DOB.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(DOB) ;

        JTextField citizenDOB = addTextField("e.g:- YYYY-MM-DD", 700, 225, 350, 30) ;
        addFocusListenerToTextField(citizenDOB,"e.g:- YYYY-MM-DD");
        frame.add(citizenDOB);

        JLabel address = addLabel("Address:",540,282,150,20) ;
        address.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(address) ;

        JTextField citizenAddress = addTextField("",700,277,350,30) ;
        frame.add(citizenAddress) ;

        JLabel phoneNumber = addLabel("Phone#:",540,333,150,20) ;
        phoneNumber.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(phoneNumber) ;

        JTextField citizenPhoneNumber = addTextField("e.g:- 0##########",700,328,350,30) ;
       addFocusListenerToTextField(citizenPhoneNumber,"e.g:- 0##########");
        frame.add(citizenPhoneNumber) ;

        JLabel email = addLabel("Email:",540,385,150,20) ;
        email.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(email) ;

        JTextField citizenEmail = addTextField("e.g:- username@gmail.com",700,380,350,30) ;
        addFocusListenerToTextField(citizenEmail,"e.g:- username@gmail.com");
        frame.add(citizenEmail) ;

        JLabel occupation = addLabel("Occupation:",540,435,150,20) ;
        occupation.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(occupation) ;

        JTextField citizenOccupation = addTextField("",700,430,350,30) ;
        frame.add(citizenOccupation) ;

        JLabel maritalStatus = addLabel("Marital Status:",540,485,150,20) ;
        maritalStatus.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(maritalStatus) ;

        String[] maritalStatusOptions = {"married","single"} ;
        JComboBox<String> maritalBox = addComboBox(maritalStatusOptions,700, 480 , 120, 25) ;
        frame.add(maritalBox) ;

        JLabel departmnet = addLabel("Department ID:",540,520,150,20) ;
        departmnet.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(departmnet) ;

        JTextField citizenDepartment = addTextField("",700,515,350,30) ;
        frame.add(citizenDepartment) ;


        JButton submit = addButton("Submit",950,605,100,30) ;
        changesButtonView(submit) ;
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = citizenName.getText().trim() ;
                String cnic = citizenCNIC.getText() ;
                String dobText = citizenDOB.getText().trim();
                String address = citizenAddress.getText() ;
                String phone = citizenPhoneNumber.getText() ;
                String email = citizenEmail.getText() ;
                String occupation = citizenOccupation.getText() ;
                String marryStatus = (String) maritalBox.getSelectedItem();
                String deptID = citizenDepartment.getText().trim();
                String gender = (String) genderBox.getSelectedItem() ;


                boolean validCheck = true ;

                nameErrorLabel = invalidLabelPrompt1(
                        name.isBlank() || !name.matches("[a-zA-Z .]{1,30}"),
                        nameErrorLabel,
                        "*Name must be alphabetic, max 30 chars",
                        700, 120, 350, 15
                );
                if (nameErrorLabel != null && nameErrorLabel.isVisible()) validCheck = false;

                cnicErrorLabel = invalidLabelPrompt1(
                        cnic.isBlank() || !cnic.matches("[0-9]{13}"),
                        cnicErrorLabel,
                        "*Invalid CNIC! e.g: 4330412345678",
                        700, 170, 350, 15
                );
                if (cnicErrorLabel != null && cnicErrorLabel.isVisible()) {
                    validCheck = false;
                }

// Date of Birth
                dobErrorLabel = invalidLabelPrompt1(
                        dobText.isBlank() || dobText.equals("e.g:- YYYY-MM-DD"),
                        dobErrorLabel,
                        "*Date of Birth cannot be empty",
                        700, 258, 350, 15
                );
                if (dobErrorLabel != null && dobErrorLabel.isVisible()) {
                    validCheck = false;
                } else {
                    try {
                        LocalDate dob = LocalDate.parse(dobText);  // Validates format too
                        if (dob.isAfter(LocalDate.now())) {
                            dobErrorLabel = invalidLabelPrompt1(
                                    true,
                                    dobErrorLabel,
                                    "*Date cannot be in the future",
                                    700, 258, 350, 15
                            );
                            validCheck = false;
                        }
                    } catch (DateTimeParseException ex) {
                        dobErrorLabel = invalidLabelPrompt1(
                                true,
                                dobErrorLabel,
                                "*Invalid Date of Birth! e.g: 2005-03-29",
                                700, 258, 350, 15
                        );
                        validCheck = false;
                    }
                }

// Address
                addressErrorLabel = invalidLabelPrompt1(
                        address.isBlank() || address.length() > 60,
                        addressErrorLabel,
                        "*Address must be up to 60 and cannot be empty",
                        700, 310, 350, 15
                );
                if (addressErrorLabel != null && addressErrorLabel.isVisible()) {
                    validCheck = false;
                }

// Phone
                phoneErrorLabel = invalidLabelPrompt1(
                        !phone.matches("^[0-9]{11}$") || phone.equals("0##########"),
                        phoneErrorLabel,
                        "*Invalid phone number! e.g: 03140000000",
                        700, 360, 350, 15
                );
                if (phoneErrorLabel != null && phoneErrorLabel.isVisible()) {
                    validCheck = false;
                }

// Email
                emailErrorLabel = invalidLabelPrompt1(
                        !email.equals("e.g:- username@gmail.com") &&
                                !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,50}$"),
                        emailErrorLabel,
                        "*Invalid email address! e.g:- user@example.com",
                        700, 410, 350, 15
                );
                if (emailErrorLabel != null && emailErrorLabel.isVisible()) {
                    validCheck = false;
                }

// Occupation
                occupationErrorLabel = invalidLabelPrompt1(
                        occupation.isBlank() || !occupation.matches("[a-zA-Z .-]{2,30}"),
                        occupationErrorLabel,
                        "*Input must be up to 30 and cannot be empty",
                        700, 460, 350, 15
                );
                if (occupationErrorLabel != null && occupationErrorLabel.isVisible()) {
                    validCheck = false;
                }

// Department ID (optional, only if filled)
                if (!deptID.isBlank()) {
                    boolean deptExists = JDBCConnector.search(
                            connection,
                            "SELECT dept_id FROM department WHERE dept_id = ?",
                            deptID,
                            frame
                    );
                    deptErrorLabel = invalidLabelPrompt1(
                            !deptExists,
                            deptErrorLabel,
                            "*Invalid Department ID!",
                            700, 545, 350, 15
                    );
                    if (deptErrorLabel != null && deptErrorLabel.isVisible()) {
                        validCheck = false;
                    }
                }
                    if(!validCheck){
                        return ;
                    }

                    Citizen citizen = new Citizen(name,id[0],cnic,gender,dobText,address,phone,email,occupation,marryStatus,deptID) ;

                    String sqlInsert = "INSERT INTO citizen (citizen_id, fullName, cnic, gender, date_of_birth, phone, email, occupation, marital_status, dept_id,address) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    if (JDBCConnector.insertCitizen(frame,sqlInsert,connection,citizen) > 0) {
                        JOptionPane.showMessageDialog(frame, "Citizen info added successfully!") ;
                        JDBCConnector.insertAdminHistory(frame,adminID,"Add",id[0]);
                        int confirmAddCitizen = JOptionPane.showConfirmDialog(
                                frame,
                                "Do you want to register another citizen?",
                                "Confirm Add citizen",
                                JOptionPane.OK_CANCEL_OPTION
                        );
                        if(confirmAddCitizen == JOptionPane.OK_OPTION) {
                            id[0] = IDGenerator.generateCitizenID(connection, frame) ;
                            if (id[0] != null) {
                                textID.setText(id[0]);
                            }
                            citizenName.setText(null);
                            citizenCNIC.setText("e.g:- XXXXXXXXXXXXX");
                            citizenDOB.setText("e.g:- YYYY-MM-DD");
                            citizenAddress.setText(null);
                            citizenPhoneNumber.setText("e.g:- 0##########");
                            citizenEmail.setText("e.g:- username@gmail.com");
                            citizenOccupation.setText(null);
                            citizenDepartment.setText(null);
                            maritalBox.setSelectedIndex(0);
                            genderBox.setSelectedIndex(0);
                            return;
                        }else{
                            showAddInfoMenu();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to add citizen info.");
                        return ;
                    }
            }
        });
        frame.add(submit) ;

        formBackButton();
    }

    JLabel citizenIDSearchErrorLabel ;
    JLabel citizenCNICSearchErrorLabel ;
    JLabel citizenNameSearchErrorLabel ;
    JLabel citizenDeptIDSearchErrorLabel ;

    // Method to display window of manage citizen records such as search, update, and delete
    void viewCitizenInfo(){
        resetFrame() ;

        viewFrameHeader("********Manage Citizen Records********");

        JLabel idLabel = addLabel("Search by Citizen ID", 200, 50, 190, 30);
        idLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(idLabel);

        JTextField idSearch = addTextField("",200, 85, 178, 30);
        frame.add(idSearch);

        JButton idSearchButton = addButton("Search", 380, 85, 90, 30);
        searchButtonChanges(idSearchButton);

        idSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String citizenID = idSearch.getText().trim();

                citizenIDSearchErrorLabel = invalidLabelPrompt1(citizenID.isBlank() || !citizenID.matches("^C[0-9]{7}"),citizenIDSearchErrorLabel,
                        "*Please enter Citizen ID!", 200, 118, 200, 15) ;
                if(citizenIDSearchErrorLabel != null && citizenIDSearchErrorLabel.isVisible()){
                    return ;
                }
                showTableResult("SELECT citizen_id,fullName,cnic,gender,date_of_birth,address,phone,email,occupation,marital_status,dept_id FROM citizen WHERE citizen_id = ?",citizenID);
            }
        });
        frame.add(idSearchButton) ;

        JLabel cnicLabel = addLabel("Search by Citizen CNIC",510,50,190,30) ;
        cnicLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(cnicLabel) ;

        JTextField cnicSearch = addTextField("",510,85,178,30) ;
        frame.add(cnicSearch) ;

        JButton cnicSearchButton = addButton("Search",690,85,90,30) ;
        searchButtonChanges(cnicSearchButton) ;
        cnicSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String cnic = cnicSearch.getText().trim() ;
                citizenCNICSearchErrorLabel = invalidLabelPrompt1(cnic.isBlank() || !cnic.matches("[0-9]{13}"),citizenCNICSearchErrorLabel,
                        "*Invalid Citizen CNIC!",510,118,175,15) ;
                if(citizenCNICSearchErrorLabel != null && citizenCNICSearchErrorLabel.isVisible()){
                    return;
                }
                showTableResult("SELECT citizen_id,fullName,cnic,gender,date_of_birth,address,phone,email,occupation,marital_status,dept_id FROM citizen WHERE cnic = ?",cnic);
            }
        });
        frame.add(cnicSearchButton) ;

        JLabel deptIdLabel = addLabel("Search by Department ID", 820, 50, 230, 30);
        deptIdLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(deptIdLabel);

        JTextField deptSearch = addTextField("",820, 85, 178, 30);
        frame.add(deptSearch);

        JButton deptSearchButton = addButton("Search", 1000, 85, 90, 30);
        searchButtonChanges(deptSearchButton);

        deptSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String deptID = deptSearch.getText().trim();

                citizenDeptIDSearchErrorLabel = invalidLabelPrompt1(deptID.isBlank() || !deptID.matches("^D[0-9]{7}"),citizenDeptIDSearchErrorLabel,"*Please enter Department ID!", 820, 118, 220, 15) ;

                if (citizenDeptIDSearchErrorLabel != null && citizenDeptIDSearchErrorLabel.isVisible()) {
                    return;
                }
                showTableResult("SELECT citizen_id,fullName,cnic,gender,date_of_birth,address,phone,email,occupation,marital_status,dept_id FROM citizen WHERE dept_id = ?",deptID);
            }
        });

        frame.add(deptSearchButton);

        JLabel nameLabel = addLabel("Search by Citizen Name",1130,50,230,30) ;
        nameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(nameLabel) ;

        JTextField nameSearch = addTextField("",1130,85,178,30) ;
        frame.add(nameSearch) ;

        JButton nameSearchButton = addButton("Search",1310,85,90,30) ;
        searchButtonChanges(nameSearchButton) ;
        nameSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String name = nameSearch.getText().trim() ;
                citizenNameSearchErrorLabel = invalidLabelPrompt1(name.isBlank() || !name.matches("[a-zA-Z .]{1,30}"),citizenNameSearchErrorLabel,
                        "*Invalid Citizen Name!",1130,118,175,15) ;
                if(citizenNameSearchErrorLabel != null && citizenNameSearchErrorLabel.isVisible()){
                    return ;
                }
                showTableResult("SELECT citizen_id,fullName,cnic,gender,date_of_birth,address,phone,email,occupation,marital_status,dept_id FROM citizen WHERE fullName = ?",name);
            }
        });
        frame.add(nameSearchButton) ;

        JButton allBtn = addButton("All",110,160,55,25) ;
        allBtn.setFocusPainted(false) ;
        allBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTableResult("SELECT citizen_id,fullName,cnic,gender,date_of_birth,address,phone,email,occupation,marital_status,dept_id FROM citizen where 1 = ?","1");
            }
        });
        frame.add(allBtn) ;

        int x = 170, y = 160;
        for (char c = 'A'; c <= 'Z'; c++) {
            final char letter = c; // Capture the current letter

            JButton letterBtn = addButton(String.valueOf(letter),x,y,45,25) ;
            letterBtn.setFocusPainted(false) ;
            letterBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String buttonText = String.valueOf(letter) ;
                    String pattern = "(?i)^" + buttonText ;
                    String query = "SELECT citizen_id,fullName,cnic,gender,date_of_birth,address,phone,email,occupation,marital_status,dept_id FROM citizen where fullName regexp ?" ;
                    showTableResult(query,pattern);
                }
            });
            frame.add(letterBtn);

            x += 50;
        }

        manageBackButton() ;
    }

    JLabel liscencePlateErrorLabel ;// Label shown when user inputs an invalid licence plate number
    JLabel citizenIDErrorLabel ;// Label shown when user inputs an invalid citizen id
    JLabel vehicleModelErrorLabel ; // Label shown when user inputs an invalid vehicle model
    JLabel manufacturerErrorLabel ; // Label shown when user inputs an invalid manufacturer name
    JLabel yearErrorLabel ; // Label shown when user inputs an invalid manufacturing year
    JLabel colorErrorLabel ; // Label shown when user inputs an invalid color of vehicle
    JLabel vehicleRegisDateErrorLabel ; // Label shown when user inputs an invalid registration date of vehicle
    JLabel vehicleDeptErrorLabel ; // Label shown when user inputs an invalid department id of vehicle

    // Method to display the vehicle registration form window
    void addVehicleInfo() {
        resetFrame();
        registrationFormHeader("Vehicle Registration Form");

        registrationIDLabel("Vehicle ID:");
        JLabel textID = addLabel("", 200, 75, 100, 20);
        textID.setFont(new Font("Calibri", Font.BOLD, 18));
        frame.add(textID);

        final String[] id = {IDGenerator.generateVehicleID(connection, frame) };
        if(id[0] != null) {
            textID.setText(id[0]); // Assuming you have this generator
        }

        JLabel plateNo = addLabel("Licence Plate#:", 540, 110, 150, 20);
        plateNo.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(plateNo);

        JTextField vehiclePlateNo = addTextField("", 700, 100, 350, 30);
        frame.add(vehiclePlateNo);

        JLabel idlabel = addLabel("Citizen ID:", 540, 156, 150, 20);
        idlabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(idlabel);

        JTextField citizenId = addTextField("", 700, 148, 350, 30);
        frame.add(citizenId);

        JLabel type = new JLabel("Vehicle Type:");
        type.setBounds(540, 200, 150, 20);
        type.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(type);

        String[] vehicleOptions = {"car", "motorcycle", "scooter", "bicycle", "rickshaw",
                "auto rickshaw", "bus", "mini bus", "wagon", "goods truck",
                "ambulance", "fire brigade", "police vehicle", "tractor",
                "garbage truck", "taxi"};
        JComboBox<String> vehicleType = addComboBox(vehicleOptions, 700, 198, 120, 25);
        frame.add(vehicleType);

        JLabel model = addLabel("Model:", 540, 238, 150, 20);
        model.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(model);

        JTextField vehicleModel = addTextField("", 700, 230, 350, 30);
        frame.add(vehicleModel);

        JLabel manf = addLabel("Manufacturer:", 540, 290, 150, 20);
        manf.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(manf);

        JTextField vehicleManf = addTextField("", 700, 285, 350, 30);
        frame.add(vehicleManf);

        JLabel year = addLabel("Year:", 540, 340, 150, 20);
        year.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(year);

        JTextField vehicleYear = addTextField("", 700, 335, 350, 30);
        frame.add(vehicleYear);

        JLabel color = addLabel("Color:", 540, 390, 150, 20);
        color.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(color);

        JTextField vehicleColor = addTextField("", 700, 385, 350, 30);
        frame.add(vehicleColor);

        JLabel deptID = addLabel("Dept ID:", 540, 440, 150, 20);
        deptID.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(deptID);

        JTextField vehicleDeptID = addTextField("", 700, 435, 350, 30);
        frame.add(vehicleDeptID);

        JLabel regisDate = addLabel("Regis: Date:", 540, 490, 150, 20);
        regisDate.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(regisDate);

        JTextField vehicleRegisDate = addTextField("YYYY-MM-DD", 700, 485, 350, 30);
       addFocusListenerToTextField(vehicleRegisDate,"YYYY-MM-DD");
        frame.add(vehicleRegisDate);

        JButton submit = addButton("Submit", 950, 600, 100, 30);
        changesButtonView(submit);
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String vehiclePlateNumber = vehiclePlateNo.getText().trim() ;
                String vehicleOwner = citizenId.getText().trim() ;
                String vehicle_type = (String) vehicleType.getSelectedItem() ;
                String model = vehicleModel.getText().trim();
                String manufacturer = vehicleManf.getText().trim() ;
                String manf_year = vehicleYear.getText().trim() ;
                String color = vehicleColor.getText().trim();
                String DeptId = vehicleDeptID.getText().trim();
                String regisDate = vehicleRegisDate.getText().trim() ;

                boolean valid = true;
                boolean citizenDeptValidation = false ;


                // Validate plate number

                    liscencePlateErrorLabel = invalidLabelPrompt1( vehiclePlateNumber.isBlank() || !vehiclePlateNumber.matches("[A-Z0-9 -]{1,11}"),
                            liscencePlateErrorLabel, "*Invalid Plate Number!", 700, 130, 350, 15);
                    if (liscencePlateErrorLabel != null && liscencePlateErrorLabel.isVisible()) {
                        valid = false;
                    }

                    // Validate citizen ID exists in database
                if (!vehicleOwner.isBlank() &&  DeptId.isBlank()){
                    boolean citizenExists = JDBCConnector.search(
                            connection,
                            "SELECT citizen_id FROM citizen WHERE citizen_id = ?",
                            vehicleOwner,
                            frame
                    );
                    citizenIDErrorLabel = invalidLabelPrompt1(
                            !citizenExists,
                            citizenIDErrorLabel,
                            "*Invalid Citizen ID!",
                            700, 180, 350, 15
                    );
                    if (citizenExists) {
                        citizenDeptValidation = true;
                        if(vehicleDeptErrorLabel != null){
                            vehicleDeptErrorLabel.setVisible(false);
                        }
                    } else {
                        valid = false;
                    }
                }
                else if (!DeptId.isBlank() && vehicleOwner.isBlank()){
                    boolean deptExists = JDBCConnector.search(
                            connection,
                            "SELECT dept_id FROM department WHERE dept_id = ?",
                            DeptId,
                            frame
                    );
                    vehicleDeptErrorLabel = invalidLabelPrompt1(
                            !deptExists,
                            vehicleDeptErrorLabel,
                            "*Invalid Department ID!",
                            700, 468, 350, 15
                    );
                    if (deptExists) {
                        citizenDeptValidation = true;
                        if(citizenIDErrorLabel != null){
                            citizenIDErrorLabel.setVisible(false);
                        }
                    } else {
                        valid = false;
                    }
                }
                else {
                    vehicleDeptErrorLabel = invalidLabelPrompt1(
                            true,
                            vehicleDeptErrorLabel,
                            "*Invalid Department ID!",
                            700, 468, 350, 15
                    );
                    citizenIDErrorLabel = invalidLabelPrompt1(
                            true,
                            citizenIDErrorLabel,
                            "*Invalid Citizen ID!",
                            700, 180, 350, 15
                    );
                    valid = false;
                }

                // Validate model
                    vehicleModelErrorLabel = invalidLabelPrompt1(model.isBlank() || !model.matches("[A-Za-z0-9 -]{3,30}"), vehicleModelErrorLabel,
                            "*Invalid vehicle model!", 700, 263, 350, 15);
                    if (vehicleModelErrorLabel != null && vehicleModelErrorLabel.isVisible()) {
                        valid = false;
                    }

                // Validate manufacturer

                    manufacturerErrorLabel = invalidLabelPrompt1(manufacturer.isBlank() || !(manufacturer.matches("^[A-Za-z\\s]{2,30}$")),
                            manufacturerErrorLabel, "*Invalid Manufacturer Name", 700, 318, 350, 15);
                    if (manufacturerErrorLabel != null && manufacturerErrorLabel.isVisible()) {
                        valid = false;
                    }

                // Validate year
                int yearManfac = 0 ;
                    try {
                        int year = Integer.parseInt(manf_year.trim());
                        yearManfac = year ;
                        int currentYear = LocalDate.now().getYear();
                        yearErrorLabel = invalidLabelPrompt1(year > currentYear || year < 1947, yearErrorLabel,
                                "*Invalid Manufacturing year!", 700, 368, 350, 15);
                        if (yearErrorLabel != null && yearErrorLabel.isVisible()) {
                            valid = false;
                        }
                    } catch (NumberFormatException eyear) {
                        yearErrorLabel = invalidLabelPrompt1(true, yearErrorLabel,
                                "*Invalid Manufacturing year!", 700, 368, 350, 15);
                        valid = false;
                    }

                // Validate color

                    colorErrorLabel = invalidLabelPrompt1(color.isBlank() || !color.matches("[a-zA-Z ]{3,40}"), colorErrorLabel,
                            "*Invalid color!", 700, 418, 350, 15);
                    if (colorErrorLabel != null && colorErrorLabel.isVisible()) {
                        valid = false;
                    }

                // Validate registration date
                if(!regisDate.equals("YYYY-MM-DD")) {
                    try {
                        LocalDate regDate = LocalDate.parse(regisDate.trim());
                        int year = regDate.getYear();
                        int currentYear = LocalDate.now().getYear();
                        vehicleRegisDateErrorLabel = invalidLabelPrompt1(year > currentYear || year < 1947 || year < yearManfac, vehicleRegisDateErrorLabel,
                                "*Invalid registration date!", 700, 518, 350, 15);
                        if (vehicleRegisDateErrorLabel != null && vehicleRegisDateErrorLabel.isVisible()) {
                            valid = false;
                        }
                    } catch (DateTimeParseException rege) {
                        vehicleRegisDateErrorLabel = invalidLabelPrompt1(true, vehicleRegisDateErrorLabel,
                                "*Invalid registration date!", 700, 518, 350, 15);
                        valid = false;
                    }
                }
                else{
                    vehicleRegisDateErrorLabel = invalidLabelPrompt1(true, vehicleRegisDateErrorLabel,
                            "*Invalid registration date!", 700, 518, 350, 15);
                    valid = false;
                }

                if (!valid) {
                    if (!citizenDeptValidation) {
                        JOptionPane.showMessageDialog(frame, "Please provide **either** a valid Citizen ID or Department ID, not both.");
                    }
                    return ;
                }

                Vehicle vehicle = new Vehicle(id[0],vehiclePlateNumber,vehicleOwner,vehicle_type,model,manufacturer,manf_year,color,regisDate,DeptId) ;
                int rowsInserted = JDBCConnector.insertVehicle(frame,connection,"INSERT INTO vehicles (vehicle_id, plate_number, citizen_id, " +
                        "vehicle_model, manufacturer, manuf_year, color, dept_id, regis_date,vehicle_type) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",vehicle) ;
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(frame, "Vehicle registered successfully!");
                            JDBCConnector.insertAdminHistory(frame,adminID,"Add",id[0]);
                            int choiceToAddAnotherVehicle = JOptionPane.showConfirmDialog(frame,"Do you want to add anther vehicle?",
                                    "Confirm Add Vehicle",JOptionPane.OK_CANCEL_OPTION) ;
                            if(choiceToAddAnotherVehicle == JOptionPane.OK_OPTION){
                                id[0] = IDGenerator.generateVehicleID(connection, frame) ;
                                if (id[0] != null) {
                                    textID.setText(id[0]);
                                }
                                vehiclePlateNo.setText("") ;
                                citizenId.setText("") ;
                                vehicleType.setSelectedIndex(0);
                                vehicleModel.setText("");
                                vehicleManf.setText("");
                                vehicleYear.setText("");
                                vehicleColor.setText("");
                                vehicleDeptID.setText("");
                                vehicleRegisDate.setText("YYYY-MM-DD");
                                return ;
                            }else{
                                showAddInfoMenu();
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Failed to register vehicle.");
                            return;
                        }
            }
        });
        frame.add(submit);

        formBackButton();
    }

    JLabel vehicleCitizenIDSearchErrorLabel ; // Label shown when user enters invalid citizen id in search box
    JLabel vehicleDeptIDSearchErrorLabel ;// Label shown when user enters invalid department id in search box
    JLabel vehiclePlateSearchErrorLabel ; // Label shown when user enters invalid plate number of vehicle in search box
    JLabel vehicleIDSearchErrorLabel ; // Label shown when user enters invalid vehicle ud in search box

    // Method to manage vehicle records such as search , update and delete
    void viewVehicleInfo(){
        resetFrame() ;

        viewFrameHeader("********Manage Vehicle Records********");

        JLabel vehicleLabel = addLabel("Search by Vehicle ID",160, 50, 190, 30) ;
        vehicleLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(vehicleLabel) ;

        JTextField vehicleSearch = addTextField("",160, 85, 178, 30) ;
        frame.add(vehicleSearch) ;

        JButton vehicleIDSearchButton = addButton("Search",340, 85, 90, 30) ;
        searchButtonChanges(vehicleIDSearchButton) ;
        vehicleIDSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String vehicle_id = vehicleSearch.getText().trim() ;

                vehicleIDSearchErrorLabel = invalidLabelPrompt1(vehicle_id.isBlank() || !vehicle_id.matches("^V[0-9]{7}"),vehicleIDSearchErrorLabel,
                        "*Invalid Vehicle ID!",160, 118, 200, 15) ;
                if(vehicleIDSearchErrorLabel != null && vehicleIDSearchErrorLabel.isVisible()){
                   return;
                }
                showTableResult("select vehicle_id,plate_number,vehicle_model,manufacturer,manuf_year,color,vehicle_type,regis_date,citizen_id,dept_id FROM vehicles WHERE vehicle_id = ?",vehicle_id);
            }
        });
        frame.add(vehicleIDSearchButton) ;


        JLabel plateLabel = addLabel("Search by Licence Plate#",450,50,210,30) ;
        plateLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(plateLabel) ;

        JTextField  plateLiscence = addTextField("",450,85,178,30) ;
        frame.add(plateLiscence) ;

        JButton plateNumberSearchButton = addButton("Search",630,85,90,30) ;
        searchButtonChanges(plateNumberSearchButton) ;
        plateNumberSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String plateNumber = plateLiscence.getText().trim() ;
                vehiclePlateSearchErrorLabel = invalidLabelPrompt1(plateNumber.isBlank() || !plateNumber.matches("[A-Z0-9 -]{1,11}"),vehiclePlateSearchErrorLabel,
                        "*Invalid Vehicle Licence Plate#!",450,118,175,15) ;
                if(vehiclePlateSearchErrorLabel != null && vehiclePlateSearchErrorLabel.isVisible()){
                    return ;
                }
                showTableResult("select vehicle_id,plate_number,vehicle_model,manufacturer,manuf_year,color,vehicle_type,regis_date,citizen_id,dept_id FROM vehicles WHERE plate_number = ?",plateNumber);
            }
        });
        frame.add(plateNumberSearchButton) ;

        JLabel deptIdLabel = addLabel("Search by Department ID", 740, 50, 230, 30);
        deptIdLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(deptIdLabel);

        JTextField deptSearch = addTextField("",740, 85, 178, 30);
        frame.add(deptSearch);

        JButton deptSearchButton = addButton("Search", 920, 85, 90, 30);
        searchButtonChanges(deptSearchButton);

        deptSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String deptID = deptSearch.getText().trim();
                vehicleDeptIDSearchErrorLabel = invalidLabelPrompt1(deptID.isBlank() || !deptID.matches("^D[0-9]{7}"),vehicleDeptIDSearchErrorLabel,
                        "*Please enter Department ID!", 740, 118, 220, 15) ;

                if (vehicleDeptIDSearchErrorLabel != null && vehicleDeptIDSearchErrorLabel.isVisible()) {
                    return;
                }
                showTableResult("select vehicle_id,plate_number,vehicle_model,manufacturer,manuf_year,color,vehicle_type,regis_date,citizen_id,dept_id FROM vehicles WHERE dept_id = ?",deptID);
            }
        });

        frame.add(deptSearchButton);

        JLabel idLabel = addLabel("Search by Citizen ID",1030,50,230,30 );
        idLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(idLabel);

        JTextField idSearch = addTextField( "",1030,85,178,30);
        frame.add(idSearch);

        JButton idSearchButton = addButton("Search", 1200,85,90,30);
        searchButtonChanges(idSearchButton);

        idSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String citizenID = idSearch.getText().trim();

                vehicleCitizenIDSearchErrorLabel = invalidLabelPrompt1(citizenID.isBlank() || !citizenID.matches("^C[0-9]{7}"),vehicleCitizenIDSearchErrorLabel,
                        "*Please enter Citizen ID!", 1030,118,175,15) ;
                if(vehicleCitizenIDSearchErrorLabel != null && vehicleCitizenIDSearchErrorLabel.isVisible()){
                    return ;
                }
                showTableResult("select vehicle_id,plate_number,vehicle_model,manufacturer,manuf_year,color,vehicle_type,regis_date,citizen_id,dept_id FROM vehicles WHERE citizen_id = ?",citizenID);
            }
        });
        frame.add(idSearchButton);

        JButton searchAllVehicles = addButton("Search All Vehicles",1300,85,200,30) ;
        searchButtonChanges(searchAllVehicles);
        searchAllVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTableResult("select vehicle_id,plate_number,vehicle_model,manufacturer,manuf_year,color,vehicle_type,regis_date,citizen_id,dept_id FROM vehicles WHERE 1 = ?","1");
            }
        });
        frame.add(searchAllVehicles) ;

        manageBackButton() ;
    }

    JLabel buildingNameErrorLabel ; // Label to show a validation prompt when user enter invalid building name in the registration form
    JLabel buildingAddressErrorLabel ; // Label to show a validation prompt when user enter invalid building  in address the registration form
    JLabel buildingCitizenIDErrorLabel ; // Label to show a validation prompt when user enter invalid building citizen id in the registration form
    JLabel buildingDeptIDErrorLabel ; // Label to show a validation prompt when user enter invalid building department id in the registration form

    // Method to create  a window that displays the registration form of building
    void addBuildingInfo(){
        resetFrame() ;
        registrationFormHeader("Building Registration Form") ;

        registrationIDLabel("Building ID:") ;

        JLabel textID = addLabel("",200,75,100,20) ;
        textID.setFont(new Font("Calibri", Font.BOLD, 18));
        frame.add(textID) ;

        final String[] buildingID = {IDGenerator.generateBuildingID(connection,frame)} ;
        if( buildingID[0] != null) {
            textID.setText(buildingID[0]);
        }

        JLabel name = addLabel("Building Name:",540,110,150,20) ;
        name.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(name) ;

        JTextField buildingName = addTextField("",700,100,350,30) ;
        frame.add(buildingName) ;

        JLabel address = addLabel("Address:",540,168,150,20) ;
        address.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(address) ;

        JTextField buildingAddress = addTextField("",700,160,350,30) ;
        frame.add(buildingAddress) ;

        JLabel type = new JLabel("Type:");
        type.setBounds(540, 215, 150, 20);
        type.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(type);

        String[] buildingTypesOptions = {"Residential House",
                "Apartment Building","Shop/Commercial Unit","Office Building","School","Mosque",
                "Hospital/Clinic"
        } ;
        JComboBox<String> buildingTypeBox = addComboBox(buildingTypesOptions,700, 210, 150, 25) ;
        frame.add(buildingTypeBox) ;

        JLabel nOfloor = addLabel("Floors#:",540,255,150,20) ;
        nOfloor.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(nOfloor) ;

        String[] floors = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"} ;

        JComboBox<String> buildingNOfloor = addComboBox(floors,700,245,120,25) ;
        frame.add(buildingNOfloor) ;

        JLabel id = addLabel("Citizen ID:",540,293,150,20) ;
        id.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(id) ;

        JTextField citizenID = addTextField("",700,285,350,30) ;
        frame.add(citizenID) ;

        JLabel dept = addLabel("Department ID:",540,345,150,20) ;
        dept.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(dept) ;

        JTextField deptID = addTextField("",700,337,350,30) ;
        frame.add(deptID) ;

        JButton submit = addButton("Submit",950,450,100,30) ;
        changesButtonView(submit) ;

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent sb) {
                boolean valid = true;
                String nameVal = buildingName.getText().trim();
                String addressVal = buildingAddress.getText().trim();
                String typeVal = (String) buildingTypeBox.getSelectedItem();
                String floorsVal = (String) buildingNOfloor.getSelectedItem();
                String citizenIDVal = citizenID.getText().trim();
                String deptIDVal = deptID.getText().trim();

                // Validation
                buildingNameErrorLabel = invalidLabelPrompt1( nameVal.isBlank() || !nameVal.matches("[a-zA-Z0-9 .'-]{2,40}"),buildingNameErrorLabel,
                        "*Invalid building name!", 700, 133, 350, 15) ;
                if (buildingNameErrorLabel != null && buildingNameErrorLabel.isVisible()) {
                    valid = false;
                }

                buildingAddressErrorLabel = invalidLabelPrompt1(addressVal.isBlank() || !addressVal.matches("[a-zA-Z0-9\\s,./#'()-]{2,50}"),
                        buildingAddressErrorLabel,"*Invalid building address!", 700, 193, 350, 15) ;
                if (buildingAddressErrorLabel != null && buildingAddressErrorLabel.isVisible()) {
                    valid = false;
                }

// Case 0: Both IDs are filled – Not Allowed
                if (!citizenIDVal.isBlank() && !deptIDVal.isBlank()) {
                    buildingCitizenIDErrorLabel = invalidLabelPrompt1(
                            true, buildingCitizenIDErrorLabel,
                            "*Invalid citizen ID!", 700, 318, 350, 15
                    );
                    buildingDeptIDErrorLabel = invalidLabelPrompt1(
                            true, buildingDeptIDErrorLabel,
                            "*Invalid Department ID!", 700, 370, 350, 15
                    );

                    JOptionPane.showMessageDialog(
                            frame,
                            "Either insert citizen ID or department ID but not both",
                            "Insert Data Validation",
                            JOptionPane.ERROR_MESSAGE
                    );
                    valid = false;
                }
// Case 1: Only Citizen ID is provided
                else if (!citizenIDVal.isBlank()) {
                    boolean citizenExists = JDBCConnector.search(
                            connection,
                            "SELECT citizen_id FROM citizen WHERE citizen_id = ?",
                            citizenIDVal,
                            frame
                    );

                    buildingCitizenIDErrorLabel = invalidLabelPrompt1(
                            !citizenExists, buildingCitizenIDErrorLabel,
                            "*Invalid citizen ID!", 700, 318, 350, 15
                    );

                    if (citizenExists) {
                        // Hide department error label if present
                        if (buildingDeptIDErrorLabel != null) {
                            buildingDeptIDErrorLabel.setVisible(false);
                        }
                    } else {
                        valid = false;
                    }
                }
// Case 2: Only Department ID is provided
                else if (!deptIDVal.isBlank()) {
                    boolean deptExists = JDBCConnector.search(
                            connection,
                            "SELECT dept_id FROM department WHERE dept_id = ?",
                            deptIDVal,
                            frame
                    );

                    buildingDeptIDErrorLabel = invalidLabelPrompt1(
                            !deptExists, buildingDeptIDErrorLabel,
                            "*Invalid Department ID!", 700, 370, 350, 15
                    );

                    if (deptExists) {
                        // Hide citizen error label if present
                        if (buildingCitizenIDErrorLabel != null) {
                            buildingCitizenIDErrorLabel.setVisible(false);
                        }
                    } else {
                        valid = false;
                    }
                }
// Case 3: Both empty – Not Allowed
                else {
                    buildingCitizenIDErrorLabel = invalidLabelPrompt1(
                            true, buildingCitizenIDErrorLabel,
                            "*Invalid citizen ID!", 700, 318, 350, 15
                    );
                    buildingDeptIDErrorLabel = invalidLabelPrompt1(
                            true, buildingDeptIDErrorLabel,
                            "*Invalid Department ID!", 700, 370, 350, 15
                    );
                    valid = false;
                }

                if (!valid){
                    return;
                }

                int rowsInserted = JDBCConnector.insertBuidling(frame,connection,
                            "INSERT INTO building (building_id, bname, badress, btype, bfloor, citizen_id, dept_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                            new Building(nameVal,buildingID[0],addressVal,typeVal,floorsVal,citizenIDVal,deptIDVal)) ;
                    if ( rowsInserted > 0) {
                        JOptionPane.showMessageDialog(frame, "Building added successfully!");
                        JDBCConnector.insertAdminHistory(frame,adminID,"Add",buildingID[0]);
                        int optionToAddAnotherBuilding = JOptionPane.showConfirmDialog(frame,"Do you want to add another building?","Confirm" +
                                " Add Building",JOptionPane.OK_CANCEL_OPTION) ;
                        if(optionToAddAnotherBuilding == JOptionPane.OK_OPTION) {
                            buildingID[0] = IDGenerator.generateBuildingID(connection, frame) ;
                            if (buildingID[0] != null) {
                                textID.setText(buildingID[0]);
                            }
                            buildingName.setText("");
                            buildingAddress.setText("");
                            buildingTypeBox.setSelectedIndex(0);
                            buildingNOfloor.setSelectedIndex(0);
                            citizenID.setText("");
                            deptID.setText("");
                            return;
                        }
                        else{
                            showAddInfoMenu();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to add building!", "Error", JOptionPane.ERROR_MESSAGE);
                        return ;
                    }
            }
        });
        frame.add(submit) ;

        formBackButton() ;
    }

    JLabel buildingIDSearchErrorLabel ; // label shown when user enters an invalid building id in search box
    JLabel buildingNameSearchErrorLabel ; // label shown when user enters an invalid building name in search box
    JLabel buildingCitizenIDSearchErrorLabel ; // label shown when user enters an invalid citizen id in search box
    JLabel buildingDeptIDSearchErrorLabel ; // label shown when user enters an invalid department id in search box

    // Method to create a display to manage the building records
    void viewBuildingInfo(){
        resetFrame() ;

        viewFrameHeader("********Manage Building Records********");

        JLabel bid = addLabel("Search by Building ID",200, 50, 190, 30) ;
        bid.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(bid) ;

        JTextField  buildID = addTextField("",200, 85, 178, 30) ;
        frame.add(buildID) ;

        JButton buildIDSearchButton = addButton("Search",380, 85, 90, 30) ;
        searchButtonChanges(buildIDSearchButton) ;
        buildIDSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String building_id = buildID.getText().trim() ;

                buildingIDSearchErrorLabel = invalidLabelPrompt1(building_id.isBlank() || !building_id.matches("^B[0-9]{7}"),buildingIDSearchErrorLabel,
                        "*Invalid Building ID!", 200, 118, 200, 15) ;
                if(buildingIDSearchErrorLabel != null && buildingIDSearchErrorLabel.isVisible()){
                   return ;
                }
                showTableResult("select * from building where building_id = ?",building_id);
            }
        });
        frame.add(buildIDSearchButton) ;

        JLabel bName = addLabel("Search by Building Name",500,50,230,30) ;
        bName.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(bName) ;

        JTextField bnameSearch = addTextField("",500,85,178, 30) ;
        frame.add(bnameSearch) ;

        JButton bnameSearchButton = addButton("Search",680,85,90,30) ;
        searchButtonChanges(bnameSearchButton) ;
        bnameSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String buildingName = bnameSearch.getText().trim() ;

                buildingNameSearchErrorLabel = invalidLabelPrompt1(buildingName.isBlank() || !buildingName.matches("[a-zA-Z0-9 .'-]{2,40}"),buildingNameSearchErrorLabel,
                        "*Invalid Building Name!",500,118,175,15) ;
                if(buildingNameSearchErrorLabel != null && buildingNameSearchErrorLabel.isVisible()){
                   return ;
                }
                showTableResult("select * from building where bname = ?",buildingName);
            }
        });
        frame.add(bnameSearchButton) ;

        JLabel deptIdLabel = addLabel("Search by Department ID", 820, 50, 230, 30);
        deptIdLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(deptIdLabel);

        JTextField deptSearch = addTextField("",820, 85, 178, 30);
        frame.add(deptSearch);

        JButton deptSearchButton = addButton("Search", 1000, 85, 90, 30);
        searchButtonChanges(deptSearchButton);

        deptSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String deptID = deptSearch.getText().trim();

                buildingDeptIDSearchErrorLabel = invalidLabelPrompt1(deptID.isBlank() || !deptID.matches("^D[0-9]{7}"),buildingDeptIDSearchErrorLabel,
                        "*Please enter Department ID!", 820, 118, 220, 15) ;

                if (buildingDeptIDSearchErrorLabel != null && buildingDeptIDSearchErrorLabel.isVisible()) {
                    return;
                }
                showTableResult("SELECT * FROM building WHERE dept_id = ?",deptID);
            }
        });
        frame.add(deptSearchButton);

        JLabel idLabel = addLabel("Search by Citizen ID", 1140,50,230,30);
        idLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(idLabel);

        JTextField idSearch = addTextField("",1140,85,178,30);
        frame.add(idSearch);

        JButton idSearchButton = addButton("Search",  1320,85,90,30);
        searchButtonChanges(idSearchButton);

        idSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String citizenID = idSearch.getText().trim();

                buildingCitizenIDSearchErrorLabel = invalidLabelPrompt1(citizenID.isBlank() || !citizenID.matches("^C[0-9]{7}"),buildingCitizenIDSearchErrorLabel,
                        "*Invalid Citizen ID!",1140,118,175,15) ;

                if (buildingCitizenIDSearchErrorLabel != null && buildingCitizenIDSearchErrorLabel.isVisible()) {
                    return;
                }
                showTableResult("select * from building where citizen_id = ?",citizenID);
            }
        });

        frame.add(idSearchButton);

        String[] buildingTypes = {"All", "Residential House",
                "Apartment Building","Shop/Commercial Unit","Office Building","School","Mosque",
                "Hospital/Clinic"};
        int x = 200; // Starting x position for first button

        for (String type : buildingTypes) {
            int width = type.equals("All") ? 50 : type.equals("School") || type.equals("Mosque")?100:type.equals("Shop/Commercial Unit")?170:   150 ; // Narrower width for "All"

            JButton btn = addButton(type, x, 150, width, 20);
            btn.setFocusPainted(false);

            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // You can handle based on type
                    switch (type) {
                        case "All":
                            showTableResult("select * from building where 1 = ?","1");
                            break;
                        case "Residential House":
                            showTableResult("select * from building where btype = ?",type);
                            break;
                        case "Apartment Building":
                            showTableResult("select * from building where btype = ?",type);
                            break;
                        case "Shop/Commercial Unit":
                            showTableResult("select * from building where btype = ?",type);
                            break;
                        case "School":
                            showTableResult("select * from building where btype = ?",type);
                            break;
                        case "Office Building":
                            showTableResult("select * from building where btype = ?",type);
                            break;
                        case "Mosque":
                            showTableResult("select * from building where btype = ?",type);
                            break;
                        case "Hospital/Clinic":
                            showTableResult("select * from building where btype = ?",type);
                            break;

                    }
                }
            });
            frame.add(btn);
            x += (width + 5); // Space between buttons
        }
        manageBackButton() ;

    }

    JLabel departmentNameErrorLabel ; // Label shown when user enter an invalid department name in the department registration form
    JLabel departmentServiceErrorLabel ; // Label shown when user enter an invalid department service in the department registration form
    JLabel departmentContactErrorLabel ; // // Label shown when user enter an invalid department contact in the department registration form
    JLabel departmentEmailErrorLabel ; // Label shown when user enter an invalid department email in the department registration form
    JLabel departmentBuildingIdErrorLabel ; // Label shown when user enter an invalid department building id in the department registration form

    // Method to create department registration form
    void addDepartmentInfo(){
        resetFrame() ;
        registrationFormHeader("Department Registration Form") ;

        registrationIDLabel("Dept ID:") ;

        JLabel textID = addLabel("",200,75,100,20) ;
        textID.setFont(new Font("Calibri", Font.BOLD, 18));
        frame.add(textID) ;
        final String[] deptID = {IDGenerator.generateDepartmentID(connection,frame)} ;
        if( deptID[0] != null){
            textID.setText(deptID[0]);
        }

        JLabel name = addLabel("Dept Name:",540,110,150,20) ;
        name.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(name) ;

        JTextField deptName = addTextField("",700,100,350,30) ;
        frame.add(deptName) ;

        JLabel service = addLabel("Service:",540,155,150,20) ;
        service.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(service) ;

        JTextField departmentService = addTextField("",700,150,350,30) ;
        frame.add(departmentService) ;

        JLabel contact = new JLabel("Contact#:");
        contact.setBounds(540,205, 150, 20);
        contact.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(contact);

        JTextField deptContact = addTextField("e.g:- 0##########",700,200,350,30) ;
        addFocusListenerToTextField(deptContact,"e.g:- 0##########");
        frame.add(deptContact) ;

        JLabel email = addLabel("Email:",540,255,150,20) ;
        email.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(email) ;

        JTextField deptEmail = addTextField("e.g:- username@gmail.com",700,250,350,30) ;
       addFocusListenerToTextField(deptEmail,"e.g:- username@gmail.com");
        frame.add(deptEmail) ;

        JLabel buildingID = addLabel("Building ID:",540,305,150,20) ;
        buildingID.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(buildingID) ;

        JTextField deptBuildingID = addTextField("",700,300,350,30) ;
        frame.add(deptBuildingID) ;


        JButton submit = addButton("Submit",950,460,100,30) ;
        changesButtonView(submit) ;
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = deptName.getText().trim();
                String serviceText = departmentService.getText().trim();
                String contactText = deptContact.getText().trim();
                String emailText = deptEmail.getText().trim();
                String buildID = deptBuildingID.getText().trim() ;

                boolean valid = true;

                departmentNameErrorLabel = invalidLabelPrompt1(
                        name.isBlank() || !name.matches("[a-zA-Z .-]{2,40}"),
                        departmentNameErrorLabel,
                        "*Invalid department name!", 700, 133, 350, 15
                );
                if (departmentNameErrorLabel != null && departmentNameErrorLabel.isVisible()) {
                    valid = false;
                }

                departmentServiceErrorLabel = invalidLabelPrompt1(
                        serviceText.isBlank() || !serviceText.matches("[a-zA-Z .,-]{4,40}"),
                        departmentServiceErrorLabel,
                        "*Invalid department service!", 700, 183, 350, 15
                );
                if (departmentServiceErrorLabel != null && departmentServiceErrorLabel.isVisible()) {
                    valid = false;
                }

                if(!contactText.equals("e.g:- 0##########")){
                    departmentContactErrorLabel = invalidLabelPrompt1(
                            (contactText.isBlank() || !contactText.matches("0[0-9]{9,10}")),
                            departmentContactErrorLabel,
                            "*Invalid department contact!", 700, 233, 350, 15
                    );
                    if (departmentContactErrorLabel != null && departmentContactErrorLabel.isVisible()) {
                        valid = false;
                    }
                }else{
                    departmentContactErrorLabel = invalidLabelPrompt1(true,departmentContactErrorLabel,
                            "*Invalid department contact!", 700, 233, 350, 15) ;
                    valid = false ;
                }

                if(!emailText.equals("e.g:- username@gmail.com")) {
                    departmentEmailErrorLabel = invalidLabelPrompt1(
                                   (emailText.isBlank() || !emailText.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")),
                            departmentEmailErrorLabel,
                            "*Invalid email address! e.g: user@example.com", 700, 283, 350, 15
                    );
                    if (departmentEmailErrorLabel != null && departmentEmailErrorLabel.isVisible()) {
                        valid = false;
                    }
                }else{
                    departmentEmailErrorLabel = invalidLabelPrompt1(true,departmentEmailErrorLabel,
                            "*Invalid email address! e.g: user@example.com", 700, 283, 350, 15) ;
                    valid = false ;
                }

                if (!buildID.isBlank()) {
                    boolean buildingExists = JDBCConnector.search(
                            connection, "SELECT building_id FROM building WHERE building_id = ?", buildID, frame);
                    departmentBuildingIdErrorLabel = invalidLabelPrompt1(
                            !buildingExists,
                            departmentBuildingIdErrorLabel,
                            "*Invalid building ID!", 700, 333, 350, 15
                    );

                    if (departmentBuildingIdErrorLabel != null && departmentBuildingIdErrorLabel.isVisible()) {
                        valid = false;
                    }
                }


                if (!valid) return;

                int rows = JDBCConnector.insertDepartment(
                        frame, connection,
                        "INSERT INTO department (dept_id, dept_name, contact, email, service, building_id) VALUES (?, ?, ?, ?, ?, ?)",
                        new Department(name, deptID[0], contactText, emailText, serviceText, buildID)
                );

                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Department added successfully!");
                    JDBCConnector.insertAdminHistory(frame,adminID,"Add",deptID[0]);
                    int optionToAddAnotherDepartment = JOptionPane.showConfirmDialog(
                            frame,
                            "Do you want to add another department?",
                            "Confirm Add Department",
                            JOptionPane.OK_CANCEL_OPTION
                    );

                    if (optionToAddAnotherDepartment == JOptionPane.OK_OPTION) {
                      deptID[0] = IDGenerator.generateDepartmentID(connection, frame);
                        if (deptID[0] != null) {
                            textID.setText(deptID[0]);
                            deptName.setText("");
                            departmentService.setText("");
                            deptContact.setText("e.g:- 0##########");
                            deptEmail.setText("e.g:- username@gmail.com");
                            deptBuildingID.setText("");
                            return;
                        }
                    } else {
                        showAddInfoMenu();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to insert department!");
                    return;
                }
            }
        });

        frame.add(submit) ;
        formBackButton() ;
    }

    JLabel deptIDSearchErrorLabel ; // Label shown when user enters an invalid department id in search box
    JLabel deptNameSearchErrorLabel ; // Label shown when user enters an invalid department name in search box
    JLabel deptBuildingIDSearchErrorLabel ; // Label shown when user enters an invalid department building id in search box

    // Method to create a window to manage department records such as search , update , and delete
    void viewDepartmentInfo(){
        resetFrame() ;

        viewFrameHeader("********Manage Department Records********");

        JLabel deptIdLabel = addLabel("Search by Department ID", 200, 50, 230, 30);
        deptIdLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(deptIdLabel);

        JTextField deptSearch = addTextField("",200, 85, 178, 30);
        frame.add(deptSearch);

        JButton deptSearchButton = addButton("Search", 380, 85, 90, 30);
        searchButtonChanges(deptSearchButton);

        deptSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String deptID = deptSearch.getText().trim();

                deptIDSearchErrorLabel = invalidLabelPrompt1(deptID.isBlank() || !deptID.matches("^D[0-9]{7}"),deptIDSearchErrorLabel,
                        "*Invalid Department ID!", 200, 118, 200, 15) ;
                if (deptIDSearchErrorLabel != null && deptIDSearchErrorLabel.isVisible()) {
                    return;
                }
                showTableResult("SELECT * FROM department WHERE dept_id = ?",deptID);
            }
        });

        frame.add(deptSearchButton);

        JLabel bNameLabel = addLabel("Search by Department Name", 500,50,240,30);
        bNameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(bNameLabel);

        JTextField nameSearch = addTextField("",500,85,178,30);
        frame.add(nameSearch);

        JButton nameSearchButton = addButton("Search",  680,85,90,30);
        searchButtonChanges(nameSearchButton);

        nameSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameSearch.getText().trim();

                deptNameSearchErrorLabel = invalidLabelPrompt1(name.isBlank() || !name.matches("[a-zA-Z .-]{2,40}"),deptNameSearchErrorLabel,
                        "*Invalid Department Name!", 500,118,175,15) ;
                if (deptNameSearchErrorLabel != null && deptNameSearchErrorLabel.isVisible()) {
                    return;
                }
                showTableResult("select * from department where dept_name = ?",name);

            }
        });
        frame.add(nameSearchButton);


        JLabel bidLabel = addLabel("Search by Building ID", 820, 50, 230, 30) ;
        bidLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        frame.add(bidLabel) ;


        JTextField  bidSearch = addTextField("",820, 85, 178, 30) ;
        frame.add(bidSearch) ;

        JButton bidSearchButton = addButton("Search",1000, 85, 90, 30) ;
        searchButtonChanges(bidSearchButton) ;
        bidSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String building_id = bidSearch.getText().trim() ;

                deptBuildingIDSearchErrorLabel = invalidLabelPrompt1(building_id.isBlank() || !building_id.matches("^B[0-9]{7}"),deptBuildingIDSearchErrorLabel,
                        "*Invalid Building ID!",820, 118, 220, 15) ;

                if(deptBuildingIDSearchErrorLabel != null && deptBuildingIDSearchErrorLabel.isVisible()){
                    return ;
                }
                showTableResult("select * from department where building_id = ?",building_id);
            }
        });
        frame.add(bidSearchButton) ;

        JButton searchAllDepartment = addButton("Search All Departments",1150,85,230,30) ;
        searchButtonChanges(searchAllDepartment);
        searchAllDepartment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTableResult("select * from department where 1 = ?","1");
            }
        });
        frame.add(searchAllDepartment);

        manageBackButton() ;
    }


    JScrollPane currentScrollPane = null; // The current scroll pane that holds the data of the recent table displayed

   // Method to show the search results of entities except admin history in the JTable with edit and delete buttons
    void showTableResult(String query, String searchingByField) {
        ResultSet result = JDBCConnector.searchResult(connection, frame, query, searchingByField);
        if (result == null) {
            return;
        } else {
            try {
                if(!result.isBeforeFirst()){
                    JOptionPane.showMessageDialog(frame,"No Such Result Found!");
                    return;
                }
                else {
                    if (currentScrollPane != null) {
                        frame.remove(currentScrollPane);
                    }
                    ArrayList<String> columnNames = new ArrayList<>();
                    ResultSetMetaData resultSetMetaData = result.getMetaData();
                    int numberOfColumns = resultSetMetaData.getColumnCount();

                    for (int i = 1; i <= numberOfColumns; i++) {
                        columnNames.add(resultSetMetaData.getColumnName(i));
                    }

                    EditableTableModel model = new EditableTableModel();
                    for (String col : columnNames) {
                        model.addColumn(col);
                    }
                    model.addColumn("Edit");
                    model.addColumn("Delete");

                    while (result.next()) {
                        Object[] row = new Object[numberOfColumns + 2];
                        for (int i = 1; i <= numberOfColumns; i++) {
                            row[i - 1] = result.getObject(i);
                        }
                        row[numberOfColumns] = "Edit";
                        row[numberOfColumns + 1] = "Delete";
                        model.addRow(row);
                    }

                    JTable table = new JTable(model);
                    table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
                    table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), table, "Edit", frame));

                    table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
                    table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), table, "Delete", frame));

                    currentScrollPane = new JScrollPane(table);
                    currentScrollPane.setBounds(100, 250, 1350, 500);
                    frame.add(currentScrollPane);
                    frame.repaint();
                    frame.revalidate(); // this is also important!
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to display records of admin history in JTable with no editable table
    void showAdminHistoryResult(String query, String searchingByField) {
        ResultSet result = JDBCConnector.searchResult(connection, frame, query, searchingByField);
        if (result == null) {
            return;
        } else {
            try {
                if(!result.isBeforeFirst()){
                    JOptionPane.showMessageDialog(frame,"No Such Result Found!");
                    return;
                }
                else {
                    if (currentScrollPane != null) {
                        frame.remove(currentScrollPane);
                    }
                    ArrayList<String> columnNames = new ArrayList<>();
                    ResultSetMetaData resultSetMetaData = result.getMetaData();
                    int numberOfColumns = resultSetMetaData.getColumnCount();

                    for (int i = 1; i <= numberOfColumns; i++) {
                        columnNames.add(resultSetMetaData.getColumnName(i));
                    }

                    DefaultTableModel model = new DefaultTableModel() {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false; // All cells are non-editable
                        }
                    };

                    for (String col : columnNames) {
                        model.addColumn(col);
                    }
                    while (result.next()) {
                        Object[] row = new Object[numberOfColumns + 2];
                        for (int i = 1; i <= numberOfColumns; i++) {
                            row[i - 1] = result.getObject(i);
                        }
                        model.addRow(row);
                    }

                    JTable table = new JTable(model);
                    currentScrollPane = new JScrollPane(table);
                    currentScrollPane.setBounds(100, 250, 1350, 500);
                    frame.add(currentScrollPane);
                    frame.repaint();
                    frame.revalidate(); // this is also important!
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   // Method to create a back button in the registration forms of citizen, vehicle,building, and department that navigates to the add information menu
    // but before one single confirmation
    void formBackButton(){
        JButton back = addButton("BACK",250,650,120,40) ;
        changesButtonView(back) ;
        back.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Do you want to cancel the form?\nNote: If you exit, your filled data" +
                            " will be deleted!",
                    "Confirm Exit",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if(choice == JOptionPane.OK_OPTION){
                showAddInfoMenu() ;
            }
        }) ;
        frame.add(back) ;
    }

    // Method to create a back button in the manage windows of citizen, building, vehicle and department
    void manageBackButton(){
        JButton back = addButton("BACK",20,25,90,35) ;
        changesButtonView(back) ;
        back.addActionListener(e -> showViewInfoMenu()) ;
        frame.add(back) ;
    }

    // Method to show back button to navigate to method showAdminInfoMenu
    void showBackAdminBackButton(){
        JButton back = addButton("BACK",20,25,90,35) ;
        changesButtonView(back) ;
        back.addActionListener(e -> showAdminInfoMenu()) ;
        frame.add(back) ;
    }

    // Method to changes the view of a JButton
    void changesButtonView(JButton button){
        button.setFocusPainted(false) ;
        button.setForeground(Color.YELLOW) ;
        button.setBackground(Color.RED) ;
        button.setFont(new Font("Calibri",Font.BOLD,20)) ;
    }

    // method to display the header of registration form
    void registrationFormHeader(String text){
        JLabel label = addLabel(text,580, 30, 400, 30) ;
        label.setFont(new Font("Calibri", Font.BOLD, 25));
        label.setForeground(Color.RED);
        label.setOpaque(false);
        label.setHorizontalAlignment(SwingConstants.CENTER) ;
        frame.add(label) ;
    }

    // Method to create a label that displays the message weather the registration id is of citizen, vehicle, building or department or admin in the registration form
    void registrationIDLabel(String input){
        JLabel id = addLabel(input,100,75,90,20) ;
        id.setFont(new Font("Calibri", Font.BOLD, 18));
        id.setForeground(Color.RED) ;
        id.setOpaque(false) ;
        frame.add(id) ;
    }

    // Method to make changes within a button view
    void searchButtonChanges(JButton button){
        button.setFocusPainted(false) ;
        button.setForeground(Color.WHITE) ;
        button.setBackground(Color.BLUE) ;
        button.setFont(new Font("Calibri",Font.BOLD,20)) ;
    }

    // Method to display the header in the manage information window where entities are search, updated, and deleted
    void viewFrameHeader(String headerLabel){
        JLabel header = addLabel(headerLabel,500,10,500,35) ;
        header.setFont(new Font("Comic",Font.BOLD,25));
        header.setForeground(Color.BLACK);
        header.setOpaque(true);
        header.setBackground(Color.GREEN);
        header.setHorizontalAlignment(JTextField.CENTER);
        frame.add(header) ;
    }

    public static void main(String[] args) {
        SmartCity mainObj = new SmartCity();
        mainObj.adminLogin();
    }
}

