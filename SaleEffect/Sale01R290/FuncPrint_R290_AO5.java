package SaleEffect.Sale01R290;

import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import com.jacob.activeX.*;
import com.jacob.com.*;

import Farglory.util.FargloryUtil;

public class FuncPrint_R290_AO5 extends bTransaction {
  FargloryUtil fgUtil = new FargloryUtil();
  
  // 欄位檢核
  public boolean isBatchCheckOK() throws Throwable {
    int countOC = 0; // 付訂日跟簽約日必須要有一flag
    String retDate = "";
    int countDate = 0;

    // 付訂日期
    String orderDate1 = this.getValue("OrderDate1");
    if (!"".equals(orderDate1)) {
      retDate = fgUtil.getDateAC(orderDate1, "付訂日期(起)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("OrderDate1").requestFocus();
        return false;
      }
      setValue("OrderDate1", retDate);
      countDate++;
      countOC++;
    }
    String orderDate2 = this.getValue("OrderDate2");
    if (!"".equals(orderDate2)) {
      retDate = fgUtil.getDateAC(orderDate2, "付訂日期(迄)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("OrderDate2").requestFocus();
        return false;
      }
      setValue("OrderDate2", retDate);
      countDate++;
    }
    if (countDate == 1) {
      message("[付訂日期(起)(迄)] 須同時限制。");
      return false;
    }

    // 簽約日期
    String contrDate1 = this.getValue("ContrDate1");
    if (!"".equals(contrDate1)) {
      retDate = fgUtil.getDateAC(contrDate1, "簽約日期(起)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("ContrDate1").requestFocus();
        return false;
      }
      setValue("ContrDate1", retDate);
      countDate++;
      countOC++;
    }
    String contrDate2 = this.getValue("ContrDate1");
    if (!"".equals(contrDate2)) {
      retDate = fgUtil.getDateAC(contrDate2, "簽約日期(迄)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("ContrDate1").requestFocus();
        return false;
      }
      setValue("ContrDate1", retDate);
      countDate++;
    }
    if (countDate == 1) {
      message("[簽約日期(起)(迄)] 須同時限制。");
      return false;
    }

    // 付訂與簽約需擇一或共有
    if (countOC == 0) {
      message("[付訂日期] 與 [簽約日期] 必須擇一以上填寫");
      return false;
    }

    setValue("BuyerDate1", !"".equals(orderDate1) ? orderDate1 : contrDate1);
    setValue("BuyerDate2", !"".equals(orderDate2) ? orderDate2 : contrDate2);
    
    //依業績參照不同，必填付訂或簽約日期
    String dateType = this.getValue("dateType").trim();
    if("OrderDate".equals(dateType) ) {
      if(orderDate1.length() == 0 || orderDate2.length() == 0) {
        message("業績參照付訂日，則付訂日期不可為空");
        return false;
      }
    }else if("ContrDate".equals(dateType) ) {
      if(contrDate1.length() == 0 || contrDate2.length() == 0) {
        message("業績參照簽約日，則簽約日期不可為空");
        return false;
      }
    }
    
    return true;
  }
  
