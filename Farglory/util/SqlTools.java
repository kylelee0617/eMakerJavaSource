package Farglory.util;

import org.apache.commons.lang.StringUtils;

import jcx.db.talk;
import jcx.jform.bproc;

public class SqlTools extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    KUtils kUtil = new KUtils();

    String keyCode = getValue("keyCode").toString().trim();
    System.out.println("keyCode:" + keyCode);
    if (!kUtil.chkSQLpws(keyCode)) {
      messagebox("無使用權限");
      return value;
    }

    String script = getValue("script").toString().trim();
    if ("".equals(script)) {
      message("script不得為空");
      return value;
    }
    String insertYN = getValue("insertYN").toString();
    if ((script.indexOf("insert") != -1) && "N".equals(insertYN)) {
      messagebox("禁止執行 insert 語法");
      return value;
    }
    String updateYN = getValue("updateYN").toString();
    if ((script.indexOf("update") != -1) && "N".equals(updateYN)) {
      messagebox("禁止執行 update 語法");
      return value;
    }
    String deleteYN = getValue("deleteYN").toString();
    if ((script.indexOf("delete") != -1) && "N".equals(deleteYN)) {
      messagebox("禁止執行 Delete 語法");
      return value;
    }
    String database = getValue("dataBase").toString();
    if ("".equals(database)) {
      message("database不得為空");
      return value;
    }

    talk dbTalk = getTalk(database);
    StringBuilder sb = new StringBuilder();
    String[][] retTable = null;
    if (StringUtils.indexOf(script.toLowerCase(), "select") == 0) { // 查詢
      retTable = dbTalk.queryFromPool(script);
      for (int i = 0; i < retTable.length; i++) {
        String[] row1 = retTable[i];
        for (int j = 0; j < row1.length; j++) {
          sb.append(row1[j]).append(" / ");
        }
        sb.append("\n");
      }
    } else if (StringUtils.indexOf(script.toLowerCase(), "update") == 0) { // update
      retTable = new String[1][1];
      String rs = dbTalk.execFromPool(script);
      sb.append(rs);
      retTable[0][0] = rs;
    }

    setValue("sqlResult", sb.toString());
    message("done : all " + retTable.length + " Rows");

    return value;
  }

  public String getInformation() {
    return "---------------execSql(\u57f7\u884cSQL).defaultValue()----------------";
  }
}
