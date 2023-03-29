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
    System.out.println("補折讓單行動>>>>>>>>>> Start");
    
    talk dbInvoice = getTalk("" + get("put_dbInvoice"));
    talk as400 = getTalk("AS400");
    String GENLIB = ((Map) get("config")).get("GENLIB").toString().trim();
    
    //check
    String disCountNo = getValue("DiscountNo").trim();
    if (StringUtils.isBlank(disCountNo)) {
      messagebox("先查詢取得折讓單號!!!!");
      return value;
    }
   
    //寫入AS400折讓主檔
    String sql = "delete from GLEBPFUF where EB01U = '"+disCountNo+"'";
    as400.execFromPool(sql);
    
    StringBuilder sbSQL = new StringBuilder();
    sbSQL.append("INSERT INTO GLEBPFUF ");
    sbSQL.append("(EB01U, EB02U, EB03U, EB04U, EB05U, EB06U, EB07U, EB08U, EB09U, EB10U, EB11U, EB12U, EB13U, EB14U, EB15U, EB16U, EB17U, EB18U, EB19U) ");
    sbSQL.append("values ");
    sbSQL.append("(");
    sbSQL.append("'").append(disCountNo).append("', ");                         //折讓號碼
    sbSQL.append("'").append(getValue("DiscountDate").trim()).append("', ");    //折讓日期
    sbSQL.append("'").append(getValue("CompanyNo").trim()).append("', ");       //公司代碼
    sbSQL.append("'").append(getValue("DepartNo").trim()).append("', ");        //部門代碼
    sbSQL.append("'").append(getValue("ProjectNo").trim()).append("', ");       //案別代碼
    sbSQL.append("'").append(getValue("HuBei").trim()).append("', ");           //戶別代號
    sbSQL.append("'").append(getValue("CustomNo").trim()).append("', ");        //客戶代號
    sbSQL.append("'").append(getValue("DiscountWay").trim()).append("', ");      //Invoice Way
    sbSQL.append("'").append("").append("', ");                                  //新戶別
    sbSQL.append("").append(getValue("DiscountMoney").trim()).append(", ");      //未稅
    sbSQL.append("").append(getValue("DiscountTax").trim()).append(", ");        //稅額
    sbSQL.append("").append(getValue("DiscountTotalMoney").trim()).append(", "); //含稅
    sbSQL.append("'").append("N").append("', ");                                //已列印YN
    sbSQL.append("").append(0).append(", ");                                    //補印次數
    sbSQL.append("'").append("N").append("', ");                                //作廢YN
    sbSQL.append("'").append("N").append("', ");                                //入帳YN
    sbSQL.append("'").append("BUSYS").append("', ");                          //修改人
    sbSQL.append("'").append(getValue("ModifyDateTime").trim()).append("', ");   //收款時間
    sbSQL.append("'").append("1").append("' ");                                 //PROCESS DISCOUNT
    sbSQL.append(") ");
    as400.execFromPool(sbSQL.toString());
    
    sql = "select InvoiceNo ,PointNo ,InvoiceMoney ,InvoiceTax ,InvoiceTotalMoney ,YiDiscountMoney ,DiscountItemMoney "
        + "from InvoM041 "
        + "where DiscountNo = '"+disCountNo+"'";
    String[][] detail = dbInvoice.queryFromPool(sql);
    
    sql = "DELETE FROM GLECPFUF WHERE EC01U = '"+disCountNo+"'";
    as400.execFromPool(sql);
    
    //寫入AS400折讓明細
    int GLECPFUFCount = 1;
    for(int i=0;i<detail.length;i++){
      sbSQL = new StringBuilder();
      sbSQL.append("INSERT INTO GLECPFUF ");
      sbSQL.append("(EC01U, EC02U, EC03U, EC04U, EC05U, EC06U, EC07U, EC08U, EC09U, EC10U) ");
      sbSQL.append("values ");
      sbSQL.append("(");
      sbSQL.append("'").append(disCountNo).append("', ");                         //折讓號碼
      sbSQL.append("").append(GLECPFUFCount).append(", ");                        //筆數
      sbSQL.append("'").append("Y").append("', ");                                //勾選
      sbSQL.append("'").append( detail[i][0].trim() ).append("', ");             //發票號碼
      sbSQL.append("'").append( detail[i][1].trim() ).append("', ");            //摘要代碼
      sbSQL.append("").append( detail[i][2].trim() ).append(", ");               //未稅
      sbSQL.append("").append( detail[i][3].trim() ).append(", ");               //稅額
      sbSQL.append("").append( detail[i][4].trim() ).append(", ");               //總金額
      sbSQL.append("").append( detail[i][5].trim() ).append(", ");              //已折讓金額
      sbSQL.append("").append( detail[i][6].trim() ).append(" ");                //欲折讓金額
      sbSQL.append(") ");
      as400.execFromPool(sbSQL.toString());
      GLECPFUFCount++;
    }

    System.out.println("補折讓行動>>>>>>>>>> End");
    return value;
  }

  public String getInformation() {
    return "---------------BUFAPIO(GO!!!).defaultValue()----------------";
  }
}
