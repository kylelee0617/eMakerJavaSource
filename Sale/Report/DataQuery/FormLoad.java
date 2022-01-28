package Sale.Report.DataQuery;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import java.text.SimpleDateFormat;

public class FormLoad extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    
    //財務
    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy/MM");
    setValue("財務-YearMonth", dFormat.format(this.getDate()));
    
    //行銷
    dFormat = new SimpleDateFormat("yyyy");
    setValue("行銷-Year", dFormat.format(this.getDate()));
    
    //決定誰用功能
    String func = "";
    if (StringUtils.contains(this.getFunctionName(), "-財務")) {
      func = "財務";
    } else if (StringUtils.contains(this.getFunctionName(), "-行銷")) {
      func = "行銷";
    }

    // 符合哪個功能就開哪些欄位
    Map labels = this.getAllcLabels();
    for (Iterator it = labels.entrySet().iterator(); it.hasNext();) {
      Map.Entry mapEntry = (Map.Entry) it.next();
      String key = mapEntry.getKey().toString();
      if (StringUtils.contains(key, func)) {
        this.setVisible(key, true);
      }
    }

    return value;
  }

  public String getInformation() {
    return "---------------text1(FormLoad).defaultValue()----------------";
  }
}
