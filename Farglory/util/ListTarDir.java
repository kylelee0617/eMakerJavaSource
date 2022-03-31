package Farglory.util;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jcx.jform.bproc;

public class ListTarDir extends bproc{
  
  public String getDefaultValue(String value)throws Throwable{
    
    String url = this.getValue("TarURL").trim();
    if(StringUtils.isBlank(url)) {
      return value;
    }
    File fileTarDir =  new File(url);
    
    List listFiles = FileControl.getFiles2(fileTarDir);
    String[][] listFile = new String[listFiles.size()][1];
    for(int i=0; i<listFiles.size(); i++) {
      File file = (File)listFiles.get(i);
      listFile[i][0] = file.getPath();
      KUtils.info(listFile[i][0]);
    }
    
    this.setTableData("TarListTable", listFile);
    
    return value;
  }
  public String getInformation(){
    return "---------------LIST_SRC_DIR(\u5217\u51fa).defaultValue()----------------";
  }
}
