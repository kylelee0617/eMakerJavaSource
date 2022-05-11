package Sale.Sale05M274;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import Farglory.aml.AMLyodsBean;
import Farglory.aml.RiskCheckTools_Lyods;
import Farglory.aml.RiskCustomBean;
import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import Farglory.util.Result;
import Farglory.util.ResultStatus;
import Farglory.util.RiskCheckRS;
import Farglory.util.TalkBean;
import jcx.db.talk;

public class CheckRiskNew2021 extends jcx.jform.sproc {

  public String getDefaultValue(String value) throws Throwable {
    System.out.println("Class >>> Sale.Sale05M274.ChekRiskNew");
    
    // config
    Map config = (HashMap) get("config");
    boolean isTest = "PROD".equals(config.get("serverType").toString()) ? false : true;
    String lyodsSoapURL = config.get("lyodsSoapURL").toString();

    talk dbSale = getTalk("Sale");
    talk dbEMail = getTalk("eMail");
    talk db400CRM = getTalk("400CRM");
    talk dbEIP = getTalk("EIP");
    talk dbPw0d = getTalk("pw0d");
    TalkBean tBean = new TalkBean();
    tBean.setDbSale(dbSale);
    tBean.setDbEMail(dbEMail);
    tBean.setDb400CRM(db400CRM);
    tBean.setDbEIP(dbEIP);
    tBean.setDbPw0D(dbPw0d);
    KUtils kUtil = new KUtils(tBean);
    KSqlUtils kSqlUtil = new KSqlUtils(tBean);

    // 畫面欄位
    String projectId = getValue("ProjectID1").trim(); // 案別代碼
    String contractDate = getValue("ContractDate").trim(); // 簽約日期
    String contractNo = getValue("ContractNoDisplay").trim(); // 合約編號
    boolean reCheckRisk = false; // 是否重新檢核
    String rsMsg = ""; // 顯示訊息
    String sql = "";

    String funcName = value.trim();
    String recordType = "指定第三人風險值計算結果";
    String actionText = getValue("text11").trim();

    // 訂單資訊
    String orderNo = kSqlUtil.getOrderNoByContractNo(contractNo);
    if (StringUtils.isBlank(orderNo)) {
      System.out.println(this.getClass().toString() + ": 查無訂單編號 by contractNo:" + contractNo);
      return value;
    }
    String orderDate = kSqlUtil.getOrderDateByOrderNo(orderNo);

    //建立AMLyodsBean物件
    AMLyodsBean aBean = new AMLyodsBean();
    aBean.setDbSale(dbSale);
    aBean.setDbEMail(dbEMail);
    aBean.setDb400CRM(db400CRM);
    aBean.setDbEIP(dbEIP);
    aBean.settBean(tBean);
    aBean.setEmakerUserNo(getUser());
    aBean.setOrderNo(orderNo);
    aBean.setOrderDate(orderDate);
    aBean.setContractNo(contractNo);
    aBean.setProjectID1(projectId);
    aBean.setActionName(actionText);
    aBean.setFunc(funcName);
    aBean.setRecordType(recordType);
    aBean.setUpdSale05M356(true);
    aBean.setUpd070Log(true);
    aBean.setSendMail(true);
    aBean.setTestServer(isTest);
    aBean.setLyodsSoapURL(lyodsSoapURL);
    
    
    // TODO: 1. 先執行指定第三人風險值判別
    System.out.println("====================ChekRiskNew2021 : start s1====================");
    sql = "SELECT DesignatedId, DesignatedName, ExportingPlace  FROM sale05m356 WHERE ContractNo = '" + contractNo + "'";
    String[][] retM356 = dbSale.queryFromPool(sql);
    if (retM356.length > 0) {
      RiskCustomBean[] cBeans = new RiskCustomBean[retM356.length];
      for (int ii = 0; ii < retM356.length; ii++) {
        String desNo = retM356[ii][0].toString().trim();
        String desName = retM356[ii][1].toString().trim();
        QueryLogBean qBean = kSqlUtil.getQueryLogByCustNoProjectId(projectId, desNo);
        if (qBean == null) {
          rsMsg += "指定第三人" + desName + "查無黑名單資料，請先執行黑名單查詢 \n";
          continue;
        }
        RiskCustomBean cBean = new RiskCustomBean();
        cBean.setCustomNo(desNo);
        cBean.setCustomName(desName);
        cBean.setCountryName(retM356[ii][2].toString().trim());
        cBean.setBirthday(qBean.getBirthday());
        cBean.setIndustryCode(qBean.getJobType());
        cBean.setqBean(qBean);
        cBeans[ii] = cBean;
      }
      RiskCheckTools_Lyods risk = new RiskCheckTools_Lyods(aBean);
      Result rs = risk.processRisk(cBeans);
      RiskCheckRS rcRs = (RiskCheckRS) rs.getData();
      if (StringUtils.isNotBlank(rcRs.getRsMsg())) {
        rsMsg += rcRs.getRsMsg();
      }
    }
    System.out.println("====================ChekRiskNew2021 : end s1====================");
    // End of 1.

    // TODO: 2. 執行客戶風險值檢核
    System.out.println("================ChekRiskNew2021 : start s2====================");
    long subDays = kUtil.subACDaysRDay(contractDate, orderDate);
    System.out.println("subDays>>>" + subDays);
    if (subDays >= 90) reCheckRisk = true; // 付訂日與簽約日超過90日，需重新檢核風險

    if (reCheckRisk) {
      sql = "select CustomNo, CustomName, Birthday, ZIP, City, Town, Address, Tel, Tel2, CountryName, PositionName "
          + "from Sale05M091 where orderNo = '" + orderNo + "' and ISNULL(statusCd, '') != 'C' ";
      String[][] retCustom = dbSale.queryFromPool(sql);
      RiskCustomBean[] cBeans2 = new RiskCustomBean[retCustom.length];
      for (int ii = 0; ii < retCustom.length; ii++) {
        String custNo = retCustom[ii][0].trim();
        RiskCustomBean cBean = new RiskCustomBean();
        QueryLogBean qBean = kSqlUtil.getQueryLogByCustNoProjectId(projectId, custNo);
        cBean.setCustomNo(custNo);
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
        cBean.setqBean(qBean);
        cBeans2[ii] = cBean;
      }
      aBean.setRecordType("客戶風險值重新檢核");

      // 建立風險值檢核物件
      RiskCheckTools_Lyods risk2 = new RiskCheckTools_Lyods(aBean);

      // 執行結果
      Result rs2 = risk2.processRisk(cBeans2);
      String[] rsStatus2 = rs2.getRsStatus();
      System.out.println("執行結果>>>" + rsStatus2[3]);
      if (!StringUtils.equals(rs2.getRsStatus()[3], ResultStatus.SUCCESS[3])) {
        System.out.println("發生錯誤了，賽鴿快推阿!!");
        return value;
      }

      RiskCheckRS rcRs2 = (RiskCheckRS) rs2.getData();
      System.out.println("萊斯執行結果>>>" + rcRs2.getRsMsg());

      // 輸出
      if (StringUtils.isNotBlank(rcRs2.getRsMsg())) rsMsg += rcRs2.getRsMsg();
    }
    System.out.println("====================ChekRiskNew2021 : end s2====================");

    if (StringUtils.isNotBlank(rsMsg)) messagebox(rsMsg);

    return value;
  }

  public String getInformation() {
    return "---------------CheckRiskNew2021----------------";
  }
}
