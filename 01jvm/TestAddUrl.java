
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class TestAddUrl {

    public static void main(String[] args) throws Exception {
        URLClassLoader classLoader = (URLClassLoader) TestAddUrl.class.getClassLoader();
        String dir = "/Users/kimmking/Downloads/Hello";
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(classLoader, new File(dir).toURL());

        Class klass = Class.forName("Hello",true, classLoader);
        Object obj = klass.newInstance();
        Method hello = klass.getDeclaredMethod("hello");
        hello.invoke(obj);
    }

}
