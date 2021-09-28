package Sale.AML;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bproc;

public class BtChkCustName extends bproc {
  public String getDefaultValue(String value) throws Throwable {

    String payment_custName = get("payment_custName").toString().trim();
    String payment_caller = get("payment_caller").toString().trim();
    KSqlUtils ksUtil = new KSqlUtils();

    String tableName = "";
    String funcName = getFunctionName().trim();
    String recordType = "###";
    String processType = "query1821";
    String projectID = getValue("field2").trim();

    if (StringUtils.equals(payment_caller, "credit")) {
      tableName = "table5";
      recordType = "信用卡代繳款人資料";
    } else if (StringUtils.equals(payment_caller, "cash")) {
      tableName = "";
      recordType = "現金代繳款人資料";
    } else if (StringUtils.equals(payment_caller, "bank")) {
      tableName = "table9";
      recordType = "銀行代繳款人資料";
    } else if (StringUtils.equals(payment_caller, "check")) {
      tableName = "table2";
      recordType = "票據代繳款人資料";
    } else {
      return value;
    }

    if (StringUtils.isBlank(payment_custName)) return value;

    // 取得黑名單
    QueryLogBean qBean = ksUtil.getQueryLogByName(projectID, payment_custName);
    if ( qBean == null || StringUtils.isBlank(qBean.getRealId(payment_custName)) ) {
      messagebox("代繳款人: " + payment_custName + " 查無資料，請先執行黑名單查詢。");
      return value;
    }

    String errMsg = "";

    String custNo = qBean.getRealId(payment_custName);
    String birthday = qBean.getBirthday().replace("/", "-");
    String indCode = qBean.getJobType();
    String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
    String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String engName = qBean.getEngName();
    String bStatus = qBean.getbStatus();
    String cStatus = qBean.getcStatus();
    String rStatus = qBean.getrStatus();

    JTable jtb = getTable(tableName);
    int s_row = jtb.getSelectedRow();
    setValueAt(tableName, custNo, s_row, "DeputyID");
    setValueAt(tableName, "Y", s_row, "PaymentDeputy");
    setValueAt(tableName, bStatus, s_row, "B_STATUS");
    setValueAt(tableName, cStatus, s_row, "C_STATUS");
    setValueAt(tableName, rStatus, s_row, "R_STATUS");

    // 應該只會有一個訂單，真的遇到兩個再改
    String orderNo = getValueAt("table4", 0, "OrderNo").toString().trim();
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String amlText = projectID + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + custNo + "," + payment_custName + "," + birthday + "," + indCode + ","
        + countryName + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    errMsg += getValue("AMLText").trim();

    // 19. 利害關係人
    if ("Y".equals(rStatus)) {
      errMsg += "代繳款人" + payment_custName + "為公司利害關系人，請依保險業與利害關係人從事放款以外之其他交易管理辦法執行。\n";
    }

    // 20. 黑名單
    // 與控管名單合併

    // 17. 控管名單
    if ("Y".equals(bStatus) || "Y".equals(cStatus)) {
      errMsg += "代繳款人" + payment_custName + "為疑似黑名單對象，請覆核確認後，再進行後續交易相關作業。\n";
    }

    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }

    return value;
  }

  public String getInformation() {
    return "---------------BtChkCustNo(\u6536\u6b3eChkCustNo).defaultValue()----------------";
  }
}
