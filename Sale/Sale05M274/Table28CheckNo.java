package Sale.Sale05M274;

import javax.swing.JTable;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bvalidate;

public class Table28CheckNo extends bvalidate {
  public boolean check(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();
    JTable tb28 = getTable("table28");
    int sRow = tb28.getSelectedRow();

    String projectId = getValue("ProjectID1").trim();
    String orderNo = "";
    String orderDate = "";
    String recordType = "���w�ĤT�H";
    String processType = "query1821";

    String tbName = "table1";
    JTable tbOrder = this.getTable(tbName);
    if (tbOrder.getRowCount() > 0) {
      orderNo = this.getValueAt(tbName, 0, "OrderNo").toString().trim();
      orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    }

    if (!"".equals(value)) {
      String errMsg = "";
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectId, value);
      if (qBean != null) {
        String qName = qBean.getRealName(value);
        String qName2 = qBean.getOtherName(value);
        String showCountryName = ksUtil.getCountryNameByNationCode(qBean.getRealNtCode(value));
        String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
        String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
        String birthday = qBean.getBirthday();
        String indCode = qBean.getJobType();
        String funcName = getFunctionName().trim();
        String bstatus = qBean.getbStatus();
        String cstatus = ksUtil.chkIsCStatus(value, qName, birthday)? "Y":"N";
        String rstatus = qBean.getrStatus();
        setValueAt("table28", qName, sRow, "DesignatedName");
        setValueAt("table28", showCountryName, sRow, "ExportingPlace");
        setValueAt("table28", bstatus, sRow, "Blacklist");
        setValueAt("table28", cstatus, sRow, "Controllist");
        setValueAt("table28", rstatus, sRow, "Stakeholder");

        // �ܴ�Start
        String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," + birthday + "," + indCode + ","
            + countryName + "," + countryName2 + "," + qName2 + "," + processType;
        setValue("AMLText", amlText);
        getButton("BtCustAML").doClick();
        errMsg += getValue("AMLText").trim();
        // �ܴ�END

        // �¦W�� + ���ަW��
        if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
          errMsg = recordType + qName + "���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
        }

        // �Q���H
        if ("Y".equals(rstatus)) {
          errMsg += recordType + qName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
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
    return "---------------null(null).DesignatedId.field_check()----------------";
  }
}
