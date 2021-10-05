package Farglory.aml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.fglife.risk.*;
import com.fglife.soap.cr.MainReply;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import Farglory.util.Result;
import Farglory.util.ResultStatus;
import Farglory.util.RiskCheckRS;
import Farglory.util.SendMailBean;
import Farglory.util.TalkBean;
import Farglory.util.Transaction;
import Farglory.util.pojo.BenBean;
import jcx.db.talk;
import jcx.jform.bvalidate;

public class RiskCheckTools_Lyods extends bvalidate {
  String serverIP, serverName; // 伺服器IP , NAME
  talk dbSale, dbEMail, dbBen, dbEIP = null;
  KUtils kUtil;
  KSqlUtils kSqlUtil;
  String sysType = "RYB";// 不動產行銷B 銷售C
  String strProjectID1 = "";
  String strOrderNo = "";
  String strOrderDate = "";
  
  ResultStatus rsStatus = new ResultStatus();
  String serverType = "";
  String lyodsSoapURL = "";

  AMLyodsBean aml = null;
  boolean isTest = true;
//  String stringSQL = "";
  String testFlag = "";
  String userNo = "";
  String empNo = "";
  String sysdate = "";
  String systime = "";
  String actionNo = "";
  String strHouse = "";
  String strCar = "";

  public RiskCheckTools_Lyods(AMLyodsBean abean) throws Throwable {
    this.aml = abean;
    strProjectID1 = abean.getProjectID1();
    strOrderNo = abean.getOrderNo();
    strOrderDate = abean.getOrderDate();
    
    //config
    lyodsSoapURL = abean.getLyodsSoapURL();
    
    //ip & serverName
    isTest = abean.isTestServer();
    if (isTest) {
      testFlag = " (測試) ";
      System.out.println("RiskCheckTools_Lyods :環境>>>" + testFlag);
    }

    //params
    this.dbSale = abean.getDbSale();
    this.dbEMail = abean.getDbEMail();
    this.dbBen = abean.getDb400CRM();
    this.dbEIP = abean.getDbEIP();
    
    //talk bean
    TalkBean tBean = new TalkBean();
    tBean.setDbSale(dbSale);
    tBean.setDbEMail(dbEMail);
    tBean.setDb400CRM(dbBen);
    tBean.setDbEIP(dbEIP);
    kUtil = new KUtils(tBean);
    kSqlUtil = new KSqlUtils(tBean);

    // Emaker工號
    this.userNo = abean.getEmakerUserNo();
    
    // FG工號
    this.empNo = kSqlUtil.getEmpNo(this.userNo);
    
    // 系統日期時間
    this.getDateTime();
    
    // action
    this.getActionNo();
    
    // HouseCar
    this.getHouseCar();
  }
  
  /**
   * 啊我就只要看風險值而已
   * @param cBeans
   * @return
   * @throws Throwable
   */
  public Result getJustRisk(RiskCustomBean[] cBeans) throws Throwable {
    Result rs = new Result();
    RiskCheckRS rcRS = new RiskCheckRS();
    
    return rs;
  }

