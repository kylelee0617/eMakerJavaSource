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

public class CheckFirstNoIndCust3 extends bproc {
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

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>����:" + serverType);

    if (!this.����ˮ�()) return value;

    this.����();

    return value;
  }

  private void ����() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("�D�Ȥ����AS400�U���I��");

    String[] title = { "RI01", "RI0205", " RI0206", " RO0201", " RO0202", " RO0203", " RO0204", " RO0205", " RO0206", " RO0207", " RO0209", "RO0210", "RO0208" };
    this.setTableHeader("ResultTable", title);

    String testText = "";
    if (isTest) testText = "top 100";
    String sql91 = "select " + testText + " a.OrderNo , a.OrderDate , b.CustomNo , b.CustomName , b.Birthday , b.MajorName , b.IndustryCode, b.riskValue " 
        + "from Sale05M090 a "
        + "left join Sale05M091 b on a.OrderNo = b.OrderNo " 
        + "where a.OrderDate BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "' " 
        + "AND ISNULL(b.StatusCd, '') != 'C' "
        + "order by a.OrderDate asc, a.OrderNo asc";
    String[][] ret = dbSale.queryFromPool(sql91);

    int realCount = 0; // ��ڻݭn����Ƶ���
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

      String sql2 = "select RI01, RI0205, RI0206, RO0201, RO0202, RO0203, RO0204, RO0205, RO0206, RO0207, RO0209, RO0210, RO0208 "
              + "from PPSLIB/PSRI02PF WHERE RI01= '" + cBean.getCustomNo() + "' ";
      String[][] ret2 = dbAS400.queryFromPool(sql2);
      if (ret2.length > 0) {
        listData.add(ret2[0][0].trim());
        listData.add(ret2[0][1].trim());
        listData.add(ret2[0][2].trim());
        listData.add(ret2[0][3].trim());
        listData.add(ret2[0][4].trim());
        listData.add(ret2[0][5].trim());
        listData.add(ret2[0][6].trim());
        listData.add(ret2[0][7].trim());
        listData.add(ret2[0][8].trim());
        listData.add(ret2[0][9].trim());
        listData.add(ret2[0][10].trim());
        listData.add(ret2[0][11].trim());
        listData.add(ret2[0][12].trim());
        listRS.add((String[]) listData.toArray(new String[title.length]));
        realCount++;
      }
    }

    String[][] arrRS = new String[listRS.size()][];
    arrRS = (String[][]) listRS.toArray(arrRS);

    this.setValue("TableCount", Integer.toString(arrRS.length));
    this.setTableData("ResultTable", arrRS);
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
