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
      amlDesc = amlDesc.replaceAll("<customTitle>", "�Ȥ�").replace("<customName>", "");
      messagebox("�Ȥ�XXX�n�D�h�ٴڶ����I���ĤT�H�A�Ш̬~���θꮣ����@�~��z�C");
    }

//    getButton("BtGetBackPayTarget2").doClick();

    return "";
  }

  public String getInformation() {
    return "---------------Test(Test).defaultValue()----------------";
  }
}
