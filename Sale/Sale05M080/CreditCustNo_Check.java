package Sale.Sale05M080;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import Farglory.util.TalkBean;
import jcx.db.talk;
import jcx.jform.bvalidate;

public class CreditCustNo_Check extends bvalidate {
  public boolean check(String value) throws Throwable {
    String tableName = "table5";

    if (StringUtils.isBlank(value)) return false;

//    talk dbSale = getTalk("Sale");
//    talk dbpw0d = getTalk("pw0d");
//    TalkBean tBean = new TalkBean();
//    tBean.setDbSale(dbSale);
//    tBean.setDbPw0D(dbpw0d);
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
    String recordType = "信用卡代繳款人資料";
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
    
    JTable tb5 = getTable(tableName);
    int s_row = tb5.getSelectedRow();
    setValueAt(tableName, name, s_row, "DeputyName");
    setValueAt(tableName, "Y", s_row, "PaymentDeputy");
    setValueAt(tableName, bStatus, s_row, "B_STATUS");
    setValueAt(tableName, cStatus, s_row, "C_STATUS");
    setValueAt(tableName, rStatus, s_row, "R_STATUS");
    
    //應該只會有一個訂單，真的遇到兩個再改成下面的樣子，留著做參考
    String orderNo = getValueAt("table4", 0, "OrderNo").toString().trim();
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String amlText = projectID + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + name + "," 
        + birthday + "," + indCode + "," + countryName + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    errMsg += getValue("AMLText").trim();

    //210526 Kyle : 萊斯修改 Start : 對應多筆訂單及客戶
    //取訂單編號 & 日期
//    JTable jt3 = this.getTable("table3"); //客戶表格
//    JTable jt4 = this.getTable("table4"); //訂單表格
//    for(int i=0 ; i<jt4.getRowCount() ; i++) {
//      String orderNo = getValueAt("table4", i, "OrderNo").toString().trim();
//      String orderDate = "";
//      for(int ii=0 ; ii<jt3.getRowCount() ; ii++) {
//        String customNo = getValueAt("table3", i, "CustomNo").toString().trim();
//        String sql = "select a.orderNo , a.orderDate from Sale05M090 a , Sale05M091 b where a.orderNo=b.orderNo and b.orderNo='"+orderNo+"' and b.customNo='"+customNo+"' ";
//        String[][] retOrder = dbSale.queryFromPool(sql);
//        if(retOrder.length > 0) {
//          orderDate = retOrder[0][1].trim();  //應該只會有一組
//          break;
//        }
//      }
//      if(StringUtils.isNotBlank(orderDate)) {
//        //訂單&客戶已配對，執行萊斯 start
//        String amlText = projectID + "," + orderNo + "," + orderDate + "," + getFunctionName() + "," + "信用卡代繳款人資料" 
//                       + "," + value + "," + retQuery[0][0].trim() + "," + BirthDay + "," + indCode + "," + "query1821";
//        setValue("AMLText" , amlText);
//        getButton("BtCustAML").doClick();
//        errMsg += getValue("AMLText").trim();
//        // 萊斯END
//        
//        //應該只會配對到一次，做完就可以閃了
//        break;
//      }
//    }
    //210526 Kyle : 萊斯修改 End

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
