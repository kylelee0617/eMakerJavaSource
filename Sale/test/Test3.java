package ExSrc.Sale.test;

import ExSrc.Farglory.util.KSqlUtils;

public class Test3 extends jcx.jform.bproc {
  public String getDefaultValue(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();
    ksUtil.setSaleLog(this.getFunctionName(), "����log", this.getUser(), "����LOG�g�JTest210924");
  
    return value;
  }

  public String getInformation() {
    return "---------------Test2(Test2).defaultValue()----------------";
  }
}
