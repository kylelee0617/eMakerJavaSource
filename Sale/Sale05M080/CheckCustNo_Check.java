package Sale.Sale05M080;

import javax.swing.JTable;
import org.apache.commons.lang.StringUtils;
import jcx.db.talk;
import jcx.jform.bvalidate;

public class CheckCustNo_Check extends bvalidate{
  public boolean check(String value)throws Throwable{
   
   put("payment_caller", "check");
   put("payment_custNo", value);
   getButton("BtChkCustNo").doClick();
   
     return true;
  }
  public String getInformation(){
    return "---------------null(null).DeputyID.field_check()----------------";
  }
}
