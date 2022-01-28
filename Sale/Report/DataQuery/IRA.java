package Sale.Report.DataQuery;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import org.apache.commons.lang.StringUtils;
import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bproc;

public class IRA extends bproc{
  KSqlUtils ksUtil = new KSqlUtils();
  KUtils kUtil = new KUtils();
  
  //param
  String year = "";
  
  public String getDefaultValue(String value)throws Throwable{  
    
    //TODO: 查詢條件
    year = this.getValue("行銷-Year").trim();
    
    //TODO: 查詢資料
    String[][] retTable = this.getMainData();

    //從queryLOG補充缺少的資料
    int tableCount = 0;
    for(int i=0; i<retTable.length; i++) {
      String projectId = retTable[i][1].trim();
      String custNoss = retTable[i][17].trim(); //17  19
      String benNoss = retTable[i][27].trim();  //27  29  31
      
      //客戶
      if(StringUtils.isNotBlank(custNoss)) {  //有客戶資料
        String[] custNos = custNoss.split(","); //切
        
        StringBuilder sbCustNo = new StringBuilder(); 
        for(int c=0; c<custNos.length; c++) { //每一個人處理
          if(sbCustNo.length() > 0) sbCustNo.append(" , ");
          
          QueryLogBean qBean = ksUtil.getQueryLogLike3(projectId, custNos[c].trim());
          if(qBean == null) {
            KUtils.info(projectId + "/" + custNos[c].trim() + " 黑名單無資料");
            continue;
          }
          
          String sexCode = qBean.getSex();
          if(StringUtils.equals(sexCode, "M")) {
            sbCustNo.append("男");
          }else if(StringUtils.equals(sexCode, "F")) {
            sbCustNo.append("女");
          }
        }
        retTable[i][19] = sbCustNo.toString();  //回放陣列
      }
      
      //實受人
      if(StringUtils.isNotBlank(benNoss)) { //有實受人資料
        String[] benNos = benNoss.split(","); //切
        
        StringBuilder sbBenNo = new StringBuilder();
        for(int c=0; c<benNos.length; c++) {  //每一個人處理
          if(sbBenNo.length() > 0) sbBenNo.append(" , ");
          
          QueryLogBean qBean = ksUtil.getQueryLogLike3(projectId, benNos[c].trim());
          if(qBean == null) {
            KUtils.info(projectId + "/" + benNos[c].trim() + " null");
            continue;
          }
          //性別
          String sexCode = qBean.getSex();
          if(StringUtils.equals(sexCode, "M")) {
            sbBenNo.append("男");
          }else if(StringUtils.equals(sexCode, "F")) {
            sbBenNo.append("女");
          }
          retTable[i][29] = sbBenNo.toString();
          //職業(code轉name)
          retTable[i][31] = ksUtil.getNameByIndCode(qBean.getJobType());
        } 
      }
      
      tableCount++;
    }
    this.setValue("TableCount", "共" + tableCount + "筆");
    
    //TODO: 寫表格
    //表頭
    String tmpHeader = "訂單編號、案別、戶別、車位別、成交金額(萬)、付訂日、簽約日、"
        + "收款日期、收款金額(元)、信用卡(元)、現金(元)、銀行(元)、票據(元)、折讓金額、折讓原因、"
        + "退戶日、退戶原因、退款金額、"
        + "客戶國籍、客戶ID、客戶姓名、客戶性別、客戶出生日期、客戶業別、客戶風險值、客戶PEPS、客戶黑名單、客戶控管名單、"
        + "實受人國籍、實受人ID、實受人姓名、實受人性別、實受人出生日期、實受人業別、實受人PEPS、實受人黑名單、實受人控管名單";
    String[] tableHeader = tmpHeader.split("、");
    JTable tb1 = getTable("ResultTable");
    tb1.setName("IRA 問卷數據");
    this.setTableHeader("ResultTable", tableHeader);

    //框NAME
    this.setValue("TableName", "IRA 問卷數據");
    
    this.setTableData("ResultTable", retTable);   //表身，主資料
    
    //sale log
    ksUtil.setSaleLog(this.getFunctionName(), "IRA Data", this.getUser(), "查詢IRA資料成功");
    
    return value;
  }
  
