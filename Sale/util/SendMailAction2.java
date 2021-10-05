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
  
  public Result sendMail080() throws Throwable {
    Result rs = new Result();
    
    projectId = getValue("field2").trim();// �קO�N�X
    String eDate = getValue("field3").trim();// ���ڤ��
    String docNo = getValue("field4").trim();// �s��
    orderNo = ksUtil.getOrderNoByDocNo(docNo);    // �q��s��
    orderDate = ksUtil.getOrderDateByOrderNo(orderNo);  // �I�q���
    positions = ksUtil.getPositions(projectId, orderNo);
    customNames = ksUtil.getCustomNames(projectId, orderNo);
    String errMsgText = getValue("errMsgBoxText").trim();

    String sendRS = "";
    String sendRS2 = "";
    try {
      String subject = projectId + "��" + positions + "���ʲ�ñ���Ȥ᭷�I���ŵ������G�q��";
      if (!"".equals(errMsgText)) {
        String[] arrayUser = ksUtil.getEMAIL����̦W��(this.getUser(), false);
        String desc = "�@�B���ʲ������T�G<BR><BR>1. ��    �O�G<u>" + projectId + "</u>&emsp;2. �ɼӧO�G<u>" + positions + "</u>&emsp;3. �Ȥ�m�W�G<u>" + customNames + "</u>&emsp;4. �I�q����G<u>"
            + orderDate + "</u>&emsp;5. ���ڳ����G<u>" + eDate + "</u><BR><BR>�G�B�ŦX�æ��~���A�˳q���G<BR><BR>" + errMsgText;
        desc = desc.replace("\n", "<BR>");
         sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, desc, null, "", "text/html");
      }
      
      if (errMsgText.indexOf("����W��") >= 0) {
        String[] arrayUser = ksUtil.getEMAIL����̦W��(this.getUser(), true);
        String desc = "�@�B���ʲ������T�G<BR><BR>1. ��    �O�G<u>" + projectId + "</u>&emsp;2. �ɼӧO�G<u>" + positions + "</u>&emsp;3. �Ȥ�m�W�G<u>" + customNames + "</u>&emsp;4. �I�q����G<u>"
                    + orderDate + "</u>&emsp;5. ���ڳ����G<u>" + eDate + "</u><BR><BR>"
                    + "�G�B�ŦX�æ��~���A�˳q���G<BR><BR>" + ksUtil.getAMLDescOne("018").replaceAll("<customName>", customNames).replaceAll("<customTitle>", "�Ȥ�");
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
      String[] arrayUser = ksUtil.getEMAIL����̦W��(getUser(), true);
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
