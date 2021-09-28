package Sale.test;

import javax.swing.JTable;

import Farglory.util.KSqlUtils;
import jcx.jform.sproc;

public class Test090 extends sproc {
  public String getDefaultValue(String value) throws Throwable {

    KSqlUtils ksutil = new KSqlUtils();
    value = value.trim();
    
    

    if ("B".equals(value)) {
      String amlDesc = ksutil.getAMLDescOne("023");
      amlDesc = amlDesc.replaceAll("<customTitle>", "客戶").replace("<customName>", "");
      messagebox("客戶XXX要求退還款項給付予第三人，請依洗錢及資恐防制作業辦理。");
    }

//    getButton("BtGetBackPayTarget2").doClick();

    return "";
  }

  public String getInformation() {
    return "---------------Test(Test).defaultValue()----------------";
  }
}
