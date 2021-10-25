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

public class CheckFirstNoIndCust2 extends bproc {
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
    
    String[] title = { "訂單編號", "訂單日期", "客戶ID", "客戶姓名", "收款行業代碼", "收款行業中文", "AS400行業代碼", "AS400更新日期", "收款風險等級" };
    this.setTableHeader("ResultTable", title);

    String testText = "";
    if (isTest) testText = "top 100";
    String sql91 = "select " + testText + " a.OrderNo , a.OrderDate , b.CustomNo , b.CustomName , b.Birthday , b.MajorName , b.IndustryCode, b.riskValue "
        + "from Sale05M090 a "
        + "left join Sale05M091 b on a.OrderNo = b.OrderNo " + "where a.OrderDate BETWEEN '" + 史大Date + "' AND '" + 安得Date + "' " + "AND ISNULL(b.StatusCd, '') != 'C' "
        + "order by a.OrderDate asc, a.OrderNo asc";
    String[][] ret = dbSale.queryFromPool(sql91);

    int realCount = 0; // 實際需要的資料筆數
    List listRS = new ArrayList();
    for (int i = 0; i < ret.length; i++) {
      List listData = new ArrayList();
      FirstNoIndCustBean cBean = new FirstNoIndCustBean();
      cBean.setOrderNo(ret[i][0].trim());
      cBean.setOrderDate(ret[i][1].trim());
      cBean.setCustomNo(ret[i][2].trim());
      cBean.setCustomName(ret[i][3].trim());
      cBean.setBirthday(ret[i][4].trim());
      cBean.setMajorName(ret[i][5].trim());
      cBean.setIndustryCode(ret[i][6].trim());
      cBean.setRiskValue(ret[i][7].trim());

      String sql2 = "select CMTIDF, CMNAME , CVOCAT , CMLUPY , CMLUPM , CMLUPD from PLSPFLIB.CMSCLNTM where CMTIDF = '" + cBean.getCustomNo()
          + "' and Strip(IFNULL(CVOCAT, '')) = '' ";
      String[][] ret2 = dbAS400.queryFromPool(sql2);
      if (ret2.length > 0) {
        listData.add(cBean.getOrderNo());
        listData.add(cBean.getOrderDate());
        listData.add(cBean.getCustomNo());
        listData.add(cBean.getCustomName());
        listData.add(cBean.getIndustryCode());
        listData.add(cBean.getMajorName());
        listData.add(ret2[0][2].trim());
        listData.add(ret2[0][3].trim() + "/" + ret2[0][4].trim() + "/" + ret2[0][5].trim());
        listData.add(cBean.getRiskValue());
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
