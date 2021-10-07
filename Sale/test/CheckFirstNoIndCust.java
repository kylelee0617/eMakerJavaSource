package Sale.test;

import java.util.HashMap;
import java.util.Map;

import Farglory.aml.RiskCustomBean;
import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class CheckFirstNoIndCust extends bproc{
  public String getDefaultValue(String value)throws Throwable{
    
    KSqlUtils ksUtil = new KSqlUtils();
    KUtils kUtil = new KUtils();
    talk dbSale = ksUtil.getTBean().getDbSale();
    talk dbAS400 = ksUtil.getTBean().getDb400CRM();
    
    String sql91 = "select a.OrderNo , a.OrderDate , b.CustomNo , b.CustomName , b.Birthday , b.MajorName , b.IndustryCode from Sale05M090 a "
                 + "left join Sale05M091 b on a.OrderNo = b.OrderNo "
                 + "where a.OrderDate BETWEEN '2021/01/01' AND '2021/10/03' "
                 + "AND ISNULL(b.StatusCd, '') != 'C' "
                 + "order by a.OrderDate asc, a.OrderNo asc";
    String[][] ret = dbSale.queryFromPool(sql91);
    
    Map cMap = new HashMap();
    StringBuilder sb = new StringBuilder();
    StringBuilder msg = new StringBuilder();
    msg.append("AS400無行業別列表 : \n");
    for(int i=0 ; i<ret.length ; i++) {
      FirstNoIndCustBean cBean = new FirstNoIndCustBean();
      cBean.setOrderNo(ret[i][0].trim());
      cBean.setOrderDate(ret[i][1].trim());
      cBean.setCustomNo(ret[i][2].trim());
      cBean.setCustomName(ret[i][3].trim());
      cBean.setBirthday(ret[i][4].trim());
      cBean.setMajorName(ret[i][5].trim());
      cBean.setIndustryCode(ret[i][6].trim());
      cMap.put(cBean.getOrderNo()+cBean.getCustomNo(), cBean);
      
      if(i > 0) sb.append(",");
      sb.append("'").append(cBean.getCustomNo()).append("'");
      
      String sql2 = "select CMTIDF, CMNAME , CVOCAT , CMLUPY , CMLUPM , CMLUPD from PLSPFLIB.CMSCLNTM where CMTIDF = '"+cBean.getCustomNo()+"' and Strip(IFNULL(CVOCAT, '')) = '' ";
      String[][] ret2 = dbAS400.queryFromPool(sql2);
      if(ret2.length > 0) {
        if(i > 0) msg.append("\n");
        msg.append("訂單編號:" + cBean.getOrderNo() + " ,訂單日期:" + cBean.getOrderDate() + " ,id:" + cBean.getCustomNo() + " ,姓名:" + cBean.getCustomName() 
                  + ", 收款行業代碼" + cBean.getIndustryCode() + " ,收款行業中文:" + cBean.getMajorName() 
                  + " ,AS400行業代碼:" + ret2[0][2].trim() + " ,更新日期:" + ret2[0][3].trim() + "/" +ret2[0][4].trim() + "/" + ret2[0][5].trim());
      }
    }
    
    setValue("RiskCheckRS", msg.toString());
    
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
