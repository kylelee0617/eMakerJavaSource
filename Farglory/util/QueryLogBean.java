package Farglory.util;

import org.apache.commons.lang.StringUtils;

import jcx.db.talk;

/**
 * 計算風險值 實質受益人物件
 * 
 * @author B04391
 *
 */

public class QueryLogBean {
  private String qid = ""; // 0
  private String reason = "";
  private String projectId = "";
  private String nationalId = "";
  private String queryType = "";
  private String ntCode = ""; // 5
  private String name = "";
  private String queryId = "";
  private String birthday = "";
  private String sex = "";
  private String jobType = ""; // 10
  private String city = "";
  private String town = "";
  private String address = "";
  private String bStatus = "";
  private String cStatus = ""; // 15
  private String rStatus = "";
  private String status = "";
  private String contents = "";
  private String pcontents = "";
  private String empNo = ""; // 20
  private String ip4 = "";
  private String createDate = "";
  private String createTime = "";
  private String updateDate = "";
  private String updateTime = ""; // 25
  private String ntCode2 = "";
  private String engName = "";
  private String engNo = "";
  private String queryId3 = "";

  public String getNtCode2() {
    return ntCode2;
  }

  public void setNtCode2(String ntCode2) {
    this.ntCode2 = ntCode2;
  }

  public String getEngName() {
    return engName;
  }

  public void setEngName(String engName) {
    this.engName = engName;
  }

  public String getEngNo() {
    return engNo;
  }

  public void setEngNo(String engNo) {
    this.engNo = engNo;
  }

  public String getQueryId3() {
    return queryId3;
  }

  public void setQueryId3(String queryId3) {
    this.queryId3 = queryId3;
  }

  public String getQid() {
    return qid;
  }

  public void setQid(String qid) {
    this.qid = qid;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public String getNationalId() {
    return nationalId;
  }

  public void setNationalId(String nationalId) {
    this.nationalId = nationalId;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

  public String getNtCode() {
    return ntCode;
  }

  public void setNtCode(String ntCode) {
    this.ntCode = ntCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getQueryId() {
    return queryId;
  }

  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public String getPcontents() {
    return pcontents;
  }

  public void setPcontents(String pcontents) {
    this.pcontents = pcontents;
  }

  public String getEmpNo() {
    return empNo;
  }

  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

  public String getIp4() {
    return ip4;
  }

  public void setIp4(String ip4) {
    this.ip4 = ip4;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * 以ID查對應姓名 英文OR中文
   * 
   * @param chkId
   * @return
   */
  public String getRealName(String chkId) {
    String rsName = "";
    if (StringUtils.isBlank(chkId)) return rsName;
    if (StringUtils.equals(chkId, this.queryId)) {
      rsName = this.name;
    } else if (StringUtils.equals(chkId, this.engNo)) {
      rsName = this.engName;
    }
    return rsName;
  }
  
  /**
   * 以ID查另一個姓名
   * 
   * @param chkId
   * @return
   */
  public String getOtherName(String chkId) {
    String rsName = "";
    if (StringUtils.isBlank(chkId)) return rsName;
    if (StringUtils.equals(chkId, this.queryId)) {
      rsName = this.engName;
    } else if (StringUtils.equals(chkId, this.engNo)) {
      rsName = this.name;
    }
    return rsName;
  }

  /**
   * 以name 查出對應ID
   * 
   * @param chkName
   * @return
   */
  public String getRealId(String chkName) {
    String rsId = "";
    if (StringUtils.isBlank(chkName)) return rsId;
    if (StringUtils.equals(chkName, this.name)) {
      rsId = this.queryId;
    } else if (StringUtils.equals(chkName, this.engName)) {
      rsId = this.engNo;
    }
    return rsId;
  }
  
  /**
   * 以name查另一個ID
   * 
   * @param chkName
   * @return
   */
  public String getOtherId(String chkName) {
    String rsId = "";
    if (StringUtils.isBlank(chkName)) return rsId;
    if (StringUtils.equals(chkName, this.name)) {
      rsId = this.engNo;
    } else if (StringUtils.equals(chkName, this.engName)) {
      rsId = this.queryId;
    }
    return rsId;
  }

  /**
   * 以ID查對應國家
   * 
   * @param chkId
   * @return
   */
  public String getRealNtCode(String chkId) {
    String rsName = "";
    if (StringUtils.isBlank(chkId)) return rsName;
    if (StringUtils.equals(chkId, this.queryId)) {
      rsName = this.ntCode;
    } else if (StringUtils.equals(chkId, this.engNo)) {
      rsName = this.ntCode2;
    }
    return rsName;
  }
  
  /**
   * 以ID查另一國家
   * 
   * @param chkId
   * @return
   */
  public String getOtherNtCode(String chkId) {
    String rsName = "";
    if (StringUtils.isBlank(chkId)) return rsName;
    if (StringUtils.equals(chkId, this.queryId)) {
      rsName = this.ntCode2;
    } else if (StringUtils.equals(chkId, this.engNo)) {
      rsName = this.ntCode;
    }
    return rsName;
  }
  
  /**
   * 國別是否有命中其一
   * 
   * @param chkNtCode
   * @return
   */
  public boolean isShotNt(String chkNtCode) {
    boolean rs = false;
    if (StringUtils.isBlank(chkNtCode)) return rs;
    if(StringUtils.equals(chkNtCode, this.ntCode) || StringUtils.equals(chkNtCode, this.ntCode2)) {
      rs = true;
    }
    return rs;
  }

}
