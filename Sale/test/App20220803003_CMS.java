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
  String 史大Date;
  String 安得Date;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbFE5D = null;
  String serverType;
  boolean isTest = true;
  
  /**
   * 申請書編號 20220803003
   * 補CMS 行業別、是否經理人
   * 開始撰寫日 22/10/17
   *  
   */

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    dbFE5D = this.getTalk("FE5D");
    serverType = get("serverType").toString().trim();

    // 設定環境
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>環境:" + serverType);

    if (!this.欄位檢核()) return value;

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("主客戶於AS400無行業別");

    String[] title = { "訂單編號", "案別", "客戶ID" , "客戶名" ,"行業" ,"職位" };
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
      
      //更新行業別1
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
