package Farglory.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import jcx.jform.sproc;

public class ListSrcDir extends sproc {

  public String getDefaultValue(String value) throws Throwable {
    String url = "";
    String showTable = "";
    String urlObjName = "";
    if (StringUtils.equals(value, "ReLoadSRC")) {
      urlObjName = "SrcURL";
      showTable = "SrcListTable";
    } else if (StringUtils.equals(value, "ReLoadTAR")) {
      urlObjName = "TarURL";
      showTable = "TarListTable";
    }
    url = this.getValue(urlObjName).trim();

    if (StringUtils.isBlank(url)) {
      messagebox("url can't Blank");
      return value;
    }

    // url格式固定為xxxx\
    if (!StringUtils.equals(url.substring(url.length()-1), "\\")) {
      url += "\\";
    }
    this.setValue(urlObjName, url);

    File fileDir = new File(url);
    if (fileDir.isFile()) {
      messagebox("url must be a Dir !!! ");
      return value;
    }
    if (!fileDir.exists()) {
      messagebox("url not exists !!! ");
      return value;
    }

    Calendar c = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // LIST FILES
    List listFiles = FileControl.getFiles2(fileDir);
    String[][] listFile = new String[listFiles.size()][3];
    for (int i = 0; i < listFiles.size(); i++) {
      File file = (File) listFiles.get(i);
      listFile[i][0] = file.getPath().replace(url, "");
      listFile[i][1] = file.isDirectory() ? "Dir" : "File";

      c.setTimeInMillis(file.lastModified());
      String fileUpdDate = format.format(c.getTime());
      listFile[i][2] = fileUpdDate;
    }

    this.setTableData(showTable, listFile);

    return value;
  }

  public String getInformation() {
    return "---------------LIST_SRC_DIR(\u5217\u51fa).defaultValue()----------------";
  }
}
