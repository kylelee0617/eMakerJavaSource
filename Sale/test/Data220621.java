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
  String �v�jDate;
  String �w�oDate;
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
    �v�jDate = this.getValue("StartDate").trim();
    �w�oDate = this.getValue("EndDate").trim();

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>����:" + serverType);

    if (!this.����ˮ�()) return value;

    this.����();

    return value;
  }

  private void ����() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("���v�H�Ӫ������q�H");

    String[] title = { "����HID", "����H�m�W", "���qID", "���q�W" };
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
