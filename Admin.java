public class Admin extends NameID{
    private String username ;
    private String role ;
    private String password ;
    private String citizen_id ;

    Admin(String id,String name,String username,String role,String password,String citizen_id){
        super(name,id); ;
        this.username = username ;
        this.role = role ;
        this.password = password ;
        this.citizen_id = citizen_id ;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole(){
        return role ;
    }

    public void setRole(String role){
        this.role = role ;
    }

    public String getPassword(){
        return password ;
    }

    public void  setPassword(String password){
        this.password = password ;
    }

    public String getCitizen_id(){
        return citizen_id ;
    }

    public void setCitizen_id(String citizen_id){
        this.citizen_id = citizen_id ;
    }
}