  /**
   * 檢查風險值 (來源: 主要客戶)
   * @return
   * @throws Throwable
   */
  public Result processRisk(RiskCustomBean[] cBeans) throws Throwable {
    Result rs = new Result();
    RiskCheckRS rcRS = new RiskCheckRS();
    
    // start 更新實質受益人
    List bens = kSqlUtil.getBeanListByOrderNo(strOrderNo, "");
    String usedOid = "";
    for(int i=0 ; i<bens.size() ; i++) {
      BenBean ben = (BenBean)bens.get(i);
//      String ono = this.strOrderNo;       // 訂單編號
      String id = ben.getbCustomNo();     // 實受人ID
      String name = ben.getCustomName();  // 實受人name
      String oid = ben.getCustomNo();     // 法人ID
      String oname = ben.getCustomName(); // 法人NAME
      String fileType = ben.getHoldType();
      String nationCode = kSqlUtil.getNationCodeByName(ben.getCountryName());
      String bdate = ben.getBirthday().replace("/", "");
      if ("".equals(bdate)) {
        bdate = "0";
      }
      String idType = "1";

      //每個法人第一筆
      if( !StringUtils.equals(oid, usedOid) ) {
        //以法人為KEY，將他所有實質受益人複製至備份檔(訂單號無視)
        String sqlMove1 = "INSERT INTO PSHAPFHF (Select * FROM PSHAPF WHERE SHA11 = '" + oid + "' And SHA00 = 'RY')";
        dbBen.execFromPool(sqlMove1);

        //刪除主檔相關
        String sqlDelete2 = "DELETE FROM PSHAPF WHERE SHA11 = '" + oid + "' And SHA00 = 'RY' ";
        dbBen.execFromPool(sqlDelete2);
      }

      //寫入新實質受益人
      String sqlInsert3 = "Insert into PSHAPF (SHA00, SHA02, SHA03, SHA04, SHA05, SHA06, SHA07, SHA08 ,SHA97, SHA98, SHA99, SHA100, SHA101, SHA102, SHA09, SHA10, SHA11, SHA12 ) "
          + "VALUES " + "('RY','" + strOrderNo + "','" + fileType + "','" + name + "','" + idType + "','" + id + "','" + bdate + "','" + nationCode + "','" + empNo + "'," + sysdate
          + "," + systime + ",'" + empNo + "'," + sysdate + "," + systime + ",'R','3','" + oid + "','" + oname + "' )";
      dbBen.execFromPool(sqlInsert3);
      
      usedOid = oid;
    }
    // End 更新實質受益人
    
    //取得AS400資訊
    ResourceBundle resource = ResourceBundle.getBundle("configK");
    String as400ip = resource.getString("AS400.IP");
    String as400account = resource.getString("AS400.ACCOUNT");
    String as400password = resource.getString("AS400.PASSWORD");
    String as400init = resource.getString("AS400.INIT");
    String cms00c = resource.getString("CMS00C.LIB");
    RPGAS400Interface ra = null;
    
//    System.out.println(">>as400ip: " + as400ip);
//    System.out.println(">>as400account: " + as400account);
//    System.out.println(">>as400password: " + as400password);
//    System.out.println(">>cms00c: " + cms00c);

    String riskValue = "";
    String riskPoint = "";
    String riskPending = "";
    ArrayList list = new ArrayList();
    
    try {
      //20210816 必須先傳入CMS00C
      ra = new RPGCMS00C(as400ip, as400account, as400password);
      
      String msgboxtext = "";
      String tmpMsgText = "";
      for(int i=0 ; i<cBeans.length ; i++) {
        RiskCustomBean cBean = cBeans[i];
        QueryLogBean qBean = cBean.getqBean();
        String custNo = cBean.getCustomNo();
        String custName = cBean.getCustomName();
        if (isTest) {
          System.out.println("custom>>>" + i + "-" + cBean.getCustomNo() + "," + cBean.getCustomName());
        }
        
        // N: 個人 C: 公司 F: 外國人
        String type = kUtil.getUserType(custNo);
        if (!"中華民國".equals(cBean.getCountryName())) {
          type = "F";
        }

        String idnCode = StringUtils.isNotBlank(qBean.getJobType())? qBean.getJobType():"X";
        String managerFlag = kSqlUtil.isManager(cBean.getPositionName());
        System.out.println("高階經理人:" + managerFlag);

        LinkedHashMap map = new LinkedHashMap();
        map.put("INAME", custName);                             // 客戶姓名
        map.put("IDATE", cBean.getBirthday().replace("/", "")); // 生日
        map.put("ID", custNo);                                  // 身份證號
        map.put("IAD1", cBean.getZip());                        // 地址 1
        map.put("IAD2", cBean.getCity());                       // 地址 2
        map.put("IAD3", cBean.getTown());                       // 地址 3
        map.put("IADD", cBean.getCity() + cBean.getTown() + cBean.getAddress());// 長地址
        map.put("IZIP", cBean.getZip());            // 郵遞區號
        map.put("ITELO", cBean.getTel());           // 公司電話
        map.put("ITELH", cBean.getTel2());          // 住家電話
        map.put("TYPE", type);                      // N: 個人 C: 公司
        map.put("SEX", qBean.getSex());             // 性別 M,F
        map.put("CNY", qBean.getNtCode());          // 國籍
        map.put("JOB", "");                         // 職業代碼
        map.put("VOC", idnCode);                    // 行業別
        map.put("CUST", " ");                       // 監護宣告
        map.put("IESTD", " ");                      // 設定日期
        map.put("IEXEC", managerFlag);              // 高階管理人 Y/N
        map.put("CNY2", " ");                       // 國籍 2
        map.put("CNY3", " ");                       // 國籍 3
        map.put("ICHGD", "");                       // 變更登記日期
        map.put("CHGNO", empNo);                    // 異動人員員編
        map.put("RTCOD", "");                       // 回覆碼
        map.put("INSN", "");                        // 客戶編號
        boolean a = ra.invoke(as400init, cms00c, map);        
//        System.out.println("RTCODE:" + ra.getResult()[22]);
        
        //Lyods GO
        aml.setRiskResult("Y");
        aml.setCheckAll("Y");
        aml.setCustBean(cBean);
        LyodsTools lyodsTools = new LyodsTools(aml);
        Result result = lyodsTools.checkRisk();
        if(result.getRsStatus()[0] != ResultStatus.SUCCESS[0]) {
          System.out.println(">>> checkRisk Error >>>" + i + "-" + cBean.getCustomNo() + "," + cBean.getCustomName());
          System.out.println(">>>Error:" + result.getRsStatus()[3]);
          continue;
        }
        
        //取出結果物件
        MainReply mainReply = (MainReply) result.getData();
        if(mainReply != null) {
          if(StringUtils.isBlank(mainReply.getMessage().toString())) {
            //訊息處理
            riskPoint = mainReply.getRiskScore().trim();
            riskPending = mainReply.getIsPending().trim();
            riskValue = "阿災";
            String lyodsRiskLv = mainReply.getRiskLevel().trim();
            if(StringUtils.equals(riskPending, "Y")){
              riskValue = "待判定";
            }else if(StringUtils.equals(lyodsRiskLv, "H")) {
              riskValue = "高風險";
            }else if(StringUtils.equals(lyodsRiskLv, "M")) {
              riskValue = "中風險";
            }else if(StringUtils.equals(lyodsRiskLv, "L")) {
              riskValue = "低風險";
            }else if(StringUtils.equals(lyodsRiskLv, "P")) {
              riskValue = "優先法高";
            }else if(StringUtils.equals(lyodsRiskLv, "X")) {
              riskValue = "禁止往來";
            }
            
            System.out.println("19洗錢風險值 : " + riskPoint);
            System.out.println("20洗錢風險等級 : " + riskValue);            
          }
        }

        String customTitle = "客戶 ";
        if(this.aml.getRecordType().indexOf("指定第三人") != -1) {
          customTitle = "指定第三人 "; 
        }
        
        msgboxtext += customTitle + custName + " 洗錢風險等級 : " + riskValue + "\n";
        tmpMsgText = customTitle + ">>>" + custName + "\n"
                    +"isBlacklist是否黑名單" + ">>> " + mainReply.getIsBlacklist() + "\n"
                    +"isPending是否待判定" + ">>> " + mainReply.getIsPending() + "\n"
                    +"checkResult最後判定結果" + ">>> " + mainReply.getCheckResult() + "\n"
                    +"riskLevel參考風險等級" + ">>> " + mainReply.getRiskScore() + "/" + mainReply.getRiskLevel() + "\n"
                    +"settleDate最後判定日期" + ">>> " + mainReply.getSettleDate() + "\n"
                    +"customerScore客戶分數" + ">>> " + mainReply.getCustomerScore() + "/" + mainReply.getCustomerRisk() + "\n"
                    +"nationScore國籍分數" + ">>> " + mainReply.getNationScore() + "/" + mainReply.getNationRisk() + "\n"
                    +"channelScore通路分數" + ">>> " + mainReply.getChannelScore() + "/" + mainReply.getChannelRisk() + "\n"
                    +"productScore產品分數" + ">>> " + mainReply.getProductScore() + "/" + mainReply.getProductRisk() + "\n";

        HashMap m = new HashMap();
        m.put("p01", strProjectID1);
        m.put("p02", strHouse.toUpperCase());
        m.put("p025", strCar.toUpperCase());
        m.put("p03", custName);
        m.put("p035", custNo);
        m.put("p04", strOrderDate);
        m.put("p05", riskPoint);
        m.put("p06", riskValue.replace("風險", ""));
        m.put("riskValue", riskValue);
        list.add(m);
        
        System.out.println("風險值結果: \n" + tmpMsgText);
        
//        System.out.println(customTitle + ">>>" + custName);
//        System.out.println("isBlacklist是否黑名單" + ">>>" + mainReply.getIsBlacklist());
//        System.out.println("isPending是否待判定" + ">>>" + mainReply.getIsPending());
//        System.out.println("checkResult最後判定結果" + ">>>" + mainReply.getCheckResult());
//        System.out.println("riskLevel參考風險等級" + ">>>" + mainReply.getRiskLevel());
//        System.out.println("riskLevel風險總分" + ">>>" + mainReply.getRiskScore());
//        System.out.println("settleDate最後判定日期" + ">>>" + mainReply.getSettleDate());
//        System.out.println("customerScore客戶分數" + ">>>" + mainReply.getCustomerScore() + "/" + mainReply.getCustomerRisk());
//        System.out.println("nationScore國籍分數" + ">>>" + mainReply.getNationScore() + "/" + mainReply.getNationRisk());
//        System.out.println("channelScore通路分數" + ">>>" + mainReply.getChannelScore() + "/" + mainReply.getChannelRisk());
//        System.out.println("productScore產品分數" + ">>>" + mainReply.getProductScore() + "/" + mainReply.getProductRisk());
      }
      
      // 風險值結果輸出
      rcRS.setRsMsg(msgboxtext);

      // 更新客戶風險值
      if (this.aml.isUpdSale05M091()) this.updSaleM091(list);
      
      //更新合約客戶風險值
      if (this.aml.isUpdSale05M277()) this.updSaleM277(list);
      
      //更新合約指定第三人風險值
      if (this.aml.isUpdSale05M356()) this.updSaleM356(list);
      
      // insert into Sale05M070
      if (this.aml.isUpd070Log()) this.insSale05M070(list);

      // 組成MAIL
      SendMailBean smBean = new SendMailBean();
      smBean = this.sendMail(list);

      List sendMailList = new ArrayList();
      sendMailList.add(smBean);
      rcRS.setSendMailList(sendMailList);

      rs.setData(rcRS);
      rs.setRsStatus(ResultStatus.SUCCESS);
      
    } catch (Exception e) {
      System.out.println("錯誤訊息:" + e.toString());
      rs.setExp(e);
      rs.setRsStatus(ResultStatus.ERROR);
      
    }

    return rs;
  }

