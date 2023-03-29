package Farglory.util;

import org.apache.commons.lang.StringUtils;
import jcx.jform.bdisplay;

public class ReSearchDir extends bdisplay {
  
  
  
  public void actionPerformed(int value) throws Throwable {
    // 可自定當表格選取某一行後，要做什麼事,傳入值為選取第幾行

    String[] retTable1 = this.getTableData("TarListTable")[value];
    if("Dir".equals(retTable1[1])) {
      this.setValue("TarURL", retTable1[0].trim());
      this.getButton("LIST_TAR_DIR").doClick();
    }
    
    return;
  }

  public String getInformation() {
    return "---------------SrcListTable().select on cell trigger----------------";
  }
}
