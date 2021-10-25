package Sale.test;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.apache.commons.lang.StringUtils;

public class Test2 extends jcx.jform.bproc {
  public String getDefaultValue(String value) throws Throwable {

    JTable tb1 = this.getTable("ResultTable");
    DefaultTableModel dtm = (DefaultTableModel) tb1.getModel();
    Vector v = new Vector();
    int columnNum = 0;
    while(true) {
      String columnName = dtm.getColumnName(columnNum);
      System.out.println(">>>tt:" + columnName);
      if(StringUtils.isBlank(columnName)) break;
      v.add(columnName);
      columnNum++;
    }
    String[] title = (String[]) v.toArray(new String[v.size()]);
    

    return value;
  }

  public String getInformation() {
    return "---------------Test2(Test2).defaultValue()----------------";
  }
}
