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
    System.out.println("chk==>"+getUser()+" , value==>�R��");
      if(getUser() != null && getUser().toUpperCase().equals("B9999")) {
        messagebox("�R���v�������\!!!");
        return false;
    }
    //201808check FINISH  
    // �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
    // �^�ǭȬ� false ��ܱ��U�Ӥ����������O
    // �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
    setValue("OrderNo",getValue("field3").trim());
    /*
      String OrderNo=getValue("field3").trim();
      
      showForm("�ʫ��ҩ���-�P�B-��P(Sale02M030)");
      setValue("OrderNo",OrderNo);
      getButton("button�R��").doClick();
      getInternalFrame("�ʫ��ҩ���-�P�B-��P(Sale02M030)").setVisible(false);  
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
        message("�ʫε�����:" + getValue("OrderNo").trim() + " ���s�b!"); 
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
          message("��P A_Sale �ɼӧO���s�b!"); 
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
                      " CommMoney1 = 0," +    // 2015-10-13 B3018 �W�[�������
                      " H_CommMoney1 = 0," +  // 2015-10-13 B3018 �W�[�������
                      " L_CommMoney1 = 0," +  // 2015-10-13 B3018 �W�[�������
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
                      " SaleID6 =''," +           // �W�[��X�H 20090414    
                      " SaleName6 =''," +           // �W�[��X�H 20090414 
                      " SSMediaID =''," +           // �W�[ �C��          2015-06-01
                      " SSMediaID1 =''," +          // �W�[ �C��Ӷ�      2015-06-01
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
                      " SaleID7 =''," +           // �W�[��X�H 20090525  
                      " SaleName7 =''," +       // �W�[��X�H 20090525
                      " SaleID8 =''," +           // �W�[��X�H 20090525    
                      " SaleName8 =''," +       // �W�[��X�H 20090525
                      " SaleID9 =''," +           // �W�[��X�H 20090525    
                      " SaleName9 =''," +       // �W�[��X�H 20090525
                      " SaleID10 =''," +         // �W�[��X�H 20090525     
                      " SaleName10 ='' " +       // �W�[��X�H 20090525
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
                         "N'�ʫ�',"+
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
