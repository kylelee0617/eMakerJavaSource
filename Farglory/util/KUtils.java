/**
 * 2020-02-04 kyle���@�Τ���
 */
package Farglory.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;

public class KUtils extends bproc {
  private talk dbSale = null;
  private talk dbPW0D = null;
  private talk db400 = null;
  private talk dbEIP = null;
  private talk dbEMail = null;
  private talk dbDoc = null;
  private TalkBean tBean = null;

  public String getDefaultValue(String value) throws Throwable {
    return value;
  }

  /**
   * �Q�e�ݩI�s�A��ۦ沣��talk����
   */
  public KUtils() {
    System.err.println("KUtils init 0");
//    dbSale = getTalk("Sale");
//    dbPW0D = getTalk("pw0d");
//    db400 = getTalk("400CRM");
//    dbEIP = getTalk("EIP");
//    dbEMail = getTalk("eMail");
//    dbDoc = getTalk("Doc");
//    TalkBean tBean = new TalkBean();
//    tBean.setDbSale(dbSale);
//    tBean.setDbPw0D(dbPW0D);
//    tBean.setDb400CRM(db400);
//    tBean.setDbEIP(dbEIP);
//    tBean.setDbEMail(dbEMail);
//    tBean.setDbDOC(dbDoc);
//    this.tBean = tBean;
  }

  /**
   * �Q��ݩI�s�A�L�k�ۦ沣��talk����A�n�q�I�s�B�ǹL��
   * 
   * @param tBean talk����
   */
  public KUtils(TalkBean tBean) {
    System.err.println("KUtils init 1");
//    dbSale = tBean.getDbSale();
//    dbPW0D = tBean.getDbPw0D();
//    db400 = tBean.getDb400CRM();
//    dbEIP = tBean.getDbEIP();
//    dbEMail = tBean.getDbEMail();
//    this.tBean = tBean;
  }

  public TalkBean getTBean() {
    return tBean;
  }
  
  /**
   * log info
   * @param o
   */
  public static void info(Object o) {
    System.out.print(">>>info...");
    System.out.println(o);
  }

  /**
   * �X�֨����Ҹ� & �@�Ӹ��X compareTo����A�j����e��
   * 
   * @param cusNo1
   * @param custNo2
   * @return
   * @throws Throwable
   */
  public String getCustNo3(String custNo, String engNo) throws Throwable {
    String custNo3 = "";
    if (custNo.compareTo(engNo) < 0) {
      custNo3 = engNo + custNo;
    } else {
      custNo3 = custNo + engNo;
    }
    return custNo3;
  }

  // �P�_�ʶR�H���O�_���k�H
  public boolean isCusCompany(String cusNos) throws Throwable {
    System.out.println(">>> isCusCompany==> cusNos >>>" + cusNos);

    boolean boo = false;
    String[] cusNo = cusNos.split("\n");
    for (int i = 0; i < cusNo.length; i++) {
      String thisCusNo = cusNo[i].trim();
      String firstWord = thisCusNo.substring(0, 1);
      if ((thisCusNo.length() != 10) && !firstWord.matches("[a-zA-Z]+")) {
        // System.out.println(thisCusNo + " �O�k�H") ;
        boo = true;
        break;
      }
    }
    return boo;
  }

  /**
   * ���o���ӤH�Ϊk�H : N.�ӤH(1) C.�k�H(2)�A�~��H���ӤH
   * 
   * @param id
   * @return
   */
  public String getUserType(String id) {
    String sp1 = id.substring(0, 1);
    String userType = "N";
    if (StringUtils.trimToEmpty(id).length() == 8 && !(sp1.matches("[A-Z]+"))) userType = "C";

    return userType;
  }

  /**
   * �ˮ֬O�_���褸�~
   * 
   * @param value
   * @param stringErrorFieldName
   * @return
   * @throws Throwable
   */
  public String getDateAC(String value, String stringErrorFieldName) throws Throwable {
    int intIndexStart = 0;
    int intIndexEnd = value.indexOf("/");
    String stringTmp = "";
    Vector retVector = new Vector();
    int i = 0;
    while (intIndexEnd != -1) {
      stringTmp = value.substring(intIndexStart, intIndexEnd);
      if (!"".equals(stringTmp)) retVector.add(stringTmp.trim());
      intIndexStart = intIndexEnd + "/".length();
      intIndexEnd = value.indexOf("/", intIndexStart);
      i++;
    }
    stringTmp = value.substring(intIndexStart, value.length());
    if (!"".equals(stringTmp)) retVector.add(stringTmp.trim());
    boolean booleanFlow = (retVector.size() != 3);
    booleanFlow = booleanFlow && ((retVector.size() == 1 && value.length() != 8) || (retVector.size() != 1));
    if (booleanFlow) {
      return "[" + stringErrorFieldName + "] ����榡���~(YYYY/MM/DD)�C";
    }
    stringTmp = "";
    booleanFlow = true;
    if (((String) retVector.get(0)).length() < 4) stringTmp = "0" + (String) retVector.get(0);
    else
      stringTmp = (String) retVector.get(0);
    for (int intRetVector = 1; intRetVector < retVector.size(); intRetVector++) {
      if (((String) retVector.get(intRetVector)).length() == 1) {
        stringTmp += "/0" + (String) retVector.get(intRetVector);
      } else {
        stringTmp += "/" + (String) retVector.get(intRetVector);
      }
    }
    if (stringTmp.length() == 8) {
      stringTmp = stringTmp.substring(0, 4) + "/" + stringTmp.substring(4, 6) + "/" + stringTmp.substring(6, 8);
    }
    String retDate = stringTmp;
    stringTmp = stringTmp.substring(0, 4) + stringTmp.substring(5, 7) + stringTmp.substring(8, 10);
    if (!check.isACDay(stringTmp)) {
      return "[" + stringErrorFieldName + "] ����榡���~(YYYY/MM/DD)�C";
    }
    return retDate;
  }

