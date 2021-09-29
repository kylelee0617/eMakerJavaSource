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
  String testRemark = "(����)"; // �b�������ҭn�[�����զr��
  String testPGMail = "Kyle_Lee@fglife.com.tw"; // �������ұH�e����mail
  
  //�e����
  String projectId = ""; // �קO�N�X
  String orderDate = ""; // �I�q���
  String orderNo = ""; // �q��s��
  String positions = "";
  String customNames = "";
  String errMsgText = "";

  public SendMailAction2() {
    // 20200508 kyle Add �ھڦ��A���O�_�[���հT��
    isTest = "PROD".equals(get("serverType").toString().trim()) ? false : true;
    System.out.println(">>>����:" + testRemark);
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
    
    // �����\��
    String funcName = value.trim();
    if (StringUtils.contains(funcName, "�ʫ��ҩ���")) {
      //�ȮɨS��
    } else if (StringUtils.contains(funcName, "����")) {
      //�ȵL��
    } else if (StringUtils.contains(funcName, "�h��")) {
      this.sendMailRule23();
    }

    System.out.println("==============sendMailAction2 END====================================");
    return value;
  }

  
  public Result sendMailRule23() throws Throwable {
    Result rs = new Result();
    projectId = getValue("field1").trim();  // �קO�N�X
    orderNo = getValue("field3").trim();    // �q��s��
    orderDate = ksUtil.getOrderDateByOrderNo(orderNo);  // �I�q���
    positions = ksUtil.getPositions(projectId, orderNo);
    customNames = ksUtil.getCustomNames(projectId, orderNo);
    String amlNo = "23";  //�~���A�˽s��

    try {
      String desc = "�קO�N�X�G" + projectId + "<BR>�ɼӧO�G" + positions + "<BR>�q��m�W�G" + customNames + "<BR>�I�q����G" + orderDate 
                  + "<BR>ĵ�ܰT�� : " + ksUtil.getAMLDescOne(amlNo).replaceAll("<customName>", customNames).replaceAll("<customTitle>", "�Ȥ�");
      String subject = "�h��  �קO�G" + projectId + "  �ɼӧO�G" + positions + "  �Ȥ�~���A�˺ʱ��wĵ" + testRemark;
      String[] arrayUser = ksUtil.getEMAIL����̦W��(getUser(), isTest);
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

    // �ӿ�ID
    stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + getUser().toUpperCase().trim() + "'";
    retEip = dbEIP.queryFromPool(stringSQL);
    if (retEip.length > 0) {
      empNo = retEip[0][0];
    }

    // �ӿ�EMAIL
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

    // ���ID
    stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + DPCode + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      DPManageemNo = reteMail[0][0];
    }

    // ���MAIL
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

    // ����
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
