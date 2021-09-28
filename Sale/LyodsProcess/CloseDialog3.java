package Sale.LyodsProcess;

import javax.swing.JFrame;

import jcx.jform.bproc;

public class CloseDialog3 extends bproc {
  public String getDefaultValue(String value) throws Throwable {

    JFrame jd = (JFrame) get("JD3");
    System.out.println(">>>JD32:" + jd);
    jd.setVisible(false);
	
	

    return value;
  }

  public String getInformation() {
    return "---------------CloseDialog2(CloseDialog).defaultValue()----------------";
  }
}
