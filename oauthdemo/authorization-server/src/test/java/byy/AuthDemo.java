package byy;

import com.byy.AuthorizationApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = {AuthorizationApplication.class})
@RunWith(SpringRunner.class)
public class AuthDemo {
    @Autowired
    ApplicationContext applicationContext;




    @Test
    public void demo1(){
        KeyProperties bean = applicationContext.getBean(KeyProperties.class);
        System.out.println(bean);
    }
}
