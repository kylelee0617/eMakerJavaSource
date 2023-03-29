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

public class Data220624_ver220726 extends bproc {
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
   * 20220726 update 
   * 1. 無視換名退戶
   * 2. 不看有無400行業別
   * 3. 增加欄位，打完新風險之後，判斷PSRI02PF，若通路風險大於10 的 RO0213 非空或不等於訂單編號註記為Y
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

    String[] title = { "訂單編號", "案別", "棟樓別" , "車位別" ,"客戶姓名" ,"客戶ID" ,"訂單日期" ,"簽約日期" ,"交屋日期" ,"退戶日" ,"退戶原因" //0-10
        ,"原客戶背景(RO0201)" ,"原往來通路(RO0205)" ,"原相關地域(RO0207)" ,"原往來產品(RO0203)" ,"原風險計算(RO0209)" ,"原風險等級(RO0210)"    //11-16 
        ,"新客戶背景" ,"新往來通路" ,"新相關地域" ,"新往來產品" ,"新風險計算" ,"新風險等級"};                                                  //17-23
    this.setTableHeader("ResultTable", title);

    String sql91 = 
        "Select distinct  a.OrderNo, a.ProjectID1 " 
        + ",ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as 棟樓別 " 
        + ",ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as 車位別 " 
        + ",b.CustomName ,b.CustomNo , a.OrderDate " 
        + ",ISNULL((select aaa.ContractDate from Sale05M275_New aa, Sale05M274 aaa where aa.ContractNo=aaa.ContractNo and aa.OrderNo=a.OrderNo and aaa.IsVoid = 'N') , '') as 簽約日 " 
        + ",'' as 交屋日 " 
        + ",ISNULL(c.TrxDate, '') 退戶日 "
        + ",ISNULL((select DItemName from Sale05M233 aa where aa.BItemCd=c.BItemCd and aa.MItemCd=c.MItemCd and aa.SItemCd=c.SItemCd and aa.DItemCd=c.DItemCd), '') 退戶原因 " 
        + ",'' ,'' ,'' ,'' ,'' ,ISNULL(b.riskValue, '') "
        + ",'' ,'' ,'' ,'' ,'' ,'' " 
        + "from Sale05M090 a " 
        + "Left join Sale05M091 b on a.OrderNo = b.OrderNo " 
        + "left join Sale05M094 c on a.OrderNo = c.OrderNo " 
        + "where a.OrderDate BETWEEN '2019/12/04' AND '2021/10/02'  " 
//        + "AND ISNULL(b.StatusCd, '') != 'C' " 
        + "AND ISNULL(b.MajorName, '') != '' " 
        + "AND ISNULL(b.CustomNo, '') != '' " 
        + "Order by a.OrderDate desc ";
    String[][] ret = dbSale.queryFromPool(sql91);

    List listRS = new ArrayList();
    for (int i = 0; i < ret.length; i++) {
      String orderNo = ret[i][0].trim();
      String projectId = ret[i][1].trim();
      String customNo = ret[i][5].trim();
      String customName = ret[i][4].trim();
      String orderDate = ret[i][6].trim();
      
      //抓舊的風險分數
      String sql3 = "select RO0201, RO0205, RO0207, RO0203, RO0209, RO0210, RI02 " 
                  + "from PSRI02PF " 
                  + "WHERE RI01= '" + customNo + "' "
                  + "and RIFILE = '" + orderNo + "' "
                  + "and RI04 = 'RY' "
                  + "and RI02 > 1081001 and RI02 < 1101002 " 
                  + "order by RI02 asc, RI03 asc "
                  + "FETCH FIRST 1 ROWS ONLY ";
      String[][] ret3 = dbAS400.queryFromPool(sql3);
      if (ret3.length > 0) {
        ret[i][11] = ret3[0][0].trim();
        ret[i][12] = ret3[0][1].trim();
        ret[i][13] = ret3[0][2].trim();
        ret[i][14] = ret3[0][3].trim();
        ret[i][15] = ret3[0][4].trim();
        ret[i][16] = ret3[0][5].trim();
      }
      
      //抓交屋日
      StringBuilder sbPosition = new StringBuilder();
      String[] arrHouse = ret[i][2].trim().split(",");
      for(int j=0 ; j<arrHouse.length ; j++) {
        if(sbPosition.length() > 0) sbPosition.append(",");
        sbPosition.append("'").append(arrHouse[j].trim()).append("'"); 
      }
      String[] arrCar = ret[i][3].trim().split(",");
      for(int j=0 ; j<arrCar.length ; j++) {
        if(sbPosition.length() > 0) sbPosition.append(",");
        sbPosition.append("'").append(arrCar[j].trim()).append("'"); 
      }
      String sql4 = "select DISTINCT projectId ,OBJECT_CD , date1 from "
                  + "(select DEPT_CD+DEPT_CD_1 as projectId ,OBJECT_CD ,HANDOVER_DATE as date1 from fe5d05 "
                  + "where DEPT_CD = '" + StringUtils.substring(projectId, 0 , projectId.length()-1) + "' and OBJECT_CD in("+sbPosition.toString()+")  and ISNULL(HANDOVER_DATE, '') != '' "
                  + "union "
                  + "select DEPT_CD+DEPT_CD_1 as projectId ,OBJECT_CD ,REMIT_DATE as date1 from fe5d15 "
                  + "where DEPT_CD = '" + StringUtils.substring(projectId, 0 , projectId.length()-1) + "' and OBJECT_CD in("+sbPosition.toString()+") and ISNULL(REMIT_DATE, '') != '' ) as table1 "
                  + "order by date1 desc";
      String[][] ret4 = dbFE5D.queryFromPool(sql4);
      
      if(ret4.length > 0) {
        String date1 = convert.roc2ac(ret4[0][2].trim());
        ret[i][8] = StringUtils.substring(date1, 0, 4) + "/" + StringUtils.substring(date1, 4, 6) + "/" + StringUtils.substring(date1, 6, 8);
      }

      listRS.add(ret[i]);

    }
    String[] arrEnd = new String[23];
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