  // 系統日期時間
  public void getDateTime() throws Throwable {
    sysdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    systime = new SimpleDateFormat("HHmmss").format(new Date());
  }

  // actionNo
  public void getActionNo() throws Throwable {
    String ram = "";
    Random random = new Random();
    for (int i = 0; i < 4; i++) {
      ram += String.valueOf(random.nextInt(10));
    }
    this.actionNo = this.sysdate + this.systime + ram;
    System.out.println("strActionNo>>>" + this.actionNo);
  }

  // 棟樓別
  public void getHouseCar() throws Throwable {
    String sql092 = "SELECT HouseCar,Position FROM Sale05M092 WHERE OrderNo = '" + aml.getOrderNo() + "' ORDER BY RecordNo";
    String[][] retPosition = dbSale.queryFromPool(sql092);
    if (retPosition.length > 0) {
      for (int a = 0; a < retPosition.length; a++) {
        if ("House".equals(retPosition[a][0].trim())) {
          if ("".equals(strHouse)) {
            strHouse = retPosition[a][1].trim();
          } else {
            strHouse = strHouse + "," + retPosition[a][1].trim();
          }
        } else {
          if ("".equals(strCar)) {
            strCar = retPosition[a][1].trim();
          } else {
            strCar = strCar + "," + retPosition[a][1].trim();
          }
        }
      }
    }
  }

