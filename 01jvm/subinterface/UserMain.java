package subinterface;

public class UserMain {

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        dao.insert(new User(123));
    }

}
