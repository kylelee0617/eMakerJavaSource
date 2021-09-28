/**
 * �������ҦC�L�o��(��)
 */
package Invoice;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.datetime;

public class InvoiceR010Batch extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    System.out.println("-----------------------------------------S");
    String pPrinter = getValue("Printer").trim(); // "TOSHIBA e-STUDIO452Series PCL6";
    //
    if ("".equals(pPrinter)) {
      message("�п�ܦC���");
      return value;
    }
    /*
     * Farglory.util.FargloryUtil exeUtil = new Farglory.util.FargloryUtil() ;
     * String pPrinterT = convert.replace(pPrinter, "\\",  "/") ;
     * if(!exeUtil.isPrinterExist2 (pPrinterT)) {
     * message("["+pPrinterT+"] �C������s�b�A�Ь���T�Ǧw�ˡC") ; return value ; }
     */
    // Runtime.getRuntime().exec("cmd /c start taskkill /f /im acrord32.exe");
    String CompanyNo = getValue("CompanyNo");
    String InvoiceDate = getValue("InvoiceDate");
    String[][] table = getTableData("table1");
    String PrintUserNo = getUser();
    Hashtable ServerPrint = new Hashtable();
    StringBuffer choose = new StringBuffer();
    int cv = 0;
    for (int x = 0; x < table.length; x++) {
      if (table[x][0].trim().equals("Y")) {
        if (choose.length() == 0) choose.append("'" + table[x][2] + "'");
        else
          choose.append(",'" + table[x][2] + "'");
        cv++;
      }
    }
    if (cv == 0) {
      messagebox("�Цܤ֤Ŀ�@�����");
      return value;
    }
    StringBuffer sql = new StringBuffer();
    sql.append(" select InvoiceNo ");
    sql.append(" from InvoM030");
    sql.append(" where DELYes='Y'");
    System.out.println("CompanyNo------>>" + CompanyNo.trim());
    System.out.println("InvoiceDate------->>>>>" + InvoiceDate.trim());
    if (CompanyNo.length() != 0) sql.append(" and CompanyNo='" + CompanyNo + "'");
    if (InvoiceDate.length() != 0)
      sql.append(" and SUBSTRING(InvoiceDate,1,4)+SUBSTRING(InvoiceDate,6,2)+SUBSTRING(InvoiceDate,9,2) between " + InvoiceDate.replaceAll("/", "") + " ");
    if (choose.length() != 0) sql.append(" and InvoiceNo IN (" + choose.toString() + ") ORDER BY InvoiceNo");
    String[][] M030 = getTalk("Invoice").queryFromPool(sql.toString());
    System.out.println("-----------------------------------------11111");
    HashMap hM030 = new HashMap();
    for (int x = 0; x < M030.length; x++) {
      hM030.put(M030[x][0], M030[x][0]);
    }
    StringBuffer vM030 = new StringBuffer();
    for (int x = 0; x < table.length; x++) {
      if (hM030.containsKey(table[x][2]) && table[x][0].trim().equals("Y")) {
        if (vM030.length() == 0) vM030.append("�o�����X�@�o�A���I��O�w�A�˵��@�o�o���M��\r\n");
        vM030.append("�o�����X : " + table[x][2] + "\r\n");
      }
    }
    System.out.println("-----------------------------------------222222");
    String PrintDateTime = datetime.getToday("YYYYmmdd") + " " + datetime.getTime("hms");
    setValue("PrintDateTime", PrintDateTime);
    ServerPrint.put("PrintDateTime", PrintDateTime);
    ServerPrint.put("CompanyNo", CompanyNo);
    ServerPrint.put("table", table);
    ServerPrint.put("PrintUserNo", PrintUserNo);
    ServerPrint.put("InvoiceDate", InvoiceDate);
    ServerPrint.put("PrintStatus", "���͵o��");
    ServerPrint.put("POSITION", "130");
    ServerPrint.put("choose", choose);

    System.out.println("PrintDateTime=====>" + PrintDateTime);
    System.out.println("CompanyNo=====>" + CompanyNo);
    System.out.println("table=====>" + table);
    System.out.println("PrintUserNo=====>" + PrintUserNo);
    System.out.println("InvoiceDate=====>" + InvoiceDate);

    System.out.println("-----------------------------------------333333");
    Hashtable PDF = (Hashtable) call("ServerPrint", "PDF", ServerPrint);
    System.out.println("-----------------------------------------44444");
    Vector PATH_A3 = (Vector) PDF.get("FILE_NAME");
    Vector vectorData = new Vector();
    String[] arrayTemp = null;
    String[] A3_PATH = (String[]) PATH_A3.toArray(new String[0]);
    talk dbInvoice = getTalk("Invoice");
    String stringSQL = "";
    // put("InvoR010_PDF",PDF);
    for (int x = 0; x < A3_PATH.length; x++) {
      // String FileName_A3 = "c:\\temp_"+x+"_A3.pdf";
      // Hashtable ServerPrint1 = new Hashtable();
      arrayTemp = new String[1];
      arrayTemp[0] = A3_PATH[x] + "_A4_Mul_Print.pdf";
      vectorData.add(arrayTemp);
      // Print Queue
      /*
       * stringSQL = " INSERT InvoR010Batch" + " ( " + " PrintTime," + " Stubpath," +
       * " Printer," + " PrintYES " + " ) " + "VALUES " + " ( " + "'" + PrintDateTime
       * + "'," + "'" + arrayTemp[0] + "'," + "'" + pPrinter + "'," + "'N'" + " ) " ;
       * 
       */
      stringSQL = " INSERT InvoR010Batch" + " ( " + " PrintTime," + " Stubpath," + " Printer," + " PrintYES " + " ) " + "VALUES " + " ( " + "'" + PrintDateTime + "'," + "'"
          + arrayTemp[0] + "'," + "'" + pPrinter + "'," + "'N'" + " ) ";
      dbInvoice.execFromPool(stringSQL);
      /*
       * try{ String PATH_ADOBE_READER = "cmd /c start acrord32"; String
       * ADOBE_READER_PRINT_COMMAND = "/s /h /t"; String SLASH = "/"; String QUOTE =
       * "\""; String SPACE = " "; // Command to be executed String lCommand =
       * PATH_ADOBE_READER + SPACE + ADOBE_READER_PRINT_COMMAND + SPACE + QUOTE +
       * arrayTemp[0] + QUOTE + SPACE + QUOTE + pPrinter + QUOTE; Process process =
       * Runtime.getRuntime().exec(lCommand); System.out.println(lCommand);
       * Thread.currentThread().sleep(8000);// }catch(Exception e){
       * System.out.println("Printer Exception:"+e.toString()); return value; }
       */
    }
    //
    if (vM030.length() != 0) message(vM030.toString());
    getButton("button5").doClick();
    messagebox("�w�i�J�L��Ƶ{");
    // messagebox("�L����");
    getButton("button1").setVisible(false);
    return value;
  }

  public String getInformation() {
    return "---------------button1(�o���M�L).defaultValue()----------------";
  }
}