  /**
   * 回寫Sale05M091 客戶風險值
   * 
   * @param customList
   * @return
   * @throws Throwable
   */
  public String updSaleM091(List customList) throws Throwable {
    System.out.println("回寫05M091資料----------" + customList.size() + "------------------S");

    Transaction trans = new Transaction();
    for (int ii = 0; ii < customList.size(); ii++) {
      HashMap data = (HashMap) customList.get(ii);
      String M091Sql = "UPDATE Sale05M091 SET RiskValue = '" + data.get("riskValue").toString().trim() + "' " 
                     + "WHERE OrderNo = '" + this.aml.getOrderNo() + "' AND CustomNo = '" + data.get("p035").toString().trim() + "' and ISNULL(StatusCd , '') = '';  ";
//      dbSale.execFromPool(M091Sql);
      trans.append(M091Sql);
    }
    trans.close();
    dbSale.execFromPool(trans.getString());

    System.out.println("回寫05M091資料----------" + customList.size() + "------------------E");
    return "0";
  }

  /**
   * 回寫Sale05M277 合約客戶風險值
   * 
   * @param customList
   * @return
   * @throws Throwable
   */
  public String updSaleM277(List customList) throws Throwable {
    System.out.println("回寫05M277資料----------" + customList.size() + "------------------S");

    Transaction trans = new Transaction();
    for (int ii = 0; ii < customList.size(); ii++) {
      HashMap data = (HashMap) customList.get(ii);
      String M277Sql = "UPDATE Sale05M277 "
               + "SET RiskValue = '" + data.get("riskValue").toString().trim() + "' " 
               + "WHERE ContractNo = '" + this.aml.getContractNo() + "' "
               + "AND CustomNo = '" + data.get("p035").toString().trim() + "' "
               + "AND ISNULL(StatusCd , '') = '' ";
      trans.append(M277Sql);
    }
    trans.close();
    dbSale.execFromPool(trans.getString());

    System.out.println("回寫05M277資料----------" + customList.size() + "------------------E");
    return "0";
  }
  
