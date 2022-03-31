package Farglory.util;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import jcx.jform.sproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

public class MkDir extends sproc {
  public String getDefaultValue(String value) throws Throwable {
    String url = "";
    if (StringUtils.equals(value, "MKDIRSRC")) {
      url = this.getValue("SrcURL").trim();
    } else if (StringUtils.equals(value, "MKDIRTAR")) {
      url = this.getValue("TarURL").trim();
    }

    if (StringUtils.isBlank(url)) {
      messagebox("url cant isBlank");
      return value;
    }

    File fileDir = new File(url);
    if (fileDir.isFile()) {
      messagebox("url must be a Dir !!! ");
      return value;
    }
    if (fileDir.exists()) {
      messagebox("dir is exists !!! ");
      return value;
    }

    fileDir.mkdirs();
    message("創建資料夾完成");

    return value;
  }

  public String getInformation() {
    return "---------------button2(MK DIR).defaultValue()----------------";
  }
}
