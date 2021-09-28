/**
 * 2020-02-04 kyle的共用元件
 */
package Farglory.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
   * 被前端呼叫，能自行產生talk物件
   */
  public KUtils() {
    System.err.println("KUtils init 0");
    dbSale = getTalk("Sale");
    dbPW0D = getTalk("pw0d");
    db400 = getTalk("400CRM");
    dbEIP = getTalk("EIP");
    dbEMail = getTalk("eMail");
    dbDoc = getTalk("Doc");
    TalkBean tBean = new TalkBean();
    tBean.setDbSale(dbSale);
    tBean.setDbPw0D(dbPW0D);
    tBean.setDb400CRM(db400);
    tBean.setDbEIP(dbEIP);
    tBean.setDbEMail(dbEMail);
    tBean.setDbDOC(dbDoc);
    this.tBean = tBean;
  }

  /**
   * 被後端呼叫，無法自行產生talk物件，要從呼叫處傳過來
   * 
   * @param tBean talk物件
   */
  public KUtils(TalkBean tBean) {
    System.err.println("KUtils init 1");
    dbSale = tBean.getDbSale();
    dbPW0D = tBean.getDbPw0D();
    db400 = tBean.getDb400CRM();
    dbEIP = tBean.getDbEIP();
    dbEMail = tBean.getDbEMail();
    this.tBean = tBean;
  }

  public TalkBean getTBean() {
    return tBean;
  }

  /**
   * 合併身分證號 & 護照號碼 conpareTo比較，大的放前面
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

  // 判斷購買人中是否有法人
  public boolean isCusCompany(String cusNos) throws Throwable {
    System.out.println(">>> isCusCompany==> cusNos >>>" + cusNos);

    boolean boo = false;
    String[] cusNo = cusNos.split("\n");
    for (int i = 0; i < cusNo.length; i++) {
      String thisCusNo = cusNo[i].trim();
      String firstWord = thisCusNo.substring(0, 1);
      if ((thisCusNo.length() != 10) && !firstWord.matches("[a-zA-Z]+")) {
        // System.out.println(thisCusNo + " 是法人") ;
        boo = true;
        break;
      }
    }
    return boo;
  }

  /**
   * 取得為個人或法人 : N.個人(1) C.法人(2)，外國人為個人
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
   * 取得 郵遞區號 - 縣市 - 鄉鎮
   * 
   * @return
   * @throws Throwable
   */
  public String[] getCityTownZipName(String cityCode, String townCode) throws Throwable {
    String[] rs = new String[3];
    if (StringUtils.isBlank(cityCode)) return rs; // 沒city就不用玩了，直接回家吧

    // city
    String sql = "select CounName FROM City where 1=1 ";
    if (StringUtils.isNotBlank(cityCode)) sql += "and Coun = '" + cityCode + "' ";
    String[][] ret = dbDoc.queryFromPool(sql);
    if (ret.length == 0) return rs;
    rs[1] = ret[0][0].trim();

    // town & zip
    sql = "select TownName, zip FROM Town where 1=1 ";
    if (StringUtils.isNotBlank(townCode)) sql += "and Coun = '" + cityCode + "' and Town = '" + townCode + "' ";
    ret = dbDoc.queryFromPool(sql);
    if (ret.length == 0) return rs;
    rs[2] = ret[0][0].trim();
    rs[0] = ret[0][1].trim();

    return rs;
  }

  /**
   * 取得洗錢態樣
   * 
   * @return
   * @throws Throwable
   */
  public Map getAMLDesc() throws Throwable {
    String sql = "select * from saleRY773 where AMLType = 'AML' order by AMLNo asc";
    String[][] retAML = dbSale.queryFromPool(sql);
    Map mapAMLMsg = new HashMap();
    for (int i = 0; i < retAML.length; i++) {
      String[] retAML1 = retAML[i];
      mapAMLMsg.put(retAML1[1], retAML1[2]);
    }
    return mapAMLMsg;
  }

  /**
   * 行業別 中文 to 代碼
   * 
   * @param majorName
   * @return
   * @throws Throwable
   */
  public String getIndustryCodeByMajorName(String majorName) throws Throwable {
    String sql = "SELECT CZ02,CZ09 FROM PDCZPF WHERE CZ01='INDUSTRY' And CZ09 = '" + majorName + "'";
    String[][] retMajor = db400.queryFromPool(sql);
    String ind = "";
    if (retMajor.length > 0) ind = retMajor[0][0] != null ? retMajor[0][0].trim() : "";

    return ind;
  }

  /**
   * 行業別 代碼 to 中文
   * 
   * @param majorName
   * @return
   * @throws Throwable
   */
  public String getNameByIndCode(String indCode) throws Throwable {
    String sql = "SELECT CZ09 FROM PDCZPF WHERE CZ01='INDUSTRY' And CZ02 = '" + indCode + "'";
    String[][] ret = db400.queryFromPool(sql);
    String code = "";
    if (ret.length > 0) code = ret[0][0] != null ? ret[0][0].trim() : "";

    return code;
  }

  /**
   * 國別 - 代碼 to 中文
   * 
   * @param nationCode
   * @return
   * @throws Throwable
   */
  public String getCountryNameByNationCode(String nationCode) throws Throwable {
    String sql = "SELECT CZ09 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ02='" + nationCode + "' ";
    String[][] ret = db400.queryFromPool(sql);
    String name = "";
    if (ret.length > 0) name = ret[0][0] != null ? ret[0][0].trim() : "";

    return name;
  }

  /**
   * 
   * 用orderNo 取 orderDate
   * 
   * @param projectId
   * @param custNo
   * @return
   * @throws Throwable
   */
  public String getOrderDateByOrderNo(String orderNo) throws Throwable {
    return dbSale.queryFromPool("select top 1 orderDate from Sale05M090 where orderNo = '" + orderNo + "' ")[0][0].trim();
  }

  /**
   * 取得QueryLog
   * 
   * @param projectId
   * @param custNo
   * @return
   * @throws Throwable
   */
  public QueryLogBean getQueryLogByCustNoProjectId(String projectId, String custNo) throws Throwable {
    QueryLogBean bean = null;
    String sql = "select top 1 * from query_log a where a.PROJECT_ID = '" + projectId + "' and a.query_id = '" + custNo + "' order by CREATE_DATE desc , CREATE_TIME desc ";
    String[][] ret = dbPW0D.queryFromPool(sql);
    if (ret.length > 0) {
      bean = new QueryLogBean();
      bean.setQid(ret[0][0].toString().trim());
      bean.setReason(ret[0][1].toString().trim());
      bean.setProjectId(ret[0][2].toString().trim());
      bean.setNationalId(ret[0][3].toString().trim());
      bean.setQueryType(ret[0][4].toString().trim());
      bean.setNtCode(ret[0][5].toString().trim());
      bean.setName(ret[0][6].toString().trim());
      bean.setQueryId(ret[0][7].toString().trim());
      bean.setBirthday(ret[0][8].toString().trim());
      bean.setSex(ret[0][9].toString().trim());
      bean.setJobType(ret[0][10].toString().trim());
      bean.setCity(ret[0][11].toString().trim());
      bean.setTown(ret[0][12].toString().trim());
      bean.setAddress(ret[0][13].toString().trim());
      bean.setbStatus(ret[0][14].toString().trim());
      bean.setcStatus(ret[0][15].toString().trim());
      bean.setrStatus(ret[0][16].toString().trim());
      bean.setStatus(ret[0][17].toString().trim());
      bean.setContents(ret[0][18].toString().trim());
      bean.setPcontents(ret[0][19].toString().trim());
      bean.setEmpNo(ret[0][20].toString().trim());
      bean.setIp4(ret[0][21].toString().trim());
      bean.setCreateDate(ret[0][22].toString().trim());
      bean.setCreateTime(ret[0][23].toString().trim());
      bean.setUpdateDate(ret[0][24].toString().trim());
      bean.setUpdateTime(ret[0][25].toString().trim());
    }

    return bean;
  }

  /**
   * 取得QueryLog by projectId
   * 
   * @param projectId
   * @return Map
   * @throws Throwable
   */
  public Map getMapQueryLogByProjectId(String projectId) throws Throwable {
    String sql = "select * from query_log where a.PROJECT_ID = '" + projectId + "' ";
    String[][] ret = dbPW0D.queryFromPool(sql);

    Map rs = new HashMap();
    for (int i = 0; i < ret.length; i++) {
      QueryLogBean bean = new QueryLogBean();
      bean.setQid(ret[i][0].toString().trim());
      bean.setReason(ret[i][1].toString().trim());
      bean.setProjectId(ret[i][2].toString().trim());
      bean.setNationalId(ret[i][3].toString().trim());
      bean.setQueryType(ret[i][4].toString().trim());
      bean.setNtCode(ret[i][5].toString().trim());
      bean.setName(ret[i][6].toString().trim());
      bean.setQueryId(ret[i][7].toString().trim());
      bean.setBirthday(ret[i][8].toString().trim());
      bean.setSex(ret[i][9].toString().trim());
      bean.setJobType(ret[i][10].toString().trim());
      bean.setCity(ret[i][11].toString().trim());
      bean.setTown(ret[i][12].toString().trim());
      bean.setAddress(ret[i][13].toString().trim());
      bean.setbStatus(ret[i][14].toString().trim());
      bean.setcStatus(ret[i][15].toString().trim());
      bean.setrStatus(ret[i][16].toString().trim());
      bean.setStatus(ret[i][17].toString().trim());
      bean.setContents(ret[i][18].toString().trim());
      bean.setPcontents(ret[i][19].toString().trim());
      bean.setEmpNo(ret[i][20].toString().trim());
      bean.setIp4(ret[i][21].toString().trim());
      bean.setCreateDate(ret[i][22].toString().trim());
      bean.setCreateTime(ret[i][23].toString().trim());
      bean.setUpdateDate(ret[i][24].toString().trim());
      bean.setUpdateTime(ret[i][25].toString().trim());
      rs.put(ret[i][7].toString().trim(), bean);
    }

    return rs;
  }

  /**
   * 取得QueryLog by projectId
   * 
   * @param projectId
   * @return Array
   * @throws Throwable
   */
  public QueryLogBean[] getArrQueryLogByProjectId(String projectId) throws Throwable {
    String sql = "select * from query_log where a.PROJECT_ID = '" + projectId + "' ";
    String[][] ret = dbPW0D.queryFromPool(sql);

    QueryLogBean[] rs = new QueryLogBean[ret.length];
    for (int i = 0; i < ret.length; i++) {
      QueryLogBean bean = new QueryLogBean();
      bean.setQid(ret[i][0].toString().trim());
      bean.setReason(ret[i][1].toString().trim());
      bean.setProjectId(ret[i][2].toString().trim());
      bean.setNationalId(ret[i][3].toString().trim());
      bean.setQueryType(ret[i][4].toString().trim());
      bean.setNtCode(ret[i][5].toString().trim());
      bean.setName(ret[i][6].toString().trim());
      bean.setQueryId(ret[i][7].toString().trim());
      bean.setBirthday(ret[i][8].toString().trim());
      bean.setSex(ret[i][9].toString().trim());
      bean.setJobType(ret[i][10].toString().trim());
      bean.setCity(ret[i][11].toString().trim());
      bean.setTown(ret[i][12].toString().trim());
      bean.setAddress(ret[i][13].toString().trim());
      bean.setbStatus(ret[i][14].toString().trim());
      bean.setcStatus(ret[i][15].toString().trim());
      bean.setrStatus(ret[i][16].toString().trim());
      bean.setStatus(ret[i][17].toString().trim());
      bean.setContents(ret[i][18].toString().trim());
      bean.setPcontents(ret[i][19].toString().trim());
      bean.setEmpNo(ret[i][20].toString().trim());
      bean.setIp4(ret[i][21].toString().trim());
      bean.setCreateDate(ret[i][22].toString().trim());
      bean.setCreateTime(ret[i][23].toString().trim());
      bean.setUpdateDate(ret[i][24].toString().trim());
      bean.setUpdateTime(ret[i][25].toString().trim());
      rs[i] = bean;
    }

    return rs;
  }

  /**
   * 檢核是否為西元年
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
      return "[" + stringErrorFieldName + "] 日期格式錯誤(YYYY/MM/DD)。";
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
      return "[" + stringErrorFieldName + "] 日期格式錯誤(YYYY/MM/DD)。";
    }
    return retDate;
  }

  /**
   * 是否測試服務器
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
   * 取得伺服器IP
   * 
   * @return
   * @throws Throwable
   */
  public String getServerIP() throws Throwable {
    String ip = get("serverIP").toString().trim();
    return ip;
  }

  /**
   * 字串前後補字元
   * 
   * @param src   原始字串
   * @param count 補到幾位
   * @param ch    要補的字元
   * @param FB    前0，後1
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
      return "參數錯誤";
    }
  }

  /**
   * 數字前後補0
   * 
   * @param intSrc 原數字
   * @param count  要補到幾位數
   * @param FB     補在前面F，補在後面B
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
   * 兩日期相減，負號為後面日期較大 格式 yyyy/MM/dd
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

    // 日期相減得到相差的日期
    long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);

    return day;
  }

  /**
   * format 日期格式 yyyyMMdd to yyyy(dash)MM(dash)dd
   * 
   * @param day
   * @param dash
   * @return
   * @throws Throwable
   */
  public String formatACDate(String day, String dash) throws Throwable {
    if (day == null || "".equals(day)) {
      return "傳入日期為空";
    }
    if (day.length() != 8) {
      return "不為AC格式";
    }
    return day.substring(0, 4) + dash + day.substring(4, 6) + dash + day.substring(6, 8);
  }

  public String formatACDate(String day) throws Throwable {
    if (day == null || "".equals(day)) {
      return "傳入日期為空";
    }
    if (day.length() != 8) {
      return "不為AC格式";
    }
    return day.substring(0, 4) + '/' + day.substring(4, 6) + '/' + day.substring(6, 8);
  }

  /**
   * 取得指定日期+-N天之日期
   * 
   * @param dateTime 開始日期
   * @param dash     連接符號 / or -
   * @param days     往前N天，往後-N天
   * @return 結果日期
   */
  public String getDateAfterNDays(String dateTime, String dash, int days) {
    Calendar calendar = Calendar.getInstance();
    String[] dateTimeArray = dateTime.split(dash);
    int year = Integer.parseInt(dateTimeArray[0]);
    int month = Integer.parseInt(dateTimeArray[1]);
    int day = Integer.parseInt(dateTimeArray[2]);
    calendar.set(year, month - 1, day);
    long time = calendar.getTimeInMillis();
    calendar.setTimeInMillis(time + days * 1000 * 60 * 60 * 24);// 用\u7ed9定的 long值\u8bbe置此Calendar的\u5f53前\u65f6\u95f4值
    String newYear = Integer.toString(calendar.get(Calendar.YEAR));
    String newMonth = (calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : Integer.toString((calendar.get(Calendar.MONTH) + 1));
    String newDay = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

    return newYear + "/" + newMonth + "/" + newDay;
  }

  /**
   * String[]中 gen出符合SQL IN () 的語法
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
   * 謎之密碼
   * 
   * @param keyCode 有其父必有犬子 u.3fu6zj41u4u.3fm03y3
   * @return
   * @throws Throwable
   */
  public boolean chkSQLpws(String keyCode) throws Throwable {
    System.out.println("util:" + keyCode);
    if (StringUtils.equals(keyCode, "u3fu6zj41u4u3fm03y3")) return true;
    return false;
  }

}