  /**
   * �O�_���ժA�Ⱦ�
   * 
   * @return
   * @throws Throwable
   */
  public boolean isTest() throws Throwable {
    String serverIP = get("serverIP").toString().trim();
    boolean isTest = serverIP.contains("172.16.") ? false : true;
    return isTest;
  }

  /**
   * ���o���A��IP
   * 
   * @return
   * @throws Throwable
   */
  public String getServerIP() throws Throwable {
    String ip = get("serverIP").toString().trim();
    return ip;
  }

  /**
   * �r��e��ɦr��
   * 
   * @param src   ��l�r��
   * @param count �ɨ�X��
   * @param ch    �n�ɪ��r��
   * @param FB    �e0�A��1
   * @return String
   * @throws Throwable
   */
  public String addWhat(String src, int count, String ch, int FB) throws Throwable {
    String strPlus = "";
    for (int ii = 0; ii < count - (src.length()); ii++) {
      strPlus += ch;
    }

    if (FB == 0) {
      return strPlus + src;
    } else if (FB == 1) {
      return src + strPlus;
    } else {
      return "�Ѽƿ��~";
    }
  }

  /**
   * �Ʀr�e���0
   * 
   * @param intSrc ��Ʀr
   * @param count  �n�ɨ�X���
   * @param FB     �ɦb�e��F�A�ɦb�᭱B
   * @return
   * @throws Throwable
   */
  public String add0(int intSrc, int count, String FB) throws Throwable {
    String strSrc = Integer.toString(intSrc);
    String strPlus = "";
    for (int ii = 0; ii < count - (strSrc.length()); ii++) {
      strPlus += "0";
    }

    if ("F".equals(FB)) {
      return strPlus + strSrc;
    } else {
      return strSrc + strPlus;
    }

  }

  /**
   * �����۴�A�t�����᭱������j �榡 yyyy/MM/dd
   * 
   * @param day1
   * @param day2
   * @return (day1 - day2)
   * @throws Throwable
   */
  public long subACDaysRDay(String day1, String day2) throws Throwable {
    if ("".equals(day1) || "".equals(day2)) {
      return 0;
    }
    if (day1.length() != 10 || day2.length() != 10) {
      return 0;
    }

    Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(day1);
    Date date2 = new SimpleDateFormat("yyyy/MM/dd").parse(day2);

//    System.out.println("day1>>>" + day1);
//    System.out.println("day2>>>" + day2);
//    System.out.println("Date1>>>" + date1);
//    System.out.println("Date2>>>" + date2);
//    System.out.println("time>>>" + (date1.getTime()-date2.getTime()) );

    // ����۴�o��ۮt�����
    long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);

    return day;
  }

  /**
   * format ����榡 yyyyMMdd to yyyy(dash)MM(dash)dd
   * 
   * @param day
   * @param dash
   * @return
   * @throws Throwable
   */
  public String formatACDate(String day, String dash) throws Throwable {
    if (day == null || "".equals(day)) {
      return "�ǤJ�������";
    }
    if (day.length() != 8) {
      return "����AC�榡";
    }
    return day.substring(0, 4) + dash + day.substring(4, 6) + dash + day.substring(6, 8);
  }

  public String formatACDate(String day) throws Throwable {
    if (day == null || "".equals(day)) {
      return "�ǤJ�������";
    }
    if (day.length() != 8) {
      return "����AC�榡";
    }
    return day.substring(0, 4) + '/' + day.substring(4, 6) + '/' + day.substring(6, 8);
  }

  /**
   * ���o���w���+-N�Ѥ����
   * 
   * @param dateTime �}�l���
   * @param dash     �s���Ÿ� / or -
   * @param days     ���eN�ѡA����-N��
   * @return ���G���
   */
  public String getDateAfterNDays(String dateTime, String dash, int days) {
    Calendar calendar = Calendar.getInstance();
    String[] dateTimeArray = dateTime.split(dash);
    int year = Integer.parseInt(dateTimeArray[0]);
    int month = Integer.parseInt(dateTimeArray[1]);
    int day = Integer.parseInt(dateTimeArray[2]);
    calendar.set(year, month - 1, day);
    long time = calendar.getTimeInMillis();
    calendar.setTimeInMillis(time + days * 1000 * 60 * 60 * 24);// ��\u7ed9�w�� long��\u8bbe�m��Calendar��\u5f53�e\u65f6\u95f4��
    String newYear = Integer.toString(calendar.get(Calendar.YEAR));
    String newMonth = (calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : Integer.toString((calendar.get(Calendar.MONTH) + 1));
    String newDay = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

    return newYear + "/" + newMonth + "/" + newDay;
  }

  /**
   * String[]�� gen�X�ŦXSQL IN () ���y�k
   * 
   * @param srcStr
   * @return '' , '' , '' , '' ....
   * @throws Throwable
   */
  public String genQueryInString(String[] srcStr) throws Throwable {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < srcStr.length; i++) {
      if (i != 0) {
        sb.append(",");
      }
      sb.append("'").append(srcStr[i].trim()).append("'");
    }

    return sb.toString();
  }

  /**
   * �����K�X
   * 
   * @param keyCode ������������l u.3fu6zj41u4u.3fm03y3
   * @return
   * @throws Throwable
   */
  public boolean chkSQLpws(String keyCode) throws Throwable {
    System.out.println("util:" + keyCode);
    if (StringUtils.equals(keyCode, "u3fu6zj41u4u3fm03y3")) return true;
    return false;
  }

}
