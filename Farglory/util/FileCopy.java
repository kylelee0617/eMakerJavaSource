package Farglory.util;

import java.io.File;
import org.apache.commons.lang.StringUtils;

import jcx.jform.sproc;

public class FileCopy extends sproc{
  public String getDefaultValue(String value)throws Throwable{
    
    //SRC TABLE
    String[] ii = this.getValue("SrcSelectedText").split(",");
    
    //focusURL
    String tarURL = this.getValue("TarURL").trim();
    if(StringUtils.isBlank(tarURL)) return value;
    
    String rsMsg = "";
    String[][] srcTableData = this.getTableData("SrcListTable");
    for(int i=0; i<ii.length; i++) {
      int selRow = Integer.parseInt(ii[i].trim());
      File oldFile = new File(srcTableData[selRow][0]);
      FileControl.copyFile(oldFile, tarURL+"/"+oldFile.getName());
      
      rsMsg += "¤w½Æ»s " + oldFile.getPath() + " ¨ì " + tarURL + " \n";
    }
    
    messagebox(rsMsg);
    return value;
  }
  public String getInformation(){
    return "---------------COPY(>>>>>>).defaultValue()----------------";
  }
}
