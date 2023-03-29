package Invoice.RD;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import Farglory.util.*;

public class BuDiscount extends bproc {

  KUtils util = new KUtils();

  public String getDefaultValue(String value) throws Throwable {
    System.out.println("�ɧ�������>>>>>>>>>> Start");
    
    talk dbInvoice = getTalk("" + get("put_dbInvoice"));
    talk as400 = getTalk("AS400");
    String GENLIB = ((Map) get("config")).get("GENLIB").toString().trim();
    
    //check
    String disCountNo = getValue("DiscountNo").trim();
    if (StringUtils.isBlank(disCountNo)) {
      messagebox("���d�ߨ��o�����渹!!!!");
      return value;
    }
   
    //�g�JAS400�����D��
    String sql = "delete from GLEBPFUF where EB01U = '"+disCountNo+"'";
    as400.execFromPool(sql);
    
    StringBuilder sbSQL = new StringBuilder();
    sbSQL.append("INSERT INTO GLEBPFUF ");
    sbSQL.append("(EB01U, EB02U, EB03U, EB04U, EB05U, EB06U, EB07U, EB08U, EB09U, EB10U, EB11U, EB12U, EB13U, EB14U, EB15U, EB16U, EB17U, EB18U, EB19U) ");
    sbSQL.append("values ");
    sbSQL.append("(");
    sbSQL.append("'").append(disCountNo).append("', ");                         //�������X
    sbSQL.append("'").append(getValue("DiscountDate").trim()).append("', ");    //�������
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
    sbSQL.append("'").append("BUSYS").append("', ");                          //�ק�H
    sbSQL.append("'").append(getValue("ModifyDateTime").trim()).append("', ");   //���ڮɶ�
    sbSQL.append("'").append("1").append("' ");                                 //PROCESS DISCOUNT
    sbSQL.append(") ");
    as400.execFromPool(sbSQL.toString());
    
    sql = "select InvoiceNo ,PointNo ,InvoiceMoney ,InvoiceTax ,InvoiceTotalMoney ,YiDiscountMoney ,DiscountItemMoney "
        + "from InvoM041 "
        + "where DiscountNo = '"+disCountNo+"'";
    String[][] detail = dbInvoice.queryFromPool(sql);
    
    sql = "DELETE FROM GLECPFUF WHERE EC01U = '"+disCountNo+"'";
    as400.execFromPool(sql);
    
    //�g�JAS400��������
    int GLECPFUFCount = 1;
    for(int i=0;i<detail.length;i++){
      sbSQL = new StringBuilder();
      sbSQL.append("INSERT INTO GLECPFUF ");
      sbSQL.append("(EC01U, EC02U, EC03U, EC04U, EC05U, EC06U, EC07U, EC08U, EC09U, EC10U) ");
      sbSQL.append("values ");
      sbSQL.append("(");
      sbSQL.append("'").append(disCountNo).append("', ");                         //�������X
      sbSQL.append("").append(GLECPFUFCount).append(", ");                        //����
      sbSQL.append("'").append("Y").append("', ");                                //�Ŀ�
      sbSQL.append("'").append( detail[i][0].trim() ).append("', ");             //�o�����X
      sbSQL.append("'").append( detail[i][1].trim() ).append("', ");            //�K�n�N�X
      sbSQL.append("").append( detail[i][2].trim() ).append(", ");               //���|
      sbSQL.append("").append( detail[i][3].trim() ).append(", ");               //�|�B
      sbSQL.append("").append( detail[i][4].trim() ).append(", ");               //�`���B
      sbSQL.append("").append( detail[i][5].trim() ).append(", ");              //�w�������B
      sbSQL.append("").append( detail[i][6].trim() ).append(" ");                //���������B
      sbSQL.append(") ");
      as400.execFromPool(sbSQL.toString());
      GLECPFUFCount++;
    }

    System.out.println("�ɧ������>>>>>>>>>> End");
    return value;
  }

  public String getInformation() {
    return "---------------BUFAPIO(GO!!!).defaultValue()----------------";
  }
}
