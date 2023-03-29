package Invoice.RD;

import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import Farglory.util.*;

public class BufaPio_Query extends bproc {

  KUtils util = new KUtils();

  public String getDefaultValue(String value) throws Throwable {
    System.out.println("干祇布琩琩>>>>>>>>>> Start");
    talk dbInvoice = getTalk("" + get("put_dbInvoice"));
    talk dbAs400 = getTalk("AS400");
    String GENLIB = ((Map) get("config")).get("GENLIB").toString().trim();

    String projectId = this.getValue("projectID").trim();
    String invoiceNo = this.getValue("InvoiceNo").trim();
    String sDate = this.getValue("SDate").trim();
    String eDate = this.getValue("EDate").trim();
    String fsChar = this.getValue("FSChar").trim();
    StringBuilder sql = null;

    // not null
    if ("".equals(sDate)) {
      messagebox("Sら戳ゲ恶");
      return value;
    }

    // ㄌ酚兵ンM030祇布
    sql = new StringBuilder();
    sql.append("SELECT * FROM INVOM030 a ");
    sql.append("WHERE 1=1 ");
    sql.append("AND invoiceDate >= '" + util.formatACDate(sDate) + "' ");
    if (!"".equals(eDate)) sql.append("AND invoiceDate <= '" + util.formatACDate(eDate) + "' ");
    if (!"".equals(projectId)) sql.append("AND projectNo = '" + projectId + "' ");
    if (!"".equals(invoiceNo)) sql.append("AND invoiceNO = '" + invoiceNo + "' ");
    if (!"".equals(fsChar)) sql.append("AND substring(invoiceNO,1,2) = '" + fsChar + "' ");
    
    sql.append("ORDER BY invoiceDate desc, CreateDateTime desc , InvoiceNo desc");
    String[][] retM030 = dbInvoice.queryFromPool(sql.toString());

    if (retM030.length == 0) {
      messagebox("琩礚祇布戈");
      return value;
    }

    StringBuilder sbM030 = new StringBuilder();
    sbM030.append(":").append(retM030.length).append("掸").append("\n");
    for (int idx = 0; idx < retM030.length; idx++) {
      sbM030.append(retM030[idx][0].trim());
      sbM030.append(" / ").append(retM030[idx][1].trim());
      sbM030.append(" / ").append(retM030[idx][6].trim());
      sbM030.append(" / ").append(retM030[idx][9].trim());
      sbM030.append(" / ").append(retM030[idx][10].trim());
      sbM030.append(" / ").append(retM030[idx][12].trim());
      sbM030.append("\n");
    }
    setValue("RsMsg", sbM030.toString());

    System.out.println("干祇布琩琩>>>>>>>>>> End");
    return value;
  }

  public String getInformation() {
    return "---------------BUFAPIO(GO!!!).defaultValue()----------------";
  }
}
