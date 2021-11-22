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
    String recordType = "指定第三人";
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

        // 萊斯Start
        String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," + birthday + "," + indCode + ","
            + countryName + "," + countryName2 + "," + qName2 + "," + processType;
        setValue("AMLText", amlText);
        getButton("BtCustAML").doClick();
        errMsg += getValue("AMLText").trim();
        // 萊斯END

        // 黑名單 + 控管名單
        if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
          errMsg = recordType + qName + "為疑似黑名單對象，請覆核確認後，再進行後續交易相關作業。\n";
        }

        // 利關人
        if ("Y".equals(rstatus)) {
          errMsg += recordType + qName + "為公司利害關系人，請依保險業與利害關係人從事放款以外之其他交易管理辦法執行。\n";
        }

        // 顯示
        if (!"".equals(errMsg)) {
          messagebox(errMsg);
        }

      } else {
        message("黑名單系統無此資訊。");
      }
    }
    return true;
  }

  public String getInformation() {
    return "---------------null(null).DesignatedId.field_check()----------------";
  }
}
