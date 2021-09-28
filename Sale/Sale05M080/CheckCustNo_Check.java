package Sale.Sale05M080;

import javax.swing.JTable;
import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import jcx.db.talk;
import jcx.jform.bvalidate;

public class CheckCustNo_Check extends bvalidate {
  public boolean check(String value) throws Throwable {
    String tableName = "table2";
    
    if (StringUtils.isBlank(value)) return false;

    KSqlUtils ksUtil = new KSqlUtils();
    String projectID = getValue("field2").trim();

    // 取得黑名單
    QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectID, value);
    if (qBean == null) {
      message("ID: " + value + " 查無資料，請先執行黑名單查詢。");
      return false;
    }

    String errMsg = "";
    String funcName = getFunctionName().trim();
    String recordType = "銀行代繳款人資料";
    String name = qBean.getName();
    String birthday = qBean.getBirthday().replace("/", "-");
    String indCode = qBean.getJobType();
    String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
    String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String engName = qBean.getEngName();
    String bStatus = qBean.getbStatus();
    String cStatus = qBean.getcStatus();
    String rStatus = qBean.getrStatus();
    String processType = "query1821";

    JTable jtb = getTable(tableName);
    int s_row = jtb.getSelectedRow();
    setValueAt(tableName, name, s_row, "DeputyName");
    setValueAt(tableName, "Y", s_row, "PaymentDeputy");
    setValueAt(tableName, bStatus, s_row, "B_STATUS");
    setValueAt(tableName, cStatus, s_row, "C_STATUS");
    setValueAt(tableName, rStatus, s_row, "R_STATUS");

    // 應該只會有一個訂單，真的遇到兩個再改，備份見信用卡
    String orderNo = getValueAt("table4", 0, "OrderNo").toString().trim();
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String amlText = projectID + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + name + "," + birthday + "," + indCode + "," + countryName
        + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    errMsg += getValue("AMLText").trim();

    // 19. 利害關係人
    if ("Y".equals(rStatus)) {
      errMsg += "代繳款人" + name + "為公司利害關系人，請依保險業與利害關係人從事放款以外之其他交易管理辦法執行。\n";
    }

    // 20. 黑名單
    // 與控管名單合併

    // 17. 控管名單
    if ("Y".equals(bStatus) || "Y".equals(cStatus)) {
      errMsg += "代繳款人" + name + "為疑似黑名單對象，請覆核確認後，再進行後續交易相關作業。\n";
    }

    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }

    return true;
  }

  public String getInformation() {
    return "---------------null(null).DeputyID.field_check()----------------";
  }
}
