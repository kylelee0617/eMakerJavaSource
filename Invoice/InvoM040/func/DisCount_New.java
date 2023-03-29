package Invoice.InvoM040.func;
import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;

import Farglory.util.KUtils;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

/**
 * ���� : PT1387941781533
 * 2020/12/07 Kyle : add�}�ߧ����g�JAS400�\��
 * 
 * @author B04391
 *
 */

public class DisCount_New extends bTransaction{
  
  KUtils kUtil = new KUtils();
  
  public boolean action(String value)throws Throwable{
    // �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
    // �^�ǭȬ� false ��ܱ��U�Ӥ����������O
    // �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
    message("");
    String stringDiscountDate = getValue("DiscountDate");
    if(!check.isACDay(stringDiscountDate.replace("/",""))) {
        message("�����������~(YYYY/MM/DD)");
        return false;
    }
    talk dbInvoice = getTalk("Invoice");
    String stringSQL = "";
    String stringUserkey = "";
    
    //����
    String [][] A_table = getTableData("table1");
    if (A_table.length ==0){
       message("���ӥ����ܤ֦��@��");
       return false;
    }
    //����������i�p��o�����
    for(int ii=0 ; ii<A_table.length ; ii++) {
      String[] aTable = A_table[ii];
      String thisInvoDate = aTable[3].trim();
      long subDays = kUtil.subACDaysRDay(stringDiscountDate, thisInvoDate);
      if(subDays < 0) {
        message("����������i�p���" + aTable[1].trim() + "���o�����");
        return false;
      }
    }
    
    Calendar cal= Calendar.getInstance();//Current time
    stringUserkey = getUser() + "_T" + ""+( (cal.get(Calendar.HOUR_OF_DAY)*10000) + (cal.get(Calendar.MINUTE)*100) + cal.get(Calendar.SECOND) );
    stringSQL = "DELETE FROM InvoM041TempBody WHERE UseKey = '" + stringUserkey + "'";
    dbInvoice.execFromPool(stringSQL);
    int intBodyCount = 0; 
    String stringCustomNo = "";
    String stringMessage = "";
    int intCustomNo = 0;
    for(int i=0;i<A_table.length;i++){  //�g����
       if(!A_table[i][8].equals("0")){
           if (!stringCustomNo.equals(A_table[i][18])){
             stringCustomNo = A_table[i][18];
           stringMessage += stringCustomNo+ "�B";
             intCustomNo ++;     
         }
        stringSQL =  "INSERT INTO InvoM041TempBody " +
                          "(" +
                            " UseKey," +
                            " RecordNo," +
                            " ChoiceYES," +
                            " InvoiceNo," +
                            " InvoiceKind," +
                            " InvoiceWay," +
                            " PointNo," +
                            " InvoiceMoney," +
                            " InvoiceTax," +
                            " InvoiceTotalMoney," +
                            " YiDiscountMoney," +
                            " DiscountItemMoney," +
                            " TaxRate," +
                            " TaxKind," +
                            " ProcessInvoiceNo," +
                            " DisCountTimes" +
                          " ) " +
                   " VALUES (" +
                      "'" + stringUserkey +  "'," +
                      (intBodyCount + 1) + "," +
                      "'" +  A_table[i][0] +  "'," +                
                      "'" +  A_table[i][2] +  "'," +
                      "'" +  A_table[i][12]+  "'," +                
                      "'" +  A_table[i][13]+  "'," +                
                      "'" +  A_table[i][4]+  "'," +               
                           A_table[i][10]+  "," +          //8     
                           A_table[i][11]+  "," +               
                           A_table[i][6]+  "," +                
                           A_table[i][7]+  "," +                                     
                           A_table[i][8]+  "," +                                                         
                      "'" +  A_table[i][17]+  "'," +                
                      "'" +  A_table[i][14]+  "'," +                                
                      "'" +  A_table[i][15]+  "'," +                                
                      "'" +  A_table[i][16]+  "'" +                                               
                      ")";
        dbInvoice.execFromPool(stringSQL);
        intBodyCount++;
      }
    }
    if(intCustomNo >1){ 
      stringMessage = stringMessage.substring(0,stringMessage.length()-1);
      message("�P�@�i�������i���h���Ȥ᪺�o��!�A�ж}���P��������F" + stringMessage);     
        return false; 
    } 
    /*
    if (stringDiscountDate.equals("9999/01/01")){
    } 
    */
    // �p�ⲣ�ʹX��������(�J��C{n=N_RecordNo%}���@�`���ɨϥ�)
    int intDiscountNoCount = intBodyCount  / 8;
    intDiscountNoCount ++;
    if (intBodyCount % 8 == 0) intDiscountNoCount = intDiscountNoCount -1;
    if(intDiscountNoCount == 0){
      message("�o������ ���i�ť�!");
      return false;
    }
    getButton("button3").doClick();
    //
    String retSystemDateTime[][] = dbInvoice.queryFromPool("spInvoSystemDateTime  'Admin'");
    String stringSystemDateTime ="";
    stringSystemDateTime = retSystemDateTime[0][0].replace("-","/");
    stringSystemDateTime = stringSystemDateTime.substring(0,19);
    //
    stringSQL = "spInvoM040Insert " +
                   intDiscountNoCount + "," +
               "'" + getValue("DiscountDate").trim() + "'," +
               "'" + getValue("CompanyNo").trim() + "'," +
               "'" + getValue("DepartNo").trim() + "'," +
               "'" + getValue("ProjectNo").trim() + "'," +           
               "'" + getValue("HuBei").trim() + "'," +                               
               "'" + getValue("CustomNo").trim() + "'," +                              
               "'" + getValue("DiscountWay").trim() + "'," +                     
               "'',"
               + getValue("DiscountMoney").trim() + ","                                         
               + getValue("DiscountTax").trim() + ","                                         
               + getValue("DiscountTotalMoney").trim() + "," +
               "'1'," +                              
               "'" + getUser() + "'," +
               "'" + stringSystemDateTime  + "'," +
               "'" + stringSystemDateTime  + "'," +          
                   "'A'," +
               "'" + stringUserkey + "'," +
               "'" + getValue("Reason").trim() + "'" ;
    dbInvoice.execFromPool(stringSQL);
    stringSQL = "SELECT DiscountNo FROM InvoM040 WHERE EmployeeNo = '" + getUser() + "' AND ModifyDateTime = '" + stringSystemDateTime + "'";
    String retInvoM040[][] = dbInvoice.queryFromPool(stringSQL);
    String stringDiscountNo = "";
    for(int n=0;n<retInvoM040.length;n++){
      stringDiscountNo += retInvoM040[n][0] + ";";
      
      //�g�JAS400�����D��
      talk as400 = getTalk("AS400");
      String disCountNo = retInvoM040[n][0].trim();
      StringBuilder sbSQL = new StringBuilder();
      sbSQL.append("INSERT INTO GLEBPFUF ");
      sbSQL.append("(EB01U, EB02U, EB03U, EB04U, EB05U, EB06U, EB07U, EB08U, EB09U, EB10U, EB11U, EB12U, EB13U, EB14U, EB15U, EB16U, EB17U, EB18U, EB19U) ");
      sbSQL.append("values ");
      sbSQL.append("(");
      sbSQL.append("'").append(disCountNo).append("', ");                 //�������X
      sbSQL.append("'").append(getValue("DiscountDate").trim()).append("', ");     //�������
      sbSQL.append("'").append(getValue("CompanyNo").trim()).append("', ");       //���q�N�X
      sbSQL.append("'").append(getValue("DepartNo").trim()).append("', ");        //�����N�X
      sbSQL.append("'").append(getValue("ProjectNo").trim()).append("', ");       //�קO�N�X
      sbSQL.append("'").append(getValue("HuBei").trim()).append("', ");           //��O�N��
      sbSQL.append("'").append(getValue("CustomNo").trim()).append("', ");        //�Ȥ�N��
      sbSQL.append("'").append(getValue("DiscountWay").trim()).append("', ");      //Invoice Way
      sbSQL.append("'").append("").append("', ");                                  //�s��O
      sbSQL.append("").append(getValue("DiscountMoney").trim()).append(", ");      //���|
      sbSQL.append("").append(getValue("DiscountTax").trim()).append(", ");        //�|�B
      sbSQL.append("").append(getValue("DiscountTotalMoney").trim()).append(", "); //�t�|
      sbSQL.append("'").append("N").append("', ");                                //�w�C�LYN
      sbSQL.append("").append(0).append(", ");                                    //�ɦL����
      sbSQL.append("'").append("N").append("', ");                                //�@�oYN
      sbSQL.append("'").append("N").append("', ");                                //�J�bYN
      sbSQL.append("'").append(getUser()).append("', ");                          //�ק�H
      sbSQL.append("'").append(stringSystemDateTime).append("', ");                //���ڮɶ�
      sbSQL.append("'").append("1").append("' ");                                 //PROCESS DISCOUNT
      sbSQL.append(") ");
      as400.execFromPool(sbSQL.toString());
      
      //�g�JAS400��������
      int GLECPFUFCount = 1;
      for(int i=0;i<A_table.length;i++){
        sbSQL = new StringBuilder();
        sbSQL.append("INSERT INTO GLECPFUF ");
        sbSQL.append("(EC01U, EC02U, EC03U, EC04U, EC05U, EC06U, EC07U, EC08U, EC09U, EC10U) ");
        sbSQL.append("values ");
        sbSQL.append("(");
        sbSQL.append("'").append(disCountNo).append("', ");                         //�������X
        sbSQL.append("").append(GLECPFUFCount).append(", ");                        //����
        sbSQL.append("'").append("Y").append("', ");                                //�Ŀ�
        sbSQL.append("'").append( A_table[i][2].trim() ).append("', ");             //�o�����X
        sbSQL.append("'").append( A_table[i][4].trim() ).append("', ");            //�K�n�N�X
        sbSQL.append("").append( A_table[i][10].trim() ).append(", ");               //���|
        sbSQL.append("").append( A_table[i][11].trim() ).append(", ");               //�|�B
        sbSQL.append("").append( A_table[i][6].trim() ).append(", ");               //�`���B
        sbSQL.append("").append( A_table[i][7].trim() ).append(", ");              //�w�������B
        sbSQL.append("").append( A_table[i][8].trim() ).append(" ");                //���������B
        sbSQL.append(") ");
        as400.execFromPool(sbSQL.toString());
        GLECPFUFCount++;
      }
      
    }
    action(9);
    message("�w���ͧ����� = " + stringDiscountNo);    
    // 2013-12-25 �ƻs��ŶKï      
    Farglory.util.FargloryUtil  exeUtil  =  new  Farglory.util.FargloryUtil() ;
    exeUtil.ClipCopy (exeUtil.doSubstring(stringDiscountNo,  0,  stringDiscountNo.length()-1)) ;
    return false;
  }
}
