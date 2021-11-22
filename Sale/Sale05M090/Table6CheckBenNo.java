package Sale.Sale05M090;

import javax.swing.JTable;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bvalidate;

public class Table6CheckBenNo extends bvalidate {
  public boolean check(String value) throws Throwable {

    KSqlUtils ksUtil = new KSqlUtils();

    JTable tb6 = getTable("table6");
    int sRow = tb6.getSelectedRow();
    String projectId = getValue("field1").trim();
    String orderNo = getValue("field3").trim();
    String orderDate = getValue("field2").trim();
    String recordType = "�����q�H���";
    String processType = "query1821";

    if (!"".equals(value)) {
      String tmpMsg = "";
      String errMsg = "";
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectId, value);
      if (qBean != null) {
        String qName = qBean.getRealName(value);
        String qName2 = qBean.getOtherName(value);
        String birthday = qBean.getBirthday();
        String indCode = qBean.getJobType();
        String funcName = getFunctionName().trim();
        String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
        String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
        String bstatus = qBean.getbStatus();
        String cstatus = ksUtil.chkIsCStatus(value, qName, birthday)? "Y":"N";
        String rstatus = qBean.getrStatus();
        setValueAt("table6", qName, sRow, "BenName");
        setValueAt("table6", ksUtil.getCountryNameByNationCode(qBean.getRealNtCode(value)), sRow, "CountryName");
        setValueAt("table6", birthday, sRow, "Birthday");
        setValueAt("table6", bstatus, sRow, "IsBlackList");
        setValueAt("table6", cstatus, sRow, "IsControlList");
        setValueAt("table6", rstatus, sRow, "IsLinked");

        // �ܴ�Start
        String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," + birthday + "," + indCode + ","
            + countryName + "," + countryName2 + "," + qName2 + "," + processType;
        setValue("AMLText", amlText);
        getButton("BtCustAML").doClick();
        tmpMsg = getValue("AMLText").trim();
        errMsg += tmpMsg;
        // �ܴ�END

        // �¦W�� + ���ަW��
        if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
          tmpMsg = "�����q�H" + qName + "���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
          errMsg += tmpMsg;
        }

        // �Q���H
        if ("Y".equals(rstatus)) {
          tmpMsg += "�����q�H" + qName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
          errMsg += tmpMsg;
        }

        // ���
        if (!"".equals(errMsg)) {
          messagebox(errMsg);
        }

      } else {
        message("�¦W��t�εL����T�C");
      }
    }
    return true;
  }

  public String getInformation() {
    return "---------------null(null).BCustomNo.field_check()----------------";
  }
}
