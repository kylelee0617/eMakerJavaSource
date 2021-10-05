package Sale.Sale05M080;

import Farglory.util.KSqlUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class SendMail080old extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    talk dbSale = getTalk("Sale");
    talk dbEIP = getTalk("EIP");
    talk dbEMail = getTalk("eMail");
    String stringSQL = "";
    String userNo = getUser().toUpperCase().trim();
    String empNo = "";
    String userEmail = "";
    String userEmail2 = "";
    String DPCode = "";
    String DPManageemNo = "";
    String DPeMail = "";
    String DPeMail2 = "";
    String[][] retEip = null;
    String[][] reteMail = null;

    stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + userNo + "'";
    retEip = dbEIP.queryFromPool(stringSQL);
    if (retEip.length > 0) {
      empNo = retEip[0][0];
    }
    /////////////////
    System.out.println("userNo===>" + userNo);
    System.out.println("empNo===>" + empNo);
    ////////////////
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
    /////////////////////////////////////////
    System.out.println("DPCode===>" + DPCode);
    System.out.println("userEmail===>" + userEmail);
    System.out.println("userEmail2===>" + userEmail2);
    /////////////////////////////////////////////////
    stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + DPCode + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      DPManageemNo = reteMail[0][0];
    }
    /////////////////////////////////////////
    System.out.println("DPManageemNo===>" + DPManageemNo);
    /////////////////////////////////////////////////
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
    ////////////////////////////////////
    System.out.println("DPeMail===>" + DPeMail);
    System.out.println("DPeMail2===>" + DPeMail2);
    /////////////////////////////////////////////////////////
    String PNCode = "";
    String PNManageemNo = "";
    String PNMail = "";

    stringSQL = "SELECT PN_DEPTCODE FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNCode = reteMail[0][0];
    System.out.println("PNCode===>" + PNCode);
    stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + PNCode + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNManageemNo = reteMail[0][0];
    System.out.println("PNManageemNo===>" + PNManageemNo);
    stringSQL = "SELECT PN_EMAIL1 FROM PERSONNEL WHERE PN_EMPNO='" + PNManageemNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNMail = reteMail[0][0];
    System.out.println("PNMail===>" + PNMail);
    //////////////////////////////////////////////////////////////////

    // ���e����
    String strProjectID1 = getValue("field2").trim();// �קO�N�X
    String strEDate = getValue("field3").trim();// ���ڤ��
    String strDocNo = getValue("field4").trim();// �s��
    String strOrderNo = "";
    String strPosition = "";
    String strCustomName = "";
    String strOrderDate = "";
    stringSQL = "SELECT OrderNo FROM Sale05M086 WHERE DocNo = '" + strDocNo + "' ";
    reteMail = dbSale.queryFromPool(stringSQL);
    strOrderNo = reteMail[0][0];
    stringSQL = "SELECT OrderDate FROM Sale05M090 WHERE OrderNo = '" + strOrderNo + "' ";
    reteMail = dbSale.queryFromPool(stringSQL);
    strOrderDate = reteMail[0][0];
    stringSQL = "SELECT CustomName FROM Sale05M091 WHERE OrderNo = '" + strOrderNo + "' ";
    reteMail = dbSale.queryFromPool(stringSQL);
    for (int g = 0; g < reteMail.length; g++) {
      if ("".equals(strCustomName)) {
        strCustomName = reteMail[g][0].trim();
      } else {
        strCustomName = strCustomName + "�B" + reteMail[g][0].trim();
      }
    }
    stringSQL = "SELECT Position FROM Sale05M092 WHERE OrderNo = '" + strOrderNo + "' ORDER BY RecordNo ";
    reteMail = dbSale.queryFromPool(stringSQL);
    strPosition = reteMail[0][0];
    ////////////////////////////////////
    System.out.println("strProjectID1===>" + strProjectID1);
    System.out.println("strEDate===>" + strEDate);
    System.out.println("strDocNo===>" + strDocNo);
    System.out.println("strOrderNo===>" + strOrderNo);
    System.out.println("strOrderDate===>" + strOrderDate);
    System.out.println("strCustomName===>" + strCustomName);
    System.out.println("strPosition===>" + strPosition);
    /////////////////////////////////////////////////////////
    // send email
    String errMsgText = getValue("errMsgBoxText").trim();
    if (!"".equals(errMsgText)) {
      // String msg
      // ="�קO�N�X�G"+strProjectID1+"\n�ɼӧO�G"+strPosition+"\n�q��m�W�G"+strCustomName+"\n�I�q����G"+strOrderDate+"\n���ڤ���G"+strEDate+"\n�æ��~���˺A�G\n"+
      // errMsgText;
      // errMsg����\n
      String msg = "�@�B���ʲ������T�G<BR><BR>1. ��    �O�G<u>" + strProjectID1 + "</u>&emsp;2. �ɼӧO�G<u>" + strPosition + "</u>&emsp;3. �Ȥ�m�W�G<u>" + strCustomName + "</u>&emsp;4. �I�q����G<u>"
          + strOrderDate + "</u>&emsp;5. ���ڳ����G<u>" + strEDate + "</u><BR><BR>�G�B�ŦX�æ��~���A�˳q���G<BR><BR>" + errMsgText;
      msg = msg.replace("\n", "<BR>");
      String subject = strProjectID1 + "��" + strPosition + "���ʲ�ñ���Ȥ᭷�I���ŵ������G�q��";
      String[] arrayUser = { "Kyle_Lee@fglife.com.tw", userEmail };
      String sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, msg, null, "", "text/html");

      System.out.println("sendRS===>" + sendRS);
    }
    // ����W��
    if (errMsgText.indexOf("����W��") >= 0) {
      // String msg2
      // ="�קO�N�X�G"+strProjectID1+"\n�ɼӧO�G"+strPosition+"\n�q��m�W�G"+strCustomName+"\n�I�q����G"+strOrderDate+"\n���ڤ���G"+strEDate+"\n�æ��~���˺A�G\n�Ȥᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC";
      String msg2 = "�@�B���ʲ������T�G<BR><BR>1. ��    �O�G<u>" + strProjectID1 + "</u>&emsp;2. �ɼӧO�G<u>" + strPosition + "</u>&emsp;3. �Ȥ�m�W�G<u>" + strCustomName + "</u>&emsp;4. �I�q����G<u>"
          + strOrderDate + "</u>&emsp;5. ���ڳ����G<u>" + strEDate + "</u><BR><BR>�G�B�ŦX�æ��~���A�˳q���G<BR><BR>�Ȥ�" + strCustomName + "�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
      msg2 = msg2.replace("\n", "<BR>");
      String subject2 = strProjectID1 + "��" + strPosition + "���ʲ�ñ���Ȥ᭷�I���ŵ������G�q��";
      String[] arrayUser2 = { "Kyle_Lee@fglife.com.tw", DPeMail, PNMail };
      String sendRS2 = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser2, subject2, msg2, null, "", "text/html");

      System.out.println("sendRS2===>" + sendRS2);
    }
    return value;
  }

  public String getInformation() {
    return "---------------sendMail(mail).defaultValue()----------------";
  }
}
