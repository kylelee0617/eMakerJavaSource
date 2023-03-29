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

/**
 * �B�z�w�諸���� 
 * ���s�s�@�C��
 * call doRisk �B�z���I��
 * �ץX
 * 
 * @author B04391
 *
 */
public class ReCheckRisk_Ver2 extends bproc {
  String �v�jDate;
  String �w�oDate;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  String serverType;
  boolean isTest = true;
  String[][] retRsTable;

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    serverType = get("serverType").toString().trim();

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>����:" + serverType);
    
    if (!this.����ˮ�()) return value;

    this.����();

    return value;
  }

  private void ����() throws Throwable {
    //AS400 LIB SET
    String PSLIB = get("PSLIB").toString();
    
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
      String orderNo = retTable1[0].trim();
      String orderDate = retTable1[1].trim();
      String projectId1 = retTable1[6].trim();
      String custNo = retTable1[5].trim();
      
      //�����I��
      this.setValue("TempSend", orderNo+","+orderDate+","+projectId1);
      this.getButton("DoRisk").doClick();
      
      //�p�G�o�Ϳ��~
      String tempRs = this.getValue("TempResult").trim();
      if(tempRs.length() != 0) {
        retTable1[22] = tempRs;
        this.setValue("TempResult", "");
        continue;
      }
      
      //���s�d�߭��I����
      String sql3 = "select RO0201, RO0205, RO0207, RO0203, RO0209, RO0210, RI02 " 
          + "from PSRI02PF "
          + "WHERE RI01= '" + custNo + "' "
          + "and RIFILE = '" + orderNo + "' "
          + "and RI04 = 'RY' "
          + "and RI02 = '" + this.getToday("yymmdd") + "' "
          + "order by RI02 desc, RI03 desc "
          + "FETCH FIRST 1 ROWS ONLY ";
      String[][] ret3 = dbAS400.queryFromPool(sql3);
      if(ret3.length > 0) {
        kUtil.info("new risk date:" + ret3[0][6].trim());
        
        retTable1[17] = ret3[0][0].trim();
        retTable1[18] = ret3[0][1].trim();
        retTable1[19] = ret3[0][2].trim();
        retTable1[20] = ret3[0][3].trim();
        retTable1[21] = ret3[0][4].trim();
        retTable1[22] = ret3[0][5].trim();
        retTable1[23] = StringUtils.equals(retTable1[16].trim(), retTable1[22].trim()) == true? "":"Y"; //�O�_����
      }
      
      //���Ҧ�~�O
      String sqlInd = "select CMTIDF, CMNAME ,CVOCAT ,CMLUPY ,CMLUPM ,CMLUPD "
          + "from PLSPFLIB.CMSCLNTM "
          + "where CMTIDF = '" + custNo + "' "
          + "order by CMLUPY desc , CMLUPM desc , CMLUPD desc "
          + "FETCH FIRST 1 ROWS ONLY ";
      String[][] retInd = dbAS400.queryFromPool(sqlInd);
      if(retInd.length > 0 && StringUtils.isNotBlank(retInd[0][2].trim())) {
        KUtils.info("CVOCAT date:" + retInd[0][3].trim()+retInd[0][4].trim()+retInd[0][5].trim());
        retTable1[25] = retInd[0][2].trim();
      }
      
    }
    this.setTableData("ResultTable", newTable);
    
    this.getButton("RSTableToExcel").doClick();
  }

  public boolean ����ˮ�() throws Throwable {
    retRsTable = this.getTableData("ResultTable");
    if (retRsTable.length == 0) return false;

    return true;
  }

  public String getInformation() {
    return "---------------ReCheckRisk DONE----------------";
  }
}
