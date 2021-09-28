package Sale.LyodsProcess;

import javax.swing.JDialog;

import jcx.jform.bproc;

public class CloseDialog2 extends bproc {
  public String getDefaultValue(String value) throws Throwable {

    JDialog jd = (JDialog) get("JD");
    System.out.println(">>>JD2:" + jd);
    jd.setVisible(false);
    
    jd.disable();

    return value;
  }

  public String getInformation() {
    return "---------------CloseDialog2(CloseDialog).defaultValue()----------------";
  }
}
