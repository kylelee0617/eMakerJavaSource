package Sale.Sale05M090;

import jcx.jform.bNotify;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

public class FuncAfterDelete extends bNotify {
  public void actionPerformed(String value) throws Throwable {
    
    //µù¾PÃöÁp¤H
    getButton("RenewRelated").doClick();

    return;
  }

  public String getInformation() {
    return "---------------delete_trigger()----------------";
  }
}
