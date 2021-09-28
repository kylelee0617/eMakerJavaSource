package Sale.AML;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class RenewRelated extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    System.out.println(">>>��s���p�H>>> Start"); 
    
    KUtils kUtil = new KUtils();
    String errMsg = "";
    String funcName = value.trim();
    String recordType = "��s�D�n�Ȥ�P�L�̪����p�H";
    String projectId = "";
    String orderNo = "";
    String orderDate = "";
    String processType = "updRelated";
    
    //config
    Map config = (HashMap) get("config");
    boolean isTest = "PROD".equals(config.get("serverType").toString()) ? false : true;
    
    if(StringUtils.contains(funcName, "�ʫ��ҩ���")) {
      projectId = getValue("field1").trim();
      orderNo = getValue("field3").trim();
      orderDate = getValue("field2").trim();
    }else if(StringUtils.contains(funcName, "���W")) {
      projectId = getValue("ProjectID1").trim();
      orderNo = getValue("OrderNo").trim();
      orderDate = kUtil.getOrderDateByOrderNo(orderNo);
    }else if(StringUtils.contains(funcName, "�X���|�f")) {
      projectId = getValue("ProjectID1").trim();
      orderNo = this.getValueAt("table1", 0, "OrderNo").toString().trim();
      orderDate = kUtil.getOrderDateByOrderNo(orderNo);
    }else if(StringUtils.contains(funcName, "�h��")) {
      projectId = getValue("field1").trim();
      orderNo = getValue("field3").trim();
      orderDate = kUtil.getOrderDateByOrderNo(orderNo);
      processType = "deleteRelated";
    }
    
    //21-05-13 Kyle : ��s�ܴ��D�n�Ȥ�
    talk dbSale = getTalk("Sale");
    String sql = "select CustomNo , CustomName , Birthday , IndustryCode, countryName, countryName2, EngNo, EngName from Sale05M091 where orderNO = '"+orderNo+"' and ISNULL(statusCd, '') != 'C' ";
    String[][] retCustom = dbSale.queryFromPool(sql);
    for(int i=0 ; i<retCustom.length ; i++) {
      String tmpId = StringUtils.isNotBlank(retCustom[i][0].trim())? retCustom[i][0].trim():retCustom[i][6].trim();;
      String tmpName = retCustom[i][1].trim();
      String tmpBirth = retCustom[i][2].trim();
      String tmpIndCode = retCustom[i][3].trim(); 
      String countryName = retCustom[i][4].trim();
      String countryName2 = retCustom[i][5].trim();
      String engName = retCustom[i][7].trim();
      String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + tmpId + "," + tmpName + ","
          + tmpBirth + "," + tmpIndCode + "," + countryName + "," + countryName2 + "," + engName + "," + processType;
      System.out.println("amlText" + amlText);
      setValue("AMLText", amlText);
      getButton("BtCustAML").doClick();
      errMsg += getValue("AMLText").trim();
    }
    
    if(StringUtils.isNotBlank(errMsg)) {
      System.out.println(funcName + "���G:" + errMsg);
    }

    System.out.println(">>>��s���p�H>>> End");
    return value;
  }

  public String getInformation() {
    return "---------------Test(Test).defaultValue()----------------";
  }
}