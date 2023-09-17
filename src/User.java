import java.util.ArrayList;

public class User {
    public ArrayList<Urun> choicesList;
    private String userName;
    private String password;
    private String ccNo;

    public User(String userName, String password){

        choicesList = new ArrayList<>();
        this.userName = userName;
        this.password = password;

    }
    public String getUserName() {
        return userName;
    }
    public ArrayList<Urun> getChoicesList() {
        return choicesList;
    }
}
