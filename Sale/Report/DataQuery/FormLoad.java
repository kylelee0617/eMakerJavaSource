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
    
    //�]��
    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy/MM");
    setValue("�]��-YearMonth", dFormat.format(this.getDate()));
    
    //��P
    dFormat = new SimpleDateFormat("yyyy");
    setValue("��P-Year", dFormat.format(this.getDate()));
    
    //�M�w�֥Υ\��
    String func = "";
    if (StringUtils.contains(this.getFunctionName(), "-�]��")) {
      func = "�]��";
    } else if (StringUtils.contains(this.getFunctionName(), "-��P")) {
      func = "��P";
    }

    // �ŦX���ӥ\��N�}�������
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
