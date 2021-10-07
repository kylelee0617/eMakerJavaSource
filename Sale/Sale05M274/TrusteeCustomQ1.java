package Sale.Sale05M274;

import javax.swing.*;
import jcx.jform.bQuery;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import Farglory.util.KSqlUtils;

public class TrusteeCustomQ1 extends bQuery {
  public String getFilter() throws Throwable {
    // 回傳值為自定查詢條件
    // 回傳值必須是空白或以 and 開始,如 "and FIELD1='ABC'"
    // 也可以回傳完整的 SQL 語法取代原設定的值 如 select distinct display_field,data_field from table1
    // where type=100
    String orderNo = this.getTableData("table4")[0][2].trim();

    return "select cuatomName, customName from Sale05m091 where 1=1 and orderNo = '" + orderNo + "' and ISNULL(StatusCd, '') != 'C' ";
  }

  public String getInformation() {
    return "---------------null(null).setf_defined_where_clause()----------------";
  }
}
