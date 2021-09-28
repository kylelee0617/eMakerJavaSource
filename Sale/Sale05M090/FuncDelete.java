package Sale.Sale05M090;

import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import Farglory.util.FargloryUtil ;

public class FuncDelete extends bTransaction{
  public boolean action(String value)throws Throwable{
    //201808check BEGIN
    System.out.println("chk==>"+getUser()+" , value==>刪除");
      if(getUser() != null && getUser().toUpperCase().equals("B9999")) {
        messagebox("刪除權限不允許!!!");
        return false;
    }
    //201808check FINISH  
    // 回傳值為 true 表示執行接下來的資料庫異動或查詢
    // 回傳值為 false 表示接下來不執行任何指令
    // 傳入值 value 為 "新增","查詢","修改","刪除","列印","PRINT" (列印預覽的列印按鈕),"PRINTALL" (列印預覽的全部列印按鈕) 其中之一
    setValue("OrderNo",getValue("field3").trim());
    /*
      String OrderNo=getValue("field3").trim();
      
      showForm("購屋證明單-同步-行銷(Sale02M030)");
      setValue("OrderNo",OrderNo);
      getButton("button刪除").doClick();
      getInternalFrame("購屋證明單-同步-行銷(Sale02M030)").setVisible(false);  
    */  
    //------------------------------------------
    talk  dbSale  =  getTalk((String)get("put_dbSale")) ;//SQL2000
    String  stringSQL = "";
    //
    stringSQL = "SELECT ProjectID1 " +
                " FROM Sale05M090 " +
              " WHERE OrderNo = '" + getValue("OrderNo").trim() + "'" ;
    String[][]  retSale05M090 = dbSale.queryFromPool(stringSQL);
    if(retSale05M090.length == 0){
        message("購屋証明單:" + getValue("OrderNo").trim() + " 不存在!"); 
      return false;   
    }
    String stringProjectID1 = retSale05M090[0][0].trim();
    //
    stringSQL = "SELECT HouseCar, " +
                     " Position " +
                " FROM Sale05M092 " +
              " WHERE OrderNo = '" + getValue("OrderNo").trim() + "'" ;
    String[][]  retSale05M092  =  dbSale.queryFromPool(stringSQL);
    for(int  intSale05M092=0  ;  intSale05M092<retSale05M092.length  ;  intSale05M092++){
      String stringHouseCar = retSale05M092[intSale05M092][0].trim();
      String stringPosition = retSale05M092[intSale05M092][1].trim();
      //
      String stringA_Sale = " A_Sale";
      stringSQL = "SELECT ID1 " +
                  " FROM " + stringA_Sale +
                " WHERE ProjectID1 = '" + stringProjectID1 + "'";
      if(stringHouseCar.equals("House"))
        stringSQL = stringSQL + " AND HouseCar = 'Position'" +  
                              " AND Position = '" + stringPosition + "'";
      if(stringHouseCar.equals("Car"))
        stringSQL = stringSQL + " AND HouseCar = 'Car'" +   
                              " AND Car = '" + stringPosition + "'";
      String[][]  retA_Sale  =  dbSale.queryFromPool(stringSQL);
      if(retA_Sale.length == 0){
          message("行銷 A_Sale 棟樓別不存在!"); 
        return false;   
      }
      String stringID1 = retA_Sale[0][0]; 
      //UPDATE A_Sale
       stringSQL = "UPDATE " + stringA_Sale +
                   " SET YearMM = ''," +
                        " Depart = 0," +                                
                      " Custom =''," +               
                      " OrderDate =''," +              
                      " OrderMon = 0," +
                      " H_OrderMon = 0," +
                      " L_OrderMon = 0," +
                      " EnougDate =''," +              
                      " EnougMon = 0," +
                      " H_EnougMon = 0," +
                      " L_EnougMon = 0," +
                      " ContrDate =''," +              
                      " ContrMon = 0," +
                      " H_ContrMon = 0," +
                      " L_ContrMon = 0," +
                      " Deldate =''," +              
                      " PingSu = 0," +                                
                      " DealDiscount = 0," +                                
                      " BonusDiscount = 0," +                               
                      " PreMoney = 0," +                                
                      " H_PreMoney = 0," +                                
                      " L_PreMoney = 0," +                                
                      " DealMoney = 0," +                               
                      " H_DealMoney = 0," +                               
                      " L_DealMoney = 0," +                               
                      " GiftMoney = 0," +                               
                      " H_GiftMoney = 0," +                               
                      " L_GiftMoney = 0," +                               
                      " CommMoney = 0," + 
                      " H_CommMoney = 0," +                               
                      " L_CommMoney = 0," +                               
                      " CommMoney1 = 0," +    // 2015-10-13 B3018 增加中原佣金
                      " H_CommMoney1 = 0," +  // 2015-10-13 B3018 增加中原佣金
                      " L_CommMoney1 = 0," +  // 2015-10-13 B3018 增加中原佣金
                      " PureMoney = 0," +                               
                      " H_PureMoney = 0," +                               
                      " L_PureMoney = 0," +                               
                      " LastMoney = 0," +                               
                      " H_LastMoney = 0," +                               
                      " L_LastMoney = 0," +                               
                      " BalaMoney = 0," +                               
                      " H_BalaMoney = 0," +                               
                      " L_BalaMoney = 0," +                               
                      " SaleID1 =''," +              
                      " SaleName1 =''," +              
                      " SaleID2 =''," +              
                      " SaleName2 =''," +              
                      " SaleID3 =''," +              
                      " SaleName3 =''," +              
                      " SaleID4 =''," +              
                      " SaleName4 =''," +              
                      " SaleID5 =''," +              
                      " SaleName5 =''," +              
                      " SaleID6 =''," +           // 增加售出人 20090414    
                      " SaleName6 =''," +           // 增加售出人 20090414 
                      " SSMediaID =''," +           // 增加 媒體          2015-06-01
                      " SSMediaID1 =''," +          // 增加 媒體細項      2015-06-01
                      " SaleGroup =''," +              
                      " MediaID =''," +              
                      " MediaName =''," +              
                      " ZoneID =''," +               
                      " ZoneName =''," +               
                      " MajorID =''," +              
                      " MajorName =''," +              
                      " UseType =''," +              
                      " Remark =''," +               
                      " DateRange =''," +              
                      " DateCheck =''," +              
                      " DateFile =''," +               
                      " DateBonus =''," +              
                      " RentRange = 0," +                               
                      " PingRentPrice = 0," +                               
                      " PingRent = 0," +                                
                      " PingRentLast = 0," +                                
                      " RentPrice = 0," +                               
                      " Rent = 0," +                                
                      " RentLast = 0," +                                
                      " Guranteer = 0," +                               
                      " RentFree = 0," +                                
                      " Position1 =''," +              
                      " PositionRent1 =''," +              
                      " Custom1 =''," +              
                      " ViMoney = 0," +                               
                      " H_ViMoney = 0," +                               
                      " L_ViMoney = 0," +                               
                      " AO_sn = 0," +                               
                      " Plan1 =''," +              
                      " ComNo =''," +              
                      " OrderNo ='', " +               
                      " SaleID7 =''," +           // 增加售出人 20090525  
                      " SaleName7 =''," +       // 增加售出人 20090525
                      " SaleID8 =''," +           // 增加售出人 20090525    
                      " SaleName8 =''," +       // 增加售出人 20090525
                      " SaleID9 =''," +           // 增加售出人 20090525    
                      " SaleName9 =''," +       // 增加售出人 20090525
                      " SaleID10 =''," +         // 增加售出人 20090525     
                      " SaleName10 ='' " +       // 增加售出人 20090525
                      " WHERE ID1 = " +  stringID1;
       dbSale.execFromPool(stringSQL);
       //
       (new  FargloryUtil()).doDeleteDB("A_Sale_SaleID",  new  Hashtable(),  " AND  ID1  =  "+stringID1+" ",  true,  dbSale) ;
    }
    message("OK!");
    //------------------------------------------  
    /*
      talk  dbSale    =  getTalk(""+get("put_dbSale"));
      String stringSQL = " INSERT  " + 
                    " INTO Sale02M030 "  +
                       " ( " +
                         " OP," +
                         " Type," +
                         " TypeNo," +
                         " DateTime " +
                       " ) " +
                    " VALUES " +
                       " ( " +
                         "N'DELETE',"+
                         "N'購屋',"+
                         "'" + OrderNo + "',"+
                         "N'" + datetime.getTime("YYYY/mm/dd h:m:s") + "'" +
                       " ) ";
      dbSale.execFromPool(stringSQL);   
    */          
    put("TrustAccountNo",  value) ;
    getButton("ButtonTrustAccountNo").doClick() ;     
    return true;
  }
  public String getInformation(){
    return "---------------\u522a\u9664\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
