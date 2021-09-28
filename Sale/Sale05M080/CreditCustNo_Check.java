package Sale.Sale05M080;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import Farglory.util.TalkBean;
import jcx.db.talk;
import jcx.jform.bvalidate;

public class CreditCustNo_Check extends bvalidate {
  public boolean check(String value) throws Throwable {

   put("payment_caller", "credit");
   put("payment_custNo", value);
   getButton("BtChkCustNo").doClick();
  
    return true;
  }

  public String getInformation() {
    return "---------------null(null).DeputyID.field_check()----------------";
  }
}
