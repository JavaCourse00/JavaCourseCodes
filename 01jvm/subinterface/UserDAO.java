package subinterface;

public class UserDAO implements UserMapper {

    @Override
    public void insert(User user) {
        System.out.println("Insert user id: " + user.getId());
    }
}
