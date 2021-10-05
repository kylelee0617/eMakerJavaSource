package Sale.Sale05M080;

import Farglory.util.KSqlUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class SendMail080 extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    talk dbSale = getTalk("Sale");
    
    KSqlUtils ksUtil = new KSqlUtils();
    String stringSQL = "";
    String[][] reteMail = null;
    
    //////////////////////////////////////////////////////////////////
    // 取畫面值
    String strProjectID1 = getValue("field2").trim();// 案別代碼
    String strEDate = getValue("field3").trim();// 收款日期
    String strDocNo = getValue("field4").trim();// 編號
    String strOrderNo = ksUtil.getOrderNoByDocNo(strDocNo);
    String strOrderDate = ksUtil.getOrderDateByOrderNo(strOrderNo);
    String strCustomName = ksUtil.getCustomNames(strProjectID1, strOrderNo);
    
    stringSQL = "SELECT Position FROM Sale05M092 WHERE OrderNo = '" + strOrderNo + "' ORDER BY RecordNo ";
    reteMail = dbSale.queryFromPool(stringSQL);
    String strPosition = reteMail[0][0];
    
    ////////////////////////////////////
    System.out.println("strProjectID1===>" + strProjectID1);
    System.out.println("strEDate===>" + strEDate);
    System.out.println("strDocNo===>" + strDocNo);
    System.out.println("strOrderNo===>" + strOrderNo);
    System.out.println("strOrderDate===>" + strOrderDate);
    System.out.println("strCustomName===>" + strCustomName);
    System.out.println("strPosition===>" + strPosition);
    
    
    // send email
    String errMsgText = getValue("errMsgBoxText").trim();
    if (!"".equals(errMsgText)) {
      String msg = "一、不動產交易資訊：<BR><BR>1. 案    別：<u>" + strProjectID1 + "</u>&emsp;2. 棟樓別：<u>" + strPosition + "</u>&emsp;3. 客戶姓名：<u>" + strCustomName + "</u>&emsp;4. 付訂日期：<u>"
          + strOrderDate + "</u>&emsp;5. 收款單日期：<u>" + strEDate + "</u><BR><BR>二、符合疑似洗錢態樣通知：<BR><BR>" + errMsgText;
      msg = msg.replace("\n", "<BR>");
      String subject = strProjectID1 + "案" + strPosition + "不動產簽約客戶風險等級評估結果通知";
      String[] arrayUser = ksUtil.getEMAIL收件者名單(this.getUser(), false);
      String sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, msg, null, "", "text/html");

      System.out.println("sendRS===>" + sendRS);
    }
    
    // 制裁名單
    if (errMsgText.indexOf("制裁名單") >= 0) {
      String msg2 = "一、不動產交易資訊：<BR><BR>1. 案    別：<u>" + strProjectID1 + "</u>&emsp;2. 棟樓別：<u>" + strPosition + "</u>&emsp;3. 客戶姓名：<u>" + strCustomName + "</u>&emsp;4. 付訂日期：<u>"
          + strOrderDate + "</u>&emsp;5. 收款單日期：<u>" + strEDate + "</u><BR><BR>二、符合疑似洗錢態樣通知：<BR><BR>客戶" + strCustomName + "為控管之制裁名單對象，請禁止交易，並依洗錢防制內部通報作業送呈法遵室。";
      msg2 = msg2.replace("\n", "<BR>");
      String subject2 = strProjectID1 + "案" + strPosition + "不動產簽約客戶風險等級評估結果通知";
      String[] arrayUser2 = ksUtil.getEMAIL收件者名單(this.getUser(), true);
      String sendRS2 = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser2, subject2, msg2, null, "", "text/html");

      System.out.println("sendRS2===>" + sendRS2);
    }
    return value;
  }

  public String getInformation() {
    return "---------------sendMail(mail).defaultValue()----------------";
  }
}
