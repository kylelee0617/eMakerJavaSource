package Farglory.util;

import java.util.List;

/**
 * �p�⭷�I�ȵ��G�^��
 * 
 * @author B04391
 *
 */

public class RiskCheckRS {
  private String rsMsg = "";    //���I�ȵ��G�T��
  private List sendMailList = null;   //�H�oEMAIL��T
  
  public String getRsMsg() {
    return rsMsg;
  }
  public void setRsMsg(String rsMsg) {
    this.rsMsg = rsMsg;
  }
  public List getSendMailList() {
    return sendMailList;
  }
  public void setSendMailList(List sendMailList) {
    this.sendMailList = sendMailList;
  }

}
