package Farglory.util;

import org.apache.commons.lang.StringUtils;

import jcx.jform.bproc;

public class Prev extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    String urlName = "";
    String showTableName = "";
    String reLoadBtnName = "";
    
    String btnName = this.getName();
    if (StringUtils.contains(btnName, "SRC")) {
      urlName = "SrcURL";
      showTableName = "SrcListTable";
      reLoadBtnName = "LIST_SRC_DIR";
    } else if (StringUtils.contains(btnName, "TAR")) {
      urlName = "TarURL";
      showTableName = "TarListTable";
      reLoadBtnName = "LIST_TAR_DIR";
    }
    
    String url = this.getValue(urlName).trim();
    if("\\".equals(url.substring(url.length()-1))) url = url.substring(0 ,url.length()-1);
    
    String rsUrl = StringUtils.substring(url, 0 , url.lastIndexOf("\\")+1);
    this.setValue(urlName, rsUrl);
    this.getButton(reLoadBtnName).doClick();

    return value;
  }

  public String getInformation() {
    return "---------------PrevSRC(\u2191).defaultValue()----------------";
  }
}
