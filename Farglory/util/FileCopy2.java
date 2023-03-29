package Farglory.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import jcx.jform.sproc;

public class FileCopy2 extends sproc{
  public String getDefaultValue(String value)throws Throwable{
    
    //targetURL
    String tarURL = this.getValue("TarURL").trim();
    if(StringUtils.isBlank(tarURL)) return value;
    
    String filePath2 = this.getValue("FilePath2").trim();
    File oldFile = new File(filePath2);
    FileControl.copyFile(oldFile, tarURL+"/"+oldFile.getName());
    String rsMsg = "¤w½Æ»s " + oldFile.getPath() + " ¨ì " + tarURL + " \n";
    
    messagebox(rsMsg);
    return value;
  }
  
  public String getInformation(){
    return "---------------COPY(>>>>>>).defaultValue()----------------";
  }
}
