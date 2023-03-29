package Farglory.util;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import jcx.jform.bproc;

public class StartCopy extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    
    //isL2R or isR2L  是左到右 或 右到左
    boolean isR2L = StringUtils.equals(value.trim(), "<"); 
    
    JTable j1;
    if(isR2L) {
      j1 = this.getTable("TarListTable");
    }else {
      j1 = this.getTable("SrcListTable");
    }
    int[] ii = j1.getSelectedRows();

    String tmpIndex = "";
    for (int i = 0; i < ii.length; i++) {
      if (i != 0) {
        tmpIndex += ",";
      }
      tmpIndex += ii[i];
    }

    setValue("SrcSelectedText", tmpIndex);
    setValue("TempSend", value.trim());

    getButton("FileCopy").doClick();

    return value;
  }

  public String getInformation() {
    return "---------------COPY(>>>>>>).defaultValue()----------------";
  }
}
