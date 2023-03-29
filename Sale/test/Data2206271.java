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
 * �פJ���wEXCEL
 * 
 * @author B04391
 *
 */
public class Data2206271 extends bproc {
  String �v�jDate;
  String �w�oDate;
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

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>Data220627 ReadExcel  ����:" + serverType);

    this.����();

    return value;
  }

  private void ����() throws Throwable {
    /**
     * 1. �פJexcle
     * 2. post to viewTable
     * 3. doRisk
     * 4. ���G�g�JviewTable
     * 5. 
     */
    
    // �إ�com����
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
    
    String[] title = { "�q��s��", "�קO", "�ɼӧO" , "����O" ,"�Ȥ�m�W" ,"�Ȥ�ID" ,"�q����" ,"ñ�����" ,"��Τ��" ,"�h���" ,"�h���]" //0-10
        ,"��Ȥ�I��(RO0201)" ,"�쩹�ӳq��(RO0205)" ,"������a��(RO0207)" ,"�쩹�Ӳ��~(RO0203)" ,"�쭷�I�p��(RO0209)" ,"�쭷�I����(RO0210)"    //11-16 
        ,"�s�Ȥ�I��" ,"�s���ӳq��" ,"�s�����a��" ,"�s���Ӳ��~" ,"�s���I�p��" ,"�s���I����" ,"�O�_����"};                                      //17-23
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
      
      //�w�]�襤�Ĥ@��
      JTable tb1 = getTable("ResultTable");
      if (tb1.getRowCount() > 0) tb1.addRowSelectionInterval(0, 0);
    }catch(Exception ex) {
      ex.printStackTrace();
    }finally {
      //����Excel
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

  public boolean ����ˮ�() throws Throwable {
    �v�jDate = this.getValue("StartDate").trim();
    �w�oDate = this.getValue("EndDate").trim();
    if (StringUtils.isBlank(�v�jDate) || StringUtils.isBlank(�w�oDate)) {
      message("���!");
      return false;
    }

    if (�v�jDate.length() == 8) {
      if (!check.isACDay(�v�jDate)) return false;
      �v�jDate = kUtil.formatACDate(�v�jDate, "/");
    } else if (�v�jDate.length() == 10 && StringUtils.contains(�v�jDate, "/")) {

    } else {
      message("�v�jDate����榡���~");
      return false;
    }

    if (�w�oDate.length() == 8) {
      if (!check.isACDay(�w�oDate)) return false;
      �w�oDate = kUtil.formatACDate(�w�oDate, "/");
    } else if (�w�oDate.length() == 10 && StringUtils.contains(�w�oDate, "/")) {

    } else {
      message("�w�oDate����榡���~");
      return false;
    }

    return true;
  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
