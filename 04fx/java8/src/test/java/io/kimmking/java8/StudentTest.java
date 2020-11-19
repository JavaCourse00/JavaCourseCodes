package io.kimmking.java8;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class StudentTest {
    
    private Student class1;
    
    @Test
    public void KlassTest(){
        class1 = Mockito.mock(Student.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(class1.getApplicationContext().getId()).thenReturn("10");
        Assert.assertEquals("10", (class1.getApplicationContext().getId()));
    }
    // 单元测试
}
