package Farglory.util;

import java.io.File;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import jcx.jform.sproc;

public class ListSrcDir extends sproc{
  
  public String getDefaultValue(String value)throws Throwable{
    String url = "";
    String showTable = "";
    if(StringUtils.equals(value, "ReLoadSRC")) {
      url = this.getValue("SrcURL").trim();
      showTable = "SrcListTable";
    }else if(StringUtils.equals(value, "ReLoadTAR")) {
      url = this.getValue("TarURL").trim();
      showTable = "TarListTable";
    }
    
    if(StringUtils.isBlank(url)) {
      messagebox("url cant isBlank");
      return value;
    }
    
    File fileDir =  new File(url);
    if(fileDir.isFile()) {
      messagebox("url must be a Dir !!! ");
      return value;
    }
    if(!fileDir.exists()) {
      messagebox("url not exists !!! ");
      return value;
    }
    
    //LIST FILES
    List listFiles = FileControl.getFiles2(fileDir);
    String[][] listFile = new String[listFiles.size()][1];
    for(int i=0; i<listFiles.size(); i++) {
      File file = (File)listFiles.get(i);
      listFile[i][0] = file.getPath();
      KUtils.info(listFile[i][0]);
    }
    
    this.setTableData(showTable, listFile);
    
    return value;
  }
  public String getInformation(){
    return "---------------LIST_SRC_DIR(\u5217\u51fa).defaultValue()----------------";
  }
}
