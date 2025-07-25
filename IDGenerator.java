import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class IDGenerator{

    private static int buildingCounter = 1;
    private static int citizenCounter = 1;
    private static int departmentCounter = 1;
    private static int vehicleCounter = 1;
    private static int adminCounter = 1 ;

    public static String generateAdminID(Connection connection,JFrame frame){
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT admin_id from admin order by admin_id desc limit 1") ;
            ResultSet result = ps.executeQuery() ;
            if(result.next()){
                String lastID = result.getString("admin_id") ;
                int lastNum = Integer.parseInt(lastID.substring(1)) ;
                adminCounter = lastNum + 1 ;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return null ;
        }
        return "A" + String.format("%07d",adminCounter++) ;
    }

    public static String generateBuildingID(Connection connection,JFrame frame){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT building_id FROM building ORDER BY building_id DESC LIMIT 1");
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                String lastID = result.getString("building_id"); // e.g., "B0000005"
                int lastNum = Integer.parseInt(lastID.substring(1)); // Extract numeric part
                buildingCounter = lastNum + 1;
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return null ;
        }
        return "B" + String.format("%07d", buildingCounter++);
    }

    public static String generateCitizenID(Connection connection,JFrame frame) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT citizen_id FROM citizen ORDER BY citizen_id DESC LIMIT 1");
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                String lastID = result.getString("citizen_id");
                int lastNum = Integer.parseInt(lastID.substring(1));
                citizenCounter = lastNum + 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return null ;
        }
        return "C" + String.format("%07d", citizenCounter++);
    }


    public static String generateDepartmentID(Connection connection,JFrame frame) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT dept_id FROM department ORDER BY dept_id DESC LIMIT 1");
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                String lastID = result.getString("dept_id");
                int lastNum = Integer.parseInt(lastID.substring(1));
                departmentCounter = lastNum + 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return null ;
        }
        return "D" + String.format("%07d", departmentCounter++);
    }


    public static String generateVehicleID(Connection connection,JFrame frame) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT vehicle_id FROM vehicles ORDER BY vehicle_id DESC LIMIT 1");
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                String lastID = result.getString("vehicle_id");
                int lastNum = Integer.parseInt(lastID.substring(1));
                vehicleCounter = lastNum + 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
            return null ;
        }
        return "V" + String.format("%07d", vehicleCounter++);
    }
}
