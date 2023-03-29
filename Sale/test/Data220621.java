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

public class Data220621 extends bproc {
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
    史大Date = this.getValue("StartDate").trim();
    安得Date = this.getValue("EndDate").trim();

    // 設定環境
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>環境:" + serverType);

    if (!this.欄位檢核()) return value;

    this.執行();

    return value;
  }

  private void 執行() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("有史以來的實質受益人");

    String[] title = { "實受人ID", "實受人姓名", "公司ID", "公司名" };
    this.setTableHeader("ResultTable", title);
    
    String sql = "select b.BCustomNo , b.BenName , b.CustomNo , (select top 1 c.CustomName from sale05m091 c where b.OrderNo=c.OrderNo AND b.CustomNo=c.CustomNo) " 
        + "from Sale05M091Ben b " 
        + "left join Sale05M090 t90 on t90.OrderNo = b.OrderNo " 
        + "left join Sale05M094 t94 on t94.OrderNo = b.OrderNo " 
        + "where ISNULL(t94.OrderNo , '') = '' AND ISNULL(b.StatusCd, '') != 'C' ";
    String[][] ret = dbSale.queryFromPool(sql);
    
    this.setValue("TableCount", Integer.toString(ret.length));
    this.setTableData("ResultTable", ret);

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
