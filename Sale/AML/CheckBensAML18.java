package Sale.AML;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import Farglory.util.TalkBean;
import jcx.jform.bproc;

/**
 * �b�s�W�έק��x�s�e�A�ˬd�k�H������H�O�_������W��ÿ�X���G
 * 
 * @author B04391
 *
 */
public class CheckBensAML18 extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    System.out.println(">>> �����q�H AML18  >>> Start");

    KSqlUtils ksUtil = new KSqlUtils();
    String rsMsg = "";
    String funcName = value.trim();
    String recordType = "�����q�H���";
    String projectId = "";
    String orderNo = "";
    String orderDate = "";
    String processType = "query18";
    String tbName = "";
    String comId = ""; // id column
    String comName = ""; // name column
    if (StringUtils.contains(funcName, "�ʫ��ҩ���")) {
      projectId = getValue("field1").trim();
      orderNo = getValue("field3").trim();
      orderDate = getValue("field2").trim();
      tbName = "table6";
      comId = "BCustomNo";
      comName = "BenName";
    } else if (StringUtils.contains(funcName, "���W")) {
      projectId = getValue("ProjectID1").trim();
      orderNo = getValue("OrderNo").trim();
      orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
      tbName = "table5";
      comId = "BenNo";
      comName = "BenName";
    }

    String[][] tbData = this.getTableData(tbName);
    System.out.println("tbData : " + tbData.length);
    for (int tbRow = 0; tbRow < tbData.length; tbRow++) {
      String id = this.getValueAt(tbName, tbRow, comId).toString().trim();
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectId, id);
      if (qBean == null) {
        System.out.println("qBean No DATA : " + id);
        continue;
      }
      String name = this.getValueAt(tbName, tbRow, comName).toString().trim();
      String birthday = this.getValueAt(tbName, tbRow, "Birthday").toString().trim();
      String indCode = qBean.getJobType();
      String countryName = this.getValueAt(tbName, tbRow, "CountryName").toString().trim();
      String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
      String engName = qBean.getEngName();
      String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + id + "," + name + ","
          + birthday + "," + indCode + "," + countryName + "," + countryName2 + "," + engName + "," + "query18";
      setValue("AMLText", amlText);
      getButton("BtCustAML").doClick();
      rsMsg += StringUtils.equals(getValue("AMLText").trim(), "Y") ? "" : getValue("AMLText").trim();
    }
    setValue("AMLText", rsMsg);

    // ���
    if (StringUtils.isNotBlank(rsMsg)) {
      messagebox(rsMsg);
    }

    System.out.println(">>> �����q�H AML18  >>> End");
    return value;
  }

  public String getInformation() {
    return "---------------Test(Test).defaultValue()----------------";
  }
}
