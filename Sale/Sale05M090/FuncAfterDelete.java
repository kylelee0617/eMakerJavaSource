package Sale.Sale05M090;

import jcx.jform.bNotify;

public class FuncAfterDelete extends bNotify {
  public void actionPerformed(String value) throws Throwable {
    getButton("RenewRelated").doClick();

    return;
  }

  public String getInformation() {
    return "---------------delete_trigger()----------------";
  }
}
