package Sale.LyodsProcess;

import javax.swing.JDialog;

import jcx.jform.bproc;

public class CloseDialog2 extends bproc {
  public String getDefaultValue(String value) throws Throwable {

    JDialog jd = (JDialog) get("JD2");
    System.out.println(">>>JD22:" + jd);
    jd.setVisible(false);

    return value;
  }

  public String getInformation() {
    return "---------------CloseDialog2(CloseDialog).defaultValue()----------------";
  }
}
