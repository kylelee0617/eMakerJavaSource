package Sale.util;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.Result;
import Farglory.util.ResultStatus;
import jcx.db.talk;
import jcx.jform.bproc;

public class SendMailAction2 extends bproc {

  talk dbSale,dbEIP,dbEMail;
  KSqlUtils ksUtil;
  boolean isTest;
  
  String userEmail = "";
  String userEmail2 = "";
  String DPeMail = "";
  String DPeMail2 = "";
  String PNMail = "";
  String testRemark = "(測試)"; // 在測試環境要加註測試字樣
  String testPGMail = "Kyle_Lee@fglife.com.tw"; // 測試環境寄送測試mail
  
  //畫面值
  String projectId = ""; // 案別代碼
  String orderDate = ""; // 付訂日期
  String orderNo = ""; // 訂單編號
  String positions = "";
  String customNames = "";
  String errMsgText = "";

  public SendMailAction2() {
    // 20200508 kyle Add 根據伺服器是否加測試訊息
    isTest = "PROD".equals(get("serverType").toString().trim()) ? false : true;
    System.out.println(">>>環境:" + testRemark);
    if (!isTest) {
      testRemark = "";
      testPGMail = "";
    }
    dbSale = getTalk("Sale");
    dbEIP = getTalk("EIP");
    dbEMail = getTalk("eMail");
  }

  public String getDefaultValue(String value) throws Throwable {
    System.out.println("==============sendMailAction2 STAR====================================");
    ksUtil = new KSqlUtils();
    this.getSendUser();
    
    // 切換功能
    String funcName = value.trim();
    if (StringUtils.contains(funcName, "購屋證明單")) {
      //暫時沒用
    } else if (StringUtils.contains(funcName, "收款")) {
      //暫無用
    } else if (StringUtils.contains(funcName, "退戶")) {
      this.sendMailRule23();
    }

    System.out.println("==============sendMailAction2 END====================================");
    return value;
  }

  
  public Result sendMailRule23() throws Throwable {
    Result rs = new Result();
    projectId = getValue("field1").trim();  // 案別代碼
    orderNo = getValue("field3").trim();    // 訂單編號
    orderDate = ksUtil.getOrderDateByOrderNo(orderNo);  // 付訂日期
    positions = ksUtil.getPositions(projectId, orderNo);
    customNames = ksUtil.getCustomNames(projectId, orderNo);
    String amlNo = "23";  //洗錢態樣編號

    try {
      String desc = "案別代碼：" + projectId + "<BR>棟樓別：" + positions + "<BR>訂戶姓名：" + customNames + "<BR>付訂日期：" + orderDate 
                  + "<BR>警示訊息 : " + ksUtil.getAMLDescOne(amlNo).replaceAll("<customName>", customNames).replaceAll("<customTitle>", "客戶");
      String subject = "退戶  案別：" + projectId + "  棟樓別：" + positions + "  客戶洗錢態樣監控預警" + testRemark;
      String[] arrayUser = ksUtil.getEMAIL收件者名單(getUser(), isTest);
      String sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, desc, null, "", "text/html");
      if (StringUtils.isNotBlank(sendRS)) {
        rs.setReturnCode(Integer.parseInt(ResultStatus.SENDEMAILERROR[0]));
        rs.setReturnMsg(sendRS);
        rs.setRsStatus(ResultStatus.SENDEMAILERROR);
        messagebox(sendRS);
      }

    } catch (Exception ex) {
      rs.setRsStatus(ResultStatus.SENDEMAILERROR);
      rs.setExp(ex);
    }
    
    System.out.println(">>>send mail RS:\n" + rs.getReturnMsg());

    return rs;
  }

  public void getSendUser() throws Throwable {
    String[][] retEip = null;
    String[][] reteMail = null;
    String empNo = "";
    String DPCode = "";
    String DPManageemNo = "";
    String stringSQL = "";

    // 承辦ID
    stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + getUser().toUpperCase().trim() + "'";
    retEip = dbEIP.queryFromPool(stringSQL);
    if (retEip.length > 0) {
      empNo = retEip[0][0];
    }

    // 承辦EMAIL
    stringSQL = "SELECT DP_CODE,PN_EMAIL1,PN_EMAIL2 FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      DPCode = reteMail[0][0];
      if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
        userEmail = reteMail[0][1];
      }
      if (reteMail[0][2] != null && !reteMail[0][2].equals("")) {
        userEmail2 = reteMail[0][2];
      }
    }

    // 科長ID
    stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + DPCode + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      DPManageemNo = reteMail[0][0];
    }

    // 科長MAIL
    stringSQL = "SELECT PN_EMAIL1,PN_EMAIL2 FROM PERSONNEL WHERE PN_EMPNO='" + DPManageemNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      if (reteMail[0][0] != null && !reteMail[0][0].equals("")) {
        DPeMail = reteMail[0][0];
      }
      if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
        DPeMail2 = reteMail[0][1];
      }
    }

    // 部長
    String PNCode = "";
    String PNManageemNo = "";

    stringSQL = "SELECT PN_DEPTCODE FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNCode = reteMail[0][0];

    stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + PNCode + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNManageemNo = reteMail[0][0];

    stringSQL = "SELECT PN_EMAIL1 FROM PERSONNEL WHERE PN_EMPNO='" + PNManageemNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNMail = reteMail[0][0];
  }

  public String getInformation() {
    return "---------------SendMailAction2.defaultValue()----------------";
  }
}
