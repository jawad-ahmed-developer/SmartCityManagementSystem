class Vehicle {
    private String liscencePlate;
    private String vehID;
    private String owner; // Can be Citizen ID or Department ID
    private String vehType;
    private String model;
    private String manufacturer;
    private String manfDate;
    private String color;
    private String regisDate;
    private String dept_id ;

    // Default Constructor
    public Vehicle() {
    }

    // Parameterized Constructor
    public Vehicle(String vehID, String liscencePlate, String owner, String vehType,
                   String model, String manufacturer, String manfDate, String color, String regisDate,String dept_id) {
        this.vehID = vehID;
        this.liscencePlate = liscencePlate;
        this.owner = owner;
        this.vehType = vehType;
        this.model = model;
        this.manufacturer = manufacturer;
        this.manfDate = manfDate;
        this.color = color;
        this.regisDate = regisDate;
        this.dept_id = dept_id ;
    }

    public String getDept_id(){
        return dept_id ;
    }

    public void setDept_id(String dept_id){
        this.dept_id = dept_id ;
    }

    // Getters and Setters
    public String getVehID() {
        return vehID;
    }

    public void setVehID(String vehID) {
        this.vehID = vehID;
    }

    public String getLiscencePlate() {
        return liscencePlate;
    }

    public void setLiscencePlate(String liscencePlate) {
        this.liscencePlate = liscencePlate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVehType() {
        return vehType;
    }

    public void setVehType(String vehType) {
        this.vehType = vehType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManfDate() {
        return manfDate;
    }

    public void setManfDate(String manfDate) {
        this.manfDate = manfDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRegisDate() {
        return regisDate;
    }

    public void setRegisDate(String regisDate) {
        this.regisDate = regisDate;
    }
}
