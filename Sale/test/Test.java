package Sale.test;
import org.apache.commons.lang.StringUtils;

import Farglory.util.KUtils;
import jcx.jform.sproc;
import jcx.util.datetime;

public class Test extends sproc{
  public String getDefaultValue(String value)throws Throwable{
    
    String stringEDate = "2022/01/09"; // 祇布ら
    String stringDateTime = datetime.getTime("YYYY/mm/dd h:m:s"); // 祇布秨ミ ╰参丁
    
    KUtils util = new KUtils();
    String invoiceOpenDate = stringDateTime.split(" ")[0].trim(); // ち祇布秨ミら戳
    
    KUtils.info(" -2 date : " + util.getDateAfterNDays(invoiceOpenDate, "/", -2));

    if (StringUtils.equals(stringEDate, util.getDateAfterNDays(invoiceOpenDate, "/", -2))) {
     
      KUtils.info("true");

    }
    
    
    return value;
  }
  public String getInformation(){
    return "---------------test111(test111).defaultValue()----------------";
  }
}
