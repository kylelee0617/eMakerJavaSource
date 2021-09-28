package Sale.AML;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;

import Farglory.util.Result;
import jcx.jform.bproc;

public class BtUpdCust extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    System.out.println(">>>��s���p�H>>> Start");
    String errMsg = "";
    String rsMsg = "";  
    Result result = null;
    
    //config
    boolean isTest = false;
    String serverType = "";
    String lyodsSoapURL = "";
    Map config = (HashMap) get("config");
    serverType = config.get("serverType").toString();
    lyodsSoapURL = config.get("lyodsSoapURL").toString();
    isTest = "PROD".equals(serverType) ? false : true;
    
    String projectId = getValue("field1").trim();
    String orderNo = getValue("field3").trim();
    String orderDate = getValue("field2").trim();
    String tableCode = "table1";
    String processType = "updRelated";
    
    //21-05-13 Kyle : ��s�ܴ��D�n�Ȥ�
    JTable t1 = this.getTable(tableCode);
    int rowCount = t1.getRowCount();
    for (int intRow = 0; intRow < rowCount; intRow++) {
      String custNo = this.getValueAt(tableCode, intRow, "CustomNo").toString().trim();
      String custName = this.getValueAt(tableCode, intRow, "CustomName").toString().trim();
      String birth = getValueAt(tableCode, intRow, "Birthday").toString().trim();
      String indCode = getValueAt(tableCode, intRow, "IndustryCode").toString();

      String amlText = projectId + "," + orderNo + "," + orderDate + "," + getFunctionName() + "," + "��s�D�n�Ȥ�P�L�̪����p�H" 
                     + "," + custNo + "," + custName + "," + birth + "," + indCode + "," + processType;
      setValue("AMLText", amlText);
      getButton("BtCustAML").doClick();
      errMsg += getValue("AMLText").trim();
    }
    
    System.out.println("errMsg>>>" + errMsg);

    System.out.println(">>>��s���p�H>>> End");
    return value;
  }

  public String getInformation() {
    return "---------------Test(Test).defaultValue()----------------";
  }
}
