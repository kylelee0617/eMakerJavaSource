package Sale.Sale05M274;

import javax.swing.JTable;

import Farglory.util.KSqlUtils;
import jcx.jform.bvalidate;

public class TrusteeCustomCheck extends bvalidate {
  public boolean check(String value) throws Throwable {
    
    KSqlUtils ksUtil = new KSqlUtils();
    String orderNo = this.getTableData("table1")[0][5].trim();
    String custName = value;
    String custNo = "";
    String thisTable = "table27";
    
    String sql = "select top 1 customNo from Sale05m091 where orderNo = '"+orderNo+"' and CustomName = '"+custName+"' and ISNULL(StatusCd, '') != 'C' ";
    String[][] ret = ksUtil.getTBean().getDbSale().queryFromPool(sql);
    if(ret.length <= 0)  return false; 
      
    custNo = ret[0][0].trim();
    
    JTable tb1 = this.getTable(thisTable);
    int sRow = tb1.getSelectedRow();
    this.setValueAt(thisTable, custNo, sRow, "CustomNo");

    return true;
  }

  public String getInformation() {
    return "---------------null(null).CustomName.field_check()----------------";
  }
}
