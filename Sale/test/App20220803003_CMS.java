package Sale.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;

public class App20220803003_CMS extends bproc {
  String �v�jDate;
  String �w�oDate;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbFE5D = null;
  String serverType;
  boolean isTest = true;
  
  /**
   * �ӽЮѽs�� 20220803003
   * ��CMS ��~�O�B�O�_�g�z�H
   * �}�l���g�� 22/10/17
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
    JTable tb1 = getTable("ResultTable");
    tb1.setName("�D�Ȥ��AS400�L��~�O");

    String[] title = { "�q��s��", "�קO", "�Ȥ�ID" , "�Ȥ�W" ,"��~" ,"¾��" };
    this.setTableHeader("ResultTable", title);

    String sql91 = "select distinct a.OrderNo ,ProjectID1 ,orderDate ,CustomNo ,CustomName ,b.MajorName ,PositionName " 
                  + "from Sale05M090 a " 
                  + "Left join Sale05M091 b on a.OrderNo = b.OrderNo "  
                  + "where a.OrderDate BETWEEN '2019/12/04' AND '2021/10/03' " 
                  + "AND ISNULL(b.MajorName, '') != '' " 
                  + "AND ISNULL(b.CustomNo, '') != '' " 
                  + "Order by a.OrderDate desc ,a.orderNo ";
    String[][] ret = dbSale.queryFromPool(sql91);

    List listRS = new ArrayList();
    for (int i = 0; i < ret.length; i++) {
      String orderNo = ret[i][0].trim();
      String projectId = ret[i][1].trim();
      String orderDate = ret[i][2].trim();
      String custId = ret[i][3].trim();
      String custName = ret[i][4].trim();
      String majorName = ret[i][5].trim();
      String positionName = ret[i][6].trim();
      String indCode = ksUtil.getIndustryCodeByMajorName(majorName);
      String managerFlag = ksUtil.isManager(positionName);
      
      //��s��~�O1
      String sql2 = "select CMCLTN ,CVOCAT ,CMTIDF " 
                  + "from PLSPFLIB.CMSCLNTM " 
                  + "where CMTIDF = '"+ custId +"' "
//                  + "and CVOCAT != '' and CMTIDF != '' "
                  + "order by CMLUPY desc , CMLUPM desc , CMLUPD desc , CMCLTN desc "
                  + "FETCH FIRST 1 ROWS ONLY ";
      String[][] ret2 = dbAS400.queryFromPool(sql2);
      if(ret2.length > 0) {
        String cvocat = ret2[0][1].trim();
        String cmtidf = ret2[0][2].trim();
        if(StringUtils.isBlank(cvocat) && StringUtils.isBlank(cmtidf)) {
          String sql3 = "UPDATE PLSPFLIB.CMSCLNTM SET CVOCAT = '"+ indCode +"' ,CMEXEC = '"+ managerFlag +"' "
              + "WHERE CMCLTN = '"+ret2[0][0].trim()+"' ";
          String updCMSCLNTM = dbAS400.execFromPool(sql3);
          if(!StringUtils.equals(updCMSCLNTM, "0")) {
            listRS.add(ret[i]);
          }
        }
      }
      
    }
    
    String[] arrEnd = new String[6];
    arrEnd[0] = "END";
    listRS.add(arrEnd);

    String[][] arrRS = new String[listRS.size()][];
    arrRS = (String[][]) listRS.toArray(arrRS);

    this.setValue("TableCount", Integer.toString(arrRS.length));
    this.setTableData("ResultTable", arrRS);

    if (tb1.getRowCount() > 0) tb1.addRowSelectionInterval(0, 0);
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
