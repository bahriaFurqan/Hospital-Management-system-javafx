package task.lab_project_oop;

public class  Admin {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String userType;
    private String Conatact;
    private String Name;

    public Admin(Integer id, String username, String email, String password, String userType, String Contact, String Name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.Conatact = Conatact;
        this.Name = Name;
    }
    public Admin(String username, String email, String password, String userType) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;

    }
    public String getName() {
        return Name;
    }
    public String setName(String Name) {
        return this.Name = Name;
    }
    public Integer setId(Integer id) {
        return this.id = id;
    }
    public String setAddress(String address) {
        return this.Conatact = Conatact;
    }
    public String setUsername(String username) {
        return this.username = username;
    }
    public String setEmail(String email) {
        return this.email = email;
    }
    public String setPassword(String password) {
        return this.password = password;
    }


    public Integer getId() {
        return id;
    }
    public String getAddress() {
        return Conatact;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }
}