/**
 * 2020-02-04 kyle的共用元件
 */
package Farglory.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import Farglory.aml.RiskCustomBean;
import Farglory.util.pojo.BenBean;
import Farglory.util.pojo.SaleRY773;
import jcx.db.talk;
import jcx.jform.bproc;

public class KSqlUtils extends bproc {
  private talk dbSale = null;
  private talk dbPW0D = null;
  private talk db400 = null;
  private talk dbEIP = null;
  private talk dbEMail = null;
  private talk dbDoc = null;
  private TalkBean tBean = null;
  KUtils kUtil;

  public String getDefaultValue(String value) throws Throwable {
    return value;
  }

  /**
   * 被前端呼叫，能自行產生talk物件
   */
  public KSqlUtils() {
    System.err.println("KSqlUtils init 0");
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
    kUtil = new KUtils();
  }

  /**
   * 被後端呼叫，無法自行產生talk物件，要從呼叫處傳過來
   * 
   * @param tBean talk物件
   */
  public KSqlUtils(TalkBean tBean) {
    System.err.println("KSqlUtils init 1");
    dbSale = tBean.getDbSale();
    dbPW0D = tBean.getDbPw0D();
    db400 = tBean.getDb400CRM();
    dbEIP = tBean.getDbEIP();
    dbEMail = tBean.getDbEMail();
    this.tBean = tBean;
    kUtil = new KUtils(tBean);
  }

  public TalkBean getTBean() {
    return tBean;
  }
  
  
  /**
   * 是否控管名單
   * 
   * @param cBean
   * @return
   * @throws Throwable
   */
  public boolean chkIsCStatus(String custNo, String custName, String birthday) throws Throwable {
    //設定控管名單編號
    //1.7.1國內政治敏感人物
    //1.8.1制裁名單
    //1.9.1洗錢防制黑名單
    //2.0.1重大犯罪
    //2.1.1疑似洗錢通報對象－外部
    //2.2.1疑似洗錢通報對象－內部
    String[] CSTATUSNO = {"X171","X181","X191","X201","X211","X221"};
    StringBuilder sb1 = new StringBuilder();
    for(int i=0 ; i<CSTATUSNO.length ; i++) {
      if(i > 0) sb1.append(",");
      sb1.append("'").append(CSTATUSNO[i].trim()).append("'");
    }
    
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    String today = format.format(date);
    System.out.println(">>>chkIsCStatus today:" + today);

    String sql = "SELECT A.ISREMOVE,C.CONTROLCLASSIFICATIONCODE,TO_CHAR(C.REMOVEDDATE,'YYYY/MM/DD') AS REMOVEDDATE,L.CONTROLCLASSIFICATIONNAME " 
               + "FROM CRCLNAPF A, CRCLNCPF C, CRCLCLPF L "
               + "WHERE A.CONTROLLISTNAMECODE=C.CONTROLLISTNAMECODE AND C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE " 
               + "AND (A.CUSTOMERID = '"+custNo+"' OR (A.CUSTOMERNAME LIKE '%"+custName+"%' AND A.BIRTHDAY = '" + birthday.replaceAll("/", "-") + "')) "
               + "AND C.CONTROLCLASSIFICATIONCODE in ("+sb1.toString()+") AND A.ISREMOVE = 'N' AND TO_CHAR(C.REMOVEDDATE,'YYYY/MM/DD') >= '"+today+"' ";
    String[][] ret = db400.queryFromPool(sql);
    if(ret != null && ret.length > 0) return true;
    
    return false;
  }


