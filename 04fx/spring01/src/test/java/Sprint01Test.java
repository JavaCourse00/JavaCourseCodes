import io.kimmking.spring02.Klass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class Sprint01Test {
    
    @Autowired
    private Klass class1;
    
    @Test
    public void KlassTest(){
        Assert.assertEquals(2, class1.getStudents().size());
    }
    
    // 集成测试
}
