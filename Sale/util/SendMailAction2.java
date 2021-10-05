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
  
  public Result sendMail080() throws Throwable {
    Result rs = new Result();
    
    projectId = getValue("field2").trim();// 案別代碼
    String eDate = getValue("field3").trim();// 收款日期
    String docNo = getValue("field4").trim();// 編號
    orderNo = ksUtil.getOrderNoByDocNo(docNo);    // 訂單編號
    orderDate = ksUtil.getOrderDateByOrderNo(orderNo);  // 付訂日期
    positions = ksUtil.getPositions(projectId, orderNo);
    customNames = ksUtil.getCustomNames(projectId, orderNo);
    String errMsgText = getValue("errMsgBoxText").trim();

    String sendRS = "";
    String sendRS2 = "";
    try {
      String subject = projectId + "案" + positions + "不動產簽約客戶風險等級評估結果通知";
      if (!"".equals(errMsgText)) {
        String[] arrayUser = ksUtil.getEMAIL收件者名單(this.getUser(), false);
        String desc = "一、不動產交易資訊：<BR><BR>1. 案    別：<u>" + projectId + "</u>&emsp;2. 棟樓別：<u>" + positions + "</u>&emsp;3. 客戶姓名：<u>" + customNames + "</u>&emsp;4. 付訂日期：<u>"
            + orderDate + "</u>&emsp;5. 收款單日期：<u>" + eDate + "</u><BR><BR>二、符合疑似洗錢態樣通知：<BR><BR>" + errMsgText;
        desc = desc.replace("\n", "<BR>");
         sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, desc, null, "", "text/html");
      }
      
      if (errMsgText.indexOf("制裁名單") >= 0) {
        String[] arrayUser = ksUtil.getEMAIL收件者名單(this.getUser(), true);
        String desc = "一、不動產交易資訊：<BR><BR>1. 案    別：<u>" + projectId + "</u>&emsp;2. 棟樓別：<u>" + positions + "</u>&emsp;3. 客戶姓名：<u>" + customNames + "</u>&emsp;4. 付訂日期：<u>"
                    + orderDate + "</u>&emsp;5. 收款單日期：<u>" + eDate + "</u><BR><BR>"
                    + "二、符合疑似洗錢態樣通知：<BR><BR>" + ksUtil.getAMLDescOne("018").replaceAll("<customName>", customNames).replaceAll("<customTitle>", "客戶");
        desc = desc.replace("\n", "<BR>");
        sendRS2 = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, desc, null, "", "text/html");
      }
      
      if ( StringUtils.isNotBlank(sendRS) || StringUtils.isNotBlank(sendRS2)) {
        rs.setReturnCode(Integer.parseInt(ResultStatus.SENDEMAILERROR[0]));
        rs.setReturnMsg(sendRS + sendRS2);
        rs.setRsStatus(ResultStatus.SENDEMAILERROR);
        messagebox(sendRS + sendRS2);
      }

    } catch (Exception ex) {
      rs.setRsStatus(ResultStatus.SENDEMAILERROR);
      rs.setExp(ex);
    }
    
    System.out.println(">>>send mail RS:\n" + rs.getReturnMsg());

    return rs;
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
      String[] arrayUser = ksUtil.getEMAIL收件者名單(getUser(), true);
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

  public String getInformation() {
    return "---------------SendMailAction2.defaultValue()----------------";
  }
}
