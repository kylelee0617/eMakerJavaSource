package Sale.Sale05M090;

import javax.swing.JButton;
import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bvalidate;
import jcx.util.check;

/**
 * table1 - 身分證字號檢核
 * 
 * @author B04391
 *
 */

public class Table1CheckCustNo extends bvalidate {
  public boolean check(String value) throws Throwable {
    System.out.println("getFunctionName>>>" + getFunctionName());
    System.out.println("POSITION>>>" + POSITION);

//    value = value.trim();
    KSqlUtils kSqlUtil = new KSqlUtils();
    KUtils kUtil = new KUtils();

    JTable tb1 = getTable("table1");
    int sRow = tb1.getSelectedRow();
    int sColumn = tb1.getSelectedColumn();
    boolean isCust = true;

    String custNo = "";
    String engNo = "";
    String projectId = getValue("field1").trim();
    String orderNo = getValue("field3").trim();
    String orderDate = getValue("field2").trim();
    String modCustFlag = getValue("CustomID_NAME_PER_Editable").toString().trim();
    String countryName = getValueAt("table1", sRow, "CountryName").toString();
    String countryName2 = getValueAt("table1", sRow, "CountryName2").toString();
    String columnName = tb1.getColumnName(sColumn); // 檢核欄位的名稱
    System.out.println(">>columnName:" + columnName);

    if (POSITION == 4 && "0".equals(modCustFlag)) return false;

    if (sRow == -1) return false;

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
        setValueAt("table1", "1", sRow, "Nationality");

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

      // 取兩個ID
//      custNo = value.trim();
//      engNo = getValueAt("table1", sRow, "EngNo").toString();
    } else if (StringUtils.equals(columnName, "護照證件號")) {
      if (StringUtils.isBlank(countryName2)) {
        messagebox("外國國籍須先行");
        return false;
      }
      
      isCust = false;

      // 取兩個ID
//      custNo = getValueAt("table1", sRow, "CustomNo").toString();
//      engNo = value.trim();
    }

    // 偷黑名單資料
    String custNo3 = kUtil.getCustNo3(custNo, engNo);
    QueryLogBean qBean = kSqlUtil.getQueryLogByCustNoProjectId(projectId, custNo3);
    if (qBean == null) {
      messagebox("無此人資訊，請先執行黑名單查詢。");
      return false;
    }

    String tmpMsg = "";
    String errMsg = "";
    String bstatus = qBean.getbStatus();
    String cstatus = qBean.getcStatus();
    String rstatus = qBean.getrStatus();
    String custName = qBean.getName();
    String birthday = qBean.getBirthday();
    String indCode = qBean.getJobType();
    custNo = qBean.getQueryId();
    engNo = qBean.getEngNo();
    countryName = kSqlUtil.getCountryNameByNationCode(qBean.getNtCode());
    countryName2 = kSqlUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String[] cityTownZip = kSqlUtil.getCityTownZipName(qBean.getCity(), qBean.getTown());
    String engName = qBean.getEngName();
    String addr = qBean.getAddress();
    String funcName = getFunctionName().trim();
    String recordType = "客戶資料";
    setValueAt("table1", "A", sRow, "auditorship"); // 身分，都是一般(已棄用，隱藏中)
    setValueAt("table1", StringUtils.equals(countryName, "中華民國") ? "1" : "2", sRow, "Nationality"); // 舊國籍，其實是本國人或外國人(改名: 本外國人)
    setValueAt("table1", custName, sRow, "CustomName");
    setValueAt("table1", engName, sRow, "EngName");
    
    //寫入另一個id
    if(isCust) setValueAt("table1", engNo, sRow, "EngNo");
    if(!isCust) setValueAt("table1", custNo, sRow, "CustomNo");
    
    setValueAt("table1", birthday, sRow, "Birthday");
    setValueAt("table1", cityTownZip[0], sRow, "ZIP");
    setValueAt("table1", cityTownZip[1], sRow, "City");
    setValueAt("table1", cityTownZip[2], sRow, "Town");
    setValueAt("table1", addr, sRow, "Address");
    setValueAt("table1", countryName, sRow, "CountryName"); // 國籍
    setValueAt("table1", countryName2, sRow, "CountryName2"); // 國籍2
    setValueAt("table1", indCode, sRow, "IndustryCode"); // 行業別代碼
    setValueAt("table1", kSqlUtil.getNameByIndCode(indCode), sRow, "MajorName"); // 行業別
    setValueAt("table1", bstatus, sRow, "IsBlackList");
    setValueAt("table1", cstatus, sRow, "IsControlList");
    setValueAt("table1", rstatus, sRow, "IsLinked");

    // 萊斯Start
    String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + custName + "," + birthday + "," + indCode + ","
        + countryName + "," + countryName2 + "," + engName + "," + "query1821";
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    tmpMsg = getValue("AMLText").trim();
    errMsg += tmpMsg;
    // 萊斯END

    // 黑名單 + 控管名單
    if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
      tmpMsg = "客戶" + custName + "為疑似黑名單對象，請覆核確認後，再進行後續交易相關作業。\n";
      errMsg += tmpMsg;
    }

    // 利關人
    if ("Y".equals(rstatus)) {
      tmpMsg += "客戶" + custName + "為公司利害關系人，請依保險業與利害關係人從事放款以外之其他交易管理辦法執行。\n";
      errMsg += tmpMsg;
    }

    // 顯示
    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }

    // 若只有一筆，給予100%
    if (tb1.getRowCount() == 1) setValueAt("table1", "100", sRow, "Percentage");

    // 定審
    JButton buyedInfo = getButton("BuyedInfo");
    buyedInfo.setText("userCusNo=" + value);
    buyedInfo.doClick();

    return true;
  }

  public String getInformation() {
    return "---------------null(null).CustomNo.field_check()----------------";
  }
}
