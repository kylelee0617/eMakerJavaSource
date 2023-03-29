package SaleSale05M25601;

import jcx.jform.bvalidate;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

public class PF1463729869815_bk230303 extends bvalidate{
  public boolean check(String value)throws Throwable{
    // 可自定欄位檢核條件 
    // 傳入值 value 原輸入值 
    value = value.trim(); if(value.length() == 0)  return true ;
    //
    talk  dbSale  =  getTalk(""+get("put_dbSale"));
    int           intRow                                    = getRow();
    String     stringProjectID1                      = getValue("ProjectID1");
    String     stringObjectCd                        = getValue("ObjectCd");
    String     stringPosition                          = (""+getValueAt("table3", intRow, "Position")).trim();
    String     stringVersion                          = (""+getValueAt("table3", intRow, "Version")).trim();
    String     stringSql                                  = "";
    String     stringNonRequestPaymentSql = "0";
    String     retData[][]                                = null;
    boolean blnHasPositionSelfPayment     = false;
    // 棟樓別是否有自己的付款條件
    stringSql = "select distinct T268.Position "+
              "from Sale05M268 T268, Sale05M264 T264 "+
              "where T268.ProjectID1=T264.ProjectID1 "+
              "and T268.Position=T264.Position "+
              "and T268.OrderNo=T264.OrderNo "+
              "and T264.ProjectID1='"+stringProjectID1+"' "+
              "and T264.ObjectCd='"+stringObjectCd+"' "+
              "and T268.Position='"+stringPosition+"'";
    retData = dbSale.queryFromPool(stringSql);
    if(retData.length > 0){
        blnHasPositionSelfPayment = true;
    }
    if(!"All".equals(value)){
        stringNonRequestPaymentSql = "(select isnull(sum(RealRequestPaymentMoney),0) "+
                                   "from Sale05M267 "+
                                   "where ProjectID1='"+stringProjectID1+"' "+
                                   "and Position='"+stringPosition+"' "+
                                   "and ObjectCd='"+stringObjectCd+"' "+
                                   "and Version='"+stringVersion+"' "+      // 2015-11-05 B3018
                                   "and convert(int,RequestPaymentTimes) < "+value+")";
    }
    if(blnHasPositionSelfPayment){
        stringSql = "select T264.OrderNo, T264.PingSu, "+
                      "(case when T264.HouseStyle='2' then '二房' "+
                           "when T264.HouseStyle='3' then '三房' "+
                           "when T264.HouseStyle='4' then '四房' "+ // 修改日期:20100118 員工編號:B3774
                           "else '' end) HouseStyle, "+
                      "(rtrim(convert(char,T268.UsancePercent1))+'%') UsancePercent1, "+
                      "(case when T268.Usance1='prompt' then '即期' "+
                           "when T268.Usance1='1' then '一年期' "+
                           "when T268.Usance1='60' then '60天' "+
                           "else '' end) Usance1, "+
                      "(case when T268.PaymentType1='bySet' then '依設定' "+
                           "when T268.PaymentType1='bill' then '票據' "+
                           "else '' end) PaymentType1, "+
                      "T268.AcquireMethod1, "+
                      "(case when T268.BillCollect1='Merge' then '合併' "+
                           "when T268.BillCollect1='Seperate' then '分戶' "+
                           "else '' end) BillCollect1, "+
                      "(rtrim(convert(char,T268.UsancePercent2))+'%') UsancePercent2, "+
                      "(case when T268.Usance2='prompt' then '即期' "+
                           "when T268.Usance2='1' then '一年期' "+
                           "when T268.Usance2='60' then '60天' "+
                           "else '' end) Usance2, "+
                      "(case when T268.PaymentType2='bySet' then '依設定' "+
                           "when T268.PaymentType2='bill' then '票據' "+
                           "else '' end) PaymentType2, "+
                      "T268.AcquireMethod2, "+
                      "(case when T268.BillCollect2='Merge' then '合併' "+
                           "when T268.BillCollect2='Seperate' then '分戶' "+
                           "else '' end) BillCollect2, "+
                      "T264.GiftMoney, "+
                      "(T264.GiftMoney - "+stringNonRequestPaymentSql+") NonRequestPaymentMoney, "+
                      "convert(int,(T264.GiftMoney*(T268.UsancePercent1+T268.UsancePercent2)/100),0) ValidRequestPaymentMoney, "+ //MEI 1050520
                      "convert(int,(T264.GiftMoney*(T268.UsancePercent1+T268.UsancePercent2)/100),0) RealRequestPaymentMoney "+ //MEI 1050520
                  "from Sale05M264 T264, Sale05M268 T268 "+
                  "where T264.ProjectID1=T268.ProjectID1 "+
                  "and T264.Position=T268.Position "+
                  "and T264.OrderNo=T268.OrderNo "+
                  "and T264.ProjectID1='"+stringProjectID1+"' "+
                  "and T264.Position='"+stringPosition+"' "+
                  "and T264.ObjectCd='"+stringObjectCd+"' "+
                  "and T264.Version='"+stringVersion+"' "+      // 2015-11-05 B3018
                  "and T268.RequestPaymentTimes='"+value+"' "+
                  "order by len(T268.RequestPaymentTimes), T268.RequestPaymentTimes";
    }else{
        stringSql = "select T264.OrderNo, T264.PingSu, "+
                      "(case when T264.HouseStyle='2' then '二房' "+
                           "when T264.HouseStyle='3' then '三房' "+
                           "when T264.HouseStyle='4' then '四房' "+ // 修改日期:20100118 員工編號:B3774
                           "else '' end) HouseStyle, "+
                      "(rtrim(convert(char,T266.UsancePercent1))+'%') UsancePercent1, "+
                      "(case when T266.Usance1='prompt' then '即期' "+
                           "when T266.Usance1='1' then '一年期' "+
                           "when T266.Usance1='60' then '60天' "+
                           "else '' end) Usance1, "+
                      "(case when T266.PaymentType1='bySet' then '依設定' "+
                           "when T266.PaymentType1='bill' then '票據' "+
                           "else '' end) PaymentType1, "+
                      "T266.AcquireMethod1, "+
                      "(case when T266.BillCollect1='Merge' then '合併' "+
                           "when T266.BillCollect1='Seperate' then '分戶' "+
                           "else '' end) BillCollect1, "+
                      "(rtrim(convert(char,T266.UsancePercent2))+'%') UsancePercent2, "+
                      "(case when T266.Usance2='prompt' then '即期' "+
                           "when T266.Usance2='1' then '一年期' "+
                           "when T266.Usance2='60' then '60天' "+
                           "else '' end) Usance2, "+
                      "(case when T266.PaymentType2='bySet' then '依設定' "+
                           "when T266.PaymentType2='bill' then '票據' "+
                           "else '' end) PaymentType2, "+
                      "T266.AcquireMethod2, "+
                      "(case when T266.BillCollect2='Merge' then '合併' "+
                           "when T266.BillCollect2='Seperate' then '分戶' "+
                           "else '' end) BillCollect2, "+
                      "T264.GiftMoney, "+
                      "(T264.GiftMoney - "+stringNonRequestPaymentSql+") NonRequestPaymentMoney, "+
                      "convert(int,(T264.GiftMoney*(T266.UsancePercent1+T266.UsancePercent2)/100)) ValidRequestPaymentMoney, "+ //mei 1050318
                      "convert(int,(T264.GiftMoney*(T266.UsancePercent1+T266.UsancePercent2)/100)) RealRequestPaymentMoney "+//mei 1050318
                  "from Sale05M264 T264, Sale05M266 T266 "+
                  "where T264.ProjectID1=T266.ProjectID1 "+
                  "and T264.ObjectCd=T266.ObjectCd "+
                  "and T264.ProjectID1='"+stringProjectID1+"' "+
                  "and T264.Position='"+stringPosition+"' "+  
                  "and T266.ObjectCd='"+stringObjectCd+"' "+
                  "and T266.Version='"+stringVersion+"' "+      // 2015-11-05 B3018
                  "and T266.RequestPaymentTimes='"+value+"' "+
                  "order by len(T266.RequestPaymentTimes), T266.RequestPaymentTimes";
    }
    retData = dbSale.queryFromPool(stringSql);
    if(retData.length > 0){
      // 使用編號
      setValueAt("table3",  retData[0][0],  intRow,  "OrderNo");
      // 姓名
      Sale.Sale05M25301  exeFun  =  new  Sale.Sale05M25301();
      setValueAt("table3",  exeFun.getCustomName(stringProjectID1, stringPosition, retData[0][0]),  intRow,  "[顯示欄位]:0");
      // 坪數
      setValueAt("table3",  retData[0][1],  intRow,  "[顯示欄位]:1");
      // 房型
      setValueAt("table3",  retData[0][2],  intRow,  "[顯示欄位]:2");
      // 票期一：比例、票期、付款工具、領取方式、票據彙總
      setValueAt("table3",  retData[0][3],  intRow,  "[顯示欄位]:3");
      setValueAt("table3",  retData[0][4],  intRow,  "[顯示欄位]:4");
      setValueAt("table3",  retData[0][5],  intRow,  "[顯示欄位]:5");
      setValueAt("table3",  retData[0][6],  intRow,  "AcquireMethod1");
      setValueAt("table3",  retData[0][7],  intRow,  "[顯示欄位]:6");
      // 票期二：比例、票期、付款工具、領取方式、票據彙總
      setValueAt("table3",  retData[0][8],    intRow,  "[顯示欄位]:7");
      setValueAt("table3",  retData[0][9],  intRow,  "[顯示欄位]:8");
      setValueAt("table3",  retData[0][10],  intRow,  "[顯示欄位]:9");
      setValueAt("table3",  retData[0][11],  intRow,  "AcquireMethod2");
      setValueAt("table3",  retData[0][12],  intRow,  "[顯示欄位]:10");
      // 贈送金額
      setValueAt("table3",  retData[0][13],  intRow,  "GiftMoney");
      // 未請款金額
      setValueAt("table3",  retData[0][14],  intRow,  "NonRequestPaymentMoney");
      
      
      // 本次可請款金額
      setValueAt("table3",  retData[0][15],  intRow,  "ValidRequestPaymentMoney");
      
      
      // 實際請款金額
      setValueAt("table3",  retData[0][16],  intRow,  "RealRequestPaymentMoney");
    }
    return true;
  }
  public String getInformation(){
    return "---------------null(null).RequestPaymentTimes.field_check()----------------";
  }
}
