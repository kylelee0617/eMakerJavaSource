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
    String recordType = "實質受益人資料";
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

        // 萊斯Start
        String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," + birthday + "," + indCode + ","
            + countryName + "," + countryName2 + "," + qName2 + "," + processType;
        setValue("AMLText", amlText);
        getButton("BtCustAML").doClick();
        tmpMsg = getValue("AMLText").trim();
        errMsg += tmpMsg;
        // 萊斯END

        // 黑名單 + 控管名單
        if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
          tmpMsg = "實質受益人" + qName + "為疑似黑名單對象，請覆核確認後，再進行後續交易相關作業。\n";
          errMsg += tmpMsg;
        }

        // 利關人
        if ("Y".equals(rstatus)) {
          tmpMsg += "實質受益人" + qName + "為公司利害關系人，請依保險業與利害關係人從事放款以外之其他交易管理辦法執行。\n";
          errMsg += tmpMsg;
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
    return "---------------null(null).BCustomNo.field_check()----------------";
  }
}
