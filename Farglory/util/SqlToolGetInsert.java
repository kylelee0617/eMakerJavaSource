package Farglory.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.apache.commons.lang.StringUtils;

import jcx.db.talk;
import jcx.jform.bproc;

public class SqlToolGetInsert extends bproc {
  talk dbTalk = null;
  int headType = 0;

  public String getDefaultValue(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();

    // 檢查權限
    String keyCode = getValue("keyCode").toString().trim();
    String chkCode = get("NINJACODE2").toString().trim();
    if (!StringUtils.equals(chkCode, this.getMD5Str(keyCode).toUpperCase())) {
      messagebox("無使用權限");
      return value;
    }

    JTable jTable1 = this.getTable("ResultTable");
    JTableHeader aa = jTable1.getTableHeader();
    
    

   
    return value;
  }

  // 處理表頭
  private void setTableHead(String script, String[][] retTable, int headType) throws Throwable {
    String column = StringUtils.substring(script, script.indexOf(" "), script.indexOf("from")).replaceAll("distinct", "").replaceAll("top", "").trim();
    String tmpCut1 = StringUtils.substring(column, 0, column.indexOf(" ")).trim();
    if (column.indexOf(" ") > 0 && StringUtils.isNumeric(tmpCut1)) column = StringUtils.substring(column, column.indexOf(" ")).trim(); // top N 過濾數字處理

    String[] tableH = null;
    if (headType == 1) { // 自動處理表頭
      List listH = new ArrayList();
      if (StringUtils.equals(column, "*")) {
        String table = StringUtils.substring(script, script.indexOf("from")).replaceAll("from", "").trim();

        if (StringUtils.indexOf(table, " ") > 0) table = StringUtils.substring(table, 0, StringUtils.indexOf(table, " ")).trim();

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
    } else if (headType == 2 && retTable.length > 0) { // 不處理表頭
      System.out.println(">>>retTable0.length :" + retTable[0].length);
      tableH = new String[retTable[0].length];
      for (int i = 0; i < retTable[0].length; i++) {
        tableH[i] = "column" + (i + 1);
      }
    }

    JTable tb1 = getTable("ResultTable");
    tb1.setName("SQL查詢結果");

    this.setTableHeader("ResultTable", tableH);
  }

  // 加密處理
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
    // 16是表示\u8f6c\u6362\u4e3a16\u8fdb制\u6570
    String md5Str = new BigInteger(1, digest).toString(16);
    return md5Str;
  }

  public String getInformation() {
    return "---------------execSql(\u57f7\u884cSQL).defaultValue()----------------";
  }
}
