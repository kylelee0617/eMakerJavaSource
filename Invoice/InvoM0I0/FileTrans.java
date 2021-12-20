package Invoice.InvoM0I0;

import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import com.jacob.activeX.*;
import com.jacob.com.*;
import Farglory.util.FargloryUtil;

public class FileTrans extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    message("");
    //
    FargloryUtil exeUtil = new FargloryUtil();
    if (getValue("FilePath").trim().length() == 0) {
      message("請輸入[檔案]位置");
      return value;
    }
    String stringFilePathOut = getValue("FilePath").trim();
    stringFilePathOut = stringFilePathOut.substring(0, stringFilePathOut.length() - 4);
    //
    if (getValue("CompanyNo").trim().length() == 0) {
      message("請輸入公司]");
      return value;
    }
    //
    // String stringProjectID = getValue("ProjectID");
    if (getValue("ProjectNo").trim().length() == 0) {
      message("請輸入[案別]");
      return value;
    }
    //
    if (getValue("InvoiceDate").trim().length() == 0) {
      message("請輸入[發票日期]");
      return value;
    }
    // 建立com元件
    ActiveXComponent Excel;
    ComThread.InitSTA();
    Excel = new ActiveXComponent("Excel.Application");
    Object objectExcel = Excel.getObject();
    Excel.setProperty("Visible", new Variant(false));
    Object objectWorkbooks = Dispatch.get(objectExcel, "Workbooks").toDispatch();
    Object objectWorkbook = Dispatch.call(objectWorkbooks, "Open", getValue("FilePath").trim()).toDispatch();
    Object objectSheets = Dispatch.get(objectWorkbook, "Sheets").toDispatch();
    Object objectSheet1 = Dispatch.get(objectWorkbook, "ActiveSheet").toDispatch();
    // Object objectSheet1 =
    // Dispatch.call(objectSheets,"Item",getValue("SheetName")).toDispatch() ;
    Dispatch.call(objectSheet1, "Activate");
    /* 發票日期 */
    String stringDate = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "A1" }, new int[1]).toDispatch(), "Value").toString();
    if (!stringDate.equals("發票日期")) {
      message("格式[A1]必須是[發票日期]");
      Excel.invoke("Quit", new Variant[] {});
      ComThread.Release();
      return value;
    }
    //
    talk dbInvoice = getTalk("發票人壽");
    String stringSQL = "";
    String stringInvoiceDate = getValue("InvoiceDate").trim();
    stringInvoiceDate = stringInvoiceDate.substring(0, 7);
    String stringUserkey = "";
    // Check
    int intRow = 2;
    String stringStatus = "";
    while (1 == 1) {
      String string統編 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "B" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string統編.equals("null")) string統編 = "";
      String string買受人 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "C" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string買受人.equals("null")) string買受人 = "";
      String string案別 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "D" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string案別.equals("null")) string案別 = "";
      String string戶別 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "E" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string戶別.equals("null")) string戶別 = "";
      String string摘要代碼 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "F" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string摘要代碼.equals("null")) string摘要代碼 = "";
      //
      if (exeUtil.doParseDouble(string摘要代碼) > 0) {
        string摘要代碼 = exeUtil.doDeleteDogAfterZero(string摘要代碼);
      }
      //
      String string收款類別 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "G" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string收款類別.equals("null")) string收款類別 = "";
      String string備註 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "H" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string備註.equals("null")) string備註 = "";
      String string備註1 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "I" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string備註1.equals("null")) string備註1 = "";
      String string金額 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "J" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string金額.equals("null")) string金額 = "";
      //
      if (string買受人.toUpperCase().equals("END") || string案別.toUpperCase().equals("END")) {
        stringStatus = "OK";
        break;
      }
      //
      if (string統編.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 統編 不可空白");
        break;
      }
      //
      if (string買受人.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 買受人 不可空白");
        break;
      }
      //
      /*
       * if (string戶別.length() == 0){ stringStatus = "Error"; message("第" + intRow +
       * "列 戶別 不可空白"); break; }
       */
      //
      if (string摘要代碼.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 摘要代碼 不可空白");
        break;
      }
      stringSQL = "SELECT PointNo FROM InvoM010  WHERE PointNo = '" + string摘要代碼 + "'";
      String retInvoM010A[][] = dbInvoice.queryFromPool(stringSQL);
      if (retInvoM010A.length == 0) {
        // System.out.println("AAA" + retInvoM010A[0][0]);
        stringStatus = "Error";
        message("第" + intRow + "列 摘要代碼 不存在");
        break;
      }
      //
      if (string備註.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 備註 不可空白");
        break;
      }
      //
      if (string金額.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 金額 不可空白");
        break;
      }
      //
      if (string統編.length() == 8 && !check.isCoId(string統編)) {
        stringStatus = "Error";
        message("第" + intRow + "列 統編 錯誤");
        break;
      }
      intRow++;
    }
    if (stringStatus.equals("Error")) {
      return value;
    }
    // 實際轉
    intRow = 2;
    stringStatus = "";
    while (1 == 1) {
      String string統編 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "B" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string統編.equals("null")) string統編 = "";
      String string買受人 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "C" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string買受人.equals("null")) string買受人 = "";
      String string案別 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "D" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string案別.equals("null")) string案別 = "";
      String string戶別 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "E" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string戶別.equals("null")) string戶別 = "";
      String string摘要代碼 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "F" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string摘要代碼.equals("null")) string摘要代碼 = "";
      //
      if (exeUtil.doParseDouble(string摘要代碼) > 0) {
        string摘要代碼 = exeUtil.doDeleteDogAfterZero(string摘要代碼);
      }
      String string收款類別 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "G" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string收款類別.equals("null")) string收款類別 = "";
      String string備註 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "H" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string備註.equals("null")) string備註 = "";
      String string備註1 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "I" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string備註1.equals("null")) string備註1 = "";
      String string金額 = Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "J" + intRow }, new int[1]).toDispatch(), "Value").toString().trim();
      if (string金額.equals("null")) string金額 = "";
      //
      if (string買受人.toUpperCase().equals("END") || string案別.toUpperCase().equals("END")) {
        stringStatus = "OK";
        break;
      }
      //
      if (string統編.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 統編 不可空白");
        break;
      }
      //
      if (string買受人.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 買受人 不可空白");
        break;
      }
      //
      /*
       * if (string戶別.length() == 0){ stringStatus = "Error"; message("第" + intRow +
       * "列 戶別 不可空白"); break; }
       */
      //
      if (string摘要代碼.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 摘要代碼 不可空白");
        break;
      }
      //
      if (string備註.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 備註 不可空白");
        break;
      }
      //
      if (string金額.length() == 0) {
        stringStatus = "Error";
        message("第" + intRow + "列 金額 不可空白");
        break;
      }
      //
      if (string統編.length() == 8 && !check.isCoId(string統編)) {
        stringStatus = "Error";
        message("第" + intRow + "列 統編 錯誤");
        break;
      }
      //
      String stringInvoiceKind = "2";
      //
      if (string統編.length() == 8 && check.isCoId(string統編)) stringInvoiceKind = "3";

      // 發票號碼
      stringSQL = "SELECT TOP 1 InvoiceYYYYMM, FSChar, StartNo, InvoiceBook, InvoiceStartNo, InvoiceEndNo, MaxInvoiceNo, "
          + " SUBSTRING(MaxInvoiceNo,3,10)+1 FROM InvoM022  WHERE CompanyNo = '" + getValue("CompanyNo").trim() + "' AND DepartNo = '5600' AND ProjectNo = '"
          + getValue("ProjectNo").trim() + "' AND InvoiceKind = '" + stringInvoiceKind + "' AND UseYYYYMM = '" + stringInvoiceDate + "' AND (MaxInvoiceDate <= '"
          + getValue("InvoiceDate").trim() + "' OR MaxInvoiceDate IS NULL) AND ENDYES = 'N'  AND CloseYes = 'N'  AND ProcessInvoiceNo = '1'";
      System.out.println(stringSQL);
      String retInvoM022[][] = dbInvoice.queryFromPool(stringSQL);
      String stringInvoiceYYYYMM = "";
      String stringFSChar = "";
      String stringStartNo = "";
      String stringInvoiceBook = "";
      String stringInvoiceStartNo = "";
      String stringInvoiceEndNo = "";
      String stringMaxInvoiceNo = "";
      String stringMaxInvoiceNo1 = "";
      String stringNowInvoiceNo = "";
      String stringEndYes = "N";
      for (int i = 0; i < retInvoM022.length; i++) {
        stringInvoiceYYYYMM = retInvoM022[i][0];
        stringFSChar = retInvoM022[i][1];
        stringStartNo = retInvoM022[i][2];
        stringInvoiceBook = retInvoM022[i][3];
        stringInvoiceStartNo = retInvoM022[i][4];
        stringInvoiceEndNo = retInvoM022[i][5];
        stringMaxInvoiceNo = retInvoM022[i][6];
        stringMaxInvoiceNo1 = retInvoM022[i][7].trim();
        if (stringMaxInvoiceNo1.length() == 7) stringMaxInvoiceNo1 = "0" + stringMaxInvoiceNo1;
        if (stringMaxInvoiceNo1.length() == 6) stringMaxInvoiceNo1 = "00" + stringMaxInvoiceNo1;
        if (stringMaxInvoiceNo1.length() == 5) stringMaxInvoiceNo1 = "000" + stringMaxInvoiceNo1;
        if (stringMaxInvoiceNo1.length() == 4) stringMaxInvoiceNo1 = "0000" + stringMaxInvoiceNo1;
        if (stringMaxInvoiceNo1.length() == 3) stringMaxInvoiceNo1 = "00000" + stringMaxInvoiceNo1;
        if (stringMaxInvoiceNo1.length() == 2) stringMaxInvoiceNo1 = "000000" + stringMaxInvoiceNo1;
        if (stringMaxInvoiceNo.length() == 0) {
          stringNowInvoiceNo = stringInvoiceStartNo;
        } else {
          stringNowInvoiceNo = stringFSChar + stringMaxInvoiceNo1;
        }
        if (stringNowInvoiceNo.equals(stringInvoiceEndNo)) {
          stringEndYes = "Y";
        }
      }
      if (stringNowInvoiceNo.length() == 0) {
        stringStatus = "Error";
        message("電腦發票已用完 請洽財務室領取!");
        break;
      }
      System.out.println(string統編 + "-" + intRow + "-" + stringNowInvoiceNo + "-" + stringInvoiceEndNo);
      // 明細
      Calendar cal = Calendar.getInstance();// Current time
      stringUserkey = getUser() + "_T" + ((cal.get(Calendar.HOUR_OF_DAY) * 10000) + (cal.get(Calendar.MINUTE) * 100) + cal.get(Calendar.SECOND));
      stringSQL = "DELETE  FROM InvoM030TempBody  WHERE UseKey = '" + stringUserkey + "'";
      dbInvoice.execFromPool(stringSQL);
      //
      if (string備註.length() > 0 && string備註1.length() > 0) {
        stringSQL = " INSERT  INTO InvoM030TempBody( UseKey, RecordNo, DetailItem, Remark )  VALUES ('" + stringUserkey + "',1," + "'" + string收款類別 + "','" + string備註 + "')";
        dbInvoice.execFromPool(stringSQL);
        stringSQL = " INSERT  INTO InvoM030TempBody( UseKey, RecordNo, DetailItem, Remark )  VALUES ('" + stringUserkey + "',2," + "' ','" + string備註1 + "')";
        dbInvoice.execFromPool(stringSQL);
      }
      //
      if (string備註.length() > 0 && string備註1.length() == 0) {
        stringSQL = " INSERT  INTO InvoM030TempBody( UseKey, RecordNo, DetailItem, Remark )  VALUES ('" + stringUserkey + "',1," + "'" + string收款類別 + "','" + string備註 + "')";
        dbInvoice.execFromPool(stringSQL);
      }
      //
      if (string備註.length() == 0 && string備註1.length() > 0) {
        stringSQL = " INSERT  INTO InvoM030TempBody( UseKey, RecordNo, DetailItem, Remark )  VALUES ('" + stringUserkey + "',1," + "'" + string收款類別 + "','" + string備註1 + "')";
        dbInvoice.execFromPool(stringSQL);
      }
      //
      stringSQL = " SELECT TaxRate,  TaxKind  FROM InvoM010  WHERE PointNo = '" + string摘要代碼 + "'";
      String retInvoM010[][] = dbInvoice.queryFromPool(stringSQL);
      String stringInvoiceTax = "0";
      String stringInvoiceMoney = "0";
      String stringInvoiceTotalMoney = "0";
      stringInvoiceTotalMoney = string金額;
      double floatInvoiceTotalMoney = Double.parseDouble(stringInvoiceTotalMoney);
      String stringTaxRate = "";
      String stringTaxKind = "";
      if (retInvoM010.length > 0) {
        stringTaxRate = retInvoM010[0][0];
        stringTaxKind = retInvoM010[0][1];
        double floatTaxRate = Double.parseDouble(stringTaxRate);
        if (floatTaxRate > 0) {
          stringInvoiceMoney = "" + floatInvoiceTotalMoney / (1 + floatTaxRate / 100);
        } else {
          stringInvoiceMoney = stringInvoiceTotalMoney;
        }
        stringInvoiceMoney = convert.FourToFive(stringInvoiceMoney, 0);
        stringInvoiceTax = "" + (floatInvoiceTotalMoney - Double.parseDouble(stringInvoiceMoney));
        stringInvoiceTax = convert.FourToFive(stringInvoiceTax, 0);
      }
      //
      String retSystemDateTime[][] = dbInvoice.queryFromPool("spInvoSystemDateTime  'Admin'");
      String stringSystemDateTime = "";
      stringSystemDateTime = retSystemDateTime[0][0].replace("-", "/");
      stringSystemDateTime = stringSystemDateTime.substring(0, 19);
      stringSQL = "spInvoM030Insert '" + stringNowInvoiceNo + "','" + stringFSChar + "','" + stringStartNo + "','" + getValue("InvoiceDate").trim() + "','" + stringInvoiceKind
          + "','" + getValue("CompanyNo").trim() + "','5600','" + getValue("ProjectNo").trim() + "','A','" + string戶別 + "','" + string統編 + "','" + string摘要代碼 + "',"
          + stringInvoiceMoney + "," + stringInvoiceTax + "," + stringInvoiceTotalMoney + ",'A','1','" + stringInvoiceYYYYMM + "'," + stringInvoiceBook + ",'" + stringEndYes
          + "','" + getUser() + "','" + stringSystemDateTime + "','" + stringSystemDateTime + "','A','" + stringUserkey + "'";
      dbInvoice.execFromPool(stringSQL);
      // RollBack
      stringSQL = " INSERT  INTO InvoM0I0RollBack( UseKey, InvoiceNo  )  VALUES ('" + stringUserkey + "','" + stringNowInvoiceNo + "'" + ")";
      dbInvoice.execFromPool(stringSQL);
      //
      stringSQL = "SELECT CustomNo FROM InvoM0C0  WHERE CustomNo = '" + string統編 + "'";
      String retInvoM0C0[][] = dbInvoice.queryFromPool(stringSQL);
      if (retInvoM0C0.length == 0) {
        stringSQL = " INSERT  INTO InvoM0C0( CustomNo, CustomName, Transfer )  VALUES ('" + string統編 + "',N'" + string買受人 + "'," + "'Y')";
        dbInvoice.execFromPool(stringSQL);
      }
      Dispatch.put(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "K" + intRow }, new int[1]).toDispatch(), "Value", "");
      Dispatch.put(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "K" + intRow }, new int[1]).toDispatch(), "Value", stringNowInvoiceNo);
      intRow++;
    }
    if (stringStatus.equals("Error")) {
      stringSQL = "SELECT InvoiceNo  FROM InvoM0I0RollBack  WHERE UseKey = '" + stringUserkey + "'";
      String retInvoM0I0RollBack[][] = dbInvoice.queryFromPool(stringSQL);
      for (int i = 0; i < retInvoM0I0RollBack.length; i++) {
        stringSQL = " spInvoM0F0Delete '" + retInvoM0I0RollBack[i][0].trim() + "'";
        dbInvoice.execFromPool(stringSQL);
      }
      // Excel.invoke("Quit", new Variant[] {});
      // Dispatch.put(objectWorkbook, "Quit", new Variant(true));
      Excel.setProperty("DisplayAlerts", new Variant(false));
      Excel.invoke("Quit", new Variant[] {});
      ComThread.Release();
    }
    if (stringStatus.equals("OK")) {
      // Dispatch.call(objectSheet1, "Activate");
      message("OK!");
      Dispatch.call(objectWorkbook, "SaveAs", stringFilePathOut + "(含發票號碼).XLS");
      Excel.invoke("Quit", new Variant[] {});
      ComThread.Release();
    }
    //
    stringSQL = " DELETE  FROM InvoM0I0RollBack WHERE UseKey = '" + stringUserkey + "'";
    dbInvoice.execFromPool(stringSQL);
    return value;
  }
}
