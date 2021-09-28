package Sale.Sale05M093;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bvalidate;

public class Table6CheckCustNo extends bvalidate {
  public boolean check(String value) throws Throwable {

    KSqlUtils ksUtil = new KSqlUtils();
    String thisTable = "table6";

    JTable jTable = getTable(thisTable);
    int sRow = jTable.getSelectedRow();
    int sColumn = jTable.getSelectedColumn();
    String projectId = getValue("ProjectID1").trim();
    String orderNo = getValue("OrderNo");
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo); // �� orderDate

    // ���ˮ�
    String columnName = jTable.getColumnName(sColumn); // �ˮ���쪺�W��
    if ("".equals(projectId)) {
      message("[�קO�N�X] ���i�ť�!");
      return false;
    }
    if (value.length() == 0) {
      message(columnName + " �ˮֿ��~ : �ť�!");
      return false;
    }
    if (value.length() != value.trim().length()) {
      message(columnName + " �ˮֿ��~ : �Ů�!");
      return false;
    }

    if (!"".equals(value)) {
      String tmpMsg = "";
      String errMsg = "";
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectId, value);
      if (qBean == null) {
        messagebox("�L���H��T�A�Х�����¦W��d�ߡC");
        return false;
      }
      
      String bstatus = qBean.getbStatus();
      String cstatus = qBean.getcStatus();
      String rstatus = qBean.getrStatus();
      String qName = qBean.getRealName(value);
      String qName2 = qBean.getOtherName(value);
      String birthday = qBean.getBirthday();
      String indCode = qBean.getJobType();
      String funcName = getFunctionName().trim();
      String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
      String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
      String recordType = "�N�z�H���";
      String processType = "query1821";
      setValueAt("table6", qName, sRow, "AgentName");
      setValueAt("table6", ksUtil.getCountryNameByNationCode(qBean.getRealNtCode(value)), sRow, "CountryName");
      setValueAt("table6", bstatus, sRow, "IsBlackList");
      setValueAt("table6", cstatus, sRow, "IsControlList");
      setValueAt("table6", rstatus, sRow, "IsLinked");

      // �ܴ�Start
      String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," 
          + birthday + "," + indCode + "," + countryName + "," + countryName2 + "," + qName2 + "," + processType;
      setValue("AMLText", amlText);
      getButton("BtCustAML").doClick();
      tmpMsg = getValue("AMLText").trim();
      errMsg += tmpMsg;
      // �ܴ�END

      // �¦W�� + ���ަW��
      if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
        tmpMsg = "�N�z�H" + qName + "���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
        errMsg += tmpMsg;
      }

      // �Q���H
      if ("Y".equals(rstatus)) {
        tmpMsg += "�N�z�H" + qName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
        errMsg += tmpMsg;
      }

      // ���
      if (!"".equals(errMsg)) {
        messagebox(errMsg);
      }

    }
    return true;
  }

  public String getInformation() {
    return "---------------null(null).ACustomNo.field_check()----------------";
  }
}
