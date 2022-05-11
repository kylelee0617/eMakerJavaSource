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
    tb1.setName("�D�Ȥ��AS400�L��~�O");

    String[] title = { "�Ȥ�ID", "�Ȥ�m�W", "���ڦ�~����", "���ڭ��I����", "����¾�~", "AS400��~", 
                       "RI04", "RI0205", " RO0201", " RO0202", " RO0203", " RO0204", " RO0205", " RO0206", " RO0207", "RO0208", "RO0209", "RO0210", "��~���I����", "��~���I����" };
    this.setTableHeader("ResultTable", title);

    String testText = "";
    if (isTest) testText = "top 100";
    
    
    String sql91 = "select distinct " + testText + " b.CustomNo , b.CustomName , b.MajorName, b.PositionName " 
                 + "from Sale05M090 a "
                 + "left join Sale05M091 b on a.OrderNo = b.OrderNo " 
                 + "where a.OrderDate BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "' " 
                 + "AND ISNULL(b.StatusCd, '') != 'C' "
                 + "order by b.customNo";
    String[][] ret = dbSale.queryFromPool(sql91);
    
    System.out.println(">>>test111 :" + ret.length);

    int realCount = 0; // ��ڻݭn����Ƶ���
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
