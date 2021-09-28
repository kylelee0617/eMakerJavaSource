package Farglory.aml;

import Farglory.util.QueryLogBean;
import jcx.db.talk;

/**
 * 計算風險值 客戶物件
 * @author B04391
 *
 */

public class RiskCustomBean {
  private QueryLogBean qBean = null;
  private String orderNo = "";
  private String recordNo = "";
  private String custTitle = "客戶";
  private String customNo = "";
  private String customName = "";
  private String percentage = "";
  private String agentRel = "";
  private String custTitle2 = "客戶";
  private String customNo2 = "";
  private String customName2 = "";
  private String birthday = "";
  private String positionName = "";
  private String countryName = "";
  private String zip = "";
  private String city = "";
  private String town = "";
  private String address = "";
  private String cellphone = "";
  private String tel = "";
  private String tel2 = "";
  private String email = "";
  private String statusCd = "";
  private String majorName = "";
  private String industryCode = "";
  private String professionCode1 = "";
  private String professionCode2 = "";
  private String professionName1 = "";
  private String professionName2 = "";
  private String auditorship = "";
  private String bStatus = "";
  private String cStatus = "";
  private String rStatus = "";
  private String trxDate = "";
  private String trxDateDown = "";
  private String nationality = "";
  private String countryName2 = "";
  private String engNo = "";
  private String engName = "";
  private String riskValue;
  
  
  public String getRecordNo() {
    return recordNo;
  }
  public void setRecordNo(String recordNo) {
    this.recordNo = recordNo;
  }
  public String getCellphone() {
    return cellphone;
  }
  public void setCellphone(String cellphone) {
    this.cellphone = cellphone;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getAuditorship() {
    return auditorship;
  }
  public void setAuditorship(String auditorship) {
    this.auditorship = auditorship;
  }
  public String getTrxDate() {
    return trxDate;
  }
  public void setTrxDate(String trxDate) {
    this.trxDate = trxDate;
  }
  public String getNationality() {
    return nationality;
  }
  public void setNationality(String nationality) {
    this.nationality = nationality;
  }
  public String getRiskValue() {
    return riskValue;
  }
  public void setRiskValue(String riskValue) {
    this.riskValue = riskValue;
  }
  public String getCountryName2() {
    return countryName2;
  }
  public void setCountryName2(String countryName2) {
    this.countryName2 = countryName2;
  }
  public String getEngNo() {
    return engNo;
  }
  public void setEngNo(String engNo) {
    this.engNo = engNo;
  }
  public String getEngName() {
    return engName;
  }
  public void setEngName(String engName) {
    this.engName = engName;
  }
  public String getCustTitle() {
    return custTitle;
  }
  public void setCustTitle(String custTitle) {
    this.custTitle = custTitle;
  }
  public String getCustomNo() {
    return customNo;
  }
  public void setCustomNo(String customNo) {
    this.customNo = customNo;
  }
  public String getCustomName() {
    return customName;
  }
  public void setCustomName(String customName) {
    this.customName = customName;
  }
  public String getCustTitle2() {
    return custTitle2;
  }
  public void setCustTitle2(String custTitle2) {
    this.custTitle2 = custTitle2;
  }
  public String getCustomNo2() {
    return customNo2;
  }
  public void setCustomNo2(String customNo2) {
    this.customNo2 = customNo2;
  }
  public String getCustomName2() {
    return customName2;
  }
  public void setCustomName2(String customName2) {
    this.customName2 = customName2;
  }
  public String getBirthday() {
    return birthday;
  }
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }
  public String getPositionName() {
    return positionName;
  }
  public void setPositionName(String positionName) {
    this.positionName = positionName;
  }
  public String getCountryName() {
    return countryName;
  }
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }
  public String getZip() {
    return zip;
  }
  public void setZip(String zip) {
    this.zip = zip;
  }
  public String getCity() {
    return city;
  }
  public void setCity(String city) {
    this.city = city;
  }
  public String getTown() {
    return town;
  }
  public void setTown(String town) {
    this.town = town;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public String getTel() {
    return tel;
  }
  public void setTel(String tel) {
    this.tel = tel;
  }
  public String getTel2() {
    return tel2;
  }
  public void setTel2(String tel2) {
    this.tel2 = tel2;
  }
  public String getStatusCd() {
    return statusCd;
  }
  public void setStatusCd(String statusCd) {
    this.statusCd = statusCd;
  }
  public String getMajorName() {
    return majorName;
  }
  public void setMajorName(String majorName) {
    this.majorName = majorName;
  }
  public String getIndustryCode() {
    return industryCode;
  }
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }
  public String getbStatus() {
    return bStatus;
  }
  public void setbStatus(String bStatus) {
    this.bStatus = bStatus;
  }
  public String getcStatus() {
    return cStatus;
  }
  public void setcStatus(String cStatus) {
    this.cStatus = cStatus;
  }
  public String getrStatus() {
    return rStatus;
  }
  public void setrStatus(String rStatus) {
    this.rStatus = rStatus;
  }
  public String getAgentRel() {
    return agentRel;
  }
  public void setAgentRel(String agentRel) {
    this.agentRel = agentRel;
  }
  public String getProfessionCode1() {
    return professionCode1;
  }
  public void setProfessionCode1(String professionCode1) {
    this.professionCode1 = professionCode1;
  }
  public String getProfessionCode2() {
    return professionCode2;
  }
  public void setProfessionCode2(String professionCode2) {
    this.professionCode2 = professionCode2;
  }
  public String getProfessionName1() {
    return professionName1;
  }
  public void setProfessionName1(String professionName1) {
    this.professionName1 = professionName1;
  }
  public String getProfessionName2() {
    return professionName2;
  }
  public void setProfessionName2(String professionName2) {
    this.professionName2 = professionName2;
  }
  public QueryLogBean getqBean() {
    return qBean;
  }
  public void setqBean(QueryLogBean qBean) {
    this.qBean = qBean;
  }
  
  public String getPercentage() {
    return percentage;
  }
  public void setPercentage(String percentage) {
    this.percentage = percentage;
  }
  public String getOrderNo() {
    return orderNo;
  }
  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }
  public String getTrxDateDown() {
    return trxDateDown;
  }
  public void setTrxDateDown(String trxDateDown) {
    this.trxDateDown = trxDateDown;
  }
  
}
