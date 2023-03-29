package Sale.test;
import org.apache.commons.lang.StringUtils;

import Farglory.util.KUtils;
import jcx.jform.sproc;
import jcx.util.datetime;

public class Test extends sproc{
  public String getDefaultValue(String value)throws Throwable{
    String table = getValue("table").toString().trim();
    String db = getValue("dataBase").trim();
    
    String sql = "";
    if("400CRM".equals(db)) {
      sql = "select * from " + table + " FETCH FIRST 100 ROWS ONLY";
    } else {
      sql = "select top 100 * from " + table;
    }

    setValue("script" , sql);

    return value;
  }
  public String getInformation(){
    return "---------------test111(test111).defaultValue()----------------";
  }
}
