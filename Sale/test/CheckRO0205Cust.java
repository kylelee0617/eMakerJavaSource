package Sale.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class CheckRO0205Cust extends bproc{
  public String getDefaultValue(String value)throws Throwable{
    
    KSqlUtils ksUtil = new KSqlUtils();
    KUtils kUtil = new KUtils();
    talk dbSale = ksUtil.getTBean().getDbSale();
    talk dbAS400 = ksUtil.getTBean().getDb400CRM();
    
    String sql91 = "select a.OrderNo , a.OrderDate , b.CustomNo , b.CustomName , b.Birthday , b.MajorName , b.IndustryCode from Sale05M090 a "
                 + "left join Sale05M091 b on a.OrderNo = b.OrderNo "
                 + "where a.OrderDate BETWEEN '2021/01/01' AND '2021/10/03' "
//                 + "AND ISNULL(b.StatusCd, '') != 'C' "
                 + "order by a.OrderDate asc, a.OrderNo asc";
    String[][] ret = dbSale.queryFromPool(sql91);
    
    Map cMap = new HashMap();
    StringBuilder sb = new StringBuilder();
    for(int i=0 ; i<ret.length ; i++) {
      FirstNoIndCustBean cBean = new FirstNoIndCustBean();
      cBean.setOrderNo(ret[i][0].trim());
      cBean.setOrderDate(ret[i][1].trim());
      cBean.setCustomNo(ret[i][2].trim());
      cBean.setCustomName(ret[i][3].trim());
      cBean.setBirthday(ret[i][4].trim());
      cBean.setMajorName(ret[i][5].trim());
      cBean.setIndustryCode(ret[i][6].trim());
      cMap.put(cBean.getCustomNo(), cBean);
      
      if(i > 0) sb.append(",");
      sb.append("'").append(cBean.getCustomNo()).append("'");
    }
    
    String sql2 = "select DISTINCT RI01, RIFILE, RO0205, RO0206, RI02, RI03 from PPSLIB.PSRI02PF a " 
                + "WHERE RI02 BETWEEN 1100101 AND 1101003 "  
                + "AND Strip(IFNULL(RO0206, '')) = '高風險' "  
                + "AND RI01 in ("+sb.toString()+") "   
                + "ORDER BY RI02 ASC, ri03 ASC";
    String[][] ret2 = dbAS400.queryFromPool(sql2);
    
    StringBuilder msg = new StringBuilder();
    if(ret2.length > 0) {
      for(int j=0 ; j<ret2.length ; j++) {
        if(j >0) msg.append("\n");
        FirstNoIndCustBean cBean2 = (FirstNoIndCustBean)cMap.get(ret2[j][0].trim());
        msg.append("訂單:"+ret2[j][1].trim() + " ,id:"+cBean2.getCustomNo()+" ,姓名:"+cBean2.getCustomName()+" ,更新時間:"+ret2[j][4].trim());
      }
    }
    
    this.setValue("RiskCheckRS", msg.toString());
    
    return value;
  }
  
  class FirstNoIndCustBean {
    String orderNo = "";
    String OrderDate = "";
    String CustomNo = "";
    String CustomName = "";
    String Birthday = "";
    String MajorName = "";
    String IndustryCode = "";
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
  
  public String getInformation(){
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