  public String[][] getMainData() throws Throwable{
    String sql = "select DISTINCT " + 
        "a.OrderNo as 訂單編號" + 
        ", a.ProjectID1 AS 案別" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 3, '') , '') as 棟樓別" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 3, '') , '') as 車位別" +
        ", ISNULL( (SELECT SUM(aa.DealMoney) FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and HouseCar='house' GROUP BY aa.OrderNo) , 0) + ISNULL( (SELECT SUM(aa.DealMoney) FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and HouseCar='car' GROUP BY aa.OrderNo) , 0) as 成交金額" +
        ", a.orderdate as 付訂日" + 
        ",ISNULL( (select top 1 aa.contractDate  from Sale05M274 aa , Sale05M278 bb where aa.ContractNo=bb.ContractNo and bb.OrderNo=a.OrderNo order by aa.ContractDate desc) , '') AS 簽約日" +
        ", b.EDate as 收款日期 " +
        ", b.ReceiveMoney as 收款金額 " +
        ",ISNULL((select sum(aa.CreditCardMoney) from sale05m083 aa where aa.docNo = b.DocNo), '0') as 信用卡 " +
        ", b.CashMoney as 現金 " +
        ",ISNULL((select sum(aa.BankMoney) from sale05m328 aa where aa.docNo = b.DocNo), '0') as 銀行 " + 
        ",ISNULL((select sum(aa.CheckMoney) from sale05m082 aa where aa.docNo = b.DocNo), '0') as 票據 " +
        ", b.DiscountTotalMoney as 折讓金額 " +
        ", b.DiscountReason as 折讓原因 " +
        ",ISNULL( (select TrxDate from Sale05M094 aa where aa.OrderNo=a.OrderNo) , '') AS 退戶日期" + 
        ",ISNULL( (select top 1 (select top 1 aaa.BItemName from Sale05M230 aaa where aaa.BItemCd=aa.BItemCd) + '-' + (select top 1 aaa.MItemName from Sale05M231 aaa where aaa.MItemCd=aa.MItemCd) + '-' + (select top 1 aaa.SItemName + '-' + aaa.DItemName from Sale05M233 aaa where aaa.MItemCd=aa.MItemCd and aaa.SItemCd=aa.SItemCd and aaa.DItemCd=aa.DItemCd) from Sale05M094 aa where aa.OrderNo=a.OrderNo order by TrxDate desc) , '') AS 退戶原因" + 
        ",ISNULL( (select top 1 aa.Amt from Sale05M094 aa where aa.OrderNo=a.OrderNo order by TrxDate desc) , '') AS 退款金額" +
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.CountryName) FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 客戶國籍" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.customNo FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '客戶ID'" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '客戶Name'" +
        ", '' as 客戶性別" + 
        ",ISNULL( STUFF( (SELECT ',' + aa.Birthday FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 客戶生日" + 
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.MajorName) FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 客戶職業" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.RiskValue FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 客戶風險值" + 
        ", case when (select count(*) from Sale05M070 aa where aa.SHB06B = '021' and RecordDesc like '客戶%' and aa.OrderNo = a.orderNo) > 0 then 'Y' else '' end as 客戶PEPS" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsBlackList FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 客戶黑名單" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsControlList FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 客戶控管名單" + 
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.CountryName) FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 實受人國籍" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.BCustomNo FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '實受人ID'" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.BenName FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '實受人Name'" +
        ", '' as 實受人性別" + 
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.Birthday) FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 實受人生日" + 
        ", '' as 實受人職業" + 
        ", case when (select count(*) from Sale05M070 aa where aa.SHB06B = '021' and RecordDesc like '實質受益人%' and aa.OrderNo = a.orderNo) > 0 then 'Y' else '' end as 實受人PEPS" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsBlackList FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 實受人黑名單" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsControlList FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as 實受人控管名單 " + 
        "from Sale05M090 a , Sale05M080 b , Sale05M086 c " +
        "where b.DocNo = c.DocNo and a.OrderNo = c.OrderNo " + 
        "and a.OrderDate BETWEEN '"+year+"/01/01' AND '"+year+"/12/31' " + 
        "order by a.OrderNo ASC ";
    String[][] retTable = ksUtil.getTBean().getDbSale().queryFromPool(sql);
    
    return retTable;
  }
  
  
  public String getInformation(){
    return "---------------Query(\u67e5\u8a62).defaultValue()----------------";
  }
}
