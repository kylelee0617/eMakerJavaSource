package Sale.Sale05R090;

import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import javax.swing.*;

public class Sale05R090_AML_Q extends bTransaction {
  talk dbSale = getTalk("Sale");

  public boolean action(String value) throws Throwable {
    // 回傳值為 true 表示執行接下來的資料庫異動或查詢
    // 回傳值為 false 表示接下來不執行任何指令
    // 傳入值 value 為 "新增","查詢","修改","刪除","列印","PRINT" (列印預覽的列印按鈕),"PRINTALL"
    // (列印預覽的全部列印按鈕) 其中之一

    JTable jtable090 = getTable("Table090");
    String qProjectID1 = getQueryValue("projectID1").trim();
    String qposition = getQueryValue("position").trim();
    String qcustomName = getQueryValue("customName").trim();
    String qOrderDate = getQueryValue("orderDate").trim();
    String qcontractDate = getQueryValue("contractDate").trim();
    String qbigMoneyDate = getQueryValue("bigMoneyDate").trim();
    String qAMLDate = getQueryValue("AMLDate").trim();
    String qriskValue = getQueryValue("riskValue").trim();
    String qAMLNo = getQueryValue("aml").trim();

    // System.out.println(">>>" + qProjectID1);
    // System.out.println(">>>" + qposition);
    // System.out.println(">>>" + qcustomName);
    // System.out.println(">>>" + qOrderDate);
    // System.out.println(">>>" + qcontractDate);
    // System.out.println(">>>" + qbigMoneyDate);
    // System.out.println(">>>" + qAMLDate);

    if (qProjectID1.length() == 0) {
      message("[案別代碼] 不可空白!");
      return false;
    }
    if (qOrderDate.length() == 0) {
      message("[訂單日期] 不可空白!");
      return false;
    }

    // 檢核通過，把查詢值塞到畫面欄位
    setValue("projectID1", qProjectID1);

    // 0.訂單編號 1. 表單編號 2. 案別 3. 棟樓別 4. 車位別 5. 訂單日期 6. 客戶姓名
    StringBuffer sbSQL = new StringBuffer();
    sbSQL.append("select distinct T90.OrderNO, '', T90.ProjectID1 ");
    sbSQL.append(
        ", STUFF((SELECT ',' + CASE T92b.HouseCar WHEN 'House' THEN position END FROM Sale05M092 T92b WHERE T92b.orderNo = T92.orderNo FOR XML PATH('')), 1, 1, '') AS Houses ");
    sbSQL
        .append(", STUFF((SELECT ',' + CASE T92b.HouseCar WHEN 'Car' THEN position END FROM Sale05M092 T92b WHERE T92b.orderNo = T92.orderNo FOR XML PATH('')), 1, 1, '') AS car ");
    sbSQL.append(", T90.orderDate , (select top 1 T91.customName from Sale05M091 T91 where T90.orderNo = T91.orderNo) as customName ");
    sbSQL
        .append(", T90.SaleName1, T90.SaleName2 , T90.SaleName3 , T90.SaleName4 , T90.SaleName5 , T90.SaleName6 , T90.SaleName7 , T90.SaleName8 , T90.SaleName9 , T90.SaleName10 ");
    sbSQL.append(", (select top 1 T92.TrxDate from Sale05M092 T92c where T92c.orderNo = T92.orderNo and T92c.StatusCd = 'D' order by T92c.TrxDate asc) as TrxDate ");
    sbSQL.append(", (select top 1 T92.StatusCd from Sale05M092 T92d where T92d.orderNo = T92.orderNo and T92d.StatusCd = 'D' order by T92d.TrxDate asc) as StatusCd ");
    sbSQL.append(
        ", sum(T92.pingsu) as pingsu , sum(T92.DealMoney) as DealMoney , sum(T92.ViMoney) as ViMoney , sum(T92.CommMoney) as CommMoney ,sum(T92.CommMoney1) as CommMoney1 , sum(T92.FloorPrice) as FloorPrice ");
    sbSQL.append(", T90.FundsSrc , (select distinct useType from A_Usage Tuse where Tuse.useId = T90.useID) as UseType ");
    sbSQL.append(", sum(T92.GiftMoney) as GiftMoney ");
    sbSQL.append("from Sale05M090 T90  , Sale05M092 T92 ");
    sbSQL.append("where T90.OrderNo = T92.OrderNo ");
    sbSQL.append("and T90.projectID1  = '" + qProjectID1 + "' ");
    sbSQL.append("and T90.orderDate between " + formatDate(qOrderDate) + " ");

    if (qposition.length() != 0) {
      sbSQL.append("and  T92.position = '" + qposition + "' ");
    }
    if (qcustomName.length() != 0) {
      sbSQL.append("and T91.customName like '%" + qcustomName + "%' ");
    }
    if (qcontractDate.length() != 0) {
      sbSQL.append("and T90.orderNo in ( ");
      sbSQL.append("select distinct orderNo from Sale05M275_new T275 , Sale05M274 T274 where  1=1 ");
      sbSQL.append("and T274.contractNo = T275.contractNo ");
      sbSQL.append("and T274.isVoid = 'N' ");
      sbSQL.append("and T275.lastDateTime between " + formatDate(qcontractDate) + " ) ");
    }
    if (qbigMoneyDate.length() != 0) {
      sbSQL.append("and T90.orderNo in ( ");
      sbSQL.append("select distinct orderNo from Sale05M086 where docNo in (select docNo from Sale05M080 T80 where 1=1 ");
      sbSQL.append("and T80.reportBigMoney = 'Y' and T80.EDate between " + formatDate(qbigMoneyDate) + ") ) ");
    }
    if (qAMLDate.length() > 0 || qAMLNo.length() > 0) {
      sbSQL.append("and T90.orderNo in ( select distinct orderNo from Sale05M070 where 1=1 ");

      if (qAMLDate.length() > 0) {
        sbSQL.append("and EDate between " + formatDate(qAMLDate));
      }
      if (qAMLNo.length() > 0) {
        sbSQL.append("and SHB06B = '" + qAMLNo + "' ");
      }
      sbSQL.append(" ) ");
    }
    if (qriskValue.length() != 0) {
      sbSQL
          .append("and T90.orderNo in ( select distinct orderNo from Sale05M091 T91 where riskValue = '" + qriskValue + "' and ( T91.StatusCd != 'C' or T91.StatusCd is null ) ) ");
    }

    sbSQL.append(
        "group by T90.OrderNO, T92.OrderNO, T90.ProjectID1, T90.orderDate, T90.SaleName1, T90.SaleName2 , T90.SaleName3 , T90.SaleName4 , T90.SaleName5 , T90.SaleName6 , T90.SaleName7 , T90.SaleName8 , T90.SaleName9 , T90.SaleName10 ");
    sbSQL.append(",T92.TrxDate, T92.StatusCd, T90.FundsSrc, T90.useID ");
    sbSQL.append("order by T90.orderNo ASC ");
    String[][] retSale05M090Mix = dbSale.queryFromPool(sbSQL.toString());

    if (retSale05M090Mix.length == 0) {
      message("查無資料!!");
    }
    setTableData("table090", retSale05M090Mix);

    return false;
  }

  private String formatDate(String dayStr) throws Throwable {
    String[] spDay = dayStr.split(" and ");
    String bDay = spDay[0].trim();
    String eDay = spDay[1].trim();

    String newBDay = "'" + bDay.substring(0, 4) + '/' + bDay.substring(4, 6) + '/' + bDay.substring(6, 8) + "'";
    String newEDay = "'" + eDay.substring(0, 4) + '/' + eDay.substring(4, 6) + '/' + eDay.substring(6, 8) + "'";

    return newBDay + " and " + newEDay;
  }

  public String getInformation() {
    return "---------------\u67e5\u8a62\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
