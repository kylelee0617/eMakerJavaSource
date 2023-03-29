package Sale.test;

import javax.swing.*;
import org.apache.commons.lang.StringUtils;
import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.jform.bvalidate;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

// �i�۩w����ˮֱ��� 
// �ǤJ�� value ���J�� 

public class CheckNameData extends bvalidate {
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbFE5D = null;
  String serverType;
  boolean isTest = true;
  String �v�jDate;
  String �w�oDate;
  String checkName

  public boolean check(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    dbFE5D = this.getTalk("FE5D");
    serverType = get("serverType").toString().trim();
    checkName = this.getValue("CheckNameData").trim();

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>CheckNameData site:" + serverType);

    this.����();

    return true;
  }

  private void ����() throws Throwable {

    String sql91 = "Select distinct a.ProjectID1 "
        + ",ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO "
        + ",ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O "
        + ",b.CustomName ,b.CustomNo , a.OrderDate ,ISNULL(b.riskValue, '') "
        + "from Sale05M090 a "
        + "Left join Sale05M091 b on a.OrderNo = b.OrderNo "
        + "left join Sale05M094 c on a.OrderNo = c.OrderNo "
        + "where 1=1 "
        + "AND b.CustomName like '%" + checkName + "%' "
        + "Order by a.OrderDate desc ";
    String[][] ret = dbSale.queryFromPool(sql91);

//    List listRS = new ArrayList();    
//    String[] arrEnd = new String[23];
//    arrEnd[0] = "END";
//    listRS.add(arrEnd);
//
//    String[][] arrRS = new String[listRS.size()][];
//    arrRS = (String[][]) listRS.toArray(arrRS);

    this.setTableData("TableCustomer", ret);

  }


  public String getInformation() {
    return "---------------CheckNameData.field_check()----------------";
  }
}
