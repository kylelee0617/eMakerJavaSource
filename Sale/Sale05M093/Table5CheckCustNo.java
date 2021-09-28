package Sale.Sale05M093;

import javax.swing.JTable;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bvalidate;

public class Table5CheckCustNo extends bvalidate {
  public boolean check(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();
    String thisTable = "table5";

    JTable jTable = getTable(thisTable);
    int sRow = jTable.getSelectedRow();
    int sColumn = jTable.getSelectedColumn();
    String projectId = getValue("ProjectID1").trim();
    String orderNo = getValue("OrderNo");
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo); // 取 orderDate

    // 基本檢核
    String columnName = jTable.getColumnName(sColumn); // 檢核欄位的名稱
    if ("".equals(projectId)) {
      message("[案別代碼] 不可空白!");
      return false;
    }
    if (value.length() == 0) {
      message(columnName + " 檢核錯誤 : 空白!");
      return false;
    }
    if (value.length() != value.trim().length()) {
      message(columnName + " 檢核錯誤 : 空格!");
      return false;
    }

    String tmpMsg = "";
    String errMsg = "";
    QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectId, value);
    if (qBean == null) {
      messagebox("無此人資訊，請先執行黑名單查詢。");
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
    String recordType = "實質受益人資料";
    String processType = "query1821";
    setValueAt(thisTable, qName, sRow, "BenName");
    setValueAt(thisTable, ksUtil.getCountryNameByNationCode(qBean.getRealNtCode(value)), sRow, "CountryName");
    setValueAt(thisTable, birthday, sRow, "Birthday");
    setValueAt(thisTable, bstatus, sRow, "IsBlackList");
    setValueAt(thisTable, cstatus, sRow, "IsControlList");
    setValueAt(thisTable, rstatus, sRow, "IsLinked");

    // 萊斯Start
    String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," 
                   + birthday + "," + indCode + "," + countryName + "," + countryName2 + "," + qName2 + "," + processType;
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

    return true;
  }

  public String getInformation() {
    return "---------------null(null).BenNO.field_check()----------------";
  }
}
