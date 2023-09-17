public class Admin extends User {
    String authority;
    public Admin(String userName, String password, String authority) {
        super(userName,password);
        this.authority = authority;
    }
}
