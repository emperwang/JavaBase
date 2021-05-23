package mock;

import com.wk.mock.Weather;
import com.wk.mock.Work;
import javafx.beans.value.WritableValue;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author: Sparks
 * @Date: 2021/5/23 9:22
 * @Description
 */
public class MockTest {

    @InjectMocks
    private Work work;

    @Mock
    private Weather weather;

    @BeforeClass
    public void init(){
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testcase1(){
        Mockito.when(weather.getWeather()).thenReturn("晴");
        int workTime = work.getWorkTime();
        Assert.assertEquals(workTime,6);
    }

}
