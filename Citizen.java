class Citizen extends NameID {

    private String CNIC;
    private String gender;
    private String dob;
    private String address;
    private String phone;
    private String email;
    private String occupation;
    private String maritalStatus;
    private String[] vehID = new String[3];
    private String deptID ;
    private String[] buildingID = new String[5];
    private int nOfVehicles = 0;
    private int nOfBuilding = 0;

    Citizen(){

    }

    Citizen(String name, String id, String CNIC, String gender, String dob, String address,
            String phone, String email, String occupation, String maritalStatus,
            String deptID) {
        super(name, id);
        this.CNIC = CNIC;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.occupation = occupation;
        this.maritalStatus = maritalStatus;
        this.deptID = deptID;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDOB(String dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setOccupation(String occupation){
        this.occupation = occupation ;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getCNIC() {
        return CNIC;
    }

    public String getGender() {
        return gender;
    }

    public String getDOB() {
        return dob;
    }

    public String getOccupation(){
        return occupation ;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String[] getVehID() {
        return vehID;
    }

    public String getDeptID() {
        return deptID;
    }

    public int nOfVehicles() {
        return nOfVehicles;
    }

    public void addVehicle(String vehicleID) {
        vehID[nOfVehicles++] = vehicleID;
    }

    public int nOfBuilding() {
        return nOfBuilding;
    }

    public void addBuilding(String buildID) {
        buildingID[nOfBuilding++] = buildID;
    }

    public String[] getBuildingID(){
        return buildingID ;
    }
}
