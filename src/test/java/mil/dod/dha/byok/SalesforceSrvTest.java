package mil.dod.dha.byok;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import mil.dod.dha.byok.service.*;
import java.util.*;
@SpringBootTest
public class SalesforceSrvTest{
@Autowired
SalesforceSrv salesforcesrv;
 @Test
  public  void Test1() throws Exception{
	salesforcesrv.test1();
    }

}