  /**
   * 回寫Sale05M356 合約指定第三人風險值
   * 
   * @param customList
   * @return
   * @throws Throwable
   */
  public String updSaleM356(List customList) throws Throwable {
    System.out.println("回寫05M356資料----------" + customList.size() + "------------------S");

    Transaction trans = new Transaction();
    for (int ii = 0; ii < customList.size(); ii++) {
      HashMap data = (HashMap) customList.get(ii);
      String M277Sql = "UPDATE Sale05M356 "
               + "SET RiskValue = '" + data.get("riskValue").toString().trim() + "' " 
               + "WHERE ContractNo = '" + this.aml.getContractNo() + "' "
               + "  AND DesignatedId = '" + data.get("p035").toString().trim() + "' ";
      trans.append(M277Sql);
    }
    trans.close();
    dbSale.execFromPool(trans.getString());

    System.out.println("回寫05M356資料----------" + customList.size() + "------------------E");
    return "0";
  }

  /**
   * 寫入Sale05M070
   * 
   * @param customList
   * @return
   * @throws Throwable
   */
  public String insSale05M070(List customList) throws Throwable {
    System.out.println("存入05M070資料-----------------------------------S");

    // 序號
    int intRecordNo = 1;
    
    String sql = "";
    if (this.aml.getFunc().indexOf("購屋") == 0 && !"".equals(this.aml.getOrderNo())) {
      sql = "SELECT MAX(CAST(RecordNo AS decimal(18, 0))) AS MaxNo FROM Sale05M070 WHERE OrderNo ='" + this.aml.getOrderNo() + "'";
    } else if (this.aml.getFunc().indexOf("合約") == 0 && !"".equals(this.aml.getContractNo())) {
      sql = "SELECT MAX(CAST(RecordNo AS decimal(18, 0))) AS MaxNo FROM Sale05M070 WHERE ContractNo ='" + this.aml.getContractNo() + "'";
    }else if (this.aml.getFunc().indexOf("換名") == 0 && !"".equals(this.aml.getOrderNo())) {
      sql = "SELECT MAX(CAST(RecordNo AS decimal(18, 0))) AS MaxNo FROM Sale05M070 WHERE OrderNo ='" + this.aml.getOrderNo() + "'";
    }
    
    String[][] ret05M070 = dbSale.queryFromPool(sql);
    if (ret05M070.length > 0 && !"".equals(ret05M070[0][0].trim())) {
      intRecordNo = Integer.parseInt(ret05M070[0][0].trim()) + 1;
    }
    System.out.println("insSale05M070 intRecordNo >>>" + intRecordNo);

    Transaction trans = new Transaction();
    for (int ii = 0; ii < customList.size(); ii++) {
      HashMap data = (HashMap) customList.get(ii);
      String strCustomNo = data.get("p035").toString().trim();
      String strCustomName = data.get("p03").toString().trim();
      String riskValue = data.get("riskValue").toString().trim();
      sql = "INSERT INTO Sale05M070 "
          + "(OrderNo ,ContractNo ,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,OrderDate ,CDate ,SHB00,SHB06A,SHB06B,SHB06,SHB97, SHB98, SHB99) " 
          + "VALUES "
          + "('" + this.aml.getOrderNo() + "' ,'"+this.aml.getContractNo()+"' ,'" + this.aml.getProjectID1() + "','" + intRecordNo + "','" + actionNo + "','" + this.aml.getFunc() + "','"
          + this.aml.getRecordType() + "' " + ",'" + this.aml.getActionName() + "','風險值:" + riskValue + "','" + strCustomNo + "' " + ",'" + strCustomName + "','"
          + this.aml.getOrderDate() + "' ,'"+this.aml.getcDate()+"' ,'RY','773','022','風險值:" + riskValue + "','" + empNo + "'," + this.sysdate + "," + this.systime + ") ";
      trans.append(sql);
      intRecordNo++;
    }
    trans.close();
    dbSale.execFromPool(trans.getString());

    System.out.println("存入05M070資料-----------------------------------E");
    return "0";
  }

