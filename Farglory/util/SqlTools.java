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
      messagebox("�L�ϥ��v��");
      return value;
    }

    String script = getValue("script").toString().trim().toLowerCase();
    if ("".equals(script)) {
      message("script���o����");
      return value;
    }

    // ���sql�y�k
    String action = StringUtils.substring(script, 0, script.indexOf(" "));
    System.out.println("action:" + action);

    String insertYN = getValue("insertYN").toString();
    if ((script.indexOf("insert") != -1) && "N".equals(insertYN)) {
      messagebox("�T����� insert �y�k");
      return value;
    }
    String updateYN = getValue("updateYN").toString();
    if ((script.indexOf("update") != -1) && "N".equals(updateYN)) {
      messagebox("�T����� update �y�k");
      return value;
    }
    String deleteYN = getValue("deleteYN").toString();
    if ((script.indexOf("delete") != -1) && "N".equals(deleteYN)) {
      messagebox("�T����� Delete �y�k");
      return value;
    }

    String database = getValue("dataBase").toString();
    if ("".equals(database)) {
      message("database���o����");
      return value;
    }
    talk dbTalk = getTalk(database);

    // start
    StringBuilder sb = new StringBuilder();
    String[][] retTable = null;
    if (StringUtils.equals(action, "select") ) { // �d��
      String column = StringUtils.substring(script, script.indexOf(" "), script.indexOf("from")).replaceAll("distinct", "").replaceAll("top", "").trim();
//      System.out.println("column0:" + column);
      String tmpCut1 = StringUtils.substring(column, 0, column.indexOf(" ")).trim();
      if (column.indexOf(" ") > 0 && StringUtils.isNumeric(tmpCut1)) column = StringUtils.substring(column, column.indexOf(" ")).trim(); // top N �L�o�Ʀr�B�z
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
      tb1.setName("SQL�d�ߵ��G");
      this.setTableHeader("ResultTable", tableH);

      retTable = dbTalk.queryFromPool(script);
      this.setTableData("ResultTable", retTable);
    } else if (StringUtils.equals(action, "update") || StringUtils.equals(action, "delete") || StringUtils.equals(action, "insert")) { // update
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
