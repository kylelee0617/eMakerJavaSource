package Sale.LyodsProcess;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRootPane;

import jcx.jform.bproc;

public class CloseDialog extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    String name = getName();
    
    name = getName();
    System.out.println(">>>getName:" + name);
    
    JButton bt = getButton(name);
    System.out.println(">>>bt:" + bt);
    
    JRootPane pane = bt.getRootPane();
    System.out.println(">>>pane:" + pane);
    
    JDialog Con = (JDialog)pane.getParent();
    System.out.println(">>>Con:" + Con);

    JDialog jd = (JDialog) getButton(name).getRootPane().getParent();
    jd.setVisible(false);

    return value;
  }

  public String getInformation() {
    return "---------------CloseDialog(CloseDialog).defaultValue()----------------";
  }
}
