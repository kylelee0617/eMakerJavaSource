package Sale.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;

public class CheckRO0205Cust2 extends bproc {
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
    tb1.setName("主要客戶通路高風險");
    
    String[] title = { "訂單", "客戶ID", "客戶姓名", "AS400更新日期", "收款風險等級" };
    this.setTableHeader("ResultTable", title);

    String sql91 = "select a.OrderNo , a.OrderDate , b.CustomNo , b.CustomName , b.Birthday , b.MajorName , b.IndustryCode, b.riskValue "
        + "from Sale05M090 a "
        + "left join Sale05M091 b on a.OrderNo = b.OrderNo where a.OrderDate BETWEEN '2021/01/01' AND '2021/10/03' order by a.OrderDate asc, a.OrderNo asc";
    String[][] ret = dbSale.queryFromPool(sql91);

    Map cMap = new HashMap();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ret.length; i++) {
      FirstNoIndCustBean cBean = new FirstNoIndCustBean();
      cBean.setOrderNo(ret[i][0].trim());
      cBean.setOrderDate(ret[i][1].trim());
      cBean.setCustomNo(ret[i][2].trim());
      cBean.setCustomName(ret[i][3].trim());
      cBean.setBirthday(ret[i][4].trim());
      cBean.setMajorName(ret[i][5].trim());
      cBean.setIndustryCode(ret[i][6].trim());
      cBean.setRiskValue(ret[i][7].trim());
      cMap.put(cBean.getCustomNo(), cBean);

      if (i > 0) sb.append(",");
      sb.append("'").append(cBean.getCustomNo()).append("'");
    }

    String sql2 = "select  DISTINCT RI01, RIFILE, RO0205, RO0206, RI02, RI03 from PPSLIB.PSRI02PF a " + "WHERE RI02 BETWEEN 1100101 AND 1101003 "
        + "AND Strip(IFNULL(RO0206, '')) = '高風險' " + "AND RI01 in (" + sb.toString() + ") " + "ORDER BY RI02 ASC, ri03 ASC";
    String[][] ret2 = dbAS400.queryFromPool(sql2);

    List listRS = new ArrayList();
    for (int j = 0; j < ret2.length; j++) {
      FirstNoIndCustBean cBean2 = (FirstNoIndCustBean) cMap.get(ret2[j][0].trim());
      List listData = new ArrayList();
      listData.add(ret2[j][1].trim());
      listData.add(cBean2.getCustomNo());
      listData.add(cBean2.getCustomName());
      listData.add(ret2[j][4].trim());
      listData.add(cBean2.getRiskValue());
      listRS.add((String[]) listData.toArray(new String[title.length]));
    }

    String[][] arrRS = new String[listRS.size()][];
    arrRS = (String[][]) listRS.toArray(arrRS);

    this.setValue("TableCount", Integer.toString(arrRS.length));
    this.setTableData("ResultTable", arrRS);
  }

  private boolean 欄位檢核() throws Throwable {
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
