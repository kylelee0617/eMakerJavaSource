package Farglory.aml;

import org.apache.commons.lang3.StringUtils;

import com.fglife.soap.client.BlackListClient;
import com.fglife.soap.cr.MainQuery;
import com.fglife.soap.cr.MainReply;
import com.fglife.soap.cr.RenewRelatedQuery;
import com.fglife.soap.cr.RenewRelatedReply;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.Result;
import Farglory.util.ResultStatus;
import Farglory.util.TalkBean;
import jcx.db.talk;
import jcx.jform.bvalidate;

/**
 * for 萊斯
 * 
 * @author B04391
 *
 */

public class LyodsTools extends bvalidate {
  // DB
  talk db400 = null;
  talk dbSale = null;
  talk dbEIP = null;
  
  //param
  String empNo = "";
  String serverType = "";
  String lyodsSoapURL = "";
  String sysType = "RYB";// 不動產行銷B 銷售C
  boolean isTest = true;
  AMLyodsBean lyodsBean;
  
  //util
  KUtils kUtil;
  KSqlUtils kSqlUtil;

  public LyodsTools() throws Throwable {
  }

  public LyodsTools(AMLyodsBean lyodsBean) throws Throwable {
    isTest = lyodsBean.isTestServer();
    lyodsSoapURL = lyodsBean.getLyodsSoapURL();
    this.lyodsBean = lyodsBean;
    
    db400 = lyodsBean.getDb400CRM();
    dbSale = lyodsBean.getDbSale();
    dbEIP = lyodsBean.getDbEIP();
    TalkBean tBean = lyodsBean.gettBean();
    kUtil = new KUtils(tBean);
    kSqlUtil = new KSqlUtils(tBean);
    
    this.empNo = kSqlUtil.getEmpNo(lyodsBean.getEmakerUserNo());
    
    System.out.println(">>>LyodsTools init done");
  }
  
  /**
   * 寫入訂單-客戶，並更新關聯人
   * @return
   * @throws Throwable
   */
  public Result renewRelated() throws Throwable{
    Result rs = new Result();
    RiskCustomBean cBean = lyodsBean.getCustBean();
    String custNo = cBean.getCustomNo();
    
    BlackListClient blackListClient = new BlackListClient(lyodsSoapURL);  
    RenewRelatedQuery renewRelatedQuery = new RenewRelatedQuery();        
    renewRelatedQuery.setWriteLog("Y");                                   //寫入LOG v
    renewRelatedQuery.setCalculationCode(lyodsBean.getCalculationCode()); //是否計算風險評分代碼
    renewRelatedQuery.setAddCustomer(lyodsBean.getAddCustomer());         //新增或註銷主要客戶 Y.新增 N.註銷
    renewRelatedQuery.setCustomerType(this.isCompany(custNo)? "2":"1");   //主要客戶類別 v
    renewRelatedQuery.setId(custNo);                                      //身分證字號/統編 v
    renewRelatedQuery.setKeyNumber(lyodsBean.getOrderNo());               //保單號碼/契約號碼 v
    renewRelatedQuery.setCaseNumber(lyodsBean.getOrderNo());              //案件編號 v
    renewRelatedQuery.setSystemCode("RYB");                               //系統代碼 v
    renewRelatedQuery.setProgramCode("RYB");                              //程式代碼 v
    renewRelatedQuery.setUserId(this.empNo);                              //承辦人員編 v
    renewRelatedQuery.setUserUnit("");                                    //承辦人單位 v
    RenewRelatedReply renewRelatedReply = blackListClient.executeRenewRelated(renewRelatedQuery);
    
    rs.setData(renewRelatedReply);
    rs.setRsStatus(ResultStatus.SUCCESS);
    return rs;
  }

