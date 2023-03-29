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

public class GetNoPSRIPFCustomer extends bproc {
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
    System.out.println(">>>環境:" + serverType);

    if (!this.欄位檢核()) return value;

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("主客戶於AS400無行業別");

    String[] title = { "訂單編號", "案別", "棟樓別" , "車位別" ,"客戶姓名" ,"客戶ID" ,"訂單日期" ,"簽約日期" ,"交屋日期" ,"退戶日" ,"退戶原因" //0-10
        ,"AS400風險評鑑日期" ,"AS400風險分數(RO0209)" ,"AS400風險等級(RO0210)"    //11-13 
        ,""};                                      //14
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
        + ",'' ,'' ,'' "
        + " ,'' " 
        + "from Sale05M090 a " 
        + "Left join Sale05M091 b on a.OrderNo = b.OrderNo " 
        + "left join Sale05M094 c on a.OrderNo = c.OrderNo " 
        + "where a.OrderDate BETWEEN '2019/10/01' AND '2021/10/02'  " 
        //+ "AND ISNULL(b.StatusCd, '') != 'C' " 
        + "AND ISNULL(b.MajorName, '') != '' " 
        + "AND ISNULL(b.CustomNo, '') != '' " 
        + "Order by a.OrderDate desc ";
    String[][] ret = dbSale.queryFromPool(sql91);

    //沒有PSRI02PF的人 
    List listRSF = new ArrayList();
    for (int i = 0; i < ret.length; i++) {
      String orderNo = ret[i][0].trim();
      String projectId = ret[i][1].trim();
      String customNo = ret[i][5].trim();
      String customName = ret[i][4].trim();
      String orderDate = ret[i][6].trim();
      
      String tmpDate = kUtil.getDateAfterNDays(orderDate, "/", -5);
      String[] arrTempDate = tmpDate.split("/");
      String sDate = convert.ac2roc(arrTempDate[0]) + arrTempDate[1] + arrTempDate[2];
      
      tmpDate = kUtil.getDateAfterNDays(orderDate, "/", 5);
      arrTempDate = tmpDate.split("/");
      String eDate = convert.ac2roc(arrTempDate[0]) + arrTempDate[1] + arrTempDate[2];
      
      String sql3 = "select * " 
                  + "from PSRI02PF " 
                  + "WHERE RI01= '" + customNo + "' "
                  + "and RIFILE = '"+orderNo+"' "
                  + "and RI04 = 'RY' "
                  + "and RI02 > "+sDate+" and RI02 < "+eDate+" "
                  + "order by RI02 asc, RI03 asc "
                  + "FETCH FIRST 1 ROWS ONLY ";
      String[][] ret3 = dbAS400.queryFromPool(sql3);
      if ( ret3.length == 0 ) {
        listRSF.add(ret[i]);
      }

    }
    
    //再撈出上面的人的所有PSRI02PF
    List listRS = new ArrayList();
    for(int i=0; i<listRSF.size(); i++) {
      String[] retNoPSRI02 = (String[])listRSF.get(i);
      String orderNo = retNoPSRI02[0].trim();
      String customNo = retNoPSRI02[5].trim();
      
      String sql3 = "select RI02, RO0209, RO0210 " 
          + "from PSRI02PF " 
          + "WHERE RI01= '" + customNo + "' "
          + "and RIFILE = '"+orderNo+"' "
          + "and RI04 = 'RY' "
          + "order by RI02 asc, RI03 asc ";
      String[][] ret3 = dbAS400.queryFromPool(sql3);
      
      //第一筆
      if(ret3.length > 0) {
        KUtils.info(ret3[0][0].trim());
        String psDate = convert.roc2ac(ret3[0][0].trim());
        psDate = StringUtils.substring(psDate, 0 ,4) + "/" + StringUtils.substring(psDate, 4, 6) + "/" + StringUtils.substring(psDate, 6 ,8); 
        retNoPSRI02[11] = psDate;
        retNoPSRI02[12] = ret3[0][1].trim();
        retNoPSRI02[13] = ret3[0][2].trim();
      }
      listRS.add(retNoPSRI02);
      
      //後面的
      if(ret3.length > 1) {
        for(int ii=1; ii<ret3.length; ii++) {
          String[] tmpRet = new String[24];
          KUtils.info(ret3[0][0].trim());
          String psDate = convert.roc2ac(ret3[0][0].trim());
          psDate = StringUtils.substring(psDate, 0 ,4) + "/" + StringUtils.substring(psDate, 4, 6) + "/" + StringUtils.substring(psDate, 6 ,8);
          tmpRet[11] = psDate;
          tmpRet[12] = ret3[0][1].trim();
          tmpRet[13] = ret3[0][2].trim();
          listRS.add(tmpRet);
        }
      }
      
    }
    
    String[] arrEnd = new String[24];
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
