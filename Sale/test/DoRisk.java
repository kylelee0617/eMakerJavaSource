package Sale.test;

import org.apache.commons.lang3.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.Result;
import Farglory.util.ResultStatus;
import Farglory.util.RiskCheckBean;
import Farglory.util.RiskCheckRS;
import Farglory.util.RiskCheckTool2;
import Farglory.util.TalkBean;
import jcx.db.talk;
import jcx.jform.sproc;

public class DoRisk extends sproc {
  String 史大Date;
  String 安得Date;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbEIP;
  talk dbEMail;
  talk dbPW0D;
  String serverType;
  boolean isTest = true;
  String[][] retRsTable;

  public String getDefaultValue(String value) throws Throwable {
    dbSale = getTalk("Sale");
    dbPW0D = getTalk("pw0d");
    dbAS400 = getTalk("400CRM");
    dbEIP = getTalk("EIP");
    dbEMail = getTalk("eMail");
    serverType = get("serverType").toString().trim();
    
    TalkBean tBean = new TalkBean();
    tBean.setDbSale(dbSale);
    tBean.setDb400CRM(dbAS400);
    tBean.setDbPw0D(dbPW0D);
    tBean.setDbEIP(dbEIP);
    tBean.setDbEMail(dbEMail);
    ksUtil = new KSqlUtils(tBean);
    kUtil = new KUtils();

    // 設定環境
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>環境:" + serverType);

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
    kUtil.info("DoRisk 執行");
    
    String tempValues = this.getValue("TempSend");
    String[] tempValue = tempValues.split(",");
    String orderNo = tempValue[0].trim();
    String orderDate = tempValue[1].trim();
    String projectId1 = tempValue[2].trim();
    kUtil.info("tempValues:" + tempValues);
    
    RiskCheckBean rcBean = new RiskCheckBean();
    rcBean.setUserNo(this.getUser());
    rcBean.setUpd070Log(true);
    rcBean.setDbSale(dbSale);
    rcBean.setDbEMail(dbEMail);
    rcBean.setDb400CRM(dbAS400);
    rcBean.setDbEIP(dbEIP);
    rcBean.setOrderNo(orderNo);
    rcBean.setOrderDate(orderDate);
    rcBean.setProjectID1(projectId1);
    rcBean.setActionText("再來一次");
    
    String sqlCust = "select DISTINCT a.OrderNo,RecordNo,auditorship,Nationality,CountryName,CustomNo,CustomName,Percentage,Birthday,a.MajorName,PositionName,ZIP,City,Town,Address,Cellphone,Tel,Tel2,eMail,IsLinked,IsControlList,IsBlackList,TrxDate,StatusCd,industryCode "
        + "from Sale05M091 a , Sale05M090 b " 
        + "where a.OrderNo = b.OrderNo " 
        + "and a.OrderNo = '"+orderNo+"' ";
        //+ "and ISNULL(a.StatusCd, '') != 'C'";
    String[][] retCust = dbSale.queryFromPool(sqlCust);
    rcBean.setRetCustom(retCust);
    
    String sqlBen = "select OrderNo,CustomNo,RecordNo,BenName,BCustomNo,Birthday,CountryName,HoldType,IsBlackList,IsControlList,IsLinked,TrxDate,StatusCd "
                  + "from Sale05M091Ben where OrderNo = '"+orderNo+"' "
                  + "and ISNULL(StatusCd, '') != 'C'";
    String[][] retBen = dbSale.queryFromPool(sqlBen);
    rcBean.setRetSBen(retBen);
    
    RiskCheckTool2 rcTool = new RiskCheckTool2(rcBean);
    Result rs = rcTool.processRisk();
    
    if(StringUtils.equals(ResultStatus.ERROR[0], rs.getRsStatus()[0])) {
      kUtil.info("DoRisk Error");
      this.setValue("TempResult", rs.getReturnMsg());
    }
    
    String logMsg = "舊程式重新計算風險值>>> orderNo:" + orderNo + ", Msg:" + ((RiskCheckRS)rs.getData()).getRsMsg();
    ksUtil.setSaleLog("RD隨意查", "舊程式重新計算風險值", this.getUser(), logMsg);
  }

  public boolean 欄位檢核() throws Throwable {
    return true;
  }

  public String getInformation() {
    return "---------------ReCheckRisk DONE----------------";
  }
}
