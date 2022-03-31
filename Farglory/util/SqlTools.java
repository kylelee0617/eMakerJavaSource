package Farglory.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import jcx.db.talk;
import jcx.jform.bproc;

public class SqlTools extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    
    //檢查權限
    String keyCode = getValue("keyCode").toString().trim();
    String chkCode = get("NINJACODE2").toString().trim();
    if (!StringUtils.equals(chkCode, this.getMD5Str(keyCode).toUpperCase())) {
      messagebox("無使用權限");
      return value;
    }

    String script = getValue("script").toString().trim().toLowerCase(); // 拆解用
    String scriptReal = getValue("script").toString().trim(); // 執行用
    if ("".equals(script)) {
      message("script不得為空");
      return value;
    }

    // 拆解sql語法
    String action = StringUtils.substring(script, 0, script.indexOf(" "));
    System.out.println("action:" + action);

    String insertYN = getValue("insertYN").toString();
    if (StringUtils.equals(action, "insert") && "N".equals(insertYN)) {
      messagebox("禁止執行 insert 語法");
      return value;
    }
    String updateYN = getValue("updateYN").toString();
    if (StringUtils.equals(action, "update") && "N".equals(updateYN)) {
      messagebox("禁止執行 update 語法");
      return value;
    }
    String deleteYN = getValue("deleteYN").toString();
    if (StringUtils.equals(action, "delete") && "N".equals(deleteYN)) {
      messagebox("禁止執行 Delete 語法");
      return value;
    }

    String database = getValue("dataBase").toString();
    if ("".equals(database)) {
      message("database不得為空");
      return value;
    }
    talk dbTalk = getTalk(database);

    // start
    StringBuilder sb = new StringBuilder();
    String[][] retTable = null;
    if (StringUtils.equals(action, "select")) { // 查詢
      String column = StringUtils.substring(script, script.indexOf(" "), script.indexOf("from")).replaceAll("distinct", "").replaceAll("top", "").trim();
//      System.out.println("column0:" + column);
      String tmpCut1 = StringUtils.substring(column, 0, column.indexOf(" ")).trim();
      if (column.indexOf(" ") > 0 && StringUtils.isNumeric(tmpCut1)) column = StringUtils.substring(column, column.indexOf(" ")).trim(); // top N 過濾數字處理
//      System.out.println("column1:" + column);

      String[] tableH = null;
      List listH = new ArrayList();
      if (StringUtils.equals(column, "*")) {
        String table = StringUtils.substring(script, script.indexOf("from")).replaceAll("from", "").trim();
//        System.out.println("table1:" + table);

        if (StringUtils.indexOf(table, " ") > 0) table = StringUtils.substring(table, 0, StringUtils.indexOf(table, " ")).trim();
//        System.out.println("table2:" + table);

        String[][] head = dbTalk.getColumnsFromPool(table);
        for (int i = 0; i < head.length; i++) {
          listH.add(head[i][0].trim());
        }
        tableH = (String[]) listH.toArray(new String[listH.size()]);
      } else {
        String[] tmpTableH = column.split(",");
        tableH = new String[tmpTableH.length];
        for (int i = 0; i < tmpTableH.length; i++) {
          String tmpH = tmpTableH[i];
          if (StringUtils.contains(tmpH, " as ")) tmpH = StringUtils.substring(tmpH, tmpH.indexOf(" as ")).replaceAll("as", "").trim();
          tableH[i] = tmpH;
        }
      }
      JTable tb1 = getTable("ResultTable");
      tb1.setName("SQL查詢結果");
      this.setTableHeader("ResultTable", tableH);

      retTable = dbTalk.queryFromPool(scriptReal);
      this.setTableData("ResultTable", retTable);
    } else if (StringUtils.equals(action, "update") || StringUtils.equals(action, "delete") || StringUtils.equals(action, "insert")) { // update
      retTable = new String[1][1];
      String rs = dbTalk.execFromPool(scriptReal);
      sb.append(rs);
      retTable[0][0] = rs;
      setValue("sqlResult", sb.toString());
    } else {
      retTable = new String[1][1];
      String rs = dbTalk.execFromPool(scriptReal);
      sb.append(rs);
      retTable[0][0] = rs;
      setValue("sqlResult", sb.toString());
    }

    message("done : all " + retTable.length + " Rows");

    return value;
  }
  
  //加密處理
  public String getMD5Str(String str) {
    byte[] digest = null;
    try {
      MessageDigest md5 = MessageDigest.getInstance("md5");
      digest = md5.digest(str.getBytes("utf-8"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    // 16是表示转换为16进制数
    String md5Str = new BigInteger(1, digest).toString(16);
    return md5Str;
  }

  public String getInformation() {
    return "---------------execSql(\u57f7\u884cSQL).defaultValue()----------------";
  }
}
