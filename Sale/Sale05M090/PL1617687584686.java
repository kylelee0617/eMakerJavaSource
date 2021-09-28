package Sale.Sale05M090;

import java.util.HashMap;
import java.util.Map;

import Farglory.aml.AMLTools_Lyods;
import Farglory.aml.AMLyodsBean;
import Farglory.aml.RiskCustomBean;
import Farglory.util.Result;
import jcx.jform.sproc;

/**
 * 
 * checkAML_18_21
 * 
 * @author B04391
 *
 */

public class PL1617687584686 extends sproc {
  public String getDefaultValue(String value) throws Throwable {
    String serverType = "";
    String lyodsSoapURL = "";
    String lyodsCustType = getValue("lyodsCustType");
    String orderNo = getValue("field3").trim();
    String orderDate = getValue("field2").trim();
    RiskCustomBean[] custBeans = null;

    Map config = (HashMap) get("config");
    serverType = config.get("serverType").toString();
    lyodsSoapURL = config.get("lyodsSoapURL").toString();
    System.out.println("serverType>>>" + serverType);
    System.out.println("lyodsSoapURL>>>" + lyodsSoapURL);

    AMLyodsBean aBean = new AMLyodsBean();
    aBean.setOrderNo(orderNo);
    aBean.setOrderDate(orderDate.replaceAll("/", ""));
    aBean.setEmakerUserNo(getUser());
    aBean.setTestServer("PROD".equals(serverType) ? false : true);
    aBean.setLyodsSoapURL(lyodsSoapURL);
    aBean.setDb400CRM(getTalk("400CRM"));
    aBean.setDbSale(getTalk("Sale"));
    aBean.setDbEIP(getTalk("EIP"));
    AMLTools_Lyods aml = new AMLTools_Lyods(aBean);
    System.out.println("test1>>>" + value);

    if ("custom".equals(lyodsCustType)) {
      String[][] t1 = this.getTableData("table1");
      custBeans = new RiskCustomBean[t1.length];
      for (int intRow = 0; intRow < t1.length; intRow++) {
        RiskCustomBean custBean = new RiskCustomBean();
        custBean.setCustomNo(t1[intRow][5].toString().trim());
        custBean.setCustomName(t1[intRow][6].toString().trim());
//        custBean.setBirthday(t1[intRow][8].toString().trim());
        custBean.setBirthday("19830101");
//        custBean.setIndustryCode(t1[intRow][24].toString().trim());
        custBean.setIndustryCode("22");
        custBeans[intRow] = custBean;
      }
    } else if ("ben".equals(value)) {

    } else if ("agent".equals(value)) {

    }

    for (int custCount = 0; custCount < custBeans.length; custCount++) {
      System.out.println("test3>>>");
      RiskCustomBean custBean = custBeans[custCount];
      System.out.println("no>>>" + custBean.getCustomNo());
      System.out.println("name>>>" + custBean.getCustomName());
      System.out.println("bir>>>" + custBean.getBirthday());
      System.out.println("idt>>>" + custBean.getIndustryCode());

      // ¨îµô¦W³æ
      Result rs018 = aml.chkAML018_San(custBean);
      System.out.println("rs018>>>" + (String) rs018.getData());
      System.out.println("rs018>>>" + (String) rs018.getRsStatus()[1]);

      // PEPS
      Result rs021 = aml.chkAML021_PEPS(custBean);
      System.out.println("rs021>>>" + (String) rs021.getData());
      System.out.println("rs021>>>" + (String) rs021.getRsStatus()[1]);
    }

    return value;
  }

  public String getInformation() {
    return "---------------checkAML_18_21(checkAML_18_21).defaultValue()----------------";
  }
}
