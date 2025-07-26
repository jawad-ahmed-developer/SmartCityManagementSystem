import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean clicked;
    private JTable table;
    private String action;
    private JFrame frame;


    public ButtonEditor(JCheckBox checkBox, JTable table, String action, JFrame frame) {
        super(checkBox);
        this.table = table;
        this.action = action;
        this.frame = frame;
        button = new JButton();
        button.setOpaque(true);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int col) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clicked = true;
        return button;
    }

    JButton saveChangesButton = null ;
    public Object getCellEditorValue(){
        if (clicked){
            int row = table.getEditingRow();
            String primaryKey = table.getValueAt(row, 0).toString();

            if ("Edit".equals(action)) {
                // Enable editing for that row
                EditableTableModel model = (EditableTableModel) table.getModel();
                model.setEditableRow(row);
                table.repaint();

                saveChangesButton = new JButton("Save Changes") ;
                saveChangesButton.setBounds(100,200,150,30);
                saveChangesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(primaryKey.charAt(0) == 'C'){
                            String fullName ;
                            String cnic ;
                            String date_of_birth ;
                            String address ;
                            String phone ;
                            String email ;
                            String occupation ;
                            String dept_id ;

                            Object object = table.getValueAt(row,1);
                            if(object != null) {
                                 fullName = object.toString();
                                if(fullName.isBlank() || !fullName.matches("[a-zA-Z .]{1,30}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Name cannot be empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                return;
                            }

                            object =  table.getValueAt(row, 2) ;
                            if(object != null) {
                                 cnic = object.toString();
                                if(cnic.isBlank() || !cnic.matches("[0-9]{13}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Name cannot be empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                return;
                            }

                            String gender = table.getValueAt(row,3).toString() ;

                            object = table.getValueAt(row,4) ;
                            if(object != null){
                                 date_of_birth = object.toString() ;
                                if(date_of_birth.isBlank() || date_of_birth.equals("e.g:- YYYY-MM-DD")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                    return;
                                }else {
                                    try {
                                        LocalDate dob = LocalDate.parse(date_of_birth);  // Validates format too
                                        if (dob.isAfter(LocalDate.now())) {
                                            JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                            return;
                                        }
                                    }catch (DateTimeParseException ex){
                                        JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                        return;
                                    }
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Name cannot be empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                return;
                            }

                            object = table.getValueAt(row,5) ;
                            if(object != null){
                                 address = object.toString() ;
                                if(address.isBlank() || address.length() > 60){
                                    JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                    return;
                                }
                            }else{
                                 address = null ;
                            }

                            object = table.getValueAt(row,6) ;
                            if(object != null) {
                                 phone = object.toString();
                                if(!phone.matches("^[0-9]{11}$") || phone.equals("0##########")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Name cannot be empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                return;
                            }

                            object = table.getValueAt(row,7) ;
                            if(object != null) {
                                 email = object.toString();
                                if(!email.equals("e.g:- username@gmail.com") &&
                                        !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,50}$")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                    return;
                                }
                            }else{
                                 email = null ;
                            }

                            object = table.getValueAt(row, 8);
                            if(object != null) {
                                 occupation = object.toString();
                                if(occupation.isBlank() || !occupation.matches("[a-zA-Z .-]{2,30}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Input. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Name cannot be empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE) ;
                                return;
                            }

                            String marital_status = table.getValueAt(row,9).toString() ;

                            object = table.getValueAt(row, 10) ;
                            if(object != null) {
                                 dept_id =object.toString();
                                if (!dept_id.isBlank()) {
                                    Connection connection = JDBCConnector.getConnection(frame);
                                    if(connection != null) {
                                        boolean deptExists = JDBCConnector.search(
                                                connection,
                                                "SELECT dept_id FROM department WHERE dept_id = ?",
                                                dept_id,
                                                frame
                                        );
                                        if (!deptExists) {
                                            JOptionPane.showMessageDialog(frame, "Invalid Input. Please Try Again!", "Update Error", JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
                                    }
                                }
                            }else{
                                 dept_id = null ;
                            }

                          Citizen citizen = new Citizen(fullName,primaryKey,cnic,gender,date_of_birth,address,phone,email,occupation,marital_status,dept_id) ;
                           int checkUpdate = JDBCConnector.updateTable(frame,1,citizen) ;
                           if(checkUpdate > 0){
                               JOptionPane.showMessageDialog(frame,"Citizen updated successfully!");
                               JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Update",primaryKey);
                               saveChangesButton.setVisible(false);
                           }else{
                               JOptionPane.showMessageDialog(frame,"Citizen failed to update!");
                               return;
                           }


                        }else if(primaryKey.charAt(0) == 'V'){
                            String plate_number ;
                            String vehicle_model ;
                            String manufacturer ;
                            String manuf_year ;
                            String color ;
                            String vehicle_type ;
                            String regis_date ;
                            String citizen_id ;
                            String dept_id ;

                            Object object = table.getValueAt(row,1) ;
                            if(object != null){
                                plate_number = object.toString() ;
                                if(plate_number.isBlank() || ! plate_number.matches("[A-Z0-9 -]{1,11}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Plate Number. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Plate Number Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,2) ;
                            if(object != null){
                                vehicle_model = object.toString() ;
                                if(vehicle_model.isBlank() || !vehicle_model.matches("[A-Za-z0-9 -]{3,30}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Vehicle Model. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Vehicle Model Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return ;
                            }

                            object = table.getValueAt(row,3) ;
                            if(object != null){
                                manufacturer = object.toString() ;
                                if(manufacturer.isBlank() || !(manufacturer.matches("^[A-Za-z\\s]{2,30}$"))){
                                    JOptionPane.showMessageDialog(frame,"Invalid Manufacturer Name. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Manufacturer Name Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,4) ;
                            if(object != null){
                                manuf_year = object.toString() ;
                                int yearManfac = 0 ;
                                try {
                                    int year = Integer.parseInt(manuf_year.trim());
                                    yearManfac = year ;
                                    int currentYear = LocalDate.now().getYear();
                                    if (year > currentYear || year < 1947) {
                                        JOptionPane.showMessageDialog(frame,"Invalid Manufacturing Year. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                } catch (NumberFormatException eyear) {
                                    JOptionPane.showMessageDialog(frame,"Invalid Manufacturing Year. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Manufacturing Year Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,5) ;
                            if(object != null){
                                color = object.toString() ;
                                if(color.isBlank() || !color.matches("[a-zA-Z ]{3,40}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Vehicle Color. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Vehicle Color Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            vehicle_type = table.getValueAt(row,6).toString() ;

                            object = table.getValueAt(row,7) ;
                            if(object != null){
                                regis_date = object.toString() ;
                                try {
                                    LocalDate regDate = LocalDate.parse(regis_date.trim());
                                    int year = regDate.getYear();
                                    int currentYear = LocalDate.now().getYear();

                                    if (year > currentYear || year < 1947) {
                                        JOptionPane.showMessageDialog(frame,"Invalid Registration Date. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                } catch (DateTimeParseException rege) {
                                    JOptionPane.showMessageDialog(frame,"Invalid Registration Date. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Registration Date Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,8) ;
                            if(object != null){
                                citizen_id = object.toString() ;
                            }else{
                                citizen_id = null ;
                            }

                            object = table.getValueAt(row,9) ;
                            if(object != null){
                                dept_id = object.toString() ;
                            }else{
                                dept_id = null ;
                            }
                            Connection connection = JDBCConnector.getConnection(frame);
                            if(connection != null) {
                                if ((dept_id == null || dept_id.isBlank()) && (citizen_id == null || citizen_id.isBlank())) {
                                    JOptionPane.showMessageDialog(frame, "Both Citizen ID and Department ID cannot be Empty. Please Try Again!");
                                    return;
                                } else if ((dept_id != null && !dept_id.isBlank()) && (citizen_id != null && !citizen_id.isBlank())) {
                                    JOptionPane.showMessageDialog(frame, "Both Citizen ID and Department ID cannot be Inserted. Please Try Again!");
                                    return;
                                }else if ((citizen_id != null && !citizen_id.isBlank()) && (dept_id == null || citizen_id.isBlank())) {
                                    boolean citizenExists = JDBCConnector.search(
                                            connection,
                                            "SELECT citizen_id FROM vehicles WHERE citizen_id = ?",
                                            citizen_id,
                                            frame
                                    );
                                    if (!citizenExists) {
                                        JOptionPane.showMessageDialog(frame, "Invalid Citizen ID. Please Try Again!");
                                        return;
                                    }
                                } else if ((dept_id != null && !dept_id.isBlank()) && (citizen_id == null || citizen_id.isBlank())) {
                                    boolean deptExists = JDBCConnector.search(connection,"select dept_id from vehicles where dept_id = ?",dept_id,frame) ;
                                    if(!deptExists){
                                        JOptionPane.showMessageDialog(frame, "Invalid Department ID. Please Try Again!");
                                        return;
                                    }
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Invalid Database Connection","Database Error",JOptionPane.ERROR_MESSAGE);
                                return ;
                            }

                            Vehicle vehicle = new Vehicle(primaryKey,plate_number,citizen_id,vehicle_type,vehicle_model,manufacturer,manuf_year,color,regis_date,dept_id) ;
                            int checkUpdate = JDBCConnector.updateTable(frame,2,vehicle) ;
                            if(checkUpdate > 0){
                                JOptionPane.showMessageDialog(frame,"Vehicle updated successfully!");
                                JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Update",primaryKey);
                                saveChangesButton.setVisible(false);
                            }else{
                                JOptionPane.showMessageDialog(frame,"Vehicle failed to update!");
                                return;
                            }

                        }else if(primaryKey.charAt(0) == 'B'){
                            String buildingName ;
                            String buildingAddress ;
                            String buildingType = table.getValueAt(row,3).toString() ;
                            String buildingFloor = table.getValueAt(row,4).toString();
                            String citizen_id ;
                            String dept_id ;

                            Object object = table.getValueAt(row,1);
                            if(object != null){
                                buildingName = object.toString() ;
                                if(buildingName.isBlank() || !buildingName.matches("[a-zA-Z0-9 .'-]{2,40}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Building Name. Please Try Again!");
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Building Name Cannot be Empty. Please Try Again!");
                                return;
                            }

                            object = table.getValueAt(row,2) ;
                            if(object != null){
                                buildingAddress = object.toString() ;
                                if(buildingAddress.isBlank() || !buildingAddress.matches("[a-zA-Z0-9\\s,./#'()-]{2,50}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Building Address. Please Try Again!");
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Building Address Cannot be Empty. Please Try Again!");
                                return;
                            }

                            object = table.getValueAt(row,5) ;
                            if(object != null){
                                citizen_id = object.toString() ;
                            }else{
                                citizen_id = null ;
                            }

                            object = table.getValueAt(row,6) ;
                            if(object != null){
                                dept_id = object.toString() ;
                            }else{
                                dept_id = null ;
                            }
                            Connection connection = JDBCConnector.getConnection(frame);
                            if(connection != null) {
                                if ((dept_id == null || dept_id.isBlank()) && (citizen_id == null || citizen_id.isBlank())) {
                                    JOptionPane.showMessageDialog(frame, "Both Citizen ID and Department ID cannot be Empty. Please Try Again!");
                                    return;
                                } else if ((dept_id != null && !dept_id.isBlank()) && (citizen_id != null && !citizen_id.isBlank())) {
                                    JOptionPane.showMessageDialog(frame, "Both Citizen ID and Department ID cannot be Inserted. Please Try Again!");
                                    return;
                                }else if ((citizen_id != null && !citizen_id.isBlank()) && (dept_id == null || citizen_id.isBlank())) {
                                    boolean citizenExists = JDBCConnector.search(
                                            connection,
                                            "SELECT citizen_id FROM vehicles WHERE citizen_id = ?",
                                            citizen_id,
                                            frame
                                    );
                                    if (!citizenExists) {
                                        JOptionPane.showMessageDialog(frame, "Invalid Citizen ID. Please Try Again!");
                                        return;
                                    }
                                } else if ((dept_id != null && !dept_id.isBlank()) && (citizen_id == null || citizen_id.isBlank())) {
                                    boolean deptExists = JDBCConnector.search(connection,"select dept_id from vehicles where dept_id = ?",dept_id,frame) ;
                                    if(!deptExists){
                                        JOptionPane.showMessageDialog(frame, "Invalid Department ID. Please Try Again!");
                                        return;
                                    }
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Invalid Database Connection","Database Error",JOptionPane.ERROR_MESSAGE);
                                return ;
                            }

                            Building building = new Building(buildingName,primaryKey,buildingAddress,buildingType,buildingFloor,citizen_id,dept_id) ;
                            int checkUpdate = JDBCConnector.updateTable(frame,3,building) ;
                            if(checkUpdate > 0){
                                JOptionPane.showMessageDialog(frame,"Building updated successfully!");
                                JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Update",primaryKey);
                                saveChangesButton.setVisible(false);
                            }else{
                                JOptionPane.showMessageDialog(frame,"Building failed to update!");
                                return;
                            }

                        }else if(primaryKey.charAt(0) == 'D'){
                            String deptName ;
                            String contact ;
                            String email ;
                            String service ;
                            String building_id ;

                            Object object = table.getValueAt(row,1) ;
                            if(object != null){
                                deptName = object.toString() ;
                                if(deptName.isBlank() || !deptName.matches("[a-zA-Z .-]{2,40}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Department Name. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Department Name Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,2) ;
                            if(object != null){
                                contact = object.toString() ;
                                if(contact.isBlank() || !contact.matches("0[0-9]{9,10}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Department Name. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Department Contact Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,3) ;
                            if(object != null){
                                email = object.toString() ;
                                if(email.isBlank() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Department Email. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Department Email Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,4) ;
                            if(object != null){
                                service = object.toString() ;
                                if(service.isBlank() || !service.matches("[a-zA-Z .,-]{4,40}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Department Service. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Department Service Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,5) ;
                            if(object != null){
                                building_id = object.toString() ;
                            }
                            else{
                                building_id = null ;
                            }

                            if(building_id != null && !building_id.isBlank()){
                                Connection connection = JDBCConnector.getConnection(frame) ;
                                if(connection != null) {
                                    boolean buildingIDExists = JDBCConnector.search(connection, "select * from department where building_id = ?", building_id, frame);
                                    if (!buildingIDExists) {
                                        JOptionPane.showMessageDialog(frame, "Invalid Department Building ID. Please Try Again!", "Update Error", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(frame,"Invalid Database Connection!","Database Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Department Building ID Cannot be Empty. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            Department department = new Department(deptName,primaryKey,contact,email,service,building_id) ;
                            int checkUpdate = JDBCConnector.updateTable(frame,4,department) ;
                            if(checkUpdate > 0){
                                JOptionPane.showMessageDialog(frame,"Department updated successfully!");
                                JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Update",primaryKey);
                                saveChangesButton.setVisible(false);
                            }else{
                                JOptionPane.showMessageDialog(frame,"Department failed to update!");
                                return;
                            }
                        }else if(primaryKey.charAt(0) == 'A'){
                            String admin_role ;

                            Object object = table.getValueAt(row,2) ;
                            if(object != null){
                                admin_role = object.toString() ;
                                if(admin_role.equals("super_admin") && !SmartCity.adminRole.equals("super_admin") ){
                                    JOptionPane.showMessageDialog(frame,"You cannot Edit Super Admin");
                                    return;
                                }
                            }else{
                                admin_role = null ;
                            }

                            String admin_name ;
                            String admin_username ;
                            String admin_password ;
                            String admin_Citizen_id ;

                            object = table.getValueAt(row,1) ;
                            if(object != null){
                                admin_name = object.toString() ;
                                if(admin_name.isBlank() || !admin_name.matches("[A-Za-z .]{1,30}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Admin Name. Please Try Again!"
                                            ,"Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Please Insert Admin Name","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,3) ;
                            if(object != null){
                                admin_username = object.toString() ;
                                if(admin_username.isBlank() || !admin_username.matches("[a-zA-Z .]{1,30}")){
                                    JOptionPane.showMessageDialog(frame,"Invalid Admin Username. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Please Insert Admin Username!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,4) ;
                            if(object != null){
                                admin_password = object.toString() ;
                                boolean checkPassword = admin_password.length() == 8 &&
                                        admin_password.replaceAll("[^0-9]", "").length() >= 2 &&
                                        admin_password.replaceAll("[^a-zA-Z]", "").length() >= 2 &&
                                        admin_password.replaceAll("[^@#$&^*]", "").length() >= 2 &&
                                        admin_password.replaceAll("[^0-9a-zA-Z@#$&^*]", "").length() == 8;
                                if(admin_password.isBlank() || !checkPassword){
                                    JOptionPane.showMessageDialog(frame,"Invalid Admin Password. Please Try Again!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame,"Please enter Admin Password!","Update Error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            object = table.getValueAt(row,5) ;
                            if(object != null){
                                admin_Citizen_id = object.toString() ;
                                if(admin_Citizen_id.isBlank() || !admin_Citizen_id.matches("^C[0-9]{7}")){
                                    JOptionPane.showMessageDialog(frame,"Please enter Admin Password!","Update Error",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }else{
                                admin_Citizen_id = null ;
                            }

                            Admin admin = new Admin(primaryKey,admin_name,admin_username,admin_role,admin_password,admin_Citizen_id) ;
                            int adminUpdate = JDBCConnector.updateTable(frame,5,admin) ;
                            if(adminUpdate > 0){
                                JOptionPane.showMessageDialog(frame,"Admin updated successfully!");
                                JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Update",primaryKey);
                                saveChangesButton.setVisible(false);
                            }else{
                                JOptionPane.showMessageDialog(frame,"Failed to update Admin!");
                                return;
                            }
                        }
                    }
                });
                frame.add(saveChangesButton);
                frame.revalidate();
                frame.repaint();

            } else if ("Delete".equals(action)) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION){
                    char tablePrimaryKey1stLetter = primaryKey.charAt(0) ;
                    if(tablePrimaryKey1stLetter == 'C') {
                        int deleted = JDBCConnector.deleteTable(frame,1,primaryKey);
                        if (deleted > 0) {
                            ((DefaultTableModel) table.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(frame, "Citizen deleted successfully!");
                            JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Delete",primaryKey);
                        }else{
                            JOptionPane.showMessageDialog(frame, "Failed to Delete Citizen!","Delete Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }else if(tablePrimaryKey1stLetter == 'V'){
                        int deleted = JDBCConnector.deleteTable(frame,2,primaryKey);
                        if (deleted > 0) {
                            ((DefaultTableModel) table.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(frame, "Vehicle deleted successfully!");
                            JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Delete",primaryKey);
                        }else{
                            JOptionPane.showMessageDialog(frame, "Failed to Delete Vehicle!","Delete Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }else if(tablePrimaryKey1stLetter == 'B'){
                        int deleted = JDBCConnector.deleteTable(frame,3,primaryKey);
                        if (deleted > 0){
                            ((DefaultTableModel) table.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(frame, "Building deleted successfully!");
                            JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Delete",primaryKey);
                        }else{
                            JOptionPane.showMessageDialog(frame, "Failed to Delete Building!","Delete Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }else if(tablePrimaryKey1stLetter == 'D'){
                        int deleted = JDBCConnector.deleteTable(frame,4,primaryKey);
                        if (deleted > 0) {
                            ((DefaultTableModel) table.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(frame, "Department deleted successfully!");
                            JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Delete",primaryKey);
                        }else{
                            JOptionPane.showMessageDialog(frame, "Failed to Delete Department!","Delete Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }else if(tablePrimaryKey1stLetter == 'A'){
                        String admin_role = table.getValueAt(row,2).toString() ;
                        if(!SmartCity.adminRole.equals("super_admin") && admin_role.equals("super_admin")) {
                            JOptionPane.showMessageDialog(frame,"You cannot delete Super Admin!");
                        }else {
                            int deleted = JDBCConnector.deleteTable(frame, 5, primaryKey);
                            if (deleted > 0) {
                                ((DefaultTableModel) table.getModel()).removeRow(row);
                                JOptionPane.showMessageDialog(frame, "Admin deleted successfully!");
                                JDBCConnector.insertAdminHistory(frame,SmartCity.adminID,"Delete",primaryKey);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Failed to Admin Record!", "Delete Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(frame,"Discarded to Delete Record!");
                }
            }
        }
        clicked = false;
        return label;
    }

    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
