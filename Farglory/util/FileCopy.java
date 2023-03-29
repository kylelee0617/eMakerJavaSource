package Farglory.util;

import java.io.File;
import org.apache.commons.lang.StringUtils;

import jcx.jform.sproc;

public class FileCopy extends sproc{
  public String getDefaultValue(String value)throws Throwable{
    
    //isL2R or isR2L  �O����k �� �k�쥪
    boolean isR2L = StringUtils.equals(this.getValue("TempSend").trim(), "<");
    
    //SRC TABLE
    String[] ii = this.getValue("SrcSelectedText").split(",");
    
    //target URL
    String srcURL;
    String tarURL;
    if(isR2L) { //�k�쥪
      srcURL = this.getValue("TarURL").trim();  
      tarURL = this.getValue("SrcURL").trim();
    } else {
      srcURL = this.getValue("SrcURL").trim();
      tarURL = this.getValue("TarURL").trim();      
    }
    if(StringUtils.isBlank(tarURL)) return value;
    
    String rsMsg = "";
    String[][] srcTableData;
    if(isR2L) {
      srcTableData = this.getTableData("TarListTable");
    } else {
      srcTableData = this.getTableData("SrcListTable");
    }
    for(int i=0; i<ii.length; i++) {
      int selRow = Integer.parseInt(ii[i].trim());
      File oldFile = new File(srcURL + srcTableData[selRow][0]);
      FileControl.copyFile(oldFile, tarURL+"/"+oldFile.getName());
    
      rsMsg += "�w�ƻs " + oldFile.getPath() + " �� " + tarURL + " \n";
    }
    
    messagebox(rsMsg);
    return value;
  }
  public String getInformation(){
    return "---------------COPY(>>>>>>).defaultValue()----------------";
  }
}
