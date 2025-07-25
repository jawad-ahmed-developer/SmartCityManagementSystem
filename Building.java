class Building extends NameID {
    private String address;
    private String type;
    private String floors;
    private String owner;
    private String deptID;

    // Parameterized constructor
    public Building(String name, String id, String address, String type, String floors, String owner,String deptID) {
        super(name, id); // Calling constructor of NameID
        this.address = address;
        this.type = type;
        this.floors = floors;
        this.owner = owner;
        this.deptID = deptID ;
    }

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFloors() {
        return floors;
    }

    public void setFloors(String floors) {
        this.floors = floors;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getDeptID() {
        return deptID;
    }
}
