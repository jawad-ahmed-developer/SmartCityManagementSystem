class Department extends NameID {
    private String contact;
    private String email;
    private String service;

    private String[] citizensID = new String[100];
    private int nOfCitizen = 0;

    private String[] vehiclesID = new String[10];
    private int nOfVehicles = 0;

    private String buildingsID ;

    // Default constructor
    public Department() {
    }

    // Parameterized constructor
    public Department(String name, String id, String contact, String email, String service,String buildingsID) {
        super(name, id);
        this.contact = contact;
        this.email = email;
        this.service = service;
        this.buildingsID = buildingsID ;
    }

    public void setBuildingsID(String buildingsID){
        this.buildingsID = buildingsID ;
    }

    public String getBuildingsID(){
        return buildingsID ;
    }

    // Add methods
    public void addCitizen(String citizenID) {
        if (nOfCitizen < citizensID.length) {
            citizensID[nOfCitizen++] = citizenID;
        }
    }

    public void addVehicle(String vehicleID) {
        if (nOfVehicles < vehiclesID.length) {
            vehiclesID[nOfVehicles++] = vehicleID;
        }
    }

    public int getNumberOfCitizens() {
        return nOfCitizen;
    }

    public int getNumberOfVehicles() {
        return nOfVehicles;
    }

    // Standard Getters and Setters
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
