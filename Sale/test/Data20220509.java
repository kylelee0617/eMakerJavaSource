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

public class Data20220509 extends bproc {
  String 史大Date;
  String 安得Date;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  String serverType;
  boolean isTest = true;

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
    JTable tb1 = getTable("ResultTable");
    tb1.setName("主客戶於AS400無行業別");

    String[] title = { "客戶ID", "客戶姓名", "收款行業中文", "收款風險等級", "收款職業", "AS400行業", 
                       "RI04", "RI0205", " RO0201", " RO0202", " RO0203", " RO0204", " RO0205", " RO0206", " RO0207", "RO0208", "RO0209", "RO0210", "行業風險分數", "行業風險等級" };
    this.setTableHeader("ResultTable", title);

    String testText = "";
    if (isTest) testText = "top 100";
    
    
    String sql91 = "select distinct " + testText + " b.CustomNo , b.CustomName , b.MajorName, b.PositionName " 
                 + "from Sale05M090 a "
                 + "left join Sale05M091 b on a.OrderNo = b.OrderNo " 
                 + "where a.OrderDate BETWEEN '" + 史大Date + "' AND '" + 安得Date + "' " 
                 + "AND ISNULL(b.StatusCd, '') != 'C' "
                 + "order by b.customNo";
    String[][] ret = dbSale.queryFromPool(sql91);
    
    System.out.println(">>>test111 :" + ret.length);

    int realCount = 0; // 實際需要的資料筆數
    List listRS = new ArrayList();
    for (int i = 0; i < ret.length; i++) {
      List listData = new ArrayList();
      FirstNoIndCustBean cBean = new FirstNoIndCustBean();
      cBean.setCustomNo(ret[i][0].trim());
      cBean.setCustomName(ret[i][1].trim());
      cBean.setMajorName(ret[i][2].trim());

      String sql2 = "select CMTIDF, CMNAME , CVOCAT , CMLUPY , CMLUPM , CMLUPD from PLSPFLIB.CMSCLNTM where CMTIDF = '" + cBean.getCustomNo()
          + "' and Strip(IFNULL(CVOCAT, '')) = '' ";
      String[][] ret2 = dbAS400.queryFromPool(sql2);
      if (ret2.length > 0) {
        listData.add(cBean.getCustomNo());
        listData.add(cBean.getCustomName());
        listData.add(cBean.getMajorName());
        listData.add(cBean.getRiskValue());
        listData.add(ret[i][3].trim());
        listData.add(ret2[0][2].trim());
//        listData.add(ret2[0][3].trim() + "/" + ret2[0][4].trim() + "/" + ret2[0][5].trim());
        
        String sql3 = "select RI04, RI0205, RO0201, RO0202, RO0203, RO0204, RO0205, RO0206, RO0207, RO0208, RO0209, RO0210 " 
                    + "from PPSLIB/PSRI02PF "
                    + "WHERE RI01= '" + cBean.getCustomNo() + "' "
                    + "and RI04 = 'RY' and RI0205 = 'RYB' "
                    + "and RI02 > 1081001 and RI02 < 1101002 "
                    + "order by RI02 desc  FETCH FIRST 1 ROWS ONLY ";
        String[][] ret3 = dbAS400.queryFromPool(sql3);
        if (ret3.length > 0) {
          listData.add(ret3[0][0].trim());
          listData.add(ret3[0][1].trim());
          listData.add(ret3[0][2].trim());
          listData.add(ret3[0][3].trim());
          listData.add(ret3[0][4].trim());
          listData.add(ret3[0][5].trim());
          listData.add(ret3[0][6].trim());
          listData.add(ret3[0][7].trim());
          listData.add(ret3[0][8].trim());
          listData.add(ret3[0][9].trim());
          listData.add(ret3[0][10].trim());
          listData.add(ret3[0][11].trim());
        }else {
          for(int k=0; k<12; k++) {
            listData.add("");
          }
        }
        
        String sql4 = "SELECT CZ04,CZ07 FROM PWENLIB/PDCZPF WHERE CZ01='INDUSTRY' AND CZ09='"+cBean.getMajorName()+"'";
        String[][] ret4 = dbAS400.queryFromPool(sql4);
        if (ret4.length > 0) {
          listData.add(ret4[0][0].trim());
          listData.add(ret4[0][1].trim());
        }else {
          for(int k=0; k<2; k++) {
            listData.add("");
          }
        }
        
        listRS.add((String[]) listData.toArray(new String[title.length]));
        realCount++;
      }
      
    }

    String[][] arrRS = new String[listRS.size()][];
    arrRS = (String[][]) listRS.toArray(arrRS);

    this.setValue("TableCount", Integer.toString(arrRS.length));
    this.setTableData("ResultTable", arrRS);
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

  class FirstNoIndCustBean {
    String orderNo = "";
    String OrderDate = "";
    String CustomNo = "";
    String CustomName = "";
    String Birthday = "";
    String MajorName = "";
    String IndustryCode = "";
    String riskValue = "";

    public String getRiskValue() {
      return riskValue;
    }

    public void setRiskValue(String riskValue) {
      this.riskValue = riskValue;
    }

    public String getOrderNo() {
      return orderNo;
    }

    public void setOrderNo(String orderNo) {
      this.orderNo = orderNo;
    }

    public String getOrderDate() {
      return OrderDate;
    }

    public void setOrderDate(String orderDate) {
      OrderDate = orderDate;
    }

    public String getCustomNo() {
      return CustomNo;
    }

    public void setCustomNo(String customNo) {
      CustomNo = customNo;
    }

    public String getCustomName() {
      return CustomName;
    }

    public void setCustomName(String customName) {
      CustomName = customName;
    }

    public String getBirthday() {
      return Birthday;
    }

    public void setBirthday(String birthday) {
      Birthday = birthday;
    }

    public String getMajorName() {
      return MajorName;
    }

    public void setMajorName(String majorName) {
      MajorName = majorName;
    }

    public String getIndustryCode() {
      return IndustryCode;
    }

    public void setIndustryCode(String industryCode) {
      IndustryCode = industryCode;
    }

  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
