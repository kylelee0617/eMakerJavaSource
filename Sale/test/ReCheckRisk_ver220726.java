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
 * 處理已選的項目 
 * 重新製作列表
 * call doRisk 處理風險值
 * 匯出
 * 
 * @author B04391
 *
 */
public class ReCheckRisk_ver220726 extends bproc {
  String 史大Date;
  String 安得Date;
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

    // 設定環境
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>環境:" + serverType);
    
    if (!this.欄位檢核()) return value;

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
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
      
      //打風險值
      this.setValue("TempSend", orderNo+","+orderDate+","+projectId1);
      this.getButton("DoRisk").doClick();
      
      //如果發生錯誤
      String tempRs = this.getValue("TempResult").trim();
      if(tempRs.length() != 0) {
        retTable1[22] = tempRs;
        this.setValue("TempResult", "");
        continue;
      }
      
      //重新查詢風險分數
      String sql3 = "select RO0201, RO0205, RO0207, RO0203, RO0209, RO0210, RI02 ,RO0213 " 
          + "from PSRI02PF "
          + "WHERE RI01= '" + retTable1[5].trim() + "' "
          + "and RIFILE = '" + orderNo + "' "
          + "and RI04 = 'RY' "
          + "and RI02 = '" + this.getToday("yymmdd") + "' "
          + "order by RI02 desc, RI03 desc "
          + "FETCH FIRST 1 ROWS ONLY ";
      String[][] ret3 = dbAS400.queryFromPool(sql3);
      if(ret3.length > 0) {
        kUtil.info("new risk date:" + ret3[0][6].trim());
        String r0205 = ret3[0][1].trim();
        
        retTable1[17] = ret3[0][0].trim();
        retTable1[18] = ret3[0][1].trim();
        retTable1[19] = ret3[0][2].trim();
        retTable1[20] = ret3[0][3].trim();
        retTable1[21] = ret3[0][4].trim();
        retTable1[22] = ret3[0][5].trim();
        retTable1[23] = StringUtils.equals(retTable1[16].trim(), retTable1[22].trim()) == true? "":"Y";
        
        //220726新增 : 增加欄位，打完新風險之後，判斷PSRI02PF，若通路風險大於10 的 RO0213 非空或不等於訂單編號註記為Y
        int iR0205 = Integer.parseInt(r0205.length()>0? r0205:"0");
        if(iR0205 > 10) {
          String r0213 = ret3[0][7].trim();
          if(!StringUtils.equals(r0213, orderNo)) {
            if(!StringUtils.isBlank(r0213)){
              retTable1[24] = "Y";
            }
          }
        }
        
      }
    }
    this.setTableData("ResultTable", newTable);
    
    this.getButton("RSTableToExcel").doClick();
  }

  public boolean 欄位檢核() throws Throwable {
    retRsTable = this.getTableData("ResultTable");
    if (retRsTable.length == 0) return false;

    return true;
  }

  public String getInformation() {
    return "---------------ReCheckRisk DONE----------------";
  }
}
