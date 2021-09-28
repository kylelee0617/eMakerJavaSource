package ExSrc.Sale.test;

import ExSrc.Farglory.util.KSqlUtils;

public class Test3 extends jcx.jform.bproc {
  public String getDefaultValue(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();
    ksUtil.setSaleLog(this.getFunctionName(), "´ú¸Õlog", this.getUser(), "´ú¸ÕLOG¼g¤JTest210924");
  
    return value;
  }

  public String getInformation() {
    return "---------------Test2(Test2).defaultValue()----------------";
  }
}
