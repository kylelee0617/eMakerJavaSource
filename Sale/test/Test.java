package Sale.test;
import org.apache.commons.lang.StringUtils;

import Farglory.util.KUtils;
import jcx.jform.sproc;
import jcx.util.datetime;

public class Test extends sproc{
  public String getDefaultValue(String value)throws Throwable{
    
    String stringEDate = "2022/01/09"; // 發票日
    String stringDateTime = datetime.getTime("YYYY/mm/dd h:m:s"); // 發票開立時 系統時間
    
    KUtils util = new KUtils();
    String invoiceOpenDate = stringDateTime.split(" ")[0].trim(); // 切出發票開立日期
    
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