  public boolean action(String value) throws Throwable {
    
    //付訂&簽約 日期檢核
    if( !this.isBatchCheckOK() ) return false;
    
    // 付訂日期
    String stringOrderDate1 = getValue("OrderDate1").trim();
    String stringOrderDate2 = getValue("OrderDate2").trim();
    // 補足日期
    String stringEnougDate1 = getValue("EnougDate1").trim();
    String stringEnougDate2 = getValue("EnougDate2").trim();
    // 簽約日期
    String stringContrDate1 = getValue("ContrDate1").trim();
    String stringContrDate2 = getValue("ContrDate2").trim();
    // 合約會審
    String stringDateCheck1 = getValue("DateCheck1").trim();
    String stringDateCheck2 = getValue("DateCheck2").trim();
    // 簽約金到期
    String stringDateRange1 = getValue("DateRange1").trim();
    String stringDateRange2 = getValue("DateRange2").trim();
    //
    String stringKind = getValue("Kind").trim();
    // 成交日期
    String stringBuyerDate1 = getValue("BuyerDate1").trim();
    String stringBuyerDate2 = getValue("BuyerDate2").trim();
    if (stringBuyerDate1.length() == 0 || stringBuyerDate2.length() == 0) {
      message("成交日期 不可空白!");
      return false;
    }
    // 訂單日期
    String stringEDate1 = getValue("EDate1").trim();
    String stringEDate2 = getValue("EDate2").trim();
    // 賣方兌現
    String stringSellerCashDate1 = getValue("SellerCashDate1").trim();
    String stringSellerCashDate2 = getValue("SellerCashDate2").trim();
    // 買方兌現
    String stringBuyerCashDate1 = getValue("BuyerCashDate1").trim();
    String stringBuyerCashDate2 = getValue("BuyerCashDate2").trim();
    //
    String stringSaleKind = getValue("SaleKind").trim();
    //
    String stringCompanyNo = getValue("CompanyNo").trim();
    //業績參照
    String strDateType = this.getValue("dateType");
    
    // 日期處理
    Farglory.util.FargloryUtil exeFun = new Farglory.util.FargloryUtil();
    if (stringOrderDate1.length() > 0)
      setValue("OrderDate1", exeFun.getDateAC(stringOrderDate1, "付訂日期"));
    if (stringOrderDate2.length() > 0)
      setValue("OrderDate2", exeFun.getDateAC(stringOrderDate2, "付訂日期"));
    if (stringEnougDate1.length() > 0)
      setValue("EnougDate1", exeFun.getDateAC(stringEnougDate1, "補足日期"));
    if (stringEnougDate2.length() > 0)
      setValue("EnougDate2", exeFun.getDateAC(stringEnougDate2, "補足日期"));
    if (stringContrDate1.length() > 0)
      setValue("ContrDate1", exeFun.getDateAC(stringContrDate1, "簽約日期"));
    if (stringContrDate2.length() > 0)
      setValue("ContrDate2", exeFun.getDateAC(stringContrDate2, "簽約日期"));
    if (stringDateCheck1.length() > 0)
      setValue("DateCheck1", exeFun.getDateAC(stringDateCheck1, "合約會審"));
    if (stringDateCheck2.length() > 0)
      setValue("DateCheck2", exeFun.getDateAC(stringDateCheck2, "合約會審"));
    if (stringDateRange1.length() > 0)
      setValue("DateRange1", exeFun.getDateAC(stringDateRange1, "簽約金到期"));
    if (stringDateRange2.length() > 0)
      setValue("DateRange2", exeFun.getDateAC(stringDateRange2, "簽約金到期"));
    //
    if (stringBuyerDate1.length() > 0)
      setValue("BuyerDate1", exeFun.getDateAC(stringBuyerDate1, "成交日期"));
    if (stringBuyerDate2.length() > 0)
      setValue("BuyerDate2", exeFun.getDateAC(stringBuyerDate2, "成交日期"));
    if (stringEDate1.length() > 0)
      setValue("EDate1", exeFun.getDateAC(stringEDate1, "訂單日期"));
    if (stringEDate2.length() > 0)
      setValue("EDate2", exeFun.getDateAC(stringEDate2, "訂單日期"));
    if (stringSellerCashDate1.length() > 0)
      setValue("SellerCashDate1", exeFun.getDateAC(stringSellerCashDate1, "賣方兌現日"));
    if (stringSellerCashDate2.length() > 0)
      setValue("SellerCashDate2", exeFun.getDateAC(stringSellerCashDate2, "賣方兌現日"));
    if (stringBuyerCashDate1.length() > 0)
      setValue("BuyerCashDate1", exeFun.getDateAC(stringBuyerCashDate1, "買方兌現日"));
    if (stringBuyerCashDate2.length() > 0)
      setValue("BuyerCashDate2", exeFun.getDateAC(stringBuyerCashDate2, "買方兌現日"));
    //
    talk dbSale = getTalk("" + get("put_dbSale"));
    String stringSQL = "";
    String retData[][] = null;
    //
    Farglory.util.FargloryUtil exeUtil = new Farglory.util.FargloryUtil();
    long longTime1 = exeUtil.getTimeInMillis();
    //
    stringSQL = " speMakerSale01R290_AO5_COM2 " 
              + "'" + stringOrderDate1 + "'," 
              + "'" + stringOrderDate2 + "'," 
              + "'" + stringEnougDate1 + "'," 
              + "'" + stringEnougDate2 + "'," 
              + "'" + stringContrDate1 + "'," 
              + "'" + stringContrDate2 + "'," 
              + "'" + stringDateCheck1 + "'," 
              + "'" + stringDateCheck2 + "'," 
              + "'" + stringDateRange1 + "'," 
              + "'" + stringDateRange2 + "'," 
              + "'" + stringKind + "', " 
              + "'" + stringBuyerDate1 + "'," 
              + "'" + stringBuyerDate2 + "'," 
              + "'" + stringEDate1 + "'," 
              + "'" + stringEDate2 + "',"
              + "'" + stringSellerCashDate1 + "'," 
              + "'" + stringSellerCashDate2 + "'," 
              + "'" + stringBuyerCashDate1 + "'," 
              + "'" + stringBuyerCashDate2 + "'," 
              + "'" + stringSaleKind + "' , " 
              + "'" + stringCompanyNo + "'  "
              + ",'" + strDateType + "'  "
              + "WITH RECOMPILE";
    retData = dbSale.queryFromPool(stringSQL);
    if (retData.length == 0) {
      message("沒有資料!");
      return false;
    }
    
    //與AO資料做檢核  (依照[業績參照]改變日期區間)
    String strTypeDate1 = ( "ContrDate".equals(strDateType) )? stringContrDate1 : stringOrderDate1;
    String strTypeDate2 = ( "ContrDate".equals(strDateType) )? stringContrDate2 : stringOrderDate2;
    talk dbAO = getTalk("" + get("put_dbAO"));
    stringSQL = "Select [AgentDEPT4],[Agent_Num],[Agent_Name],SUM(CAST(TEL_V AS real)) AS TEL_V,SUM(CAST(DS_V AS real)) AS DS_V,SUM(CAST(Income_V AS real)) AS Income_V "
        + ",SUM(CAST(Friend_V AS real)) AS Friend_V,SUM(CAST(First_V AS real)) AS First_V,SUM(CAST(Repeat_V AS real)) AS Repeat_V "
        + " from AO_DayPerReportTempShow where (Date_Str between '" + strTypeDate1 + "' and '" + strTypeDate2
        + "') group by [AgentDEPT4], Agent_Num, Agent_Name order by [AgentDEPT4] ";
    String[][] retAOData = dbAO.queryFromPool(stringSQL);
    double tmpNum = 0;
    if (retAOData.length > 0) {
      for (int idx = 0; idx < retData.length; idx++) {
        for (int chkIdx = 0; chkIdx < retAOData.length; chkIdx++) {
          System.out.println("retData:" + retData[idx][2].trim() + " <==> retAOData:" + retAOData[chkIdx][2].trim());
          if (retData[idx][2].trim().equals(retAOData[chkIdx][2].trim())) {
            System.out.println("equals GO");
            retData[idx][3] = retAOData[chkIdx][3];
            tmpNum = Double.parseDouble(retData[idx][4].trim());
            if (tmpNum > 0) {
              retData[idx][5] = Double.toString(Double.parseDouble(retData[idx][3].trim()) / tmpNum);
            }
            retData[idx][7] = retAOData[chkIdx][4];
            tmpNum = Double.parseDouble(retData[idx][8].trim());
            if (tmpNum > 0) {
              retData[idx][9] = Double.toString(Double.parseDouble(retData[idx][7].trim()) / tmpNum);
            }
            retData[idx][11] = retAOData[chkIdx][5];
            tmpNum = Double.parseDouble(retData[idx][12].trim());
            if (tmpNum > 0) {
              retData[idx][13] = Double.toString(Double.parseDouble(retData[idx][11].trim()) / tmpNum);
            }
            retData[idx][15] = retAOData[chkIdx][6];
            tmpNum = Double.parseDouble(retData[idx][16].trim());
            if (tmpNum > 0) {
              retData[idx][17] = Double.toString(Double.parseDouble(retData[idx][15].trim()) / tmpNum);
            }
            // System.out.println("go caculate");
            // retData[idx][17] =
            // String.valueOf(Math.round(Double.parseDouble(retData[idx][15])/Double.parseDouble(retData[idx][16]))
            // );
            retData[idx][19] = retAOData[chkIdx][7];
            tmpNum = Double.parseDouble(retData[idx][20].trim());
            if (tmpNum > 0) {
              retData[idx][21] = Double.toString(Double.parseDouble(retData[idx][19].trim()) / tmpNum);
            }
            retData[idx][23] = retAOData[chkIdx][8];
            tmpNum = Double.parseDouble(retData[idx][24].trim());
            if (tmpNum > 0) {
              retData[idx][25] = Double.toString(Double.parseDouble(retData[idx][23].trim()) / tmpNum);
            }
          }
        }
      }
    }
    //
    Farglory.Excel.FargloryExcel exeExcel = new Farglory.Excel.FargloryExcel();
    Vector retVector = exeExcel.getExcelObject("G:\\資訊室\\Excel\\SaleEffect\\Sale01R290.xlt");
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);
    int intInsertDataRow = 2;
    String stringCondition = "";
    String stringBalaPrint = getValue("BalaPrint").trim();
    //
    stringCondition = "付訂日期:" + stringOrderDate1 + "∼" + stringOrderDate2;
    if (stringEnougDate1.length() > 0) {
      stringCondition += ";補足日期:" + stringEnougDate1 + "∼" + stringEnougDate2;
    }
    if (stringContrDate1.length() > 0) {
      stringCondition += ";簽約日期:" + stringContrDate1 + "∼" + stringContrDate2;
    }
    if (stringDateCheck1.length() > 0) {
      stringCondition += ";合約會審:" + stringDateCheck1 + "∼" + stringDateCheck2;
    }
    if (stringDateRange1.length() > 0) {
      stringCondition += ";簽約金到期:" + stringDateRange1 + "∼" + stringDateRange2;
    }
    stringCondition += ";" + stringKind + ";" + getDisplayValue("BalaPrint") + ";成交日期:" + stringBuyerDate1 + "∼" + stringBuyerDate2;
    if (stringEDate1.length() > 0) {
      stringCondition += ";訂單日期:" + stringEDate1 + "∼" + stringEDate2;
    }
    if (stringSellerCashDate1.length() > 0) {
      stringCondition += ";賣方兌現日:" + stringSellerCashDate1 + "∼" + stringSellerCashDate2;
    }
    if (stringBuyerCashDate1.length() > 0) {
      stringCondition += ";買方兌現日:" + stringBuyerCashDate1 + "∼" + stringBuyerCashDate2;
    }
    stringCondition += ";" + stringSaleKind;
    // 畫面條件
    exeExcel.putDataIntoExcel(0, 0, stringCondition, objectSheet1);
    // 匯出資料
    for (int intRow = 0; intRow < retData.length; intRow++) {
      for (int intCol = 0; intCol <= 33; intCol++) {
        if ((intCol == 31 || intCol == 32) && "N".equals(stringBalaPrint)) {
          continue;
        }
        exeExcel.putDataIntoExcel(intCol, intInsertDataRow, retData[intRow][intCol].trim(), objectSheet1);
      }
      intInsertDataRow++;
    }
    //
    exeExcel.doDeleteRows(intInsertDataRow + 1, 1002, objectSheet1);
    //
    exeExcel.setVisiblePropertyOnFlow(true, retVector); // 控制顯不顯示 Excel
    exeExcel.getReleaseExcelObject(retVector);
    //
    long longTime2 = exeUtil.getTimeInMillis();
    System.out.println("實際---" + ((longTime2 - longTime1) / 1000) + "秒---");
    return false;
  }

  public String getInformation() {
    return "---------------\u5217\u5370\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