  // 發MAIL
  public SendMailBean sendMail(List customList) throws Throwable {
    System.out.println("組成 EMAIL-----------------------------------S");
    String userEmail = "";
    String userEmail2 = "";
    String DPCode = "";
    String DPManageemNo = "";
    String DPeMail = "";
    String DPeMail2 = "";
    String[][] reteMail = null;

    ////////////////
    String sql = "SELECT DP_CODE,PN_EMAIL1,PN_EMAIL2 FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(sql);
    if (reteMail.length > 0) {
      DPCode = reteMail[0][0];
      if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
        userEmail = reteMail[0][1];
      }
      if (reteMail[0][2] != null && !reteMail[0][2].equals("")) {
        userEmail2 = reteMail[0][2];
      }
    }
    System.out.println("DPCode===>" + DPCode);
    System.out.println("userEmail===>" + userEmail);
    System.out.println("userEmail2===>" + userEmail2);
    
    /////////////////////////////////////////////////
    sql = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + DPCode + "'";
    reteMail = dbEMail.queryFromPool(sql);
    if (reteMail.length > 0) {
      DPManageemNo = reteMail[0][0];
    }
    System.out.println("DPManageemNo===>" + DPManageemNo);
    
    /////////////////////////////////////////////////
    sql = "SELECT PN_EMAIL1,PN_EMAIL2 FROM PERSONNEL WHERE PN_EMPNO='" + DPManageemNo + "'";
    reteMail = dbEMail.queryFromPool(sql);
    if (reteMail.length > 0) {
      if (reteMail[0][0] != null && !reteMail[0][0].equals("")) {
        DPeMail = reteMail[0][0];
      }
      if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
        DPeMail2 = reteMail[0][1];
      }
    }
    System.out.println("DPeMail===>" + DPeMail);
    System.out.println("DPeMail2===>" + DPeMail2);
    
    /////////////////////////////////////////////////////////
    String PNCode = "";
    String PNManageemNo = "";
    String PNMail = "";
    sql = "SELECT PN_DEPTCODE FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(sql);
    PNCode = reteMail[0][0];
    System.out.println("PNCode===>" + PNCode);
    
    sql = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + PNCode + "'";
    reteMail = dbEMail.queryFromPool(sql);
    PNManageemNo = reteMail[0][0];
    System.out.println("PNManageemNo===>" + PNManageemNo);
    
    sql = "SELECT PN_EMAIL1 FROM PERSONNEL WHERE PN_EMPNO='" + PNManageemNo + "'";
    reteMail = dbEMail.queryFromPool(sql);
    PNMail = reteMail[0][0];
    System.out.println("PNMail===>" + PNMail);
    ////////////////////////////////////////////////////////////////////////////////////////

    // send email
    boolean hilitgt = false;
    String table1 = "<table style='text-align:center;' border=1><tr><td>案別</td><td>棟樓別</td><td>車位別</td><td>客戶名稱</td><td>付訂日期</td><td>風險綜合值</td><td>客戶風險等級</td><td>說明</td></tr>";
    String tail = "</table>";
    String contextsample = "<tr><td>${p01}</td><td>${p02}</td><td>${p025}</td><td>${p03}</td><td>${p04}</td><td>${p05}</td><td>${p06}</td><td align='left' valign='center'>${p07}</td></tr>";
    String cbottom = "</body></html>";
    String context = this.aml.getProjectID1() + "案" + strHouse + "不動產訂購客戶風險等級評估結果通知" + testFlag + "<BR>";

    context = table1;
    for (int i = 0; i < customList.size(); i++) {
      HashMap cm = (HashMap) customList.get(i);

      String l1 = new String(contextsample);
      l1 = l1.replace("${p01}", (String) cm.get("p01"));
      l1 = l1.replace("${p02}", (String) cm.get("p02"));
      l1 = l1.replace("${p025}", (String) cm.get("p025"));
      l1 = l1.replace("${p03}", (String) cm.get("p03"));
      l1 = l1.replace("${p035}", (String) cm.get("p035"));
      l1 = l1.replace("${p04}", (String) cm.get("p04"));
      l1 = l1.replace("${p05}", (String) cm.get("p05"));
      l1 = l1.replace("${p06}", (String) cm.get("p06"));
      String tempPo6 = "" + cm.get("p06");
      tempPo6 = tempPo6.trim();
      if ("高".equals(tempPo6.trim())) {
        l1 = l1.replace("${p07}", "洗錢及資恐風險評估為" + tempPo6 + "風險客戶，請依洗錢防制作業規定，執行加強式管控措施");
        hilitgt = true;
      } else {
        l1 = l1.replace("${p07}", "洗錢及資恐風險評估為" + tempPo6 + "風險客戶");
      }

      context = context + l1;
    } // customList for End
    context = context + tail + cbottom;
 
    String subject = this.aml.getProjectID1() + "案" + strHouse + "不動產訂購客戶風險等級評估結果通知" + testFlag;
    SendMailBean send = new SendMailBean();
    send.setColm1("ex.fglife.com.tw");
    send.setColm2("Emaker-Invoice@fglife.com.tw");
    send.setSubject(subject);
    send.setContext(context);
    send.setColm6(null);
    send.setColm7("");
    send.setColm8("text/html");

    if (hilitgt) {
      String[] arrayUser = { "Kyle_Lee@fglife.com.tw", userEmail, DPeMail, PNMail };
      send.setArrayUser(arrayUser);
    } else {
      String[] arrayUser = { "Kyle_Lee@fglife.com.tw", userEmail };
      send.setArrayUser(arrayUser);
    }
    System.out.println("組成 EMAIL-----------------------------------E");

    return send;
  }
  

  // 是數字回傳 true，否則回傳 false。
  public boolean check(String value) throws Throwable {
    return false;
  }

}
