package Sale.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;

public class Data221004 extends bproc {
  String 史大Date;
  String 安得Date;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  String serverType;
  boolean isTest = true;

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    serverType = get("serverType").toString().trim();
    史大Date = this.getValue("StartDate").trim();
    安得Date = this.getValue("EndDate").trim();

    // 設定環境
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>環境:" + serverType);

    if (!this.欄位檢核()) return value;

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("不動產行銷警示訊息清單");

    String[] title = { "訂單編號", "收款編號", "交易日期", "案別", "棟樓別", "車位別", "客戶姓名", "受檢人ID", "受檢人姓名"
        , "功能別", " 疑似洗錢交易種類", " 疑似洗錢態樣內容", "繳款金額", "繳款方式", "匯款地", "代理人", "關係", "代理原因" };
    this.setTableHeader("ResultTable", title);
    
    String sql = "select DISTINCT   " //購屋證明單 - 客戶
        + "t70.OrderNo as 訂單編號,  " 
        + "t70.DocNo as 收款編號,   " 
        + "t70.OrderDate as 交易日期,   " 
        + "t70.ProjectID1 as 案別,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別,  " 
        + "t70.CustomName as 客戶姓名,  " 
        + "t70.CustomID as 受檢人ID,  " 
        + "t70.CustomName as 受檢人姓名,  " 
        + "t70.Func as 功能別,   " 
        + "t70.RecordType as 疑似洗錢交易種類,   " 
        + "t70.RecordDesc as 疑似洗錢態樣內容,   " 
        + "'' as 繳款金額,   " 
        + "'' as 繳款方式,   " 
        + "'' as 匯款地,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理人,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 關係,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理原因  " 
        + "from Sale05M070 t70  " 
        + "where 1=1 "
//        + "and RecordDesc != '不適用' and RecordDesc != '不符合' and t70.Func like '購屋%' and t70.RecordType like '客戶%'  " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''   " 
        + "and charindex(t70.CustomName, ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE t70.OrderNo = aa.OrderNo FOR XML PATH('')) , 1, 1, '') , '') ) > 0  " 
        + "and t70.CustomName != ''  " 
        + "AND ISNULL(t70.OrderDate , '') BETWEEN '" + 史大Date + "' AND '" + 安得Date + "'  " 
//        + "AND RecordDesc not like '風險值:%'  " 
        + "  " 
        + "UNION   "  //購屋證明單 - 實受人
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.OrderDate as 交易日期, t70.ProjectID1 as 案別,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as 客戶姓名,  " 
        + "t70.CustomID as 受檢人ID,  " 
        + "t70.CustomName as 受檢人姓名,  " 
        + "t70.Func as 功能別, t70.RecordType as 疑似洗錢交易種類, t70.RecordDesc as 疑似洗錢態樣內容,   " 
        + "'' as 繳款金額,   " 
        + "'' as 繳款方式,   " 
        + "'' as 匯款地,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理人,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 關係,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理原因  " 
        + "from Sale05M070 t70  " 
        + "where 1=1 "
//        + "and RecordDesc != '不適用' and RecordDesc != '不符合' and t70.Func like '購屋%' and t70.RecordType like '實質受益人%'  " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''   " 
        + "and charindex(t70.CustomName, ISNULL( STUFF( (SELECT ',' + aa.benName FROM Sale05M091ben aa WHERE t70.OrderNo = aa.OrderNo FOR XML PATH('')) , 1, 1, '') , '') ) > 0  " 
        + "and t70.CustomName != ''  " 
        + "AND ISNULL(t70.OrderDate , '') BETWEEN '" + 史大Date + "' AND '" + 安得Date + "'  " 
//        + "AND RecordDesc not like '風險值:%'  " 
        + "  " 
        + "UNION   "  //購屋證明單 - 代理人
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.OrderDate as 交易日期, t70.ProjectID1 as 案別,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as 客戶姓名,  " 
        + "t70.CustomID as 受檢人ID,  " 
        + "t70.CustomName as 受檢人姓名,  " 
        + "t70.Func as 功能別, t70.RecordType as 疑似洗錢交易種類, t70.RecordDesc as 疑似洗錢態樣內容,   " 
        + "'' as 繳款金額,   " 
        + "'' as 繳款方式,   " 
        + "'' as 匯款地,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理人,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 關係,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理原因  " 
        + "from Sale05M070 t70  " 
        + "where 1=1 "
//        + "and RecordDesc != '不適用' and RecordDesc != '不符合' and t70.Func like '購屋%' and t70.RecordType like '代理人%'  " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''   " 
        + "and charindex(t70.CustomName, ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE t70.OrderNo = aa.OrderNo FOR XML PATH('')) , 1, 1, '') , '') ) > 0  " 
        + "and t70.CustomName != ''  " 
        + "AND ISNULL(t70.OrderDate , '') BETWEEN '" + 史大Date + "' AND '" + 安得Date + "'  " 
        + "AND RecordDesc not like '風險值:%'  " 
        + "  " 
        + "UNION   "  //收款
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.EDate as 交易日期, t70.ProjectID1 as 案別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as 客戶姓名,  " 
        + "t70.CustomID as 受檢人ID,  " 
        + "t70.CustomName as 受檢人姓名,  " 
        + "t70.Func as 功能別, t70.RecordType as 疑似洗錢交易種類, t70.RecordDesc as 疑似洗錢態樣內容,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '現金' then t80.CashMoney   " 
        + "when '信用' then t80.CreditCardMoney   " 
        + "when '銀行' then t80.BankMoney   " 
        + "when '票據' then t80.CheckMoney   " 
        + "else ''   " 
        + "end ) as 繳款金額,   " 
        + "SUBSTRING(t70.RecordType, 1, 2) as 繳款方式,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '銀行' then (select top 1 ExportingPlace from Sale05M328 t328 where t328.DocNo=t70.DocNo)   " 
        + "else ''  " 
        + "end ) as 匯款地,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '現金' then t80.DeputyName   " 
        + "when '信用' then (select top 1 DeputyName from Sale05M083 aa  where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '銀行' then (select top 1 DeputyName from Sale05M328 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '票據' then (select top 1 DeputyName from Sale05M082 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "else ''  " 
        + "end ) as 代理人,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '現金' then t80.DeputyRelationship   " 
        + "when '信用' then (select top 1 DeputyRelationship from Sale05M083 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '銀行' then (select top 1 DeputyRelationship from Sale05M328 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)   " 
        + "when '票據' then (select top 1 DeputyRelationship from Sale05M082 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)   " 
        + "else ''  " 
        + "end ) as 關係,  " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '現金' then t80.Reason   " 
        + "when '信用' then (select top 1 Reason from Sale05M083 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '銀行' then (select top 1 Reason from Sale05M328 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '票據' then (select top 1 Reason from Sale05M082 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "else ''  " 
        + "end ) as 代理原因  " 
        + "from Sale05M070 t70  " 
        + "left join sale05m080 t80 on t70.DocNo = t80.DocNo   " 
        + "where 1=1 "
//        + "and RecordDesc != '不適用' and RecordDesc != '不符合' and t70.Func like '收款%'   " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "and ISNULL( STUFF( (SELECT ',' + aa.DocNo FROM Sale05M080 aa WHERE t70.DocNo = aa.DocNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "AND ISNULL(t70.EDate , '') BETWEEN '" + 史大Date + "' AND '" + 安得Date + "'  " 
        + "and RecordDesc not like '%同一客戶3個營業日內，累計繳交現金超過50萬元%'   " 
        + "and RecordDesc not like '%該客戶為疑似黑名單對象%'  " 
        + "and RecordDesc not like '%同一客戶同一營業日內2筆(含)以上包含現金%'   " 
        + "and RecordDesc not like '%客戶支付不動產交易之款項%'  " 
        + "  " 
        + "UNION   " //合約-被委託人
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.CDate as 交易日期, t70.ProjectID1 as 案別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as 客戶姓名,  " 
        + "t70.CustomID as 受檢人ID,  " 
        + "t70.CustomName as 受檢人姓名,  " 
        + "t70.Func as 功能別, t70.RecordType as 疑似洗錢交易種類, t70.RecordDesc as 疑似洗錢態樣內容,   " 
        + "'' as 繳款金額,   " 
        + "'' as 繳款方式,   " 
        + "'' as 匯款地,   " 
        + "(SELECT top 1 aa.TrusteeName FROM Sale05M355 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.TrusteeId = t70.CustomID)  as 代理人,  " 
        + "(SELECT top 1 aa.Relation FROM Sale05M355 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.TrusteeId = t70.CustomID) as 關係,  " 
        + "(SELECT top 1 aa.Reason FROM Sale05M355 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.TrusteeId = t70.CustomID) as 代理原因  " 
        + "from Sale05M070 t70  " 
        + "where RecordDesc != '不適用' and RecordDesc != '不符合' and t70.Func like '合約%' and RecordType like '%附件-被委託人%'  " 
        + "AND ISNULL( STUFF( (SELECT ',' +  t355.TrusteeName  from Sale05M355 t355 join Sale05M275_New t275 on t355.ContractNo = t275.ContractNo  and t275.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "AND ISNULL(t70.CDate , '') BETWEEN '" + 史大Date + "' AND '" + 安得Date + "'  " 
        + "  " 
        + "UNION   " //合約-指定第三人
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.CDate as 交易日期, t70.ProjectID1 as 案別, " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別," 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別," 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as 客戶姓名," 
        + "t70.CustomID as 受檢人ID," 
        + "t70.CustomName as 受檢人姓名," 
        + "t70.Func as 功能別, t70.RecordType as 疑似洗錢交易種類, t70.RecordDesc as 疑似洗錢態樣內容, " 
        + "'' as 繳款金額, " 
        + "'' as 繳款方式, " 
        + "'' as 匯款地, " 
        + "(SELECT top 1 aa.DesignatedName FROM Sale05M356 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.DesignatedId = t70.CustomID)  as 代理人, " 
        + "(SELECT top 1 aa.Relation FROM Sale05M356 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.DesignatedId = t70.CustomID) as 關係, " 
        + "(SELECT top 1 aa.Reason FROM Sale05M356 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.DesignatedId = t70.CustomID) as 代理原因 " 
        + "from Sale05M070 t70 " 
        + "where 1=1 "
//        + "and RecordDesc != '不適用' and RecordDesc != '不符合' and t70.Func like '合約%' and RecordType like '%附件-指定第三人%' " 
        + "AND ISNULL( STUFF( (SELECT ',' +  t356.DesignatedName  from Sale05M356 t356 join Sale05M275_New t275 on t356.ContractNo = t275.ContractNo  and t275.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "AND ISNULL(t70.CDate , '') BETWEEN '" + 史大Date + "' AND '" + 安得Date + "'  " 
        + "  " 
        + "UNION   "  //退戶
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.EDate as 交易日期, t70.ProjectID1 as 案別,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as 客戶姓名,  " 
        + "t70.CustomID as 受檢人ID,  " 
        + "t70.CustomName as 受檢人姓名,  " 
        + "t70.Func as 功能別, t70.RecordType as 疑似洗錢交易種類, t70.RecordDesc as 疑似洗錢態樣內容,   " 
        + "'' as 繳款金額,   " 
        + "'' as 繳款方式,   " 
        + "'' as 匯款地,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理人,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 關係,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as 代理原因  " 
        + "from Sale05M070 t70  " 
        + "where 1=1"
//        + "and RecordDesc != '不適用' and RecordDesc != '不符合' and t70.Func like '退戶%'  "  
        + "and (SELECT COUNT(t94.OrderNo) FROM Sale05M094 t94 WHERE t70.OrderNo = t94.OrderNo group by t94.OrderNo) > 0  " 
        + "AND ISNULL(t70.EDate , '') BETWEEN '" + 史大Date + "' AND '" + 安得Date + "' "
        + "order by t70.Func desc, t70.orderno, t70.ProjectID1 desc";
    String[][] ret = dbSale.queryFromPool(sql);
    
    this.setValue("TableCount", Integer.toString(ret.length));
    this.setTableData("ResultTable", ret);

    if (tb1.getRowCount() > 0) tb1.addRowSelectionInterval(0, 0);
  }

  public boolean 欄位檢核() throws Throwable {
    史大Date = this.getValue("StartDate").trim();
    安得Date = this.getValue("EndDate").trim();
    if (StringUtils.isBlank(史大Date) || StringUtils.isBlank(安得Date)) {
      message("日期!");
      return false;
    }

    if (史大Date.length() == 8) {
      if (!check.isACDay(史大Date)) return false;
      史大Date = kUtil.formatACDate(史大Date, "/");
    } else if (史大Date.length() == 10 && StringUtils.contains(史大Date, "/")) {

    } else {
      message("史大Date日期格式錯誤");
      return false;
    }

    if (安得Date.length() == 8) {
      if (!check.isACDay(安得Date)) return false;
      安得Date = kUtil.formatACDate(安得Date, "/");
    } else if (安得Date.length() == 10 && StringUtils.contains(安得Date, "/")) {

    } else {
      message("安得Date日期格式錯誤");
      return false;
    }

    return true;
  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
