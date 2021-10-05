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
    // ���e����
    String strProjectID1 = getValue("field2").trim();// �קO�N�X
    String strEDate = getValue("field3").trim();// ���ڤ��
    String strDocNo = getValue("field4").trim();// �s��
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
      String msg = "�@�B���ʲ������T�G<BR><BR>1. ��    �O�G<u>" + strProjectID1 + "</u>&emsp;2. �ɼӧO�G<u>" + strPosition + "</u>&emsp;3. �Ȥ�m�W�G<u>" + strCustomName + "</u>&emsp;4. �I�q����G<u>"
          + strOrderDate + "</u>&emsp;5. ���ڳ����G<u>" + strEDate + "</u><BR><BR>�G�B�ŦX�æ��~���A�˳q���G<BR><BR>" + errMsgText;
      msg = msg.replace("\n", "<BR>");
      String subject = strProjectID1 + "��" + strPosition + "���ʲ�ñ���Ȥ᭷�I���ŵ������G�q��";
      String[] arrayUser = ksUtil.getEMAIL����̦W��(this.getUser(), false);
      String sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, msg, null, "", "text/html");

      System.out.println("sendRS===>" + sendRS);
    }
    
    // ����W��
    if (errMsgText.indexOf("����W��") >= 0) {
      String msg2 = "�@�B���ʲ������T�G<BR><BR>1. ��    �O�G<u>" + strProjectID1 + "</u>&emsp;2. �ɼӧO�G<u>" + strPosition + "</u>&emsp;3. �Ȥ�m�W�G<u>" + strCustomName + "</u>&emsp;4. �I�q����G<u>"
          + strOrderDate + "</u>&emsp;5. ���ڳ����G<u>" + strEDate + "</u><BR><BR>�G�B�ŦX�æ��~���A�˳q���G<BR><BR>�Ȥ�" + strCustomName + "�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
      msg2 = msg2.replace("\n", "<BR>");
      String subject2 = strProjectID1 + "��" + strPosition + "���ʲ�ñ���Ȥ᭷�I���ŵ������G�q��";
      String[] arrayUser2 = ksUtil.getEMAIL����̦W��(this.getUser(), true);
      String sendRS2 = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser2, subject2, msg2, null, "", "text/html");

      System.out.println("sendRS2===>" + sendRS2);
    }
    return value;
  }

  public String getInformation() {
    return "---------------sendMail(mail).defaultValue()----------------";
  }
}
