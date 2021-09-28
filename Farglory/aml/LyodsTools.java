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
 * for �ܴ�
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
  String sysType = "RYB";// ���ʲ���PB �P��C
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
   * �g�J�q��-�Ȥ�A�ç�s���p�H
   * @return
   * @throws Throwable
   */
  public Result renewRelated() throws Throwable{
    Result rs = new Result();
    RiskCustomBean cBean = lyodsBean.getCustBean();
    String custNo = cBean.getCustomNo();
    
    BlackListClient blackListClient = new BlackListClient(lyodsSoapURL);  
    RenewRelatedQuery renewRelatedQuery = new RenewRelatedQuery();        
    renewRelatedQuery.setWriteLog("Y");                                   //�g�JLOG v
    renewRelatedQuery.setCalculationCode(lyodsBean.getCalculationCode()); //�O�_�p�⭷�I�����N�X
    renewRelatedQuery.setAddCustomer(lyodsBean.getAddCustomer());         //�s�W�ε��P�D�n�Ȥ� Y.�s�W N.���P
    renewRelatedQuery.setCustomerType(this.isCompany(custNo)? "2":"1");   //�D�n�Ȥ����O v
    renewRelatedQuery.setId(custNo);                                      //�����Ҧr��/�νs v
    renewRelatedQuery.setKeyNumber(lyodsBean.getOrderNo());               //�O�渹�X/�������X v
    renewRelatedQuery.setCaseNumber(lyodsBean.getOrderNo());              //�ץ�s�� v
    renewRelatedQuery.setSystemCode("RYB");                               //�t�ΥN�X v
    renewRelatedQuery.setProgramCode("RYB");                              //�{���N�X v
    renewRelatedQuery.setUserId(this.empNo);                              //�ӿ�H���s v
    renewRelatedQuery.setUserUnit("");                                    //�ӿ�H��� v
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

    // 210114 Kyle : ��~�O
    String industryCode = cBean.getIndustryCode() != null ? cBean.getIndustryCode() : "";
    if(StringUtils.isBlank(industryCode)) industryCode = kSqlUtil.getIndustryCodeByMajorName(cBean.getMajorName());

    // �k�H �۵M�H �ʧO
    String type = kUtil.getUserType(custNo).replaceAll("N", "1").replaceAll("C", "2");
    String sex = cBean.getqBean().getSex();

    // ���y��X(�Y�Źw�]999)
    String cnyCode = kSqlUtil.getNationCodeByName(cBean.getCountryName());
    String cnyCode2 = kSqlUtil.getNationCodeByName(cBean.getCountryName2());

    // �ո�ơASOAP
    try {
      BlackListClient blackListClient = new BlackListClient(lyodsSoapURL);
      MainQuery mainQuery = new MainQuery();
      mainQuery.setRiskResult(lyodsBean.getRiskResult());   // �d���I�� v
      mainQuery.setCheckAll(lyodsBean.getCheckAll());       // �d�Ҧ����O v
      mainQuery.setChangeOrgnization("Y");                  // ��������� v
      mainQuery.setAddCustomer(lyodsBean.getAddCustomer()); // �s�W�ε��P�D�n�Ȥ� v
      mainQuery.setAddAccount(lyodsBean.getAddAccount());   // �s�W�ε��P�O�� v
      mainQuery.setWriteLog("Y");                           // �g�JLOG v
      mainQuery.setModifyData(lyodsBean.getModifyData());   // ��s�Ȥ���
      mainQuery.setCustomerType(type);                      // �D�n�Ȥ����O v
      mainQuery.setChineseName(custName);                   // ����m�W
      mainQuery.setEnglishName(cBean.getEngName());         // �^��m�W
      mainQuery.setId(custNo);                              // �����Ҧr�� v
      mainQuery.setSex(sex);                                // �ʧO
      mainQuery.setBirth(cBean.getBirthday().replaceAll("/", "").replaceAll("-", "")); // �ͤ�
      mainQuery.setRegisterNation(cnyCode);                 // �~��a���y  �Pnation1 => �����ܴ����u��a/�a��(��v)�v
      mainQuery.setBirthNation(cnyCode2);                   // �X�ͦa���y  �Pnation2 => �����ܴ����u���y/�a�ϡv
      mainQuery.setProfession(cBean.getProfessionCode1());  // ¾�~����
      mainQuery.setProfession2(cBean.getProfessionCode2()); // ��¾�~����
      mainQuery.setIndustry(industryCode);                  // ��~���O
      mainQuery.setIndustry2("");                           // ����~���O
      mainQuery.setKeyNumber(lyodsBean.getOrderNo());       // �O�渹�X/�������X v
      mainQuery.setCaseNumber(lyodsBean.getOrderNo());      // �ץ�s�� v (��q��s���@�˧Y�i)
      mainQuery.setSystemCode(this.sysType);                // �t�ΥN�X V (��{���N�X�@�˧Y�i)
      mainQuery.setProgramCode(this.sysType);               // �{���N�X v
      mainQuery.setApplyDate("");                           // ���z/�ӽФ��
      mainQuery.setContractDate(lyodsBean.getOrderDate().replaceAll("/", "").replaceAll("-", ""));  // �����ͮĤ��
      mainQuery.setProduct("RYB");                             // ���~�N�X
      mainQuery.setChannel("");                             // �q���N�X
      mainQuery.setUserId(this.empNo);                      // �ӿ�H���s v
      mainQuery.setUserUnit("");                            // �ӿ�H���
      mainQuery.setChannel("1");
      MainReply mainReply = blackListClient.executeMain(mainQuery);

      // ���I�ȵ��G��X
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
   * is�k�H?
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
