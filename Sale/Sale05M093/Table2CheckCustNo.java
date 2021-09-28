package Sale.Sale05M093;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import jcx.db.talk;
import jcx.jform.bvalidate;
import jcx.util.check;

public class Table2CheckCustNo extends bvalidate {
  public boolean check(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();
    KUtils kUtil = new KUtils();
    String custTable = "table2";

    JTable tb2 = getTable(custTable);
    int sRow = tb2.getSelectedRow();
    int sColumn = tb2.getSelectedColumn();

    String custNo = "";
    String engNo = "";
    String projectId = getValue("ProjectID1").trim();
    String orderNo = getValue("OrderNo");
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String countryName = getValueAt(custTable, sRow, "CountryName").toString();
    String countryName2 = getValueAt(custTable, sRow, "CountryName2").toString();
    String columnName = tb2.getColumnName(sColumn); // 檢核欄位的名稱
    System.out.println(">>columnName:" + columnName);

    if (sRow == -1) return true;

    // 基本檢核
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

    // 僅檢核統編/身分證號欄位 之 ID正確性
    if (StringUtils.equals(columnName, "統編/身分證號")) {
      if (StringUtils.isBlank(countryName)) {
        messagebox("(本國)國籍須先行");
        return false;
      }

      // 本國人檢核身分證號
      if (StringUtils.equals(countryName, "中華民國")) { // 本國人
        setValueAt(custTable, "1", sRow, "Nationality");

        if (value.length() != 8 && value.length() != 10) {
          messagebox("[統編/身分證號] 長度錯誤!");
          return false;
        } else if (value.length() == 8 && check.isCoId(value) == false) {
          messagebox("[統編/身分證號] 統一編號錯誤!");
          return false;
        } else if (value.length() == 10 && check.isID(value) == false) {
          messagebox("[統編/身分證號] 身分證號錯誤!");
          return false;
        }
      }

      // 取ID值
      custNo = value.trim();
      engNo = getValueAt(custTable, sRow, "EngNo").toString();
    } else if (StringUtils.equals(columnName, "護照證件號")) {
      if (StringUtils.isBlank(countryName2)) {
        messagebox("外國國籍須先行");
        return false;
      }

      // 取ID
      custNo = getValueAt(custTable, sRow, "CustomNo").toString();
      engNo = value.trim();
    }
    System.out.println(">>>custNO:" + custNo);
    System.out.println(">>>engNo:" + engNo);

    // 偷黑名單資料
    String custNo3 = kUtil.getCustNo3(custNo, engNo);
    QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectId, custNo3);
    if (qBean == null) {
      messagebox("無此人資訊，請先執行黑名單查詢。");
      return false;
    }

    String tmpMsg = "";
    String errMsg = "";
    String bstatus = qBean.getbStatus();
    String cstatus = qBean.getcStatus();
    String rstatus = qBean.getrStatus();
    String qName = qBean.getName();
    String birthday = qBean.getBirthday();
    countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
    countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String[] cityTownZip = ksUtil.getCityTownZipName(qBean.getCity(), qBean.getTown());
    String engName = qBean.getEngName();
    String addr = qBean.getAddress();
    String funcName = getFunctionName().trim();
    String recordType = "客戶資料";
    String indCode = qBean.getJobType();
    String processType = "query1821";

    setValueAt(custTable, StringUtils.equals(countryName, "中華民國") ? "1" : "2", sRow, "Nationality"); // 舊國籍，其實是本國人或外國人(改名: 本外國人)
    setValueAt(custTable, qName, sRow, "CustomName");
    setValueAt(custTable, engName, sRow, "EngName");
    setValueAt(custTable, qBean.getEngNo(), sRow, "EngNo");
    setValueAt(custTable, birthday, sRow, "Birthday");
    setValueAt(custTable, cityTownZip[0], sRow, "ZIP");
    setValueAt(custTable, cityTownZip[1], sRow, "City");
    setValueAt(custTable, cityTownZip[2], sRow, "Town");
    setValueAt(custTable, addr, sRow, "Address");
    setValueAt(custTable, countryName, sRow, "CountryName"); // 國籍
    setValueAt(custTable, countryName2, sRow, "CountryName2"); // 國籍2
//    setValueAt(custTable, indCode, sRow, "IndustryCode"); // 行業別代碼
    setValueAt(custTable, ksUtil.getNameByIndCode(indCode), sRow, "MajorName"); // 行業別
    setValueAt(custTable, bstatus, sRow, "IsBlackList");
    setValueAt(custTable, cstatus, sRow, "IsControlList");
    setValueAt(custTable, rstatus, sRow, "IsLinked");

    // 萊斯Start
    String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," + birthday + "," + indCode + ","
        + countryName + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    tmpMsg = getValue("AMLText").trim();
    errMsg += tmpMsg;
    // 萊斯END

    // 黑名單 + 控管名單
    if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
      tmpMsg = "客戶" + qName + "為疑似黑名單對象，請覆核確認後，再進行後續交易相關作業。\n";
      errMsg += tmpMsg;
    }

    // 利關人
    if ("Y".equals(rstatus)) {
      tmpMsg += "客戶" + qName + "為公司利害關系人，請依保險業與利害關係人從事放款以外之其他交易管理辦法執行。\n";
      errMsg += tmpMsg;
    }

    // 顯示
    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }

    return true;
  }

  public String getInformation() {
    return "---------------null(null).CustomNoNew.field_check()----------------";
  }
}
