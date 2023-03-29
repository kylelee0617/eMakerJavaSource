package SaleSale05M25601;

import jcx.jform.bvalidate;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

public class PF1463729869815_bk230303 extends bvalidate{
  public boolean check(String value)throws Throwable{
    // �i�۩w����ˮֱ��� 
    // �ǤJ�� value ���J�� 
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
    // �ɼӧO�O�_���ۤv���I�ڱ���
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
                      "(case when T264.HouseStyle='2' then '�G��' "+
                           "when T264.HouseStyle='3' then '�T��' "+
                           "when T264.HouseStyle='4' then '�|��' "+ // �ק���:20100118 ���u�s��:B3774
                           "else '' end) HouseStyle, "+
                      "(rtrim(convert(char,T268.UsancePercent1))+'%') UsancePercent1, "+
                      "(case when T268.Usance1='prompt' then '�Y��' "+
                           "when T268.Usance1='1' then '�@�~��' "+
                           "when T268.Usance1='60' then '60��' "+
                           "else '' end) Usance1, "+
                      "(case when T268.PaymentType1='bySet' then '�̳]�w' "+
                           "when T268.PaymentType1='bill' then '����' "+
                           "else '' end) PaymentType1, "+
                      "T268.AcquireMethod1, "+
                      "(case when T268.BillCollect1='Merge' then '�X��' "+
                           "when T268.BillCollect1='Seperate' then '����' "+
                           "else '' end) BillCollect1, "+
                      "(rtrim(convert(char,T268.UsancePercent2))+'%') UsancePercent2, "+
                      "(case when T268.Usance2='prompt' then '�Y��' "+
                           "when T268.Usance2='1' then '�@�~��' "+
                           "when T268.Usance2='60' then '60��' "+
                           "else '' end) Usance2, "+
                      "(case when T268.PaymentType2='bySet' then '�̳]�w' "+
                           "when T268.PaymentType2='bill' then '����' "+
                           "else '' end) PaymentType2, "+
                      "T268.AcquireMethod2, "+
                      "(case when T268.BillCollect2='Merge' then '�X��' "+
                           "when T268.BillCollect2='Seperate' then '����' "+
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
                      "(case when T264.HouseStyle='2' then '�G��' "+
                           "when T264.HouseStyle='3' then '�T��' "+
                           "when T264.HouseStyle='4' then '�|��' "+ // �ק���:20100118 ���u�s��:B3774
                           "else '' end) HouseStyle, "+
                      "(rtrim(convert(char,T266.UsancePercent1))+'%') UsancePercent1, "+
                      "(case when T266.Usance1='prompt' then '�Y��' "+
                           "when T266.Usance1='1' then '�@�~��' "+
                           "when T266.Usance1='60' then '60��' "+
                           "else '' end) Usance1, "+
                      "(case when T266.PaymentType1='bySet' then '�̳]�w' "+
                           "when T266.PaymentType1='bill' then '����' "+
                           "else '' end) PaymentType1, "+
                      "T266.AcquireMethod1, "+
                      "(case when T266.BillCollect1='Merge' then '�X��' "+
                           "when T266.BillCollect1='Seperate' then '����' "+
                           "else '' end) BillCollect1, "+
                      "(rtrim(convert(char,T266.UsancePercent2))+'%') UsancePercent2, "+
                      "(case when T266.Usance2='prompt' then '�Y��' "+
                           "when T266.Usance2='1' then '�@�~��' "+
                           "when T266.Usance2='60' then '60��' "+
                           "else '' end) Usance2, "+
                      "(case when T266.PaymentType2='bySet' then '�̳]�w' "+
                           "when T266.PaymentType2='bill' then '����' "+
                           "else '' end) PaymentType2, "+
                      "T266.AcquireMethod2, "+
                      "(case when T266.BillCollect2='Merge' then '�X��' "+
                           "when T266.BillCollect2='Seperate' then '����' "+
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
      // �ϥνs��
      setValueAt("table3",  retData[0][0],  intRow,  "OrderNo");
      // �m�W
      Sale.Sale05M25301  exeFun  =  new  Sale.Sale05M25301();
      setValueAt("table3",  exeFun.getCustomName(stringProjectID1, stringPosition, retData[0][0]),  intRow,  "[������]:0");
      // �W��
      setValueAt("table3",  retData[0][1],  intRow,  "[������]:1");
      // �Ы�
      setValueAt("table3",  retData[0][2],  intRow,  "[������]:2");
      // �����@�G��ҡB�����B�I�ڤu��B����覡�B���ڷJ�`
      setValueAt("table3",  retData[0][3],  intRow,  "[������]:3");
      setValueAt("table3",  retData[0][4],  intRow,  "[������]:4");
      setValueAt("table3",  retData[0][5],  intRow,  "[������]:5");
      setValueAt("table3",  retData[0][6],  intRow,  "AcquireMethod1");
      setValueAt("table3",  retData[0][7],  intRow,  "[������]:6");
      // �����G�G��ҡB�����B�I�ڤu��B����覡�B���ڷJ�`
      setValueAt("table3",  retData[0][8],    intRow,  "[������]:7");
      setValueAt("table3",  retData[0][9],  intRow,  "[������]:8");
      setValueAt("table3",  retData[0][10],  intRow,  "[������]:9");
      setValueAt("table3",  retData[0][11],  intRow,  "AcquireMethod2");
      setValueAt("table3",  retData[0][12],  intRow,  "[������]:10");
      // �ذe���B
      setValueAt("table3",  retData[0][13],  intRow,  "GiftMoney");
      // ���дڪ��B
      setValueAt("table3",  retData[0][14],  intRow,  "NonRequestPaymentMoney");
      
      
      // �����i�дڪ��B
      setValueAt("table3",  retData[0][15],  intRow,  "ValidRequestPaymentMoney");
      
      
      // ��ڽдڪ��B
      setValueAt("table3",  retData[0][16],  intRow,  "RealRequestPaymentMoney");
    }
    return true;
  }
  public String getInformation(){
    return "---------------null(null).RequestPaymentTimes.field_check()----------------";
  }
}
