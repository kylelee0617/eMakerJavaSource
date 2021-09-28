package Sale.test;

import javax.swing.*;

public class Test2 extends jcx.jform.bproc {
  public String getDefaultValue(String value) throws Throwable {

    JDialog jd = showDialog("LyodsProcess", "", false, false, 500, 80, 1300, 400);
    System.out.println(">>>JD21:" + jd);
    put("JD2", jd);
    
    

    return value;
  }

  public String getInformation() {
    return "---------------Test2(Test2).defaultValue()----------------";
  }
}
