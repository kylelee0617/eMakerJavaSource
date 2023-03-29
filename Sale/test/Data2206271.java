package Sale.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;

/**
 * 匯入指定EXCEL
 * 
 * @author B04391
 *
 */
public class Data2206271 extends bproc {
  String 史大Date;
  String 安得Date;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbFE5D = null;
  String serverType;
  boolean isTest = true;

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    dbFE5D = this.getTalk("FE5D");
    serverType = get("serverType").toString().trim();

    // 設定環境
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>Data220627 ReadExcel  環境:" + serverType);

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
    /**
     * 1. 匯入excle
     * 2. post to viewTable
     * 3. doRisk
     * 4. 結果寫入viewTable
     * 5. 
     */
    
    // 建立com元件
    String filePath = "G:\\kyleTest\\Excel\\Test\\PaperExcel3.xls";
    String filePath2 = getValue("FilePath").trim();
    ActiveXComponent Excel;
    ComThread.InitSTA();
    Excel = new ActiveXComponent("Excel.Application");
    Object objectExcel = Excel.getObject();
    Excel.setProperty("Visible", new Variant(false));
    Object objectWorkbooks = Dispatch.get(objectExcel, "Workbooks").toDispatch();
    Object objectWorkbook = Dispatch.call(objectWorkbooks, "Open", filePath).toDispatch();
    Object objectSheet1 = Dispatch.get(objectWorkbook, "ActiveSheet").toDispatch();
    Dispatch.call(objectSheet1, "Activate");
    
    String[] title = { "訂單編號", "案別", "棟樓別" , "車位別" ,"客戶姓名" ,"客戶ID" ,"訂單日期" ,"簽約日期" ,"交屋日期" ,"退戶日" ,"退戶原因" //0-10
        ,"原客戶背景(RO0201)" ,"原往來通路(RO0205)" ,"原相關地域(RO0207)" ,"原往來產品(RO0203)" ,"原風險計算(RO0209)" ,"原風險等級(RO0210)"    //11-16 
        ,"新客戶背景" ,"新往來通路" ,"新相關地域" ,"新往來產品" ,"新風險計算" ,"新風險等級" ,"是否異動"};                                      //17-23
    this.setTableHeader("ResultTable", title);
    
    int intRow = 3;
    List listImport = new ArrayList();
    try {
      while(1==1) {
        String orderNo = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "A" + intRow }, new int[1]).toDispatch(), "Value").toString()).trim(); 
        if(StringUtils.equals(orderNo.toUpperCase(), "END"))  break;
        
        String[] tempArr = new String[24];
        tempArr[0] = orderNo;
        tempArr[1] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "B" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[2] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "C" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[3] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "D" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[4] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "E" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[5] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "F" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[6] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "G" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[7] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "H" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[8] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "I" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[9] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "J" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[10] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "K" + intRow }, new int[1]).toDispatch(), "Value").toString());
        
        tempArr[11] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "L" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[12] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "M" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[13] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "N" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[14] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "O" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[15] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "P" + intRow }, new int[1]).toDispatch(), "Value").toString());
        tempArr[16] = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "Q" + intRow }, new int[1]).toDispatch(), "Value").toString());
        
        tempArr[17] = "";
        tempArr[18] = "";
        tempArr[19] = "";
        tempArr[20] = "";
        tempArr[21] = "";
        tempArr[22] = "";
        listImport.add(tempArr);
        
        intRow++;
      }
      String[][] arrRS = new String[listImport.size()][];
      arrRS = (String[][]) listImport.toArray(arrRS);
      this.setTableData("ResultTable", arrRS);
      this.setValue("TableCount", Integer.toString(arrRS.length));
      
      //預設選中第一筆
      JTable tb1 = getTable("ResultTable");
      if (tb1.getRowCount() > 0) tb1.addRowSelectionInterval(0, 0);
    }catch(Exception ex) {
      ex.printStackTrace();
    }finally {
      //關閉Excel
      Excel.setProperty("DisplayAlerts", new Variant(false));
      Excel.invoke("Quit", new Variant[] {});
      ComThread.Release();
    }
    
  }
  
  private String isNull(String text) {
    if (StringUtils.isBlank(text) || StringUtils.equals(text.toLowerCase(), "null")) return "";
    
    return text.trim();
  }

  private boolean isBlank(String test) {
    boolean rs = false;
    if (StringUtils.isBlank(test) || StringUtils.equals(test, "null")) rs = true;
    return rs;
  }

  public boolean 欄位檢核() throws Throwable {
    史大Date = this.getValue("StartDate").trim();
    安得Date = this.getValue("EndDate").trim();
    if (StringUtils.isBlank(史大Date) || StringUtils.isBlank(安得Date)) {
      message("日期!");
      return false;
    }

    if (史大Date.length() == 8) {
      if (!check.isACDay(史大Date)) return false;
      史大Date = kUtil.formatACDate(史大Date, "/");
    } else if (史大Date.length() == 10 && StringUtils.contains(史大Date, "/")) {

    } else {
      message("史大Date日期格式錯誤");
      return false;
    }

    if (安得Date.length() == 8) {
      if (!check.isACDay(安得Date)) return false;
      安得Date = kUtil.formatACDate(安得Date, "/");
    } else if (安得Date.length() == 10 && StringUtils.contains(安得Date, "/")) {

    } else {
      message("安得Date日期格式錯誤");
      return false;
    }

    return true;
  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