  /**
   * 取得EMAIL收件者名單
   * 
   * @param eMakerUserNo  建設員編
   * @param hasManager    包含主管?
   * @return
   * @throws Throwable
   */
  public String[] getEMAIL收件者名單(String eMakerUserNo, boolean hasManager) throws Throwable {
    String[] rs = new String[4];
    String[][] retEip = null;
    String[][] reteMail = null;
    String empNo = "";
    String stringSQL = "";

    rs[0] = "Kyle_Lee@fglife.com.tw";

    // 承辦ID
    stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + eMakerUserNo + "'";
    retEip = dbEIP.queryFromPool(stringSQL);
    if (retEip.length > 0) {
      empNo = retEip[0][0];
    }

    // 承辦EMAIL
    String DPCode = "";
    stringSQL = "SELECT DP_CODE,PN_EMAIL1,PN_EMAIL2 FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      DPCode = reteMail[0][0];
      if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
        rs[1] = reteMail[0][1].trim();
      }
      if (reteMail[0][2] != null && !reteMail[0][2].equals("")) {
        // userEmail2 = reteMail[0][2];
      }
    }

    if (hasManager) {
      // 科長ID
      String DPManageemNo = "";
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
          rs[2] = reteMail[0][0].trim();
        }
        if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
          // DPeMail2 = reteMail[0][1];
        }
      }

      // 部長
      String PNCode = "";
      stringSQL = "SELECT PN_DEPTCODE FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
      reteMail = dbEMail.queryFromPool(stringSQL);
      PNCode = reteMail[0][0];

      String PNManageemNo = "";
      stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + PNCode + "'";
      reteMail = dbEMail.queryFromPool(stringSQL);
      PNManageemNo = reteMail[0][0];

      stringSQL = "SELECT PN_EMAIL1 FROM PERSONNEL WHERE PN_EMPNO='" + PNManageemNo + "'";
      reteMail = dbEMail.queryFromPool(stringSQL);
      rs[3] = reteMail[0][0].trim();
    }

    return rs;
  }

  /**
   * 查詢指定條件下所有的棟樓別(過濾退戶)，頓號分隔
   * 
   * @param projectId
   * @param orderNo
   * @return
   * @throws Throwable
   */
  public String getPositions(String projectId, String orderNo) throws Throwable {
    String rs = "<NONE>";
    String sql = "select ISNULL( STUFF( (SELECT '、' + aa.position FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'D' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別 "
        + "from sale05m090 a where 1=1 ";
    if (StringUtils.isNotBlank(projectId)) sql += "and projectId1 = '" + projectId + "' ";
    if (StringUtils.isNotBlank(orderNo)) sql += "and orderNo = '" + orderNo + "' ";
    String[][] ret = this.dbSale.queryFromPool(sql);
    if (ret.length > 0) rs = ret[0][0].trim();

    return rs;
  }

  /**
   * 查詢指定條件下所有的主要客戶(過濾被換名)，頓號分隔
   * 
   * @param projectId
   * @param orderNo
   * @return
   * @throws Throwable
   */
  public String getCustomNames(String projectId, String orderNo) throws Throwable {
    String rs = "<NONE>";
    String sql = "select ISNULL( STUFF( (SELECT '、' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 客戶姓名 "
        + "from sale05m090 a " + "where 1=1 ";
    if (StringUtils.isNotBlank(projectId)) sql += "and projectId1 = '" + projectId + "' ";
    if (StringUtils.isNotBlank(orderNo)) sql += "and orderNo = '" + orderNo + "' ";
    String[][] ret = this.dbSale.queryFromPool(sql);
    if (ret.length > 0) rs = ret[0][0].trim();

    return rs;
  }

  /**
   * 紀錄使用者軌跡
   * 
   * @param funcName   getFunctionName(功能)
   * @param recordType (項目: OO資料)
   * @param emakerUser FG工號
   * @param logDesc    LOG內容
   * @return
   * @throws Throwable
   */
  public boolean setSaleLog(String funcName, String recordType, String emakerUser, String logDesc) throws Throwable {
    SaleLogBean bean = new SaleLogBean();
    bean.setFuncName(funcName);
    bean.setRecordType(recordType);
    bean.setEmpNo(this.getEmpNo(emakerUser));
    bean.setFgEmpNo(emakerUser);
    bean.setLogDesc(logDesc);

    SimpleDateFormat nowsdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSSSSS");
    bean.setUpdateDate(nowsdf.format(new Date()));

    return this.insSaleLog(bean);
  }

  /**
   * 寫LOG
   * 
   * @param bean
   * @return
   * @throws Throwable
   */
  public boolean insSaleLog(SaleLogBean bean) throws Throwable {
    boolean rs = false;
    String sql = "INSERT INTO SaleLog (FuncName, RecordType, EmpNo, FgEmpNo, LogDesc, UpdateDate, Type1, Type2, Type3) " + "VALUES " + "('" + bean.getFuncName() + "', '"
        + bean.getRecordType() + "', '" + bean.getEmpNo() + "', '" + bean.getFgEmpNo() + "', " + "N'" + bean.getLogDesc() + "', '" + bean.getUpdateDate() + "', '" + bean.getType1()
        + "', '" + bean.getType2() + "', '" + bean.getType3() + "') ";
    try {
      this.dbSale.execFromPool(sql);
      rs = true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return rs;
    }

    return rs;
  }

  /**
   * 取 empNo
   * 
   * @param eMakerUserNo eMaker的虛擬員編
   * @return 人壽員編
   * @throws Throwable
   */
  public String getEmpNo(String eMakerUserNo) throws Throwable {
    String empNo = "FGLife";
    String sql = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + eMakerUserNo + "'";
    String[][] dbData = dbEIP.queryFromPool(sql);
    if (dbData.length > 0) empNo = dbData[0][0].trim();
    System.out.println(">>>EmpNo :" + empNo);

    return empNo;
  }

  /**
   * 取得客戶
   * 
   * @param orderNo   使用編號
   * @return
   * @throws Throwable
   */
  public String[][] getCustom4Sale093Query(String orderNo) throws Throwable {
    String sql = "SELECT OrderNo, RecordNo, '', Nationality, CountryName2, EngNo, EngName, CountryName, CustomNo,  CustomName, Percentage, "
        + "Birthday, MajorName, PositionName, ZIP, City, Town, Address, Cellphone, Tel, Tel2, eMail, IsBlackList, IsControlList, IsLinked "
        + "FROM Sale05M091 "
        + "WHERE OrderNo='"+ orderNo + "' ";
    sql += "AND ISNULL(StatusCd, '') != 'C' ";
    String[][] retCustom = dbSale.queryFromPool(sql);

    return retCustom;
  }

  /**
   * 取得客戶
   * 
   * @param projectId 案別
   * @param orderNo   使用編號
   * @param startDate 訂單日 START
   * @param endDate   訂單日 END
   * @param isWork    查詢有效客戶? true:僅查詢有效客戶 , false:查詢所有客戶含換名
   * @return
   * @throws Throwable
   */
  public RiskCustomBean[] getCustoms(String projectId, String orderNo, String startDate, String endDate, boolean isWork) throws Throwable {
    String sql = "select DISTINCT "
        + "b.OrderNo, b.RecordNo, b.CustomNo, b.CustomName, b.Percentage, b.ZIP, b.City, b.Town, b.Address, b.Cellphone, b.Tel, b.Tel2, b.eMail, b.auditorship, "
        + "b.IsLinked, b.IsControlList, b.IsBlackList, b.TrxDate, b.StatusCd, b.Nationality, b.TrxDateDown, b.PositionName, b.Birthday, b.MajorName, b.CountryName, b.RiskValue, "
        + "b.IndustryCode, b.CountryName2, b.EngName, b.EngNo, a.ProjectID1, a.orderDate "
        + "from sale05m090 a "
        + "left join Sale05M091 b on a.OrderNo = b.OrderNo " 
        + "left join Sale05M094 c on a.OrderNo = c.OrderNo " 
        + "left join Sale05M092 d on a.OrderNo = d.OrderNo " 
        + "where 1=1 ";
    if (StringUtils.isNotBlank(orderNo)) sql += "and b.OrderNo='" + orderNo + "' ";
    if (StringUtils.isNotBlank(projectId)) sql += "and a.ProjectID1 ='" + projectId + "' ";
    if (StringUtils.isNotBlank(startDate)) sql += "AND a.orderDate >= '"+startDate+"' ";
    if (StringUtils.isNotBlank(endDate)) sql += "AND a.orderDate <= '" + endDate + "' ";
    if (isWork) {
      sql += "AND ISNULL(b.StatusCd, '') != 'C'";
      sql += "AND ISNULL(c.OrderNo, '') = ''";
      sql += "AND ISNULL(d.StatusCd , '') != 'D'";
    }
    
    String[][] retCustom = dbSale.queryFromPool(sql);
    RiskCustomBean[] cBeans = new RiskCustomBean[retCustom.length];
    for (int ii = 0; ii < retCustom.length; ii++) {
      String custNo = retCustom[ii][2].trim();
      String engNo = retCustom[ii][29].trim();
      String projectId1 = retCustom[ii][30].trim();

      String custNo3 = kUtil.getCustNo3(custNo, engNo);
      QueryLogBean qBean = this.getQueryLogByCustNoProjectId(projectId1, custNo3);

      RiskCustomBean cBean = new RiskCustomBean();
      cBean.setqBean(qBean);
      cBean.setOrderNo(retCustom[ii][0].trim());
      cBean.setRecordNo(retCustom[ii][1].trim());
      cBean.setCustomNo(custNo);
      cBean.setCustomName(retCustom[ii][3].trim());
      cBean.setPercentage(retCustom[ii][4].trim());
      cBean.setZip(retCustom[ii][5].trim());
      cBean.setCity(retCustom[ii][6].trim());
      cBean.setTown(retCustom[ii][7].trim());
      cBean.setAddress(retCustom[ii][8].trim());
      cBean.setCellphone(retCustom[ii][9].trim());
      cBean.setTel(retCustom[ii][10].trim());
      cBean.setTel2(retCustom[ii][11].trim());
      cBean.setEmail(retCustom[ii][12].trim());
      cBean.setAuditorship(retCustom[ii][13].trim());
      cBean.setrStatus(retCustom[ii][14].trim());
      cBean.setcStatus(retCustom[ii][15].trim());
      cBean.setbStatus(retCustom[ii][16].trim());
      cBean.setTrxDate(retCustom[ii][17].trim());
      cBean.setStatusCd(retCustom[ii][18].trim());
      cBean.setNationality(retCustom[ii][19].trim());
      cBean.setTrxDateDown(retCustom[ii][20].trim());
      cBean.setPositionName(retCustom[ii][21].trim());
      cBean.setBirthday(retCustom[ii][22].trim());
      cBean.setMajorName(retCustom[ii][23].trim());
      cBean.setCountryName(retCustom[ii][24].trim());
      cBean.setRiskValue(retCustom[ii][25].trim());
      cBean.setIndustryCode(retCustom[ii][26].trim());
      cBean.setCountryName2(retCustom[ii][27].trim());
      cBean.setEngName(retCustom[ii][28].trim());
      cBean.setEngNo(engNo);
      cBean.setOrderDate(retCustom[ii][31].trim());
      cBeans[ii] = cBean;
    }

    return cBeans;
  }

  /**
   * 取得客戶
   * 
   * @param projectId 案別
   * @param orderNo   使用編號
   * @param isWork    查詢有效客戶? true:僅查詢有效客戶 , false:查詢所有客戶含換名
   * @return
   * @throws Throwable
   */
  public RiskCustomBean[] getCustom(String projectId, String orderNo, boolean isWork) throws Throwable {
    String sql = "SELECT distinct b.OrderNo, b.RecordNo, b.CustomNo, b.CustomName, b.Percentage, b.ZIP, b.City, b.Town, b.Address, b.Cellphone, b.Tel, b.Tel2, b.eMail, b.auditorship, "
        + "b.IsLinked, b.IsControlList, b.IsBlackList, b.TrxDate, b.StatusCd, b.Nationality, b.TrxDateDown, b.PositionName, b.Birthday, b.MajorName, b.CountryName, b.RiskValue, "
        + "b.IndustryCode, b.CountryName2, b.EngName, b.EngNo, a.ProjectID1 " 
        + "FROM Sale05M091 b "
        + "left join Sale05M094 c on b.orderNo = c.orderNo "
        + "left join Sale05M090 a on b.orderNo = a.orderNo "
        + "left join Sale05M092 d on b.orderNo = d.orderNo "
        + "WHERE 1=1 ";
    if (StringUtils.isNotBlank(orderNo)) sql += "and b.OrderNo='" + orderNo + "' ";
    if (StringUtils.isNotBlank(projectId)) sql += "and a.ProjectID1 ='" + projectId + "' ";
    if (isWork) {
      sql += "AND ISNULL(b.StatusCd, '') != 'C' ";
      sql += "AND ISNULL(c.orderNo, '') = '' ";
      sql += "AND ISNULL(d.StatusCd , '') != 'D' ";
    }
    
    String[][] retCustom = dbSale.queryFromPool(sql);
    RiskCustomBean[] cBeans = new RiskCustomBean[retCustom.length];
    for (int ii = 0; ii < retCustom.length; ii++) {
      String custNo = retCustom[ii][2].trim();
      String engNo = retCustom[ii][29].trim();
      String projectId1 = retCustom[ii][30].trim();

      String custNo3 = kUtil.getCustNo3(custNo, engNo);
      QueryLogBean qBean = this.getQueryLogByCustNoProjectId(projectId1, custNo3);

      RiskCustomBean cBean = new RiskCustomBean();
      cBean.setqBean(qBean);
      cBean.setOrderNo(retCustom[ii][0].trim());
      cBean.setRecordNo(retCustom[ii][1].trim());
      cBean.setCustomNo(custNo);
      cBean.setCustomName(retCustom[ii][3].trim());
      cBean.setPercentage(retCustom[ii][4].trim());
      cBean.setZip(retCustom[ii][5].trim());
      cBean.setCity(retCustom[ii][6].trim());
      cBean.setTown(retCustom[ii][7].trim());
      cBean.setAddress(retCustom[ii][8].trim());
      cBean.setCellphone(retCustom[ii][9].trim());
      cBean.setTel(retCustom[ii][10].trim());
      cBean.setTel2(retCustom[ii][11].trim());
      cBean.setEmail(retCustom[ii][12].trim());
      cBean.setAuditorship(retCustom[ii][13].trim());
      cBean.setrStatus(retCustom[ii][14].trim());
      cBean.setcStatus(retCustom[ii][15].trim());
      cBean.setbStatus(retCustom[ii][16].trim());
      cBean.setTrxDate(retCustom[ii][17].trim());
      cBean.setStatusCd(retCustom[ii][18].trim());
      cBean.setNationality(retCustom[ii][19].trim());
      cBean.setTrxDateDown(retCustom[ii][20].trim());
      cBean.setPositionName(retCustom[ii][21].trim());
      cBean.setBirthday(retCustom[ii][22].trim());
      cBean.setMajorName(retCustom[ii][23].trim());
      cBean.setCountryName(retCustom[ii][24].trim());
      cBean.setRiskValue(retCustom[ii][25].trim());
      cBean.setIndustryCode(retCustom[ii][26].trim());
      cBean.setCountryName2(retCustom[ii][27].trim());
      cBean.setEngName(retCustom[ii][28].trim());
      cBean.setEngNo(engNo);
      cBeans[ii] = cBean;
    }

    return cBeans;
  }

  /**
   * 取得有效主客戶
   * 
   * @param projectId 案別
   * @param orderNo   訂單編號
   * @return
   * @throws Throwable
   */
  public RiskCustomBean[] getCustomBean(String projectId, String orderNo) throws Throwable {
    String sql = "select CustomNo, CustomName, Birthday, ZIP, City, Town, Address, Tel, Tel2, CountryName, PositionName, CountryName2, EngNo, EngName, MajorName, IndustryCode "
        + "from Sale05M091 where orderNo = '" + orderNo + "' and ISNULL(statusCd, '') != 'C' ";
    String[][] retCustom = dbSale.queryFromPool(sql);
    RiskCustomBean[] cBeans = new RiskCustomBean[retCustom.length];
    for (int ii = 0; ii < retCustom.length; ii++) {
      String custNo = retCustom[ii][0].trim();
      String custNo2 = retCustom[ii][12].trim();
      String custNo3 = "";
      if (custNo.compareTo(custNo2) < 0) {
        custNo3 = custNo2 + custNo;
      } else {
        custNo3 = custNo + custNo2;
      }
      RiskCustomBean cBean = new RiskCustomBean();
      QueryLogBean qBean = this.getQueryLogByCustNoProjectId(projectId, custNo3);
      cBean.setCustomNo(StringUtils.isNotBlank(custNo) ? custNo : custNo2);
      cBean.setCustomName(retCustom[ii][1].trim());
      cBean.setBirthday(retCustom[ii][2].trim());
      cBean.setZip(retCustom[ii][3].trim());
      cBean.setCity(retCustom[ii][4].trim());
      cBean.setTown(retCustom[ii][5].trim());
      cBean.setAddress(retCustom[ii][6].trim());
      cBean.setTel(retCustom[ii][7].trim());
      cBean.setTel2(retCustom[ii][8].trim());
      cBean.setCountryName(retCustom[ii][9].trim());
      cBean.setPositionName(retCustom[ii][10].trim());
      cBean.setCountryName2(retCustom[ii][11].trim());
      cBean.setEngNo(retCustom[ii][12].trim());
      cBean.setEngName(retCustom[ii][13].trim());
      cBean.setMajorName(retCustom[ii][14].trim());
      cBean.setIndustryCode(retCustom[ii][15].trim());
      cBean.setqBean(qBean);
      cBeans[ii] = cBean;
    }

    return cBeans;
  }

  /**
   * 由收款編號取得訂單編號
   * 
   * @param contractNo
   * @return
   * @throws Throwable
   */
  public String getOrderNoByDocNo(String docNo) throws Throwable {
    String sql = "select top 1 OrderNo FROM Sale05M086 WHERE DocNo = '" + docNo + "' ";
    String ret[][] = dbSale.queryFromPool(sql);
    if (ret.length > 0 && StringUtils.isNotBlank(ret[0][0].trim())) return ret[0][0].trim();

    return "";
  }
  
  /**
   * 由合約編號取得訂單編號
   * 
   * @param contractNo
   * @return
   * @throws Throwable
   */
  public String getOrderNoByProjectIdAndPosition(String projectId, String position) throws Throwable {
    String sql = "select top 1 OrderNo from Sale05M090 a , sale05M092 c where a.OrderNo = c.OrderNo "
               + "and a.ProjectID1 = '"+projectId+"' and c.[Position] = '"+position+"' "
               + "and ISNULL(c.StatusCd, '') != 'D' order by OrderDate desc";
    String ret[][] = dbSale.queryFromPool(sql);
    if (ret.length > 0 && StringUtils.isNotBlank(ret[0][0].trim())) return ret[0][0].trim();

    return "";
  }

  /**
   * 由合約編號取得訂單編號
   * 
   * @param contractNo
   * @return
   * @throws Throwable
   */
  public String getOrderNoByContractNo(String contractNo) throws Throwable {
    String sql = "select top 1 orderNo from Sale05M275_New a where a.ContractNo = '" + contractNo + "' order by LastDateTime DESC";
    String ret[][] = dbSale.queryFromPool(sql);
    if (ret.length > 0 && StringUtils.isNotBlank(ret[0][0].trim())) return ret[0][0].trim();

    return "";
  }

  /**
   * 是否高階主管
   * 
   * @param pName 職務名稱(中文)
   * @return
   * @throws Throwable
   */
  public String isManager(String pName) throws Throwable {
    String manager = "N";
    if (StringUtils.isBlank(pName)) return manager;

    String sql = "SELECT TOP 1 PositionCD, PName, ChairMan From A_Position  WHERE PName = '" + pName + "' ORDER BY PositionCD DESC";
    String ret[][] = dbSale.queryFromPool(sql);
    if (ret.length > 0 && StringUtils.isNotBlank(ret[0][2].trim())) manager = ret[0][2];
    return manager;
  }

  /**
   * 取得有效實質受益人
   * 
   * @param orderNo  訂單編號
   * @param customNo 主法人統一編號
   * @return List<BenBean>
   * @throws Throwable
   */
  public List getBeanListByOrderNo(String orderNo, String customNo) throws Throwable {
    List rs = new ArrayList();
    String sql = "select OrderNo, RecordNo, CustomNo, BCustomNo, BenName, HoldType, Birthday, IsBlackList, IsControlList, IsLinked, CountryName, TrxDate, StatusCd "
        + ", (select top 1 CustomName from Sale05M091 b where a.OrderNo=b.OrderNo and a.CustomNo=b.CustomNo) as custonName "
        + "from Sale05M091Ben a where ISNULL(a.StatusCd, '') != 'C' ";
    if (StringUtils.isNotBlank(orderNo)) sql += "and a.OrderNo = '" + orderNo + "' ";
    if (StringUtils.isNotBlank(customNo)) sql += "and a.CustomNo = '" + customNo + "' ";

    String[][] ret = dbSale.queryFromPool(sql);
    for (int i = 0; i < ret.length; i++) {
      BenBean bean = new BenBean();
      bean.setOrderNo(ret[i][0].trim());
      bean.setRecordNo(ret[i][1].trim());
      bean.setCustomNo(ret[i][2].trim());
      bean.setbCustomNo(ret[i][3].trim());
      bean.setBenName(ret[i][4].trim());
      bean.setHoldType(ret[i][5].trim());
      bean.setBirthday(ret[i][6].trim());
      bean.setIsBlackLink(ret[i][7].trim());
      bean.setIsControlLink(ret[i][8].trim());
      bean.setIsinkLed(ret[i][9].trim());
      bean.setCountryName(ret[i][10].trim());
      bean.setTrxDate(ret[i][11].trim());
      bean.setStatusCd(ret[i][12].trim());
      bean.setCustomName(ret[i][13].trim());
      rs.add(bean);
    }

    return rs;
  }

  /**
   * 國別 - 中文 to 代碼
   * 
   * @param nationName
   * @return
   * @throws Throwable
   */
  public String getNationCodeByName(String nationName) throws Throwable {
    String sql = "SELECT CZ02 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09='" + nationName + "' ";
    String[][] ret = db400.queryFromPool(sql);
    String code = "999";
    if (ret.length > 0 && StringUtils.isNotBlank(ret[0][0])) code = ret[0][0].trim();

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
   * 取得 郵遞區號 - 縣市 - 鄉鎮(中文)
   * 
   * @return String[zip , city , town]
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
   * 取得(一項)洗錢態樣
   * 
   * @return
   * @throws Throwable
   */
  public String getAMLDescOne(String amlNo) throws Throwable {
    String rs = "AMLDESC";
    String sql = "select AMLDesc from saleRY773 where AMLType = 'AML' and amlNo = '" + amlNo + "' ";
    String[][] retAML = dbSale.queryFromPool(sql);
    if (retAML.length > 0) rs = retAML[0][0].trim();
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
   * 取得洗錢態樣
   * 
   * @return
   * @throws Throwable
   */
  public Map getAMLDescBean() throws Throwable {
    String sql = "select * from saleRY773 where AMLType = 'AML' order by AMLNo asc";
    String[][] retAML = dbSale.queryFromPool(sql);
    Map mapAMLMsg = new HashMap();
    for (int i = 0; i < retAML.length; i++) {
      SaleRY773 bean = new SaleRY773();
      bean.setAmlType(retAML[i][0].trim());
      bean.setAmlNo(retAML[i][1].trim());
      bean.setAmlDesc(retAML[i][2].trim());
      mapAMLMsg.put(retAML[i][1].trim(), bean);
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
   * 
   * 用orderNo 取 orderDate
   * 
   * @param projectId
   * @param custNo
   * @return
   * @throws Throwable
   */
  public String getOrderDateByOrderNo(String orderNo) throws Throwable {
    return dbSale.queryFromPool("select top 1 orderDate from Sale05M090 where orderNo = '" + orderNo + "' order by orderDate desc ")[0][0].trim();
  }

  /**
   * 取得 one QueryLog By Name
   * 
   * @param projectId
   * @param custNo
   * @return
   * @throws Throwable
   */
  public QueryLogBean getQueryLogByName(String projectId, String custName) throws Throwable {
    QueryLogBean bean = null;

    String sql = "select top 1 QID, REASON, PROJECT_ID, NATIONAL_ID, QUERY_TYPE, NTCODE, NAME, QUERY_ID, BIRTHDAY, SEX, JOB_TYPE, CITY, TOWN, ADDRESS, B_STATUS, C_STATUS, R_STATUS, STATUS, CONTENTS, PCONTENTS, EMPNO, IP4, CREATE_DATE, CREATE_TIME, UPDATE_DATE, UPDATE_TIME, NTCODE2, ENG_NAME, ENG_NO, QUERY_ID3 "
        + "from query_log a where a.PROJECT_ID = '" + projectId + "' and (a.NAME = '" + custName + "' or a.ENG_NAME = '" + custName + "') "
        + "order by CREATE_DATE desc , CREATE_TIME desc ";

    String[][] ret = dbPW0D.queryFromPool(sql);
    if (ret.length > 0) {
      String[] ret1 = ret[0]; // 只要第一筆
      bean = new QueryLogBean();
      bean.setQid(ret1[0].toString().trim());
      bean.setReason(ret1[1].toString().trim());
      bean.setProjectId(ret1[2].toString().trim());
      bean.setNationalId(ret1[3].toString().trim());
      bean.setQueryType(ret1[4].toString().trim());
      bean.setNtCode(ret1[5].toString().trim());
      bean.setName(ret1[6].toString().trim());
      bean.setQueryId(ret1[7].toString().trim());
      bean.setBirthday(ret1[8].toString().trim());
      bean.setSex(ret1[9].toString().trim());
      bean.setJobType(ret1[10].toString().trim());
      bean.setCity(ret1[11].toString().trim());
      bean.setTown(ret1[12].toString().trim());
      bean.setAddress(ret1[13].toString().trim());
      bean.setbStatus(ret1[14].toString().trim());
      bean.setcStatus(ret1[15].toString().trim());
      bean.setrStatus(ret1[16].toString().trim());
      bean.setStatus(ret1[17].toString().trim());
      bean.setContents(ret1[18].toString().trim());
      bean.setPcontents(ret1[19].toString().trim());
      bean.setEmpNo(ret1[20].toString().trim());
      bean.setIp4(ret1[21].toString().trim());
      bean.setCreateDate(ret1[22].toString().trim());
      bean.setCreateTime(ret1[23].toString().trim());
      bean.setUpdateDate(ret1[24].toString().trim());
      bean.setUpdateTime(ret1[25].toString().trim()); // 25
      bean.setNtCode2(ret1[26].toString().trim());
      bean.setEngName(ret1[27].toString().trim());
      bean.setEngNo(ret1[28].toString().trim());
      bean.setQueryId3(ret1[29].toString().trim());
    }

    return bean;
  }

  /**
   * 取得 one QueryLog like queryId3
   * 
   * @param projectId
   * @param custNo
   * @return
   * @throws Throwable
   */
  public QueryLogBean getQueryLogLike3(String projectId, String custNo) throws Throwable {
    QueryLogBean bean = null;

    String sql = "select top 1 " + "QID, REASON, PROJECT_ID, NATIONAL_ID, QUERY_TYPE, NTCODE, NAME, QUERY_ID, BIRTHDAY, SEX, JOB_TYPE, CITY, "
        + "TOWN, ADDRESS, B_STATUS, C_STATUS, R_STATUS, STATUS, CONTENTS, PCONTENTS, EMPNO, IP4, CREATE_DATE, CREATE_TIME, UPDATE_DATE, UPDATE_TIME, "
        + "NTCODE2, ENG_NAME, ENG_NO, QUERY_ID3 " + "from query_log a where a.PROJECT_ID = '" + projectId + "' and a.QUERY_ID3 like '%" + custNo.toUpperCase()
        + "%' order by CREATE_DATE desc , CREATE_TIME desc ";

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
      bean.setUpdateTime(ret[0][25].toString().trim()); // 25
      bean.setNtCode2(ret[0][26].toString().trim());
      bean.setEngName(ret[0][27].toString().trim());
      bean.setEngNo(ret[0][28].toString().trim());
      bean.setQueryId3(ret[0][29].toString().trim());
    }

    return bean;
  }

  /**
   * 取得QueryLog one
   * 
   * @param projectId
   * @param custNo
   * @return
   * @throws Throwable
   */
  public QueryLogBean getQueryLogByCustNoProjectId(String projectId, String custNo) throws Throwable {
    QueryLogBean bean = null;

    String sql = "select top 1 " + "QID, REASON, PROJECT_ID, NATIONAL_ID, QUERY_TYPE, NTCODE, NAME, QUERY_ID, BIRTHDAY, SEX, JOB_TYPE, CITY, "
        + "TOWN, ADDRESS, B_STATUS, C_STATUS, R_STATUS, STATUS, CONTENTS, PCONTENTS, EMPNO, IP4, CREATE_DATE, CREATE_TIME, UPDATE_DATE, UPDATE_TIME, "
        + "NTCODE2, ENG_NAME, ENG_NO, QUERY_ID3 " + "from query_log a where a.PROJECT_ID = '" + projectId + "' and a.QUERY_ID3 like '%" + custNo.toUpperCase()
        + "%' order by CREATE_DATE desc , CREATE_TIME desc ";

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
      bean.setUpdateTime(ret[0][25].toString().trim()); // 25
      bean.setNtCode2(ret[0][26].toString().trim());
      bean.setEngName(ret[0][27].toString().trim());
      bean.setEngNo(ret[0][28].toString().trim());
      bean.setQueryId3(ret[0][29].toString().trim());
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

}
