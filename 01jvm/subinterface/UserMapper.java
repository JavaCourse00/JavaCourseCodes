package subinterface;

public interface UserMapper extends Mapper<User> {

    @Override
    void insert(User user);
}
