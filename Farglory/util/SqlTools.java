package Farglory.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

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

    String script = getValue("script").toString().trim().toLowerCase();
    if ("".equals(script)) {
      message("script不得為空");
      return value;
    }

    // 拆解sql語法
    String action = StringUtils.substring(script, 0, script.indexOf(" "));
    System.out.println("action:" + action);

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

    //
    String column = StringUtils.substring(script, script.indexOf(" "), script.indexOf("from")).replaceAll("distinct", "").replaceAll("top", "").trim();
    System.out.println("column0:" + column);
    String tmpCut1 = StringUtils.substring(column, 0, column.indexOf(" ")).trim();
    if(column.indexOf(" ") > 0 && StringUtils.isNumeric(tmpCut1)) column = StringUtils.substring(column, column.indexOf(" ")).trim(); //top N 過濾數字處理
    System.out.println("column1:" + column);

    String[] tableH = null;
    List listH = new ArrayList();
    if (StringUtils.equals(column, "*")) {
      String table = StringUtils.substring(script, script.indexOf("from")).replaceAll("from", "").trim();
      System.out.println("table1:" + table);

      if (StringUtils.indexOf(table, " ") > 0) table = StringUtils.substring(table, 0, StringUtils.indexOf(table, " ")).trim();
      System.out.println("table2:" + table);

      String[][] head = dbTalk.getColumnsFromPool(table);
      for (int i = 0; i < head.length; i++) {
        listH.add(head[i][0].trim());
      }
      tableH = (String[]) listH.toArray(new String[listH.size()]);
    } else {
      String[] tmpTableH = column.split(",");
      tableH = new String[tmpTableH.length];
      for(int i=0 ; i<tmpTableH.length ; i++) {
        String tmpH = tmpTableH[i];
        if(StringUtils.contains(tmpH, " as ")) tmpH = StringUtils.substring(tmpH, tmpH.indexOf(" as ")).replaceAll("as", "").trim();
        tableH[i] = tmpH;
      }
//      tableH = column.split(",");
    }
    JTable tb1 = getTable("ResultTable");
    tb1.setName("SQL查詢結果");
    this.setTableHeader("ResultTable", tableH);

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

      this.setTableData("ResultTable", retTable);
      setValue("sqlResult", sb.toString());
    } else if (StringUtils.indexOf(script.toLowerCase(), "update") == 0 || StringUtils.indexOf(script.toLowerCase(), "delete") == 0) { // update
      retTable = new String[1][1];
      String rs = dbTalk.execFromPool(script);
      sb.append(rs);
      retTable[0][0] = rs;

      setValue("sqlResult", sb.toString());
    }

    message("done : all " + retTable.length + " Rows");

    return value;
  }

  public String getInformation() {
    return "---------------execSql(\u57f7\u884cSQL).defaultValue()----------------";
  }
}
