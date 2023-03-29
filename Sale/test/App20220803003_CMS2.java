package Sale.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;

public class App20220803003_CMS2 extends bproc {
  String �v�jDate;
  String �w�oDate;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbFE5D = null;
  String serverType;
  boolean isTest = true;
  String[][] retRsTable;
  
  /**
   * �ӽЮѽs�� 20220803003
   * ��CMS ��~�O�B�O�_�g�z�H
   * �}�l���g�� 22/10/17
   * 
   * step2: ����W��A����update
   *  
   */

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    dbFE5D = this.getTalk("FE5D");
    serverType = get("serverType").toString().trim();

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>����:" + serverType);

    if (!this.����ˮ�()) return value;

    this.����();

    return value;
  }

  private void ����() throws Throwable {

    JTable rsTable = this.getTable("ResultTable");
    int[] selRows = rsTable.getSelectedRows();
    
    List newList = new ArrayList();
    Set keySet = new TreeSet();
    for (int idx1 = 0; idx1 < selRows.length; idx1++) {
      String[] retTable1 = retRsTable[selRows[idx1]];
      String orderNo = retTable1[0].trim();
      
      if(keySet.contains(retTable1[0].trim() + retTable1[5].trim())) continue;
      newList.add(retTable1);
      keySet.add(orderNo);
    }
    String[][] newTable = (String[][])newList.toArray(new String[newList.size()][rsTable.getColumnCount()]);

    for (int idx1 = 0; idx1 < newTable.length; idx1++) {
      kUtil.info("ReCheckRisk idx:" + idx1);
      
      String[] retTable1 = newTable[idx1];
      String majorName = retTable1[5].trim();
      String positionName = retTable1[6].trim();
      String indCode = ksUtil.getIndustryCodeByMajorName(majorName);
      String managerFlag = ksUtil.isManager(positionName);
      String cmsId = retTable1[7].trim();
      
      String sql3 = "UPDATE PLSPFLIB.CMSCLNTM SET CVOCAT = '"+ indCode +"' ,CMEXEC = '"+ managerFlag +"' WHERE CMCLTN = '"+cmsId+"' ";
      String updCMSCLNTM = dbAS400.execFromPool(sql3);
    }

    message("done");
  }

  public boolean ����ˮ�() throws Throwable {
    retRsTable = this.getTableData("ResultTable");
    if (retRsTable.length == 0) return false;

    return true;
  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