  //
  public Result checkRisk() throws Throwable {
    Result rs = new Result();
    RiskCustomBean cBean = this.lyodsBean.getCustBean();
    String custNo = StringUtils.isNotBlank(cBean.getCustomNo())? cBean.getCustomNo():cBean.getEngNo();
    String custName = cBean.getCustomName();

    if (isTest) {
      System.out.println("LydosTools check custom>>>" + custNo + "," + custName);
    }

    // 210114 Kyle : 行業別
    String industryCode = cBean.getIndustryCode() != null ? cBean.getIndustryCode() : "";
    if(StringUtils.isBlank(industryCode)) industryCode = kSqlUtil.getIndustryCodeByMajorName(cBean.getMajorName());

    // 法人 自然人 性別
    String type = kUtil.getUserType(custNo).replaceAll("N", "1").replaceAll("C", "2");
    String sex = cBean.getqBean().getSex();

    // 國籍轉碼(若空預設999)
    String cnyCode = kSqlUtil.getNationCodeByName(cBean.getCountryName());
    String cnyCode2 = kSqlUtil.getNationCodeByName(cBean.getCountryName2());

    // 組資料，SOAP
    try {
      BlackListClient blackListClient = new BlackListClient(lyodsSoapURL);
      MainQuery mainQuery = new MainQuery();
      mainQuery.setRiskResult(lyodsBean.getRiskResult());   // 查風險值 v
      mainQuery.setCheckAll(lyodsBean.getCheckAll());       // 查所有類別 v
      mainQuery.setChangeOrgnization("Y");                  // 轉指派部門 v
      mainQuery.setAddCustomer(lyodsBean.getAddCustomer()); // 新增或註銷主要客戶 v
      mainQuery.setAddAccount(lyodsBean.getAddAccount());   // 新增或註銷保單 v
      mainQuery.setWriteLog("Y");                           // 寫入LOG v
      mainQuery.setModifyData(lyodsBean.getModifyData());   // 更新客戶資料
      mainQuery.setCustomerType(type);                      // 主要客戶類別 v
      mainQuery.setChineseName(custName);                   // 中文姓名
      mainQuery.setEnglishName(cBean.getEngName());         // 英文姓名
      mainQuery.setId(custNo);                              // 身分證字號 v
      mainQuery.setSex(sex);                                // 性別
      mainQuery.setBirth(cBean.getBirthday().replaceAll("/", "").replaceAll("-", "")); // 生日
      mainQuery.setRegisterNation(cnyCode);                 // 居住地國籍  同nation1 => 對應萊斯的「國家/地區(住宅)」
      mainQuery.setBirthNation(cnyCode2);                   // 出生地國籍  同nation2 => 對應萊斯的「國籍/地區」
      mainQuery.setProfession(cBean.getProfessionCode1());  // 職業類型
      mainQuery.setProfession2(cBean.getProfessionCode2()); // 次職業類型
      mainQuery.setIndustry(industryCode);                  // 行業類別
      mainQuery.setIndustry2("");                           // 次行業類別
      mainQuery.setKeyNumber(lyodsBean.getOrderNo());       // 保單號碼/契約號碼 v
      mainQuery.setCaseNumber(lyodsBean.getOrderNo());      // 案件編號 v (跟訂單編號一樣即可)
      mainQuery.setSystemCode(this.sysType);                // 系統代碼 V (跟程式代碼一樣即可)
      mainQuery.setProgramCode(this.sysType);               // 程式代碼 v
      mainQuery.setApplyDate("");                           // 受理/申請日期
      mainQuery.setContractDate(lyodsBean.getOrderDate().replaceAll("/", "").replaceAll("-", ""));  // 契約生效日期
      mainQuery.setProduct("RYB");                             // 產品代碼
      mainQuery.setChannel("");                             // 通路代碼
      mainQuery.setUserId(this.empNo);                      // 承辦人員編 v
      mainQuery.setUserUnit("");                            // 承辦人單位
      mainQuery.setChannel("1");
      MainReply mainReply = blackListClient.executeMain(mainQuery);

      // 風險值結果輸出
      rs.setData(mainReply);
      rs.setRsStatus(ResultStatus.SUCCESS);
    }
    catch(Exception ex){
      rs.setExp(ex);
      rs.setRsStatus(ResultStatus.ERROR);
    }

    return rs;
  }
  
  /**
   * is法人?
   * @param id
   * @return
   */
  private boolean isCompany(String id) {
    boolean brs = false;
    String[] spCustNo = id.split("");
    if (!spCustNo[0].matches("[A-Z]+") && spCustNo.length == 8) brs = true;
    
    return brs;
  }

  public boolean check(String value) throws Throwable {
    return false;
  }

}
