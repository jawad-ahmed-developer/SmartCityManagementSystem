import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.CookieHandler;
import java.sql.* ;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class JDBCConnector {
    private final static String url = "jdbc:mysql://localhost:3306/smart_city" ;
    private final static String username = "root" ;
    private final static String password = "root" ;

    public static Connection getConnection(JFrame frame){
        try{
            Connection connection = DriverManager.getConnection(url,username,password) ;
            return connection ;
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(frame, e.getMessage(),
                    "Database Connection Error",JOptionPane.ERROR_MESSAGE);
        }
        return null ;
    }

    public static int insertCitizen(JFrame frame, String query,
                                    Connection connection, Citizen citizen) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, citizen.getID());
            ps.setString(2, citizen.getName());
            ps.setString(3, citizen.getCNIC());
            ps.setString(4, citizen.getGender());
            ps.setDate  (5, java.sql.Date.valueOf(citizen.getDOB()));
            ps.setString(6, citizen.getPhone());
            String email = citizen.getEmail() ;
            if((!email.isBlank() && !email.equals("e.g:- username@gmail.com"))){
                ps.setString(7, citizen.getEmail());
            }else{
                ps.setNull(7,java.sql.Types.VARCHAR);
            }
            ps.setString(8, citizen.getOccupation());
            ps.setString(9, citizen.getMaritalStatus());

            if (citizen.getDeptID() != null && !citizen.getDeptID().isBlank()) {
                ps.setString(10, citizen.getDeptID());
            } else {
                ps.setNull(10, java.sql.Types.VARCHAR);
            }

            if(citizen.getAddress() != null && !citizen.getAddress().isBlank()){
                ps.setString(11,citizen.getAddress()) ;
            }else{
                ps.setNull(11,java.sql.Types.VARCHAR);
            }

            return ps.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return 0;

        }
    }

    public static int insertVehicle(JFrame frame,Connection connection,String query,Vehicle vehicle){
        PreparedStatement ps = null ;
        try{
            ps = connection.prepareStatement(query) ;
            ps.setString(1,vehicle.getVehID());
            ps.setString(2,vehicle.getLiscencePlate());
            if (vehicle.getOwner() != null && !vehicle.getOwner().isBlank()) {
                ps.setString(3, vehicle.getOwner());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }

            ps.setString(4,vehicle.getModel());
            ps.setString(5,vehicle.getManufacturer());
            ps.setInt(6,Integer.parseInt(vehicle.getManfDate()));
            ps.setString(7,vehicle.getColor());
            if (vehicle.getDept_id() != null && !vehicle.getDept_id().isBlank()) {
                ps.setString(8, vehicle.getDept_id());
            } else {
                ps.setNull(8, java.sql.Types.VARCHAR);
            }

            LocalDate regDate = LocalDate.parse(vehicle.getRegisDate().trim());
            ps.setObject(9, regDate);
            ps.setString(10,vehicle.getVehType());
            return ps.executeUpdate() ;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(frame, e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        catch(DateTimeParseException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Date Time Error",JOptionPane.ERROR_MESSAGE);
            return 0 ;
        }
    }
    public static int insertBuidling(JFrame frame,Connection connection,String query,Building building){
        PreparedStatement ps = null ;
        try{
            ps = connection.prepareStatement(query) ;
            ps.setString(1,building.getID());
            ps.setString(2,building.getName());
            ps.setString(3,building.getAddress());
            ps.setString(4,building.getType());
            ps.setInt(5,Integer.parseInt(building.getFloors()));
            if(!building.getOwner().isBlank()) {
                ps.setString(6, building.getOwner());
            }
            else{
                ps.setNull(6,java.sql.Types.VARCHAR);
            }
            if(!building.getDeptID().isBlank()){
                ps.setString(7,building.getDeptID());
            }else{
                ps.setNull(7,java.sql.Types.VARCHAR);
            }
            return ps.executeUpdate() ;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return 0 ;
        }
    }

    public static int insertDepartment(JFrame frame,Connection connection,String query,Department department){
        PreparedStatement ps = null ;
        try{
            ps = connection.prepareStatement(query) ;
            ps.setString(1,department.getID());
            ps.setString(2,department.getName());
            ps.setString(3, department.getContact());
            ps.setString(4,department.getEmail());
            ps.setString(5,department.getService());
            if(!department.getBuildingsID().isBlank()){
                ps.setString(6, department.getBuildingsID());
            }else{
               ps.setNull(6,java.sql.Types.VARCHAR);
            }
            return ps.executeUpdate() ;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return 0 ;
        }
    }

    public static int updateTable(JFrame frame,int tableNumber,Object object){
        Connection connection = JDBCConnector.getConnection(frame) ;
        PreparedStatement ps = null ;
        try{
            switch (tableNumber) {
                case 1:
                    JOptionPane.showMessageDialog(frame,"Entered into the update citizen table");
                    Citizen citizen = (Citizen) object ;
                    ps = connection.prepareStatement("UPDATE citizen set fullName = ?,cnic = ?,gender = ?,date_of_birth = ?,address = ?,phone = ?,email = ?,occupation = ?,marital_status = ?,dept_id = ? where citizen_id = ?");
                    ps.setString(1,citizen.getName());
                    ps.setString(2,citizen.getCNIC());
                    ps.setString(3,citizen.getGender());
                    ps.setDate  (4, java.sql.Date.valueOf(citizen.getDOB()));
                    if(citizen.getAddress() != null && !citizen.getAddress().isBlank()) {
                        ps.setString(5, citizen.getAddress());
                    }else{
                        ps.setNull(5,java.sql.Types.VARCHAR);
                    }
                    ps.setString(6,citizen.getPhone());
                    if(citizen.getEmail() != null && !citizen.getEmail().isBlank()) {
                        ps.setString(7, citizen.getEmail());
                    }else{
                        ps.setNull(7,java.sql.Types.VARCHAR);
                    }
                    ps.setString(8,citizen.getOccupation());
                    ps.setString(9,citizen.getMaritalStatus());
                    if(citizen.getDeptID() != null && !citizen.getDeptID().isBlank()){
                        ps.setString(10,citizen.getDeptID());
                    }else{
                        ps.setNull(10,java.sql.Types.VARCHAR);
                    }
                    ps.setString(11,citizen.getID());
                    JOptionPane.showMessageDialog(frame,"How many rows updated: "+ ps.executeUpdate() );
                    return ps.executeUpdate() ;

                case 2:
                    Vehicle vehicle = (Vehicle) object ;
                    ps = connection.prepareStatement("UPDATE vehicles set plate_number = ?,citizen_id = ?,vehicle_model = ?,manufacturer = ?,manuf_year = ?,color = ?,dept_id = ?,regis_date = ?,vehicle_type = ? where vehicle_id = ?") ;
                    ps.setString(1,vehicle.getLiscencePlate()) ;
                    if(vehicle.getOwner() != null && !vehicle.getOwner().isBlank()) {
                        ps.setString(2, vehicle.getOwner());
                    }else{
                        ps.setNull(2,java.sql.Types.VARCHAR) ;
                    }
                    ps.setString(3,vehicle.getModel());
                    ps.setString(4, vehicle.getManufacturer()) ;
                    ps.setInt(5,Integer.parseInt(vehicle.getManfDate()));
                    ps.setString(6,vehicle.getColor());
                    if(vehicle.getDept_id() != null && !vehicle.getDept_id().isBlank()) {
                        ps.setString(7, vehicle.getDept_id());
                    }
                    else{
                        ps.setNull(7,java.sql.Types.VARCHAR) ;
                    }
                    ps.setString(8,vehicle.getRegisDate());
                    ps.setString(9,vehicle.getVehType());
                    ps.setString(10,vehicle.getVehID());
                    return ps.executeUpdate() ;
                case 3:
                    Building building = (Building) object ;
                    ps = connection.prepareStatement("Update building set bname = ?,badress = ?,btype = ?,bfloor = ?,citizen_id = ?,dept_id = ? where building_id = ?") ;
                    ps.setString(1,building.getName());
                    ps.setString(2,building.getAddress());
                    ps.setString(3,building.getType());
                    ps.setInt(4, Integer.parseInt(building.getFloors()));
                    if(!building.getOwner().isBlank()) {
                        ps.setString(5, building.getOwner());
                    }else {
                        ps.setNull(5,java.sql.Types.VARCHAR);
                    }
                    if(!building.getDeptID().isBlank()){
                        ps.setString(6, building.getDeptID());
                    }else{
                        ps.setNull(6,java.sql.Types.VARCHAR);
                    }
                    ps.setString(7,building.getID());
                    return ps.executeUpdate() ;
                case 4:
                    Department department = (Department) object ;
                    ps = connection.prepareStatement("Update department set dept_name = ?,contact = ?,email = ?,service = ?,building_id = ? where dept_id = ?") ;
                    ps.setString(1,department.getName());
                    ps.setString(2,department.getContact());
                    ps.setString(3,department.getEmail());
                    ps.setString(4,department.getService());
                    if(!department.getBuildingsID().isBlank()) {
                        ps.setString(5, department.getBuildingsID());
                    }else{
                        ps.setNull(6,java.sql.Types.VARCHAR);
                    }
                    ps.setString(6,department.getID());
                    return ps.executeUpdate() ;
                case 5:
                    Admin admin = (Admin)object ;
                    ps = connection.prepareStatement("Update admin set admin_name = ?,admin_role = ?,admin_username = ?,admin_password = ?,admin_citizen_id = ? where admin_id = ?") ;
                    ps.setString(1,admin.getName());
                    if(admin.getRole() != null) {
                        ps.setString(2, admin.getRole());
                    }else{
                        ps.setString(2,"Admin Role Updated");
                    }
                    ps.setString(3,admin.getUsername());
                    ps.setString(4,admin.getPassword());
                    if(admin.getCitizen_id() != null)
                        ps.setString(5,admin.getCitizen_id());
                    else
                        ps.setNull(5,java.sql.Types.VARCHAR);
                    ps.setString(6,admin.getID());
                    return ps.executeUpdate() ;
            }
        }catch (SQLException sx){
            JOptionPane.showMessageDialog(frame,sx.getMessage());
            return 0 ;
        }
        catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(frame,ex.getMessage());
            return  0 ;
        }
        return 0 ;
    }

    public static int deleteTable(JFrame frame,int tableNumber,String deleteByKeyField){
        PreparedStatement ps = null ;
        Connection connection = getConnection(frame) ;
        if(connection == null)
            return  0 ;
        try{
            switch (tableNumber){
                case 1:
                    ps = connection.prepareStatement("DELETE FROM citizen where citizen_id = ?") ;
                    ps.setString(1,deleteByKeyField);
                    return ps.executeUpdate() ;
                case 2:
                    ps = connection.prepareStatement("DELETE FROM vehicles where vehicle_id = ?") ;
                    ps.setString(1,deleteByKeyField);
                    return ps.executeUpdate() ;
                case 3:
                    ps = connection.prepareStatement("DELETE FROM building where building_id = ?") ;
                    ps.setString(1,deleteByKeyField);
                    return ps.executeUpdate() ;
                case 4:
                    ps = connection.prepareStatement("DELETE FROM department where dept_id = ?") ;
                    ps.setString(1,deleteByKeyField);
                    return  ps.executeUpdate() ;
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return 0 ;
        }
        return 0 ;
    }

    public static ResultSet searchResult(Connection connection,JFrame frame,String query,String fieldToCheck){
        PreparedStatement ps = null ;
        try{
            ps = connection.prepareStatement(query) ;
            ps.setString(1,fieldToCheck);
            return  ps.executeQuery() ;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return  null ;
        }
    }

    public static boolean search(Connection connection, String query, String anySingleField,JFrame frame){
        try {
            PreparedStatement prStatement = connection.prepareStatement(query);
            prStatement.setString(1, anySingleField);
            ResultSet result = prStatement.executeQuery();
            return result.next() ;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return false ;
        }
    }

    public static int insertAdmin(Connection connection,JFrame frame,Admin admin){
        PreparedStatement ps = null ;
        try{
            ps = connection.prepareStatement("insert into admin(admin_id,admin_name,admin_username,admin_role,admin_password,admin_citizen_id) values" +
                    "(?,?,?,?,?,?)") ;
            ps.setString(1,admin.getID());
            ps.setString(2,admin.getName());
            ps.setString(3,admin.getUsername());
            ps.setString(4,admin.getRole());
            ps.setString(5,admin.getPassword());
            if(admin.getCitizen_id() != null && !admin.getCitizen_id().isBlank() ){
                ps.setString(6, admin.getCitizen_id());
            }else{
                ps.setNull(6,java.sql.Types.VARCHAR);
            }
            return ps.executeUpdate() ;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return 0 ;
        }
    }

    public static String getFieldByTwoPlaceHolders(Connection connection,String query,JFrame frame,String placeHolderOne,String placeHolderTwo){
        try{
            PreparedStatement ps = connection.prepareStatement(query) ;
            ps.setString(1,placeHolderOne);
            ps.setString(2,placeHolderTwo);
            ResultSet result = ps.executeQuery() ;
            if(result.next()){
                return result.getString(1) ;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }
        return null ;
    }

    public static void insertAdminHistory(JFrame frame,String admin_id,String action,String performed_on){
        PreparedStatement ps = null ;
        try{
            Connection connection = getConnection(frame) ;
            if(connection != null) {
                ps = connection.prepareStatement("insert into admin_history(admin_id,action,performed_on) values (?,?,?)");
                ps.setString(1, admin_id);
                ps.setString(2, action);
                ps.setString(3, performed_on);
                if (ps.executeUpdate() <= 0) {
                    JOptionPane.showMessageDialog(frame, "Failed to add History of an event");
                }
            }else{
                JOptionPane.showMessageDialog(frame,"Connection Failed!","Database Error",JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}


