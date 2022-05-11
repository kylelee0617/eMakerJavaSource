package Sale.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;
import jcx.util.datetime;

public class ReProcess094 extends bproc {
  String 史大Date;
  String 安得Date;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbEIP;
  talk dbJPSLIB;
  String serverType;
  boolean isTest = true;

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    dbEIP = ksUtil.getTBean().getDbEIP();
    dbJPSLIB = getTalk("JGENLIB");
    serverType = get("serverType").toString().trim();

    // 設定環境
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>環境:" + serverType);

    if (!this.欄位檢核()) return value;

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("重跑退戶");

    String testText = "";
    if (isTest) testText = "top 100";

    String sql91 = "select " + testText + " * from sale05m094 where trxdate BETWEEN '" + 史大Date + "' AND '" + 安得Date + "' "
                 + "and OrderNo not in (select OrderNo from sale05m094 "
                 + "where trxdate BETWEEN '" + 史大Date + "' AND '" + 安得Date + "' "
                 + "and BItemCd='03' and MItemCd='03' and SItemCd='01' and DItemCd in ('01','03','04','07','08','09','10','11') ) " + "order by trxdate desc";
    String[][] ret = dbSale.queryFromPool(sql91);

    String stringToday = datetime.getToday("YYYY/mm/dd"); // 2015-05-13 B3018
    String RocNowDate = stringToday.replace("/", "");
    String tempROCYear = "" + (Integer.parseInt(RocNowDate.substring(0, RocNowDate.length() - 4)) - 1911);
    RocNowDate = tempROCYear + RocNowDate.substring(RocNowDate.length() - 4, RocNowDate.length());
    String strNowTime = new SimpleDateFormat("HHmmss").format(getDate());
    String userNo = getUser().toUpperCase().trim();
    String empNo = "system";
    String[][] retEip = null;
    String errMsg = "";

    // actionNo
    String ram = "";
    Random random = new Random();
    for (int i = 0; i < 4; i++) {
      ram += String.valueOf(random.nextInt(10));
    }
    String actionNo = stringToday.replace("/", "") + strNowTime + ram;
    System.out.println("actionNo=====>" + actionNo);

    // 取員工編號
    String stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + userNo + "'";
    retEip = dbEIP.queryFromPool(stringSQL);
    if (retEip.length > 0) {
      empNo = retEip[0][0];
    }

    for (int i = 0; i < ret.length; i++) {
      String projectId1 = ret[i][0].trim();
      String orderNo = ret[i][1].trim();
      String trxDate = ret[i][2].trim();

      // 洗錢追蹤流水號
      int intRecordNo = 1;
      stringSQL = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE OrderNo ='" + orderNo + "'";
      String[][] ret070Table = dbSale.queryFromPool(stringSQL);
      if (!"".equals(ret070Table[0][0].trim())) {
        intRecordNo = Integer.parseInt(ret070Table[0][0].trim()) + 1;
      }

      // 取客戶
      stringSQL = "select distinct CustomNo , CustomName from Sale05M091 a " + "left join sale05m090 b on a.OrderNo = b.OrderNo "
          + "where ISNULL(a.StatusCd, '') != 'C' and a.OrderNo = '" + orderNo + "' ";
      String[][] ret2 = dbSale.queryFromPool(stringSQL);

      for (int j = 0; j < ret2.length; j++) {
        String strCustomNo = ret2[j][0].trim();
        String strCustomName = ret2[j][1].trim();

        // Sale05M070
        stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) "
            + "VALUES " + "('" + orderNo + "','" + projectId1 + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','客戶" + strCustomName + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。','"
            + strCustomNo + "','" + strCustomName + "','" + trxDate + "','RY','773','006','客戶" + strCustomName + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。','" + empNo + "','" + RocNowDate + "','"
            + strNowTime + "')";
        dbSale.execFromPool(stringSQL);
        intRecordNo++;

        // AS400
        stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + orderNo + "', '" + RocNowDate + "', '"
            + strCustomNo + "', '" + strCustomName + "', '773', '006', '同一客戶不動產買賣，簽約前退訂取消購買警示。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
        dbJPSLIB.execFromPool(stringSQL);
      }

    }

    this.message(">>> 重跑退戶警示完成 !!! <<<");
  }

  public boolean 欄位檢核() throws Throwable {
    史大Date = this.getValue("StartDate").trim();
    安得Date = this.getValue("EndDate").trim();
    if (StringUtils.isBlank(史大Date) || StringUtils.isBlank(安得Date)) {
      message("日期!");
      return false;
    }

    if (史大Date.length() == 8) {
      if (!check.isACDay(史大Date)) return false;
      史大Date = kUtil.formatACDate(史大Date, "/");
    } else if (史大Date.length() == 10 && StringUtils.contains(史大Date, "/")) {

    } else {
      message("史大Date日期格式錯誤");
      return false;
    }

    if (安得Date.length() == 8) {
      if (!check.isACDay(安得Date)) return false;
      安得Date = kUtil.formatACDate(安得Date, "/");
    } else if (安得Date.length() == 10 && StringUtils.contains(安得Date, "/")) {

    } else {
      message("安得Date日期格式錯誤");
      return false;
    }

    return true;
  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
